package elvq;

import core.Options;

class Parameter {

    // elasticitiy
    private int e = 0;

    // number of prototypes per class
    private int k = 1;

    // slope parameter
    double g = 1.0;

    // learning rate
    double l = 0.1;

    // termination: maximum number of epochs
    int T = 500;

    // termination: maximum number of epochs without improvement
    int t = 100;

    // output mode
    int o = 3;

    // options
    Options opts;

    private Parameter() {
        setOptions();
    }

    Parameter(String opts) {
        this();
        setOptions(opts);
    }

    Parameter(Parameter param) {
        e = param.e;
        k = param.k;
        g = param.g;
        l = param.l;
        T = param.T;
        t = param.t;
        o = param.o;
        opts = new Options();
        opts.putAll(param.opts);
    }

    private void setOptions() {
        opts = new Options();
        opts.put("-e", Integer.toString(e));
        opts.put("-k", Integer.toString(k));
        opts.put("-g", Double.toString(g));
        opts.put("-l", Double.toString(l));
        opts.put("-T", Integer.toString(T));
        opts.put("-t", Integer.toString(t));
        opts.put("-o", Integer.toString(o));
    }

    private Options setOptions(String args) {
        opts.add(args);

        String flag = "-e";
        if (opts.containsKey(flag)) {
            e = opts.getInt(flag);
        }
        flag = "-k";
        if (opts.containsKey(flag)) {
            k = opts.getInt(flag);
            if (k < 1) {
                error(flag, k);
            }
        }
        flag = "-g";
        if (opts.containsKey(flag)) {
            g = opts.getDouble(flag);
            if (g <= 0) {
                error(flag, g);
            }
        }
        flag = "-l";
        if (opts.containsKey(flag)) {
            l = opts.getDouble(flag);
            if (l <= 0) {
                error(flag, l);
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
        return  " -e " + e + " -k " + k + " -g " + g + " -l " + l
                + " -T " + T + " -t " + t + " -o " + o + " ";
    }

    private void error(String flag, Object value) {
        String OPTS = "ERROR: Invalid value for parameter %s: %s%n"
                + "OPTIONS:%n"
                + "-e <int> : elasticitiy (default 0). IF e > 0 ... %n"
                + "    ...  THEN length of prototypes is fixed to e %n"
                + "    ...  ELSE sample time series determine length of prototypes%n"
                + "-g <double> : slope parameter > 0 (default 1)%n"
                + "-l <double> : learning rate > 0 (default 0.1)%n"
                + "-k <int> : #prototypes per class (default 1)%n"
                + "-T <int> : max number of epochs (default 5000)%n"
                + "-t <int> : max number of stable epochs (default 200)%n"
                + "-o <int> : output mode (default 4)%n"
                + "    0 -- quiet mode %n"
                + "    1 -- cross-validation mode %n"
                + "    2 -- progress info after each epoch %n";
        System.out.flush();
        System.err.printf(String.format(OPTS, flag, value));
        System.exit(1);
    }
}
