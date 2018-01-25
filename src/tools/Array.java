package tools;

import java.util.Arrays;

public final class Array {

    private static final String NULL = "<null>";

    private Array() {
    }

    public static int indexOfMin(double[] x) {
        if (x == null || x.length == 0) {
            Msg.error("Error. Array is empty.");
        }
        int index = 0;
        int n = x.length;
        for (int i = 1; i < n; i++) {
            if (x[i] < x[index]) {
                index = i;
            }
        }
        return index;
    }

    public static double[] cp(double[] x) {
        if (x == null) {
            return null;
        }
        return Arrays.copyOf(x, x.length);
    }

    public static double[][] cp(double[][] x) {
        if (x == null) {
            return null;
        }
        int n = x.length;
        double[][] y = new double[n][];
        for (int i = 0; i < n; i++) {
            y[i] = cp(x[i]);
        }
        return y;
    }

    public static int max(int[] x) {
        int n = x.length;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            if (max < x[i]) {
                max = x[i];
            }
        }
        return max;
    }

    public static double max(double[] x) {
        int n = x.length;
        double max = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < n; i++) {
            if (max < x[i]) {
                max = x[i];
            }
        }
        return max;
    }

    public static double min(double[] x) {
        int n = x.length;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < n; i++) {
            if (x[i] < min) {
                min = x[i];
            }
        }
        return min;
    }

    private static double sum(double[] x) {
        if (x == null) {
            return 0;
        }
        int n = x.length;
        double s = 0;
        for (int i = 0; i < n; i++) {
            s += x[i];
        }
        return s;
    }

    private static double sum(double[][] x) {
        if (x == null) {
            return 0;
        }
        int n = x.length;
        double s = 0;
        for (int i = 0; i < n; i++) {
            s += sum(x[i]);
        }
        return s;
    }

    public static double[] rowsum(double[][] x) {
        if (x == null) {
            return null;
        }
        int n = x.length;
        double[] s = new double[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s[i] += x[i][j];
            }
        }
        return s;
    }

    public static double[] normalize(double[] x) {
        if (x == null) {
            return x;
        }
        int n = x.length;
        double mean = mean(x);
        double std = std(x, mean);
        double[] z = new double[n];
        if (std == 0) {
            return z;
        }
        for (int i = 0; i < n; i++) {
            z[i] = (x[i] - mean) / std;
        }
        return z;
    }

    public static double[][] normalize(double[][] x) {
        if (x == null) {
            return x;
        }
        int n = x.length;
        int m = x[0].length;
        double mean = mean(x);
        double std = std(x, mean);
        double[][] z = new double[n][m];
        if (std == 0) {
            return z;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                z[i][j] = (x[i][j] - mean) / std;
            }
        }
        return z;
    }

    /***
     * Statistics
     ***********************************************************/
    public static double mean(double[] x) {
        if (x == null || x.length == 0) {
            return 0;
        }
        return Array.sum(x) / ((double) x.length);
    }

    private static double mean(double[][] x) {
        if (x == null || x.length == 0) {
            return 0;
        }
        if (x[0] == null || x[0].length == 0) {
            return 0;
        }
        double n = x.length * x[0].length;
        return Array.sum(x) / n;
    }

    private static double var(double[] x, double mean) {
        if (x == null || x.length == 0) {
            return 0;
        }
        double v = 0;
        int n = x.length;
        for (int i = 0; i < n; i++) {
            v += Math.pow(x[i] - mean, 2);
        }
        return v / ((double) n);
    }

    private static double var(double[][] x, double mean) {
        if (x == null || x.length == 0) {
            return 0;
        }
        if (x[0] == null || x[0].length == 0) {
            return 0;
        }
        double v = 0;
        int dim = x.length;
        for (int i = 0; i < dim; i++) {
            v += var(x[i], mean);
        }
        double n = x.length * x[0].length;
        return v / n;
    }

    public static double std(double[] x, double mean) {
        return Math.sqrt(var(x, mean));
    }

    private static double std(double[][] x, double mean) {
        return Math.sqrt(var(x, mean));
    }

    /***
     * Converters
     ***********************************************************/
    public static int[] toIntArray(double[] x) {
        if (x == null) {
            return null;
        }
        int n = x.length;
        int[] y = new int[n];
        for (int i = 0; i < n; i++) {
            y[i] = (int) x[i];
        }
        return y;
    }

    public static double[] toDoubleArray(String[] x) {
        if (x == null) {
            return null;
        }
        int n = x.length;
        double[] y = new double[n];
        for (int i = 0; i < n; i++) {
            y[i] = Double.parseDouble(x[i]);
        }
        return y;
    }

    private static String toString(double[] x, String format) {
        if (x == null) {
            return NULL;
        }
        StringBuilder s = new StringBuilder();
        int n = x.length;
        for (int i = 0; i < n; i++) {
            s.append(String.format(format, x[i]));
        }
        return s.toString();
    }

    public static String toString(double[][] x, String format) {
        if (x == null) {
            return NULL;
        }
        StringBuilder s = new StringBuilder();
        int n = x.length;
        for (int i = 0; i < n; i++) {
            s.append(toString(x[i], format)).append("\n");
        }
        return s.toString();
    }


    public static String toString(String[] x) {
        if (x == null) {
            return "";
        }
        StringBuilder s = new StringBuilder();
        int n = x.length;
        for (int i = 0; i < n; i++) {
            s.append(x[i]).append(" ");
        }
        return s.toString();
    }

    public static String[] toStringArray(int[] x) {
        if (x == null) {
            return null;
        }
        int n = x.length;
        String[] s = new String[n];
        for (int i = 0; i < n; i++) {
            s[i] = String.valueOf(x[i]);
        }
        return s;
    }

    public static String[] toStringArray(double[] x) {
        if (x == null) {
            return null;
        }
        int n = x.length;
        String[] s = new String[n];
        for (int i = 0; i < n; i++) {
            s[i] = String.valueOf(x[i]);
        }
        return s;
    }
}
