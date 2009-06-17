 /*
 * Created on 03.03.2009
 * Copyright(c) 2009 tcmj.  All Rights Reserved.
 * @author TDEUT - Thomas Deutsch - 2009
 */

package com.tcmj.pm.conflicts.data;

import java.util.Date;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author tdeut
 */
public class SimpleBarTest {

    private static SimpleBar simplebarC1;
    private static SimpleBar simplebarC2;
    
    public SimpleBarTest() {
    }
    @BeforeClass
    public static void setUpClass() throws Exception {
        simplebarC1 = new SimpleBar("key1", new Date(), new Date());
        simplebarC2 = new SimpleBar("key2", new Date(), new Date(),5D);
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    @Test
    public void testGetKey() {
        assertEquals("key1", simplebarC1.getKey());
        assertEquals("key2", simplebarC2.getKey());
    }


    @Test
    public void testGetStartDate() {
        assertNotNull(simplebarC1.getStartDate());
        assertNotNull(simplebarC2.getStartDate());
    }


    @Test
    public void testGetEndDate() {
        assertNotNull(simplebarC1.getEndDate());
        assertNotNull(simplebarC2.getEndDate());
    }



    @Test
    public void testGetWeight() {
        assertEquals(1D, simplebarC1.getWeight(),0);
        assertEquals(5D, simplebarC2.getWeight(),0);
        
        
        SimpleBar sbar = new SimpleBar("xxx", new Date(), new Date(), 1.2345D);
        assertEquals(1.2345D, sbar.getWeight(),0);
        sbar.setWeight(0.5454);
        assertEquals(0.5454D, sbar.getWeight(),0);
    }


}