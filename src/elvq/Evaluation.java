package elvq;

import cluster.KMeans;
import data.Dataset;
import tools.Msg;

/**
 * Created by jain on 17/03/2017.
 */
public class Evaluation {

    // k-means algorithm
    private KMeans m_kmeans;
    // number of prototypes per class
    private int m_k;

    public Evaluation(KMeans kmeans, int k) {
        m_kmeans = kmeans;
        m_k = k;
    }

    public double eval(DTWGLVQ glvq, Dataset train, Dataset test) {
        Dataset codebook = m_kmeans.fit(train, m_k);
        glvq.fit(train, codebook);
        return glvq.score(train);
    }


    public double[] eval(DTWGLVQ glvq, Dataset X, int numFolds) {
        int nfolds = checkfolds(X, numFolds);
        boolean isLoggable = DTWGLVQ.OUT_VAL <= glvq.verbosity();
        if (isLoggable) {
            System.out.println(nfolds + "-fold cross-validation:");
        }
        Dataset[] folds = X.splitFolds(nfolds);
        double[] acc = new double[nfolds];
        for (int i = 0; i < nfolds; i++) {
            Dataset train = merge(folds, i);
            Dataset test = folds[i];
            Dataset codebook = m_kmeans.fit(train, m_k);
            glvq.fit(train, codebook);
            acc[i] = glvq.score(test);
            if(isLoggable) {
                System.out.printf("[CV-DTWGLVQ] accuracy on fold %d: %6.2f%n", i, acc[i]);
            }
        }
        return acc;
    }

    private static int checkfolds(Dataset X, int numFolds) {
        if (numFolds < 1) {
            Msg.error("Invalid number of cv-folds: %d\n", numFolds);
        } else if (numFolds == 1) {
            numFolds = X.size();
        } else if (X.size() < numFolds) {
            numFolds = X.size();
            Msg.warn("Warning! #folds is larger than #data. Perform LOOV.");
        }
        return numFolds;
    }

    private static Dataset merge(Dataset[] fold, int index) {
        Dataset train = new Dataset(fold[0].numLabels());
        int numFolds = fold.length;
        for (int i = 0; i < numFolds; i++) {
            if (i != index) {
                train.addAll(fold[i]);
            }
        }
        return train;
    }
}
