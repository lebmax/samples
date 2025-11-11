/**
 * Заменить последовательности пробелов в строке на одиночные пробелы. O(n) по времени, O(1) по памяти.
 * Пример:
 * "some    string " -> "some string "
 * "" -> ""
 * "  some    string " -> " some string "
 */
public class NormalizeWhitespace {

    public static void main(String[] args) {
        System.out.println(normalize("some    string "));
        System.out.println(normalize(""));
        System.out.println(normalize("  some    string "));
    }

    private static String normalize(String input) {
        if (!input.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            boolean isOneWhitespace = false;
            for (char c : input.toCharArray()) {
                if (!Character.isWhitespace(c)) {
                    sb.append(c);
                    isOneWhitespace = false;
                } else if (!isOneWhitespace) {
                    sb.append(c);
                    isOneWhitespace = true;
                }
            }
            return sb.toString();
        }
        return "";
    }
}
