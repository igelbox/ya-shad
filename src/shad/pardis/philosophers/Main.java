package shad.pardis.philosophers;

public class Main {

    static class DeadlockPhilosopher extends AbstractPhilosopher {

        DeadlockPhilosopher(int id, Fork left, Fork right) {
            super(id, left, right);
        }

        @Override
        void process() throws Exception {
            think();
            synchronized (left) {
                log("took left fork");
                Thread.sleep(100);//force deadlock
                synchronized (right) {
                    log("took right fork");
                    eat();
                }
            }
        }
    }

    static volatile boolean stopSimulation;

    public static void main(String[] args) throws Throwable {
        final int PHILOSOPHER_COUNT = 5;
        AbstractPhilosopher[] philosophers = new AbstractPhilosopher[PHILOSOPHER_COUNT];
        Fork left = new Fork(), last = left;
        for (int i = 0; i < philosophers.length; i++) {
            Fork right = (i == philosophers.length - 1) ? last : new Fork();
            philosophers[i] = new DeadlockPhilosopher(i, left, right);
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

        int minEatCount = Integer.MAX_VALUE, maxEatCount = Integer.MIN_VALUE;
        long minWaitTime = Long.MAX_VALUE, maxWaitTime = Long.MIN_VALUE;
        for (AbstractPhilosopher philosopher : philosophers) {
            philosopher.log("ate " + philosopher.eatCount + " times and waited " + philosopher.waitTime + " ms");
            int eatCount = philosopher.eatCount;
            minEatCount = Math.min(minEatCount, eatCount);
            maxEatCount = Math.max(maxEatCount, eatCount);
            long waitTime = philosopher.waitTime;
            minWaitTime = Math.min(minWaitTime, waitTime);
            maxWaitTime = Math.max(maxWaitTime, waitTime);
        }
        float cEatCount = (minEatCount + maxEatCount) / 2f;
        float cWaitTime = (minWaitTime + maxWaitTime) / 2f;
        System.out.println("Philosophers ate " + cEatCount + "\u00B1" + (maxEatCount - cEatCount) + " times and waited " + cWaitTime + '\u00B1' + (maxWaitTime - cWaitTime) + " ms");
    }
}
