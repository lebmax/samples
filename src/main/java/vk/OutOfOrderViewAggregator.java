package vk;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;

public final class OutOfOrderViewAggregator {
    private static final long WINDOW_SECONDS = 60;

    private record View(String pageId, long eventSecond) {}

    private final PriorityQueue<View> views =
            new PriorityQueue<>(Comparator.comparingLong(View::eventSecond));
    private final Map<String, Long> countsByPage = new HashMap<>();
    private long lastNowSecond;

    public synchronized boolean recordView(String pageId, long eventSecond, long nowSecond) {
        Objects.requireNonNull(pageId);
        if (eventSecond < 0 || eventSecond > nowSecond) {
            throw new IllegalArgumentException("Event time must be between zero and now");
        }

        long cutoff = advanceWindow(nowSecond);
        if (eventSecond <= cutoff) {
            return false;
        }

        views.add(new View(pageId, eventSecond));
        countsByPage.merge(pageId, 1L, Long::sum);
        return true;
    }

    public synchronized long countViews(String pageId, long nowSecond) {
        Objects.requireNonNull(pageId);
        advanceWindow(nowSecond);
        return countsByPage.getOrDefault(pageId, 0L);
    }

    private long advanceWindow(long nowSecond) {
        if (nowSecond < lastNowSecond) {
            throw new IllegalArgumentException("Now must be non-negative and non-decreasing");
        }
        lastNowSecond = nowSecond;

        long cutoff = nowSecond - WINDOW_SECONDS;
        while (!views.isEmpty() && views.element().eventSecond() <= cutoff) {
            View expired = views.remove();
            long remaining = countsByPage.get(expired.pageId()) - 1;
            if (remaining == 0) {
                countsByPage.remove(expired.pageId());
            } else {
                countsByPage.put(expired.pageId(), remaining);
            }
        }
        return cutoff;
    }
}
