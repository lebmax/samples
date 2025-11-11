/**
 * Даны три неубывающих массива чисел. Найти число,
 * которое присутствует во всех трех массивах.
 * <p>
 * Input: [1,2,4,5], [3,3,4], [2,3,4,5,6]
 * Output: 4
 * Целевое решение работает за O(p + q + r),
 * где p, q, r – длины массивов, доп. память O(1).
 */
public class CommonArrElementFinder {
	
	public static void main(String[] args) {
		int[] arr1 = {1, 2, 4, 5};
		int[] arr2 = {3, 3, 4};
		int[] arr3 = {2, 3, 4, 5, 6};
		
		Integer result = findCommonElement(arr1, arr2, arr3);
		System.out.println("Общий элемент: " + (result != null ? result : "не найден"));
	}

	public static Integer findCommonElement(int[] arr1, int[] arr2, int[] arr3) {
		int i = 0, j = 0, k = 0;
		
		while (i < arr1.length && j < arr2.length && k < arr3.length) {
			// Если все три числа равны - нашли общий элемент
			if (arr1[i] == arr2[j] && arr2[j] == arr3[k]) {
				return arr1[i];
			}
			
			// Двигаем указатель с наименьшим значением
			int min = Math.min(arr1[i], Math.min(arr2[j], arr3[k]));
			
			if (arr1[i] == min) i++;
			if (arr2[j] == min) j++;
			if (arr3[k] == min) k++;
		}
		
		return null; // Если общего элемента нет
	}
}
