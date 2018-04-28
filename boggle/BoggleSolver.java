import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;
import edu.princeton.cs.algs4.TrieST;

public class BoggleSolver {
    private myTrieST dict;
    private boolean[] marked;
//    private Node node;

    public BoggleSolver(String[] dictionary) {
        dict = new myTrieST();
        int i = 0;
        for (String curr : dictionary) {
            dict.put(curr, i++);
        }
    }

    private int getIndex(int row, int col, int row_capac, int col_capac) {
        if (row >=0 && row < row_capac && col >= 0 && col < col_capac)
            return row * col_capac + col;
        else return -1;
    }

    private Iterable<Integer> neighbors(int row, int col, int row_capac, int col_capac) {
        Stack<Integer> res = new Stack<>();
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i == row && j == col)
                    continue;
                int neighbor = getIndex(i, j, row_capac, col_capac);
                if (neighbor != -1)
                    res.push(neighbor);
            }
        }
        return res;
    }

    private void depth_search(int row, int col, BoggleBoard board, StringBuffer currWord, SET<String> valid) {
        StringBuffer copy = new StringBuffer();
        for (int i = 0; i < currWord.length(); i++) {
            if (currWord.charAt(i) == 'Q')
                copy.append("QU");
            else
                copy.append(currWord.charAt(i));
        }
        String word = copy.toString();
        int rows = board.rows();
        int cols = board.cols();
        if (dict.get(word) == -2) {
            marked[row * cols + col] = false;
            currWord.deleteCharAt(currWord.length() - 1);
            return;
        }
        if (dict.contains(word) && !valid.contains(word) && word.length() > 2)
            valid.add(word);
        for (int neighbor : neighbors(row, col, rows, cols)) {
            if (!marked[neighbor]) {
                currWord.append(board.getLetter(neighbor / cols, neighbor % cols));
                marked[neighbor] = true;
                depth_search(neighbor / cols, neighbor % cols, board, currWord, valid);
            }
        }
        marked[row * cols + col] = false;
        currWord.deleteCharAt(currWord.length() - 1);
    }

    private void depth_search_nonrecur(Stack<Integer> pathstore, Stack<Node> prenodes,
                                       BoggleBoard board, StringBuffer currWord, SET<String> valid) {
        int rows = board.rows();
        int cols = board.cols();
        int curr = pathstore.pop();
        if (marked[curr]) {
            marked[curr] = false;
            currWord.deleteCharAt(currWord.length() - 1);
            prenodes.pop();
            return;
        }
        int row = curr / cols;
        int col = curr % cols;
        currWord.append(board.getLetter(curr / cols, curr % cols));
        marked[curr] = true;
        StringBuffer copy = new StringBuffer();
        for (int i = 0; i < currWord.length(); i++) {
            if (currWord.charAt(i) == 'Q')
                copy.append("QU");
            else
                copy.append(currWord.charAt(i));
        }
        String word = copy.toString();
        Node prenode = prenodes.iterator().next();
        int begin_length;
        if (word.length() < 2)
            begin_length = 0;
        else
            begin_length = word.charAt(word.length() - 2) == 'Q' ? word.length() - 2 : word.length() - 1;
        if (dict.get(prenode, word, begin_length) == null) {
            marked[curr] = false;
            pathstore.pop();
            currWord.deleteCharAt(currWord.length() - 1);
            return;
        }
        prenodes.push(dict.get(prenode, word, begin_length));
//        if (dict.get(word) == -2) {
//            marked[curr] = false;
//            pathstore.pop();
//            currWord.deleteCharAt(currWord.length() - 1);
//            return;
//        }
        if (dict.contains(word) && !valid.contains(word) && word.length() > 2)
            valid.add(word);
        for (int neighbor : neighbors(row, col, rows, cols)) {
            if (!marked[neighbor]) {
                pathstore.push(neighbor);
                pathstore.push(neighbor);
            }
        }
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        SET<String> res = new SET<>();
        int rows = board.rows();
        int cols = board.cols();
        marked = new boolean[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                marked[i * cols + j] = true;
                char c = board.getLetter(i, j);
//                node = dict.root.next[c - 65];
                StringBuffer begin = new StringBuffer("" + board.getLetter(i, j));
                depth_search(i, j, board, begin, res);
//                Stack<Integer> pathstore = new Stack<>();
//                Stack<Node> prenodes = new Stack<>();
//                StringBuffer begin = new StringBuffer();
//                pathstore.push(i * cols + j);
//                pathstore.push(i * cols + j);
//                prenodes.push(dict.root);
//                while (!pathstore.isEmpty()) {
//                    depth_search_nonrecur(pathstore, prenodes, board, begin, res);
//                }
            }
        }
        return res;
    }

    public int scoreOf(String word) {
        if (dict.contains(word)) {
            int length = word.length();
            if (length <= 2) return 0;
            if (length <= 4) return 1;
            if (length == 5) return 2;
            if (length == 6) return 3;
            if (length == 7) return 5;
            return 11;
        }
        return 0;
    }

    public static void main(String[] args) {
        In in = new In("C:\\Users\\JXT\\IdeaProjects\\Boggle\\test\\dictionary-algs4.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("C:\\Users\\JXT\\IdeaProjects\\Boggle\\test\\board4x4.txt");
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
