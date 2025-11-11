import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Учитывая поток целых чисел и размер окна,
 * вычислите скользящее среднее для всех целых чисел в скользящем окне.
 */
public class MovingAverage {
	
	private int s;
	private int cnt;
	private final int[] data;
	
	public MovingAverage(int size) {
		data = new int[size];
	}
	
	public double next(int val) {
		int i = cnt % data.length;
		s += val - data[i];
		data[i] = val;
		++cnt;
		return s * 1.0 / Math.min(cnt, data.length);
	}
	
	static class MovingAverageQueue {
		private final Deque<Integer> q = new ArrayDeque<>();
		private final int n;
		private int s;
		
		public MovingAverageQueue(int size) {
			n = size;
		}
		
		public double next(int val) {
			if (q.size() == n) {
				s -= q.pollFirst();
			}
			q.offer(val);
			s += val;
			return s * 1.0 / q.size();
		}
	}

	public static void main(String... args) {
		MovingAverage movingAverage = new MovingAverage(3);
		System.out.println(movingAverage.next(1)); // return 1.0 = 1 / 1
		System.out.println(movingAverage.next(10)); // return 5.5 = (1 + 10) / 2
		System.out.println(movingAverage.next(3)); // return 4.66667 = (1 + 10 + 3) / 3
		System.out.println(movingAverage.next(5)); // return 6.0 = (10 + 3 + 5) / 3
		
		System.out.println("With queue");
		MovingAverageQueue movingAverageQueue = new MovingAverageQueue(3);
		System.out.println(movingAverageQueue.next(1));
		System.out.println(movingAverageQueue.next(10));
		System.out.println(movingAverageQueue.next(3));
		System.out.println(movingAverageQueue.next(5));
	}
}
