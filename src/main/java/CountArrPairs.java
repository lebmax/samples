import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Дан массив интов и число K, K >= 0.
 * Нужно найти количество пар(уникальных или не уникальных,
 * с точки зрения их значения) элементов, разница которых >= K
 */
public class CountArrPairs {

    public static void main(String[] args) {
        int res = kDiffPairs(Arrays.asList(1, 2, 3, 4, 5), 1);
        System.out.println(res);

        res = kDiffPairs(Arrays.asList(1, 2, 3, 3), 1);
        System.out.println(res);

        res = kDiffPairs(Arrays.asList(1, 2, 3, 3), 0);
        System.out.println(res);
    }

    private static int kDiffPairs(List<Integer> v, int k) {
        Collections.sort(v);
        int lf = 0;
        int rg = 0;
        int ans = 0;
        do {
            while (rg + 1 < v.size() && v.get(rg + 1) - v.get(lf) < k) {
                ++rg;
            }
            ans += (v.size() - rg - 1);
            ++lf;
            rg = Math.max(lf, rg);
        } while (rg < v.size());
        return ans;
    }
}
