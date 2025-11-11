package sobes.atm;

import java.util.Map;

public class Result {
	
	private final Map<Integer, Integer> numCells;
	private final int sum;
	
	public Result(Map<Integer, Integer> numCells, int sum) {
		this.numCells = numCells;
		this.sum = sum;
	}
	
	public Map<Integer, Integer> getNumCells() {
		return numCells;
	}
	
	public int getSum() {
		return sum;
	}
}
