package io.xkr.downloader;

import io.xkr.DownloadTask;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author ernest
 * @since 14/11/16.
 */
public interface TaskDownloader {

    InputStream download(DownloadTask task) throws IOException;
}
