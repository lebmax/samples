package sobes.atm;

import java.util.HashMap;
import java.util.Map;

public class Bankomat {
	
	// Номиналы купюр
	private static final int[] DENOMINATIONS = {5000, 1000, 500, 100, 50};
	
	// Хранилище купюр
	private final Map<Integer, Integer> banknotes = new HashMap<>();
	
	// Общий баланс банкомата
	private long totalBalance;
	
	public Bankomat(Map<Integer, Integer> initialBanknotes) {
		for (int denomination : DENOMINATIONS) {
			banknotes.put(denomination, initialBanknotes.getOrDefault(denomination, 0));
			totalBalance += (long) denomination * banknotes.get(denomination);
		}
	}
	
	public Map<Integer, Integer> withdraw(int amount) {
		if (amount > totalBalance) {
			throw new IllegalArgumentException("Недостаточно средств в банкомате");
		}
		
		if (amount % 50 != 0) {
			throw new IllegalArgumentException("Сумма должна быть кратна 50");
		}
		
		Map<Integer, Integer> result = new HashMap<>();
		
		for (int denomination : DENOMINATIONS) {
			if (amount >= denomination && banknotes.get(denomination) > 0) {
				int count = Math.min(amount / denomination, banknotes.get(denomination));
				result.put(denomination, count);
				amount -= denomination * count;
				banknotes.put(denomination, banknotes.get(denomination) - count);
				totalBalance -= (long) denomination * count;
			}
		}
		
		if (amount > 0) {
			rollback(result);
			throw new IllegalArgumentException("Невозможно выдать указанную сумму имеющимися купюрами");
		}
		
		return result;
	}
	
	private void rollback(Map<Integer, Integer> result) {
		for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
			banknotes.put(entry.getKey(), banknotes.get(entry.getKey()) + entry.getValue());
			totalBalance += (long) entry.getKey() * entry.getValue();
		}
	}
	
	public long getBalance() {
		return totalBalance;
	}
	
	public Map<Integer, Integer> getBanknotes() {
		return banknotes;
	}
	
	public static void main(String[] args) {
		Map<Integer, Integer> initialBanknotes = new HashMap<>();
		initialBanknotes.put(5000, 5);
		initialBanknotes.put(1000, 10);
		initialBanknotes.put(500, 15);
		initialBanknotes.put(100, 20);
		initialBanknotes.put(50, 30);
		
		Bankomat atm = new Bankomat(initialBanknotes);
		
		try {
			Map<Integer, Integer> result = atm.withdraw(850);
			System.out.println("Выдано: " + result);
			System.out.println("Оставшийся баланс: " + atm.getBalance());
		} catch (IllegalArgumentException e) {
			System.out.println("Ошибка: " + e.getMessage());
		}
	}
}
