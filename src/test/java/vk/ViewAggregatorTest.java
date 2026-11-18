package vk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ViewAggregatorTest {
    @Test
    void expiresViewsAtTheSixtySecondBoundary() {
        ViewAggregator aggregator = new ViewAggregator();
        aggregator.recordView("page", 100L);

        assertEquals(1L, aggregator.countViews("page", 159L));
        assertEquals(0L, aggregator.countViews("page", 160L));
    }

    @Test
    void countsDuplicateViewsAndKeepsPageKeysIndependent() {
        ViewAggregator aggregator = new ViewAggregator();
        aggregator.recordView("first", 10L);
        aggregator.recordView("first", 10L);
        aggregator.recordView("second", 11L);

        assertEquals(2L, aggregator.countViews("first", 11L));
        assertEquals(1L, aggregator.countViews("second", 11L));
        assertEquals(0L, aggregator.countViews("missing", 11L));
    }

    @Test
    void acceptsZeroAndRejectsTimeRegression() {
        ViewAggregator aggregator = new ViewAggregator();
        aggregator.recordView("page", 0L);

        assertEquals(1L, aggregator.countViews("page", 0L));
        assertEquals(1L, aggregator.countViews("page", 1L));
        assertThrows(IllegalArgumentException.class,
                () -> aggregator.countViews("page", 0L));
        assertThrows(IllegalArgumentException.class,
                () -> aggregator.recordView("page", 0L));

        aggregator.recordView("page", 1L);
        assertEquals(2L, aggregator.countViews("page", 1L));
    }

    @Test
    void aggregatesConcurrentViewsRecordedInTheSameSecond() throws Exception {
        ViewAggregator aggregator = new ViewAggregator();
        int workers = 8;
        int viewsPerWorker = 500;

        ConcurrentTestSupport.runConcurrently(workers, viewsPerWorker,
                () -> aggregator.recordView("popular", 42L));

        assertEquals((long) workers * viewsPerWorker,
                aggregator.countViews("popular", 42L));
    }
}
