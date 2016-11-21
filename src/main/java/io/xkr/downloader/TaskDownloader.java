package io.xkr.downloader;

import io.xkr.DownloadTask;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author ernest
 * @since 14/11/16.
 *
 * NB task downloader does not support authorization. If you need one, add additional params in {@link #download}
 *  or create new interface like AuthorizedTaskDownloader (leads to if-instaceofs).
 */
public interface TaskDownloader {

    InputStream download(DownloadTask task) throws IOException;
}
