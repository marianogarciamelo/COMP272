/*
 * *** Mariano Garcia Melo / Section 001 ***
 *
 * This java file is a Java object implementing simple AVL Tree.
 * You are to complete the deleteElement method.
 *
 */

import java.lang.Math;


/**
 *  Class: Node
 *
 *  This class represents an inner node of the AVL Tree. Additional candidate
 *  attributes can be considered, but if added, they must be maintained during
 *  tree modifications, including rotations.
 *
 *  The values currently maintained in each node include:
 *    - value : This is the value of the node.
 *    - height: Height of the node in the tree. A node's height is the maximum
 *              number of edges to its deepest leaf of its two subtrees.
 *
 *  Additional candidate values that can be considered (but currently not
 *  implemented):
 *    - size: # of nodes in a node's subtrees
 *    - bf  : the nodes's balance factor, e.g., {-1, 0, 1}
 *
 */

class Node {
    int value;                      // the node's value
    int height;                     // height of node based on its [sub]trees
    Node leftChild, rightChild;     // left and right subtrees

    public Node(int data) {         // parameterized constructor
        value = data;
        height = 0;
        leftChild = rightChild = null;
    }
}


/**
 *  Class 'LUC_AVLTree'
 *
 *  LUC_AVLTree class definition for constructing / accessing / modifying the
 *  AVL tree. In order to provide a general purpose AVL tree, better error
 *  handling should to be provided.
 *
 *  Notes:
 *   1. This AVL tree does not allow duplicates, if one is attempted to be
 *      inserted, no action is taken by the method (though an error should be
 *      thrown).
 *   2. If a deletion is attempted for a non-existent value, no action is taken
 *      (again, returning an error indicator and/or throwing an error would be
 *      appropriate.
 *
 *  Public methods:
 *   void    removeAll()         - Remove all nodes of the tree (empties tree)
 *   boolean checkEmpty()        - Returns boolean value if tree is empty or not
 *   void    insert(int value)   - inserts 'value' into the tree
 *   void    delete(int value)   - removes 'value' from the tree
 *   String  preorderTraversal() - returns a preorder traversal of tree in a String
 *
 */

class LUC_AVLTree {
    private Node rootNode;           // The root node of the AVL Tree

    public LUC_AVLTree()              { rootNode = null; }       // Constructor
    public void removeAll()           { rootNode = null; }       // Make tree empty
    public boolean checkEmpty()       { if (rootNode == null) return true; else return false; }
    public void insert(int value)     { rootNode = insertElement(value, rootNode); }
    public void delete(int value)     { rootNode = deleteElement(value, rootNode); }
    public String preorderTraversal() { return preorderTraversal(rootNode); }

    private boolean isTreeBalanced()   { return isTreeBalanced(rootNode); }
    private boolean isBST()            { return isBST(rootNode); }
    private int getHeight(Node node)  { return node == null ? -1 : node.height; }
    private int getMaxHeight(int leftNodeHeight, int rightNodeHeight) {
        return leftNodeHeight > rightNodeHeight ? leftNodeHeight : rightNodeHeight;
    }

    private boolean isTreeBalanced(Node node) {
        if (node == null)
            return true;

        int leftSubTree  = getHeight(node.leftChild)  + 1;
        int rightSubTree = getHeight(node.rightChild) + 1;

        // Calculate the bf
        if (Math.abs(leftSubTree - rightSubTree) > 1)
            return false;

        return isTreeBalanced(node.leftChild) && isTreeBalanced(node.rightChild);
    }

    private boolean isBST( Node node) {
        if (node == null)
            return true;

        if ( (node.leftChild != null) && (maxValue(node.leftChild) > node.value) )
            return false;

        if ( (node.rightChild != null) && (minValue(node.rightChild) < node.value) )
            return false;

        if ( ( ! isBST(node.leftChild) ) || ( ! isBST(node.rightChild) ) )
            return false;

        return true;
    }

