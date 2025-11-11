package sobes;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Implement a last-in-first-out (LIFO) stack using only one queue.
 * The implemented stack should support all the functions
 * of a normal stack (push, top, pop, and empty).
 */
public class MyStack {
	
	private final Queue<Integer> queue;
	
	public MyStack() {
		queue = new LinkedList<>();
	}
	
	public void push(int x) {
		queue.offer(x); // Add to back
		
		// Rotate the queue: move the first (size - 1) elements to the back
		int size = queue.size();
		for (int i = 0; i < size - 1; i++) {
			queue.offer(queue.poll());
		}
	}
	
	public int pop() {
		return queue.poll(); // Front = top of stack
	}
	
	public int top() {
		return queue.peek();
	}
	
	public boolean empty() {
		return queue.isEmpty();
	}
	
	public static void main(String[] args) {
		MyStack stack = new MyStack();
		stack.push(1);
		stack.push(2);
		System.out.println(stack.top());   // Output: 2
		System.out.println(stack.pop());   // Output: 2
		System.out.println(stack.empty()); // Output: false
	}
}
