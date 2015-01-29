package com.tcmj.pm.spread.impl;

import com.tcmj.pm.spread.AbstractSpreadTest;
import com.tcmj.pm.spread.impl.SpreadPeriod;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

public class SpreadCalculatorTest extends AbstractSpreadTest {

    /**
     * Internal
     * @param periods
     * @param sum
     * @return
     */
    public static double sum(SpreadPeriod[] periods, int valueNo) {
        double mysum = 0;
        for (int i = 0; i < periods.length; i++) {
            if (periods[i].getPeriodValues() != null) {
                mysum += periods[i].getPeriodValues()[valueNo];
            }
        }
        return mysum;
    }

    public static double sum(SpreadPeriod[] periods) {
        return sum(periods, 0);
    }

    /**
     * Test of computeSpread method, of class SpreadCalculator.
     * Simple test of case
     * - 1 period
     * - 1 value
     * - time span (start - finish) equals period
     */
    @Test
    public void testComputeSpread_Testcase1() {
        System.out.println("\ntestComputeSpread_Testcase1");
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
     * Test of computeSpread method, of class SpreadCalculator.
     */
    @Test
    public void testComputeSpread_Testcase2() {
        System.out.println("\ntestComputeSpread_Testcase2");
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
     * Test of computeSpread method, of class SpreadCalculator.
     */
    @Test
    public void testComputeSpread_Testcase3() {
        System.out.println("\ntestComputeSpread_Testcase3");
        double[] pValue = {10000D};
        SpreadPeriod pTimespan = new SpreadPeriod(0, 100000);
        SpreadPeriod[] pPeriods = {new SpreadPeriod(0, 50000), new SpreadPeriod(50000, 100000)};
        double[] pCurve = {5};

        SpreadPeriod[] pExclusionPeriods = null;

        spread.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
        System.out.println("\tResult1: " + pPeriods[0].getPeriodValue());
        System.out.println("\tResult2: " + pPeriods[1].getPeriodValue());

        assertEquals("Period 1", 5000D, pPeriods[0].getPeriodValue(), DELTA);
        assertEquals("Period 2", 5000D, pPeriods[1].getPeriodValue(), DELTA);
        assertEquals("Sum", pValue[0], sum(pPeriods), DELTA);
    }

    /**
     * Test of computeSpread method, of class SpreadCalculator.
     */
    @Test
    public void testComputeSpread_Testcase4() {
        System.out.println("\ntestComputeSpread_Testcase4");
        double[] pValue = {1000D};
        SpreadPeriod pTimespan = new SpreadPeriod(0, 15000);
        SpreadPeriod[] pPeriods = {new SpreadPeriod(0, 5000), new SpreadPeriod(5000, 10000), new SpreadPeriod(10000, 15000)};
        double[] pCurve = {1};
        SpreadPeriod[] pExclusionPeriods = null;

        spread.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
        assertEquals("Period 1", 333.333333D, pPeriods[0].getPeriodValue(), DELTA);
        assertEquals("Period 2", 333.333333D, pPeriods[1].getPeriodValue(), DELTA);
        assertEquals("Period 3", 333.333333D, pPeriods[2].getPeriodValue(), DELTA);
        assertEquals("Sum", pValue[0], sum(pPeriods), DELTA);
    }

    /**
     * Test of computeSpread method, of class SpreadCalculator.
     */
    @Test
    public void testComputeSpread_Testcase5() {
        System.out.println("\ntestComputeSpread_Testcase5");
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
     * Test of computeSpread method, of class SpreadCalculator.
     */
    @Test
    public void testComputeSpread_Testcase6() {
        System.out.println("\ntestComputeSpread_Testcase6");
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
     * Test of computeSpread method, of class SpreadCalculator.
     */
    @Test
    public void testComputeSpread_Testcase7() {
        System.out.println("\ntestComputeSpread_Testcase7");
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
     * Test of computeSpread method, of class SpreadCalculator.
     */
    @Test
    public void testComputeSpread_Testcase8() {
        System.out.println("\ntestComputeSpread_Testcase8");
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
     * Test of computeSpread method, of class SpreadCalculator.
     * 3 zu verteilende Gesamtwerte.
     * 4 Perioden, die sich gegenseitig überlappen
     * 1 Periode wird von 1 anderen Periode vollständig überdeckt.
     * Keine Periode ragt aus dem Timespan heraus.
     * Kurve aus 2 Werten.
     * Keine Exclusions.
     */
    @Test
    public void testComputeSpread_Testcase9() {
        System.out.println("\ntestComputeSpread_Testcase9");
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
        System.out.println("\ntestComputeSpread_Testcase10");
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
    @Test
    public void testComputeSpread_Testcase11() {
        System.out.println("\ntestComputeSpread_Testcase11");
        double[] pValue = {1000D, 2000D, 0D};
        SpreadPeriod pTimespan = new SpreadPeriod(0, 20000);
        SpreadPeriod[] pPeriods = {new SpreadPeriod(0, 10000), new SpreadPeriod(5000, 15000), new SpreadPeriod(10000, 20000), new SpreadPeriod(15000, 20000)};
        double[] pCurve = {25, 75};
        SpreadPeriod[] pExclusionPeriods = {new SpreadPeriod(0, 10000)};

        spread.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
        System.out.println("Result 1: " + Arrays.asList(pPeriods));
        assertEquals("Period 1", 0D, pPeriods[0].getPeriodValue(0), DELTA);
        // TODO 
        assertEquals("Period 2", 250D, pPeriods[1].getPeriodValue(0), DELTA);
        assertEquals("Period 3", 1000D, pPeriods[2].getPeriodValue(0), DELTA);
        assertEquals("Period 4", 750D, pPeriods[3].getPeriodValue(0), DELTA);
        assertEquals("Sum", 2000D, sum(pPeriods, 0), DELTA);

        System.out.println("Result 2: " + Arrays.asList(pPeriods));
        assertEquals("Period 1", 0D, pPeriods[0].getPeriodValue(1), DELTA);
        assertEquals("Period 2", 500D, pPeriods[1].getPeriodValue(1), DELTA);
        assertEquals("Period 3", 2000D, pPeriods[2].getPeriodValue(1), DELTA);
        assertEquals("Period 4", 1500D, pPeriods[3].getPeriodValue(1), DELTA);
        assertEquals("Sum", 4000D, sum(pPeriods, 1), DELTA);

        System.out.println("Result 3: " + Arrays.asList(pPeriods));
        assertEquals("Period 1", 0D, pPeriods[0].getPeriodValue(2), DELTA);
        assertEquals("Period 2", 0D, pPeriods[1].getPeriodValue(2), DELTA);
        assertEquals("Period 3", 0D, pPeriods[2].getPeriodValue(2), DELTA);
        assertEquals("Period 4", 0D, pPeriods[3].getPeriodValue(2), DELTA);
        assertEquals("Sum", 0D, sum(pPeriods, 2), DELTA);
    }

    /**
     * Wie testComputeSpread_Testcase11, aber
     * - mit einer größeren Anzahl an Kurvenwerten (die jedoch dasselbe bedeuten)
     * - mit einer größeren Anzahl an Exclusions (die jedoch dasselbe bewirken)
     */
    @Test
    public void testComputeSpread_Testcase12() {
        System.out.println("\ntestComputeSpread_Testcase12");
        double[] pValue = {1000D, 2000D, 0D};
        SpreadPeriod pTimespan = new SpreadPeriod(0, 20000);
        SpreadPeriod[] pPeriods = {new SpreadPeriod(0, 10000), new SpreadPeriod(5000, 15000), new SpreadPeriod(10000, 20000), new SpreadPeriod(15000, 20000)};
        double[] pCurve = {25, 25, 25, 25, 75, 75, 75, 75};
        SpreadPeriod[] pExclusionPeriods = {new SpreadPeriod(0, 1000), new SpreadPeriod(1000, 2000),
            new SpreadPeriod(2000, 3000), new SpreadPeriod(3000, 4000), new SpreadPeriod(4000, 5000),
            new SpreadPeriod(5000, 6000), new SpreadPeriod(6000, 7000), new SpreadPeriod(7000, 8000),
            new SpreadPeriod(8000, 9000), new SpreadPeriod(9000, 10000)};

        spread.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
        System.out.println("Result 1: " + Arrays.asList(pPeriods));
        assertEquals("Period 1", 0D, pPeriods[0].getPeriodValue(0), DELTA);
        // TODO
        assertEquals("Period 2", 250D, pPeriods[1].getPeriodValue(0), DELTA);
        assertEquals("Period 3", 1000D, pPeriods[2].getPeriodValue(0), DELTA);
        assertEquals("Period 4", 750D, pPeriods[3].getPeriodValue(0), DELTA);
        assertEquals("Sum", 2000D, sum(pPeriods, 0), DELTA);

        System.out.println("Result 2: " + Arrays.asList(pPeriods));
        assertEquals("Period 1", 0D, pPeriods[0].getPeriodValue(1), DELTA);
        assertEquals("Period 2", 500D, pPeriods[1].getPeriodValue(1), DELTA);
        assertEquals("Period 3", 2000D, pPeriods[2].getPeriodValue(1), DELTA);
        assertEquals("Period 4", 1500D, pPeriods[3].getPeriodValue(1), DELTA);
        assertEquals("Sum", 4000D, sum(pPeriods, 1), DELTA);

        System.out.println("Result 3: " + Arrays.asList(pPeriods));
        assertEquals("Period 1", 0D, pPeriods[0].getPeriodValue(2), DELTA);
        assertEquals("Period 2", 0D, pPeriods[1].getPeriodValue(2), DELTA);
        assertEquals("Period 3", 0D, pPeriods[2].getPeriodValue(2), DELTA);
        assertEquals("Period 4", 0D, pPeriods[3].getPeriodValue(2), DELTA);
        assertEquals("Sum", 0D, sum(pPeriods, 2), DELTA);
    }

}
