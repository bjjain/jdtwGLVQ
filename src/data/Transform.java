package data;

import tools.Array;
import tools.Rand;

import java.util.Arrays;

/**
 * Created by jain on 13/10/2016.
 */
@SuppressWarnings("Convert2streamapi")
public class Transform {

    private static final Rand RND = Rand.getInstance();

    /**
     * Z-Normalization of all time series patterns of the specified dataset.
     */
    public static Dataset normalize(Dataset X) {
        Dataset Y = new Dataset(X.name(), X.numLabels());
        for (Pattern x : X) {
            Y.add(normalize(x));
        }
        return Y;
    }

    public static Dataset perturbe(Dataset X) {
        Dataset Y = new Dataset(X.name(), X.numLabels());
        for (Pattern x : X) {
            Y.add(perturbe(x));
        }
        return Y;
    }


    public static Dataset setLength(Dataset X, int m) {
        Dataset Y = new Dataset(X.name(), X.numLabels());
        for (Pattern x : X) {
            Y.add(setLength(x, m));
        }
        return Y;
    }

    /**
     * Performs z-normalization with zero mean and unit standard deviation of specified pattern.
     */
    private static Pattern normalize(Pattern x) {
        x.set(Array.normalize(x.sequence()));
        return x;
    }

    private static Pattern perturbe(Pattern x) {
        double[] sx = x.sequence();
        int n = x.length();
        for (int i = 0; i < n; i++) {
            sx[i] = sx[i] + sx[i] * RND.nextGaussian() / 100.0;
        }
        x.set(sx);
        return x;
    }

    private static Pattern setLength(Pattern x, int m) {
        int n = x.length();
        if (m <= 0 || m == n) {
            return x;
        }
        double[] sx = x.sequence();
        double[] ssx = new double[m];
        int[] idx;
        if (m < n) {
            idx = RND.choose(m, n);
        } else {
            idx = new int[m];
            for (int i = 0; i < n; i++) {
                idx[i] = i;
            }
            for (int i = n; i < m; i++) {
                int i0 = RND.nextInt(n);
                idx[i] = i0;
            }
        }
        Arrays.sort(idx);
        for (int i = 0; i < m; i++) {
            ssx[i] = sx[idx[i]];
        }
        x.set(ssx);
        return x;
    }
}
