package centroid;

import data.Dataset;
import data.Pattern;

import java.util.Arrays;

/**
 * DBA Algorithm for mean computation.
 * <p>
 * Created by jain on 11/07/16.
 */
public class DBA extends TSA {


    DBA(Parameter params) {
        super(params);
    }

    public Pattern compute(Dataset X) {
        Pattern y = X.get(RND.nextInt(X.size())).cp();
        double[] my = y.sequence();
        double[][] mx = getSequences(X);
        double[] m = compute(my, mx);
        return new Pattern(m, y.label());
    }

    private double[] compute(double[] mean, double[][] x) {

        // sample size
        int N = x.length;
        // dimension
        int n = mean.length;
        // previous V
        double fprev;
        // next V
        double fnext = 0;
        // warping path
        int[][] w;
        // valence matrix
        int[] V = new int[n];
        // output mode
        boolean loggable = m_param.o == 1;

        // auxiliary variables
        double[] z = new double[n];
        double diff = 1.0;
        double eps = 1E-4;
        int T = m_param.T;
        for (int epoch = 0; epoch < T && eps < diff; epoch++) {
            Arrays.fill(z, 0);
            Arrays.fill(V, 0);
            for (int i = 0; i < N; i++) {
                w = dtw.align(mean, x[i]);
                int l = w.length;
                for (int j = 0; j < l; j++) {
                    V[w[j][0]]++;
                    z[w[j][0]] += x[i][w[j][1]];
                }
            }
            for (int i = 0; i < n; i++) {
                mean[i] = z[i] / V[i];
            }
            fprev = fnext;
            fnext = f(mean, x);
            diff = Math.abs(fprev - fnext) / Math.max(fnext, eps);

            // output
            if(loggable) {
                System.out.format("[DBA] %d  %7.4f  %7.4f%n", epoch, fprev, fnext);
            }

        }
        return mean;
    }

    public String name() {
        return "DBA";
    }
}
