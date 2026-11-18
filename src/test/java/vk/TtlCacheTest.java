package vk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Test;

class TtlCacheTest {
    private static final long WAIT_SECONDS = 5;

    @Test
    void coalescesColdLoadsAndIsolatesCancellation() throws Exception {
        AtomicLong clock = new AtomicLong();
        AtomicInteger loadCount = new AtomicInteger();
        CountDownLatch loadStarted = new CountDownLatch(1);
        CountDownLatch releaseLoad = new CountDownLatch(1);

        try (ExecutorService loaderExecutor = Executors.newSingleThreadExecutor()) {
            TtlCache<String, String> cache = new TtlCache<>(Duration.ofSeconds(60), key -> {
                int attempt = loadCount.incrementAndGet();
                loadStarted.countDown();
                await(releaseLoad);
                return "value-" + attempt;
            }, loaderExecutor, clock::get);

            try {
                CompletionStage<String> first = cache.get("key");
                await(loadStarted);

                List<CompletionStage<String>> followers = new ArrayList<>();
                for (int index = 0; index < 12; index++) {
                    followers.add(cache.get("key"));
                }

                CompletionStage<String> cancelled = cache.get("key");
                assertTrue(cancelled.toCompletableFuture().cancel(false));
                assertFalse(first.toCompletableFuture().isDone());
                assertEquals(1, loadCount.get());

                releaseLoad.countDown();

                assertEquals("value-1", result(first));
                for (CompletionStage<String> follower : followers) {
                    assertEquals("value-1", result(follower));
                }
                assertEquals("value-1", result(cache.get("key")));
                assertEquals(1, loadCount.get());
            } finally {
                releaseLoad.countDown();
            }
        }
    }

    @Test
    void startsTtlAtLoadCompletionAndCoalescesExpiredRefreshes() throws Exception {
        AtomicLong clock = new AtomicLong();
        AtomicInteger loadCount = new AtomicInteger();
        CountDownLatch firstLoadStarted = new CountDownLatch(1);
        CountDownLatch releaseFirstLoad = new CountDownLatch(1);
        CountDownLatch refreshStarted = new CountDownLatch(1);
        CountDownLatch releaseRefresh = new CountDownLatch(1);
        CountDownLatch bothCallersSawExpiredEntry = new CountDownLatch(2);
        CountDownLatch letCallersRefresh = new CountDownLatch(1);
        AtomicInteger expirationChecks = new AtomicInteger();

        try (ExecutorService loaderExecutor = Executors.newSingleThreadExecutor();
             ExecutorService callers = Executors.newFixedThreadPool(2)) {
            TtlCache<String, String> cache = new TtlCache<>(Duration.ofNanos(10), key -> {
                int attempt = loadCount.incrementAndGet();
                if (attempt == 1) {
                    firstLoadStarted.countDown();
                    await(releaseFirstLoad);
                } else if (attempt == 2) {
                    refreshStarted.countDown();
                    await(releaseRefresh);
                }
                return "value-" + attempt;
            }, loaderExecutor, () -> {
                long now = clock.get();
                if (now == 110 && expirationChecks.incrementAndGet() <= 2) {
                    bothCallersSawExpiredEntry.countDown();
                    await(letCallersRefresh);
                }
                return now;
            });

            try {
                CompletionStage<String> first = cache.get("key");
                await(firstLoadStarted);

                clock.set(100);
                releaseFirstLoad.countDown();
                assertEquals("value-1", result(first));

                clock.set(109);
                assertEquals("value-1", result(cache.get("key")));
                assertEquals(1, loadCount.get());

                clock.set(110);
                Future<CompletionStage<String>> firstCaller =
                        callers.submit(() -> cache.get("key"));
                Future<CompletionStage<String>> secondCaller =
                        callers.submit(() -> cache.get("key"));

                await(bothCallersSawExpiredEntry);
                letCallersRefresh.countDown();
                await(refreshStarted);
                CompletionStage<String> firstRefresh =
                        firstCaller.get(WAIT_SECONDS, TimeUnit.SECONDS);
                CompletionStage<String> secondRefresh =
                        secondCaller.get(WAIT_SECONDS, TimeUnit.SECONDS);

                assertFalse(firstRefresh.toCompletableFuture().isDone());
                assertFalse(secondRefresh.toCompletableFuture().isDone());
                releaseRefresh.countDown();

                assertEquals("value-2", result(firstRefresh));
                assertEquals("value-2", result(secondRefresh));
                assertEquals(2, loadCount.get());
            } finally {
                letCallersRefresh.countDown();
                releaseFirstLoad.countDown();
                releaseRefresh.countDown();
            }
        }
    }

