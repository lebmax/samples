import java.util.*;

/**
 * Дана строка символов. Найти количество пар индексов i и j,
 * между которыми, включительно, нет повторяющихся символов.
 * Для строки "aba" ответ 5: [0, 0] ("a"), [0, 1] ("ab"), [1, 1] ("b"), [1, 2] ("ba"), [2, 2] ("a")
 * "aba" -> 5
 * "abc" -> 6
 * "abba" -> 4
 * "acbacbacaa" -> 10
 */
public class NumSubstrWithoutRepetitions {

    public static void main(String[] args) {
        System.out.println(uniqueSubstr("aba"));
        System.out.println(uniqueSubstr("abc"));
        System.out.println(uniqueSubstr("abba"));
        System.out.println(uniqueSubstr("acbacbacaa"));
    }

//    private static int uniqueSubstr(String str) {
//        int result = 0;
//        if (!str.isEmpty()) {
//            int left = 0;
//            int right = 0;
//            HashMap<Character, Integer> map = new HashMap<>();
//            while (right < str.length()) {
//                if (map.getOrDefault(str.charAt(right), -1) < left) {
//                    result += right - left + 1;
//                } else {
//                    left = map.get(str.charAt(right));
//                    result += map.size();
//                }
//                map.put(str.charAt(right), right);
//                right++;
//            }
//        }
//        return result;
//    }

    private static int uniqueSubstr(String s) {
        Set<Character> set = new LinkedHashSet<>();
        int result = 0;
        for (int i = 0; i < s.length(); i++) {
            if (set.contains(s.charAt(i))) {
                Iterator<Character> iterator = set.iterator();
                while (iterator.hasNext()) {
                    char ch = iterator.next();
                    iterator.remove();
                    if (ch == s.charAt(i)) {
                        break;
                    }
                }
            }
            set.add(s.charAt(i));
            result += set.size();
        }
        return result;
    }

    // Function to count all unique
    // distinct character subStrings
    private static int distinctSubString(String str) {
        // Hashmap to store all subStrings
        HashSet<String> set = new HashSet<>();

        // Iterate over all the subStrings
        for (int i = 0; i < str.length(); ++i) {

            // Boolean array to maintain all
            // characters encountered so far
            boolean[] freq = new boolean[26];

            // Variable to maintain the
            // subString till current position
            String s = "";

            for (int j = i; j < str.length(); ++j) {

                // Get the position of the
                // character in the String
                int pos = str.charAt(j) - 'a';

                // Check if the character is encountered
                if (freq[pos])
                    break;

                freq[pos] = true;

                // Add the current character to the subString
                s += str.charAt(j);

                // Insert subString in Hashmap
                set.add(s);
            }
        }

        return set.size();
    }
}
