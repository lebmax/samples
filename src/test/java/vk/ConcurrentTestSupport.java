package vk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

final class ConcurrentTestSupport {
    private ConcurrentTestSupport() {
    }

    static void runConcurrently(int workerCount, int operationsPerWorker,
                                        Runnable action) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(workerCount);
        CountDownLatch ready = new CountDownLatch(workerCount);
        CountDownLatch start = new CountDownLatch(1);
        List<Future<?>> futures = new ArrayList<>();

        try {
            for (int worker = 0; worker < workerCount; worker++) {
                futures.add(executor.submit(() -> {
                    ready.countDown();
                    if (!start.await(5, TimeUnit.SECONDS)) {
                        throw new IllegalStateException("Timed out waiting for start signal");
                    }
                    for (int operation = 0; operation < operationsPerWorker; operation++) {
                        action.run();
                    }
                    return null;
                }));
            }

            assertTrue(ready.await(5, TimeUnit.SECONDS), "Workers did not become ready");
            start.countDown();
            for (Future<?> future : futures) {
                future.get(10, TimeUnit.SECONDS);
            }
        } finally {
            start.countDown();
            executor.shutdownNow();
            assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS),
                    "Workers did not terminate");
        }
    }
}
