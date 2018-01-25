package centroid;

import data.Dataset;
import data.Pattern;

import java.util.Arrays;

/**
 * Stochastic Generalized Gradient Algorithm for mean computation.
 * <p>
 * Created by jain on 11/07/16.
 */
public class SSG extends TSA {

    SSG(Parameter params) {
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
        // best solution
        double[] m0 = Arrays.copyOf(mean, n);
        // best V
        double f0 = Double.POSITIVE_INFINITY;
        // current V
        double ft;
        // number of epochs without improvements
        int numStable = 0;
        // learning rate
        double eta = m_param.l0;
        // decay of learning rate
        double delta = (eta - m_param.l1) / N;
        // output mode
        boolean loggable = m_param.o == 1;

        // auxiliary variables
        int[][] p;
        double[] z = new double[n];
        int T = m_param.T;
        int maxStable = m_param.t;
        for (int epoch = 0; epoch < T && numStable <= maxStable; epoch++) {
            int[] pi = RND.shuffle(N);
            for (int i = 0; i < N; i++) {
                int i0 = pi[i];
                p = dtw.align(mean, x[i0]);

                int l = p.length;
                Arrays.fill(z, 0);
                for (int j = 0; j < l; j++) {
                    z[p[j][0]] += mean[p[j][0]] - x[i0][p[j][1]];
                }
                for (int j = 0; j < n; j++) {
                    mean[j] -= eta * z[j];
                }
                if (epoch == 0) {
                    eta -= delta;
                }
            }
            ft = f(mean, x);

            // update stability
            if (ft < f0) {
                f0 = ft;
                m0 = Arrays.copyOf(mean, n);
                numStable = 0;
            } else {
                numStable++;
            }

            // output
            if(loggable) {
                System.out.format("[SSG] %d  %7.4f  %7.4f%n", epoch, ft, f0);
            }
        }
        mean = m0;
        return mean;
    }

    public String name() {
        return "SGG";
    }
}
