package com.tcmj.pm.spread.impl;

import com.tcmj.pm.spread.impl.SpreadPeriod;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author TDEUT
 */
public class SpreadPeriodTest {

    public SpreadPeriodTest() {
    }


    /**
     * Test of getEndMillis method, of class SpreadPeriod.
     */
    @Test
    public void testGetFinishDate() {
        System.out.println("getFinishDate");
        assertEquals(23435L, new SpreadPeriod(100, 23435L).getEndMillis());
        assertEquals(101L, new SpreadPeriod(1, 101).getEndMillis());
        assertEquals(102L, new SpreadPeriod(0, 102).getEndMillis());
    }


    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testGetFinishDateWithSameStartAndEndValue() {
        new SpreadPeriod(100, 100);
    }


    /**
     * Test of getStartMillis method, of class SpreadPeriod.
     */
    @Test
    public void testGetStartDate() {
        System.out.println("getStartDate");
        assertEquals(100L, new SpreadPeriod(100, 200).getStartMillis());
        assertEquals(500L, new SpreadPeriod(500, 2000).getStartMillis());
        assertEquals(0L, new SpreadPeriod(00, 2000).getStartMillis());
    }


    /**
     * Test of getCalcDuration method, of class SpreadPeriod.
     */
    @Test
    public void testGetCalcDuration() {
        System.out.println("getCalcDuration");
        assertEquals(100L, new SpreadPeriod(100, 200).getCalcDuration());
        assertEquals(200L, new SpreadPeriod(0, 200).getCalcDuration());
    }


    /**
     * Test of getPeriodValues method, of class SpreadPeriod.
     */
    @Test
    public void testGetPeriodValues() {
        System.out.println("getPeriodValues");
        SpreadPeriod instance = new SpreadPeriod(0, 2000);
        double[] input = {5.5};
        instance.setPeriodValues(input);
        double[] expResult = {5.5};
        double[] result = instance.getPeriodValues();
        assertEquals(expResult[0], result[0], 0);
    }


    /**
     * Test of getPeriodValue method, of class SpreadPeriod.
     */
    @Test
    public void testGetPeriodValue() {
        System.out.println("getPeriodValue");
        SpreadPeriod instance = new SpreadPeriod(0, 1);
        assertEquals(0, instance.getPeriodValue(), 0);
    }


    /**
     * Test of getCalcStartDate method, of class SpreadPeriod.
     */
    @Test
    public void testGetSetCalcStartDate() {
        System.out.println("getCalcStartDate");
        SpreadPeriod instance = new SpreadPeriod(0, 1000);
        assertEquals(0L, instance.getCalcStartDate());
        instance.setCalcStartDate(1234L);
        assertEquals(1234L, instance.getCalcStartDate());
    }


    /**
     * Test of getCalcFinishDate method, of class SpreadPeriod.
     */
    @Test
    public void testGetSetCalcFinishDate() {
        System.out.println("getCalcFinishDate");
        SpreadPeriod instance = new SpreadPeriod(0, 1000);
        assertEquals(1000L, instance.getCalcFinishDate());
        System.out.println("setCalcFinishDate");
        instance.setCalcFinishDate(1235L);
        assertEquals(1235L, instance.getCalcFinishDate());
    }


    /**
     * Test of isInsideTimespan method, of class SpreadPeriod.
     */
    @Test
    public void testInsideTimespan() {
        System.out.println("isInsideTimespan");
        SpreadPeriod instance = new SpreadPeriod(0, 20);
        assertEquals(false, instance.isInsideTimespan());
        instance.setInsideTimespan(true);
        assertEquals(true, instance.isInsideTimespan());
    }




