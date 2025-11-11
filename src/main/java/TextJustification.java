import java.util.ArrayList;
import java.util.List;

/**
 * Дан массив строк (слов) и максимальная ширина maxWidth.
 * Вам необходимо отформатировать текст таким образом,
 * чтобы каждая строка содержала ровно maxWidth символов,
 * и была полностью выровнена по ширине (слева и справа)
 * <p>
 * При необходимости нужно добавлять дополнительные пробелы,
 * чтобы в каждой строке было ровно maxWidth символов.
 * Они должны быть распределены как можно более равномерно.
 * Если количество пробелов в строке неравномерно распределяется между словами,
 * то слева будет больше пробелов, чем справа.
 * <p>
 * Последняя строка должна быть выровнена по левому краю,
 * а между словами не должно быть лишних пробелов (только для последней строки)
 * <p>
 * Пример:
 * words = ["What","must","be","acknowledgment","shall","be"]
 * maxWidth = 16
 * Output:
 * [
 * "What   must   be",
 * "acknowledgment  ",
 * "shall be        "
 * ]
 */
public class TextJustification {
	
	private static List<String> fullJustify(String[] words, int maxWidth) {
		List<String> result = new ArrayList<>();
		int n = words.length;
		int i = 0;
		
		while (i < n) {
			// Шаг 1: определяем, какие слова помещаются в текущую строку
			int j = i;
			int currentLength = 0;
			
			// Считаем, сколько слов можно поместить в строку
			// Здесь (j - i) — это минимальное количество пробелов между словами
			while (j < n && currentLength + words[j].length() + (j - i) <= maxWidth) {
				currentLength += words[j].length();
				j++;
			}
			
			// Шаг 2: форматируем строку
			String line;
			if (j == n) { // Последняя строка - выравнивание по левому краю
				line = leftJustify(words, i, j, maxWidth);
			} else { // Обычная строка - выравнивание по ширине
				line = fullJustifyLine(words, i, j, maxWidth, currentLength);
			}
			
			result.add(line);
			i = j;
		}
		
		return result;
	}
	
	// Выравнивание по левому краю (для последней строки)
	private static String leftJustify(String[] words, int start, int end, int maxWidth) {
		StringBuilder sb = new StringBuilder();
		
		// Добавляем слова через один пробел
		for (int i = start; i < end; i++) {
			sb.append(words[i]);
			if (i < end - 1) {
				sb.append(" ");
			}
		}
		
		// Добавляем оставшиеся пробелы в конец
		while (sb.length() < maxWidth) {
			sb.append(" ");
		}
		
		return sb.toString();
	}
	
	// Полное выравнивание по ширине
	private static String fullJustifyLine(String[] words, int start, int end,
										  int maxWidth, int totalWordsLength) {
		int numWords = end - start;
		int totalSpaces = maxWidth - totalWordsLength;
		
		// Если только одно слово в строке
		if (numWords == 1) {
			return words[start] + " ".repeat(totalSpaces);
		}
		
		int numGaps = numWords - 1;
		int spacesPerGap = totalSpaces / numGaps;
		// дополнительные пробелы для левых промежутков
		int extraSpaces = totalSpaces % numGaps;
		
		StringBuilder sb = new StringBuilder();
		
		for (int i = start; i < end; i++) {
			sb.append(words[i]);
			
			// Не добавляем пробелы после последнего слова
			if (i < end - 1) {
				// Основное количество пробелов
				sb.append(" ".repeat(spacesPerGap));
				
				// Дополнительные пробелы (распределяем слева направо)
				if (extraSpaces > 0) {
					sb.append(" ");
					extraSpaces--;
				}
			}
		}
		
		return sb.toString();
	}
	
	// Тестовый метод
	public static void main(String[] args) {
		String[] words = {"What", "must", "be", "acknowledgment", "shall", "be"};
		int maxWidth = 16;
		
		List<String> result = fullJustify(words, maxWidth);
		
		for (String line : result) {
			System.out.println("\"" + line + "\"");
		}
	}
}
