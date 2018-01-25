package cluster;

import core.DTW;
import data.Dataset;
import data.Pattern;
import tools.Msg;
import tools.Rand;

import java.util.Arrays;

/**
 * Created by jain on 14/10/2016.
 */
class Kpp {

    private static final Rand RND = Rand.getInstance();

    // DTW distance
    private DTW m_dtw;

    // dataset
    private Dataset m_X;

    // auxiliary attribtues
    private double[] m_distances;
    private boolean[] m_selected;

    Kpp() {
        m_dtw = new DTW();
    }

    Dataset selectPrototypes(Dataset X, int k) {
        check(X, k);
        m_X = X;
        int n = m_X.size();
        Dataset Y = new Dataset(X.numLabels());

        // CASE: n <= k
        if (n <= k) {
            return m_X.cp();
        }

        // CASE: 0 < k < n
        m_selected = new boolean[n];
        m_distances = new double[n];
        Arrays.fill(m_distances, Double.POSITIVE_INFINITY);

        // select first prototype
        int next = RND.nextInt(n);
        m_selected[next] = true;
        Pattern x = m_X.get(next).cp();
        Y.add(x);
        updateDistances(x);

        for (int i = 1; i < k; i++) {
            next = next();
            m_selected[next] = true;
            x = m_X.get(next).cp();
            Y.add(x);
            m_distances = updateDistances(x);
        }
        return Y;
    }


    private double[] updateDistances(Pattern x) {
        int n = m_selected.length;
        for (int i = 0; i < n; i++) {
            if (!m_selected[i]) {
                double d = m_dtw.d(m_X.get(i), x);
                m_distances[i] = Math.min(d, m_distances[i]);
            }
        }
        return m_distances;
    }

    private int next() {
        double sum = 0;
        int n = m_selected.length;
        double[] d = new double[n];
        for (int i = 0; i < n; i++) {
            d[i] = m_selected[i] ? 0 : m_distances[i] * m_distances[i];
            sum += d[i];
        }

        double p = RND.nextDouble();
        double sumProb = 0;
        for (int i = 0; i < n; i++) {
            if (!m_selected[i]) {
                sumProb += d[i] / sum;
                if (p <= sumProb) {
                    return i;
                }
            }
        }
        return n - 1;
    }

    private void check(Dataset X, int k) {
        if (X == null || X.size() == 0) {
            Msg.error("Error. Dataset is empty.");
        }
        if (k <= 0) {
            Msg.error("Error. Parameter k is not positive: %d.", k);
        }
    }
}
