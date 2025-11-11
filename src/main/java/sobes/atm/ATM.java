package sobes.atm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Банкомат.
 * Инициализируется набором купюр и умеет выдавать купюры для заданной суммы,
 * либо отвечать отказом.
 * При выдаче купюры списываются с баланса банкомата.
 * Допустимые номиналы: 50₽, 100₽, 500₽, 1000₽, 5000₽.
 * <p>
 * Другие валюты и номиналы должны легко добавляться разработчиками в будущем.
 * Многопоточные сценарии могут быть добавлены позже (например резервирование).
 */
public class ATM {
	
	private final Balance balance;
	private final List<Integer> correctNominals;
	
	public ATM(Balance balance) {
		this.balance = balance;
		this.correctNominals = new ArrayList<>(Arrays.asList(50, 100, 500, 1000, 5000));
	}
	
	private Map<Integer, Integer> getCells(int sum) {
		Map<Integer, Integer> result = new HashMap<>();
		for (int i = correctNominals.size() - 1; i >= 0; i--) {
			int nominal = correctNominals.get(i);
			if (sum > nominal) {
				int count = sum / nominal;
				if (count > 0) {
					result.put(i, count);
					sum -= nominal * count;
				}
			}
		}
		return result;
	}
	
	public Map<Integer, Integer> canWithdraw(int sum) {
		Map<Integer, Integer> numCells = getCells(sum);
		if (!numCells.isEmpty()) {
			int balanceSum = balance.getSum();
			if (balanceSum < sum || (balanceSum - sum) < 0) {
				return Collections.emptyMap();
			}
		}
		return numCells;
	}
	
	public Result getMoney(int sum) {
		Map<Integer, Integer> numCells = canWithdraw(sum);
		if (!numCells.isEmpty()) {
			balance.decrement(sum);
			return new Result(numCells, sum);
		}
		throw new IllegalStateException("Невозможно выдать");
	}
}
