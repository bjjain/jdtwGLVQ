package elvq;

import core.Alignment;
import core.DTW;
import core.Options;
import data.Dataset;
import data.Pattern;
import tools.Array;
import tools.Rand;

public class DTWGLVQ {

    private static final Rand RND = Rand.getInstance();

    // output modes
    static final int OUT_NIL = 0;               // quiet mode
    static final int OUT_VAL = 1;               // cross validation mode
    private static final int OUT_FIT = 2;       // training mode

    private Parameter m_params;            // parameters
    private Dataset m_Y;                   // protoytpes

    public DTWGLVQ(String opts) {
        m_params = new Parameter(opts);
    }

    public Options getOptions() {
        return m_params.opts;
    }

    public Dataset getPrototypes() {
        return m_Y;
    }

    private int predict(Pattern x) {
        double[] d = DTW.d(x, m_Y);
        int i0 = Array.indexOfMin(d);
        return m_Y.label(i0);
    }

    public double score(Dataset X) {
        double numX = X.size();
        if(numX == 0) {
            return 1;
        }
        int acc = 0;
        for(Pattern x : X) {
            int predictedLabel = predict(x);
            if(predictedLabel == x.label()) {
                acc++;
            }
        }
        return acc/numX;
    }

    public void fit(Dataset X, Dataset Y) {

        // monitoring
        double maxAcc = 0;              // minimum error on training set
        double curAcc = 0;              // current training error
        int numStable = 0;              // number of epochs without improvement

        // output mode
        boolean loggable = m_params.o == OUT_FIT;

        // initialization
        m_Y = Y.cp();
        Dataset Yopt = m_Y.cp();
        int numX = X.size();
        int numY = Y.size();

        int T = m_params.T;
        int maxStable = m_params.t;
        double g = m_params.g;
        double eta = m_params.l;
        boolean isActive = true;
        for (int t = 1; t <= T && isActive; t++) {

            int[] f = RND.shuffle(numX);
            for (int i = 0; i < numX; i++) {
                Pattern x = X.get(f[i]);

                double d1 = Double.POSITIVE_INFINITY;
                double d2 = Double.POSITIVE_INFINITY;
                Alignment a1 = null;
                Alignment a2 = null;

                int xlabel = x.label();
                for (int j = 0; j < numY; j++) {
                    Pattern y = m_Y.get(j);
                    Alignment a = new Alignment(x, y);
                    double d = a.d();
                    if (xlabel == y.label()) {
                        if (d < d1) {
                            d1 = d;
                            a1 = a;
                        }
                    } else {
                        if (d < d2) {
                            d2 = d;
                            a2 = a;
                        }
                    }
                }
                double sum_d = d1 + d2;
                double mu = (d1 - d2) / sum_d;
                double sgn = 1.0 / (1.0 + Math.exp(-mu*g*t));
                double delta = sgn * (1.0 - sgn) / (sum_d * sum_d);
                double delta1 = d2 * delta;
                double delta2 = -d1 * delta;
                a1.update(delta1, 1.0 - delta1);
                a2.update(delta2, 1.0 - delta2);

            }

            // check convergence
            numStable++;
            curAcc = score(X);
            if (maxAcc < curAcc) {
                maxAcc = curAcc;
                Yopt = m_Y.cp();
                numStable = 0;
            }
            isActive = maxAcc < 1.0 && numStable <= maxStable;

            // report
            if(loggable) {
                System.out.printf("[DTWGLVQ] %5d  train = %1.3f (%1.3f)%n", t, curAcc, maxAcc);
                if (!isActive) {
                    System.out.println();
                }
            }
        }
        m_Y = Yopt;
    }


    public int verbosity() {
        return m_params.o;
    }

    public String toString() {
        return "DTWGLVQ " + m_params.getOptions();
    }
}
