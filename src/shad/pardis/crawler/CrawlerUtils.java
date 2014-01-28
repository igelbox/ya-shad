package shad.pardis.crawler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author igel
 */
class CrawlerUtils {

    private static final String DEFAULT_CONTENT_ENCODING = "UTF-8";
    private static final int FETCH_BUFFER_SIZE = 4096;

    static CharSequence fetch(URL url) throws IOException {
        HttpURLConnection c = (HttpURLConnection) url.openConnection();
        try {
            int responseCode = c.getResponseCode();
            if (responseCode != 200)
                throw new IOException(c.getResponseMessage());

            String contentType = c.getContentType();
            if ((contentType == null) || (!contentType.startsWith("text/html")))
                return null;

            String contentEncoding = c.getContentEncoding();
            if (contentEncoding == null) {
                final String CHARSET_KEY = "charset=";
                int csi = contentType.indexOf(CHARSET_KEY);
                if (csi < 0)
                    contentEncoding = DEFAULT_CONTENT_ENCODING;
                else
                    contentEncoding = contentType.substring(csi + CHARSET_KEY.length());
            }
            try (Reader r = new InputStreamReader(c.getInputStream(), contentEncoding)) {
                StringBuilder result = new StringBuilder();
                char[] buff = new char[FETCH_BUFFER_SIZE];
                int l;
                while ((l = r.read(buff)) >= 0)
                    result.append(buff, 0, l);
                return result;
            }
        } finally {
            c.disconnect();
        }
    }

    private static final Pattern RG_HTTP = Pattern.compile("http://[a-z\\\\./&=\\\\?]+", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);

    static Iterator<String> extract(CharSequence content) {
        Collection<String> result = new ArrayList<>();
        Matcher m = RG_HTTP.matcher(content);
        while (m.find())
            result.add(m.group());
        return result.iterator();
    }
}
