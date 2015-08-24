package com.tcmj.common.collections;

import com.tcmj.common.lang.Objects;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @todo Test rename feature (data-loss).
 * @author tcmj
 */
public class ResourceBundleMergerTest {

    /** slf4j Logging framework. */
    private static final Logger LOG = LoggerFactory.getLogger(ResourceBundleMergerTest.class);

    private static final String PROPFILEENCODING = "ISO-8859-1";

    private static final List<String> testfiles = new ArrayList<>();

    @AfterClass
    public static void tearDownClass() throws Exception {
        testfiles.stream().filter((file) -> (!deleteFile(file))).forEach((file) -> {
            LOG.error("cannot delete testfile {}", file);
        });
    }

    /** Deletes a file. */
    private static boolean deleteFile(String filename) {
        return new File(filename).delete();
    }

    /** Creates a properties file. */
    private static String createPropFile(String filename, String... keyvalues) {
        Objects.notBlank(filename, "empty filename");
        if (keyvalues.length % 2 != 0) {
            throw new UnsupportedOperationException("you can only set key,value,key,value....");
        }
        testfiles.add(filename); //..to delete them @AfterClass

        // create and populate files
        try (PrintWriter w = new PrintWriter(filename, PROPFILEENCODING)) {
            for (int i = 0; i < keyvalues.length; i++) {
                w.println(keyvalues[i] + "=" + keyvalues[++i]);
            }
        } catch (Exception ex) {
            LOG.error("Exception", ex);
        }
        return filename;
    }

    /**
     * Test of builder pattern of class ResourceBundleMerger.
     * <p>
     * call to build without using any mandatory methods.
     */
    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionOnWrongBuilderPatternUsageDirectCallToBuild() {
        new ResourceBundleMerger.Builder().build();
    }

    /**
     * Test of builder pattern of class ResourceBundleMerger.
     * <p>
     * use of input with null parameter
     */
    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionOnWrongBuilderPatternInputFileIsNull() {
        String[] inp = null;
        new ResourceBundleMerger.Builder().input(inp).build();
    }

    /**
     * Test of builder pattern of class ResourceBundleMerger.
     * <p>
     * use of input with empty content
     */
    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionOnWrongBuilderPatternInputFileIsEmpty() {
        String[] inp = new String[0];
        new ResourceBundleMerger.Builder().input(inp).build();
    }

    /**
     * Test of builder pattern of class ResourceBundleMerger.
     * <p>
     * use of only one input file
     */
    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionOnWrongBuilderPatternOnlyOneInputFile() {
        String[] inp = new String[]{"onlyone"};
        new ResourceBundleMerger.Builder().input(inp).build();
    }

    /**
     * Test of start method, of class ResourceBundleMerger.
     * TestCase:
     * <p>
     * <b>given:</b> a customer has a property file with 2 entries and he made own changes to the text.
     * <p>
     * <b>when:</b> he gets a new file delivered containing an additional entry
     * <p>
     * <b>then:</b> the new one should be merged with the existing one without deleting his changes
     */
    @Test
    public void shouldMergeChangedOldWithNewFile() throws Exception {

        //given
        String newFile = createPropFile("new.properties", "one", "eins", "two", "zwei", "three", "drei");
        String existingFile = createPropFile("existing.properties", "one", "eins", "two", "my own change");

        //when
        ResourceBundleMerger rbm = new ResourceBundleMerger.Builder()
                //(order is important to keep the value of the existing file)
                .input(new String[]{newFile, existingFile})
                .build();
        rbm.start();

        //then
        File inputFile = new File(rbm.getOutputFileName());
        assertThat("outputfile created", inputFile.isFile(), is(true));

        try (BufferedInputStream reader = new BufferedInputStream(new FileInputStream(inputFile))) {

            Properties mergedProps = new Properties();
            mergedProps.load(reader);

            assertThat("amount of properties ", mergedProps.size(), is(3));

            assertThat("content " + mergedProps, mergedProps.getProperty("one"), equalTo("eins"));
            assertThat("content " + mergedProps, mergedProps.getProperty("two"), equalTo("my own change"));
            assertThat("content " + mergedProps, mergedProps.getProperty("three"), equalTo("drei"));
        }

        testfiles.add(rbm.getOutputFileName()); //..cleanup

    }

    /**
     * Test of getInputFileNames method, of class ResourceBundleMerger.
     */
    @Test
    public void testGetInput() {
        ResourceBundleMerger rbm = new ResourceBundleMerger.Builder().input(new String[]{"one", "two"}).build();
        String[] expResult = new String[]{"one", "two"};
        String[] result = rbm.getInputFileNames();
        assertArrayEquals(expResult, result);

        rbm.getInputFileNames()[0] = "try2expose";
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getOutputFileName method, of class ResourceBundleMerger.
     */
    @Test
    public void testGetOutput() {
        ResourceBundleMerger rbm = new ResourceBundleMerger.Builder().input(new String[]{"one", "two"}).build();
        assertEquals("merged_one_two.properties", rbm.getOutputFileName());

        rbm = new ResourceBundleMerger.Builder().input(new String[]{"one", "two"}).deltaMode().build();
        assertEquals("delta_one_two.properties", rbm.getOutputFileName());
    }

    /**
     * Test of isSort method, of class ResourceBundleMerger.
     */
    @Test
    public void testIsSort() {
        ResourceBundleMerger rbm = new ResourceBundleMerger.Builder().input(new String[]{"one", "two"}).build();
        assertEquals("sort mode off", false, rbm.isSort());

        rbm = new ResourceBundleMerger.Builder().input(new String[]{"one", "two"}).sort().build();
        assertEquals("sort mode on", true, rbm.isSort());
    }

    /**
     * Test of isQuiet method, of class ResourceBundleMerger.
     */
    @Test
    public void testIsQuiet() {
        ResourceBundleMerger rbm = new ResourceBundleMerger.Builder().input(new String[]{"one", "two"}).build();
        assertEquals("quiet mode off", false, rbm.isQuiet());

        rbm = new ResourceBundleMerger.Builder().input(new String[]{"one", "two"}).quiet().build();
        assertEquals("quiet mode on", true, rbm.isQuiet());
    }

}