    @Test
    public void testIntersectPeriod() {

        System.out.println("testIntersectPeriod");

        SpreadPeriod timespan = new SpreadPeriod(20, 30);

        //komplett ausserhalb (vorher)
        SpreadPeriod periodBefore = new SpreadPeriod(0, 10);
        periodBefore.adjustIntersectionDate(timespan);
        assertEquals("before [start]", 0, periodBefore.getCalcStartDate());
        assertEquals("before [finish]", 10, periodBefore.getCalcFinishDate());
        assertFalse("before [flag]", periodBefore.isInsideTimespan());

        //anstossend/beruehrend (vorne)
        SpreadPeriod periodProceeding = new SpreadPeriod(15, 20);
        periodProceeding.adjustIntersectionDate(timespan);
        assertEquals("proceeding [start]", 15, periodProceeding.getCalcStartDate());
        assertEquals("proceeding [finish]", 20, periodProceeding.getCalcFinishDate());
        assertFalse("proceeding [flag]", periodProceeding.isInsideTimespan());

        //ueberlappend (vorne)
        SpreadPeriod periodOverlapBefore = new SpreadPeriod(15, 25);
        periodOverlapBefore.adjustIntersectionDate(timespan);
        assertEquals("overlapping-before [start]", 20, periodOverlapBefore.getCalcStartDate());
        assertEquals("overlapping-before [finish]", 25, periodOverlapBefore.getCalcFinishDate());
        assertTrue("overlapping-before [flag]", periodOverlapBefore.isInsideTimespan());

        //ueberlappend um nur 1 Millisekunde (vorne)
        SpreadPeriod periodOverlapBeforeMS = new SpreadPeriod(19, 21);
        periodOverlapBeforeMS.adjustIntersectionDate(timespan);
        assertEquals("overlapping-before-only1MS [start]", 20, periodOverlapBeforeMS.getCalcStartDate());
        assertEquals("overlapping-before-only1MS [finish]", 21, periodOverlapBeforeMS.getCalcFinishDate());
        assertTrue("overlapping-before-only1MS [flag]", periodOverlapBeforeMS.isInsideTimespan());

        //exakt
        SpreadPeriod periodEquals = new SpreadPeriod(timespan.getStartMillis(), timespan.getEndMillis());
        periodEquals.adjustIntersectionDate(timespan);
        assertEquals("equal [start]", timespan.getStartMillis(), periodEquals.getCalcStartDate());
        assertEquals("equal [finish]", timespan.getEndMillis(), periodEquals.getCalcFinishDate());
        assertTrue("equal [flag]", periodEquals.isInsideTimespan());

        //innerhalb
        SpreadPeriod periodInside = new SpreadPeriod(22, 28);
        periodInside.adjustIntersectionDate(timespan);
        assertEquals("inside [start]", 22, periodInside.getCalcStartDate());
        assertEquals("inside [finish]", 28, periodInside.getCalcFinishDate());
        assertTrue("inside [flag]", periodInside.isInsideTimespan());

        //ueberlappend (hinten)
        SpreadPeriod periodOverlapAfter = new SpreadPeriod(25, 35);
        periodOverlapAfter.adjustIntersectionDate(timespan);
        assertEquals("overlapping-after [start]", 25, periodOverlapAfter.getCalcStartDate());
        assertEquals("overlapping-after [finish]", 30, periodOverlapAfter.getCalcFinishDate());
        assertTrue("overlapping-after [flag]", periodOverlapAfter.isInsideTimespan());

        //ueberlappend - nur 1 Millisekunde (hinten)
        SpreadPeriod periodOverlapAfter1MS = new SpreadPeriod(29, 31);
        periodOverlapAfter1MS.adjustIntersectionDate(timespan);
        assertEquals("overlapping-after-only1MS [start]", 29, periodOverlapAfter1MS.getCalcStartDate());
        assertEquals("overlapping-after-only1MS [finish]", 30, periodOverlapAfter1MS.getCalcFinishDate());
        assertTrue("overlapping-after-only1MS [flag]", periodOverlapAfter1MS.isInsideTimespan());

        //exakt anschließed/beruehrend (hinten)
        SpreadPeriod periodFollowing = new SpreadPeriod(30, 50);
        periodFollowing.adjustIntersectionDate(timespan);
        assertEquals("following [start]", 30, periodFollowing.getCalcStartDate());
        assertEquals("following [finish]", 50, periodFollowing.getCalcFinishDate());
        assertFalse("following [flag]", periodFollowing.isInsideTimespan());

        //komplett ausserhalb (nachher)
        SpreadPeriod periodAfter = new SpreadPeriod(40, 50);
        periodAfter.adjustIntersectionDate(timespan);
        assertEquals("after [start]", 40, periodAfter.getCalcStartDate());
        assertEquals("after [finish]", 50, periodAfter.getCalcFinishDate());
        assertFalse("after [flag]", periodAfter.isInsideTimespan());

    }



}
