package cluster;

import centroid.TSA;
import core.DTW;
import data.Dataset;
import tools.Array;
import tools.Msg;


public class KMeans {

    private Parameter m_params;            // parameters
    private TSA m_mean;                    // centroid algorithm

    public KMeans(TSA mean, String opts) {
        m_params = new Parameter(opts);
        m_mean = mean;
    }

    public Dataset fit(Dataset X, int k) {
        Dataset[] folds = X.splitClasswise();
        Dataset Y = new Dataset(X.numLabels());
        for (Dataset fold : folds) {
            Y.addAll(cluster(fold, k));
        }
        return Y;
    }

    private Dataset cluster(Dataset X, int k) {

        // check dataset
        if (X == null || X.size() == 0) {
            Msg.warn("Dataset to be clustered is empty.");
            return new Dataset(0);
        }
        // size of dataset
        int numX = X.size();
        // number of labels
        int numLabels = X.numLabels();
        if (numX < k) {
            Msg.error("Number k of clusters exceeds size of dataset.");
        }

        // centroids
        Dataset Y = new Dataset(numLabels);

        //*** handle special case ***
        if (k == 1) {
            Y.add(m_mean.compute(X));
            return Y;
        }
        //*** END ***

        // initial set of centroids
        Kpp kpp = new Kpp();
        Y = kpp.selectPrototypes(X, k);

        // store best solution
        Dataset Yopt = Y.cp();

        // clusters
        Dataset[] C = new Dataset[k];
        for (int j = 0; j < k; j++) {
            C[j] = new Dataset(numLabels);
        }
        // output mode
        boolean loggable = m_params.o == 1;
        // best error so far
        double minError = Double.MAX_VALUE;
        // current error
        double curError;
        // max number of epochs
        int T = m_params.T;
        // max number of stable epochs
        int maxStable = m_params.t;
        // number of epochs without improvement
        int numStable = 0;
        // flag for termination
        boolean is_stable = false;


        //*** k-means procedure ***
        for (int t = 1; t <= T && !is_stable; t++) {
            // initialize
            curError = 0;
            for (int j = 0; j < k; j++) {
                C[j].clear();
            }
            // assign
            double[][] d = DTW.d(X, Y);
            for (int i = 0; i < numX; i++) {
                int j0 = Array.indexOfMin(d[i]);
                C[j0].add(X.get(i));
                curError += d[i][j0];
            }
            curError /= (double) k;
            // monitor
            if (curError < minError) {
                Yopt = Y.cp();
                minError = curError;
                numStable = 0;
            } else {
                numStable++;
                is_stable = numStable > maxStable;
            }
            // recompute
            Y.clear();
            for (int j = 0; j < k; j++) {
                Y.add(m_mean.compute(C[j]));
            }
            // report progress
            if (loggable) {
                String msg = "[kmeans] %5d  E = %6.3f (%6.3f)%n";
                System.out.printf(msg, t, curError, minError);
            }
        }
        return Yopt;
    }
}
