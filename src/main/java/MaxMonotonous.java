import java.util.Arrays;

/**
 * Дан массив чисел a_1, a_2, ..., a_n.
 * Необходимо найти монотонный подотрезок (то есть строго убывающий или строго возрастающий)
 * максимальной длины и вернуть пару индексов его начала и конца.
 * Примеры:
 * [2, 7, 5, 4, 4, 3] -> {1, 3}
 * [1, 1] -> {1, 1} // or {0, 0}
 */
public class MaxMonotonous {

    public static void main(String[] args) {
        int[] res = findMaxMonotonous(new int[]{2, 7, 5, 4, 4, 3});
        System.out.println(Arrays.toString(res));

        res = findMaxMonotonous(new int[]{1, 1});
        System.out.println(Arrays.toString(res));

        res = findMaxMonotonous(new int[]{1, 2});
        System.out.println(Arrays.toString(res));

        res = findMaxMonotonous(new int[]{1, 2, 3, 3, 3, 1, 0});
        System.out.println(Arrays.toString(res));

        res = findMaxMonotonous(new int[]{1});
        System.out.println(Arrays.toString(res));
    }

    private static int [] findMaxMonotonous(int[] vec) {
        int left = 0;
        int right = 0;
        int retLeft = left;
        int retRight = right;
        while (right + 1 < vec.length) {
            ++right;
            if (vec[right] == vec[right - 1]) {
                left = right;
            }
            if (left + 1 < vec.length) {
                if ((vec[right] > vec[right - 1]) != (vec[left + 1] > vec[left])) {
                    left = right - 1;
                }
            }
            if (retRight - retLeft < right - left) {
                retLeft = left;
                retRight = right;
            }
        }
        return new int[]{retLeft, retRight};
    }
}
