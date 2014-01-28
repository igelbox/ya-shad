package shad.pardis.crawler.tasks;

public abstract class AbstractCountedTask implements Runnable {

    protected final CountedTaskExecutor executor;

    protected AbstractCountedTask(CountedTaskExecutor executor) {
        this.executor = executor;
    }

    protected abstract void execute();

    @Override
    public final void run() {
        try {
            execute();
        } finally {
            executor.taskCompleted();
        }
    }
}
