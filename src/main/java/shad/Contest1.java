package shad;

import java.util.Scanner;

public class Contest1 {
	
	/**
	 * Напиши алгоритм на java для задачи, описанной ниже
	 * <p>
	 * Алексей решил изучить статистику и узнал, что одной из важных характеристик выборки является ее кубическое отклонение.
	 * <p>
	 * Так как Алексей занимается актуальной проблемой, ему могут приходить новые данные в выборку. Каждое новое измерение он записывает в конец текущей выборки. При этом ему необходимо уметь считать кубическое отклонение не всей выборки, а только некоторого числа самых актуальных измерений.
	 * <p>
	 * Помогите Алексею в его исследованиях: напишите программу, которая будет способна обрабатывать следующие запросы:
	 * <p>
	 * get k — узнать кубическое отклонение наиболее актуальных на текущий момент k измерений;
	 * add x — добавить только что пришедшее измерение x в выборку.
	 * <p>
	 * Формат ввода
	 * В первой строке даны два натуральных числа N и Q — размер известной выборки на начало работы программы и число запросов к программе соответственно.
	 * <p>
	 * В второй строке идут N целых неотрицательных чисел Xi — i-е измерение известной до старта программы выборки.
	 * <p>
	 * Далее идет Q строк в формате выше
	 * <p>
	 * 1 k — запрос get k, k целое, не менее двух и не превосходит текущего размера выборки;
	 * 2 x — запрос add x, x целое неотрицательное число, не превосходящее 200.
	 * <p>
	 * Формат вывода
	 * Выведите ответы на запросы типа get в отдельных строках с точностью не менее семи знаков после запятой. Ответ будет засчитан, если каждый ответ по относительной или абсолютной погрешности отличается от эталона не более чем на 10 в −6.
	 * <p>
	 * Пример
	 * Ввод
	 * 3 7
	 * 1 3 2
	 * 2 1
	 * 1 2
	 * 1 3
	 * 2 10
	 * 1 4
	 * 2 5
	 * 1 6
	 * <p>
	 * Вывод
	 * 0.0000000
	 * 0.0000000
	 * 3.5568933
	 * 3.2894241
	 * <p>
	 * Объяснение примера:
	 * <img src="/Users/lebmax/Desktop/Contest1.png" />
	 */
	private static final int MAX_N = 100000;
	private static final double EPS = 1e-7;
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		int n = scanner.nextInt();
		int q = scanner.nextInt();
		
		int[] data = new int[MAX_N];
		long[] prefixSums = new long[MAX_N + 1];
		
		for (int i = 0; i < n; i++) {
			data[i] = scanner.nextInt();
			prefixSums[i + 1] = prefixSums[i] + data[i];
		}
		
		while (q-- > 0) {
			int type = scanner.nextInt();
			if (type == 1) {
				int k = scanner.nextInt();
				System.out.printf("%.7f%n", calculateCubicDeviation(data, prefixSums, n, k));
			} else {
				int x = scanner.nextInt();
				data[n++] = x;
				prefixSums[n] = prefixSums[n - 1] + x;
			}
		}
	}
	
	private static double calculateCubicDeviation(int[] data, long[] prefixSums, int n, int k) {
		if (k > n) {
			return 0.0;
		}
		
		long sum = prefixSums[n] - prefixSums[n - k];
		double mean = (double) sum / k;
		
		double sumCubes = 0.0;
		for (int i = n - k; i < n; i++) {
			double diff = data[i] - mean;
			sumCubes += diff * diff * diff; // Оптимизация вместо Math.pow
		}
		
		return cbrt(sumCubes / k); // Оптимизированная кубическая корнеизвлечение
	}
	
	private static double cbrt(double x) {
		if (x == 0) return 0;
		double x1 = x;
		double x2 = (2 * x1 + x / (x1 * x1)) / 3;
		while (Math.abs(x2 - x1) > Contest1.EPS) {
			x1 = x2;
			x2 = (2 * x1 + x / (x1 * x1)) / 3;
		}
		return x2;
	}
}
