import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    private static char pre(String s, int index) {
        if (index <= 0)
            index += (s.length() - 1);
        else
            index -= 1;
        return s.charAt(index);
    }

    public static void transform() {
        String s = BinaryStdIn.readString();
        int N = s.length();
        CircularSuffixArray sortedSuffix = new CircularSuffixArray(s);
        StringBuffer t = new StringBuffer();
//        while (BinaryStdIn.isEmpty()) {
        for (int i = 0; i < N; i++) {
            if (sortedSuffix.index(i) == 0)
                BinaryStdOut.write(i);
            t.append(pre(s, sortedSuffix.index(i)));
//            BinaryStdOut.write();
        }
        BinaryStdOut.write(t.toString());
        BinaryStdOut.close();
    }

    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
//        BinaryStdOut.write(first);
        String t = BinaryStdIn.readString();
//        BinaryStdOut.write(t);
//        if (first != 3) assert false;
//        int first = t.charAt(0);
//        BinaryStdOut.write(first);
        StringBuffer res = new StringBuffer();
        int N = t.length();
        int[] next = new int[N];
        int[] count = new int[257];
        for (int i = 0; i < N; i++)
            count[t.charAt(i) + 1]++;
        for (int r = 0; r < 256; r++)
            count[r + 1] += count[r];
        for (int i = 0; i < N; i++)
            next[count[t.charAt(i)]++] = i;
        for (int i = 0; i < N; i++) {
            first = next[first];
            res.append(t.charAt(first));
        }
        BinaryStdOut.write(res.toString());
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        if (args[0].equals("+")) inverseTransform();
    }
}
