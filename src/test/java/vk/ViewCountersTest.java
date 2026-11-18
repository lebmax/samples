package vk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ViewCountersTest {
    @Test
    void startsAtZeroAndCountsDuplicateViewsForIndependentKeys() {
        ViewCounters counters = new ViewCounters();

        assertEquals(0L, counters.countViews("first"));
        counters.recordView("first");
        counters.recordView("first");
        counters.recordView("second");

        assertEquals(2L, counters.countViews("first"));
        assertEquals(1L, counters.countViews("second"));
        assertEquals(0L, counters.countViews("missing"));
    }

    @Test
    void countsConcurrentUpdatesToOneHotKey() throws Exception {
        ViewCounters counters = new ViewCounters();
        int workers = 8;
        int incrementsPerWorker = 1_000;

        ConcurrentTestSupport.runConcurrently(workers, incrementsPerWorker,
                () -> counters.recordView("hot"));

        assertEquals((long) workers * incrementsPerWorker, counters.countViews("hot"));
    }
}
