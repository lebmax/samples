import java.util.ArrayList;
import java.util.List;

/**
 * Дан отсортированный массив целых чисел a, индекс элемента index и целое число k.
 * Необходимо вернуть в любом порядке k чисел из массива,
 * которые являются ближайшими по значению к элементу a[index]
 * Ограничения:
 * - Размер массива 1 <= N <= 10^6 ;
 * - Элементы массива: -10^9 <= a[i] <= 10^9 ;
 * - Число 0 <= k <= N ;
 * Индекс элемента 0 <= index < N .
 * a = {1, 2, 3, 4, 5}, index = 3, k = 4 -> {1, 2, 3, 4}
 * a = {1, 2, 3, 4, 5}, index = -1, k = 4 -> {1, 2, 3, 4}
 * a = {2, 3, 5, 7, 11}, index = 3, k = 2 -> {5, 7}
 * a = {4, 12, 15, 15, 24}, index = 1, k = 3 -> {12, 15, 15}
 * a = {2, 3, 5, 7, 11}, index = 2, k = 2 -> {3, 5} или {5, 7}
 */
public class FindKClosestElements {

    public static void main(String[] args) {
        List<Integer> res = findClosestElements(new int[]{1, 2, 3, 4, 5}, 3, 4);
        System.out.println(res);

        res = findClosestElements(new int[]{1, 2, 3, 4, 5}, 0, 4);
        System.out.println(res);

        res = findClosestElements(new int[]{2, 3, 5, 7, 11}, 3, 2);
        System.out.println(res);

        res = findClosestElements(new int[]{4, 12, 15, 15, 24}, 1, 3);
        System.out.println(res);

        res = findClosestElements(new int[]{2, 3, 5, 7, 11}, 2, 2);
        System.out.println(res);
    }

    private static List<Integer> findClosestElements(int[] a, int index, int k) {
        int start = 0;
        int end = a.length - 1;
        int elemVal = a[index];
        while (end - start >= k) {
            if (Math.abs(a[start] - elemVal) > Math.abs(a[end] - elemVal)) {
                start++;
            } else {
                end--;
            }
        }

        List<Integer> result = new ArrayList<>(k);
        for (int i = start; i <= end; i++) {
            result.add(a[i]);
        }
        return result;
    }
}
