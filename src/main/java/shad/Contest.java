package shad;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

public class Contest {
	
	/**
	 * Инвестор Алексей уже давно следит за курсом кривдовалюты ШАДкоин.
	 * Он хочет обучить модель предсказания курса,
	 * используя подходы из анализа временных рядов.
	 * Для этого данные анализируются скользящим окном размера
	 * K дней. В течение дня курс ШАДкоина стабилен.
	 * <p>
	 * Алексей считает, что одной из важнейших фичей является сумма
	 * максимального курса в окне и минимального.
	 * Не является индивидуальной инвестиционной рекомендацией.
	 * <p>
	 * Помогите Алексею найти максимальное значение данной фичи
	 * на протяжении всей истории его наблюдений за курсом, которая занимает N дней.
	 * <p>
	 * Формат ввода
	 * В первой строке даны два числа N и K — длина истории наблюдений
	 * и «ширина» окна в днях соответственно.
	 * <p>
	 * Во второй строке даны через пробел N целых чисел ai — курс ШАДкоина в i-й день.
	 * <p>
	 * Формат вывода
	 * Выведите единственное число — максимальное значение фичи Алексея.
	 * <p>
	 * Пример 1
	 * Ввод
	 * 4 2
	 * 1 4 3 5
	 * <p>
	 * Вывод
	 * 8
	 * <p>
	 * Пример 2
	 * Ввод
	 * 5 2
	 * -10 -9 10 1 80
	 * <p>
	 * Вывод
	 * 81
	 * <p>
	 * Пример 3
	 * Ввод
	 * 5 2
	 * 1 2 1 1 1
	 * <p>
	 * Вывод
	 * 3
	 * <p>
	 * Пример 4
	 * Ввод
	 * 5 3
	 * -10 -9 10 1 80
	 * <p>
	 * Вывод
	 * 81
	 * <p>
	 * Примечания
	 * Скользящее окно длины K по массиву из N элементов [11,…,aN]]
	 * принимает следующие положения:
	 * [a1,…,aK],[a2,…,aK+1], …, [aN−K+1,…,aN].
	 * <p>
	 * В четвертом примере в окне оказываются следующие наборы курсов:
	 * [−10,−9,10], максимум: 1−10 – значение фичи 0
	 * [−9,10,1], максимум: 10, минимум −9 – значение фичи 1
	 * [10,1,80], максимум: 80, минимум 1 – значение фичи 81
	 * <p>
	 * Максимальное значение равно 81.
	 */
//	public static void main(String[] args) {
//		Scanner scanner = new Scanner(System.in);
//
//		// Считываем входные данные
//		int N = scanner.nextInt();
//		int K = scanner.nextInt();
//
//		int[] prices = new int[N];
//		for (int i = 0; i < N; i++) {
//			prices[i] = scanner.nextInt();
//		}
//
//		// Инициализируем переменные для результата
//		int maxFeature = Integer.MIN_VALUE;
//
//		// Проходим по всем возможным окнам размера K
//		for (int i = 0; i <= N - K; i++) {
//			int windowMax = Integer.MIN_VALUE;
//			int windowMin = Integer.MAX_VALUE;
//
//			// Находим максимум и минимум в текущем окне
//			for (int j = i; j < i + K; j++) {
//				windowMax = Math.max(windowMax, prices[j]);
//				windowMin = Math.min(windowMin, prices[j]);
//			}
//
//			// Вычисляем значение фичи для текущего окна
//			int featureValue = windowMax + windowMin;
//
//			// Обновляем максимальное значение фичи
//			maxFeature = Math.max(maxFeature, featureValue);
//		}
//
//		// Выводим результат
//		System.out.println(maxFeature);
//	}
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		int N = scanner.nextInt();
		int K = scanner.nextInt();
		
		int[] prices = new int[N];
		for (int i = 0; i < N; i++) {
			prices[i] = scanner.nextInt();
		}
		
		// Создаем очереди для хранения индексов максимумов и минимумов
		Deque<Integer> maxDeque = new ArrayDeque<>();
		Deque<Integer> minDeque = new ArrayDeque<>();
		
		int maxFeature = Integer.MIN_VALUE;
		
		// Заполняем начальные значения для первого окна
		for (int i = 0; i < K; i++) {
			// Поддерживаем максимум в окне
			while (!maxDeque.isEmpty() && prices[i] >= prices[maxDeque.getLast()]) {
				maxDeque.removeLast();
			}
			maxDeque.addLast(i);
			
			// Поддерживаем минимум в окне
			while (!minDeque.isEmpty() && prices[i] <= prices[minDeque.getLast()]) {
				minDeque.removeLast();
			}
			minDeque.addLast(i);
		}
		
		// Вычисляем значение фичи для первого окна
		maxFeature = prices[maxDeque.getFirst()] + prices[minDeque.getFirst()];
		
		// Проходим по остальным окнам
		for (int i = K; i < N; i++) {
			// Обновляем очереди для следующего окна
			while (!maxDeque.isEmpty() && maxDeque.getFirst() <= i - K) {
				maxDeque.removeFirst();
			}
			while (!minDeque.isEmpty() && minDeque.getFirst() <= i - K) {
				minDeque.removeFirst();
			}
			
			// Добавляем новые элементы
			while (!maxDeque.isEmpty() && prices[i] >= prices[maxDeque.getLast()]) {
				maxDeque.removeLast();
			}
			maxDeque.addLast(i);
			
			while (!minDeque.isEmpty() && prices[i] <= prices[minDeque.getLast()]) {
				minDeque.removeLast();
			}
			minDeque.addLast(i);
			
			// Обновляем максимальное значение фичи
			maxFeature = Math.max(maxFeature, prices[maxDeque.getFirst()] + prices[minDeque.getFirst()]);
		}
		
		System.out.println(maxFeature);
	}
}
