package com.tcmj.pm.spread.impl;

import com.tcmj.pm.spread.AbstractSpreadTest;
import com.tcmj.pm.spread.impl.SpreadPeriod;
import org.junit.Rule;
import org.databene.contiperf.junit.ContiPerfRule;
import org.databene.contiperf.Required;
import org.databene.contiperf.PerfTest;
import java.util.Arrays;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author TWOSE
 */
//@PerfTest(invocations = 5)
//  @Required(max = 1200, average = 250)
@PerfTest(invocations = 100, threads = 30)
public class SpreadTest extends AbstractSpreadTest {

    @Rule
    public ContiPerfRule i = new ContiPerfRule();


//    /**
//     * Eine Periode über die ganze Zeitspanne mit einer Exclusion innerhalb.
//     */
//    @Test
//    public void testReducePeriodsByExclusions() {
//
//        SpreadPeriod pTimespan = new SpreadPeriod(1000, 11000);
//        SpreadPeriod[] pPeriods = {new SpreadPeriod(1000, 11000)};
//        SpreadPeriod[] pExclusions = {new SpreadPeriod(3000, 5000)};
//
//        assertEquals("Period-CalcDuration-before", 10000, pPeriods[0].getCalcDuration());
//        assertEquals("Timespan-CalcDuration-before", 10000, pTimespan.getCalcDuration());
//        spread.subtractExclusions(pPeriods, pTimespan, pExclusions);
//
//        assertEquals("Period-CalcStart", 1000, pPeriods[0].getCalcStartDate());
//        assertEquals("Period-CalcFinish", 9000, pPeriods[0].getCalcFinishDate());
//        assertEquals("Period-CalcDuration", 8000, pPeriods[0].getCalcDuration());
//
//        assertEquals("Timespan-CalcStart", 1000, pPeriods[0].getCalcStartDate());
//        assertEquals("Timespan-CalcFinish", 9000, pPeriods[0].getCalcFinishDate());
//        assertEquals("Timespan-CalcDuration", 8000, pPeriods[0].getCalcDuration());
//
//    }

//    /**
//     * Zwei Perioden, nicht überlappend, die gemeinsam die gesamte Zeitspanne abdecken
//     * mit je einer Exclusion innerhalb jeder Periode.
//     */
//    @Test
//    public void testReducePeriodsByExclusions2() {
//
//        SpreadPeriod pTimespan = new SpreadPeriod(1000, 11000);
//        SpreadPeriod[] pPeriods = {new SpreadPeriod(1000, 5000), new SpreadPeriod(5000, 11000)};
//        SpreadPeriod[] pExclusions = {new SpreadPeriod(2000, 4000), new SpreadPeriod(8000, 10000)};
//
//        assertEquals("Period[0]-CalcDuration-before", 4000, pPeriods[0].getCalcDuration());
//        assertEquals("Period[1]-CalcDuration-before", 6000, pPeriods[1].getCalcDuration());
//        assertEquals("Timespan-CalcDuration-before", 10000, pTimespan.getCalcDuration());
//
//        spread.subtractExclusions(pPeriods, pTimespan, pExclusions);
//
//        assertEquals("Timespan-CalcStart", 1000, pTimespan.getCalcStartDate());
//        assertEquals("Timespan-CalcFinish", 7000, pTimespan.getCalcFinishDate());
//        assertEquals("Timespan-CalcDuration", 6000, pTimespan.getCalcDuration());
//
//        assertEquals("Exclusion[0]-CalcStart", 2000, pExclusions[0].getCalcStartDate());
//        assertEquals("Exclusion[0]-CalcFinish", 4000, pExclusions[0].getCalcFinishDate());
//        assertEquals("Exclusion[0]-CalcDuration", 2000, pExclusions[0].getCalcDuration());
//
//        assertEquals("Period[0]-CalcStart", 1000, pPeriods[0].getCalcStartDate());
//        assertEquals("Period[0]-CalcFinish", 3000, pPeriods[0].getCalcFinishDate());
//        assertEquals("Period[0]-CalcDuration", 2000, pPeriods[0].getCalcDuration());
//
//
//        assertEquals("Exclusion[1]-CalcStart", 6000, pExclusions[1].getCalcStartDate());
//        assertEquals("Exclusion[1]-CalcFinish", 8000, pExclusions[1].getCalcFinishDate());
//        assertEquals("Exclusion[1]-CalcDuration", 2000, pExclusions[1].getCalcDuration());
//
//        assertEquals("Period[1]-CalcStart", 3000, pPeriods[1].getCalcStartDate());
//        assertEquals("Period[1]-CalcFinish", 7000, pPeriods[1].getCalcFinishDate());
//        assertEquals("Period[1]-CalcDuration", 4000, pPeriods[1].getCalcDuration());
//
//
//
//    }

