package io.xkr;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ernest
 * @since 10/11/16.
 */
public class ResourceListParser {

    public List<DownloadTask> extractTasks(Path file) throws IOException {
        return Files.lines(file)
                .map(String::trim)
                .filter(StringUtils::isNotBlank)
                .map(DownloadTask::new)
                .distinct()
                .collect(Collectors.toList());
    }
}
