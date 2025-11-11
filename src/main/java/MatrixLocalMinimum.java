/**
 * Дана матрица nxn, содержащая попарно различные натуральные числа.
 * Требуется найти в ней локальный минимум за время O(n).
 */
public class MatrixLocalMinimum {
	
	public static void main(String[] args) {
		int[][] matrix = {
				{9, 8, 7, 6},
				{10, 1, 5, 4},
				{11, 2, 3, 2},
				{12, 3, 4, 1}
		};
		
		System.out.println("Локальный минимум: " + findLocalMinimum(matrix)); // Выведет 1
	}

	public static Integer findLocalMinimum(int[][] matrix) {
		return findLocalMinimum(matrix, 0, matrix[0].length - 1);
	}
	
	private static Integer findLocalMinimum(int[][] matrix, int left, int right) {
		if (left > right) {
			return null;
		}
		
		int mid = (left + right) / 2;
		int minRow = findMinRow(matrix, mid);
		
		// Проверяем, является ли элемент локальным минимумом
		if (isLocalMinimum(matrix, minRow, mid)) {
			return matrix[minRow][mid];
		}
		
		// Рекурсивно ищем в левом или правом подмножестве
		if (mid > 0 && matrix[minRow][mid - 1] < matrix[minRow][mid]) {
			return findLocalMinimum(matrix, left, mid - 1);
		}
		return findLocalMinimum(matrix, mid + 1, right);
	}
	
	private static int findMinRow(int[][] matrix, int col) {
		int minRow = 0;
		for (int row = 1; row < matrix.length; row++) {
			if (matrix[row][col] < matrix[minRow][col]) {
				minRow = row;
			}
		}
		return minRow;
	}
	
	private static boolean isLocalMinimum(int[][] matrix, int row, int col) {
		int val = matrix[row][col];
		return (row == 0 || val < matrix[row - 1][col]) &&
				(row == matrix.length - 1 || val < matrix[row + 1][col]) &&
				(col == 0 || val < matrix[row][col - 1]) &&
				(col == matrix[0].length - 1 || val < matrix[row][col + 1]);
	}
}
