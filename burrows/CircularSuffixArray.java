import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
    private String origin;
    private int length;
    private int[] index;
    private char[] suffixs;
//    private char[][] suffixs;
    private int[] aux_index;

    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        origin = s;
        length = s.length();
        index = new int[length];
        for (int i = 0; i < length; i++)
            index[i] = i;
        suffixs = new char[length];  // 用来保存确定每一行的后缀的头一个单词
//        suffixs = new char[length][length];
        aux_index = new int[length];
        sort(s, 0, length, 0);
    }

    private char next(String s, int index) {
        if (index < s.length() - 1)
            index += 1;
        else
            index -= (s.length() - 1);
        return s.charAt(index);
    }

    private void sort(String a, int lo, int hi, int d) {
        if (hi - lo <= 1) return;
        int N = a.length();
        int[] count = new int[256];
        for (int i = 0; i < N; i++)
            count[a.charAt(i)] += 1;  // 每个字符出现加1
        for (int r = 1; r < 256; r++)
            count[r] += count[r - 1];
        for (int i = 0; i < N; i++) {
            aux_index[--count[a.charAt(i)] + lo] = index[lo + i];
            suffixs[count[a.charAt(i)] + lo] = a.charAt(i);
//            suffixs[d][count[a.charAt(i)] + lo] = a.charAt(i);
        }
        for (int i = lo; i < hi; i++)
            index[i] = aux_index[i];
        StringBuffer temp = new StringBuffer("" + next(origin, aux_index[lo] + d));
        for (int i = lo + 1; i < hi; i++) {
            if (suffixs[i] == suffixs[i - 1]) {
//            if (suffixs[d][i] == suffixs[d][i - 1]) {
                temp.append(next(origin, aux_index[i] + d));
//                if (i == hi - 1)
//                    sort(temp.toString(), i - temp.length() + 1, i + 1, d + 1);
            } else {
                sort(temp.toString(), i - temp.length(), i, d + 1);
                temp = new StringBuffer("" + next(origin, aux_index[i] + d));
            }
        }
        sort(temp.toString(), hi - temp.length(), hi, d + 1);
    }

    public int length() {
        return length;
    }

    public int index(int i) {
        if (i < 0 || i >= length) throw new IllegalArgumentException();
        return index[i];
    }

    public static void main(String[] args) {
        CircularSuffixArray test = new CircularSuffixArray("ABRACADABRA!");
        StdOut.print(test.length());
    }
}
