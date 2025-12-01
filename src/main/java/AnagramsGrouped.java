import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Дан массив строк strs. Найдите анаграммы и сгруппируйте их вместе.
 * Группы анаграмм можно вывести в любом порядке.
 * Пример:
 * Input: strs = ["eat","tea","tan","ate","nat","bat"]
 * Output: [["bat"],["nat","tan"],["ate","eat","tea"]]
 */
public class AnagramsGrouped {
	
	private static List<List<String>> groupAnagrams(String[] strs) {
		if (strs == null || strs.length == 0) {
			return new ArrayList<>();
		}
		
		Map<String, List<String>> groups = new HashMap<>();
		
		for (String str : strs) {
			// Преобразуем строку в массив символов, сортируем, собираем обратно
			char[] chars = str.toCharArray();
			Arrays.sort(chars);
			String key = new String(chars);
			
			// Добавляем строку в соответствующую группу
			groups.computeIfAbsent(key, k -> new ArrayList<>()).add(str);
		}
		
		// Возвращаем все группы
		return new ArrayList<>(groups.values());
	}
	
	public static void main(String... args) {
		System.out.println(
				groupAnagrams(new String[]{"eat","tea","tan","ate","nat","bat"})
		);
	}
}