    /**
     * Internal
     * @param periods
     * @param sum
     * @return
     */
    private double sum(SpreadPeriod[] periods, int valueNo) {
        double mysum = 0;
        for (int i = 0; i < periods.length; i++) {
            mysum += periods[i].getPeriodValues()[valueNo];
        }
        return mysum;
    }


    private double sum(SpreadPeriod[] periods) {
        return sum(periods, 0);
    }


    /**
     * Test of computeSpread method, of class SpreadDoubleImpl.
     * Simple test of case
     * - 1 period
     * - 1 value
     * - time span (start - finish) equals period
     */
    @Test
    public void testComputeSpread_Testcase1() {
        System.out.println("computeSpread_Testcase1");
        double[] pValue = {1000D};
        SpreadPeriod pTimespan = new SpreadPeriod(0, 10000);
        SpreadPeriod[] pPeriods = {new SpreadPeriod(0, 10000)};
        double[] pCurve = {1};
        SpreadPeriod[] pExclusionPeriods = null;
        spread.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
        assertEquals("Period 1", pValue[0], pPeriods[0].getPeriodValue(), DELTA);
        assertEquals("Sum", pValue[0], sum(pPeriods), DELTA);
    }

    /**
     * Test of computeSpread method, of class SpreadDoubleImpl.
     */
    @Test
    public void testComputeSpread_Testcase2() {
        System.out.println("computeSpread_Testcase2");
        double[] pValue = {5000D};
        SpreadPeriod pTimespan = new SpreadPeriod(0, 10000);
        SpreadPeriod[] pPeriods = {new SpreadPeriod(0, 10000)};
        double[] pCurve = {1};
        SpreadPeriod[] pExclusionPeriods = null;
        spread.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
        assertEquals("Period 1", 5000D, pPeriods[0].getPeriodValue(), DELTA);
        assertEquals("Sum", pValue[0], sum(pPeriods), DELTA);
    }


    /**
     * Test of computeSpread method, of class SpreadDoubleImpl.
     */
    @Test
//    @Ignore
    public void testComputeSpread_Testcase3() {
        System.out.println("computeSpread_Testcase3");
        double[] pValue = {10000D};
        SpreadPeriod pTimespan = new SpreadPeriod(1, 100000);
        SpreadPeriod[] pPeriods = {new SpreadPeriod(1, 50000), new SpreadPeriod(50000, 100000)};
        double[] pCurve = {1, 1};
        SpreadPeriod[] pExclusionPeriods = null;

        spread.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
        System.out.println("\tResult1: " + pPeriods[0].getPeriodValue());
        System.out.println("\tResult2: " + pPeriods[1].getPeriodValue());

        assertEquals("Period 1", 5000D, pPeriods[0].getPeriodValue(), DELTA);
        assertEquals("Period 2", 5000D, pPeriods[1].getPeriodValue(), DELTA);
        assertEquals("Sum", pValue[0], sum(pPeriods), DELTA);
    }


