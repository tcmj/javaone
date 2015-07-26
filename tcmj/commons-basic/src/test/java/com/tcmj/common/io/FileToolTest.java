package com.tcmj.common.io;

import java.io.File;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.spi.FileSystemProvider;
import java.util.function.Consumer;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit and ussage tests for {@link FileTool}.
 * <p>
 * @author tcmj
 */
public class FileToolTest {

    /** slf4j Logging framework. */
    private static final transient Logger LOG = LoggerFactory.getLogger(FileToolTest.class);

    public FileToolTest() {
    }

    @Test
    public void testCheckFileExistsFileNotThere() throws Exception {
        LOG.info("*** testCheckFileExistsFileNotThere ***");
        assertFalse(FileTool.checkIsFileAndExists(new File("DoesNotExist.txt")));
    }

    @Test
    public void testCheckFileExistsFileThere() throws Exception {
        LOG.info("*** testCheckFileExistsFileThere ***");
        assertTrue(FileTool.checkIsFileAndExists(new File("pom.xml")));
    }

    @Test
    public void testCheckFileExistsPath() throws Exception {
        LOG.info("*** testCheckFileExistsPath ***");
        assertFalse(FileTool.checkIsFileAndExists(FileSystems.getDefault().getPath("DoesNotExist.txt")));
    }

    @Test
    public void testFindFile() throws Exception {
        LOG.info("*** testFindFile ***");
        File result = FileTool.locateFile("FileToolTest.class", FileTool.class);
        assertNull(result);
    }

    @Test
    public void testFindFile2() throws Exception {
        LOG.info("*** testFindFile2 ***");
        assertNotNull(FileTool.locateFile("C:\\bootmgr", FileTool.class));
    }

    @Test
    public void testFindFile3() throws Exception {
        LOG.info("*** testFindFile3 ***");
        assertNull(FileTool.locateFile("C:\\", FileTool.class));
    }

    @Test
    public void testFindFile4() throws Exception {
        LOG.info("*** testFindFile4 ***");
        assertNotNull(FileTool.locateFile("pom.xml", FileTool.class));
    }

    @Test
    public void testFindFile5() throws Exception {
        LOG.info("*** testFindFile5 ***");
        assertNotNull(FileTool.locateFile("read.me", FileTool.class));
    }

    @Test
    public void testReadString() throws Exception {
        LOG.info("*** testReadString ***");
        String result = FileTool.read("FileToolTest.class");
        LOG.info("\t" + StringUtils.abbreviate(result, 10));
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    /**
     * Test of read method, of class FileTool.
     */
    @Test
    public void testReadPath() throws Exception {
        LOG.info("*** testReadPath ***");
        String result = FileTool.read(Paths.get("pom.xml"));
        LOG.info("\t" + StringUtils.abbreviate(result, 80));
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test
    public void testForEachLine() {
        LOG.info("*** forEachLine ***");
        Consumer<? super String> action = (s) -> {
            if (s.contains("artifactId")) {
                LOG.info("\t" + StringUtils.substringBetween(s, "<artifactId>", "</artifactId>"));
            }
        };
        FileTool.forEachLine(Paths.get("pom.xml"), action);
    }

    private static void definePathFromURI() {
        //URI uri = URI.create("file:///c:/Lokesh/Setup/workspace/NIOExamples/src/sample.txt"); //OR
        URI uri = URI.create("file:///Lokesh/Setup/workspace/NIOExamples/src/sample.txt");

        String scheme = uri.getScheme();
        if (scheme == null) {
            throw new IllegalArgumentException("Missing scheme");
        }

        //check for default provider to avoid loading of installed providers
        if (scheme.equalsIgnoreCase("file")) {
            LOG.info(FileSystems.getDefault().provider().getPath(uri).toAbsolutePath().toString());
        }

        // try to find provider
        for (FileSystemProvider provider : FileSystemProvider.installedProviders()) {
            if (provider.getScheme().equalsIgnoreCase(scheme)) {
                LOG.info(provider.getPath(uri).toAbsolutePath().toString());
                break;
            }
        }
    }
}
