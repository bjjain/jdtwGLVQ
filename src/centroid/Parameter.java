package centroid;

import core.Options;

class Parameter {

    // mean algorithms
    static final int DBA = 1;
    static final int SGG = 2;

    // mean algorithm
    int A = 1;

    // initial learning rate (SSG)
    double l0 = 0.1;

    // final learning rate (SSG)
    double l1 = 0.01;

    // termination: maximum number of epochs
    int T = 50;

    // termination: maximum number of epochs without improvement
    int t = 5;

    // output mode
    int o = 2;

    // options
    private Options opts;

    private Parameter() {
        setOptions();
    }

    Parameter(String opts) {
        this();
        setOptions(opts);
    }

    Parameter(Parameter param) {
        A = param.A;
        l0 = param.l0;
        l1 = param.l1;
        T = param.T;
        t = param.t;
        o = param.o;
        opts = new Options();
        opts.putAll(param.opts);
    }

    private void setOptions() {
        opts = new Options();
        opts.put("-A", Integer.toString(A));
        opts.put("-l0", Double.toString(l0));
        opts.put("-l1", Double.toString(l1));
        opts.put("-T", Integer.toString(T));
        opts.put("-t", Integer.toString(t));
        opts.put("-o", Integer.toString(o));
    }

    private Options setOptions(String args) {
        opts.add(args);

        String flag = "-A";
        if (opts.containsKey(flag)) {
            A = opts.getInt(flag);
            if (A < 0 || A > 2) {
                error(flag, A);
            }
        }
        flag = "-l0";
        if (opts.containsKey(flag)) {
            l0 = opts.getDouble(flag);
            if (l0 <= 0) {
                error(flag, l0);
            }
        }
        flag = "-l1";
        if (opts.containsKey(flag)) {
            l1 = opts.getDouble(flag);
            if (l1 <= 0) {
                error(flag, l1);
            }
        }
        flag = "-T";
        if (opts.containsKey(flag)) {
            T = opts.getInt(flag);
            if (T < 1) {
                error(flag, T);
            }
        }
        flag = "-t";
        if (opts.containsKey(flag)) {
            t = opts.getInt(flag);
            if (t < 1) {
                error(flag, t);
            }
        }
        flag = "-o";
        if (opts.containsKey(flag)) {
            o = opts.getInt(flag);
        }

        return opts;
    }

    String getOptions() {
        return " -A " + A + " -l0 " + l0 + " -l1 " + l1 + " -T " + T + " -t " + t + " -o " + o + " ";
    }

    void error(String flag, Object value) {
        String OPTS = "ERROR: Invalid value for parameter %s: %s%n"
                + "OPTIONS:%n"
                + "-A  <int> : t_alg of solver (default " + A + ")%n"
                + "    1 -- DBA %n"
                + "    2 -- SSG %n"
                + "-l0 <double> : initial learning rate > 0 (default " + l0 + ")%n"
                + "-l1 <double> : final learning rate > 0 (default " + l1 + ")%n"
                + "-T  <int> : max number of epochs > 0 (default " + T + ")%n"
                + "-t  <int> : max number of stable epochs > 0 (default " + t + ")%n"
                + "-o  <int> : output mode (default " + o + ")%n"
                + "    0 -- quiet mode %n"
                + "    1 -- report progress after each epoch %n";
        System.out.flush();
        System.err.printf(String.format(OPTS, flag, value));
        System.exit(1);
    }
}
