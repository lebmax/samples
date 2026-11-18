package vk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Test;

class BoundedTtlCacheTest {
    private static final long GET_TIMEOUT_SECONDS = 1;

    @Test
    void evictsOldestReadyInsertionWithoutRefreshingItsFifoPosition() throws Exception {
        AtomicLong nanoTime = new AtomicLong();
        AtomicInteger loadCount = new AtomicInteger();
        ManualExecutor executor = new ManualExecutor();
        BoundedTtlCache<String, String> cache = new BoundedTtlCache<>(
                2,
                Duration.ofNanos(10),
                key -> key + "-" + loadCount.incrementAndGet(),
                executor,
                nanoTime::get);

        CompletionStage<String> firstA = cache.get("A");
        executor.runNext();
        assertEquals("A-1", value(firstA));

        CompletionStage<String> firstB = cache.get("B");
        executor.runNext();
        assertEquals("B-2", value(firstB));

        assertEquals("A-1", value(cache.get("A")));
        assertEquals(0, executor.pendingTaskCount());

        CompletionStage<String> firstC = cache.get("C");
        executor.runNext();
        assertEquals("C-3", value(firstC));

        CompletionStage<String> retainedB = cache.get("B");
        assertEquals(0, executor.pendingTaskCount());
        assertEquals("B-2", value(retainedB));
        assertEquals(3, loadCount.get());

        CompletionStage<String> reloadedA = cache.get("A");
        assertEquals(1, executor.pendingTaskCount());
        executor.runNext();
        assertEquals("A-4", value(reloadedA));
    }

    @Test
    void countsInflightLoadsTowardCapacityAndSharesLoadsForTheSameKey() throws Exception {
        AtomicLong nanoTime = new AtomicLong();
        AtomicInteger loadCount = new AtomicInteger();
        ManualExecutor executor = new ManualExecutor();
        BoundedTtlCache<String, String> cache = new BoundedTtlCache<>(
                2,
                Duration.ofNanos(10),
                key -> key + "-" + loadCount.incrementAndGet(),
                executor,
                nanoTime::get);

        CompletionStage<String> firstA = cache.get("A");
        CompletionStage<String> followerA = cache.get("A");
        CompletionStage<String> firstB = cache.get("B");
        assertEquals(2, executor.pendingTaskCount());
        assertEquals(0, loadCount.get());
        assertFalse(firstA.toCompletableFuture().isDone());

        CompletionStage<String> rejectedC = cache.get("C");
        assertEquals(2, executor.pendingTaskCount());
        ExecutionException rejected = assertThrows(
                ExecutionException.class, () -> value(rejectedC));
        assertEquals(RejectedExecutionException.class, rejected.getCause().getClass());

        executor.runNext();
        executor.runNext();
        assertEquals("A-1", value(firstA));
        assertEquals("A-1", value(followerA));
        assertEquals("B-2", value(firstB));
        assertEquals(2, loadCount.get());

        CompletionStage<String> retriedC = cache.get("C");
        assertEquals(1, executor.pendingTaskCount());
        executor.runNext();
        assertEquals("C-3", value(retriedC));
    }

    @Test
    void cleanupKeepsPendingLoadsAndExpiresReadyValuesFromCompletionTime() throws Exception {
        AtomicLong nanoTime = new AtomicLong();
        AtomicInteger loadCount = new AtomicInteger();
        ManualExecutor executor = new ManualExecutor();
        BoundedTtlCache<String, String> cache = new BoundedTtlCache<>(
                1,
                Duration.ofNanos(10),
                key -> key + "-" + loadCount.incrementAndGet(),
                executor,
                nanoTime::get);

        CompletionStage<String> pending = cache.get("key");
        nanoTime.set(1_000);
        assertEquals(0, cache.cleanUp());

        CompletionStage<String> pendingFollower = cache.get("key");
        assertEquals(1, executor.pendingTaskCount());
        executor.runNext();
        assertEquals("key-1", value(pending));
        assertEquals("key-1", value(pendingFollower));

        nanoTime.set(1_009);
        assertEquals("key-1", value(cache.get("key")));
        assertEquals(0, cache.cleanUp());
        assertEquals(0, executor.pendingTaskCount());

        nanoTime.set(1_010);
        assertEquals(1, cache.cleanUp());
        assertEquals(0, cache.cleanUp());

        CompletionStage<String> refreshed = cache.get("key");
        assertEquals(1, executor.pendingTaskCount());
        executor.runNext();
        assertEquals("key-2", value(refreshed));
    }

    @Test
    void cleansUpInCompletionOrderWhenLoadsFinishOutOfOrder() throws Exception {
        AtomicLong nanoTime = new AtomicLong();
        AtomicInteger loadCount = new AtomicInteger();
        ManualExecutor executor = new ManualExecutor();
        BoundedTtlCache<String, String> cache = new BoundedTtlCache<>(
                2, Duration.ofNanos(10), key -> key + "-" + loadCount.incrementAndGet(),
                executor, nanoTime::get);

        CompletionStage<String> firstA = cache.get("A");
        CompletionStage<String> firstB = cache.get("B");

        nanoTime.set(1);
        executor.runLast();
        assertEquals("B-1", value(firstB));

        nanoTime.set(5);
        executor.runNext();
        assertEquals("A-2", value(firstA));

        nanoTime.set(11);
        assertEquals(1, cache.cleanUp());
        assertEquals("A-2", value(cache.get("A")));

        CompletionStage<String> reloadedB = cache.get("B");
        executor.runNext();
        assertEquals("B-3", value(reloadedB));
    }

    @Test
    void loaderFailureDoesNotPreventRetry() throws Exception {
        AtomicLong nanoTime = new AtomicLong();
        AtomicInteger loadCount = new AtomicInteger();
        ManualExecutor executor = new ManualExecutor();
        IllegalStateException loaderFailure = new IllegalStateException("temporary failure");
        BoundedTtlCache<String, String> cache = new BoundedTtlCache<>(
                1,
                Duration.ofNanos(10),
                key -> {
                    if (loadCount.incrementAndGet() == 1) {
                        throw loaderFailure;
                    }
                    return "recovered";
                },
                executor,
                nanoTime::get);

        CompletionStage<String> failedLoad = cache.get("key");
        executor.runNext();
        ExecutionException failure = assertThrows(
                ExecutionException.class, () -> value(failedLoad));
        assertSame(loaderFailure, failure.getCause());
        assertEquals(0, executor.pendingTaskCount());

        CompletionStage<String> retry = cache.get("key");
        assertEquals(1, executor.pendingTaskCount());
        executor.runNext();
        assertEquals("recovered", value(retry));
        assertEquals(2, loadCount.get());
    }

    private static <V> V value(CompletionStage<V> stage)
            throws InterruptedException, ExecutionException, TimeoutException {
        return stage.toCompletableFuture().get(GET_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    private static final class ManualExecutor implements Executor {
        private final Deque<Runnable> tasks = new ArrayDeque<>();

        @Override
        public void execute(Runnable command) {
            tasks.add(command);
        }

        private int pendingTaskCount() {
            return tasks.size();
        }

        private void runNext() {
            tasks.removeFirst().run();
        }

        private void runLast() {
            tasks.removeLast().run();
        }
    }
}
