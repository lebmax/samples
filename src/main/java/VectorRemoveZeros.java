/**
 * Удалить нули из вектора
 * <p>
 * Дан вектор, надо удалить из него нули, сохранив порядок остальных элементов.
 * Интересует как с использованием стандартных средств, так и без них.
 * Для языков типа Python или Java, где управление памятью не так очевидно,
 * можно использовать формулировку "перенести нули в конец массива" вместо удаления
 * Руками за O(n) времени и O(1) памяти.
 */
public class VectorRemoveZeros {
	
	public static void main(String[] args) {
		int[] a = {0, 1, 2, 0, 0, 3, 0, 4, 0, 0, 5, 0};
		moveZeros(a);
		for (int i : a) {
			System.out.print(i + " ");
		}
		System.out.println();
		int[] a1 = {0, 1, 0, 3, 12};
		moveZeros(a1);
		for (int i : a1) {
			System.out.print(i + " ");
		}
		System.out.println();
		int[] a2 = {0};
		moveZeros(a2);
		for (int i : a2) {
			System.out.print(i + " ");
		}
	}
	
	static void moveZeros(int[] nums) {
		int n = nums.length, i = 0;
		for (int j = 0; j < n; j++) {
			if (nums[j] != 0) {
				nums[i] = nums[j];
				i++;
			}
		}
		for (int j = i; j < n; j++) {
			nums[j] = 0;
		}
	}
}
