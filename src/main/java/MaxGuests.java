import java.util.*;

/**
 * Даны даты заезда и отъезда каждого гостя.
 * Для каждого гостя дата заезда строго раньше даты отъезда (то есть каждый гость останавливается хотя бы на одну ночь)
 * Необходимо написать функцию, которая считает максимальное число посетителей, которые одновременно проживали в гостинице.
 * Ограничение по сложности — строго меньше O(N^2)
 * Примеры:
 * {} -> 0
 * {{1, 2}} -> 1
 * {{1, 2}, {2, 3}} -> 1
 * {{1, 5}, {0, 1}, {4, 5}} -> 2
 * {{1, 5}, {0, 1}, {4, 6}} -> 2
*/
public class MaxGuests {

    public static void main(String[] args) {
        int res = calcMaxGuestsCount(Arrays.asList(new Integer[]{1, 2}, new Integer[]{2, 3}));
        System.out.println(res);
        res = calcMaxGuestsCount(Arrays.asList(new Integer[]{1, 5}, new Integer[]{0, 1}, new Integer[]{4, 5}));
        System.out.println(res);
        res = calcMaxGuestsCount(Arrays.asList(new Integer[]{1, 5}, new Integer[]{0, 1}, new Integer[]{4, 6}));
        System.out.println(res);
    }

    private static int calcMaxGuestsCount(List<Integer[]> segments) {
        if (segments.isEmpty()) return 0;
        if (segments.size() == 1) return 1;
        segments.sort(Comparator.comparing(a -> a[0]));

        int res = 0;

        for (int i = 0; (i + 1) < segments.size(); i++) {
            int leave = segments.get(i)[1];
            Integer[] next = segments.get(i + 1);
            int arriveOth = next[0];
            if (leave >= arriveOth) {
                ++res;
            }
        }

        return res;
    }
}
