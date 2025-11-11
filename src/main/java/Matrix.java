import java.io.*;

public class Matrix {

	/**
	 * Вам дано целое неотрицательное число nn.
	 * Сгенерируйте матрицу размера (2n + 1) \times (2n + 1)(2n+1)×(2n+1), что:
	 * Клетки раскрашиваются в шахматном порядке, все углы черные.
	 * На главной диагонали стоят нули.
	 * В белые клетки последовательно записываются целые положительные числа, начиная с 11,
	 * проходя последовательно по строкам от верхней до нижней,
	 * и по столбцам от левого до правого.
	 * В пустые черные клетки последовательно записываются целые отрицательные числа,
	 * начиная с -1−1, проходя последовательно по столбцам от левого до правого,
	 * и по строкам от верхней до нижней.
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

		final int n = Integer.parseInt(reader.readLine());
		final int len = 2 * n + 1;
		int[][] matrix = new int[len][len];

		boolean black = true;
		int row = 1;
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				if (i == j || black) {
					matrix[i][j] = 0;
					black = false;
				} else {
					matrix[i][j] = row;
					row++;
					black = true;
				}
			}
		}

		int col = -1;
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				final int cell = matrix[j][i];
				if (i != j && cell == 0) {
					matrix[j][i] = col;
					col--;
				}
			}
		}

		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				if (j == len - 1) {
					writer.write(Integer.toString(matrix[i][j]));
				} else {
					writer.write(matrix[i][j] + " ");
				}
			}
			writer.newLine();
		}

		reader.close();
		writer.close();
	}
}