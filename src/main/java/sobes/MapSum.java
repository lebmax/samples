package sobes;

import java.util.HashMap;
import java.util.Map;

public class MapSum {
	// Основная хэш-таблица для хранения пар ключ-значение
	private final Map<String, Integer> map;
	
	// Префиксная сумма:
	// для каждого префикса храним сумму значений всех ключей, начинающихся с него
	private final Map<String, Integer> prefixSum;
	
	public MapSum() {
		map = new HashMap<>();
		prefixSum = new HashMap<>();
	}
	
	public void insert(String key, int val) {
		// Получаем предыдущее значение (если ключ существует)
		int oldValue = map.getOrDefault(key, 0);
		
		// Обновляем значение в основной мапе
		map.put(key, val);
		
		// Обновляем префиксные суммы для всех префиксов ключа
		for (int i = 1; i <= key.length(); i++) {
			String prefix = key.substring(0, i);
			// Добавляем новое значение и вычитаем старое (если ключ существовал)
			prefixSum.put(prefix, prefixSum.getOrDefault(prefix, 0) - oldValue + val);
		}
	}
	
	public int sum(String prefix) {
		return prefixSum.getOrDefault(prefix, 0);
	}
	
	public static void main(String[] args) {
		MapSum mapSum = new MapSum();
		
		mapSum.insert("apple", 3);
		System.out.println(mapSum.sum("ap")); // 3
		
		mapSum.insert("app", 2);
		System.out.println(mapSum.sum("ap")); // 5
		
		mapSum.insert("apple", 5); // Обновление существующего ключа
		System.out.println(mapSum.sum("ap")); // 7 (5 + 2)
		
		// Дополнительные тесты
		mapSum.insert("banana", 10);
		System.out.println(mapSum.sum("ba")); // 10
		System.out.println(mapSum.sum("b"));  // 10
		System.out.println(mapSum.sum("c"));  // 0
	}
}
