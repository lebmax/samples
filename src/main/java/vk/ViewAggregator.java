package vk;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ViewAggregator {
    private static final long WINDOW_SECONDS = 60;

    private record View(String pageId, long second) {}

    private final Deque<View> views = new ArrayDeque<>();
    private final Map<String, Long> countsByPage = new HashMap<>();
    private long lastSecond;

    public synchronized void recordView(String pageId, long second) {
        Objects.requireNonNull(pageId);
        advanceWindow(second);
        views.addLast(new View(pageId, second));
        countsByPage.merge(pageId, 1L, Long::sum);
    }

    public synchronized long countViews(String pageId, long nowSecond) {
        Objects.requireNonNull(pageId);
        advanceWindow(nowSecond);
        return countsByPage.getOrDefault(pageId, 0L);
    }

    private void advanceWindow(long nowSecond) {
        if (nowSecond < lastSecond) {
            throw new IllegalArgumentException(
                    "Time must be non-negative and non-decreasing");
        }
        lastSecond = nowSecond;

        long cutoff = nowSecond - WINDOW_SECONDS;
        while (!views.isEmpty() && views.getFirst().second() <= cutoff) {
            View expired = views.removeFirst();
            long remaining = countsByPage.get(expired.pageId()) - 1;
            if (remaining == 0) {
                countsByPage.remove(expired.pageId());
            } else {
                countsByPage.put(expired.pageId(), remaining);
            }
        }
    }
}
