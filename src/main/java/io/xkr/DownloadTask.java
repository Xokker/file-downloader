package io.xkr;

/**
 * @author ernest
 * @since 10/11/16.
 */
public class DownloadTask {

    private final String path;

    public DownloadTask(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