    /**
     * Test of computeSpread method, of class SpreadDoubleImpl.
     */
//    @Test
//    @Ignore
    public void testComputeSpread_Testcase4() {
        System.out.println("computeSpread_Testcase4");
        double[] pValue = {1000D};
        SpreadPeriod pTimespan = new SpreadPeriod(0, 15000);
        SpreadPeriod[] pPeriods = {new SpreadPeriod(0, 5000), new SpreadPeriod(5001, 10000), new SpreadPeriod(10001, 15000)};
        double[] pCurve = {1};
        SpreadPeriod[] pExclusionPeriods = null;

        spread.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
        assertEquals("Period 1", 333.33333D, pPeriods[0].getPeriodValue(), DELTA);
        assertEquals("Period 2", 333.33333D, pPeriods[1].getPeriodValue(), DELTA);
        assertEquals("Period 3", 333.33333D, pPeriods[2].getPeriodValue(), DELTA);
        assertEquals("Sum", pValue[0], sum(pPeriods), DELTA);
    }

    /**
     * Test of computeSpread method, of class SpreadDoubleImpl.
     */
    @Test
    public void testComputeSpread_Testcase5() {
        System.out.println("computeSpread_Testcase5");
        double[] pValue = {1000D};
        SpreadPeriod pTimespan = new SpreadPeriod(0, 20000);
        SpreadPeriod[] pPeriods = {new SpreadPeriod(0, 5000), new SpreadPeriod(5000, 10000), new SpreadPeriod(10000, 15000), new SpreadPeriod(15000, 20000)};
        double[] pCurve = {1};
        SpreadPeriod[] pExclusionPeriods = null;

        spread.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
        assertEquals("Period 1", 250D, pPeriods[0].getPeriodValue(), DELTA);
        assertEquals("Period 2", 250D, pPeriods[1].getPeriodValue(), DELTA);
        assertEquals("Period 3", 250D, pPeriods[2].getPeriodValue(), DELTA);
        assertEquals("Period 4", 250D, pPeriods[3].getPeriodValue(), DELTA);
        assertEquals("Sum", pValue[0], sum(pPeriods), DELTA);
    }


    /**
     * Test of computeSpread method, of class SpreadDoubleImpl.
     */
    @Test
    public void testComputeSpread_Testcase6() {
        System.out.println("computeSpread_Testcase6");
        double[] pValue = {1000D};
        SpreadPeriod pTimespan = new SpreadPeriod(0, 20000);
        SpreadPeriod[] pPeriods = {new SpreadPeriod(0, 5000), new SpreadPeriod(5000, 10000), new SpreadPeriod(10000, 15000), new SpreadPeriod(15000, 20000)};
        double[] pCurve = {25, 75};
        SpreadPeriod[] pExclusionPeriods = null;

        spread.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
        assertEquals("Period 1", 125D, pPeriods[0].getPeriodValue(), DELTA);
        assertEquals("Period 2", 125D, pPeriods[1].getPeriodValue(), DELTA);
        assertEquals("Period 3", 375D, pPeriods[2].getPeriodValue(), DELTA);
        assertEquals("Period 4", 375D, pPeriods[3].getPeriodValue(), DELTA);
        assertEquals("Sum", pValue[0], sum(pPeriods), DELTA);
    }

    /**
     * Test of computeSpread method, of class SpreadDoubleImpl.
     */
    @Test
    public void testComputeSpread_Testcase7() {
        System.out.println("computeSpread_Testcase7");
        double[] pValue = {1000D};
        SpreadPeriod pTimespan = new SpreadPeriod(0, 20000);
        SpreadPeriod[] pPeriods = {new SpreadPeriod(0, 10000), new SpreadPeriod(5000, 15000), new SpreadPeriod(10000, 20000), new SpreadPeriod(15000, 20000)};
        double[] pCurve = {25, 75};
        SpreadPeriod[] pExclusionPeriods = null;

        spread.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
        System.out.println("Result: " + Arrays.asList(pPeriods));
        assertEquals("Period 1", 250D, pPeriods[0].getPeriodValue(), DELTA);  // ganzes erstes intervall
        assertEquals("Period 2", 500D, pPeriods[1].getPeriodValue(), DELTA); // 125 + 375
        assertEquals("Period 3", 750D, pPeriods[2].getPeriodValue(), DELTA); //ganzes zweites intervall
        assertEquals("Period 4", 375D, pPeriods[3].getPeriodValue(), DELTA); // hälfte vom zweiten intervall
        assertEquals("Sum", 1875D, sum(pPeriods), DELTA);   //Achtung! Ueberlappende Perioden!
    }

