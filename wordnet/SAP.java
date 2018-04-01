import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private class myBreadthSearch {
        private int[] v_res;
        private int[] w_res;
        private boolean[] v_marked;
        private boolean[] w_marked;
        private int[] v_edgeTo;
        private int[] w_edgeTo;

        public myBreadthSearch(int V) {
            v_res = new int[V];
            w_res = new int[V];
            v_marked = new boolean[V];
            w_marked = new boolean[V];
            v_edgeTo = new int[V];
            w_edgeTo = new int[V];
            for (int i = 0; i < V; i++) {
                v_edgeTo[i] = i;
                w_edgeTo[i] = i;
            }
        }

        public int searchPaths(Queue<Integer> from_v, Queue<Integer> from_w) {
            for (Integer e : from_v)
                v_marked[e] = true;
            for (Integer e : from_w) {
                if (v_marked[e])
                    return e;
                w_marked[e] = true;
            }
            Bag<Integer> res = new Bag<>();
            while (!from_v.isEmpty() || !from_w.isEmpty()) {
                int curr;
                if (!from_v.isEmpty()) {
                    curr = from_v.dequeue();
                    for (int e : original.adj(curr)) {
                        if (!v_marked[e]) {
                            v_res[e] = v_res[curr] + 1;
                            v_edgeTo[e] = curr;
                            v_marked[e] = true;
                            from_v.enqueue(e);
                        }
                        if (w_marked[e]) {
                            res.add(e);
                        }
                    }
                }
                if (!from_w.isEmpty()) {
                    curr = from_w.dequeue();
                    for (int e : original.adj(curr)) {
                        if (!w_marked[e]) {
                            w_res[e] = w_res[curr] + 1;
                            w_edgeTo[e] = curr;
                            w_marked[e] = true;
                            from_w.enqueue(e);
                        }
                        if (v_marked[e]) {
                            res.add(e);
                        }
                    }
                }
            }
            int min_dis = 1 << 10;
            int ancenstor = -1;
            for (int e : res) {
                if (v_res[e] + w_res[e] < min_dis) {
                    min_dis = v_res[e] + w_res[e];
                    ancenstor = e;
                }
            }
            return ancenstor;
        }
    }
    private int V;
    private Digraph original;
    private Graph noDirectWordNet;

    private boolean check(int arg) {
        return arg >= 0 && arg < V;
    }

    public SAP(Digraph G) {
        if (G == null) throw new IllegalArgumentException();
        V = G.V();
        original = G;
        noDirectWordNet = new Graph(V);
        for (int i = 0; i < G.V(); i++) {
            for (int e : G.adj(i))
                noDirectWordNet.addEdge(i, e);
        }
    }

    public int length(int v, int w) {
        if (!check(v) || !check(w)) throw new IllegalArgumentException();
        myBreadthSearch paths = new myBreadthSearch(noDirectWordNet.V());
        Queue<Integer> toRetrive = new Queue<>();
        toRetrive.enqueue(v);
        Queue<Integer> target = new Queue<>();
        target.enqueue(w);
        int ancestor = paths.searchPaths(toRetrive, target);
        return ancestor == -1 ? -1 : (paths.v_res[ancestor] + paths.w_res[ancestor]);
    }

    public int ancestor(int v, int w) {
        if (!check(v) || !check(w)) throw new IllegalArgumentException();
        myBreadthSearch paths = new myBreadthSearch(noDirectWordNet.V());
        Queue<Integer> toRetrive = new Queue<>();
        toRetrive.enqueue(v);
        Queue<Integer> target = new Queue<>();
        target.enqueue(w);
        return paths.searchPaths(toRetrive, target);
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        myBreadthSearch paths = new myBreadthSearch(noDirectWordNet.V());
        Queue<Integer> toRetrive = new Queue<>();
        for (int e : v)
            toRetrive.enqueue(e);
        Queue<Integer> target = new Queue<>();
        for (int e : w)
            target.enqueue(e);
        int ancestor = paths.searchPaths(toRetrive, target);
        return ancestor == -1 ? -1 : (paths.v_res[ancestor] + paths.w_res[ancestor]);
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        myBreadthSearch paths = new myBreadthSearch(noDirectWordNet.V());
        Queue<Integer> toRetrive = new Queue<>();
        for (int e : v) {
            if (!check(e)) throw new IllegalArgumentException();
            toRetrive.enqueue(e);
        }
        Queue<Integer> target = new Queue<>();
        for (int e : w) {
            if (!check(e)) throw new IllegalArgumentException();
            target.enqueue(e);
        }
        return paths.searchPaths(toRetrive, target);
    }

    public static void main(String[] args) {
        In in = new In("C:\\Users\\JXT\\IdeaProjects\\WordNet\\test\\digraph1.txt");
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
