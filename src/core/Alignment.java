package core;

import data.Pattern;

public final class Alignment {

    private DTW m_distance;
    private Pattern m_x;
    private Pattern m_w;
    private int[][] m_path;
    private double m_dis;

    public Alignment(Pattern x, Pattern w) {
        m_distance = new DTW();
        m_w = w;
        m_x = x;
        m_dis = m_distance.d(x, w);
        m_path = m_distance.path();
        align(x, w);
    }

    public double d() {
        return m_dis;
    }


    public Pattern update(double ax, double aw) {
        double[] x = m_x.sequence();
        double[] w = m_w.sequence();
        int[][] p = m_path;
        int n = p.length;
        int i, j;
        for (int t = 0; t < n; t++) {
            i = p[t][0];
            j = p[t][1];
            w[j] = ax * x[i] + aw * w[j];
        }
        m_w.set(w);
        return m_w;
    }

    private void align(Pattern x, Pattern w) {
        m_w = w;
        m_x = x;
        m_dis = m_distance.d(x, w);
        m_path = m_distance.path();
    }
}
