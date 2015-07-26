package com.tcmj.common.lang;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application
 */
public class ApplicationTest {

    /** slf4j Logging framework. */
    private static final transient Logger LOG = LoggerFactory.getLogger(ApplicationTest.class);

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.getProperties().entrySet().forEach(entry -> {
            System.out.println("key=" + entry.getKey() + " \t = " + entry.getValue());
        });
    }

    public ApplicationTest() {
    }

    /**
     * Test of getApplicationTitle method, of class Application.
     */
    @Test
    public void testGetApplicationTitle() {
        Class context = getClass();
        String result = Application.get(context).getApplicationTitle();
        LOG.info("getApplicationTitle: '{}'", result);
        assertNotNull(result);
    }

    /**
     * Test of getApplicationVersion method, of class Application.
     */
    @Test
    public void testGetApplicationVersion() {
        Class context = getClass();
        String result = Application.get(context).getApplicationVersion();
        LOG.info("getApplicationVersion: '{}'", result);
        assertNotNull(result);
    }

    /**
     * Test of getApplicationVendor method, of class Application.
     */
    @Test
    public void testGetApplicationVendor() {
        Class context = getClass();
        String result = Application.get(context).getApplicationVendor();
        LOG.info("getApplicationVendor: '{}'", result);
        assertNotNull(result);
    }

    /**
     * <p>
     * Test of getJavaVersionString method, of class Application.
     */
    @Test
    public void testGetSystemStrings() {
        String result = Application.get(getClass()).getJavaVersionString();
        LOG.info("getJavaVersionString: '{}'", result);
        assertNotNull(result);

        result = Application.get(getClass()).getJavaEncodingInfos();
        LOG.info("getJavaEncodingInfos: '{}'", result);
        assertNotNull(result);

        result = Application.get(getClass()).getJavaTimezone();
        LOG.info("getJavaTimezone: '{}'", result);
        assertNotNull(result);

        result = Application.get(getClass()).getJavaVmName();
        LOG.info("getJavaVmName: '{}'", result);
        assertNotNull(result);

        result = Application.get(getClass()).getOsName();
        LOG.info("getOsName: '{}'", result);
        assertNotNull(result);

    }

    /**
     * Test of getJavaClassVersion method, of class Application.
     */
    @Test
    public void testGetJavaClassVersion() {
        Float result = Application.get(getClass()).getJavaClassVersion();
        LOG.info("getJavaClassVersion: '{}'", result);
        assertNotNull(result);
    }
/**
     * Test of getMaxMemory method, of class Application.
     */
    @Test
    public void testGetMaxMemory() {
        String result = Application.get(getClass()).getMaxMemory();
        LOG.info("getMaxMemory: '{}'", result);
        assertNotNull(result);
    }
}
