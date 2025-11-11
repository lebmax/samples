/**
 * Дано бинарное дерево (не поиска)
 * Найти ближайшего общего предка двух узлов дерева
 * Ограничение по памяти О(1)
 * Самый низкий общий предок определяется между двумя узлами p и q
 * как самый низкий узел в T,
 * который имеет как p, так и q в качестве потомков
 * (где мы разрешаем узлу быть потомком самого себя).
 * Примеры:
 * [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1 -> 3
 * LCA узлов 5 и 1 равен 3.
 * <p>
 * [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 4 -> 5
 * LCA узлов 5 и 4 равен 5, поскольку узел может быть потомком самого себя
 * в соответствии с определением LCA.
 * <p>
 * [1,2], p = 1, q = 2 -> 1
 */
public class LowestCommonAncestor {

    public static void main(String[] args) {
        TreeNode zero = new TreeNode(0);
        TreeNode one = new TreeNode(1);
        TreeNode two = new TreeNode(2);
        TreeNode three = new TreeNode(3);
        TreeNode four = new TreeNode(4);
        TreeNode five = new TreeNode(5);
        TreeNode six = new TreeNode(6);
        TreeNode seven = new TreeNode(7);
        TreeNode eight = new TreeNode(8);

        two.left = seven;
        two.right = four;

        five.left = six;
        five.right = two;

        one.left = zero;
        one.right = eight;

        three.left = five;
        three.right = one;

        System.out.println(lowestCommonAncestor(three, new TreeNode(5), new TreeNode(1)).val);
        System.out.println(lowestCommonAncestor(three, new TreeNode(5), new TreeNode(4)).val);
		
		one.parent = three;
		zero.parent = one;
		eight.parent = one;
		
		five.parent = three;
		six.parent = five;
		two.parent = five;
		
		four.parent = two;
		seven.parent = two;
		
		System.out.println("------ Fast -------");
		System.out.println(lowestCommonAncestorFast(five, one).val);
		System.out.println(lowestCommonAncestorFast(five, four).val);
		System.out.println("------ End -------");
		
        two.left = null;
        two.right = null;

        one.left = two;
        one.right = null;
        System.out.println(lowestCommonAncestor(one, new TreeNode(1), new TreeNode(2)).val);
    }

    private static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root.val == p.val || root.val == q.val) return root;
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        return left == null ? right : right == null ? left : root;
    }
	
	private static TreeNode lowestCommonAncestorFast(TreeNode a, TreeNode b) {
		if (a == null || b == null) {
			return null;
		}
		
		TreeNode pA = a;
		TreeNode pB = b;
		
		// Пока указатели не встретятся
		while (pA != pB) {
			// Если pA достиг корня, переключаем его на b
			pA = (pA == null) ? b : pA.parent;
			// Если pB достиг корня, переключаем его на a
			pB = (pB == null) ? a : pB.parent;
		}
		
		return pA; // или pB, они равны
	}

    private static class TreeNode {
        int val;
		TreeNode parent;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) {
            val = x;
        }
    }
}
