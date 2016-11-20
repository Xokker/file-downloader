package io.xkr;

import io.xkr.downloader.FtpDownloader;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.assertTrue;

/**
 * @author ernest
 * @since 14/11/16.
 */
public class FtpDownloaderTest {

    private FtpDownloader downloader;

    @Before
    public void setUp() throws Exception {
        downloader = new FtpDownloader();
    }

    @Test
    public void downloadOk() throws Exception {
        InputStream stream = downloader.download(new DownloadTask("ftp://ftp.vim.org/pub/vim//README"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        long linesContaingVim = reader.lines()
                .filter(line -> line.contains("vim"))
                .count();

        assertTrue(linesContaingVim > 2);
    }

    @Test(expected = RuntimeException.class)
    public void downloadFail() throws Exception {
        downloader.download(new DownloadTask("ftp://ftp.vim.org/pub/vim//no-such-file"));
    }
}