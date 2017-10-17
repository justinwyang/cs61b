import java.util.List;
import java.util.ArrayList;

/**
 * Implementation of a BST based String Set.
 * @author Justin Yang
 */
public class BSTStringSet implements StringSet {
    /** Creates a new empty set. */
    public BSTStringSet() {
        root = null;
    }

    @Override
    public void put(String s) {
        if (root == null) {
            root = new Node(s);
        } else {
            root.put(s);
        }
    }

    @Override
    public boolean contains(String s) {
        if (root == null) {
            return false;
        }
        return root.contains(s);
    }

    @Override
    public List<String> asList() {
        ArrayList<String> list = new ArrayList<>();
        if (root != null) {
            root.asList(list);
        }
        return list;
    }

    /** Represents a single Node of the tree. */
    private static class Node {

        /** Returns if the current subtree contains s.
         *
         * @param string the string to check
         * @return whether the tree at node contains s
         */
        public boolean contains(String string) {
            if (s.equals(string)) {
                return true;
            } else if (s.compareTo(string) < 0 && left != null
                    && left.contains(string)) {
                return true;
            } else if (right != null && right.contains(string)) {
                return true;
            }
            return false;
        }

        /** Puts s into the subtree at node.
         *
         * @param string the string to put
         */
        public void put(String string) {
            if (string.compareTo(s) < 0) {
                if (left == null) {
                    left = new Node(string);
                } else {
                    left.put(string);
                }
            } else {
                if (right == null) {
                    right = new Node(string);
                } else {
                    right.put(string);
                }
            }
        }

        /** Adds the values in the subtree to list.
         *
         * @param list The list to add the strings to
         */
        private void asList(ArrayList<String> list) {
            if (left != null) {
                left.asList(list);
            }
            list.add(s);
            if (right != null) {
                right.asList(list);
            }
        }

        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        public Node(String sp) {
            s = sp;
        }
    }

    /** Root node of the tree. */
    private Node root;
}
