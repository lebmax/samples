package sobes;

/**
 * Given an integer array nums, handle multiple queries of the following type:
 * Calculate the sum of the elements of nums between indices left and right
 * inclusive where left <= right.
 */
public class RangeSumQuery {
	
//	private final int[] nums;
//
//	public RangeSumQuery(int[] nums) {
//		this.nums = nums;
//	}
//
//	public int sumRange(int left, int right) {
//		if (nums.length == 0) {
//			return 0;
//		}
//		if (left > right) {
//			throw new IllegalArgumentException();
//		}
//		int sum = nums[left];
//		for (int i = left + 1; i <= right && i < nums.length; i++) {
//			sum += nums[i];
//		}
//		return sum;
//	}
	
	private final int[] prefix;
	
	/* --- Prefix variant ---- */
	public RangeSumQuery(int[] nums) {
		// prefix[0] = 0, prefix.length = nums.length + 1
		prefix = new int[nums.length + 1];
		for (int i = 0; i < nums.length; i++) {
			prefix[i + 1] = prefix[i] + nums[i];
		}
	}
	
	public int sumRange(int left, int right) {
		return prefix[right + 1] - prefix[left];
	}
	
	public static void main(String... arg) {
		RangeSumQuery numArray = new RangeSumQuery(new int[]{-2, 0, 3, -5, 2, -1});
		System.out.println(numArray.sumRange(0, 2)); // return (-2) + 0 + 3 = 1
		System.out.println(numArray.sumRange(2, 5)); // return 3 + (-5) + 2 + (-1) = -1
		System.out.println(numArray.sumRange(0, 5)); // return (-2) + 0 + 3 + (-5) + 2 + (-1) = -3
	}
}
