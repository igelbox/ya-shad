package shad.pardis.crawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import shad.pardis.crawler.tasks.AbstractCountedTask;
import shad.pardis.crawler.tasks.CountedTaskExecutor;

class ParserTask extends AbstractCountedTask {

    final CharSequence content;
    final int depthLeft;

    ParserTask(CountedTaskExecutor executor, CharSequence content, int depthLeft) {
        super(executor);
        this.content = content;
        this.depthLeft = depthLeft;
    }

    @Override
    protected void execute() {
        try {
            Iterator<String> i = CrawlerUtils.extract(content);
            while (i.hasNext()) {
                String sUrl = i.next();
                if (FileStorage.alreadyFetched(sUrl))
                    continue;
                URL url;
                try {
                    url = new URL(sUrl);
                } catch (MalformedURLException e) {
                    System.err.println(sUrl + ':' + e);
                    continue;//skip bad url-s
                }
                executor.execute(new FetcherTask(executor, url, depthLeft));
            }
        } catch (Throwable e) {
            e.printStackTrace(System.err);
        }
    }
}
