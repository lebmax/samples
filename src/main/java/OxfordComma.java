import java.io.IOException;

/**
 * Оксфордская запятая — это спорное и поддерживаемое не всеми носителями языка правило пунктуации в английском,
 * согласно которому при перечислении трёх или более сущностей следует ставить запятую перед союзом
 * (обычно and или or, а также nor) перед последней сущностью в списке.
 * Таким образом, словосочетание C++, Python and Ruby с оксфордской запятой
 * будет иметь вид C++, Python, and Ruby.
 * Пример 1
 * Ввод
 * C, Python and Ruby.
 * Вывод
 * C, Python, and Ruby.
 * Пример 2
 * Ввод
 * C and Ruby.
 * Вывод
 * C and Ruby.
 * Пример 3
 * Ввод
 * He knows C, Python, Perl, or Ruby.
 * Вывод
 * He knows C, Python, Perl, or Ruby.
 */
public class OxfordComma {

    public static void main(String[] args) throws IOException {
        System.out.println(checkComma("C, Python and Ruby."));
        System.out.println(checkComma("C and Ruby."));
        System.out.println(checkComma("He knows C, Python, Perl, or Ruby."));
        System.out.println(checkComma("He knows C and Python."));
        System.out.println(checkComma("C, Python."));
        System.out.println(checkComma("C, Python and Ruby and Java."));
    }

    private static String checkComma(String inStr) {
        String[] words = inStr.split(",");
        StringBuilder sb = new StringBuilder();
        if (words.length <= 1) {
            return inStr;
        }
        char[] lastWords = words[words.length - 1].toCharArray();
        for (int i = 0; i < words.length - 1; i++) {
            sb.append(words[i]).append(",");
        }

        StringBuilder s = new StringBuilder();
        for (int i = lastWords.length - 1; i >= 0; i--) {
            char c = lastWords[i];
            s.insert(0, c);
            if (c == ' ') {
                var mStr = s.toString().trim();
                if (mStr.startsWith("and") || mStr.startsWith("or") || mStr.startsWith("nor")) {
                    for (int j = 0; j < i; j++) {
                        sb.append(lastWords[j]);
                    }
                    if (sb.charAt(sb.length() - 1) == ',') {
                        sb.append(c).append(mStr);
                    } else {
                        sb.append(", ").append(mStr);
                    }
                    s.setLength(0);
                    break;
                }
            }
        }
        if (!s.isEmpty()) {
            sb.append(s);
        }
        return sb.toString();
    }
}
