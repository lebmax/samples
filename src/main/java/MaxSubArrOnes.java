/**
 * Дан непустой массив из нулей и единиц. Нужно определить,
 * какой максимальный по длине подынтервал единиц можно получить,
 * удалив (пропустив) ровно один элемент массива. Вернуть 0,
 * если такого подынтервала не существует.
 * Удалять (пропустить) один элемент из массива обязательно.
 * <p>
 * Примеры
 * assert(maxOnes({0, 0}) == 0)
 * assert(maxOnes({1, 0}) == 1)
 * assert(maxOnes({0, 1}) == 1)
 * assert(maxOnes({1, 1, 0, 1}) == 3)
 * assert(maxOnes({1, 1, 0, 0, 1}) == 2)
 * assert(maxOnes({1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 1, 1}) == 5)
 * assert(maxOnes({1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1}) == 6)
 * assert(maxOnes({1, 1, 1, 1, 0, 0}) == 4)
 * assert(maxOnes({1, 1, 1, 1, 1}) == 4)
 */
public class MaxSubArrOnes {

	public static void main(String[] args) throws Exception {
		int res = maxOnes(new int[]{0, 0});
		System.out.println(res);

		res = maxOnes(new int[]{1, 0});
		System.out.println(res);

		res = maxOnes(new int[]{0, 1});
		System.out.println(res);

		res = maxOnes(new int[]{1, 1, 0, 1});
		System.out.println(res);

		res = maxOnes(new int[]{1, 1, 0, 0, 1});
		System.out.println(res);

		res = maxOnes(new int[]{1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 1, 1});
		System.out.println(res);

		res = maxOnes(new int[]{1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1});
		System.out.println(res);

		res = maxOnes(new int[]{1, 1, 1, 1, 0, 0});
		System.out.println(res);

		res = maxOnes(new int[]{1, 1, 1, 1, 1});
		System.out.println(res);
	}

	public static int maxOnes(int[] nums) {
		int cur_len = 0, prev_len = 0, max_len = 0;

		for (int n : nums) {
			if (n != 0) {
				cur_len++;
				max_len = Math.max(max_len, cur_len + prev_len);
			} else {
				prev_len = cur_len;
				cur_len = 0;
			}
		}
		if (max_len == nums.length) {
			max_len--;
		}
		return max_len;
	}
}
