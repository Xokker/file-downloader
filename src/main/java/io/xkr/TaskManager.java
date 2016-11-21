package io.xkr;

import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.AbstractFuture;
import io.xkr.downloader.FtpDownloader;
import io.xkr.downloader.HttpTaskDownloader;
import io.xkr.downloader.TaskDownloader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author ernest
 * @since 14/11/16.
 */
public class TaskManager {

    private final static Future<?> EMPTY_FUTURE = new AbstractFuture<Object>() {};

    private final Map<ResourceType, TaskDownloader> downloaders = ImmutableMap.of(
            ResourceType.HTTP, new HttpTaskDownloader(),
            ResourceType.FTP, new FtpDownloader()
    );

    private final ThreadPoolExecutor executor;
    private final ConcurrentMap<DownloadTask, Future<?>> pendingTasks = new ConcurrentHashMap<>();
    private final ResourceStore resourceStore;

    public TaskManager(Path storePath) {
        int processors = Runtime.getRuntime().availableProcessors();
        executor = new ThreadPoolExecutor(processors * 2, processors * 4, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());
        resourceStore = new ResourceStore(storePath);
    }

    public void scheduleTask(DownloadTask task) {
        pendingTasks.put(task, EMPTY_FUTURE);
        System.out.println("Task " + task + " scheduled");
        Future<?> future = executor.submit(() -> executeTask(task));
        pendingTasks.replace(task, EMPTY_FUTURE, future);  // replace only if task haven't been already removed
    }

    private void executeTask(DownloadTask task) {
        try {
            ResourceType resourceType = ResourceType.resolve(task);
            TaskDownloader downloader = downloaders.get(resourceType);
            if (downloader == null) {
                System.out.println("Task " + task + " discarded");
                throw new IllegalArgumentException("Resource type is not supported. Path: " + task.getResourcePath());
            }
            System.out.println("Task " + task + " started");

            InputStream stream;
            try {
                stream = downloader.download(task);
            } catch (Exception e) {
                System.err.println("Task " + task + " failed");
                e.printStackTrace();
                return;
            }
            resourceStore.store(task, stream);
            System.out.println("Task " + task + " completed");
            try {
                stream.close();
            } catch (IOException e) {
                // logger.warn
            }
        } finally {
            pendingTasks.remove(task);
        }
    }

    public void shutdown() {
        executor.shutdown();
    }

    public void shutdownNow() {
        shutdown();
        pendingTasks.forEach((task, future) -> future.cancel(true));
        executor.shutdownNow();
    }
}
