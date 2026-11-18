package vk;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenBucketRateLimiterTest {
    @Test
    void acceptsInitialBurstAndRefillsAtTheExactSecond() {
        AtomicLong clock = new AtomicLong();
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(clock::get);
        exhaustBucket(limiter, "A");

        clock.set(MILLISECONDS.toNanos(900));
        assertFalse(limiter.tryAcquire("A"));

        clock.set(SECONDS.toNanos(1) - 1);
        assertFalse(limiter.tryAcquire("A"));

        clock.set(SECONDS.toNanos(1));
        assertTrue(limiter.tryAcquire("A"));
        assertFalse(limiter.tryAcquire("A"));
    }

    @Test
    void capsRefillAndKeepsTheFractionalSecond() {
        AtomicLong clock = new AtomicLong();
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(clock::get);
        exhaustBucket(limiter, "A");

        clock.set(MILLISECONDS.toNanos(10_700));
        exhaustBucket(limiter, "A");

        clock.set(SECONDS.toNanos(11));
        assertTrue(limiter.tryAcquire("A"));
        assertFalse(limiter.tryAcquire("A"));
    }

    @Test
    void clientsHaveIndependentBucketsAndRefillTimes() {
        AtomicLong clock = new AtomicLong();
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(clock::get);
        exhaustBucket(limiter, "A");

        clock.set(MILLISECONDS.toNanos(500));
        exhaustBucket(limiter, "B");

        clock.set(SECONDS.toNanos(1));
        assertTrue(limiter.tryAcquire("A"));
        assertFalse(limiter.tryAcquire("B"));

        clock.set(MILLISECONDS.toNanos(1_500));
        assertFalse(limiter.tryAcquire("A"));
        assertTrue(limiter.tryAcquire("B"));
    }

    @Test
    void onlyOneConcurrentRequestGetsTheRefilledToken() throws Exception {
        AtomicLong clock = new AtomicLong();
        TokenBucketRateLimiter limiter = new TokenBucketRateLimiter(clock::get);
        exhaustBucket(limiter, "A");
        clock.set(SECONDS.toNanos(1));
        AtomicInteger granted = new AtomicInteger();

        ConcurrentTestSupport.runConcurrently(2, 1, () -> {
            if (limiter.tryAcquire("A")) {
                granted.incrementAndGet();
            }
        });

        assertEquals(1, granted.get());
        assertFalse(limiter.tryAcquire("A"));
    }

    private static void exhaustBucket(TokenBucketRateLimiter limiter, String clientId) {
        assertTrue(limiter.tryAcquire(clientId));
        assertTrue(limiter.tryAcquire(clientId));
        assertTrue(limiter.tryAcquire(clientId));
        assertFalse(limiter.tryAcquire(clientId));
    }
}
