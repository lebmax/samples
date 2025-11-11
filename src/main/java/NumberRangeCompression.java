import java.util.Arrays;

/**
 * Дан массив целых чисел, повторяющихся элементов в массиве нет.
 * Нужно преобразовать в строку, сворачивая соседние по числовому ряду
 * числа в диапазоны.
 * <p>
 * Input: [1,4,5,2,3,9,8,11,0,13]
 * Output: "0-5,8-9,11,13"
 * <p>
 * Input: [3,1,5]
 * Output: "1,3,5"
 * <p>
 * Input: [1,3,5]
 * Output: "1,3,5"
 */
public class NumberRangeCompression {
	
	public static void main(String[] args) {
		System.out.println(formatRanges(new int[]{1,4,5,2,3,9,8,11,0,13})); // 0-5,8-9,11,13
		System.out.println(formatRanges(new int[]{3,1,5})); // 1,3,5
		System.out.println(formatRanges(new int[]{1,3,5})); // 1,3,5
		System.out.println(formatRanges(new int[]{1,2})); // 1-2
	}
	
	public static String formatRanges(int[] nums) {
		Arrays.sort(nums);
		
		StringBuilder result = new StringBuilder();
		int start = nums[0];
		
		for (int i = 1; i < nums.length; i++) {
			if (nums[i] != nums[i-1] + 1) {
				appendRange(result, start, nums[i-1]);
				start = nums[i];
			}
		}
		appendRange(result, start, nums[nums.length-1]);
		
		return result.toString();
	}
	
	private static void appendRange(StringBuilder sb, int start, int end) {
		if (!sb.isEmpty()) {
			sb.append(',');
		}
		if (start == end) {
			sb.append(start);
		} else {
			sb.append(start).append('-').append(end);
		}
	}
}
