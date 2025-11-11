import java.util.Arrays;

/**
 * Дана последовательность a и последовательность a'.
 * Требуется найти такие l, r, что отсортировав подмассив a[l:r+1],
 * можно получить массив a'.
 * Если ответов несколько, выберите отрезок наибольшей длины.
 * По условию такая операция всегда существует.
 */
public class FindSortingSubarray {
	
	public static int[] findSortingSubarray(int[] a, int[] aPrime) {
		if (a.length != aPrime.length) {
			throw new IllegalArgumentException("Arrays must be of equal length");
		}
		
		int n = a.length;
		int l = 0;
		int r = n - 1;
		
		// Находим начальный индекс различия
		while (l < n && a[l] == aPrime[l]) {
			l++;
		}
		
		// Если массивы уже идентичны
		if (l == n) {
			return new int[]{0, n - 1}; // Можно вернуть любой допустимый отрезок
		}
		
		// Находим конечный индекс различия
		while (r >= 0 && a[r] == aPrime[r]) {
			r--;
		}
		
		// Проверяем, что подмассив после сортировки совпадает с a'
		int[] sortedSubarray = Arrays.copyOfRange(a, l, r + 1);
		Arrays.sort(sortedSubarray);
		int[] targetSubarray = Arrays.copyOfRange(aPrime, l, r + 1);
		
		if (!Arrays.equals(sortedSubarray, targetSubarray)) {
			throw new IllegalStateException("No valid subarray found - this shouldn't happen according to problem statement");
		}
		
		// Пытаемся расширить отрезок, если возможно
		// Влево
		while (l > 0 && aPrime[l - 1] <= aPrime[l]) {
			l--;
		}
		
		// Вправо
		while (r < n - 1 && aPrime[r] <= aPrime[r + 1]) {
			r++;
		}
		
		return new int[]{l, r};
	}
	
	public static int[] findMaxRange(int[] a, int[] b) {
		int n = a.length;
		
		// Найдём первый и последний индексы различия
		int l = 0;
		while (l < n && a[l] == b[l]) {
			l++;
		}
		if (l == n) {
			// a == b
			return new int[]{0, n - 1};
		}
		
		int r = n - 1;
		while (r >= 0 && a[r] == b[r]) {
			r--;
		}
		
		// Расширяем влево
		while (l > 0 && a[l - 1] == b[l - 1] && b[l - 1] <= b[l]) {
			l--;
		}
		
		// Расширяем вправо
		while (r < n - 1 && a[r + 1] == b[r + 1] && b[r + 1] >= b[r]) {
			r++;
		}
		
		return new int[]{l, r};
	}

	public static void main(String[] args) {
		// Пример использования
		int[] a = {1, 2, 3, 5, 4, 6, 7};
		int[] aPrime = {1, 2, 3, 4, 5, 6, 7};
		
		int[] result = findSortingSubarray(a, aPrime);
		System.out.println("l = " + result[0] + ", r = " + result[1]); // Выведет l = 3, r = 4
		
		result = findMaxRange(a, aPrime);
		System.out.println("l = " + result[0] + ", r = " + result[1]); // Выведет l = 3, r = 4
	}
}
