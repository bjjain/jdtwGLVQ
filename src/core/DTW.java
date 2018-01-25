package core;

import data.Dataset;
import data.Pattern;


public class DTW {

    private double[][] m_d;
    private int m_nx;
    private int m_ny;

    public static double[][] d(Dataset X, Dataset Y) {
        int n = X.size();
        int m = Y.size();
        double[][] d = new double[n][m];
        DTW distance = new DTW();
        for (int i = 0; i < n; i++) {
            Pattern x = X.get(i);
            for (int j = 0; j < m; j++) {
                d[i][j] = distance.d(x, Y.get(j));
            }
        }
        return d;
    }

    public static double[] d(Pattern x, Dataset Y) {
        int m = Y.size();
        double[] d = new double[m];
        DTW distance = new DTW();
        for (int i = 0; i < m; i++) {
            d[i] = distance.d(x, Y.get(i));
        }
        return d;
    }

    public double d(Pattern x, Pattern y) {
        return d(x.sequence(), y.sequence());
    }

    public int[][] path() {

        final int LEFT = 0;
        final int UP = 1;
        final int DIAG = 2;

        int[][] path = new int[m_nx + m_ny][2];
        int ix = m_nx - 1;
        int iy = m_ny - 1;

        path[0][0] = ix;
        path[0][1] = iy;

        int n = 1;
        int direction;
        double min;
        while (ix != 0 || iy != 0) {

            if (ix == 0) {
                iy--;
            } else if (iy == 0) {
                ix--;
            } else {
                min = m_d[ix - 1][iy - 1];
                direction = DIAG;
                if (m_d[ix][iy - 1] < min) {
                    min = m_d[ix][iy - 1];
                    direction = UP;
                }
                if (m_d[ix - 1][iy] < min) {
                    direction = LEFT;
                }
                if (direction == LEFT) {
                    ix--;
                } else if (direction == UP) {
                    iy--;
                } else {
                    ix--;
                    iy--;
                }
            }
            path[n][0] = ix;
            path[n][1] = iy;
            n++;
        }

        // reverse order and trim
        int[][] p = new int[n][2];
        int m = n - 1;
        for (int i = 0; i < n; i++) {
            p[i] = path[m - i];
        }
        return p;
    }

    public double d(double[] x, double[] y) {

        m_nx = x.length;
        m_ny = y.length;

        if (m_nx == 0 || m_ny == 0) {
            return Double.NaN;
        }

        int i, j;
        m_d = new double[m_nx][m_ny];

        double cost = x[0] - y[0];
        m_d[0][0] = cost * cost;
        for (i = 1; i < m_nx; i++) {
            cost = x[i] - y[0];
            m_d[i][0] = m_d[i - 1][0] + cost * cost;
        }
        for (j = 1; j < m_ny; j++) {
            cost = x[0] - y[j];
            m_d[0][j] = m_d[0][j - 1] + cost * cost;
        }

        double min;
        for (i = 1; i < m_nx; i++) {
            for (j = 1; j < m_ny; j++) {
                min = m_d[i - 1][j];
                if (m_d[i][j - 1] < min) {
                    min = m_d[i][j - 1];
                }
                if (m_d[i - 1][j - 1] < min) {
                    min = m_d[i - 1][j - 1];
                }
                cost = x[i] - y[j];
                m_d[i][j] = min + cost * cost;
            }
        }
        return Math.sqrt(m_d[m_nx - 1][m_ny - 1]);
    }

    public int[][] align(double[] x, double[] y) {
        d(x, y);
        return path();
    }

    public String toString() {
        return "DTW";
    }
}
