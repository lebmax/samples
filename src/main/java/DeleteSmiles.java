/**
 * Есть сообщения из соцсети, например:
 * "Я работаю в Гугле :-)))"
 * "везет :-) а я туда собеседование завалил:-(("
 * "лол:)"
 * "Ааааа!!!!! :-))(())"
 * Хочется удалить из них смайлики, подпадающие под регулярку ":-\)+|:-\(+" за линейное время. То есть, сделать так:
 * "Я работаю в Гугле "
 * "везет  а я туда собеседование завалил"
 * "лол:)"
 * "Ааааа!!!!! (())"
 * Специально обрабатывать вложенные смайлики нельзя. Так, :-:-)))((( должно превратиться в :-((( , но не в пустую строку.
 */
public class DeleteSmiles {

    public static void main(String[] args) {
        String res = removeSmiles("Я работаю в Гугле :-)))");
        System.out.println(res);

        res = removeSmiles("везет :-) а я туда собеседование завалил:-((");
        System.out.println(res);

        res = removeSmiles("лол:)");
        System.out.println(res);

        res = removeSmiles("Ааааа!!!!! :-))(())");
        System.out.println(res);

        res = removeSmiles(":-:-)))(((");
        System.out.println(res);

        res = removeSmiles(":-):-)))(((");
        System.out.println(res);
    }

    private static String removeSmiles(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            while (i + 2 < str.length()
                    && str.charAt(i) == ':' && str.charAt(i + 1) == '-'
                    && (str.charAt(i + 2) == ')' || str.charAt(i + 2) == '(')) {
                i += 3;
                while (i < str.length() && str.charAt(i) == str.charAt(i - 1)) {
                    ++i;
                }
            }
            if (i < str.length()) {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }
}
