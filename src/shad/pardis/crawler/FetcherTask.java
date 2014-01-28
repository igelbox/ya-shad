package shad.pardis.crawler;

import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import shad.pardis.crawler.tasks.AbstractCountedTask;
import shad.pardis.crawler.tasks.CountedTaskExecutor;

class FetcherTask extends AbstractCountedTask {

    final URL url;
    final int depthLeft;

    FetcherTask(CountedTaskExecutor executor, URL url, int depthLeft) {
        super(executor);
        this.url = url;
        this.depthLeft = depthLeft;
    }

    @Override
    protected void execute() {
        try {
            Writer w = FileStorage.openAndLock(url);
            if (w == null)
                return;//already fetched
            Logger.log("fetching: " + url);
            try {
                CharSequence content;
                try {
                    content = CrawlerUtils.fetch(url);
                } catch (IOException e) {
                    Logger.err("error: " + e);
                    return;
                }
                if (content == null)
                    return;
                if (depthLeft > 1)
                    executor.execute(new ParserTask(executor, content, depthLeft - 1));
                w.write(content.toString());
            } finally {
                w.close();
            }
        } catch (Throwable e) {
            e.printStackTrace(System.err);
        }
    }
}
