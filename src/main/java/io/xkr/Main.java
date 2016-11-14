package io.xkr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author ernest
 * @since 10/11/16.
 */
public class Main {

    private static Path getFile(String[] args) throws NoSuchFileException {
        // TODO: use --jopt-simple

        if (args.length == 0) {
            System.out.println("no file path is given");
        }

        String filepath = args[0].trim();
        Path file = Paths.get(filepath.trim());
        if (!Files.exists(file)) {
            throw new NoSuchFileException(filepath);
        }

        return file;
    }

    public static void main(String[] args) throws IOException {
        Path file = getFile(args);
        ResourceListParser parser = new ResourceListParser();
        List<DownloadTask> tasks = parser.extractTasks(file);

        TaskManager taskManager = new TaskManager();
        tasks.forEach(taskManager::scheduleTask);

    }
}
