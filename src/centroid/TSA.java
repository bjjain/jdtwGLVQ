package centroid;

import core.DTW;
import data.Dataset;
import data.Pattern;
import tools.Rand;

/**
 * Abstract class for time series averaging.
 *
 * Created by jain on 11/07/16.
 */
public abstract class TSA {

    static final Rand RND = Rand.getInstance();

    // distance function
    DTW dtw = new DTW();

    // parameters
    Parameter m_param;

    TSA(Parameter param) {
        m_param = param;
    }

    public static TSA getCentroid(String opts) {
        Parameter param = new Parameter(opts);
        int A = param.A;
        if (A == 1) {
            return new DBA(param);
        } else if (A == 2) {
            return new SSG(param);
        } else {
            param.error("-A", A);
            return null;
        }
    }

    public abstract Pattern compute(Dataset X);

    protected abstract String name();

    public String options() {
        return name() + " " + m_param.getOptions();
    }

    double[][] getSequences(Dataset X) {
        int N = X.size();
        double[][] x = new double[N][];
        for (int i = 0; i < N; i++) {
            x[i] = X.get(i).sequence();
        }
        return x;
    }

    double f(double[] z, double[][] x) {
        int n = x.length;
        double val = 0;
        for (int i = 0; i < n; i++) {
            val += dtw.d(z, x[i]);
        }
        return val / n;
    }
}
