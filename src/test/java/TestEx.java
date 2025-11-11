import org.junit.jupiter.api.Test;
import sobes.BinaryTreeCodec;
import sobes.MyBlockingQueue;
import sobes.DiningPhilosophers;
import sobes.LRUCache;
import sobes.ListNode;
import sobes.MedianFinder;
import sobes.PrefixTree;
import sobes.RateLimiter;
import sobes.Solution;
import sobes.ThreadSafeSingleton;
import sobes.TreeNode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class TestEx {
	
	// Longest Substring Without Repeating Characters
	@Test
	public void testLengthOfLongestSubstring() {
		assertEquals(3, Solution.lengthOfLongestSubstring("abcabcbb"));
		assertEquals(1, Solution.lengthOfLongestSubstring("bbbbb"));
		assertEquals(3, Solution.lengthOfLongestSubstring("pwwkew"));
		assertEquals(0, Solution.lengthOfLongestSubstring(""));
		assertEquals(1, Solution.lengthOfLongestSubstring("a"));
	}
	
	// Trapping Rain Water
	@Test
	public void testTrap() {
		assertEquals(6, Solution.trap(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1}));
		assertEquals(9, Solution.trap(new int[]{4, 2, 0, 3, 2, 5}));
		assertEquals(0, Solution.trap(new int[]{1, 2, 3}));
		assertEquals(0, Solution.trap(new int[]{}));
	}
	
	// Merge Intervals
	@Test
	public void testMerge() {
		int[][] intervals1 = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
		int[][] expected1 = {{1, 6}, {8, 10}, {15, 18}};
		assertArrayEquals(expected1, Solution.merge(intervals1));
		
		int[][] intervals2 = {{1, 4}, {4, 5}};
		int[][] expected2 = {{1, 5}};
		assertArrayEquals(expected2, Solution.merge(intervals2));
	}
	
	// Detect Cycle in Linked List
	@Test
	public void testHasCycle() {
		ListNode head = new ListNode(1);
		head.next = new ListNode(2);
		head.next.next = new ListNode(3);
		head.next.next.next = head; // cycle
		
		assertTrue(Solution.hasCycle(head));
		
		head.next.next.next = null; // no cycle
		assertFalse(Solution.hasCycle(head));
	}
	
	// Merge Two Sorted Lists
	@Test
	public void testMergeTwoLists() {
		ListNode l1 = new ListNode(1);
		l1.next = new ListNode(2);
		l1.next.next = new ListNode(4);
		
		ListNode l2 = new ListNode(1);
		l2.next = new ListNode(3);
		l2.next.next = new ListNode(4);
		
		ListNode merged = Solution.mergeTwoLists(l1, l2);
		int[] expected = {1, 1, 2, 3, 4, 4};
		int i = 0;
		while (merged != null) {
			assertEquals(expected[i++], merged.val);
			merged = merged.next;
		}
	}
	
	// Validate Binary Search Tree
	@Test
	public void testIsValidBST() {
		TreeNode validBST = new TreeNode(2);
		validBST.left = new TreeNode(1);
		validBST.right = new TreeNode(3);
		assertTrue(Solution.isValidBST(validBST));
		
		TreeNode invalidBST = new TreeNode(5);
		invalidBST.left = new TreeNode(1);
		invalidBST.right = new TreeNode(4);
		invalidBST.right.left = new TreeNode(3);
		invalidBST.right.right = new TreeNode(6);
		assertFalse(Solution.isValidBST(invalidBST));
	}
	
	// Same Tree
	@Test
	public void testIsSameTree() {
		Solution sol = new Solution();
		
		//    1
		//   / \
		//  2   3
		TreeNode p1 = new TreeNode(1, new TreeNode(2), new TreeNode(3));
		TreeNode q1 = new TreeNode(1, new TreeNode(2), new TreeNode(3));
		assertTrue(sol.isSameTree(p1, q1));
		assertTrue(sol.isSameTreeStack(p1, q1));
		
		// p
		//    1
		//   / \
		//  2   3
		// q
		//    1
		//   / \
		//  2   4
		TreeNode p2 = new TreeNode(1, new TreeNode(2), new TreeNode(3));
		TreeNode q2 = new TreeNode(1, new TreeNode(2), new TreeNode(4));
		assertFalse(sol.isSameTree(p2, q2));
		assertFalse(sol.isSameTreeStack(p2, q2));
		
		// p
		//    1
		//   /
		//  2
		// q
		//    1
		//     \
		//      2
		TreeNode p3 = new TreeNode(1, new TreeNode(2), null);
		TreeNode q3 = new TreeNode(1, null, new TreeNode(2));
		assertFalse(sol.isSameTree(p3, q3));
		assertFalse(sol.isSameTreeStack(p3, q3));
		
		//        5
		//       / \
		//      3   8
		//     / \   \
		//    2   4   9
		TreeNode p4 = new TreeNode(5,
								  new TreeNode(3,
											   new TreeNode(2),
											   new TreeNode(4)
								  ),
								  new TreeNode(8,
											   null,
											   new TreeNode(9)
								  )
		);
		
		TreeNode q4 = new TreeNode(5,
								  new TreeNode(3,
											   new TreeNode(2),
											   new TreeNode(4)
								  ),
								  new TreeNode(8,
											   null,
											   new TreeNode(9)
								  )
		);
		assertTrue(sol.isSameTree(p4, q4));
		assertTrue(sol.isSameTreeStack(p4, q4));
		
		// p
		//    1
		//   /
		//  1
		// q
		//    1
		//     \
		//      1
		TreeNode p5 = new TreeNode(1, new TreeNode(1), null);
		TreeNode q5 = new TreeNode(1, null, new TreeNode(1));
		assertFalse(sol.isSameTree(p5, q5));
		assertFalse(sol.isSameTreeStack(p5, q5));
	}
	
	// Course Schedule
	@Test
	public void testCanFinish() {
		assertTrue(Solution.canFinish(2, new int[][]{{1, 0}}));
		assertFalse(Solution.canFinish(2, new int[][]{{1, 0}, {0, 1}}));
		assertTrue(Solution.canFinish(3, new int[][]{{0, 1}, {1, 2}}));
	}
	
	// Climbing Stairs
	@Test
	public void testClimbStairs() {
		assertEquals(2, Solution.climbStairs(2));
		assertEquals(3, Solution.climbStairs(3));
		assertEquals(8, Solution.climbStairs(5));
	}
	
	// Coin Change
	@Test
	public void testCoinChange() {
		assertEquals(3, Solution.coinChange(new int[]{1, 2, 5}, 11));
		assertEquals(-1, Solution.coinChange(new int[]{2}, 3));
		assertEquals(0, Solution.coinChange(new int[]{1}, 0));
	}
	
	// Longest Increasing Subsequence
	@Test
	public void testLengthOfLIS() {
		assertEquals(4, Solution.lengthOfLIS(new int[]{10, 9, 2, 5, 3, 7, 101, 18}));
		assertEquals(4, Solution.lengthOfLIS(new int[]{0, 1, 0, 3, 2, 3}));
		assertEquals(1, Solution.lengthOfLIS(new int[]{7, 7, 7, 7, 7, 7, 7}));
	}
	
	// Producer-Consumer Problem
	@Test
	public void testBlockingQueue() throws InterruptedException {
		MyBlockingQueue<Integer> queue = new MyBlockingQueue<>(2);
		try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
			// Producer
			executor.submit(() -> {
				try {
					queue.put(1);
					queue.put(2);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			});
			
			// Consumer
			Future<Integer> future = executor.submit(() -> {
				try {
					return queue.take();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return -1;
				}
			});
			
			try {
				assertEquals(1, future.get(1, TimeUnit.SECONDS));
			} catch (Exception e) {
				fail("Test failed with exception: " + e.getMessage());
			}
			
			executor.shutdown();
		}
	}
	
	// Dining Philosophers
	@Test
	public void testDiningPhilosophers() {
		DiningPhilosophers dp = new DiningPhilosophers();
		assertDoesNotThrow(() -> {
			dp.wantsToEat(0, () -> {
			}, () -> {
			}, () -> {
			}, () -> {
			}, () -> {
			});
		});
	}
	
	// Thread-Safe Singleton
	@Test
	public void testSingleton() {
		ThreadSafeSingleton s1 = ThreadSafeSingleton.getInstance();
		ThreadSafeSingleton s2 = ThreadSafeSingleton.getInstance();
		assertSame(s1, s2);
	}
	
	// LRU Cache
	@Test
	public void testLRUCache() {
		LRUCache cache = new LRUCache(2);
		cache.put(1, 1);
		cache.put(2, 2);
		assertEquals(1, cache.get(1));
		cache.put(3, 3);
		assertEquals(-1, cache.get(2)); // evicted
		cache.put(4, 4);
		assertEquals(-1, cache.get(1)); // evicted
		assertEquals(3, cache.get(3));
		assertEquals(4, cache.get(4));
	}
	
	// Trie
	@Test
	public void testTrie() {
		PrefixTree trie = new PrefixTree();
		trie.insert("apple");
		assertTrue(trie.search("apple"));
		assertFalse(trie.search("app"));
		assertTrue(trie.startsWith("app"));
		trie.insert("app");
		assertTrue(trie.search("app"));
	}
	
	// Serialize and Deserialize Binary Tree
	@Test
	public void testSerializeDeserialize() {
		TreeNode root = new TreeNode(1);
		root.left = new TreeNode(2);
		root.right = new TreeNode(3);
		root.right.left = new TreeNode(4);
		root.right.right = new TreeNode(5);
		
		BinaryTreeCodec codec = new BinaryTreeCodec();
		String serialized = codec.serialize(root);
		TreeNode deserialized = codec.deserialize(serialized);
		
		assertEquals(root.val, deserialized.val);
		assertEquals(root.left.val, deserialized.left.val);
		assertEquals(root.right.val, deserialized.right.val);
		assertEquals(root.right.left.val, deserialized.right.left.val);
		assertEquals(root.right.right.val, deserialized.right.right.val);
	}
	
	// Find Median from Data Stream
	@Test
	public void testMedianFinder() {
		MedianFinder mf = new MedianFinder();
		mf.addNum(1);
		mf.addNum(2);
		assertEquals(1.5, mf.findMedian());
		mf.addNum(3);
		assertEquals(2.0, mf.findMedian());
	}
	
	// Kth Largest Element in an Array
	@Test
	public void testFindKthLargest() {
		assertEquals(5, Solution.findKthLargest(new int[]{3, 2, 1, 5, 6, 4}, 2));
		assertEquals(4, Solution.findKthLargest(new int[]{3, 2, 3, 1, 2, 4, 5, 5, 6}, 4));
	}
	
	@Test
	void testWildcardMatching() {
		assertTrue(Solution.isWildcardMatch("abracadabra", "a*da*"));
		assertTrue(Solution.isWildcardMatch("", "*"));
		assertFalse(Solution.isWildcardMatch("abc", "a?b"));
		assertTrue(Solution.isWildcardMatch("hello", "h?llo"));
		assertFalse(Solution.isWildcardMatch("abc", "a*cb"));
	}
	
	@Test
	void testCompression() {
		assertEquals("A4B3C2D1", Solution.compressRLE("AAAABBBCCD"));
		assertEquals("a2b1", Solution.compressRLE("aab"));
		assertEquals("abc", Solution.compressRLE("abc")); // Не должно сжимать
		assertEquals("", Solution.compressRLE(""));
		assertEquals("Z5", Solution.compressRLE("ZZZZZ"));
	}
	
	@Test
	void testFindCheapestPrice() {
		int[][] flights = {{0, 1, 100}, {1, 2, 100}, {0, 2, 500}};
		assertEquals(200, Solution.findCheapestPrice(3, flights, 0, 2, 1));
		assertEquals(500, Solution.findCheapestPrice(3, flights, 0, 2, 0));
		assertEquals(-1, Solution.findCheapestPrice(3, flights, 2, 0, 1));
	}
	
	@Test
	void testRateLimiter() throws InterruptedException {
		RateLimiter limiter = new RateLimiter(2, 1000); // 2 запроса в секунду
		
		assertTrue(limiter.allowRequest());
		assertTrue(limiter.allowRequest());
		assertFalse(limiter.allowRequest()); // Лимит
		
		Thread.sleep(1000); // Ждем 1 секунду
		
		assertTrue(limiter.allowRequest()); // Снова доступно
	}
}
