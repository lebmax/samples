/**
 * Реализовать функцию приблизительного совпадения строк
 * Примеры:
 * fuzzysearch("car", "cartwheel");        // true
 * fuzzysearch("cwhl", "cartwheel");       // true
 * fuzzysearch("cwhee", "cartwheel");      // true
 * fuzzysearch("cartwheel", "cartwheel");  // true
 * fuzzysearch("cwheeel", "cartwheel");    // false
 * fuzzysearch("lw", "cartwheel");         // false
*/
public class FuzzySearch {

    public static void main(String[] args) {
        System.out.println(fuzzysearch("car", "cartwheel"));
        System.out.println(fuzzysearch("cwhl", "cartwheel"));
        System.out.println(fuzzysearch("cwhee", "cartwheel"));
        System.out.println(fuzzysearch("cartwheel", "cartwheel"));
        System.out.println(fuzzysearch("cwheeel", "cartwheel"));
        System.out.println(fuzzysearch("lw", "cartwheel"));
    }

    private static boolean fuzzysearch(String searchRequest, String text) {
        int i = 0;
        for (int j = 0; i < searchRequest.length() && j < text.length(); j++) {
            if (Character.toLowerCase(searchRequest.charAt(i)) == Character.toLowerCase(text.charAt(j))) {
                i++;
            }
        }
        return i == searchRequest.length();
    }
}
