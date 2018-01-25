package data;

import tools.Msg;
import tools.Rand;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Represents a dataset of labeled patterns. Class labels are internally
 * represented as integers from <code>0</code> to <code>n-1</code>, where
 * <code>n</code> is the number of distinct class labels.
 *
 * @author jain
 */
@SuppressWarnings("serial")
public class Dataset extends ArrayList<Pattern> {

    private static final Rand RND = Rand.getInstance();


    private final int m_numLabels;      // number of labels
    private String m_name;              // name of dataset

    /**
     * Creates dataset.
     */
    public Dataset(ArrayList<Pattern> patterns, int numLabels) {
        m_numLabels = numLabels;
        m_name = "unknown dataset";
        addAll(patterns);
    }

    public Dataset(String name, int numLabels) {
        m_name = name;
        m_numLabels = numLabels;
    }

    public Dataset(int numLabels) {
        m_name = "unknown dataset";
        m_numLabels = numLabels;
    }

    /**
     * Returns a deep copy of this dataset.
     *
     * @return copy of this dataset
     */
    public Dataset cp() {
        Dataset X = new Dataset(m_name, m_numLabels);
        X.addAll(stream().map(Pattern::cp).collect(Collectors.toList()));
        return X;
    }

    /**
     * Returns feature sequence of pattern at specified index.
     *
     * @param index index of pattern
     * @return eature sequence
     */
    public double[] pattern(int index) {
        return get(index).m_x;
    }

    /**
     * Returns label of pattern at specified index.
     *
     * @param index of pattern
     * @return class label
     */
    public int label(int index) {
        return get(index).m_label;
    }

    public int numLabels() {
        return m_numLabels;
    }

    /**
     * Returns maximum length over all time series in this dataset.
     *
     * @return maximum length
     */
    public int maxLength() {
        return this.size();
    }

    /**
     * Returns m_name of this dataset.
     *
     * @return m_name
     */
    public String name() {
        return m_name;
    }

    /**
     * Sets m_name of this dataset.
     *
     * @return m_name
     */
    public void setName(String name) {
        m_name = name;
    }


    /**
     * Returns class-wise partition of specified datasets.
     */
    public Dataset[] splitClasswise() {
        Dataset[] folds = new Dataset[m_numLabels];
        for (int i = 0; i < m_numLabels; i++) {
            folds[i] = new Dataset(m_numLabels);
        }
        for (Pattern x : this) {
            folds[x.label()].add(x);
        }
        return folds;
    }

    /**
     * Returns partition of specified datasets into the specified number of folds.
     *
     * @param numFolds the number of folds
     * @return class-wise partition of this dataset
     */
    public Dataset[] splitFolds(int numFolds) {

        if (numFolds < 1) {
            Msg.error("Invalid number of folds: %d.", numFolds);
        }
        Dataset[] fold = new Dataset[numFolds];
        for (int i = 0; i < numFolds; i++) {
            fold[i] = new Dataset(m_numLabels);
        }

        if (numFolds == size()) {
            for (int i = 0; i < numFolds; i++) {
                fold[i].add(get(i));
            }
            return fold;
        }
        Dataset[] split = splitClasswise();
        for (int i = 0; i < m_numLabels; i++) {
            int m = split[i].size();
            int[] pi = RND.shuffle(m);
            for (int j = 0; j < m; j++) {
                int k = pi[j] % numFolds;
                fold[k].add(split[i].get(j));
            }

        }
        return fold;
    }
}
