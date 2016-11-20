package io.xkr.downloader;

import io.xkr.DownloadTask;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author ernest
 * @since 14/11/16.
 */
public class HttpTaskDownloader implements TaskDownloader {

    private final OkHttpClient client;

    public HttpTaskDownloader() {
        client = new OkHttpClient.Builder()
                .followRedirects(false)
                .build();
    }

    @Override
    public InputStream download(DownloadTask task) throws IOException {

        Request request = new Request.Builder()
                .url(task.getResourcePath())
                .build();

        Response response = client.newCall(request).execute();
        int responseCode = response.code();
        if (responseCode / 100 != 2) {
            throw new RuntimeException("Download failed. Response code: " + responseCode);  // TODO: custom exception
        }

        return response.body().byteStream();
    }
}
