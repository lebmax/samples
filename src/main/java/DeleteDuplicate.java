import java.util.Arrays;

/**
 * Given an integer array nums sorted in non-decreasing order, remove the duplicates in-place such
 * that each unique element appears only once. The relative order of the elements should be kept the same.
 * Then return the number of unique elements in nums.
 * <p>
 * Consider the number of unique elements of nums to be k, to get accepted, you need to do the following things:
 * <p>
 * Change the array nums such that the first k elements of nums contain the unique elements in the order
 * they were present in nums initially. The remaining elements of nums are not important as well as the size of nums.
 * Return k.
 * <p>
 * Example 1:
 * <p>
 * Input: nums = [1,1,2]
 * Output: 2, nums = [1,2,_]
 * Explanation: Your function should return k = 2, with the first two elements of nums being 1 and 2 respectively.
 * It does not matter what you leave beyond the returned k (hence they are underscores).
 * Example 2:
 * <p>
 * Input: nums = [0,0,1,1,1,2,2,3,3,4]
 * Output: 5, nums = [0,1,2,3,4,_,_,_,_,_]
 * Explanation: Your function should return k = 5, with the first five elements of nums being 0, 1, 2, 3, and 4 respectively.
 * It does not matter what you leave beyond the returned k (hence they are underscores).
 */
public class DeleteDuplicate {
	
	public static void main(String[] args) throws Exception {
		System.out.println(removeDuplicates(new int[]{1, 1, 2}));
		System.out.println(removeDuplicates(new int[]{0, 0, 1, 1, 1, 2, 2, 3, 3, 4}));
		
		System.out.println(removeDuplicates1(new int[]{1, 1, 2}));
		System.out.println(removeDuplicates1(new int[]{0, 0, 1, 1, 1, 2, 2, 3, 3, 4}));
	}
	
	public static int removeDuplicates(int[] nums) {
		int last = -1;
		int res = 0;
		for (int num : nums) {
			if (last != num) {
				last = num;
				res++;
			}
		}
		return res;
	}
	
	public static int removeDuplicates1(int[] arr) {
		int i = 0;
		for (int j = 1; j < arr.length; j++) {
			if (arr[i] != arr[j]) {
				i++;
				arr[i] = arr[j];
			}
		}
		System.out.println(Arrays.toString(arr));
		return i == 0 ? i : i + 1;
	}
}
