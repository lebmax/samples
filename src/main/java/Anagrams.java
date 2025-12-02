public class Anagrams {

	public static void main(String[] args) throws Exception {
		assert isAnagram("anagram", "nagaram");
		assert !isAnagram("rat", "car");
	}
	
	private static boolean isAnagram(String s, String t) {
		if (t.equals(s)) {
			return true;
		}
		if (t.length() != s.length()) {
			return false;
		}
		int[] letters = new int[256];
		
		char[] sArray = s.toCharArray();
		for (char c : sArray) {
			letters[c]++;
		}
		
		for (int i = 0; i < t.length(); i++) {
			int c = t.charAt(i);
			if (--letters[c] < 0) {
				return false;
			}
		}
		return false;
	}
}
