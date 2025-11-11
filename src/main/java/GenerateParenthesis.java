import java.util.ArrayList;
import java.util.List;

public class GenerateParenthesis {
	
	public static void main(String[] args) {
		final List<String> res = generateParenthesis(3);
		for (String s : res) {
			System.out.print(s);
		}
	}

	public static List<String> generateParenthesis(int len) {
		int n = len * 2;
		char[] chrs = new char[n];
		List<String> res = new ArrayList<>(n);
		for (int i = 0; i < len; i++) {
			chrs[i] = '(';
		}
		for (int i = len; i < n; i++) {
			chrs[i] = ')';
		}
		res.add(new String(chrs));
		do {
			int i = n - 1;
			int c = 0;
			while (i >= 0) {
				c += chrs[i] == ')' ? -1 : 1;
				if ((c < 0) && (chrs[i] == '(')) {
					break;
				}
				--i;
			}
			if (i < 0) {
				break;
			}
			
			chrs[i++] = ')';
			int ind = i;
			for (; i < n; i++) {
				chrs[i] = (i <= (n - ind + c) / 2 + ind) ? '(' : ')';
			}
			res.add(new String(chrs));
		} while (true);
		return res;
	}
}
