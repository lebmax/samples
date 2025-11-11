
public class MaxSubstr {
	
	// Определяем диапазон символов
	private static final int CHAR_RANGE = 128;

	public static void main(String[] args) {
		System.out.println(maxLength("ab"));
		System.out.println(maxLength("aaa"));
		System.out.println(maxLength("abaccab"));
		System.out.println(maxLength("baacabb"));
		
		System.out.println(findLongestSubstring("ab"));
		System.out.println(findLongestSubstring("aaa"));
		System.out.println(findLongestSubstring("abaccab"));
		System.out.println(findLongestSubstring("baacabb"));
	}

	//	Задана строка S из малых латинских букв,
	//	требуется узнать длину наибольшей подстроки состоящей
	//	не более чем из двух различных символов.
	//	Примеры:
	//
	//	"ab" -> 2
	//	"aaa" -> 3
	//	"abaccab" -> 4
	//  "baacabb" -> 4

	static int maxLength(String s) {
		if (s.isEmpty()) {
			return 0;
		}
		if (s.length() == 1) {
			return 1;
		}
		char[] cs = s.toCharArray();
		StringBuilder str = new StringBuilder();
		StringBuilder maxLength = new StringBuilder();
		for (int i = 0; i < cs.length; i++) {
			final char c = cs[i];
			str.append(c);
			int count = 2;
			char prev = c;
			for (int j = i + 1; j < cs.length && count != 0; j++) {
				final char cj = cs[j];
				if (c != cj && prev != cj) {
					count--;
				}
				prev = cj;
				if (count != 0) {
					str.append(cj);
				}
			}
			if (maxLength.length() < str.length()) {
				maxLength.setLength(0);
				maxLength.append(str);
			}
			str.setLength(0);
		}
		return maxLength.length();
	}
	
	// Функция для поиска самой длинной подстроки со всеми
	// отдельные символы с использованием скользящего окна
	public static String findLongestSubstring(String str) {
		// базовый вариант
		if (str == null || str.isEmpty()) {
			return str;
		}
		
		// логический массив для обозначения символов, присутствующих в текущем окне
		boolean[] window = new boolean[CHAR_RANGE];
		
		// сохраняет самые длинные границы подстроки
		int begin = 0, end = 0;
		
		// `[low…high]` сохраняет границы скользящего окна
		for (int low = 0, high = 0; high < str.length(); high++) {
			// если текущий символ присутствует в текущем окне
			if (window[str.charAt(high)]) {
				// удаляем символы слева от окна до
				// встречаем текущий символ
				while (str.charAt(low) != str.charAt(high)) {
					window[str.charAt(low)] = false;
					low++;
				}
				
				low++;        // удалить текущий символ
			} else {
				// если текущий символ отсутствует в текущем
				// окно, включаем его
				window[str.charAt(high)] = true;
				
				// обновляем максимальный размер окна при необходимости
				if (end - begin < high - low) {
					begin = low;
					end = high;
				}
			}
		}
		
		// вернуть самую длинную подстроку, найденную в `str[begin…end]`
		return str.substring(begin, end + 1);
	}
}
