package tools;

import data.ClassLabels;
import data.Dataset;
import data.Pattern;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Parses time series data. Each line of the data file must have
 * the following format:
 * <p>
 * <pre>
 *    y x_1 x_2 ... x_n
 * </pre>
 * The first entry is an integer representing a class label. The following entries are real-valued elements of a time
 * series. All entries must be separated by a space.
 *
 * @author jain
 */
public class Reader {

    /**
     * Returns a list of labeled time series contained in the specified file. Every row represents a time series. Values
     * are separated by the specified regular expression. If the boolean variable labelsFirst is true, then the first
     * element of every row is interpreted as class label. Otherwise the last element of a row is interpreted as label.
     * The labels are transformed to the interval [0, k-1], where k is the number of labels. Labels are sorted in the
     * order of appearance. To keep track of the labels, a ClassLabel object cl must be passed. The object cl can be
     * null or empty if this method is called for the first time for a given problem.
     *
     * @param file        name of file
     * @param regexp      delimiter
     * @param labelsFirst true if labels come first
     * @return list of labeled time series
     */
    private static Dataset load(String file, ClassLabels cl, String regexp, boolean labelsFirst) {
        BufferedReader br = null;
        String line = "";

        ArrayList<Pattern> X = new ArrayList<>();
        if (cl == null) {
            cl = new ClassLabels();
        }
        try {
            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equals("")) {
                    continue;
                }
                String[] row = line.split(regexp);
                int posLabel = labelsFirst ? 0 : row.length - 1;
                double[] x;
                if (labelsFirst) {
                    x = getFeatures_lf(row);
                } else {
                    x = getFeatures_ll(row);
                }
                String label = row[posLabel];
                cl.putIfAbsent(label, cl.size());
                X.add(new Pattern(x, cl.get(label)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new Dataset(X, cl.size());
    }

    /**
     * Returns a list of labeled time series contained in the specified file. The first element of each line is a class
     * label. Values within a line are spearated by blank spaces. This format is used for loading datasets in the format
     * of the TestLVQ time series datasets.
     *
     * @param filename name of file
     * @return list of labeled time series
     */
    public static Dataset load(String filename, ClassLabels cl) {

        return load(filename, cl, "\\s+", true);
    }

    private static double[] getFeatures_lf(String[] row) {
        int n = row.length;
        double[] x = new double[n - 1];
        for (int i = 1; i < n; i++) {
            x[i - 1] = Double.valueOf(row[i]);
        }
        return x;
    }

    private static double[] getFeatures_ll(String[] row) {
        int n = row.length - 1;
        double[] x = new double[n + 1];
        for (int i = 0; i < n; i++) {
            x[i] = Double.valueOf(row[i]);
        }
        return x;
    }
}