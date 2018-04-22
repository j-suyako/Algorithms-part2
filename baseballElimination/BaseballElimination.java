import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.ST;

public class BaseballElimination {
    private int V;
    private ST<String, Integer> st;
    private String[] teams;
    private int[] wins;
    private int[] losses;
    private int[] remaining;
    private int[][] play_against;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        V = in.readInt();
        st = new ST<>();
        wins = new int[V];
        losses = new int[V];
        remaining = new int[V];
        play_against = new int[V][V];
        teams = new String[V];
        for (int i = 0; i < V; i++){
            String team = in.readString();
            st.put(team, i);
            teams[i] = team;
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remaining[i] = in.readInt();
            for (int j = 0; j < V; j++)
                play_against[i][j] = in.readInt();
        }

    }

    public int numberOfTeams() {
        return V;
    }

    public Iterable<String> teams() {
        Queue<String> res = new Queue<>();
        for (int i = 0; i < V; i++)
            res.enqueue(teams[i]);
        return res;
    }

    private void checkTeam(String team) {
        if (!st.contains(team)) throw new IllegalArgumentException();
    }
    public int wins(String team) {
        checkTeam(team);
        return wins[st.get(team)];
    }

    public int losses(String team) {
        checkTeam(team);
        return losses[st.get(team)];
    }

    public int remaining(String team) {
        checkTeam(team);
        return remaining[st.get(team)];
    }

    public int against(String team1, String team2) {
        checkTeam(team1);
        checkTeam(team2);
        return play_against[st.get(team1)][st.get(team2)];
    }

    private FlowNetwork generate(int ID) {
        int numOfi2j = (V - 1) * (V - 2) / 2;
        int numOfVert = V - 1;
        FlowNetwork G = new FlowNetwork(numOfi2j + numOfVert + 2);
        int count = 1;
        for (int i = 0; i < V; i++) {
            for (int j = i + 1; j < V; j++) {
                if (i == ID || j == ID) continue;
                int p = i < ID ? i : i - 1;
                int q = j < ID ? j : j - 1;
                FlowEdge edge1 = new FlowEdge(0, count, play_against[i][j]);
                FlowEdge edge2 = new FlowEdge(count, numOfi2j + 1 + p, Double.POSITIVE_INFINITY);
                FlowEdge edge3 = new FlowEdge(count, numOfi2j + 1 + q, Double.POSITIVE_INFINITY);
                G.addEdge(edge1);
                G.addEdge(edge2);
                G.addEdge(edge3);
                count++;
            }
        }
        for (int i = numOfi2j + 1; i < G.V() - 1; i++) {
            int p = i - numOfi2j - 1;
            if (p >= ID)
                p += 1;
            double capacity = wins[ID] + remaining[ID] - wins[p];
            FlowEdge edge = new FlowEdge(i, G.V() - 1, capacity);
            G.addEdge(edge);
        }
        return G;
    }

    private boolean isTrivial(int ID) {
        for (int i = 0; i < V; i++) {
            if (wins[ID] + remaining[ID] < wins[i])
                return true;
        }
        return false;
    }

    public boolean isEliminated(String team) {
        checkTeam(team);
        int ID = st.get(team);
        if (isTrivial(ID)) return true;
        FlowNetwork G = generate(ID);
        FordFulkerson solver = new FordFulkerson(G, 0, G.V() - 1);
        for (int i = 1; i < (V - 1) * (V - 2) / 2 + 1; i++) {
            if (solver.inCut(i))
                return true;
        }
        return false;
    }

    public Iterable<String> certificateOfElimination(String team) {
        checkTeam(team);
        int ID = st.get(team);
        Queue<String> res = new Queue<>();
        if (isTrivial(ID)) {
            for (int i = 0; i < V; i++) {
                if (wins[ID] + remaining[ID] < wins[i])
                    res.enqueue(teams[i]);
            }
            return res;
        }
        FlowNetwork G = generate(ID);
        FordFulkerson solver = new FordFulkerson(G, 0, G.V() - 1);
        for (int i = (V - 1) * (V - 2) / 2 + 1; i < G.V() - 1; i++) {
            int p = i - (V - 1) * (V - 2) / 2 - 1 >= ID ? i - (V - 1) * (V - 2) / 2 : i - (V - 1) * (V - 2) / 2 - 1;
            if (wins[ID] + remaining[ID] < wins[p])
                res.enqueue(teams[p]);
            else {
                if (solver.inCut(i))
                    res.enqueue(teams[p]);
            }
        }
        if (res.isEmpty()) return null;
        else return res;
    }

    public static void main(String[] args) {
//        BaseballElimination division = new BaseballElimination("C:\\Users\\JXT\\IdeaProjects\\BaseballElimination\\test\\teams48.txt");
//        for (String team : division.teams()) {
//            if (division.isEliminated(team) && !division.isTrivial(division.st.get(team))) {
//                StdOut.print(team + " is eliminated by the subset R = { ");
//                for (String t : division.certificateOfElimination(team)) {
//                    StdOut.print(t + " ");
//                }
//                StdOut.println("}");
//            }
////            else {
////                StdOut.println(team + " is not eliminated");
////            }
//        }
    }
}
