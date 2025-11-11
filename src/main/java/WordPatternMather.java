import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/*
Given a pattern and a string s, find if s follows the same pattern.

Here follow means a full match, such that there is
a bijection between a letter in pattern and a non-empty word in s.

Specifically:

Each letter in pattern maps to exactly one unique word in s.
Each unique word in s maps to exactly one letter in pattern.
No two letters map to the same word, and no two words map to the same letter.

Example 1:

Input: pattern = "abba", s = "dog cat cat dog"
Output: true
Explanation:
The bijection can be established as:
'a' maps to "dog".
'b' maps to "cat".

Example 2:

Input: pattern = "abba", s = "dog cat cat fish"
Output: false

Example 3:
Input: pattern = "aaaa", s = "dog cat cat dog"
Output: false
 */
public class WordPatternMather {
	
	public static void main(String[] args) {
		System.out.println(wordPattern("abba", "dog cat cat dog"));
		System.out.println(wordPattern("abba", "dog cat cat fish"));
		System.out.println(wordPattern("aaaa", "dog cat cat dog"));
	}
	
	private static boolean wordPattern(String pattern, String s) {
		String[] words = s.split(" ");
		if (words.length != pattern.length()) {
			return false;
		}
		Map<Object, Integer> index = new HashMap<>();
		for (int i = 0; i < words.length; ++i) {
			Integer i1 = index.put(pattern.charAt(i), i);
			Integer i2 = index.put(words[i], i);
			if (!Objects.equals(i1, i2)) {
				return false;
			}
		}
		return true;
	}
}
