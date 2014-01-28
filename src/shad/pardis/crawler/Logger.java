package shad.pardis.crawler;

class Logger {

    static void log(String message) {
        System.out.println("[" + Thread.currentThread().getId() + "] " + message);
    }

    static void err(String message) {
        System.err.println("[" + Thread.currentThread().getId() + "] " + message);
    }
}
