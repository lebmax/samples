package vk;

import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Function;
import java.util.function.LongSupplier;

public final class BoundedTtlCache<K, V> {
    private record Entry<V>(V value, long loadedAtNanos) {}

    private final Object lock = new Object();
    private final LinkedHashMap<K, Entry<V>> ready = new LinkedHashMap<>();
    private final Map<K, CompletableFuture<V>> inFlight = new HashMap<>();
    private final int maxEntries;
    private final long ttlNanos;
    private final Function<? super K, ? extends V> loader;
    private final Executor loadExecutor;
    private final LongSupplier nanoTime;

    public BoundedTtlCache(int maxEntries, Duration ttl,
                           Function<? super K, ? extends V> loader, Executor loadExecutor) {
        this(maxEntries, ttl, loader, loadExecutor, System::nanoTime);
    }

    BoundedTtlCache(int maxEntries, Duration ttl,
                    Function<? super K, ? extends V> loader, Executor loadExecutor,
                    LongSupplier nanoTime) {
        if (maxEntries <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.maxEntries = maxEntries;
        this.ttlNanos = Objects.requireNonNull(ttl).toNanos();
        if (ttlNanos <= 0) {
            throw new IllegalArgumentException("TTL must be positive");
        }
        this.loader = Objects.requireNonNull(loader);
        this.loadExecutor = Objects.requireNonNull(loadExecutor);
        this.nanoTime = Objects.requireNonNull(nanoTime);
    }

    public CompletionStage<V> get(K key) {
        Objects.requireNonNull(key);

        CompletableFuture<V> pending;
        synchronized (lock) {
            removeExpired(nanoTime.getAsLong());

            Entry<V> entry = ready.get(key);
            if (entry != null) {
                return CompletableFuture.completedFuture(entry.value());
            }

            pending = inFlight.get(key);
            if (pending != null) {
                return pending.thenApply(Function.identity());
            }

            if (inFlight.size() == maxEntries) {
                return CompletableFuture.failedFuture(
                        new RejectedExecutionException("Cache capacity is occupied by active loads"));
            }
            if (ready.size() + inFlight.size() == maxEntries) {
                ready.pollFirstEntry();
            }

            pending = new CompletableFuture<>();
            inFlight.put(key, pending);
        }

        startLoad(key, pending);
        return pending.thenApply(Function.identity());
    }

    public int cleanUp() {
        synchronized (lock) {
            return removeExpired(nanoTime.getAsLong());
        }
    }

    private int removeExpired(long nowNanos) {
        int removed = 0;
        // Ready entries have the same TTL and are ordered by publication time.
        var entries = ready.values().iterator();
        while (entries.hasNext()) {
            Entry<V> entry = entries.next();
            if (nowNanos - entry.loadedAtNanos() < ttlNanos) {
                break;
            }
            entries.remove();
            removed++;
        }
        return removed;
    }

    private void startLoad(K key, CompletableFuture<V> pending) {
        try {
            loadExecutor.execute(() -> load(key, pending));
        } catch (RejectedExecutionException failure) {
            fail(key, pending, failure);
        }
    }

    private void load(K key, CompletableFuture<V> pending) {
        V value;
        try {
            value = Objects.requireNonNull(loader.apply(key), "Loader returned null");
            synchronized (lock) {
                Entry<V> entry = new Entry<>(value, nanoTime.getAsLong());
                inFlight.remove(key);
                ready.put(key, entry);
            }
        } catch (RuntimeException | Error failure) {
            fail(key, pending, failure);
            return;
        }

        pending.complete(value);
    }

    private void fail(K key, CompletableFuture<V> pending, Throwable failure) {
        synchronized (lock) {
            inFlight.remove(key, pending);
        }
        pending.completeExceptionally(failure);
    }
}
