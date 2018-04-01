import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet w ;
    public Outcast(WordNet wordnet) {
        w = wordnet;
    }

    public String outcast(String[] nouns) {
        int length = nouns.length;
        int[] length_matrix = new int[length * length];
        int[] dist = new int[length];
        for (int i = 0; i < length - 1; i++) {
            for (int j = i + 1; j < length; j++) {
                int ij_dis = w.distance(nouns[i], nouns[j]);
                length_matrix[length * i + j] = ij_dis;
                length_matrix[length * j + i] = ij_dis;
            }
        }
        int max_dis = 0;
        int o = 0;
        for (int i = 0; i < length; i++) {
            for (int j = i * length; j < (i + 1) * length; j++)
                dist[i] += length_matrix[j];
            if (dist[i] > max_dis) {max_dis = dist[i]; o = i;}
        }
        return nouns[o];
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet("C:\\Users\\JXT\\IdeaProjects\\WordNet\\test\\synsets.txt",
                "C:\\Users\\JXT\\IdeaProjects\\WordNet\\test\\hypernyms.txt");
        Outcast outcast = new Outcast(wordnet);
//        for (int t = 2; t < args.length; t++) {
            In in = new In("C:\\Users\\JXT\\IdeaProjects\\WordNet\\test\\outcast11.txt");
            String[] nouns = in.readAllStrings();
            StdOut.println("outcast5.txt" + ": " + outcast.outcast(nouns));
//        }
    }
}
