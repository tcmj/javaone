package com.tcmj.common.io;

import com.tcmj.common.xml.map.XMLMap;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tcmj
 */
public class FileToolTest {
    
    /** slf4j Logging framework. */
    private static final transient Logger LOG = LoggerFactory.getLogger(FileToolTest.class);


    public FileToolTest() {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace");
    }
    
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    

    /**
     * Test of read method, of class FileTool.
     */
    @Test
    public void testFindFile() throws Exception {
        LOG.info("testFindFile");
        File result = FileTool.locateFile("FileToolTest.class", FileTool.class);
        System.out.println("\t"+result);
//        assertNotNull(result);
//        assertTrue(result.length()>0);
        //todo fix
    }

    /**
     * Test of read method, of class FileTool.
     */
    @Test
    public void testReadString() throws Exception {
        LOG.info("testReadString");
        String result = FileTool.read("FileToolTest.class");
        System.out.println("\t"+StringUtils.abbreviate(result, 10));
        assertNotNull(result);
        assertTrue(result.length()>0);
    }
    
    /**
     * Test of read method, of class FileTool.
     */
    @Test
    public void testReadPath() throws Exception {
        System.out.println("testReadPath");
        String result = FileTool.read(Paths.get("pom.xml"));
        System.out.println("\t"+StringUtils.abbreviate(result, 80));
        assertNotNull(result);
        assertTrue(result.length()>0);
    }

    /**
     * Test of forEachLine method, of class FileTool.
     */
    @Test
    public void testForEachLine() {
        System.out.println("forEachLine");
        Consumer<? super String> action = (s)->{
            if(s.contains("artifactId")){
                System.out.println("\t"+StringUtils.substringBetween(s, "<artifactId>", "</artifactId>"));
            }
        };
        FileTool.forEachLine(Paths.get("pom.xml"), action);
    }
    
}
