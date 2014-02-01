package shad.pardis.philosophers;

import java.util.Random;

abstract class AbstractPhilosopher {

    final int id;
    final Fork left, right;
    int eatCount;
    long waitTime;
    private long startWait;
    private final Random rnd = new Random();

    AbstractPhilosopher(int id, Fork left, Fork right) {
        this.id = id;
        this.left = left;
        this.right = right;
    }

    abstract void process() throws Exception;

    void eat() {
        if (startWait != 0)
            waitTime = System.currentTimeMillis() - startWait;
        log("is eating");
        try {
            Thread.sleep(rnd.nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);
        }
        eatCount++;
        log("finished eating");
    }

    void think() {
        log("is thinking");
        try {
            Thread.sleep(rnd.nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);
        }
        log("is hungry");
        startWait = System.currentTimeMillis();
    }

    private static final long startTime = System.currentTimeMillis();

    void log(String state) {
        System.out.format("%06d [Philosopher %d] %s %n", (System.currentTimeMillis() - startTime), id, state);
    }

    void logFork(Fork fork, String actionAndSide) {
        log(actionAndSide + " fork-" + fork.id);
    }
}
