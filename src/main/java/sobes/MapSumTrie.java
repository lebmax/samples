package sobes;

import java.util.HashMap;
import java.util.Map;

public class MapSumTrie {
	
	private static class TrieNode {
		Map<Character, TrieNode> children;
		int value;
		int sum; // Сумма всех значений в поддереве
		
		TrieNode() {
			children = new HashMap<>();
			value = 0;
			sum = 0;
		}
	}
	
	private final TrieNode root;
	// Для отслеживания старых значений при обновлении
	private final Map<String, Integer> keyValues;
	
	public MapSumTrie() {
		root = new TrieNode();
		keyValues = new HashMap<>();
	}
	
	public void insert(String key, int val) {
		int oldValue = keyValues.getOrDefault(key, 0);
		int delta = val - oldValue;
		
		TrieNode node = root;
		for (char c : key.toCharArray()) {
			node.children.putIfAbsent(c, new TrieNode());
			node = node.children.get(c);
			node.sum += delta;
		}
		
		node.value = val;
		keyValues.put(key, val);
	}
	
	public int sum(String prefix) {
		TrieNode node = root;
		for (char c : prefix.toCharArray()) {
			if (!node.children.containsKey(c)) {
				return 0;
			}
			node = node.children.get(c);
		}
		return node.sum;
	}
	
	public static void main(String[] args) {
		MapSumTrie mapSum = new MapSumTrie();
		
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
