package io.xkr;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author ernest
 * @since 14/11/16.
 */
public class FtpDownloader implements TaskDownloader {

    @Override
    public InputStream download(DownloadTask task) throws IOException {
        FTPClient client = new FTPClient();
        String path = task.getPath();
        URI uri;
        try {
            uri = new URI(path);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String domain = uri.getHost();
        client.connect(domain);
        client.login("anonymous", "anonymous");

        InputStream stream = client.retrieveFileStream(uri.getPath());  // TODO: close input stream
        if (stream == null) {
            throw new RuntimeException("Cannot download file");
        }

        return stream;
    }
}
