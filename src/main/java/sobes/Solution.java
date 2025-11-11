package sobes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class Solution {
	
	// Longest Substring Without Repeating Characters
	public static int lengthOfLongestSubstring(String s) {
		Set<Character> set = new HashSet<>();
		int left = 0, max = 0;
		for (int right = 0; right < s.length(); right++) {
			while (set.contains(s.charAt(right))) {
				set.remove(s.charAt(left++));
			}
			set.add(s.charAt(right));
			max = Math.max(max, right - left + 1);
		}
		return max;
	}
	
	// Trapping Rain Water
	// Учитывая n неотрицательных целых чисел,
	// представляющих карту высот, где ширина каждой полосы равна 1,
	// вычислите, сколько воды она может задержать после дождя.
	public static int trap(int[] height) {
		if (height.length == 0) return 0;
		int left = 0, right = height.length - 1;
		int leftMax = 0, rightMax = 0, water = 0;
		
		while (left < right) {
			if (height[left] < height[right]) {
				if (height[left] >= leftMax) leftMax = height[left];
				else water += leftMax - height[left];
				left++;
			} else {
				if (height[right] >= rightMax) rightMax = height[right];
				else water += rightMax - height[right];
				right--;
			}
		}
		return water;
	}
	
	// Merge Intervals
	public static int[][] merge(int[][] intervals) {
		Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
		List<int[]> merged = new ArrayList<>();
		for (int[] interval : intervals) {
			if (merged.isEmpty() || merged.getLast()[1] < interval[0]) {
				merged.add(interval);
			} else {
				merged.getLast()[1] = Math.max(merged.getLast()[1], interval[1]);
			}
		}
		return merged.toArray(new int[merged.size()][]);
	}
	
	// Detect Cycle in a Linked List
	public static boolean hasCycle(ListNode head) {
		if (head == null || head.next == null) return false;
		ListNode slow = head, fast = head;
		while (fast != null && fast.next != null) {
			slow = slow.next;
			fast = fast.next.next;
			if (slow == fast) return true;
		}
		return false;
	}
	
	// Merge Two Sorted Lists
	public static ListNode mergeTwoLists(ListNode list1, ListNode list2) {
		ListNode dummy = new ListNode(0);
		ListNode cur = dummy;
		while (list1 != null && list2 != null) {
			if (list1.val <= list2.val) {
				cur.next = list1;
				list1 = list1.next;
			} else {
				cur.next = list2;
				list2 = list2.next;
			}
			cur = cur.next;
		}
		cur.next = (list1 != null) ? list1 : list2;
		return dummy.next;
	}
	
	// Validate Binary Search Tree
	public static boolean isValidBST(TreeNode root) {
		return isValid(root, Long.MIN_VALUE, Long.MAX_VALUE);
	}
	
	private static boolean isValid(TreeNode node, long min, long max) {
		if (node == null) return true;
		if (node.val <= min || node.val >= max) return false;
		return isValid(node.left, min, node.val) && isValid(node.right, node.val, max);
	}
	
	// Даны корни двух бинарных деревьев p и q.
	// Нужно проверить совпадают ли деревья.
	// Два дерева считаются одинаковыми если они структурно идентичны
	// и значения соответствующих узлов равны
	public boolean isSameTree(TreeNode p, TreeNode q) {
		// Если оба узла null — они идентичны
		if (p == null && q == null) {
			return true;
		}
		
		// Если только один из узлов null — не идентичны
		if (p == null || q == null) {
			return false;
		}
		
		// Если значения разные — не идентичны
		if (p.val != q.val) {
			return false;
		}
		
		// Рекурсивно проверяем левые и правые поддеревья
		return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
	}
	
	public boolean isSameTreeStack(TreeNode p, TreeNode q) {
		Stack<TreeNode> stack = new Stack<>();
		stack.push(p);
		stack.push(q);
		
		while (!stack.isEmpty()) {
			// Извлекаем пару узлов для сравнения
			TreeNode nodeQ = stack.pop();
			TreeNode nodeP = stack.pop();
			
			// Оба узла null — продолжаем
			if (nodeP == null && nodeQ == null) {
				continue;
			}
			
			// Один null, другой нет — деревья разные
			if (nodeP == null || nodeQ == null) {
				return false;
			}
			
			// Значения не совпадают — деревья разные
			if (nodeP.val != nodeQ.val) {
				return false;
			}
			
			// Добавляем в стек пары потомков для дальнейшей проверки
			// Порядок важен: сначала левые, потом правые (или наоборот, но одинаково)
			stack.push(nodeP.left);
			stack.push(nodeQ.left);
			stack.push(nodeP.right);
			stack.push(nodeQ.right);
		}
		
		return true;
	}
	
	// Course Schedule
	// Существует общее количество курсов numCourses,
	// которые вы должны пройти, с метками от 0 до numCourses - 1.
	// Вам предоставляется массив предварительных условий,
	// где предварительные условия[i] = [ai, bi] указывают на то,
	// что вы должны сначала пройти курс bi, если хотите пройти курс ai
	public static boolean canFinish(int numCourses, int[][] prerequisites) {
		List<List<Integer>> graph = new ArrayList<>();
		for (int i = 0; i < numCourses; i++) graph.add(new ArrayList<>());
		int[] indegree = new int[numCourses];
		
		for (int[] pre : prerequisites) {
			graph.get(pre[1]).add(pre[0]);
			indegree[pre[0]]++;
		}
		
		Queue<Integer> queue = new LinkedList<>();
		for (int i = 0; i < numCourses; i++) {
			if (indegree[i] == 0) queue.offer(i);
		}
		
		int count = 0;
		while (!queue.isEmpty()) {
			int cur = queue.poll();
			count++;
			for (int neighbor : graph.get(cur)) {
				if (--indegree[neighbor] == 0) queue.offer(neighbor);
			}
		}
		return count == numCourses;
	}
	
	// Climbing Stairs
	// Вы поднимаетесь по лестнице. Чтобы достичь вершины, требуется n ступенек.
	// Каждый раз вы можете подняться на 1 или 2 ступеньки.
	// Сколькими различными способами вы можете подняться на вершину?
	public static int climbStairs(int n) {
		if (n <= 2) return n;
		int prev2 = 1, prev1 = 2;
		for (int i = 3; i <= n; i++) {
			int cur = prev1 + prev2;
			prev2 = prev1;
			prev1 = cur;
		}
		return prev1;
	}
	
	// Coin Change
	// Вам выдается целочисленный массив coins,
	// представляющий монеты разного достоинства,
	// и целочисленная сумма, представляющая общую сумму денег.
	// Верните наименьшее количество монет, необходимое для получения этой суммы.
	// Если эта сумма денег не может быть получена с помощью какой-либо комбинации монет,
	// верните -1.
	// Вы можете предположить, что у вас есть бесконечное количество монет каждого вида.
	public static int coinChange(int[] coins, int amount) {
		int[] dp = new int[amount + 1];
		Arrays.fill(dp, amount + 1);
		dp[0] = 0;
		for (int i = 1; i <= amount; i++) {
			for (int coin : coins) {
				if (coin <= i) {
					dp[i] = Math.min(dp[i], dp[i - coin] + 1);
				}
			}
		}
		return dp[amount] > amount ? -1 : dp[amount];
	}
	
	// Longest Increasing Subsequence (LIS)
	public static int lengthOfLIS(int[] nums) {
		List<Integer> sub = new ArrayList<>();
		for (int num : nums) {
			int left = Collections.binarySearch(sub, num);
			if (left < 0) left = -(left + 1);
			if (left == sub.size()) sub.add(num);
			else sub.set(left, num);
		}
		return sub.size();
	}
	
	// Kth Largest Element in an Array
	public static Integer findKthLargest(int[] nums, int k) {
		PriorityQueue<Integer> heap = new PriorityQueue<>();
		for (int num : nums) {
			heap.offer(num);
			if (heap.size() > k) heap.poll();
		}
		return heap.peek();
	}
	
	// Поиск подстроки с маской (wildcard matching)
	// Сложность: O(n*m), память: O(n*m)
	public static boolean isWildcardMatch(String s, String p) {
		int sLen = s.length(), pLen = p.length();
		boolean[][] dp = new boolean[sLen + 1][pLen + 1];
		dp[0][0] = true;
		
		// Обработка шаблонов типа "***a"
		for (int j = 1; j <= pLen; j++) {
			if (p.charAt(j-1) == '*') {
				dp[0][j] = dp[0][j-1];
			}
		}
		
		for (int i = 1; i <= sLen; i++) {
			for (int j = 1; j <= pLen; j++) {
				char pc = p.charAt(j-1);
				if (pc == '*') {
					dp[i][j] = dp[i-1][j] || dp[i][j-1];
				} else if (pc == '?' || pc == s.charAt(i-1)) {
					dp[i][j] = dp[i-1][j-1];
				}
			}
		}
		return dp[sLen][pLen];
	}
	
	// Сжатие строки (RLE)
	// Сложность: O(n)
	public static String compressRLE(String s) {
		if (s == null || s.isEmpty()) return s;
		
		StringBuilder sb = new StringBuilder();
		int count = 1;
		char prev = s.charAt(0);
		
		for (int i = 1; i < s.length(); i++) {
			if (s.charAt(i) == prev) {
				count++;
			} else {
				sb.append(prev).append(count);
				prev = s.charAt(i);
				count = 1;
			}
		}
		sb.append(prev).append(count);
		
		return sb.length() < s.length() ? sb.toString() : s;
	}
	
	// Поиск минимального пути с ограничением пересадок (Dijkstra с модификацией)
	// Сложность: O(E + V*k*log(V*k))
	public static int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
		// Строим граф: from -> {to, price}
		Map<Integer, List<int[]>> graph = new HashMap<>();
		for (int[] f : flights) {
			graph.computeIfAbsent(f[0], key -> new ArrayList<>()).add(new int[]{f[1], f[2]});
		}
		
		// Минимальная стоимость для каждой вершины и числа остановок
		int[][] costs = new int[n][k+2];
		for (int[] row : costs) Arrays.fill(row, Integer.MAX_VALUE);
		costs[src][0] = 0;
		
		// Очередь: [город, остановки, текущая стоимость]
		PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[2] - b[2]);
		pq.offer(new int[]{src, 0, 0});
		
		while (!pq.isEmpty()) {
			int[] curr = pq.poll();
			int city = curr[0], stops = curr[1], cost = curr[2];
			
			if (city == dst) return cost;
			if (stops > k) continue;
			
			if (graph.containsKey(city)) {
				for (int[] neighbor : graph.get(city)) {
					int newCost = cost + neighbor[1];
					if (newCost < costs[neighbor[0]][stops+1]) {
						costs[neighbor[0]][stops+1] = newCost;
						pq.offer(new int[]{neighbor[0], stops+1, newCost});
					}
				}
			}
		}
		
		return -1;
	}
}
