package shad.pardis.crawler.tasks;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class CountedTaskExecutor implements Executor {

    private final ExecutorService executor;
    private int taskCount;

    public CountedTaskExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public void execute(Runnable command) {
        synchronized (this) {
            taskCount++;
        }
        executor.execute(command);
    }

    synchronized void taskCompleted() {
        taskCount--;
        if (taskCount == 0)
            notify();
    }

    public void joinAndShutdown() throws InterruptedException {
        synchronized (this) {
            while (taskCount > 0)
                wait();
        }
        executor.shutdown();
    }
}
