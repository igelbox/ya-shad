package shad.pardis.crawler;

import java.net.URL;
import java.util.concurrent.Executors;
import shad.pardis.crawler.tasks.CountedTaskExecutor;

/**
 *
 * @author igel
 */
public class Main {

    public static void main(String[] args) throws Throwable {
        CountedTaskExecutor executor = new CountedTaskExecutor(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2));
        executor.execute(new FetcherTask(executor, new URL("http://ya.ru/"), 2));
        executor.joinAndShutdown();
    }
}