    private int maxValue(Node node) {
        if (node == null)
            return  Integer.MIN_VALUE;

        int value    = node.value;
        int leftMax  = maxValue(node.leftChild);
        int rightMax = maxValue(node.rightChild);

        return Math.max(value, Math.max(leftMax, rightMax));
    }

    private int minValue(Node node) {
        if (node == null)
            return Integer.MAX_VALUE;

        int value    = node.value;
        int leftMin  = minValue(node.leftChild);
        int rightMin = minValue(node.rightChild);

        return Math.max(value, Math.max(leftMin, rightMin));
    }

    private String preorderTraversal(Node node) {
        if (node == null)
            return "";

        return node.value + " " + preorderTraversal(node.leftChild)
                + preorderTraversal(node.rightChild);
    }

    private Node insertElement(int value, Node node) {
        if (node == null) {
            node = new Node(value);
            return node;
        }

        if (value < node.value) {
            node.leftChild = insertElement(value, node.leftChild);
            int bf = getBalanceFactor(node);

            if (Math.abs(bf) > 1) {
                if (value < node.leftChild.value)
                    node = LLRotation(node);
                else
                    node = LRRotation(node);
            }
        } else if (value > node.value ) {
            node.rightChild = insertElement(value, node.rightChild);
            int bf = getBalanceFactor(node);

            if (Math.abs(bf) > 1 ) {
                if (value > node.rightChild.value)
                    node = RRRotation(node);
                else
                    node = RLRotation(node);
            }
        }

        node.height = (getMaxHeight( getHeight(node.leftChild), getHeight(node.rightChild))) + 1;

        return node;
    }

    private Node deleteElement(int value, Node node) {
        if (node == null) {
            return node;
        }
        if (value < node.value) {
            node.leftChild = deleteElement(value, node.leftChild);
        } else if (value > node.value) {
            node.rightChild = deleteElement(value, node.rightChild);
        } else {
            if ((node.leftChild == null) || (node.rightChild == null)) {
                Node temp = node.leftChild == null ? node.rightChild : node.leftChild;

                if (temp == null) {
                    node = null;
                } else {
                    node = temp;
                }
            } else {
                Node temp = minValueNode(node.rightChild);
                node.value = temp.value;
                node.rightChild = deleteElement(temp.value, node.rightChild);
            }
        }

        if (node == null) {
            return node;
        }

        node.height = getMaxHeight(getHeight(node.leftChild), getHeight(node.rightChild)) + 1;

        int balance = getBalanceFactor(node);

        if (balance > 1 && getBalanceFactor(node.leftChild) >= 0) {
            return LLRotation(node);
        }

        if (balance > 1 && getBalanceFactor(node.leftChild) < 0) {
            node.leftChild = RRRotation(node.leftChild);
            return LLRotation(node);
        }

        if (balance < -1 && getBalanceFactor(node.rightChild) <= 0) {
            return RRRotation(node);
        }

        if (balance < -1 && getBalanceFactor(node.rightChild) > 0) {
            node.rightChild = LLRotation(node.rightChild);
            return RRRotation(node);
        }

        return node;
    }

    private int getBalanceFactor(Node node) {
        if (node == null) return 0;

        int leftSubTreeHeight  = getHeight(node.leftChild)  + 1;
        int rightSubTreeHeight = getHeight(node.rightChild) + 1;

        return leftSubTreeHeight - rightSubTreeHeight;
    }

    private Node minValueNode(Node node) {
        Node current = node;
        while (current.leftChild != null)
            current = current.leftChild;
        return current;
    }

    private Node LLRotation( Node x) {
        Node y = x.leftChild;
        x.leftChild = y.rightChild;
        y.rightChild = x;

        // Re-calculate the node heights
        x.height = getMaxHeight( getHeight(x.leftChild), getHeight(x.rightChild)) + 1;
        y.height = getMaxHeight( getHeight(y.leftChild), getHeight(y.rightChild)) + 1;

        return y;
    }


