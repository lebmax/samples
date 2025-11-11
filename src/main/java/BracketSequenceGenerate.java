public class BracketSequenceGenerate {

	public static void main(String[] args) throws Exception {
		System.out.println(generateBrackets(3));
		System.out.println();
		System.out.println(generateBrackets(5));
	}
	
	private static String generateBrackets(int nun) {
		int n = nun * 2;
		if (n < 2) return "";
		StringBuilder sb = new StringBuilder();
		char[] chrs = new char[n];
		for (int i = 0; i < n/2; i++) {
			chrs[i] = '(';
		}
		for (int i = n/2; i < n; i++) {
			chrs[i] = ')';
		}
		sb.append(chrs).append("\n");
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
			sb.append(chrs).append("\n");
		} while (true);
		
		return sb.toString();
	}
}
