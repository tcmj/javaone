package com.tcmj.pm.spread.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 */
public class SpreadDoubleImplTest {

    SpreadDoubleImpl spread;


    public SpreadDoubleImplTest() {
    }


    @Before
    public void setUp() {
        spread = new SpreadDoubleImpl();
    }


//    /**
//     * Test of initValues method, of class SpreadDoubleImpl.
//     */
//    @Test
//    public void testResetSingleValues() {
//        System.out.println("initValues");
//        SpreadPeriod[] pPeriods = null;
//        int pInitSize = 0;
//        SpreadDoubleImpl.initValues(pPeriods, pInitSize);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }


    /**
     * Eine Periode über die ganze Zeitspanne mit einer Exclusion innerhalb.
     */
    @Test
    public void testSubtractExclusions() {

        System.out.println("\ntestReducePeriodsByExclusions");

        SpreadPeriod pTimespan = new SpreadPeriod(1000, 11000);
        SpreadPeriod[] pPeriods = {new SpreadPeriod(1000, 11000)};
        SpreadPeriod[] pExclusions = {new SpreadPeriod(3000, 5000)};

        assertEquals("Period-CalcDuration-before", 10000, pPeriods[0].getCalcDuration());
        assertEquals("Timespan-CalcDuration-before", 10000, pTimespan.getCalcDuration());
        spread.subtractExclusions(pPeriods, pTimespan, pExclusions);

        assertEquals("Period-CalcStart", 1000, pPeriods[0].getCalcStartDate());
        assertEquals("Period-CalcFinish", 9000, pPeriods[0].getCalcFinishDate());
        assertEquals("Period-CalcDuration", 8000, pPeriods[0].getCalcDuration());

        assertEquals("Timespan-CalcStart", 1000, pPeriods[0].getCalcStartDate());
        assertEquals("Timespan-CalcFinish", 9000, pPeriods[0].getCalcFinishDate());
        assertEquals("Timespan-CalcDuration", 8000, pPeriods[0].getCalcDuration());

    }


    /**
     * Zwei Perioden, nicht überlappend, die gemeinsam die gesamte Zeitspanne abdecken
     * mit je einer Exclusion innerhalb jeder Periode.
     */
    @Test
    public void testSubtractExclusions2() {

        System.out.println("\ntestReducePeriodsByExclusions2");

        SpreadPeriod pTimespan = new SpreadPeriod(1000, 11000);
        SpreadPeriod[] pPeriods = {new SpreadPeriod(1000, 5000), new SpreadPeriod(5000, 11000)};
        SpreadPeriod[] pExclusions = {new SpreadPeriod(2000, 4000), new SpreadPeriod(8000, 10000)};

        assertEquals("Period[0]-CalcDuration-before", 4000, pPeriods[0].getCalcDuration());
        assertEquals("Period[1]-CalcDuration-before", 6000, pPeriods[1].getCalcDuration());
        assertEquals("Timespan-CalcDuration-before", 10000, pTimespan.getCalcDuration());

        spread.subtractExclusions(pPeriods, pTimespan, pExclusions);

        assertEquals("Timespan-CalcStart", 1000, pTimespan.getCalcStartDate());
        assertEquals("Timespan-CalcFinish", 7000, pTimespan.getCalcFinishDate());
        assertEquals("Timespan-CalcDuration", 6000, pTimespan.getCalcDuration());

        assertEquals("Exclusion[0]-CalcStart", 2000, pExclusions[0].getCalcStartDate());
        assertEquals("Exclusion[0]-CalcFinish", 4000, pExclusions[0].getCalcFinishDate());
        assertEquals("Exclusion[0]-CalcDuration", 2000, pExclusions[0].getCalcDuration());

        assertEquals("Period[0]-CalcStart", 1000, pPeriods[0].getCalcStartDate());
        assertEquals("Period[0]-CalcFinish", 3000, pPeriods[0].getCalcFinishDate());
        assertEquals("Period[0]-CalcDuration", 2000, pPeriods[0].getCalcDuration());


        assertEquals("Exclusion[1]-CalcStart", 6000, pExclusions[1].getCalcStartDate());
        assertEquals("Exclusion[1]-CalcFinish", 8000, pExclusions[1].getCalcFinishDate());
        assertEquals("Exclusion[1]-CalcDuration", 2000, pExclusions[1].getCalcDuration());

        assertEquals("Period[1]-CalcStart", 3000, pPeriods[1].getCalcStartDate());
        assertEquals("Period[1]-CalcFinish", 7000, pPeriods[1].getCalcFinishDate());
        assertEquals("Period[1]-CalcDuration", 4000, pPeriods[1].getCalcDuration());



    }


}
