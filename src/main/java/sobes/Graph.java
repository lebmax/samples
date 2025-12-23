package sobes;

import java.util.*;

class Graph {
	private final int vertices;
	private final List<List<Integer>> adjList;
	
	public Graph(int vertices) {
		this.vertices = vertices;
		this.adjList = new ArrayList<>(vertices);
		for (int i = 0; i < vertices; i++) {
			adjList.add(new ArrayList<>());
		}
	}
	
	public void addEdge(int src, int dest) {
		adjList.get(src).add(dest);
		adjList.get(dest).add(src); // для неориентированного графа
	}
	
	public List<Integer> getAdjList(int vertex) {
		return adjList.get(vertex);
	}
	
	// Обход в ширину (BFS)
	public static void bfs(Graph graph, int startVertex) {
		boolean[] visited = new boolean[graph.vertices];
		Queue<Integer> queue = new LinkedList<>();
		
		visited[startVertex] = true;
		queue.add(startVertex);
		
		while (!queue.isEmpty()) {
			int current = queue.poll();
			System.out.print(current + " ");
			
			for (int neighbor : graph.getAdjList(current)) {
				if (!visited[neighbor]) {
					visited[neighbor] = true;
					queue.add(neighbor);
				}
			}
		}
		System.out.println();
	}
	
	// Обход в глубину (DFS) — итеративный вариант
	public static void dfsIterative(Graph graph, int startVertex) {
		boolean[] visited = new boolean[graph.vertices];
		Stack<Integer> stack = new Stack<>();
		
		stack.push(startVertex);
		
		while (!stack.isEmpty()) {
			int current = stack.pop();
			if (!visited[current]) {
				System.out.print(current + " ");
				visited[current] = true;
				
				// Добавляем соседей в обратном порядке, чтобы сохранить порядок обхода как в рекурсии
				List<Integer> neighbors = graph.getAdjList(current);
				for (int i = neighbors.size() - 1; i >= 0; i--) {
					int neighbor = neighbors.get(i);
					if (!visited[neighbor]) {
						stack.push(neighbor);
					}
				}
			}
		}
		System.out.println();
	}
	
	// Обход в глубину (DFS) — рекурсивный вариант
	public static void dfsRecursive(Graph graph, int vertex, boolean[] visited) {
		visited[vertex] = true;
		System.out.print(vertex + " ");
		
		for (int neighbor : graph.getAdjList(vertex)) {
			if (!visited[neighbor]) {
				dfsRecursive(graph, neighbor, visited);
			}
		}
	}
	
	// Обёртка для вызова рекурсивного DFS
	public static void dfs(Graph graph, int startVertex) {
		boolean[] visited = new boolean[graph.vertices];
		dfsRecursive(graph, startVertex, visited);
		System.out.println();
	}
	
	public static void main(String[] args) {
		Graph graph = new Graph(6);
		graph.addEdge(0, 1);
		graph.addEdge(0, 2);
		graph.addEdge(1, 3);
		graph.addEdge(1, 4);
		graph.addEdge(2, 5);
		
		System.out.print("BFS: ");
		bfs(graph, 0); // Вывод: 0 1 2 3 4 5
		
		System.out.print("DFS (recursive): ");
		dfs(graph, 0); // Вывод: 0 1 3 4 2 5
		
		System.out.print("DFS (iterative): ");
		dfsIterative(graph, 0); // Вывод может отличаться в зависимости от порядка добавления соседей
	}
}
