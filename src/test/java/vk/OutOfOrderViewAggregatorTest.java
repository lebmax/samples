package vk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OutOfOrderViewAggregatorTest {
    @Test
    void expiresOutOfOrderEventsByEventTime() {
        OutOfOrderViewAggregator aggregator = new OutOfOrderViewAggregator();

        assertTrue(aggregator.recordView("page", 105L, 105L));
        assertTrue(aggregator.recordView("page", 100L, 105L));

        assertEquals(2L, aggregator.countViews("page", 159L));
        assertEquals(1L, aggregator.countViews("page", 160L));
    }

    @Test
    void rejectsTheSixtySecondBoundaryAndAdvancesTimeForLateEvents() {
        OutOfOrderViewAggregator aggregator = new OutOfOrderViewAggregator();

        assertTrue(aggregator.recordView("page", 100L, 159L));
        assertFalse(aggregator.recordView("page", 100L, 160L));

        assertThrows(IllegalArgumentException.class,
                () -> aggregator.countViews("page", 159L));
        assertEquals(0L, aggregator.countViews("page", 160L));
    }

    @Test
    void rejectsInvalidEventAndNowValuesWithoutMutatingState() {
        OutOfOrderViewAggregator aggregator = new OutOfOrderViewAggregator();
        assertTrue(aggregator.recordView("page", 5L, 5L));

        assertThrows(IllegalArgumentException.class,
                () -> aggregator.recordView("page", -1L, 5L));
        assertThrows(IllegalArgumentException.class,
                () -> aggregator.recordView("page", 7L, 6L));
        assertThrows(IllegalArgumentException.class,
                () -> aggregator.countViews("page", -1L));

        assertThrows(IllegalArgumentException.class,
                () -> aggregator.countViews("page", 4L));
        assertEquals(1L, aggregator.countViews("page", 5L));
    }

    @Test
    void rejectsRegressingNowAcrossBothMethodsWithoutMovingTimeBackwards() {
        OutOfOrderViewAggregator aggregator = new OutOfOrderViewAggregator();
        assertTrue(aggregator.recordView("page", 10L, 10L));

        assertThrows(IllegalArgumentException.class,
                () -> aggregator.countViews("page", 9L));
        assertThrows(IllegalArgumentException.class,
                () -> aggregator.recordView("page", 9L, 9L));

        assertTrue(aggregator.recordView("page", 10L, 10L));
        assertEquals(2L, aggregator.countViews("page", 10L));
    }

    @Test
    void countsDuplicatesAndKeepsPagesIndependent() {
        OutOfOrderViewAggregator aggregator = new OutOfOrderViewAggregator();

        assertTrue(aggregator.recordView("first", 10L, 10L));
        assertTrue(aggregator.recordView("first", 10L, 10L));
        assertTrue(aggregator.recordView("second", 11L, 11L));

        assertEquals(2L, aggregator.countViews("first", 11L));
        assertEquals(1L, aggregator.countViews("second", 11L));
        assertEquals(0L, aggregator.countViews("missing", 11L));
    }

    @Test
    void countsConcurrentEventsAtTheSameCurrentTime() throws Exception {
        OutOfOrderViewAggregator aggregator = new OutOfOrderViewAggregator();
        int workers = 8;
        int viewsPerWorker = 500;

        ConcurrentTestSupport.runConcurrently(workers, viewsPerWorker,
                () -> aggregator.recordView("popular", 42L, 42L));

        assertEquals((long) workers * viewsPerWorker,
                aggregator.countViews("popular", 42L));
    }
}
