import java.util.*;

/**
 * В функцию подается две строки: s и t, где t это непустой алфавит,
 * состоящий из уникальных символов
 * Надо найти наикратчайшую подстроку в s такую,
 * что она содержит все символы из алфавита t
 */
public class MinimumWindowSubstring {
	
	private static String minWindow(String s, String t) {
		if (s == null || t == null || s.isEmpty() || t.isEmpty()) {
			return "";
		}
		
		// Создаем массив для подсчета символов алфавита t
		int[] targetCount = new int[128]; // ASCII символы
		for (char c : t.toCharArray()) {
			targetCount[c]++;
		}
		
		int left = 0; // левый указатель окна
		int right = 0; // правый указатель окна
		int minLength = Integer.MAX_VALUE; // минимальная длина подстроки
		int minLeft = 0; // начальный индекс минимальной подстроки
		int requiredChars = t.length(); // количество символов, которые нужно найти
		int foundChars = 0; // количество найденных символов
		
		// Массив для подсчета символов в текущем окне
		int[] windowCount = new int[128];
		
		while (right < s.length()) {
			char currentChar = s.charAt(right);
			windowCount[currentChar]++;
			
			// Если текущий символ есть в алфавите t и его количество в окне
			// не превышает требуемое количество
			if (targetCount[currentChar] > 0 &&
					windowCount[currentChar] <= targetCount[currentChar]) {
				foundChars++;
			}
			
			// Если нашли все требуемые символы, пытаемся уменьшить окно слева
			while (foundChars == requiredChars && left <= right) {
				// Обновляем минимальную подстроку, если нашли более короткую
				int currentLength = right - left + 1;
				if (currentLength < minLength) {
					minLength = currentLength;
					minLeft = left;
				}
				
				// Убираем левый символ из окна
				char leftChar = s.charAt(left);
				windowCount[leftChar]--;
				
				// Если убрали символ из алфавита t и теперь его не хватает
				if (targetCount[leftChar] > 0 &&
						windowCount[leftChar] < targetCount[leftChar]) {
					foundChars--;
				}
				
				left++; // сдвигаем левый указатель
			}
			
			right++; // сдвигаем правый указатель
		}
		
		// Если не нашли подходящую подстроку
		if (minLength == Integer.MAX_VALUE) {
			return "";
		}
		
		return s.substring(minLeft, minLeft + minLength);
	}
	
	// Пример использования
	public static void main(String[] args) {
		String s1 = "ADOBECODEBANC";
		String t1 = "ABC";
		System.out.println("Строка: " + s1);
		System.out.println("Алфавит: " + t1);
		System.out.println("Наикратчайшая подстрока: " + minWindow(s1, t1));
		// Вывод: "BANC"
		
		String s2 = "a";
		String t2 = "a";
		System.out.println("\nСтрока: " + s2);
		System.out.println("Алфавит: " + t2);
		System.out.println("Наикратчайшая подстрока: " + minWindow(s2, t2));
		// Вывод: "a"
		
		String s3 = "a";
		String t3 = "aa";
		System.out.println("\nСтрока: " + s3);
		System.out.println("Алфавит: " + t3);
		System.out.println("Наикратчайшая подстрока: " + minWindow(s3, t3));
		// Вывод: "" (пустая строка)
	}
}