    @Test
    void loadsDifferentKeysWhileOneLoadIsBlocked() throws Exception {
        AtomicLong clock = new AtomicLong();
        AtomicInteger loadsA = new AtomicInteger();
        CountDownLatch loadAStarted = new CountDownLatch(1);
        CountDownLatch releaseLoadA = new CountDownLatch(1);

        try (ExecutorService loaderExecutor = Executors.newFixedThreadPool(2)) {
            TtlCache<String, String> cache = new TtlCache<>(Duration.ofSeconds(60), key -> {
                if (key.equals("A")) {
                    loadsA.incrementAndGet();
                    loadAStarted.countDown();
                    await(releaseLoadA);
                    return "value-A";
                }
                return "value-B";
            }, loaderExecutor, clock::get);

            try {
                CompletionStage<String> loadA = cache.get("A");
                await(loadAStarted);

                assertEquals("value-B", result(cache.get("B")));
                assertFalse(loadA.toCompletableFuture().isDone());

                releaseLoadA.countDown();
                assertEquals("value-A", result(loadA));
                assertEquals(1, loadsA.get());
            } finally {
                releaseLoadA.countDown();
            }
        }
    }

    @Test
    void sharesLoaderFailureWithFollowersAndAllowsRetry() throws Exception {
        AtomicLong clock = new AtomicLong();
        AtomicInteger loadCount = new AtomicInteger();
        IllegalStateException unavailable = new IllegalStateException("database unavailable");
        CountDownLatch releaseFailure = new CountDownLatch(1);

        try (ExecutorService loaderExecutor = Executors.newSingleThreadExecutor()) {
            TtlCache<String, String> cache = new TtlCache<>(Duration.ofSeconds(60), key -> {
                if (loadCount.incrementAndGet() == 1) {
                    await(releaseFailure);
                    throw unavailable;
                }
                return "available";
            }, loaderExecutor, clock::get);

            try {
                CompletionStage<String> first = cache.get("key");
                CompletionStage<String> follower = cache.get("key");
                releaseFailure.countDown();

                assertSame(unavailable, assertThrows(
                        ExecutionException.class, () -> result(first)).getCause());
                assertSame(unavailable, assertThrows(
                        ExecutionException.class, () -> result(follower)).getCause());
            } finally {
                releaseFailure.countDown();
            }

            assertEquals("available", result(cache.get("key")));
            assertEquals("available", result(cache.get("key")));
            assertEquals(2, loadCount.get());
        }
    }

    @Test
    void retriesAfterExecutorRejection() throws Exception {
        AtomicLong clock = new AtomicLong();
        AtomicInteger submissions = new AtomicInteger();
        RejectedExecutionException rejected = new RejectedExecutionException("queue full");

        try (ExecutorService loaderExecutor = Executors.newSingleThreadExecutor()) {
            TtlCache<String, String> cache = new TtlCache<>(Duration.ofSeconds(60),
                    key -> "value", task -> {
                        if (submissions.incrementAndGet() == 1) {
                            throw rejected;
                        }
                        loaderExecutor.execute(task);
                    }, clock::get);

            ExecutionException failure = assertThrows(
                    ExecutionException.class, () -> result(cache.get("key")));
            assertSame(rejected, failure.getCause());

            assertEquals("value", result(cache.get("key")));
            assertEquals(2, submissions.get());
        }
    }

    private static <V> V result(CompletionStage<V> stage)
            throws InterruptedException, ExecutionException, TimeoutException {
        return stage.toCompletableFuture().get(WAIT_SECONDS, TimeUnit.SECONDS);
    }

    private static void await(CountDownLatch gate) {
        try {
            if (!gate.await(WAIT_SECONDS, TimeUnit.SECONDS)) {
                throw new AssertionError("timed out waiting for test gate");
            }
        } catch (InterruptedException failure) {
            Thread.currentThread().interrupt();
            throw new AssertionError("interrupted while waiting for test gate", failure);
        }
    }
}
