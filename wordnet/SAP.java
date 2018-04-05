import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private int V;
    private Digraph original;
    private int[] v_res;
    private int[] w_res;
    private boolean[] v_marked;
    private boolean[] w_marked;
    private Queue<Integer> all_marked;

    private boolean check(int arg) {
        return arg >= 0 && arg < V;
    }
    private boolean check(Iterable<Integer> args) {
        if (args == null) throw new IllegalArgumentException();
        for (int e : args) {
            if (!check(e))
                return false;
        }
        return true;
    }

    private int searchAncestor(Queue<Integer> from_v, Queue<Integer> from_w) {
        for (Integer e : from_v) {
            v_marked[e] = true;
            all_marked.enqueue(e);
        }
        for (Integer e : from_w) {
            if (v_marked[e])
                return e;
            w_marked[e] = true;
            all_marked.enqueue(e);
        }
        int min_dis = Integer.MAX_VALUE;
        int length_from_v = 0;
        int length_from_w = 0;
        int res = -1;
        // Bag<Integer> res = new Bag<>();
        while (!from_v.isEmpty() || !from_w.isEmpty()) {
            int curr;  // the reference of current element
            if (!from_v.isEmpty()) {
                curr = from_v.dequeue();
                for (int e : original.adj(curr)) {
                    if (!v_marked[e]) {
                        v_res[e] = v_res[curr] + 1;
                        v_marked[e] = true;
                        from_v.enqueue(e);
                    }
                    if (w_marked[e]) {
                        if (v_res[e] + w_res[e] < min_dis) {
                            min_dis = v_res[e] + w_res[e];
                            res = e;
                        }
                    } else {
                        all_marked.enqueue(e);
                    }
                }
                length_from_v = v_res[curr];
            }
            if (!from_w.isEmpty()) {
                curr = from_w.dequeue();
                for (int e : original.adj(curr)) {
                    if (!w_marked[e]) {
                        w_res[e] = w_res[curr] + 1;
                        w_marked[e] = true;
                        from_w.enqueue(e);
                    }
                    if (v_marked[e]) {
                        if (v_res[e] + w_res[e] < min_dis) {
                            min_dis = v_res[e] + w_res[e];
                            res = e;
                        }
                    } else {
                        all_marked.enqueue(e);
                    }
                }
                length_from_w = w_res[curr];
            }
            //if (length_from_v + length_from_w >= min_dis) break;
        }
        return res;
    }

    private void reinitialize() {
        while (!all_marked.isEmpty()) {
            int e = all_marked.dequeue();
            v_res[e] = 0;
            w_res[e] = 0;
            v_marked[e] = false;
            w_marked[e] = false;
        }
    }

    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        V = G.V();
        original = G;
        v_res = new int[V];
        w_res = new int[V];
        v_marked = new boolean[V];
        w_marked = new boolean[V];
        all_marked = new Queue<>();
    }

    public int length(int v, int w) {
        if (!check(v) || !check(w)) throw new IllegalArgumentException();
        Queue<Integer> toRetrive = new Queue<>();
        toRetrive.enqueue(v);
        Queue<Integer> target = new Queue<>();
        target.enqueue(w);
        int ancestor = searchAncestor(toRetrive, target);
        int length = ancestor == -1 ? -1 : (v_res[ancestor] + w_res[ancestor]);
        reinitialize();
        return length;
    }

    public int ancestor(int v, int w) {
        if (!check(v) || !check(w)) throw new IllegalArgumentException();
        Queue<Integer> toRetrive = new Queue<>();
        toRetrive.enqueue(v);
        Queue<Integer> target = new Queue<>();
        target.enqueue(w);
        int ancestor = searchAncestor(toRetrive, target);
        reinitialize();
        return ancestor;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (!check(v) || !check(w)) throw new IllegalArgumentException();
        Queue<Integer> toRetrive = new Queue<>();
        for (int e : v)
            toRetrive.enqueue(e);
        Queue<Integer> target = new Queue<>();
        for (int e : w)
            target.enqueue(e);
        int ancestor = searchAncestor(toRetrive, target);
        int length = ancestor == -1 ? -1 : (v_res[ancestor] + w_res[ancestor]);
        reinitialize();
        return length;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (!check(v) || !check(w)) throw new IllegalArgumentException();
        Queue<Integer> toRetrive = new Queue<>();
        for (int e : v)
            toRetrive.enqueue(e);
        Queue<Integer> target = new Queue<>();
        for (int e : w)
            target.enqueue(e);
        int ancestor = searchAncestor(toRetrive, target);
        reinitialize();
        return ancestor;
    }

    public static void main(String[] args) {
        In in = new In("C:\\Users\\JXT\\IdeaProjects\\WordNet\\test\\digraph2.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
//        Stack<Integer> v = new Stack<>();
//        v.push(7);
//        v.push(8);
//        Stack<Integer> w = new Stack<>();
//        w.push(2);
//        w.push(11);
//        int length = sap.length(v, w);
//        StdOut.printf("length = %d\n", length);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
