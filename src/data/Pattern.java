package data;

import tools.Array;
import tools.Msg;

/**
 * This class represents a Pattern. A pattern consists of an univariate feature sequence and a class label.
 *
 * @author jain
 */
public class Pattern {

    int m_label;      // class label
    double[] m_x;     // feature sequence
    private int m_id;           // internal id

    /**
     * Creates a labeled time series pattern. The specified matrix must be rectangular and non-empty.
     *
     * @param x     time series
     * @param label class label
     */
    public Pattern(double[] x, int label) {
        check(x);
        m_x = x;
        m_label = label;
    }

    private Pattern(double[] x, int label, int id) {
        check(x);
        m_x = x;
        m_label = label;
        m_id = id;
    }

    private static void check(double[] x) {
        if (x == null || x.length == 0) {
            Msg.error("Error. Feature sequence is empty");
        }
    }

    public void set(double[] x) {
        check(x);
        m_x = x;
    }

    public void set(int id) {
        m_id = id;
    }

    /**
     * Returns feature sequence.
     *
     * @return time series
     */
    public double[] sequence() {
        return m_x;
    }

    /**
     * Returns class label.
     *
     * @return class label
     */
    public int label() {
        return m_label;
    }

    /**
     * Returns length of time series.
     *
     * @return length
     */
    public int length() {
        return m_x.length;
    }

    public int id() {
        return m_id;
    }

    /**
     * Returns norm of this time series.
     *
     * @return norm
     */
    public double norm() {
        double z = 0;
        int n = m_x.length;
        for(int i = 0; i < n; i++) {
            z += m_x[i]*m_x[i];
        }
        return Math.sqrt(z/((double)n));
    }


    /**
     * Returns deep copy of this pattern.
     *
     * @return copy of this pattern
     */
    public Pattern cp() {
        return new Pattern(Array.cp(m_x), m_label, m_id);
    }

}
