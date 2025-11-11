import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Напиши алгоритм на Java для задачи:
 * Дан массив целых чисел. Необходимо посчитать сумму минимумов
 * каждого подмассива исходного массива.
 * Ответ вывести по модулю 1e9
 * <p>
 * Пример:
 * [3, 1, 2, 4]
 * Ответ: 17
 * <p>
 * Для каждого элемента массива определим,
 * в скольких подмассивах он является минимальным.
 * Если мы знаем, сколько таких подмассивов —
 * умножаем значение элемента на это количество и суммируем.
 * <p>
 * Чтобы найти количество подмассивов, где arr[i] — минимум, нужно найти:
 * left[i] — количество элементов слева от i (включая i),
 * до ближайшего строго меньшего элемента.
 * right[i] — количество элементов справа от i (включая i),
 * до ближайшего меньшего или равного элемента.
 * <p>
 * Почему строгое слева и нестрогое справа?
 * Чтобы избежать двойного подсчёта одинаковых минимумов (например, в [2, 2]).
 * <p>
 * Тогда количество подмассивов, где arr[i] — минимум:
 * count = left[i] * right[i]
 * <p>
 * И вклад в ответ:
 * arr[i] * left[i] * right[i]
 */
public class SumOfSubarrayMins {
	
	private static final int MOD = 1_000_000_000;
	
	public static int sumSubarrayMins(int[] arr) {
		int n = arr.length;
		long result = 0;
		
		// Стек для индексов
		Deque<Integer> stack = new ArrayDeque<>();
		
		// left[i] — расстояние до ближайшего слева строго меньшего элемента
		int[] left = new int[n];
		for (int i = 0; i < n; i++) {
			while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
				stack.pop();
			}
			left[i] = stack.isEmpty() ? i + 1 : i - stack.peek();
			stack.push(i);
		}
		
		stack.clear();
		
		// right[i] — расстояние до ближайшего справа меньшего или равного элемента
		int[] right = new int[n];
		for (int i = n - 1; i >= 0; i--) {
			while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) {
				stack.pop();
			}
			right[i] = stack.isEmpty() ? n - i : stack.peek() - i;
			stack.push(i);
		}
		
		// Считаем ответ
		for (int i = 0; i < n; i++) {
			result = (result + (long) arr[i] * left[i] % MOD * right[i]) % MOD;
		}
		
		return (int) result;
	}
	
	// Тест
	public static void main(String[] args) {
		int[] arr = {3, 1, 2, 4};
		System.out.println(sumSubarrayMins(arr)); // Выведет: 17
	}
}
