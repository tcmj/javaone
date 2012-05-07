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
    public void testCreateUrlACCESS_MDB() {
        System.out.println("createURL:ACCESS_MDB");
        String expResult = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=.\\dbpath\\Accesfile.mdb";
        String result = Driver.createURL(".\\dbpath\\Accesfile.mdb", null, null, Driver.ACCESS_MDB);
        assertEquals(expResult, result);
    }

}