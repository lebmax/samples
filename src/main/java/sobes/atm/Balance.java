package sobes.atm;

public class Balance {
	
	private int sum = 0;
	
	public Balance(int beginSum) {
		this.sum = beginSum;
	}
	
	public int getSum() {
		return sum;
	}
	
	public void decrement(int s) {
		sum = sum - s;
	}
}
