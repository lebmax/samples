import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Даны два отсортированных списка с интервалами присутствия
 * пользователей в онлайне в течение дня. Начало интервала строго меньше конца.
 * Нужно вычислить интервалы, когда оба пользователя были в онлайне.
 * Примеры:
 * intersection([(8, 12), (17, 22)], [(5, 11), (14, 18), (20, 23)]) = [(8, 11), (17, 18), (20, 22)]
 * intersection([(9, 15), (18, 21)], [(10, 14), (21, 22)]) = [(10, 14)]
 */
public class ArrayIntersection {

    public static void main(String[] args) {
        System.out.println(
                intersection(
                        Arrays.asList(new Pair(8, 12), new Pair(17, 22)),
                        Arrays.asList(new Pair(5, 11), new Pair(14, 18), new Pair(20, 23))
                ).stream().map(Pair::toString).toList()
        );
        System.out.println(
                intersection(
                        Arrays.asList(new Pair(9, 15), new Pair(18, 21)),
                        Arrays.asList(new Pair(10, 14), new Pair(21, 22))
                ).stream().map(Pair::toString).toList()
        );
    }

    private static List<Pair> intersection(List<Pair> l1, List<Pair> l2) {
        int i1 = 0;
        int i2 = 0;
        List<Pair> res = new ArrayList<>();
        while (i1 < l1.size() && i2 < l2.size()) {
            int left = Math.max(l1.get(i1).first, l2.get(i2).first);
            int right = Math.min(l1.get(i1).second, l2.get(i2).second);

            if (right > left) {
                res.add(new Pair(left, right));
            }

            if (l1.get(i1).second < l2.get(i2).second) {
                i1++;
            } else {
                i2++;
            }
        }
        return res;
    }

    private static class Pair {
        int first;
        int second;

        public Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "first=" + first +
                    ", second=" + second +
                    '}';
        }
    }
}
