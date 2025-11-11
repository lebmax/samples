/**
 * Вы начинаете в точке (0, 0). Доступны 4 типа перемещений:
 * U (x, y) -> (x, y+1)
 * R (x, y) -> (x+1, y)
 * D (x, y) -> (x, y-1)
 * L (x, y) -> (x-1, y)
 * Нельзя использовать все 4 типа перемещений.
 * Можно задействовать не более 3 различных способов (каждый — сколько угодно раз).
 * Даны n точек с целочисленными координатами.
 * Определите, можно ли посетить все точки (в любом порядке),
 * соблюдая ограничение на типы перемещения.
 */
public class PointTraversalChecker {
	
	public static String canVisitAllPoints(int[][] points) {
		boolean allYge0 = true;  // y >= 0 → запретить D
		boolean allYle0 = true;  // y <= 0 → запретить U
		boolean allXge0 = true;  // x >= 0 → запретить L
		boolean allXle0 = true;  // x <= 0 → запретить R
		
		for (int[] p : points) {
			int x = p[0];
			int y = p[1];
			
			if (y < 0) allYge0 = false;
			if (y > 0) allYle0 = false;
			if (x < 0) allXge0 = false;
			if (x > 0) allXle0 = false;
		}
		
		if (allYge0 || allYle0 || allXge0 || allXle0) {
			return "YES";
		} else {
			return "NO";
		}
	}
	
	public static void main(String[] args) {
		int[][] points1 = {{1, 2}, {3, -1}};
		System.out.println(canVisitAllPoints(points1)); // YES
		
		int[][] points2 = {{-1, 2}, {3, -1}};
		System.out.println(canVisitAllPoints(points2)); // NO
	}
}
