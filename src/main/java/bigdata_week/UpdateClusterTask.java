package bigdata_week;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * В n кластерах расположены серверы, на которых установлена одинаковая операционная система.
 * В её ядре обнаружена уязвимость, поэтому необходимо обновить серверы до новой версии ядра.
 * <p>
 * В i-м кластере расположено xi серверов. Обновление одного сервера занимает одну единицу времени.
 * Обновление серверов происходит последовательно, поэтому общее время обновления всех серверов
 * в i-м кластере составляет xiединиц времени.
 * <p>
 * Процесс обновления в одном кластере нельзя прерывать до тех пор, пока все серверы
 * в нём не будут обновлены. Также нельзя одновременно обновлять серверы в двух кластерах.
 * <p>
 * Для каждого кластера выделено окно времени [ai,ai + xi],
 * в течение которого можно проводить обновление. Эти окна могут пересекаться.
 * <p>
 * Необходимо выбрать кластеры, в которых будет проведено обновление,
 * чтобы на как можно большем количестве серверов была установлена новая версия ядра.
 * <p>
 * Формат ввода
 * В первой строке дано число кластеров n.
 * В каждой из n следующих строк дана пара чисел ai и xi, разделённая пробелом.
 * <p>
 * Формат вывода
 * В первой строке выходного файла нужно вывести максимальное суммарное количество серверов,
 * на которых возможно обновить ядро.
 * Во второй строке выходного файла нужно вывести разделённые пробелом номера кластеров,
 * в которых следует обновить ядро. Кластеры нумеруются с нуля.
 * Номера можно вывести в произвольном порядке.
 * <p>
 * Пример 1
 * Ввод
 * 4
 * 1 4
 * 4 11
 * 8 5
 * 12 5
 * <p>
 * Вывод
 * 11
 * 1
 * <p>
 * Пример 2
 * Ввод
 * 4
 * 1 4
 * 4 11
 * 8 3
 * 12 5
 * <p>
 * Вывод
 * 12
 * 3 2 0
 * <p>
 * Пример 3
 * Ввод
 * 2
 * 1 1
 * 2 1
 * <p>
 * Вывод
 * 2
 * 1 0
 */
public class UpdateClusterTask {
	
	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		
		// Создаем список кластеров с информацией о них
		List<Cluster> clusters = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			int start = scanner.nextInt();
			int servers = scanner.nextInt();
			clusters.add(new Cluster(i, start, servers));
		}
		
		// Сортируем кластеры по времени окончания обновления
		clusters.sort(Comparator.comparingInt(c -> c.end));
		
		// Используем динамическое программирование для поиска оптимального решения
		int[] dp = new int[n];
		int[] prev = new int[n];
		Arrays.fill(prev, -1);
		
		for (int i = 0; i < n; i++) {
			dp[i] = clusters.get(i).servers;
			for (int j = 0; j < i; j++) {
				// Проверяем, не пересекаются ли интервалы обновления
				if (clusters.get(j).end <= clusters.get(i).start) {
					if (dp[j] + clusters.get(i).servers > dp[i]) {
						dp[i] = dp[j] + clusters.get(i).servers;
						prev[i] = j;
					}
				}
			}
		}
		
		// Находим максимальное количество серверов для обновления
		int maxServers = 0;
		int lastCluster = -1;
		for (int i = 0; i < n; i++) {
			if (dp[i] > maxServers) {
				maxServers = dp[i];
				lastCluster = i;
			}
		}
		
		// Формируем список выбранных кластеров
		List<Integer> selectedClusters = new ArrayList<>();
		while (lastCluster != -1) {
			selectedClusters.add(clusters.get(lastCluster).index);
			lastCluster = prev[lastCluster];
		}
		
		// Выводим результат
		System.out.println(maxServers);
		Collections.reverse(selectedClusters);
		for (int cluster : selectedClusters) {
			System.out.print(cluster + " ");
		}
	}
	
	// Класс для представления информации о кластере
	static class Cluster {
		int index;
		int start;
		int servers;
		int end;
		
		public Cluster(int index, int start, int servers) {
			this.index = index;
			this.start = start;
			this.servers = servers;
			this.end = start + servers;
		}
	}
}