    /**
     * Test of computeSpread method, of class SpreadDoubleImpl.
     */
    @Test
//    @PerfTest(invocations = 1000, threads = 20)
    public void testComputeSpread_Testcase8() {
        System.out.println("computeSpread_Testcase8");
        double[] pValue = {1000D};
        SpreadPeriod pTimespan = new SpreadPeriod(0, 20000);
        SpreadPeriod[] pPeriods = {new SpreadPeriod(0, 10000), new SpreadPeriod(5000, 15000), new SpreadPeriod(10000, 20000), new SpreadPeriod(15000, 20000)};
        double[] pCurve = {25, 75};
        SpreadPeriod[] pExclusionPeriods = null;

        spread.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
        System.out.println("Result: " + Arrays.asList(pPeriods));
        assertEquals("Period 1", 250D, pPeriods[0].getPeriodValue(), DELTA);  // ganzes erstes intervall
        assertEquals("Period 2", 500D, pPeriods[1].getPeriodValue(), DELTA); // 125 + 375
        assertEquals("Period 3", 750D, pPeriods[2].getPeriodValue(), DELTA); //ganzes zweites intervall
        assertEquals("Period 4", 375D, pPeriods[3].getPeriodValue(), DELTA); // hälfte vom zweiten intervall
        assertEquals("Sum", 1875D, sum(pPeriods), DELTA);   //Achtung! Ueberlappende Perioden!
    }

    /**
     * Test of computeSpread method, of class SpreadDoubleImpl.
     * 3 zu verteilende Gesamtwerte.
     * 4 Perioden, die sich gegenseitig überlappen
     * 1 Periode wird von 1 anderen Periode vollständig überdeckt.
     * Keine Periode ragt aus dem Timespan heraus.
     * Kurve aus 2 Werten.
     * Keine Exclusions.
     */
    @Test
    public void testComputeSpread_Testcase9() {
        System.out.println("computeSpread_Testcase9");
        double[] pValue = {1000D, 2000D, 0D};
        SpreadPeriod pTimespan = new SpreadPeriod(0, 20000);
        SpreadPeriod[] pPeriods = {new SpreadPeriod(0, 10000), new SpreadPeriod(5000, 15000), new SpreadPeriod(10000, 20000), new SpreadPeriod(15000, 20000)};
        double[] pCurve = {25, 75};
        SpreadPeriod[] pExclusionPeriods = null;

        spread.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
        System.out.println("Result 1: " + Arrays.asList(pPeriods));
        assertEquals("Period 1", 250D, pPeriods[0].getPeriodValue(0), DELTA);  // ganzes erstes intervall
        assertEquals("Period 2", 500D, pPeriods[1].getPeriodValue(0), DELTA); // 125 + 375
        assertEquals("Period 3", 750D, pPeriods[2].getPeriodValue(0), DELTA); //ganzes zweites intervall
        assertEquals("Period 4", 375D, pPeriods[3].getPeriodValue(0), DELTA); // hälfte vom zweiten intervall
        assertEquals("Sum", 1875D, sum(pPeriods, 0), DELTA);   //Achtung! Ueberlappende Perioden!

        System.out.println("Result 2: " + Arrays.asList(pPeriods));
        assertEquals("Period 1", 500D, pPeriods[0].getPeriodValue(1), DELTA);  // ganzes erstes intervall
        assertEquals("Period 2", 1000D, pPeriods[1].getPeriodValue(1), DELTA); // 125 + 375
        assertEquals("Period 3", 1500D, pPeriods[2].getPeriodValue(1), DELTA); //ganzes zweites intervall
        assertEquals("Period 4", 750D, pPeriods[3].getPeriodValue(1), DELTA); // hälfte vom zweiten intervall
        assertEquals("Sum", 3750D, sum(pPeriods, 1), DELTA);   //Achtung! Ueberlappende Perioden!

        System.out.println("Result 3: " + Arrays.asList(pPeriods));
        assertEquals("Period 1", 0D, pPeriods[0].getPeriodValue(2), DELTA);  // ganzes erstes intervall
        assertEquals("Period 2", 0D, pPeriods[1].getPeriodValue(2), DELTA); // 125 + 375
        assertEquals("Period 3", 0D, pPeriods[2].getPeriodValue(2), DELTA); //ganzes zweites intervall
        assertEquals("Period 4", 0D, pPeriods[3].getPeriodValue(2), DELTA); // hälfte vom zweiten intervall
        assertEquals("Sum", 0D, sum(pPeriods, 2), DELTA);   //Achtung! Ueberlappende Perioden!
    }

