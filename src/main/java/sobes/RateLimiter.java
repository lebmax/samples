package sobes;

import java.util.Deque;
import java.util.LinkedList;

public class RateLimiter {
	
	private final int limit;
	private final long interval;
	private final Deque<Long> requests;
	
	public RateLimiter(int limit, long intervalMs) {
		this.limit = limit;
		this.interval = intervalMs;
		this.requests = new LinkedList<>();
	}
	
	public synchronized boolean allowRequest() {
		long now = System.currentTimeMillis();
		// Удаляем старые запросы
		while (!requests.isEmpty() && now - requests.peekFirst() > interval) {
			requests.pollFirst();
		}
		
		if (requests.size() < limit) {
			requests.addLast(now);
			return true;
		}
		return false;
	}
}
