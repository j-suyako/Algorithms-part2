import edu.princeton.cs.algs4.Queue;

public class myTrieST {
    private static final int R = 26;

    public Node root;

//    private static class Node {
//        private int val = -1;
//        private Node[] next = new Node[R];
//    }

    public myTrieST() {
    }

    public int get(String key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        Node x = get(root, key, 0);
        if (x == null) return -2;
        return x.val;
    }

    public Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c - 65], key, d+1);
    }

    public boolean contains(String key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) > -1;
    }

    public void put(String key, int val) {
        if (key == null) throw new IllegalArgumentException("first argument to put() is null");
//        if (val == null) delete(key);
        else root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, int val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
//            if (x.val == -1) n++;
            x.val = val;
            return x;
        }
        char c = key.charAt(d);
        x.next[c - 65] = put(x.next[c - 65], key, val, d+1);
        return x;
    }
}
