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
        executor = new ThreadPoolExecutor(5, 10, 0L, TimeUnit.MILLISECONDS,
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
            System.out.println("Task " + task + " started");
            ResourceType resourceType = ResourceType.resolve(task);
            TaskDownloader downloader = downloaders.get(resourceType);
            if (downloader == null) {
                throw new IllegalArgumentException("Resource type is not supported. Path: " + task.getResourcePath());
            }

            InputStream stream = null;
            try {
                stream = downloader.download(task);
            } catch (IOException e) {
                System.err.println("Task " + task + " failed");
                e.printStackTrace();
                return;
            }
            System.out.println("storing " + task + "...");
            resourceStore.store(task, stream);
            System.out.println("Task " + task + " completed");
            stream.close();
        } catch (IOException e) {
            // ignore input stream closing exception
        } finally {
            pendingTasks.remove(task);
        }
    }

    public void shutdown() {
        executor.shutdown();
        pendingTasks.forEach((task, future) -> future.cancel(true));
        executor.shutdownNow();
    }
}
