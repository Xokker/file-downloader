package io.xkr;

/**
 * @author ernest
 * @since 14/11/16.
 */
public enum ResourceType {

    FTP("ftp"),
    HTTP("http"),
    UNKNOWN("");

    private final String resourcePrefix;

    ResourceType(String resourcePrefix) {
        this.resourcePrefix = resourcePrefix;
    }

    public static ResourceType resolve(DownloadTask task) {
        String path = task.getResourcePath();
        for (ResourceType type : ResourceType.values()) {
            if (path.startsWith(type.resourcePrefix)) {
                return type;
            }
        }

        return ResourceType.UNKNOWN;
    }
}
