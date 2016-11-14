package io.xkr;

import com.google.common.collect.ImmutableMap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author ernest
 * @since 14/11/16.
 */
public class TaskManager {

    private final Map<ResourceType, TaskDownloader> downloaders = ImmutableMap.of(
            ResourceType.HTTP, new HttpTaskDownloader(),
            ResourceType.FTP, new FtpDownloader()
    );
    private final ResourceTypeResolver resourceTypeResolver = new ResourceTypeResolver();

    private final ThreadPoolExecutor executor;

    public TaskManager() {
        executor = new ThreadPoolExecutor(5, 10, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());

    }

    public void scheduleTask(DownloadTask task) {
        executor.submit(() -> executeTask(task));
    }

    private void executeTask(DownloadTask task) {
        ResourceType resourceType = resourceTypeResolver.resolve(task);
        TaskDownloader downloader = downloaders.get(resourceType);
        try {
            InputStream stream = downloader.download(task);
            // TODO: save to file
        } catch (IOException e) {
            // TODO
        }
    }
}
