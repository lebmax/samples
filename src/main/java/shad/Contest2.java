package shad;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Contest2 {
	
	/**
	 * Напиши алгоритм на java для задачи, описанной ниже
	 * <p>
	 * Строка является симметричной, если ее можно одним разрезом между символами
	 * разделить на две части L=S[0:i] и R=S[i:∣S∣],
	 * при этом набор символов в L совпадает с набором символов в R без учета кратности.
	 * Использована нотация из языка Python, где S[i:j] обозначает подстроку Si…Sj−1.
	 * <p>
	 * Алексею досталась строка S. Ему интересно, является ли она симметричной.
	 * <p>
	 * Формат ввода
	 * На первой строке дана строка S из букв a-z.
	 * <p>
	 * Формат вывода
	 * Выведите YES, если строка симметрична и NO иначе.
	 * <p>
	 * Пример 1
	 * Ввод
	 * abacaba
	 * <p>
	 * Вывод
	 * NO
	 * <p>
	 * Пример 2
	 * Ввод
	 * ababa
	 * <p>
	 * Вывод
	 * YES
	 * <p>
	 * Пример 3
	 * Ввод
	 * abcccabab
	 * <p>
	 * Вывод
	 * YES
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		String input = scanner.nextLine();
		
		if (isSymmetricOptimized(input)) {
			System.out.println("YES");
		} else {
			System.out.println("NO");
		}
	}
	
	private static boolean isSymmetricOptimized(String s) {
		int length = s.length();
		
		int middleIndex = length / 2;
		
		Set<Character> leftSet = new HashSet<>();
		Set<Character> rightSet = new HashSet<>();
		
		for (int i = 0; i < length; i++) {
			char c = s.charAt(i);
			if (i >= middleIndex) {
				rightSet.add(c);
			} else {
				leftSet.add(c);
			}
		}
		
		return leftSet.equals(rightSet);
		
//		int length = s.length();
//
//		for (int i = 1; i < length; i++) {
//			String left = s.substring(0, i);
//			String right = s.substring(i);
//
//			Set<Character> leftSet = new HashSet<>();
//			Set<Character> rightSet = new HashSet<>();
//
//			for (int j = 0; j < left.length(); j++) {
//				leftSet.add(left.charAt(j));
//			}
//
//			for (int j = 0; j < right.length(); j++) {
//				rightSet.add(right.charAt(j));
//			}
//
//			if (leftSet.equals(rightSet)) {
//				return true;
//			}
//		}
//
//		return false;
	}
}
