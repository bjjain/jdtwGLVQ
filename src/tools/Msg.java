package tools;

public class Msg {

    private Msg() {
    }

    public static void error(String msg, Object... args) {
        System.err.printf(msg, args);
        (new Exception()).printStackTrace();
        System.exit(1);
    }

    public static void warn(String msg, Object... args) {
        System.out.flush();
        System.err.println(String.format(msg, args));
        System.err.flush();
    }

}
