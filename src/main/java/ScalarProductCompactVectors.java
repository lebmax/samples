import java.util.Arrays;
import java.util.List;

/**
 * Даны 2 вектора целых чисел одинаковой длины, заданные в сжатой форме списками пар вида (value, count).
 * Например, вектор [4, 4, 5] задается как [(4, 2), (5, 1)].
 * Необходимо посчитать скалярное произведение заданных векторов.
 * Пример:
 * [(1, 3)], [(1, 2), (10, 1)] -> 12
 */
public class ScalarProductCompactVectors {

    public static void main(String[] args) {
        long res = dotProduct(
                Arrays.asList(new CompressedVector(1, 3)),
                Arrays.asList(new CompressedVector(1, 2), new CompressedVector(10, 1))
        );
        System.out.println(res);
    }

    private static long dotProduct(List<CompressedVector> lhs, List<CompressedVector> rhs) {
        long result = 0;
        CompressedVector lhs_block = new CompressedVector(0 ,0);
        CompressedVector rhs_block = new CompressedVector(0 ,0);
        int lhs_index = 0;
        int rhs_index = 0;

        while (lhs_index < lhs.size() || rhs_index < rhs.size()) {
            if (lhs_block.count == 0) {
                lhs_block = lhs.get(lhs_index);
                lhs_index++;
            }
            if (rhs_block.count == 0) {
                rhs_block = rhs.get(rhs_index);
                rhs_index++;
            }
            int common_count = Math.min(lhs_block.count, rhs_block.count);
            result += lhs_block.value * rhs_block.value * common_count;
            lhs_block.count -= common_count;
            rhs_block.count -= common_count;
        }
        return result;
    }

    private static class CompressedVector {
        long value;
        int count;

        public CompressedVector(long value, int count) {
            this.value = value;
            this.count = count;
        }
    }
}
