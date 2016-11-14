package io.xkr;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.junit.Assert.*;

/**
 * @author ernest
 * @since 14/11/16.
 */
public class HttpTaskDownloaderTest {

    private final String ALICE_URL = "http://www.umich.edu/~umfandsf/other/ebooks/alice30.txt";

    private HttpTaskDownloader downloader;

    @Before
    public void setUp() throws Exception {
        downloader = new HttpTaskDownloader();
    }

    @Test
    public void downloadOk() throws Exception {
        InputStream stream = downloader.download(new DownloadTask(ALICE_URL));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        long linesCount = reader.lines().count();

        assertEquals(3599L, linesCount);
    }

    @Test(expected = RuntimeException.class)
    public void downloadFail() throws Exception {
        downloader.download(new DownloadTask(ALICE_URL + "blabla"));
    }
}