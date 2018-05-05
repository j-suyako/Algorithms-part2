import edu.princeton.cs.algs4.Alphabet;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdOut;

public class MoveToFront {

    private static int R = 256;

    public static void encode() {
        String s = BinaryStdIn.readString();
        int N = s.length();
        char[] alphabet = new char[R];
        for (char i = 0; i < R; i++)
            alphabet[i] = i;
        char pre = 256;
        int max = 0;
//        int[] out = new int[N];
//        while (!BinaryStdIn.isEmpty()) {
        for (int i = 0; i < N; i++) {
            char curr = s.charAt(i);
//            char curr = BinaryStdIn.readChar();
            if (curr == pre) {
//                out[i] = 0;
//                if (pre == '\n') continue;
                BinaryStdOut.write('\0');
            } else {
                pre = curr;
                char index = alphabet[curr];
//                out[i] = index;
                BinaryStdOut.write(index);
                alphabet[curr] = 0;
                if (index > max)
                    max = index;
                for (int j = 0; j <= max; j++) {
                    if (alphabet[j] < index && j != curr)
                        alphabet[j]++;
                }
            }
        }
        BinaryStdOut.close();
//        return out;
    }

    public static void decode() {
        char[] alphabet = new char[R];
        for (char i = 0; i < R; i++)
            alphabet[i] = i;
        int max = 0;
        char pre_k = 0;
//        char[] out = new char[a.length];
//        for (int i = 0; i < a.length; i++) {
        while(!BinaryStdIn.isEmpty()) {
            char curr = BinaryStdIn.readChar();
//            char curr = a[i];
            if (curr == 0) {
//                out[i] = pre_k;
//                out[i] = alphabet[0];
                BinaryStdOut.write(pre_k);
            } else {
                char k = 0;
                while (alphabet[k] != curr)
                    k++;
                BinaryStdOut.write(k);
//                out[i] = k;
                pre_k = k;
                char index = alphabet[k];
                alphabet[k] = 0;
                if (index > max)
                    max = index;
                for (int j = 0; j <= max; j++) {
                    if (alphabet[j] < index && j != k)
                        alphabet[j]++;
                }
            }
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
//        encode("ABRACADABRA!");
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();
//        char[] a = {65, 0, 82, 2, 68, 1, 69, 1, 4, 4, 2, 38};
//        encode("Article I.\n" +
//                "\n" +
//                "Congress shall make no law respe");
//        decode(a);
    }
}
