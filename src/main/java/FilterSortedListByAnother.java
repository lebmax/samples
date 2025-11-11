import java.util.ArrayList;
import java.util.List;

/**
 * Даны две отсортированных по неубыванию последовательности целых чисел.
 * Необходимо вернуть все элементы из первой последовательности, которых нет во второй.
 * Пример:
 * ([1, 2, 3], [3, 4]) -> [1, 2]
 * ([1, 2, 2], [2]) -> [1]
 * ([1, 2, 3], [2, 2, 2]) -> [1, 3]
 * ([1, 1, 1], [1]) -> []
 */
public class FilterSortedListByAnother {

    public static void main(String[] args) {
        List<Integer> res = filterSortedList(List.of(1, 2, 3), List.of(3, 4));
        System.out.println(res);
        res = filterSortedList(List.of(1, 2, 2), List.of(2));
        System.out.println(res);
        res = filterSortedList(List.of(1, 2, 3), List.of(2, 2, 2));
        System.out.println(res);
        res = filterSortedList(List.of(1, 1, 1), List.of(1));
        System.out.println(res);
    }

    public static List<Integer> filterSortedList(List<Integer> it1, List<Integer> it2) {
        if (it1.isEmpty()) return new ArrayList<>();
        if (it2.isEmpty()) return it1;
        List<Integer> res = new ArrayList<>(it1.size());
        int j = 0;
        for (int cur1 : it1) {
            if (j < it2.size()) {
                int cur2 = it2.get(j);
                if (cur1 != cur2) {
                    res.add(cur1);
                } else {
                    ++j;
                }
            }
        }
        return res;
    }
}
