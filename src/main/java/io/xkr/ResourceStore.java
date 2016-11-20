package io.xkr;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;

/**
 * @author ernest
 * @since 15/11/16.
 */
public class ResourceStore {

    private final Path storePath;

    public ResourceStore(Path storePath) {
        this.storePath = storePath;
    }

    public void store(DownloadTask task, InputStream data) {
        Path pathToFile = storePath.resolve(task.getResourcePath().replace('/', '_'));
        File outputFile = pathToFile.toFile();

        try {
            System.out.println(" Task " + task + " stored");
            OutputStream output = new FileOutputStream(outputFile);
            IOUtils.copyLarge(data, output);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
            outputFile.delete();
        }
    }
}
