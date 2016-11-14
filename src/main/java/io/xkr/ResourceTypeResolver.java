package io.xkr;

/**
 * @author ernest
 * @since 14/11/16.
 */
public class ResourceTypeResolver {

    public ResourceType resolve(DownloadTask task) {
        String path = task.getPath();
        if (path.startsWith("http")) {
            return ResourceType.HTTP;
        } else if (path.startsWith("ftp")) {
            return ResourceType.FTP;
        } else {
            throw new IllegalArgumentException("Resource type is not supported. Path: " + path);
        }
    }
}
