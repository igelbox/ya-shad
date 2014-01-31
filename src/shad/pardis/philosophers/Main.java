package shad.pardis.philosophers;

public class Main {

    static class Philosopher extends AbstractPhilosopher {

        Philosopher(int id, Fork left, Fork right) {
            super(id, left, right);
        }

        @Override
        void process() throws Exception {
        }
    }

    static volatile boolean stopSimulation;

    public static void main(String[] args) throws Throwable {
        final int PHILOSOPHER_COUNT = 5;
        AbstractPhilosopher[] philosophers = new AbstractPhilosopher[PHILOSOPHER_COUNT];
        Fork left = new Fork(), last = left;
        for (int i = 0; i < philosophers.length; i++) {
            Fork right = (i == philosophers.length - 1) ? last : new Fork();
            philosophers[i] = new Philosopher(i, left, right);
            left = right;
        }
        Thread[] threads = new Thread[philosophers.length];
        for (int i = 0; i < philosophers.length; i++) {
            final AbstractPhilosopher philosopher = philosophers[i];
            Thread thread = threads[i] = new Thread() {
                @Override
                public void run() {
                    philosopher.log("started");
                    try {
                        while (!stopSimulation)
                            philosopher.process();
                        philosopher.log("stopped");
                    } catch (Exception e) {
                        philosopher.log("crashed with: " + e);
                    }
                }
            };
            thread.start();
        }
        Thread.sleep(60000);
        stopSimulation = true;
        for (Thread thread : threads)
            thread.join();
        for (AbstractPhilosopher philosopher : philosophers)
            philosopher.log("ate " + philosopher.eatCount + " times and waited " + philosopher.waitTime + " ms");
    }
}
