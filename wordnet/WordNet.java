import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {

    private ST<String, Bag<Integer>> st;  // a symbol table restore a nouns and its related IDs
    private Queue<String>[] keys;         // the ith element in keys represent the synsets whose ID is i
    private Digraph G;                    // a directed graph constructed by hypernyms files
    private SAP searchPath;  // reinitializing the entries that changed in the previous computation

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();
        st = new ST<>();
        In in = new In(synsets);
        int ID = 0;
        while (in.hasNextLine()) {
            String[] a = in.readLine().split(",");
//            String s = String.join(",", a[1].split(" "));
            String[] synset = a[1].split(" ");
            for (String s : synset) {
                if (!st.contains(s)) {
                    Bag<Integer> currBag = new Bag<>();
                    currBag.add(Integer.parseInt(a[0]));
                    st.put(s, currBag);
                } else {
                    st.get(s).add(Integer.parseInt(a[0]));
                }
            }
            ++ID;
        }
        keys = (Queue<String>[]) new Queue[ID];
        for (int i = 0; i < ID; i++)
            keys[i] = new Queue<>();
        for (String name : st.keys()) {
            for (int e : st.get(name))
                keys[e].enqueue(name);
        }
        G = new Digraph(ID);
        boolean[] marked = new boolean[ID];
        int root = -1;
        in = new In(hypernyms);
        while (in.hasNextLine()) {
            String[] a = in.readLine().split(",");
            int v = Integer.parseInt(a[0]);
            if (a.length == 1) root = v;
            marked[v] = true;
            for (int i = 1; i < a.length; i++) {
                int w = Integer.parseInt(a[i]);
                // if (marked[v] && marked[w]) throw new IllegalArgumentException();
                G.addEdge(v, w);
                marked[w] = true;
            }
        }
//        if (root == -1) throw new IllegalArgumentException();
        searchPath = new SAP(G);
    }

    public Iterable<String> nouns() {
        return st;
    }

    public boolean isNoun(String word) {
        return st.contains(word);
    }

    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        Bag<Integer> intA = st.get(nounA);
        Bag<Integer> intB = st.get(nounB);
        int length = searchPath.length(intA, intB);

        return length;
    }

    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        Bag<Integer> intA = st.get(nounA);
        Bag<Integer> intB = st.get(nounB);
        int ancenstor = searchPath.ancestor(intA, intB);
        return String.join(" ", keys[ancenstor]);
    }

    public static void main(String[] args) {
        WordNet w = new WordNet("C:\\Users\\JXT\\IdeaProjects\\WordNet\\test\\synsets.txt",
                "C:\\Users\\JXT\\IdeaProjects\\WordNet\\test\\hypernyms.txt");
        StdOut.println(w.distance("white_marlin", "mileage"));
        StdOut.println(w.distance("Black_Plague", "black_marlin"));
        StdOut.println(w.distance("American_water_spaniel", "histology"));
        StdOut.println(w.distance("Brown_Swiss", "barrel_roll"));
        StdOut.println(w.distance("schlep", "War_of_the_Grand_Alliance"));
        StdOut.println(w.sap("schlep", "War_of_the_Grand_Alliance"));
    }
}
