package shad.pardis.crawler;

import java.net.URL;
import java.util.Iterator;

/**
 *
 * @author igel
 */
public class Main {

    public static void main(String[] args) throws Throwable {
        CharSequence content = CrawlerUtils.fetch(new URL("http://ya.ru/"));
        Iterator<String> i = CrawlerUtils.extract(content);
        while (i.hasNext())
            System.out.println(i.next());
    }
}
