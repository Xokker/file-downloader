package io.xkr.downloader;

import io.xkr.DownloadTask;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

/**
 * @author ernest
 * @since 14/11/16.
 */
public class FtpDownloader implements TaskDownloader {

    @Override
    public InputStream download(DownloadTask task) throws IOException {
        String path = task.getResourcePath();
        URI uri;
        try {
            uri = new URI(path);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String domain = uri.getHost();

        FTPClient client = new FTPClient();
        int timeout = (int) TimeUnit.SECONDS.toMillis(10);
        client.setConnectTimeout(timeout);
        client.setDataTimeout(timeout);
        client.setConnectTimeout(timeout);
        client.connect(domain);
        client.setSoTimeout(timeout);
        client.login("anonymous", "anonymous");

        InputStream stream = client.retrieveFileStream(uri.getPath());  // TODO: close input stream
        if (stream == null) {
            throw new RuntimeException("Cannot download file");
        }

        return stream;
    }
}
