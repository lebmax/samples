/**
 * На входе дана непустая строка. Требуется выяснить, можно ли удалить
 * из нее ровно один символ так, чтобы получился палиндром.
 * Примеры:
 * "aaab" -> true  # можно удалить последний символ
 * "xyz" -> false # нельзя превратить в палиндром удалением одного символа
 * "aba" -> true
 * "abca" -> true
 */
public class ValidPalindrome {
	
	public static void main(String[] args) {
		System.out.println(validPalindrome("aaab"));
		System.out.println(validPalindrome("xyz"));
		System.out.println(validPalindrome("aba"));
		System.out.println(validPalindrome("abca"));
		
		System.out.println(isPalindrome("A man, a plan, a canal: Panama"));
	}
	
	private static boolean validPalindrome(String s) {
		String a = s.toLowerCase();
		int low = 0, high = a.length() - 1, count1 = 0, count2 = 0;
		while (low < high) {
			if (a.charAt(low) == a.charAt(high)) {
				low++;
				high--;
			} else {
				count1++;
				low++;
			}
		}
		low = 0;
		high = a.length() - 1;
		while (low < high) {
			if (a.charAt(low) == a.charAt(high)) {
				low++;
			} else {
				count2++;
			}
			high--;
		}
		return count1 == 0 || count1 == 1 || count2 == 0 || count2 == 1;
	}
	
	private static boolean isPalindrome(String s) {
		String a = s.toLowerCase();
		int i = 0, j = a.length() - 1;
		
		while (i < j) {
            var cf = a.charAt(i);
            var cl = a.charAt(j);
			if (!Character.isLetterOrDigit(cf)) {
				i++;
			} else if (!Character.isLetterOrDigit(cl)) {
				j--;
			} else {
				if (cf != cl) {
					return false;
				}
				i++;
				j--;
			}
		}
		return true;
	}
}
