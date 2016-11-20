package io.xkr;

import java.util.Objects;

/**
 * @author ernest
 * @since 10/11/16.
 */
public class DownloadTask {

    private final String resourcePath;

    public DownloadTask(String resourcePath) {
        this.resourcePath = Objects.requireNonNull(resourcePath);
    }

    public String getResourcePath() {
        return resourcePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return resourcePath.equals(((DownloadTask) o).resourcePath);
    }

    @Override
    public int hashCode() {
        return resourcePath.hashCode();
    }

    @Override
    public String toString() {
        return "DownloadTask{" + resourcePath + "}";
    }
}
