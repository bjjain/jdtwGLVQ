package cluster;

import core.Options;

class Parameter {

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

    private void setOptions() {
        opts = new Options();
        opts.put("-T", Integer.toString(T));
        opts.put("-t", Integer.toString(t));
        opts.put("-o", Integer.toString(o));
    }

    private Options setOptions(String args) {
        opts.add(args);

        String flag = "-T";
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
        return " -T " + T + " -t " + t + " -o " + o + " ";
    }

    private void error(String flag, Object value) {
        String OPTS = "ERROR: Invalid value for parameter %s: %s%n"
                + "OPTIONS:%n"
                + "-T  <int> : max number of epochs > 0 (default " + T + ")%n"
                + "-t  <int> : max number of stable epochs > 0 (default " + t + ")%n"
                + "-o  <int> : output mode (default " + o + ")%n"
                + "    0 -- quiet mode %n"
                + "    1 -- report progress info after each epoch %n";
        System.out.flush();
        System.err.printf(String.format(OPTS, flag, value));
        System.exit(1);
    }
}
