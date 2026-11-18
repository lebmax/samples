package vk;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.LongSupplier;

public final class TokenBucketRateLimiter {
    private static final int CAPACITY = 3;
    private static final long REFILL_INTERVAL_NANOS = TimeUnit.SECONDS.toNanos(1);

    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final LongSupplier nanoTime;

    public TokenBucketRateLimiter() {
        this(System::nanoTime);
    }

    TokenBucketRateLimiter(LongSupplier nanoTime) {
        this.nanoTime = Objects.requireNonNull(nanoTime);
    }

    public boolean tryAcquire(String clientId) {
        Bucket bucket = buckets.computeIfAbsent(clientId, ignored -> new Bucket());
        return bucket.tryAcquire(nanoTime);
    }

    private static final class Bucket {
        private int tokens = CAPACITY;
        private long lastRefillNanos;
        private boolean initialized;

        private synchronized boolean tryAcquire(LongSupplier nanoTime) {
            long now = nanoTime.getAsLong();
            if (!initialized) {
                lastRefillNanos = now;
                initialized = true;
            }

            long intervals = (now - lastRefillNanos) / REFILL_INTERVAL_NANOS;
            if (intervals > 0) {
                tokens = (int) Math.min(CAPACITY, tokens + intervals);
                lastRefillNanos += intervals * REFILL_INTERVAL_NANOS;
            }

            if (tokens == 0) {
                return false;
            }

            tokens--;
            return true;
        }
    }
}
