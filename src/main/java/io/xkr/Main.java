package io.xkr;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

/**
 * @author ernest
 * @since 10/11/16.
 */
public class Main {

    private static Args parseArgs(String[] args) throws NoSuchFileException {
        // TODO: use --jopt-simple

        if (args.length < 2) {
            throw new IllegalArgumentException("expected args: " +
                    "<path to file with list of sources> <path to directory where downloaded content must be stored>");
        }

        String filepath = args[0].trim();
        Path file = Paths.get(filepath.trim());
        if (!Files.exists(file)) {
            throw new NoSuchFileException(filepath + " does not exist");
        }

        Path storeDirectoryPath = Paths.get(args[1].trim());
        if (!Files.isDirectory(storeDirectoryPath)) {
            throw new IllegalArgumentException(storeDirectoryPath.toString() + " is not directory or doesn't exist");
        }

        return new Args(file, storeDirectoryPath);
    }

    public static void main(String[] args) throws IOException {
        Args parsedArgs = parseArgs(args);

        ResourceListParser parser = new ResourceListParser();
        List<DownloadTask> tasks = parser.extractTasks(parsedArgs.sourcesPath);
        logTaskTypes(tasks);

        TaskManager taskManager = new TaskManager(parsedArgs.storeDir);
        tasks.forEach(taskManager::scheduleTask);

        taskManager.shutdown();
    }

    private static void logTaskTypes(List<DownloadTask> tasks) {
        String logMessage = tasks.stream()
                .map(ResourceType::resolve)
                .collect(groupingBy(identity(), counting()))
                .entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .map(e -> e.getKey() + " -> " + e.getValue())
                .collect(Collectors.joining(", ", "Got tasks: ", ""));

        System.out.println(logMessage);
    }

    private static class Args {

        final Path sourcesPath;
        final Path storeDir;

        Args(Path sourcesPath, Path storeDir) {
            this.sourcesPath = sourcesPath;
            this.storeDir = storeDir;
        }
    }
}