    /**
     *  Method: LRRotation
     *
     *  LRRotation() is a private method that performs a rotation where node X
     *  is the node out-of-balance due to Left Right children. After the
     *  rotation, node X's left Right grand-child, Z, becomes the new top of the
     *  [sub]tree. And, Z's left sub-child, zl, becomes the right sub-child of
     *  Y, and right sub-child, zr, becomes the left sub-child of X. Node Z is
     *  returned as the new top to the [sub]tree. The height values of nodes X, Y,
     *  and Z are adjusted, each changes in this rotation.
     *
     *          X
     *        /   \
     *        Y     xr                     Z
     *      /   \          ==>           /   \
     *     yl     Z                    Y       X
     *           / \                 /   \    /  \
     *          zl  zr              yl   zl  zr  xr
     *
     *  @param  - node that is out of balance
     *
     *  @return Z - new top of the [sub]tree after rotation
     */

    private Node LRRotation( Node x) {
        Node y = x.leftChild;
        Node z = x.leftChild.rightChild;
        y.rightChild = z.leftChild;
        x.leftChild  = z.rightChild;
        z.leftChild  = y;
        z.rightChild = x;

        // Re-calculate the node heights
        x.height = getMaxHeight(getHeight(x.leftChild), getHeight(x.rightChild)) + 1;
        y.height = getMaxHeight(getHeight(y.leftChild), getHeight(y.rightChild)) + 1;
        z.height = getMaxHeight(getHeight(z.leftChild), getHeight(z.rightChild)) + 1;

        return z;
    }


    /**
     *  Method: RRRotation
     *
     *  RRRotation() is a private method that performs a rotation where node X
     *  is the node out-of-balance due to Right Right children. After the
     *  rotation, its right sub-child, Y, becomes the new top of this
     *  [sub]tree. And, node Y's left sub-child, yl, becomes the right sub-child
     *  of X. Node Y is returned as the new top to the [sub]tree. The height
     *  values of nodes X and Y are re-adjusted. Z's height does not change.
     *
     *               X
     *             /   \
     *            xl     Y                          Y
     *                 /   \        ==>           /    \
     *               yl     Z                    X       Z
     *                     / \                 /   \    /  \
     *                    zl  zr              xl   yl  zl  zr
     *
     *  @param  - node that is out of balance
     *
     *  @return Y - new top node of [sub]tree after rotation
     */

    private Node RRRotation( Node x) {
        Node y = x.rightChild;
        x.rightChild = y.leftChild;
        y.leftChild = x;

        // Re-calculate the node heights
        x.height = getMaxHeight( getHeight(x.leftChild), getHeight(x.rightChild)) + 1;
        y.height = getMaxHeight( getHeight(y.leftChild), getHeight(y.rightChild)) + 1;
        return y;
    }


    /**
     *  Method: RLRotation
     *
     *  RLRotation performs a rotation where node X is the node out-of-balance
     *  due to Right Left children. After the rotation, node X's right left
     *  grandchild, Z, becomes the new top of the [sub]tree. And, Z's left
     *  subchild, zl, becomes the right subchild of X, and right subchild, zr,
     *  becomes the left subchild of Y. Node Z is returned as the new top to the
     *  [sub]tree. The height values of nodes X, Y, and Z are adjusted, each
     *  changes in this rotation.
     *
     *          X
     *        /   \
     *       xl     Y                           Z
     *            /   \         ==>           /   \
     *           Z     yr                   X       Y
     *          / \                       /   \    /  \
     *         zl  zr                    xl   zl  zr  yr
     *
     *  @param  - node that is out of balance
     *
     *  @return Z - new top of the [sub]tree after rotation
     */

    private Node RLRotation( Node x) {
        Node y = x.rightChild;
        Node z = x.rightChild.leftChild;
        y.leftChild  = z.rightChild;
        x.rightChild = z.leftChild;
        z.rightChild = y;
        z.leftChild  = x;

        // Re-calculate the node heights
        x.height = getMaxHeight( getHeight(x.leftChild), getHeight(x.rightChild)) + 1;
        y.height = getMaxHeight( getHeight(y.leftChild), getHeight(y.rightChild)) + 1;
        z.height = getMaxHeight( getHeight(z.leftChild), getHeight(z.rightChild)) + 1;
        return z;
    }
}