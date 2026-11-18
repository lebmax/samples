package vk;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public final class ViewCounters {
    private final ConcurrentHashMap<String, AtomicLong> countsByPage =
            new ConcurrentHashMap<>();

    public void recordView(String pageId) {
        AtomicLong count = countsByPage.computeIfAbsent(pageId, id -> new AtomicLong());
        count.incrementAndGet();
    }

    public long countViews(String pageId) {
        AtomicLong count = countsByPage.get(pageId);
        return count == null ? 0L : count.get();
    }
}
