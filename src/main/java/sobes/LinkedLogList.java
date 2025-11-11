package sobes;

public class LinkedLogList {
	
	ListNode node = null;
	ListNode lastNode = null;
	
	public void add(int val) {
		if (node == null) {
			node = new ListNode(val);
			lastNode = node;
		} else {
			lastNode.next = new ListNode(val);
			lastNode = lastNode.next;
		}
	}
	
	public LinkedLogList revert() {
		LinkedLogList reversedList = new LinkedLogList();
		ListNode current = new ListNode(node.val, node.next);
		while (current != null) {
			ListNode newNode = new ListNode(current.val);
			if (reversedList.node == null) {
				reversedList.node = newNode;
			} else {
				newNode.next = reversedList.node;
				reversedList.node = newNode;
			}
			current = current.next;
		}
		
		return reversedList;
	}

	public static void main(String[] args) {
		LinkedLogList linkedLogList = new LinkedLogList();
		linkedLogList.add(1);
		linkedLogList.add(2);
		linkedLogList.add(3);
		linkedLogList.add(4);
		
		System.out.println("List");
		ListNode node = linkedLogList.node;
		while (node != null) {
			System.out.println(node.val);
			node = node.next;
		}
		
		System.out.println();
		System.out.println("Reverted list");
		LinkedLogList revertedList = linkedLogList.revert();
		node = revertedList.node;
		while (node != null) {
			System.out.println(node.val);
			node = node.next;
		}
	}
}
