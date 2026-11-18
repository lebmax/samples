package vk;

import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.extension.ExtendWith;
import org.pastalab.fray.junit.junit5.FrayTestExtension;
import org.pastalab.fray.junit.junit5.annotations.FrayTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(FrayTestExtension.class)
public class LiveCodingFrayChecks {
    @FrayTest(iterations = 25)
    public void concurrentExpirationAndRecordingKeepBothViews() throws InterruptedException {
        ViewAggregator aggregator = new ViewAggregator();
        aggregator.recordView("A", 40);

        Thread first = new Thread(() -> aggregator.recordView("A", 100));
        Thread second = new Thread(() -> aggregator.recordView("A", 100));
        Thread reader = new Thread(() -> aggregator.countViews("A", 100));
        first.start();
        second.start();
        reader.start();
        first.join();
        second.join();
        reader.join();

        assertEquals(2L, aggregator.countViews("A", 100));
    }

    @FrayTest(iterations = 25)
    public void concurrentFirstWritesToHotKeyBothCount() throws InterruptedException {
        ViewCounters counters = new ViewCounters();

        Thread first = new Thread(() -> counters.recordView("hot"));
        Thread second = new Thread(() -> counters.recordView("hot"));
        first.start();
        second.start();
        first.join();
        second.join();

        assertEquals(2L, counters.countViews("hot"));
    }
    @FrayTest(iterations = 25)
    public void concurrentCacheMissesShareOneLoad() throws InterruptedException {
        var queuedLoads = new ConcurrentLinkedQueue<Runnable>();
        var cache = new TtlCache<String, String>(Duration.ofSeconds(1),
                key -> "value-" + key, queuedLoads::add, () -> 0L);
        var firstResult = new AtomicReference<CompletionStage<String>>();
        var secondResult = new AtomicReference<CompletionStage<String>>();

        Thread first = new Thread(() -> firstResult.set(cache.get("A")));
        Thread second = new Thread(() -> secondResult.set(cache.get("A")));
        first.start();
        second.start();
        first.join();
        second.join();

        assertEquals(1, queuedLoads.size());
        queuedLoads.remove().run();
        assertEquals("value-A", firstResult.get().toCompletableFuture().join());
        assertEquals("value-A", secondResult.get().toCompletableFuture().join());
    }

    @FrayTest(iterations = 25)
    public void onlyOneRequestAcquiresLastRefilledToken() throws InterruptedException {
        AtomicLong clock = new AtomicLong();
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(clock::get);
        assertTrue(limiter.tryAcquire("A"));
        assertTrue(limiter.tryAcquire("A"));
        assertTrue(limiter.tryAcquire("A"));

        clock.set(TimeUnit.SECONDS.toNanos(1));
        AtomicInteger granted = new AtomicInteger();
        Runnable request = () -> {
            if (limiter.tryAcquire("A")) {
                granted.incrementAndGet();
            }
        };

        Thread first = new Thread(request);
        Thread second = new Thread(request);
        first.start();
        second.start();
        first.join();
        second.join();

        assertEquals(1, granted.get());
        assertFalse(limiter.tryAcquire("A"));
    }

    @FrayTest(iterations = 25)
    public void concurrentOutOfOrderViewsExpireByEventTime() throws InterruptedException {
        OutOfOrderViewAggregator aggregator = new OutOfOrderViewAggregator();
        assertTrue(aggregator.recordView("A", 105, 105));

        Thread first = new Thread(() -> aggregator.recordView("A", 100, 105));
        Thread second = new Thread(() -> aggregator.recordView("A", 101, 105));
        first.start();
        second.start();
        first.join();
        second.join();

        assertEquals(3L, aggregator.countViews("A", 159));
        assertEquals(2L, aggregator.countViews("A", 160));
        assertEquals(1L, aggregator.countViews("A", 161));
        assertEquals(0L, aggregator.countViews("A", 165));
    }

    @FrayTest(iterations = 25)
    public void cleanupAndPublicationPreserveSharedLoad() throws InterruptedException {
        var queuedLoads = new ConcurrentLinkedQueue<Runnable>();
        var clock = new AtomicLong();
        var cache = new BoundedTtlCache<String, String>(1, Duration.ofSeconds(1),
                key -> "value-" + key, queuedLoads::add, clock::get);
        var first = cache.get("A");
        assertEquals(1, queuedLoads.size());

        clock.set(TimeUnit.SECONDS.toNanos(10));
        var second = new AtomicReference<CompletionStage<String>>();
        Thread reader = new Thread(() -> second.set(cache.get("A")));
        Thread cleaner = new Thread(cache::cleanUp);
        Thread publisher = new Thread(queuedLoads.remove());
        reader.start();
        cleaner.start();
        publisher.start();
        reader.join();
        cleaner.join();
        publisher.join();

        assertEquals(0, queuedLoads.size());
        assertEquals("value-A", first.toCompletableFuture().join());
        assertEquals("value-A", second.get().toCompletableFuture().join());
        clock.set(TimeUnit.SECONDS.toNanos(11));
        assertEquals(1, cache.cleanUp());
    }
}
