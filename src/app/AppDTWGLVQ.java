package app;

import centroid.TSA;
import cluster.KMeans;
import core.Options;
import data.ClassLabels;
import data.Dataset;
import data.Transform;
import elvq.DTWGLVQ;
import elvq.Evaluation;
import tools.Array;
import tools.Msg;
import tools.Rand;
import tools.Reader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

class AppDTWGLVQ {

    //--- properties
    private static final String props = "./resources/DTWGLVQ.properties";


    //### OPTIONS ##############################################################

    //--- path to directory containing time series datasets
    private String path;

    //--- dataset in UCR format
    private String ucr;

    //--- file of training set
    private String trainfile;

    //--- file of test set
    private String testfile;

    //--- number of cross-validation folds
    private int K;

    //--- toggles z-normalization of time series
    private int Z;

    //--- number of prototypes per class
    private int k;

    //--- options for algorithms
    private String optTSA;
    private String optKmeans;
    private String optGLVQ;

    //--- seed for random numbers
    private int seed;


    public static void main(String[] args) {
        AppDTWGLVQ lvq = new AppDTWGLVQ();
        lvq.apply();
    }

    private AppDTWGLVQ() {

        Properties p = properties();
        path = p.getProperty("path");
        ucr =  p.getProperty("ucr");
        trainfile = p.getProperty("train");
        testfile = p.getProperty("test", "");
        K = Integer.parseInt(p.getProperty("K"));
        Z = Integer.parseInt(p.getProperty("Z"));
        optTSA = p.getProperty("optTSA");
        optKmeans = p.getProperty("optKmeans");
        optGLVQ = p.getProperty("optGLVQ");
        seed = Integer.parseInt(p.getProperty("seed"));
    }

    public void apply() {
        Rand.SEED = seed;

        // get data
        setFileNames();
        ClassLabels cl = new ClassLabels();
        Dataset train = load(trainfile, cl);
        Dataset test = load(testfile, cl);

        // check existence
        if(train == null) {
            Msg.error("Error. Training set is missing.");
        }
        if(test == null && K < 2) {
            Msg.error("Error. Test set is missing.");
        }

        // normalize
        if (Z > 0) {
            train = Transform.normalize(train);
            test = test != null? Transform.normalize(test) : null;
        }

        // evaluate
        if(K < 2) {
            holdout(train, test);
        } else {
            if(test != null) {
                train.addAll(test);
            }
            crossval(train);
        }
    }

    private Properties properties() {
        Properties p = new Properties();
        try {
            java.io.Reader reader = new FileReader(props);
            p.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }

    private Dataset load(String filename, ClassLabels cl) {
        File f = new File(filename);
        if(f.isFile()) {
            return Reader.load(filename, cl);
        }
        return null;
    }

    private void setFileNames() {
        boolean isUCR = !ucr.equals("0");
        if(isUCR) {
            String f = path + ucr + "/" + ucr;
            trainfile = f + "_TRAIN";
            testfile =  f + "_TEST";
        } else {
            trainfile = path + trainfile;
            testfile = path + testfile;
        }
    }

    private void holdout(Dataset train, Dataset test) {

        // set number of prototypes per class
        k = (new Options(optGLVQ)).getInt("-k");

        TSA tsa = TSA.getCentroid(optTSA);
        KMeans kmeans = new KMeans(tsa, optKmeans);
        DTWGLVQ glvq = new DTWGLVQ(optGLVQ);

        Evaluation e = new Evaluation(kmeans, k);
        double acc = e.eval(glvq, train, test);

        info(acc);
    }


    private void crossval(Dataset X) {

        // set number of prototypes per class
        k = (new Options(optGLVQ)).getInt("-k");

        TSA tsa = TSA.getCentroid(optTSA);
        KMeans kmeans = new KMeans(tsa, optKmeans);
        DTWGLVQ glvq = new DTWGLVQ(optGLVQ);

        Evaluation e = new Evaluation(kmeans, k);
        double[] acc = e.eval(glvq, X, K);

        info(acc);
    }

    private void info(double acc) {
        System.out.println();
        System.out.print("AppDTWGLVQ with" + " k = " + k + ": ");
        System.out.format("%5.2f %n", acc);
    }

    private void info(double[] acc) {
        double avg = Array.mean(acc);
        double std = Array.std(acc, avg);
        System.out.println();
        System.out.print("AppDTWGLVQ with" + " k = " + k + ": ");
        System.out.format("%5.2f (%5.2f) : ", avg, std);
        for(int i = 0; i < K; i++) {
            System.out.format(" %7.4f", acc[i]);
        }
        System.out.println();
    }


}
