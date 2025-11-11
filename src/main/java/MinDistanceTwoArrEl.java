import java.util.Arrays;

/**
 * Даны два массива a и b. Нужно найти минимум abs(a[i]-b[j]):
 * Примеры:
 * minDistance({0}, {1}) == 1
 * minDistance({10, 0, 2}, {5, 100, 12}) == 2
 * </p>
 * findTheDistanceValue([4,5,8], [10,9,1,8], 2) == 2
 * findTheDistanceValue([1,4,2,3], [-4,-3,6,10,20,30], 3) == 2
 * findTheDistanceValue([2,1,100,3], [-5,-2,10,-3,7], 6) == 1
 */
public class MinDistanceTwoArrEl {
	
	public static void main(String[] args) {
		System.out.println(minDistance(new int[]{0}, new int[]{1}));
		System.out.println(minDistance(new int[]{0, 2, 10}, new int[]{5, 12, 100}));
		
		System.out.println(findTheDistanceValue(new int[]{4, 5, 8}, new int[]{10, 9, 1, 8}, 2));
		System.out.println(findTheDistanceValue(new int[]{1, 4, 2, 3}, new int[]{-4, -3, 6, 10, 20, 30}, 3));
		System.out.println(findTheDistanceValue(new int[]{2, 1, 100, 3}, new int[]{-5, -2, 10, -3, 7}, 6));
	}
	
	private static int minDistance(int[] a1, int[] a2) {
		Arrays.sort(a1);
		Arrays.sort(a2);
		
		int min = Math.abs(a1[0] - a2[0]);
		int j = 0;
		
		for (int i = 0; i < a1.length; ) {
			if (j < a2.length) {
				min = Math.min(min, Math.abs(a1[i] - a2[j]));
				if (a1[i] > a2[j]) {
					j++;
				} else {
					i++;
				}
				if (i < a1.length && j < a2.length) {
					min = Math.min(min, Math.abs(a1[i] - a2[j]));
				}
			}
		}
		return min;
	}
	
	public static int findTheDistanceValue(int[] a1, int[] a2, int d) {
		Arrays.sort(a1);
		Arrays.sort(a2);
		
		int res = 0;
        int j = 0;
		for (int i = 0; i < a1.length; i++) {
            if (j < a2.length) {
                int diff = Math.abs(a1[i + 1] - a2[i]);
                if (a1[i] > a2[j]) {
                    j++;
                } else {
                    i++;
                }
                if (i < a1.length && j < a2.length) {
                    diff = Math.abs(a1[i] - a2[j]);
                }
                if (diff <= d) {
                    res++;
                }
            }
		}
		return res;
	}
}
