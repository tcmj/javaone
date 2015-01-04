package com.tcmj.common.tools.lang;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 */
public class ApplicationTest {

    public ApplicationTest() {
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }


    /**
     * Test of getApplicationTitle method, of class Application.
     */
    @Test
    public void testGetApplicationTitle() {
        System.out.println("getApplicationTitle");
        Class context = getClass();
        String result = Application.getApplicationTitle(context);
        assertNotNull(result);
    }


    /**
     * Test of getApplicationVersion method, of class Application.
     */
    @Test
    public void testGetApplicationVersion() {
        System.out.println("getApplicationVersion");
        Class context = getClass();
        String result = Application.getApplicationVersion(context);
        assertNotNull(result);
    }


    /**
     * Test of getApplicationVendor method, of class Application.
     */
    @Test
    public void testGetApplicationVendor() {
        System.out.println("getApplicationVendor");
        Class context = getClass();
        String result = Application.getApplicationVendor(context);
        assertNotNull(result);
    }

}