    /**
     * Wie testComputeSpread_Testcase10, aber
     * - erste Periode ragt nach vorne aus dem Timespan heraus
     * - letzte Periode ragt nach hinten aus dem Timespan heraus
     */
    @Test
    public void testComputeSpread_Testcase10() {
        System.out.println("computeSpread_Testcase10");
        double[] pValue = {1000D, 2000D, 0D};
        SpreadPeriod pTimespan = new SpreadPeriod(0, 20000);
        SpreadPeriod[] pPeriods = {new SpreadPeriod(-1000, 10000), new SpreadPeriod(5000, 15000), new SpreadPeriod(10000, 20000), new SpreadPeriod(15000, 21000)};
        double[] pCurve = {25, 75};
        SpreadPeriod[] pExclusionPeriods = null;

        spread.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
        System.out.println("Result 1: " + Arrays.asList(pPeriods));
        assertEquals("Period 1", 250D, pPeriods[0].getPeriodValue(0), DELTA);  // ganzes erstes intervall
        assertEquals("Period 2", 500D, pPeriods[1].getPeriodValue(0), DELTA); // 125 + 375
        assertEquals("Period 3", 750D, pPeriods[2].getPeriodValue(0), DELTA); //ganzes zweites intervall
        assertEquals("Period 4", 375D, pPeriods[3].getPeriodValue(0), DELTA); // hälfte vom zweiten intervall
        assertEquals("Sum", 1875D, sum(pPeriods, 0), DELTA);   //Achtung! Ueberlappende Perioden!

        System.out.println("Result 2: " + Arrays.asList(pPeriods));
        assertEquals("Period 1", 500D, pPeriods[0].getPeriodValue(1), DELTA);  // ganzes erstes intervall
        assertEquals("Period 2", 1000D, pPeriods[1].getPeriodValue(1), DELTA); // 125 + 375
        assertEquals("Period 3", 1500D, pPeriods[2].getPeriodValue(1), DELTA); //ganzes zweites intervall
        assertEquals("Period 4", 750D, pPeriods[3].getPeriodValue(1), DELTA); // hälfte vom zweiten intervall
        assertEquals("Sum", 3750D, sum(pPeriods, 1), DELTA);   //Achtung! Ueberlappende Perioden!

        System.out.println("Result 3: " + Arrays.asList(pPeriods));
        assertEquals("Period 1", 0D, pPeriods[0].getPeriodValue(2), DELTA);  // ganzes erstes intervall
        assertEquals("Period 2", 0D, pPeriods[1].getPeriodValue(2), DELTA); // 125 + 375
        assertEquals("Period 3", 0D, pPeriods[2].getPeriodValue(2), DELTA); //ganzes zweites intervall
        assertEquals("Period 4", 0D, pPeriods[3].getPeriodValue(2), DELTA); // hälfte vom zweiten intervall
        assertEquals("Sum", 0D, sum(pPeriods, 2), DELTA);   //Achtung! Ueberlappende Perioden!
    }

