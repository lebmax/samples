/**
 * Дан текст T и строка S. Требуется найти подстроку S' в T такую,
 * что она совпадает с S с точностью до перестановки букв.
 * В качестве ответа стоит вернуть индекс первого вхождения,
 * или -1, если такая подстрока S' не нашлась.
 * Примеры:
 * ("reebok", "bee") -> 1
 * ("ab", "eidbaooo") -> 3
 * ("ab", "eidboaoo") -> -1
 */
public class PermutationInString {

    public static void main(String[] args) {
        System.out.println(checkInclusion("bee", "reebok"));
        System.out.println(checkInclusion("ab", "eidbaooo"));
        System.out.println(checkInclusion("ab", "eidboaoo"));
    }

    private static int checkInclusion(String s1, String s2) {
        for (int i = 0; i <= s2.length() - s1.length(); i++) {
            int res = s1.indexOf(s2.charAt(i));
            if (res != -1) {
                StringBuilder temp = new StringBuilder(s1);
                String s3 = s2.substring(i, i + s1.length());
                for (char c : s3.toCharArray()) {
                    int index = temp.indexOf(String.valueOf(c));
                    if (index == -1) break;
                    temp.deleteCharAt(index);
                }
                if (temp.isEmpty()) return i;
            }
        }
        return -1;
    }
}
