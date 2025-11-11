import java.util.Arrays;

/**
 * Заменить в массиве символы пробела на последовательность символов '%', '2', '0' по месту.
 * Гарантируется, что размер массива для этого достаточен.
 * Метод urlify() принимает исходный массив и длину исходной подпоследовательности символов, а возвращает новую длину.
 * Примеры:
 * {'m', 'y', ' ', 'u', 'r', 'l', '\0', '\0'} -> {'m', 'y', '%', '2', '0', 'u', 'r', 'l'})
 * newLength == 8
 */
public class UrlCode {

    public static void main(String[] args) {
        char[] sample = {'m', 'y', ' ', 'u', 'r', 'l', '\0', '\0'};
        var res = urlify(sample, 6);
        System.out.println(Arrays.toString(res) + " = " + res.length);
    }

    private static char[] urlify(char[] cArr, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = cArr[i];
            if (Character.isWhitespace(c)) {
                sb.append("%20");
            } else {
                sb.append(c);
            }
        }
        return sb.toString().toCharArray();
    }
}
