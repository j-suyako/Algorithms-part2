import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;

public class WordNet {

    private ST<String, Bag<Integer>> st;
    private Bag<String>[] keys;
    private Digraph G;

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
        keys = (Bag<String>[]) new Bag[ID];
        for (int i = 0; i < ID; i++)
            keys[i] = new Bag<>();
        for (String name : st.keys()) {
            for (int e : st.get(name))
                keys[e].add(name);
        }
        G = new Digraph(ID);
        in = new In(hypernyms);
        while (in.hasNextLine()) {
            String[] a = in.readLine().split(",");
            int v = Integer.parseInt(a[0]);
            for (int i = 1; i < a.length; i++) {
                G.addEdge(v, Integer.parseInt(a[i]));
            }
        }
    }

    public Iterable<String> nouns() {
        return st;
    }

    public boolean isNoun(String word) {
        return st.contains(word);
    }

    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();
        SAP var1 = new SAP(G);
        Bag<Integer> intA = st.get(nounA);
        Bag<Integer> intB = st.get(nounB);
        return var1.length(intA, intB);
    }

    public String sap(String nounA, String nounB) {
        SAP var1 = new SAP(G);
        Bag<Integer> intA = st.get(nounA);
        Bag<Integer> intB = st.get(nounB);
        int ancenstor = var1.ancestor(intA, intB);
        String res = "";
        for (String e : keys[ancenstor]) {
            res += e;
        }
        return res;
    }

    public static void main(String[] args) {
        WordNet w = new WordNet("C:\\Users\\JXT\\IdeaProjects\\WordNet\\test\\synsets.txt",
                "C:\\Users\\JXT\\IdeaProjects\\WordNet\\test\\hypernyms.txt");
        w.distance("white_marlin", "mileage");
        int a = 1;
    }
}