    /**
     * Wie testComputeSpread_Testcase10, aber
     * - die erste Hälfte ist von Exclusion überdeckt
     * - Periode 1 ist voll von Exclusion überdeckt
     * - Periode 2 ist halb von Exclusion überdeckt
     *
     */
//    @Test
    @Ignore
    public void testComputeSpread_Testcase11() {
        System.out.println("computeSpread_Testcase11");
        double[] pValue = {1000D, 2000D, 0D};
        SpreadPeriod pTimespan = new SpreadPeriod(0, 20000);
        SpreadPeriod[] pPeriods = {new SpreadPeriod(0, 10000), new SpreadPeriod(5000, 15000), new SpreadPeriod(10000, 20000), new SpreadPeriod(15000, 20000)};
        double[] pCurve = {25, 75};
        SpreadPeriod[] pExclusionPeriods = {new SpreadPeriod(0, 10000)};

        spread.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
        System.out.println("Result 1: " + Arrays.asList(pPeriods));
        assertEquals("Period 1", 0D, pPeriods[0].getPeriodValue(0), DELTA);  // ganzes erstes intervall
        // TODO 
        assertEquals("Period 2", 250D, pPeriods[1].getPeriodValue(0), DELTA); // 125 + 375
        assertEquals("Period 3", 1000D, pPeriods[2].getPeriodValue(0), DELTA); //ganzes zweites intervall
        assertEquals("Period 4", 750D, pPeriods[3].getPeriodValue(0), DELTA); // hälfte vom zweiten intervall
        assertEquals("Sum", 1875D, sum(pPeriods, 0), DELTA);   //Achtung! Ueberlappende Perioden!

        System.out.println("Result 2: " + Arrays.asList(pPeriods));
        assertEquals("Period 1", 0D, pPeriods[0].getPeriodValue(1), DELTA);  // ganzes erstes intervall
        assertEquals("Period 2", 500D, pPeriods[1].getPeriodValue(1), DELTA); // 125 + 375
        assertEquals("Period 3", 2000D, pPeriods[2].getPeriodValue(1), DELTA); //ganzes zweites intervall
        assertEquals("Period 4", 1500D, pPeriods[3].getPeriodValue(1), DELTA); // hälfte vom zweiten intervall
        assertEquals("Sum", 3750D, sum(pPeriods, 1), DELTA);   //Achtung! Ueberlappende Perioden!

        System.out.println("Result 3: " + Arrays.asList(pPeriods));
        assertEquals("Period 1", 0D, pPeriods[0].getPeriodValue(2), DELTA);  // ganzes erstes intervall
        assertEquals("Period 2", 0D, pPeriods[1].getPeriodValue(2), DELTA); // 125 + 375
        assertEquals("Period 3", 0D, pPeriods[2].getPeriodValue(2), DELTA); //ganzes zweites intervall
        assertEquals("Period 4", 0D, pPeriods[3].getPeriodValue(2), DELTA); // hälfte vom zweiten intervall
        assertEquals("Sum", 0D, sum(pPeriods, 2), DELTA);   //Achtung! Ueberlappende Perioden!
    }

//
//    @Test
//    public void testCalculateIntersectionOfPeriods() {
//        SpreadPeriod timespan = new SpreadPeriod(20, 30);
//
//        //komplett ausserhalb (vorher)
//        SpreadPeriod periodBefore = new SpreadPeriod(0, 10);
//        spread.intersectPeriod(periodBefore, timespan);
//        assertEquals("before [start]", 0, periodBefore.getCalcStartDate());
//        assertEquals("before [finish]", 10, periodBefore.getCalcFinishDate());
//        assertFalse("before [flag]", periodBefore.isInsideTimespan());
//
//        //anstossend/beruehrend (vorne)
//        SpreadPeriod periodProceeding = new SpreadPeriod(15, 20);
//        spread.intersectPeriod(periodProceeding, timespan);
//        assertEquals("proceeding [start]", 15, periodProceeding.getCalcStartDate());
//        assertEquals("proceeding [finish]", 20, periodProceeding.getCalcFinishDate());
//        assertFalse("proceeding [flag]", periodProceeding.isInsideTimespan());
//
//        //ueberlappend (vorne)
//        SpreadPeriod periodOverlapBefore = new SpreadPeriod(15, 25);
//        spread.intersectPeriod(periodOverlapBefore, timespan);
//        assertEquals("overlapping-before [start]", 20, periodOverlapBefore.getCalcStartDate());
//        assertEquals("overlapping-before [finish]", 25, periodOverlapBefore.getCalcFinishDate());
//        assertTrue("overlapping-before [flag]", periodOverlapBefore.isInsideTimespan());
//
//        //ueberlappend um nur 1 Millisekunde (vorne)
//        SpreadPeriod periodOverlapBeforeMS = new SpreadPeriod(19, 21);
//        spread.intersectPeriod(periodOverlapBeforeMS, timespan);
//        assertEquals("overlapping-before-only1MS [start]", 20, periodOverlapBeforeMS.getCalcStartDate());
//        assertEquals("overlapping-before-only1MS [finish]", 21, periodOverlapBeforeMS.getCalcFinishDate());
//        assertTrue("overlapping-before-only1MS [flag]", periodOverlapBeforeMS.isInsideTimespan());
//
//        //exakt
//        SpreadPeriod periodEquals = new SpreadPeriod(timespan.getStartMillis(), timespan.getEndMillis());
//        spread.intersectPeriod(periodEquals, timespan);
//        assertEquals("equal [start]", timespan.getStartMillis(), periodEquals.getCalcStartDate());
//        assertEquals("equal [finish]", timespan.getEndMillis(), periodEquals.getCalcFinishDate());
//        assertTrue("equal [flag]", periodEquals.isInsideTimespan());
//
//        //innerhalb
//        SpreadPeriod periodInside = new SpreadPeriod(22, 28);
//        spread.intersectPeriod(periodInside, timespan);
//        assertEquals("inside [start]", 22, periodInside.getCalcStartDate());
//        assertEquals("inside [finish]", 28, periodInside.getCalcFinishDate());
//        assertTrue("inside [flag]", periodInside.isInsideTimespan());
//
//        //ueberlappend (hinten)
//        SpreadPeriod periodOverlapAfter = new SpreadPeriod(25, 35);
//        spread.intersectPeriod(periodOverlapAfter, timespan);
//        assertEquals("overlapping-after [start]", 25, periodOverlapAfter.getCalcStartDate());
//        assertEquals("overlapping-after [finish]", 30, periodOverlapAfter.getCalcFinishDate());
//        assertTrue("overlapping-after [flag]", periodOverlapAfter.isInsideTimespan());
//
//        //ueberlappend - nur 1 Millisekunde (hinten)
//        SpreadPeriod periodOverlapAfter1MS = new SpreadPeriod(29, 31);
//        spread.intersectPeriod(periodOverlapAfter1MS, timespan);
//        assertEquals("overlapping-after-only1MS [start]", 29, periodOverlapAfter1MS.getCalcStartDate());
//        assertEquals("overlapping-after-only1MS [finish]", 30, periodOverlapAfter1MS.getCalcFinishDate());
//        assertTrue("overlapping-after-only1MS [flag]", periodOverlapAfter1MS.isInsideTimespan());
//
//        //exakt anschließed/beruehrend (hinten)
//        SpreadPeriod periodFollowing = new SpreadPeriod(30, 50);
//        spread.intersectPeriod(periodFollowing, timespan);
//        assertEquals("following [start]", 30, periodFollowing.getCalcStartDate());
//        assertEquals("following [finish]", 50, periodFollowing.getCalcFinishDate());
//        assertFalse("following [flag]", periodFollowing.isInsideTimespan());
//
//        //komplett ausserhalb (nachher)
//        SpreadPeriod periodAfter = new SpreadPeriod(40, 50);
//        spread.intersectPeriod(periodAfter, timespan);
//        assertEquals("after [start]", 40, periodAfter.getCalcStartDate());
//        assertEquals("after [finish]", 50, periodAfter.getCalcFinishDate());
//        assertFalse("after [flag]", periodAfter.isInsideTimespan());
//
//    }



}
