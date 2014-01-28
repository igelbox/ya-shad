package shad.pardis.crawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class FileStorage {

    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    private static final ConcurrentMap<String, URL> fetched = new ConcurrentHashMap<>();
    private static final File ROOT;

    static {
        ROOT = new File(".storage");
        if (!ROOT.mkdirs() && !ROOT.exists())
            throw new RuntimeException("can`t create " + ROOT.getAbsolutePath());
    }

    static Writer openAndLock(URL url) throws IOException {
        String fileName = URLEncoder.encode(url.toString(), DEFAULT_CHARSET.name());
        if (fetched.putIfAbsent(fileName, url) != null)
            return null;
        return new OutputStreamWriter(new FileOutputStream(new File(ROOT, fileName)), DEFAULT_CHARSET);
    }

    static boolean alreadyFetched(String url) throws UnsupportedEncodingException {
        String fileName = URLEncoder.encode(url.toString(), DEFAULT_CHARSET.name());
        return fetched.containsKey(fileName);
    }
}
