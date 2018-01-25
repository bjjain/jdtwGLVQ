package tools;


import java.util.Random;

public class Rand extends Random {
    private static final long serialVersionUID = 1L;
    public static int SEED = -1;

    private Rand() {
        super();
    }

    private Rand(int seed) {
        super(seed);
    }

    public static Rand getInstance() {
        return SEED < 0 ? new Rand() : new Rand(SEED);
    }

    public int[] shuffle(int n){
        int[] x = new int[n];
        for(int i = 0; i < n; i++){
            x[i] = i;
        }
        for (int i = 0; i < n; i++) {
            int pos = nextInt(n);
            int tmp = x[i];
            x[i] = x[pos];
            x[pos] = tmp;
        }
        return x;
    }

    public int[] choose(int k, int n) {
        if (k < 0) {
            return null;
        }
        if (k == 0) {
            return new int[0];
        }
        if (n == 1) {
            return new int[1];
        }
        if (k == n) {
            return arr(n);
        }

        int[] set = arr(n);

        if (k >= n) {
            return set;
        }

        int[] result = new int[k];
        int index;
        for (int i = 0; i < k; i++) {
            index = nextInt(n - i);
            result[i] = set[index];
            set[index] = set[n - i - 1];
        }
        return result;
    }

    private int[] arr(int n) {
        int[] x = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = i;
        }
        return x;
    }
}
