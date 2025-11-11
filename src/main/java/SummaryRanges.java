import java.util.Arrays;

/**
 * Дан список интов, повторяющихся элементов в списке нет.
 * Нужно преобразовать это множество в строку,
 * сворачивая соседние по числовому ряду числа в диапазоны.
 * <p>
 * Примеры:
 * [1,4,5,2,3,9,8,11,0] => "0-5,8-9,11"
 * [1,4,3,2] => "1-4"
 * [1,4] => "1,4"
 * [1,2] => "1-2"
 * [1] => "1"
 * [1,1] => "1"
 */
public class SummaryRanges {

    public static void main(String[] args) throws Exception {
        String res = compress(new int[]{1, 4, 5, 2, 3, 9, 8, 11, 0});
        System.out.println(res);

        res = compress(new int[]{1, 4, 3, 2});
        System.out.println(res);

        res = compress(new int[]{1, 4});
        System.out.println(res);

        res = compress(new int[]{1, 2});
        System.out.println(res);

        res = compress(new int[]{1});
        System.out.println(res);

        res = compress(new int[]{1, 1});
        System.out.println(res);
    }

    private static String compress(int[] array) {
        Arrays.sort(array);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            int prev_diff = 0;
            int next_diff = 0;
            if (sb.isEmpty())
                sb.append(array[i]);

            // check previous difference
            if (i > 0)
                prev_diff = array[i] - array[i - 1];

            // check next difference
            if (i < array.length - 1)
                next_diff = array[i + 1] - array[i];

            if (prev_diff == 1 && next_diff != 1) {
                sb.append("-").append(array[i]);
            }

            if (prev_diff > 1) {
                sb.append(",").append(array[i]);
            }
        }
        return sb.toString();
    }
}
