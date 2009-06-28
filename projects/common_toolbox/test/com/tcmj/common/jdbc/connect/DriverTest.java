/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tcmj.common.jdbc.connect;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tcmj
 */
public class DriverTest {

    public DriverTest() {
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
     * Test of values method, of class Driver.
     */
    @Test
    public void testValues() {
        System.out.println("values");
        Driver[] result = Driver.values();
        assertEquals(10, result.length);
    }

    /**
     * Test of valueOf method, of class Driver.
     */
    @Test
    public void testValueOf() {
        System.out.println("valueOf");
        String name = "HSQLDB";
        Driver expResult = Driver.HSQLDB;
        Driver result = Driver.valueOf(name);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of getDriverClassName method, of class Driver.
     */
    @Test
    public void testGetDriverClassName() {
        System.out.println("getDriverClassName");
        Driver instance = Driver.HSQLDB;
        String expResult = "org.hsqldb.jdbcDriver";
        String result = instance.getDriverClassName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getUrl method, of class Driver.
     */
    @Test
    public void testGetUrl() {
        System.out.println("getUrl");
        Driver instance = Driver.HSQLDB;
        String expResult = "jdbc:hsqldb:";
        String result = instance.getUrl();
        assertEquals(expResult, result);
    }

    

    /**
     * Test of createURL method, of class Driver.
     */
    @Test
    public void testCreateURL() {
        System.out.println("createURL");
        String pDB_Alias_SID = "";
        String pHost = "";
        String pPort = "";
        Driver pDriver = null;
        String expResult = "";
        String result = Driver.createURL(pDB_Alias_SID, pHost, pPort, pDriver);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}