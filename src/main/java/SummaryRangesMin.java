import java.util.ArrayList;
import java.util.List;

/**
 * Дан отсортированный в порядке возрастания массив nums,
 * состоящий из уникальных целых чисел.
 * Известно, что диапазон [a, b] представляет собой
 * множество всех целых чисел от a до b включительно.
 * Вам необходимо вернуть наименьший отсортированный список диапазонов,
 * в котором каждый элемент массива находится в одном из диапазонов.
 * И нет такого числа, которое есть в диапазоне,
 * но отсутствует в изначальном массиве nums.
 * <p>
 * Пример:
 * Input: nums = [0,2,3,4,6,8,9]
 * Output: ["0","2->4","6","8->9"]
 */
public class SummaryRangesMin {
	
	public static void main(String[] args) throws Exception {
		System.out.println(summaryRanges(new int[]{0, 2, 3, 4, 6, 8, 9}));
	}
	
	private static List<String> summaryRanges(int[] nums) {
		List<String> result = new ArrayList<>();
		if (nums == null || nums.length == 0) {
			return result;
		}
		
		int start = nums[0];
		int n = nums.length;
		
		for (int i = 1; i < n; i++) {
			// Если текущее число не следует сразу после предыдущего
			if (nums[i] != nums[i - 1] + 1) {
				// Завершаем текущий диапазон
				if (start == nums[i - 1]) {
					result.add(String.valueOf(start));
				} else {
					result.add(start + "->" + nums[i - 1]);
				}
				// Начинаем новый диапазон
				start = nums[i];
			}
		}
		
		// Добавляем последний диапазон
		if (start == nums[n - 1]) {
			result.add(String.valueOf(start));
		} else {
			result.add(start + "->" + nums[n - 1]);
		}
		
		return result;
	}
}
