package io.xkr;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author ernest
 * @since 14/11/16.
 */
public interface TaskDownloader {

    InputStream download(DownloadTask task) throws IOException;
}
