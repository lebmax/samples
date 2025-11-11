package sobes;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class BinaryTreeCodec {
	
	public String serialize(TreeNode root) {
		if (root == null) return "null";
		return root.val + "," + serialize(root.left) + "," + serialize(root.right);
	}
	
	public TreeNode deserialize(String data) {
		Queue<String> q = new LinkedList<>(Arrays.asList(data.split(",")));
		return buildTree(q);
	}
	
	private TreeNode buildTree(Queue<String> q) {
		String val = q.poll();
		if (val == null || val.isEmpty() || val.equals("null")) return null;
		TreeNode node = new TreeNode(Integer.parseInt(val));
		node.left = buildTree(q);
		node.right = buildTree(q);
		return node;
	}
}
