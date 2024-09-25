/*
 * *** Mariano Garcia Melo / Section 001 ***
 *
 * Homework # 2 (Programming Assignment). This Java class defines a few basic
 * manipulation operations of a binary trees.
 *
 * ONLY MODIFY THIS FILE (NOT 'Main.Java')
 */

import java.util.Queue;
import java.util.LinkedList;

/*
 * Class BinaryTree
 *
 * This class defines a binary tree object; it is a tree structure where every
 * node as at most two child nodes, which form the tree branches. That implies
 * that each node within the tree has a degree of 0, 1, or 2. A node of degree
 * zero (0) is called a terminal node, or leaf node.
 *
 * Each non-leaf node is often called a branch node, which will have either one or
 * two children (a left and right child node). There is no order guarantee within
 * this basic binary tree object. Given that this binary object is NOT a Binary Search Tree (BST), there is
 * no guarantee on order in the tree.
 *
 * As just stated, the insert method does NOT guarantee the order within the tree, but
 * its logic attempts to follow the rules of BSTs -- meaning the insert method will traverse
 * the binary tree searching for a location to insert the new Node using traversal
 * logic similar to BSTs. But again, this is not a BST, so there is no guarantee that
 * the tree's order maintains that defined by a BST.
 *
 * Public methods:
 *  void deleteTree()      - deletes the tree.
 *  Node insert(int data)  - inserts a new node into the tree containing value 'data'.
 *  String preOrder()      - return the tree in 'preorder' traversal in a String object.
 *
 * The following methods you will complete:
 *  void replaceValue(int k, int l) - if data value 'k' is in tree, replace with data
 *                           value 'l'; for simplicity at the moment, do not re-organize
 *                           the tree based on new value which means this operation may
 *                           violate the binary tree definition.
 *  int findMin()          - returns the smallest data value contained in the binary tree.
 *                           If tree is empty, return Integer.MAX_VALUE.
 *  int nodesGT(int val)   - returns the number of nodes in the tree that contain a
 *                           data value larger than 'val'.
 *  double average()       - returns the average of all the data values in the tree.
 */

public class BinaryTree {

    // Node class representing each node in the tree
    private class Node {
        int data;
        Node left;
        Node right;

        Node(int data) {
            this.data = data;
            left = null;
            right = null;
        }
    }

    // Root of the binary tree
    private Node root;

    // Insert method to populate the tree
    Node insert(int data) {

        Node tempNode = new Node(data);

        // If tree is empty, insert new node as the root.
        if (root == null)
            return root = tempNode;

        // Create a queue to do level order traversal
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        // Do level order traversal
        while (!queue.isEmpty()) {
            Node front = queue.peek();

            if (front.left == null) {
                front.left = tempNode;
                break;
            } else if (front.right == null) {
                front.right = tempNode;
                break;
            } else {
                // If front node in queue has both left and right
                // children, remove it from the queue.

                queue.remove();
            }

            // Enqueue the left and right children of teh current node
            if (front.left != null)
                queue.add(front.left);

            if (front.right != null)
                queue.add(front.right);
        }

        return tempNode;

    } // End method insert


    /*
     * public method replaceValue(int k, int l)
     *
     * This method will traverse the tree using depth first search (DFS)
     * and replace all nodes with the value 'k' with the value 'l'.
     */
    public void replaceValue(int oldVal, int newVal) {
        replaceValueHelper(root, oldVal, newVal);
    }

    private void replaceValueHelper(Node node, int oldVal, int newVal) {
        if (node == null) {
            return;
        }
        if (node.data == oldVal) {
            node.data = newVal;
        }
        replaceValueHelper(node.left, oldVal, newVal);
        replaceValueHelper(node.right, oldVal, newVal);
    }

    /*
     * public method findMin()
     *
     * This method will traverse the tree using depth first search (DFS)
     * and return the minimum data value found in the binary tree.
     * If the tree is empty, return Integer.MAX_VALUE.
     */
    public int findMin() {
        return findMinHelper(root);
    }

    private int findMinHelper(Node node) {
        if (node == null) {
            return Integer.MAX_VALUE;
        }
        int leftMin = findMinHelper(node.left);
        int rightMin = findMinHelper(node.right);
        return Math.min(node.data, Math.min(leftMin, rightMin));
    }

    /*
     * public method nodesGT(int val)
     *
     * This method will traverse the tree using depth first search traversal and
     * return a count on the number of nodes that contain a data value larger
     * than the parameter 'val'.
     *
     * If the tree is empty, return 0.
     */
    public int nodesGT(int val) {
        return nodesGTHelper(root, val);
    }

    private int nodesGTHelper(Node node, int val) {
        if (node == null) {
            return 0;
        }
        int count = 0;
        if (node.data > val) {
            count = 1;
        }
        count += nodesGTHelper(node.left, val);
        count += nodesGTHelper(node.right, val);
        return count;
    }

    /*
     * public method average()
     *
     * This method will traverse the tree using depth first search traversal and
     * return the average value contained in the binary tree. To easily perform a depth
     * first traversal, it invokes the helper method, averageHelper(), which is the
     * method that should be called recursively. If the tree is empty, 0 should be
     * returned.
     *
     * IMPORTANT NOTE:
     * The helper method should return an array of two integer values. In index
     * location [0] is the sum of all data values in the tree. And in index
     * location [1] is the count of nodes.
     */
    public double average() {
        int[] sumAndCount = averageHelper(root);
        if (sumAndCount[1] == 0) { // If the tree is empty
            return 0;
        }
        return (double) sumAndCount[0] / sumAndCount[1];
    }

    private int[] averageHelper(Node node) {
        if (node == null) {
            return new int[] {0, 0}; // Sum = 0, Count = 0
        }
        int[] left = averageHelper(node.left);
        int[] right = averageHelper(node.right);
        int sum = node.data + left[0] + right[0];
        int count = 1 + left[1] + right[1];
        return new int[] {sum, count};
    }

    /*
     * public method preOrder()
     *
     * This method returns a string of the node values in pre-order traversal.
     */
    public String preOrder() {
        StringBuilder sb = new StringBuilder();
        preOrderHelper(root, sb);
        return sb.toString();
    }

    private void preOrderHelper(Node node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        sb.append(node.data).append(" ");
        preOrderHelper(node.left, sb);
        preOrderHelper(node.right, sb);
    }
}
