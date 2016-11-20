package io.xkr;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author ernest
 * @since 15/11/16.
 */
public class ResourceTypeTest {

    @Test
    public void resolveHttp() throws Exception {
        DownloadTask task = new DownloadTask("http://blabla.com/sfdd/sdf.txt");
        assertEquals(ResourceType.HTTP, ResourceType.resolve(task));
    }

    @Test
    public void resolveFtp() throws Exception {
        DownloadTask task = new DownloadTask("ftp://ftp.vim.org/pub/vim//README");
        assertEquals(ResourceType.FTP, ResourceType.resolve(task));
    }

    @Test(expected = IllegalArgumentException.class)
    public void resolveIllegal() throws Exception {
        ResourceType.resolve(new DownloadTask("ldap://ldap.server.com/path/to"));
    }
}