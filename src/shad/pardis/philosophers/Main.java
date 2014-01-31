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
                logFork(left, "took left");
                Thread.sleep(100);//force deadlock
                synchronized (right) {
                    logFork(right, "took right");
                    eat();
                    logFork(right, "release right");
                }
                logFork(left, "release left");
            }
        }
    }

    static class SmartPhilosopher extends AbstractPhilosopher {

        SmartPhilosopher(int id, Fork left, Fork right) {
            super(id, left, right);
        }

        @Override
        void process() throws Exception {
            think();
            if ((id % 2) == 0)
                synchronized (left) {
                    logFork(left, "took left");
                    Thread.sleep(100);//force deadlock
                    synchronized (right) {
                        logFork(right, "took right");
                        eat();
                        logFork(right, "release right");
                    }
                    logFork(left, "release left");
                }
            else
                synchronized (right) {
                    logFork(right, "took right");
                    Thread.sleep(100);//force deadlock
                    synchronized (left) {
                        logFork(left, "took left");
                        eat();
                        logFork(left, "release left");
                    }
                    logFork(right, "release right");
                }
        }
    }

    static volatile boolean stopSimulation;

    public static void main(String[] args) throws Throwable {
        final int PHILOSOPHER_COUNT = 5;
        AbstractPhilosopher[] philosophers = new AbstractPhilosopher[PHILOSOPHER_COUNT];
        Fork left = new Fork(philosophers.length - 1), last = left;
        for (int i = 0; i < philosophers.length; i++) {
            Fork right = (i == philosophers.length - 1) ? last : new Fork(i);
            philosophers[i] = new SmartPhilosopher(i, left, right);
            left = right;
        }
        Thread[] threads = new Thread[philosophers.length];
        for (int i = 0; i < philosophers.length; i++) {
            final AbstractPhilosopher philosopher = philosophers[i];
            threads[i] = new Thread() {
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
        }

        for (Thread thread : threads)
            thread.start();
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
