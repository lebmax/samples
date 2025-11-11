/**
 * Нужно реализовать функцию, проверяющую, можно ли одну строку получить из другой не более,
 * чем за одно исправление (удаление, добавление, изменение символа)
 */
public class OneEditApart {

    public static void main(String[] args) {
        boolean res = oneEditApart("cat", "dog");
        System.out.println(res);

        res = oneEditApart("cat", "cats");
        System.out.println(res);

        res = oneEditApart("cat", "cut");
        System.out.println(res);

        res = oneEditApart("cat", "cast");
        System.out.println(res);

        res = oneEditApart("cat", "at");
        System.out.println(res);

        res = oneEditApart("cat", "acts");
        System.out.println(res);

        res = oneEditApart("aa", "ab");
        System.out.println(res);

        res = oneEditApart("ab", "bb");
        System.out.println(res);
    }

    private static boolean oneEditApart(String str1, String str2) {
        int l1 = str1.length();
        int l2 = str2.length();
        if (l1 == l2) {
            boolean was_diff = false;

            for (int c = 0; c < l1; c++) {
                if (str1.charAt(c) != str2.charAt(c)) {
                    if (was_diff) {
                        return false;
                    }
                    was_diff = true;
                }
            }
            return true;
        } else if (Math.abs(l1 - l2) == 1) {
            String shortStr = l1 < l2 ? str1 : str2;
            String longStr = l1 > l2 ? str1 : str2;
            int l = 0;

            int r = Math.min(l1, l2);
            while (shortStr.charAt(l) == longStr.charAt(l)) {
                l += 1;
                if (l == r) {
                    return true;
                }
            }
            while (shortStr.charAt(r - 1) == longStr.charAt(r)) {
                r -= 1;
                if (r == 0) break;
            }
            return l == r;
        }
        return false;
    }
}
