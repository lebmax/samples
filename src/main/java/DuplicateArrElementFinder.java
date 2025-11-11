/**
 * Дан массив A длины (n+1), содержащий натуральные числа от 1 до n.
 * Найти любой повторяющийся элемент за время O(n),
 * не изменяя массив и не используя дополнительной памяти.
 */
public class DuplicateArrElementFinder {
	
	public static void main(String[] args) {
		int[] nums = {2, 5, 1, 3, 4, 5};
		System.out.println("Повторяющийся элемент: " + findDuplicate(nums)); // Выведет 5
	}

	public static int findDuplicate(int[] nums) {
		// Шаг 1: Находим точку встречи
		int slow = nums[0];
		int fast = nums[0];
		
		do {
			slow = nums[slow];
			fast = nums[nums[fast]];
		} while (slow != fast);
		
		// Шаг 2: Находим начало цикла
		slow = nums[0];
		while (slow != fast) {
			slow = nums[slow];
			fast = nums[fast];
		}
		
		return slow;
	}
}
