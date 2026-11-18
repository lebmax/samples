package vk;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Function;
import java.util.function.LongSupplier;

public final class TtlCache<K, V> {
    private record Entry<V>(V value, long loadedAtNanos) {}

    private final ConcurrentHashMap<K, CompletableFuture<Entry<V>>> entries =
            new ConcurrentHashMap<>();
    private final long ttlNanos;
    private final Function<? super K, ? extends V> loader;
    private final Executor loadExecutor;
    private final LongSupplier nanoTime;

    public TtlCache(Duration ttl, Function<? super K, ? extends V> loader,
                    Executor loadExecutor) {
        this(ttl, loader, loadExecutor, System::nanoTime);
    }

    TtlCache(Duration ttl, Function<? super K, ? extends V> loader,
             Executor loadExecutor, LongSupplier nanoTime) {
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

        while (true) {
            var current = entries.get(key);
            if (current != null) {
                if (!current.isDone() || current.isCompletedExceptionally()) {
                    return current.thenApply(Entry::value);
                }

                Entry<V> entry = current.getNow(null);
                if (nanoTime.getAsLong() - entry.loadedAtNanos() < ttlNanos) {
                    return CompletableFuture.completedFuture(entry.value());
                }
            }

            var pending = new CompletableFuture<Entry<V>>();
            boolean installed = current == null
                    ? entries.putIfAbsent(key, pending) == null
                    : entries.replace(key, current, pending);

            if (installed) {
                startLoad(key, pending);
                return pending.thenApply(Entry::value);
            }
        }
    }

    private void startLoad(K key, CompletableFuture<Entry<V>> pending) {
        try {
            loadExecutor.execute(() -> {
                try {
                    V value = Objects.requireNonNull(loader.apply(key), "Loader returned null");
                    pending.complete(new Entry<>(value, nanoTime.getAsLong()));
                } catch (RuntimeException | Error failure) {
                    fail(key, pending, failure);
                }
            });
        } catch (RejectedExecutionException failure) {
            fail(key, pending, failure);
        }
    }

    private void fail(K key, CompletableFuture<Entry<V>> pending, Throwable failure) {
        entries.remove(key, pending);
        pending.completeExceptionally(failure);
    }
}
