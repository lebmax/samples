package bigdata_week;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * В сети компании есть несколько серверов, каждый из которых имеет свой уникальный номер.
 * <p>
 * Некоторые серверы соединены между собой, образуя группы, или кластеры.
 * Два сервера относятся к одному кластеру, если между ними есть связь.
 * <p>
 * Иногда нужно скачать файл на определённый сервер. В сети есть сервис,
 * похожий на торрент-трекер, который может показать, на каких серверах есть нужный файл.
 * <p>
 * Проблема в том, что сервер может скачивать файлы только с серверов из своего кластера.
 * <p>
 * Напишите программу, которая будет анализировать структуру сети и определять,
 * откуда сервер может скачать нужный файл.
 * <p>
 * Формат ввода
 * Первая строка содержит целое число N — количество связей между серверами.
 * Следующие N строк описывают связи между серверами.
 * Каждая из них содержит целые числа Ai и Bi — идентификаторы соединённых серверов.
 * У каждого сервера свой уникальный идентификатор.
 * <p>
 * Следующая строка содержит целое число Q — количество запросов на скачивание файлов.
 * Следующие 2Q строк описывают запросы на скачивание файлов.
 * Первая строка каждой пары содержит целые числа Xi и Ki — соответственно идентификатор сервера,
 * на который нужно скачать файл, и количество серверов, содержащих файл.
 * Вторая строка каждой пары содержит Ki целых чисел Yij — идентификаторы серверов, содержащих файл.
 * <p>
 * Формат вывода
 * Для каждого запроса в отдельной строке выведите сначала целое число Rj — количество серверов,
 * с которых можно скачать файл. Затем выведите Rj целых чисел — идентификаторы серверов,
 * с которых можно скачать файл.
 * <p>
 * Серверы следует выводить в том порядке, в котором они перечислены
 * в описании соответствующего запроса во входных данных.
 * <p>
 * Пример 1
 * Ввод
 * 8
 * 54578972 99368556
 * 79699669 54578972
 * 64508306 99368556
 * 56554555 64508306
 * 27101564 81480071
 * 89445516 27101564
 * 93762227 81480071
 * 89808815 93762227
 * 4
 * 56554555 2
 * 93762227 79699669
 * 99368556 2
 * 64508306 56554555
 * 27101564 2
 * 99368556 79699669
 * 93762227 2
 * 56554555 54578972
 * <p>
 * Вывод
 * 1 79699669
 * 2 64508306 56554555
 * 0
 * 0
 * <p>
 * Пример 2
 * Ввод
 * 10
 * 85619126 64616465
 * 98159933 85619126
 * 73978229 85619126
 * 29978081 64616465
 * 72404745 29978081
 * 97698445 75243921
 * 36815728 97698445
 * 18360411 97698445
 * 66445821 75243921
 * 92142380 66445821
 * 4
 * 97698445 4
 * 75243921 92142380 98159933 73978229
 * 66445821 4
 * 29978081 92142380 73978229 97698445
 * 18360411 4
 * 29978081 92142380 98159933 97698445
 * 36815728 4
 * 64616465 92142380 97698445 29978081
 * <p>
 * Вывод
 * 2 75243921 92142380
 * 2 92142380 97698445
 * 2 92142380 97698445
 * 2 92142380 97698445
 */
public class NetworkAnalyzer {
	
	static class DisjointSet {
		private final Map<Integer, Integer> parent = new HashMap<>();
		private final Map<Integer, Integer> rank = new HashMap<>();
		
		public void makeSet(int server) {
			parent.put(server, server);
			rank.put(server, 0);
		}
		
		public int find(int server) {
			if (!parent.containsKey(server)) {
				return -1;
			}
			if (parent.get(server) != server) {
				parent.put(server, find(parent.get(server)));
			}
			return parent.get(server);
		}
		
		public void union(int server1, int server2) {
			int root1 = find(server1);
			int root2 = find(server2);
			if (root1 != root2) {
				if (rank.get(root1) > rank.get(root2)) {
					parent.put(root2, root1);
				} else if (rank.get(root1) < rank.get(root2)) {
					parent.put(root1, root2);
				} else {
					parent.put(root2, root1);
					rank.put(root1, rank.get(root1) + 1);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		// Читаем связи между серверами
		int n = scanner.nextInt();
		NetworkAnalyzer.DisjointSet ds = new NetworkAnalyzer.DisjointSet();
		
		for (int i = 0; i < n; i++) {
			int server1 = scanner.nextInt();
			int server2 = scanner.nextInt();
			
			// Создаем множества для новых серверов
			if (!ds.parent.containsKey(server1)) ds.makeSet(server1);
			if (!ds.parent.containsKey(server2)) ds.makeSet(server2);
			
			ds.union(server1, server2);
		}
		
		// Читаем запросы
		int q = scanner.nextInt();
		
		for (int i = 0; i < q; i++) {
			int targetServer = scanner.nextInt();
			int fileServersCount = scanner.nextInt();
			
			List<Integer> availableServers = new ArrayList<>();
			
			for (int j = 0; j < fileServersCount; j++) {
				int fileServer = scanner.nextInt();
				
				if (ds.find(targetServer) == ds.find(fileServer)) {
					availableServers.add(fileServer);
				}
			}
			
			System.out.println(availableServers.size());
			for (int server : availableServers) {
				System.out.print(" " + server);
			}
			System.out.println();
		}
		
		scanner.close();
	}
}
