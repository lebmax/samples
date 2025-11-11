/**
 * Длинное умножение на разряд
 * <p>
 * Дана строка из десятичных цифр (длинное число, младшие разряды расположены по младшему индексу).
 * Написать код, который умножит это число на число 1 <= n <= 9.
 * Ограничения по памяти: O(1) дополнительной памяти, т.е. надо использовать исходную строку
 * (считаем, что возможное увеличение длины на 1 разряд не приведёт к реаллокации).
 */
public class Multiply {
	
	public static void main(String[] args) {
		System.out.println(multiply("2", "3"));
		System.out.println(multiply("123", "456"));
		System.out.println(multiply("99", "99"));
	}
	
	public static String multiply(String num1, String num2) {
		if (num1.equals("0") || num2.equals("0")) {
			return "0";
		}
		int m = num1.length();
		int n = num2.length();
		int[] result = new int[m + n];
		
		for (int i = m - 1; i >= 0; i--) {
			int i1 = num1.charAt(i) - '0';
			for (int j = n - 1; j >= 0; j--) {
				int i2 = num2.charAt(j) - '0';
				int mul = i1 * i2;
				int p1 = i + j;
				int p2 = i + j + 1;
				int sum = mul + result[p2];
				
				result[p1] += sum / 10;
				result[p2] = (sum) % 10;
			}
		}
		
		StringBuilder sb = new StringBuilder();
		for (int p : result) {
			if (!(sb.isEmpty() && p == 0)) {
				sb.append(p);
			}
		}
		return sb.isEmpty() ? "0" : sb.toString();
	}
}
