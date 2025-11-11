import java.util.*;

/**
 * Дана скобочная грамматика: в круглых скобках записано выражение,
 * следом за ними идут квадратные скобки с неотрицательным целым числом.
 * Разворачивается в виде конкатенации строки в круглых скобках на саму себя N раз,
 * где N - число в квадратных скобках
 * <p>
 * Чуть более формально, грамматика -
 * term ::= a-z*
 * term ::= (term)[\d+]
 * term ::= term1term2 - разворачивается в конкатенацию двух выражений
 * <p>
 * На вход подается строка со скобочным выражением.
 * Необходимо развернуть его в строку и вернуть.
 * Предлагается считать, что скобочная последовательность на входе всегда корректна
 * <p>
 * Примеры:
 * "ab" -> "ab"
 * "(ab)[3]" -> "ababab"
 * "((ab)[2])[2]" -> "abababab"
 * "(a)[2](b)[2]" -> "aabb"
 * "()[1]bc" -> "bc"
 */
public class BraceGrammar {
	
	public String expandExpression(String s) {
		Stack<Character> stack = new Stack<>();
		
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			
			if (c == '(') {
				stack.push('(');
			} else if (c == ')') {
				// Извлекаем все элементы до открывающей скобки
				StringBuilder content = new StringBuilder();
				while (!stack.isEmpty() && stack.peek() != '(') {
					content.insert(0, stack.pop());
				}
				stack.pop(); // Убираем "("
				for (int ind = 0; i < content.length(); ind++) {
					stack.push(content.charAt(ind));
				}
			} else if (c == ']') {
				// Извлекаем число и строку для повторения
				StringBuilder numberStr = new StringBuilder();
				while (!stack.isEmpty() && Character.isDigit(stack.peek())) {
					numberStr.insert(0, stack.pop());
				}
				
				if (numberStr.isEmpty()) {
					numberStr.append('1'); // По умолчанию 1 раз
				}
				
				int repeatCount = Integer.parseInt(numberStr.toString());
				Character content = stack.pop();
				
				for (int j = 0; j < repeatCount; j++) {
					stack.push(content);
				}
			} else if (Character.isDigit(c)) {
				stack.push(c);
			} else if (c != '[') {
				// Обычный символ
				stack.push(c);
			}
		}
		
		// Собираем финальный результат из стека
		StringBuilder result = new StringBuilder();
		while (!stack.isEmpty()) {
			result.insert(0, stack.pop());
		}
		
		return result.toString();
	}
	
	public String evaluate(String s) {
		return parse(s, new int[]{0});
	}
	
	private String parse(String s, int[] inicies) {
		StringBuilder res = new StringBuilder();
		while (inicies[0] < s.length() && s.charAt(inicies[0]) != ')' && s.charAt(inicies[0]) != ']') {
			
			if (s.charAt(inicies[0]) == '(') {
				inicies[0]++;
				String inner = parse(s, inicies);
				inicies[0]++; // skip ')'
				int n = 0;
				inicies[0]++; // skip '['
				while (Character.isDigit(s.charAt(inicies[0]))) {
					n = n * 10 + (s.charAt(inicies[0]++) - '0');
				}
				inicies[0]++; // skip ']'
				while (n-- > 0) {
					res.append(inner);
				}
			} else {
				res.append(s.charAt(inicies[0]++));
			}
		}
		return res.toString();
	}
	
	// Точка входа для тестирования
	public static void main(String[] args) {
		BraceGrammar evaluator = new BraceGrammar();
		
		// Тесты
		System.out.println("-------- First ----------");
		System.out.println(evaluator.evaluate("ab"));            // "ab"
		System.out.println(evaluator.evaluate("(ab)[3]"));       // "ababab"
		System.out.println(evaluator.evaluate("((ab)[2])[2]"));  // "abababab"
		System.out.println(evaluator.evaluate("(a)[2](b)[2]"));  // "aabb"
		System.out.println(evaluator.evaluate("()[1]bc"));       // "bc"
		
		System.out.println("-------- Second ----------");
		System.out.println(evaluator.expandExpression("ab"));            // "ab"
		System.out.println(evaluator.expandExpression("(ab)[3]"));       // "ababab"
		System.out.println(evaluator.expandExpression("((ab)[2])[2]"));  // "abababab"
		System.out.println(evaluator.expandExpression("(a)[2](b)[2]"));  // "aabb"
		System.out.println(evaluator.expandExpression("()[1]bc"));       // "bc"
	}
}
