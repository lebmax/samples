package sobes;

import java.util.Stack;

/**
 * Implement a first in first out (FIFO) queue using only two stacks.
 * The implemented queue should support all the functions
 * of a normal queue (push, peek, pop, and empty).
 */
public class MyQueue {
	
	private final Stack<Integer> inStack;
	private final Stack<Integer> outStack;
	
	public MyQueue() {
		inStack = new Stack<>();
		outStack = new Stack<>();
	}
	
	public void push(int x) {
		inStack.push(x);
	}
	
	public int pop() {
//		return inStack.removeFirst();
		if (outStack.empty()) {
			transfer();
		}
		return outStack.pop();
	}
	
	public int peek() {
//		return inStack.firstElement();
		if (outStack.empty()) {
			transfer();
		}
		return outStack.peek();
	}
	
	public boolean empty() {
		return inStack.empty() && outStack.empty();
	}
	
	private void transfer() {
		while (!inStack.empty()) {
			outStack.push(inStack.pop());
		}
	}
	
	public String print() {
		if (!inStack.empty()) {
			return inStack.toString();
		}
		if (!outStack.empty()) {
			return outStack.toString();
		}
		return "[]";
	}
	
	public static void main(String[] args) {
		MyQueue queue = new MyQueue();
		queue.push(1);
		queue.push(2);
		System.out.println(queue.peek()); // return 1
		System.out.println(queue.print());
		System.out.println(queue.pop()); // return 1, queue is [2]
		System.out.println(queue.print());
		System.out.println(queue.empty()); // return false
	}
}
