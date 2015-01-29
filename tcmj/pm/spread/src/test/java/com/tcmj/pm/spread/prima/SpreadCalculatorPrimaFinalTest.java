package com.tcmj.pm.spread.prima;

import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 */
public class SpreadCalculatorPrimaFinalTest {

    static final double DELTA = 0.000001D;
    static final String NEWLINE = System.getProperty("line.separator");

    /**
     * Eine Periode über die ganze Zeitspanne mit einer Exclusion innerhalb
     * sowie mit je einer Timespan, deren Start+Ende identisch mit Start+Ende
     * jeweils einer Periode ist.
     */
    @Test
    public void testSegregateExclusions() {

        System.out.println(NEWLINE + "testSegregateExclusions");

        Period pTimespan = new Period(1000, 11000);
        Period[] pPeriods = {new Period(1000, 11000)};
        Period[] pExclusions = {new Period(3000, 5000)};
        TimeRelatedValue[] pTotalValues = {new TimeRelatedValue(333, 1000, 11000, 22)};

        assertEquals("Period-CalcDuration-before", 10000, pPeriods[0].getCalcDuration());
        assertEquals("Timespan-CalcDuration-before", 10000, pTimespan.getCalcDuration());
        assertEquals("TotalValue-CalcDuration-before", 10000, pTimespan.getCalcDuration());
        SpreadCalculatorSaprimaFinal.segregateExclusions(pPeriods, pTimespan, pTotalValues, pExclusions);

        assertEquals("Period-CalcStart", 1000, pPeriods[0].getCalcStartDate());
        assertEquals("Period-CalcFinish", 9000, pPeriods[0].getCalcFinishDate());
        assertEquals("Period-CalcDuration", 8000, pPeriods[0].getCalcDuration());

        assertEquals("Timespan-CalcStart", 1000, pPeriods[0].getCalcStartDate());
        assertEquals("Timespan-CalcFinish", 9000, pPeriods[0].getCalcFinishDate());
        assertEquals("Timespan-CalcDuration", 8000, pPeriods[0].getCalcDuration());

        assertEquals("TotalValue-CalcStart", 1000, pTotalValues[0].getCalcStartDate());
        assertEquals("TotalValue-CalcFinish", 9000, pTotalValues[0].getCalcFinishDate());
        assertEquals("TotalValue-CalcDuration", 8000, pTotalValues[0].getCalcDuration());

        // Original values must not be changed
        assertEquals("Timespan-Start", 1000, pTimespan.getStartDate());
        assertEquals("Timespan-Finish", 11000, pTimespan.getFinishDate());

        assertEquals("Period-Start", 1000, pPeriods[0].getStartDate());
        assertEquals("Period-Finish", 11000, pPeriods[0].getFinishDate());

        assertEquals("TotalValue-Start", 1000, pTotalValues[0].getStartDate());
        assertEquals("TotalValue-Finish", 11000, pTotalValues[0].getFinishDate());
        assertEquals("TotalValue-Value", 0, Double.compare(333, pTotalValues[0].getValue()));
        assertEquals("TotalValue-Index", 22, pTotalValues[0].getIndex());

    }

    /**
     * Zwei Perioden, nicht überlappend, die gemeinsam die gesamte Zeitspanne abdecken
     * mit je einer Exclusion innerhalb jeder Periode sowie mit je einer Timespan,
     * deren Start+Ende identisch mit Start+Ende jeweils einer Periode ist.
     */
    @Test
    public void testSegregateExclusions2() {

        System.out.println(NEWLINE + "testSegregateExclusions2");

        Period pTimespan = new Period(1000, 11000);
        Period[] pPeriods = {new Period(1000, 5000), new Period(5000, 11000)};
        Period[] pExclusions = {new Period(2000, 4000), new Period(8000, 10000)};
        TimeRelatedValue[] pTotalValues = {new TimeRelatedValue(222, 1000, 5000, 11),
            new TimeRelatedValue(444, 5000, 11000, 33)};

        assertEquals("Period[0]-CalcDuration-before", 4000, pPeriods[0].getCalcDuration());
        assertEquals("Period[1]-CalcDuration-before", 6000, pPeriods[1].getCalcDuration());
        assertEquals("Timespan-CalcDuration-before", 10000, pTimespan.getCalcDuration());

        SpreadCalculatorSaprimaFinal.segregateExclusions(pPeriods, pTimespan, pTotalValues, pExclusions);

        assertEquals("Timespan-CalcStart", 1000, pTimespan.getCalcStartDate());
        assertEquals("Timespan-CalcFinish", 7000, pTimespan.getCalcFinishDate());
        assertEquals("Timespan-CalcDuration", 6000, pTimespan.getCalcDuration());

        assertEquals("Exclusion[0]-CalcStart", 2000, pExclusions[0].getCalcStartDate());
        assertEquals("Exclusion[0]-CalcFinish", 4000, pExclusions[0].getCalcFinishDate());
        assertEquals("Exclusion[0]-CalcDuration", 2000, pExclusions[0].getCalcDuration());

        assertEquals("Period[0]-CalcStart", 1000, pPeriods[0].getCalcStartDate());
        assertEquals("Period[0]-CalcFinish", 3000, pPeriods[0].getCalcFinishDate());
        assertEquals("Period[0]-CalcDuration", 2000, pPeriods[0].getCalcDuration());

        assertEquals("TotalValue[0]-CalcStart", 1000, pTotalValues[0].getCalcStartDate());
        assertEquals("TotalValue[0]-CalcFinish", 3000, pTotalValues[0].getCalcFinishDate());
        assertEquals("TotalValue[0]-CalcDuration", 2000, pTotalValues[0].getCalcDuration());

        assertEquals("Exclusion[1]-CalcStart", 6000, pExclusions[1].getCalcStartDate());
        assertEquals("Exclusion[1]-CalcFinish", 8000, pExclusions[1].getCalcFinishDate());
        assertEquals("Exclusion[1]-CalcDuration", 2000, pExclusions[1].getCalcDuration());

        assertEquals("Period[1]-CalcStart", 3000, pPeriods[1].getCalcStartDate());
        assertEquals("Period[1]-CalcFinish", 7000, pPeriods[1].getCalcFinishDate());
        assertEquals("Period[1]-CalcDuration", 4000, pPeriods[1].getCalcDuration());

        assertEquals("TotalValue[1]-CalcStart", 3000, pTotalValues[1].getCalcStartDate());
        assertEquals("TotalValue[1]-CalcFinish", 7000, pTotalValues[1].getCalcFinishDate());
        assertEquals("TotalValue[1]-CalcDuration", 4000, pTotalValues[1].getCalcDuration());

        // Original values must not be changed
        assertEquals("Timespan-Start", 1000, pTimespan.getStartDate());
        assertEquals("Timespan-Finish", 11000, pTimespan.getFinishDate());

        assertEquals("Period[0]-Start", 1000, pPeriods[0].getStartDate());
        assertEquals("Period[0]-Finish", 5000, pPeriods[0].getFinishDate());

        assertEquals("TotalValue[0]-Start", 1000, pTotalValues[0].getStartDate());
        assertEquals("TotalValue[0]-Finish", 5000, pTotalValues[0].getFinishDate());
        assertEquals("TotalValue[0]-Value", 0, Double.compare(222, pTotalValues[0].getValue()));
        assertEquals("TotalValue[0]-Index", 11, pTotalValues[0].getIndex());

        assertEquals("Period[1]-Start", 5000, pPeriods[1].getStartDate());
        assertEquals("Period[1]-Finish", 11000, pPeriods[1].getFinishDate());

        assertEquals("TotalValue[1]-Start", 5000, pTotalValues[1].getStartDate());
        assertEquals("TotalValue[1]-Finish", 11000, pTotalValues[1].getFinishDate());
        assertEquals("TotalValue[1]-Value", 0, Double.compare(444, pTotalValues[1].getValue()));
        assertEquals("TotalValue[1]-Index", 33, pTotalValues[1].getIndex());

    }

    /**
     * Sums up the values with a certain index, of all periods.<br>
     * IMPORTANT: <br>Note that - unlike the usual Java logic - NaN values are
     * treated like 0.0 if added to a number != NaN, i.e.,
     * NaN + x (with x != NaN) equals x, not NaN <br>
     * However, additions of exclusively NaN values result in a "sum" with
     * value NaN
     * @param pPeriods the periods containing the values to sum up
     * @param pIndex the index of the values to sum up
     * @return the sum of all values with index pIndex
     */
    public static double sum(Period[] pPeriods, int pIndex) {
        // This initialisation enables a distinction between sums of
        // exclusively 0.0 values and "sums" of exclusively NaN values
        double mysum = Double.NaN;
        for (int i = 0; i < pPeriods.length; i++) {
            if (null != pPeriods[i].getPeriodValues()
                    && !Double.isNaN(pPeriods[i].getPeriodValues()[pIndex])) {
                if (Double.isNaN(mysum)) {
                    mysum = 0.0;
                }
                mysum += pPeriods[i].getPeriodValues()[pIndex];
            }
        }
        return mysum;
    }

    /**
     * Sums up the values with index 0, of all periods.<br>
     * IMPORTANT: <br>Note that - unlike the usual Java logic - NaN values are
     * treated like 0.0 if added to a number != NaN, i.e.,
     * NaN + x (with x != NaN) equals x, not NaN <br>
     * However, additions of exclusively NaN values result in a "sum" with
     * value NaN
     * @param pPeriods the periods containing the values to sum up
     * @return the sum of all values with index 0
     */
    public static double sum(Period[] periods) {
        return sum(periods, 0);
    }

    /**
     * Universal test method. For input values see test method data pool.
     * @param pName
     * @param pInputValue
     * @param pInputStart
     * @param pInputFinish
     * @param pInputPeriods
     * @param pInputCurve
     * @param pInputExclusionPeriods
     * @param pExpectedPeriodValue
     */
    private void universalTestCase(String pName, double[] pInputValue,
            long pInputStart, long pInputFinish,
            Period[] pInputPeriods, double[] pInputCurve, Period[] pInputExclusionPeriods,
            double[][] pExpectedPeriodValue) {

        System.out.println(NEWLINE + "universalTestCase");

        SpreadCalculatorSaprimaFinal.computeSpread(pInputValue, pInputStart, pInputFinish, pInputPeriods, pInputCurve, pInputExclusionPeriods);

        for (int a = 0; a < pInputValue.length; a++) {

            for (int i = 0; i < pInputPeriods.length; i++) {
                assertEquals("Period " + i, pExpectedPeriodValue[i][0], pInputPeriods[i].getPeriodValue(), DELTA);
            }
            assertEquals("Sum", pInputValue[a], sum(pInputPeriods, a), DELTA);
        }
    }

    /**
     * Test of computeSpread method, of class SpreadCalculatorSaprimaFinal.
     * Simple test of case
     * - 1 period
     * - 1 value
     * - time span (start - finish) equals period
     */
    @Test
    public void testComputeSpread_Testcase1() {
        System.out.println(NEWLINE + "testComputeSpread_Testcase1");
        double[] pValue = {1000D};
        Period pTimespan = new Period(0, 10000);
        Period[] pPeriods = {new Period(0, 10000)};
        double[] pCurve = {1};
        Period[] pExclusionPeriods = null;
        SpreadCalculatorSaprimaFinal.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
        assertEquals("Period 1", pValue[0], pPeriods[0].getPeriodValue(), DELTA);
        assertEquals("Sum", pValue[0], sum(pPeriods), DELTA);
    }

    /**
     * Test of computeSpread method, of class SpreadCalculatorSaprimaFinal.
     */
    @Test
    public void testComputeSpread_Testcase2() {
        System.out.println(NEWLINE + "testComputeSpread_Testcase2");
        double[] pValue = {5000D};
        Period pTimespan = new Period(0, 10000);
        Period[] pPeriods = {new Period(0, 10000)};
        double[] pCurve = {1};
        Period[] pExclusionPeriods = null;
        SpreadCalculatorSaprimaFinal.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
        assertEquals("Period 1", 5000D, pPeriods[0].getPeriodValue(), DELTA);
        assertEquals("Sum", pValue[0], sum(pPeriods), DELTA);
    }

    /**
     * Test of computeSpread method, of class SpreadCalculatorSaprimaFinal.
     */
    @Test
    public void testComputeSpread_Testcase3() {
        System.out.println(NEWLINE + "testComputeSpread_Testcase3");
        double[] pValue = {10000D};
        Period pTimespan = new Period(0, 100000);
        Period[] pPeriods = {new Period(0, 50000), new Period(50000, 100000)};
        double[] pCurve = {5, 5, 5, 5, 5};

        Period[] pExclusionPeriods = null;

        SpreadCalculatorSaprimaFinal.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
        System.out.println("\tResult1: " + pPeriods[0].getPeriodValue());
        System.out.println("\tResult2: " + pPeriods[1].getPeriodValue());

        assertEquals("Period 1", 5000D, pPeriods[0].getPeriodValue(), DELTA);
        assertEquals("Period 2", 5000D, pPeriods[1].getPeriodValue(), DELTA);
        assertEquals("Sum", pValue[0], sum(pPeriods), DELTA);
    }

    /**
     * Test of computeSpread method, of class SpreadCalculatorSaprimaFinal.
     */
    @Test
    public void testComputeSpread_Testcase4() {
        System.out.println(NEWLINE + "testComputeSpread_Testcase4");
        double[] pValue = {1000D};
        Period pTimespan = new Period(0, 15000);
        Period[] pPeriods = {new Period(0, 5000), new Period(5000, 10000), new Period(10000, 15000)};
        double[] pCurve = {1};
        Period[] pExclusionPeriods = null;

        SpreadCalculatorSaprimaFinal.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
        assertEquals("Period 1", 333.333333D, pPeriods[0].getPeriodValue(), DELTA);
        assertEquals("Period 2", 333.333333D, pPeriods[1].getPeriodValue(), DELTA);
        assertEquals("Period 3", 333.333333D, pPeriods[2].getPeriodValue(), DELTA);
        assertEquals("Sum", pValue[0], sum(pPeriods), DELTA);
    }

    /**
     * Test of computeSpread method, of class SpreadCalculatorSaprimaFinal.
     */
    @Test
    public void testComputeSpread_Testcase5() {
        System.out.println(NEWLINE + "testComputeSpread_Testcase5");
        double[] pValue = {1000D};
        Period pTimespan = new Period(0, 20000);
        Period[] pPeriods = {new Period(0, 5000), new Period(5000, 10000), new Period(10000, 15000), new Period(15000, 20000)};
        double[] pCurve = {1};
        Period[] pExclusionPeriods = null;

        SpreadCalculatorSaprimaFinal.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
        assertEquals("Period 1", 250D, pPeriods[0].getPeriodValue(), DELTA);
        assertEquals("Period 2", 250D, pPeriods[1].getPeriodValue(), DELTA);
        assertEquals("Period 3", 250D, pPeriods[2].getPeriodValue(), DELTA);
        assertEquals("Period 4", 250D, pPeriods[3].getPeriodValue(), DELTA);
        assertEquals("Sum", pValue[0], sum(pPeriods), DELTA);
    }

    /**
     * Test of computeSpread method, of class SpreadCalculatorSaprimaFinal.
     */
    @Test
    public void testComputeSpread_Testcase6() {
        System.out.println(NEWLINE + "testComputeSpread_Testcase6");
        double[] pValue = {1000D};
        Period pTimespan = new Period(0, 20000);
        Period[] pPeriods = {new Period(0, 5000), new Period(5000, 10000), new Period(10000, 15000), new Period(15000, 20000)};
        double[] pCurve = {25, 75};
        Period[] pExclusionPeriods = null;

        SpreadCalculatorSaprimaFinal.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
        assertEquals("Period 1", 125D, pPeriods[0].getPeriodValue(), DELTA);
        assertEquals("Period 2", 125D, pPeriods[1].getPeriodValue(), DELTA);
        assertEquals("Period 3", 375D, pPeriods[2].getPeriodValue(), DELTA);
        assertEquals("Period 4", 375D, pPeriods[3].getPeriodValue(), DELTA);
        assertEquals("Sum", pValue[0], sum(pPeriods), DELTA);
    }

    /**
     * Test of computeSpread method, of class SpreadCalculatorSaprimaFinal.
     */
    @Test
    public void testComputeSpread_Testcase7() {
        System.out.println(NEWLINE + "testComputeSpread_Testcase7");
        double[] pValue = {1000D};
        Period pTimespan = new Period(0, 20000);
        Period[] pPeriods = {new Period(0, 10000), new Period(5000, 15000), new Period(10000, 20000), new Period(15000, 20000)};
        double[] pCurve = {25, 75};
        Period[] pExclusionPeriods = null;

        SpreadCalculatorSaprimaFinal.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
        System.out.println("Result: " + Arrays.asList(pPeriods));
        assertEquals("Period 1", 250D, pPeriods[0].getPeriodValue(), DELTA);  // ganzes erstes intervall
        assertEquals("Period 2", 500D, pPeriods[1].getPeriodValue(), DELTA); // 125 + 375
        assertEquals("Period 3", 750D, pPeriods[2].getPeriodValue(), DELTA); //ganzes zweites intervall
        assertEquals("Period 4", 375D, pPeriods[3].getPeriodValue(), DELTA); // hälfte vom zweiten intervall
        assertEquals("Sum", 1875D, sum(pPeriods), DELTA);   //Achtung! Ueberlappende Perioden!
    }

    /**
     * Test of computeSpread method, of class SpreadCalculatorSaprimaFinal.
     */
    @Test
    public void testComputeSpread_Testcase8() {
        System.out.println(NEWLINE + "testComputeSpread_Testcase8");
        double[] pValue = {1000D};
        Period pTimespan = new Period(0, 20000);
        Period[] pPeriods = {new Period(0, 10000), new Period(5000, 15000), new Period(10000, 20000), new Period(15000, 20000)};
        double[] pCurve = {25, 75};
        Period[] pExclusionPeriods = null;

        SpreadCalculatorSaprimaFinal.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
        System.out.println("Result: " + Arrays.asList(pPeriods));
        assertEquals("Period 1", 250D, pPeriods[0].getPeriodValue(), DELTA);  // ganzes erstes intervall
        assertEquals("Period 2", 500D, pPeriods[1].getPeriodValue(), DELTA); // 125 + 375
        assertEquals("Period 3", 750D, pPeriods[2].getPeriodValue(), DELTA); //ganzes zweites intervall
        assertEquals("Period 4", 375D, pPeriods[3].getPeriodValue(), DELTA); // hälfte vom zweiten intervall
        assertEquals("Sum", 1875D, sum(pPeriods), DELTA);   //Achtung! Ueberlappende Perioden!
    }

    /**
     * Test of computeSpread method, of class SpreadCalculatorSaprimaFinal.
     * 3 zu verteilende Gesamtwerte.
     * 4 Perioden, die sich gegenseitig überlappen
     * 1 Periode wird von 1 anderen Periode vollständig überdeckt.
     * Keine Periode ragt aus dem Timespan heraus.
     * Kurve aus 2 Werten.
     * Keine Exclusions.
     */
    @Test
    public void testComputeSpread_Testcase9() {
        System.out.println(NEWLINE + "testComputeSpread_Testcase9");
        double[] pValue = {1000D, 2000D, 0D};
        Period pTimespan = new Period(0, 20000);
        Period[] pPeriods = {new Period(0, 10000), new Period(5000, 15000), new Period(10000, 20000), new Period(15000, 20000)};
        double[] pCurve = {25, 75};
        Period[] pExclusionPeriods = null;

        SpreadCalculatorSaprimaFinal.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
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
        System.out.println(NEWLINE + "testComputeSpread_Testcase10");
        double[] pValue = {1000D, 2000D, 0D};
        Period pTimespan = new Period(0, 20000);
        Period[] pPeriods = {new Period(-1000, 10000), new Period(5000, 15000), new Period(10000, 20000), new Period(15000, 21000)};
        double[] pCurve = {25, 75};
        Period[] pExclusionPeriods = null;

        SpreadCalculatorSaprimaFinal.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
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
        System.out.println(NEWLINE + "testComputeSpread_Testcase11");
        double[] pValue = {1000D, 2000D, 0D};
        Period pTimespan = new Period(0, 20000);
        Period[] pPeriods = {new Period(0, 10000), new Period(5000, 15000), new Period(10000, 20000), new Period(15000, 20000)};
        double[] pCurve = {25, 75};
        Period[] pExclusionPeriods = {new Period(0, 10000)};

        SpreadCalculatorSaprimaFinal.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
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
        System.out.println(NEWLINE + "testComputeSpread_Testcase12");
        double[] pValue = {1000D, 2000D, 0D};
        Period pTimespan = new Period(0, 20000);
        Period[] pPeriods = {new Period(0, 10000), new Period(5000, 15000), new Period(10000, 20000), new Period(15000, 20000)};
        double[] pCurve = {25, 25, 25, 25, 75, 75, 75, 75};
        Period[] pExclusionPeriods = {new Period(0, 1000), new Period(1000, 2000),
            new Period(2000, 3000), new Period(3000, 4000), new Period(4000, 5000),
            new Period(5000, 6000), new Period(6000, 7000), new Period(7000, 8000),
            new Period(8000, 9000), new Period(9000, 10000)};

        SpreadCalculatorSaprimaFinal.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
        System.out.println("Result 0: " + Arrays.asList(pPeriods));
        assertEquals("Period 1", 0D, pPeriods[0].getPeriodValue(0), DELTA);
        assertEquals("Period 2", 250D, pPeriods[1].getPeriodValue(0), DELTA);
        assertEquals("Period 3", 1000D, pPeriods[2].getPeriodValue(0), DELTA);
        assertEquals("Period 4", 750D, pPeriods[3].getPeriodValue(0), DELTA);
        assertEquals("Sum", 2000D, sum(pPeriods, 0), DELTA);

        System.out.println("Result 1: " + Arrays.asList(pPeriods));
        assertEquals("Period 1", 0D, pPeriods[0].getPeriodValue(1), DELTA);
        assertEquals("Period 2", 500D, pPeriods[1].getPeriodValue(1), DELTA);
        assertEquals("Period 3", 2000D, pPeriods[2].getPeriodValue(1), DELTA);
        assertEquals("Period 4", 1500D, pPeriods[3].getPeriodValue(1), DELTA);
        assertEquals("Sum", 4000D, sum(pPeriods, 1), DELTA);

        System.out.println("Result 2: " + Arrays.asList(pPeriods));
        assertEquals("Period 1", 0D, pPeriods[0].getPeriodValue(2), DELTA);
        assertEquals("Period 2", 0D, pPeriods[1].getPeriodValue(2), DELTA);
        assertEquals("Period 3", 0D, pPeriods[2].getPeriodValue(2), DELTA);
        assertEquals("Period 4", 0D, pPeriods[3].getPeriodValue(2), DELTA);
        assertEquals("Sum", 0D, sum(pPeriods, 2), DELTA);
    }

    /**
     * 4 zu verteilende Gesamtwerte mit jeweils unterschiedlichen Zeiträumen
     * jeder value wird auf einen eigenen Index verteilt
     * 8 nicht überlappende Perioden, die den Gesamtzeitraum lückenlos ausfüllen
     * keine Exclusions
     */
    @Test
    public void testComputeSpread_Testcase13() {
        System.out.println(NEWLINE + "testComputeSpread_Testcase13");

        TimeRelatedValue[] pTotalValues = {
            new TimeRelatedValue(1000D, 0, 20000, 0),
            new TimeRelatedValue(2000D, 0, 5000, 1),
            new TimeRelatedValue(3000D, 5000, 15000, 2),
            new TimeRelatedValue(4000D, 15000, 20000, 3)
        };

        long pTimespanStart = 0;
        long pTimespanFinish = 20000;

        Period[] pPeriods = {
            new Period(0, 2500),
            new Period(2500, 5000),
            new Period(5000, 7500),
            new Period(7500, 10000),
            new Period(10000, 12500),
            new Period(12500, 15000),
            new Period(15000, 17500),
            new Period(17500, 20000)
        };

        double[] pCurve = {25, 75};

        Period[] pExclusionPeriods = null;

        SpreadCalculatorSaprimaFinal.computeSpread(pTotalValues, pTimespanStart, pTimespanFinish,
                pPeriods, pCurve, pExclusionPeriods);

        System.out.println("Values for index 0: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", 62.5, pPeriods[0].getPeriodValue(0), DELTA);
        assertEquals("Period 1", 62.5, pPeriods[1].getPeriodValue(0), DELTA);
        assertEquals("Period 2", 62.5, pPeriods[2].getPeriodValue(0), DELTA);
        assertEquals("Period 3", 62.5, pPeriods[3].getPeriodValue(0), DELTA);
        assertEquals("Period 4", 187.5, pPeriods[4].getPeriodValue(0), DELTA);
        assertEquals("Period 5", 187.5, pPeriods[5].getPeriodValue(0), DELTA);
        assertEquals("Period 6", 187.5, pPeriods[6].getPeriodValue(0), DELTA);
        assertEquals("Period 7", 187.5, pPeriods[7].getPeriodValue(0), DELTA);
        assertEquals("Sum 0", 1000.0, sum(pPeriods, 0), DELTA);

        System.out.println("Values for index 1: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", 1000.0, pPeriods[0].getPeriodValue(1), DELTA);
        assertEquals("Period 1", 1000.0, pPeriods[1].getPeriodValue(1), DELTA);
        assertEquals("Period 2", Double.NaN, pPeriods[2].getPeriodValue(1), DELTA);
        assertEquals("Period 3", Double.NaN, pPeriods[3].getPeriodValue(1), DELTA);
        assertEquals("Period 4", Double.NaN, pPeriods[4].getPeriodValue(1), DELTA);
        assertEquals("Period 5", Double.NaN, pPeriods[5].getPeriodValue(1), DELTA);
        assertEquals("Period 6", Double.NaN, pPeriods[6].getPeriodValue(1), DELTA);
        assertEquals("Period 7", Double.NaN, pPeriods[7].getPeriodValue(1), DELTA);
        assertEquals("Sum 1", 2000.0, sum(pPeriods, 1), DELTA);

        System.out.println("Values for index 2: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", Double.NaN, pPeriods[0].getPeriodValue(2), DELTA);
        assertEquals("Period 1", Double.NaN, pPeriods[1].getPeriodValue(2), DELTA);
        assertEquals("Period 2", 375.0, pPeriods[2].getPeriodValue(2), DELTA);
        assertEquals("Period 3", 375.0, pPeriods[3].getPeriodValue(2), DELTA);
        assertEquals("Period 4", 1125.0, pPeriods[4].getPeriodValue(2), DELTA);
        assertEquals("Period 5", 1125.0, pPeriods[5].getPeriodValue(2), DELTA);
        assertEquals("Period 6", Double.NaN, pPeriods[6].getPeriodValue(2), DELTA);
        assertEquals("Period 7", Double.NaN, pPeriods[7].getPeriodValue(2), DELTA);
        assertEquals("Sum 2", 3000.0, sum(pPeriods, 2), DELTA);

        System.out.println("Values for index 3: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", Double.NaN, pPeriods[0].getPeriodValue(3), DELTA);
        assertEquals("Period 1", Double.NaN, pPeriods[1].getPeriodValue(3), DELTA);
        assertEquals("Period 2", Double.NaN, pPeriods[2].getPeriodValue(3), DELTA);
        assertEquals("Period 3", Double.NaN, pPeriods[3].getPeriodValue(3), DELTA);
        assertEquals("Period 4", Double.NaN, pPeriods[4].getPeriodValue(3), DELTA);
        assertEquals("Period 5", Double.NaN, pPeriods[5].getPeriodValue(3), DELTA);
        assertEquals("Period 6", 2000.0, pPeriods[6].getPeriodValue(3), DELTA);
        assertEquals("Period 7", 2000.0, pPeriods[7].getPeriodValue(3), DELTA);
        assertEquals("Sum 3", 4000.0, sum(pPeriods, 3), DELTA);

    }

    /**
     * wie testComputeSpread_Testcase13, aber alle Werte werden einem
     * einzigen Index zugewiesen
     */
    @Test
    public void testComputeSpread_Testcase14() {
        System.out.println(NEWLINE + "testComputeSpread_Testcase13");

        TimeRelatedValue[] pTotalValues = {
            new TimeRelatedValue(1000D, 0, 20000, 0),
            new TimeRelatedValue(2000D, 0, 5000, 0),
            new TimeRelatedValue(3000D, 5000, 15000, 0),
            new TimeRelatedValue(4000D, 15000, 20000, 0)
        };

        long pTimespanStart = 0;
        long pTimespanFinish = 20000;

        Period[] pPeriods = {
            new Period(0, 2500),
            new Period(2500, 5000),
            new Period(5000, 7500),
            new Period(7500, 10000),
            new Period(10000, 12500),
            new Period(12500, 15000),
            new Period(15000, 17500),
            new Period(17500, 20000)
        };

        double[] pCurve = {25, 75};

        Period[] pExclusionPeriods = null;

        SpreadCalculatorSaprimaFinal.computeSpread(pTotalValues, pTimespanStart, pTimespanFinish,
                pPeriods, pCurve, pExclusionPeriods);

        System.out.println("Values for index 0: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", 1062.5, pPeriods[0].getPeriodValue(0), DELTA);
        assertEquals("Period 1", 1062.5, pPeriods[1].getPeriodValue(0), DELTA);
        assertEquals("Period 2", 437.5, pPeriods[2].getPeriodValue(0), DELTA);
        assertEquals("Period 3", 437.5, pPeriods[3].getPeriodValue(0), DELTA);
        assertEquals("Period 4", 1312.5, pPeriods[4].getPeriodValue(0), DELTA);
        assertEquals("Period 5", 1312.5, pPeriods[5].getPeriodValue(0), DELTA);
        assertEquals("Period 6", 2187.5, pPeriods[6].getPeriodValue(0), DELTA);
        assertEquals("Period 7", 2187.5, pPeriods[7].getPeriodValue(0), DELTA);
        assertEquals("Sum 0", 10000.0, sum(pPeriods, 0), DELTA);

        System.out.println("Values for index 1: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", Double.NaN, pPeriods[0].getPeriodValue(1), DELTA);
        assertEquals("Period 1", Double.NaN, pPeriods[1].getPeriodValue(1), DELTA);
        assertEquals("Period 2", Double.NaN, pPeriods[2].getPeriodValue(1), DELTA);
        assertEquals("Period 3", Double.NaN, pPeriods[3].getPeriodValue(1), DELTA);
        assertEquals("Period 4", Double.NaN, pPeriods[4].getPeriodValue(1), DELTA);
        assertEquals("Period 5", Double.NaN, pPeriods[5].getPeriodValue(1), DELTA);
        assertEquals("Period 6", Double.NaN, pPeriods[6].getPeriodValue(1), DELTA);
        assertEquals("Period 7", Double.NaN, pPeriods[7].getPeriodValue(1), DELTA);
        assertEquals("Sum 1", Double.NaN, sum(pPeriods, 1), DELTA);

        System.out.println("Values for index 2: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", Double.NaN, pPeriods[0].getPeriodValue(2), DELTA);
        assertEquals("Period 1", Double.NaN, pPeriods[1].getPeriodValue(2), DELTA);
        assertEquals("Period 2", Double.NaN, pPeriods[2].getPeriodValue(2), DELTA);
        assertEquals("Period 3", Double.NaN, pPeriods[3].getPeriodValue(2), DELTA);
        assertEquals("Period 4", Double.NaN, pPeriods[4].getPeriodValue(2), DELTA);
        assertEquals("Period 5", Double.NaN, pPeriods[5].getPeriodValue(2), DELTA);
        assertEquals("Period 6", Double.NaN, pPeriods[6].getPeriodValue(2), DELTA);
        assertEquals("Period 7", Double.NaN, pPeriods[7].getPeriodValue(2), DELTA);
        assertEquals("Sum 2", Double.NaN, sum(pPeriods, 2), DELTA);

        System.out.println("Values for index 3: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", Double.NaN, pPeriods[0].getPeriodValue(3), DELTA);
        assertEquals("Period 1", Double.NaN, pPeriods[1].getPeriodValue(3), DELTA);
        assertEquals("Period 2", Double.NaN, pPeriods[2].getPeriodValue(3), DELTA);
        assertEquals("Period 3", Double.NaN, pPeriods[3].getPeriodValue(3), DELTA);
        assertEquals("Period 4", Double.NaN, pPeriods[4].getPeriodValue(3), DELTA);
        assertEquals("Period 5", Double.NaN, pPeriods[5].getPeriodValue(3), DELTA);
        assertEquals("Period 6", Double.NaN, pPeriods[6].getPeriodValue(3), DELTA);
        assertEquals("Period 7", Double.NaN, pPeriods[7].getPeriodValue(3), DELTA);
        assertEquals("Sum 3", Double.NaN, sum(pPeriods, 3), DELTA);

    }

    /**
     * wie testComputeSpread_Testcase13, aber alle Werte werden einem
     * zwei verschiedenen Indizes zugewiesen
     */
    @Test
    public void testComputeSpread_Testcase15() {
        System.out.println(NEWLINE + "testComputeSpread_Testcase15");

        TimeRelatedValue[] pTotalValues = {
            new TimeRelatedValue(1000D, 0, 20000, 0),
            new TimeRelatedValue(2000D, 0, 5000, 1),
            new TimeRelatedValue(3000D, 5000, 15000, 1),
            new TimeRelatedValue(4000D, 15000, 20000, 0)
        };

        long pTimespanStart = 0;
        long pTimespanFinish = 20000;

        Period[] pPeriods = {
            new Period(0, 2500),
            new Period(2500, 5000),
            new Period(5000, 7500),
            new Period(7500, 10000),
            new Period(10000, 12500),
            new Period(12500, 15000),
            new Period(15000, 17500),
            new Period(17500, 20000)
        };

        double[] pCurve = {25, 75};

        Period[] pExclusionPeriods = null;

        SpreadCalculatorSaprimaFinal.computeSpread(pTotalValues, pTimespanStart, pTimespanFinish,
                pPeriods, pCurve, pExclusionPeriods);

        System.out.println("Values for index 0: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", 62.5, pPeriods[0].getPeriodValue(0), DELTA);
        assertEquals("Period 1", 62.5, pPeriods[1].getPeriodValue(0), DELTA);
        assertEquals("Period 2", 62.5, pPeriods[2].getPeriodValue(0), DELTA);
        assertEquals("Period 3", 62.5, pPeriods[3].getPeriodValue(0), DELTA);
        assertEquals("Period 4", 187.5, pPeriods[4].getPeriodValue(0), DELTA);
        assertEquals("Period 5", 187.5, pPeriods[5].getPeriodValue(0), DELTA);
        assertEquals("Period 6", 2187.5, pPeriods[6].getPeriodValue(0), DELTA);
        assertEquals("Period 7", 2187.5, pPeriods[7].getPeriodValue(0), DELTA);
        assertEquals("Sum 0", 5000.0, sum(pPeriods, 0), DELTA);

        System.out.println("Values for index 1: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", 1000.0, pPeriods[0].getPeriodValue(1), DELTA);
        assertEquals("Period 1", 1000.0, pPeriods[1].getPeriodValue(1), DELTA);
        assertEquals("Period 2", 375.0, pPeriods[2].getPeriodValue(1), DELTA);
        assertEquals("Period 3", 375.0, pPeriods[3].getPeriodValue(1), DELTA);
        assertEquals("Period 4", 1125.0, pPeriods[4].getPeriodValue(1), DELTA);
        assertEquals("Period 5", 1125.0, pPeriods[5].getPeriodValue(1), DELTA);
        assertEquals("Period 6", Double.NaN, pPeriods[6].getPeriodValue(1), DELTA);
        assertEquals("Period 7", Double.NaN, pPeriods[7].getPeriodValue(1), DELTA);
        assertEquals("Sum 1", 5000.0, sum(pPeriods, 1), DELTA);

        System.out.println("Values for index 2: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", Double.NaN, pPeriods[0].getPeriodValue(2), DELTA);
        assertEquals("Period 1", Double.NaN, pPeriods[1].getPeriodValue(2), DELTA);
        assertEquals("Period 2", Double.NaN, pPeriods[2].getPeriodValue(2), DELTA);
        assertEquals("Period 3", Double.NaN, pPeriods[3].getPeriodValue(2), DELTA);
        assertEquals("Period 4", Double.NaN, pPeriods[4].getPeriodValue(2), DELTA);
        assertEquals("Period 5", Double.NaN, pPeriods[5].getPeriodValue(2), DELTA);
        assertEquals("Period 6", Double.NaN, pPeriods[6].getPeriodValue(2), DELTA);
        assertEquals("Period 7", Double.NaN, pPeriods[7].getPeriodValue(2), DELTA);
        assertEquals("Sum 2", Double.NaN, sum(pPeriods, 2), DELTA);

        System.out.println("Values for index 3: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", Double.NaN, pPeriods[0].getPeriodValue(3), DELTA);
        assertEquals("Period 1", Double.NaN, pPeriods[1].getPeriodValue(3), DELTA);
        assertEquals("Period 2", Double.NaN, pPeriods[2].getPeriodValue(3), DELTA);
        assertEquals("Period 3", Double.NaN, pPeriods[3].getPeriodValue(3), DELTA);
        assertEquals("Period 4", Double.NaN, pPeriods[4].getPeriodValue(3), DELTA);
        assertEquals("Period 5", Double.NaN, pPeriods[5].getPeriodValue(3), DELTA);
        assertEquals("Period 6", Double.NaN, pPeriods[6].getPeriodValue(3), DELTA);
        assertEquals("Period 7", Double.NaN, pPeriods[7].getPeriodValue(3), DELTA);
        assertEquals("Sum 3", Double.NaN, sum(pPeriods, 3), DELTA);

    }

    /**
     * wie testComputeSpread_Testcase15, aber Kurve aus 4 Teilen
     */
    @Test
    public void testComputeSpread_Testcase16() {
        System.out.println(NEWLINE + "testComputeSpread_Testcase16");

        TimeRelatedValue[] pTotalValues = {
            new TimeRelatedValue(1000D, 0, 20000, 0),
            new TimeRelatedValue(2000D, 0, 5000, 1),
            new TimeRelatedValue(3000D, 5000, 15000, 1),
            new TimeRelatedValue(4000D, 15000, 20000, 0)
        };

        long pTimespanStart = 0;
        long pTimespanFinish = 20000;

        Period[] pPeriods = {
            new Period(0, 2500),
            new Period(2500, 5000),
            new Period(5000, 7500),
            new Period(7500, 10000),
            new Period(10000, 12500),
            new Period(12500, 15000),
            new Period(15000, 17500),
            new Period(17500, 20000)
        };

        double[] pCurve = {0, 25, 75, 0};

        Period[] pExclusionPeriods = null;

        SpreadCalculatorSaprimaFinal.computeSpread(pTotalValues, pTimespanStart, pTimespanFinish,
                pPeriods, pCurve, pExclusionPeriods);

        System.out.println("Values for index 0: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", 0.0, pPeriods[0].getPeriodValue(0), DELTA);
        assertEquals("Period 1", 0.0, pPeriods[1].getPeriodValue(0), DELTA);
        assertEquals("Period 2", 125.0, pPeriods[2].getPeriodValue(0), DELTA);
        assertEquals("Period 3", 125.0, pPeriods[3].getPeriodValue(0), DELTA);
        assertEquals("Period 4", 375.0, pPeriods[4].getPeriodValue(0), DELTA);
        assertEquals("Period 5", 375.0, pPeriods[5].getPeriodValue(0), DELTA);
        assertEquals("Period 6", 0.0, pPeriods[6].getPeriodValue(0), DELTA);
        assertEquals("Period 7", 0.0, pPeriods[7].getPeriodValue(0), DELTA);
        assertEquals("Sum 0", 1000.0, sum(pPeriods, 0), DELTA);

        System.out.println("Values for index 1: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", 0.0, pPeriods[0].getPeriodValue(1), DELTA);
        assertEquals("Period 1", 0.0, pPeriods[1].getPeriodValue(1), DELTA);
        assertEquals("Period 2", 375.0, pPeriods[2].getPeriodValue(1), DELTA);
        assertEquals("Period 3", 375.0, pPeriods[3].getPeriodValue(1), DELTA);
        assertEquals("Period 4", 1125.0, pPeriods[4].getPeriodValue(1), DELTA);
        assertEquals("Period 5", 1125.0, pPeriods[5].getPeriodValue(1), DELTA);
        assertEquals("Period 6", Double.NaN, pPeriods[6].getPeriodValue(1), DELTA);
        assertEquals("Period 7", Double.NaN, pPeriods[7].getPeriodValue(1), DELTA);
        assertEquals("Sum 1", 3000.0, sum(pPeriods, 1), DELTA);

        System.out.println("Values for index 2: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", Double.NaN, pPeriods[0].getPeriodValue(2), DELTA);
        assertEquals("Period 1", Double.NaN, pPeriods[1].getPeriodValue(2), DELTA);
        assertEquals("Period 2", Double.NaN, pPeriods[2].getPeriodValue(2), DELTA);
        assertEquals("Period 3", Double.NaN, pPeriods[3].getPeriodValue(2), DELTA);
        assertEquals("Period 4", Double.NaN, pPeriods[4].getPeriodValue(2), DELTA);
        assertEquals("Period 5", Double.NaN, pPeriods[5].getPeriodValue(2), DELTA);
        assertEquals("Period 6", Double.NaN, pPeriods[6].getPeriodValue(2), DELTA);
        assertEquals("Period 7", Double.NaN, pPeriods[7].getPeriodValue(2), DELTA);
        assertEquals("Sum 2", Double.NaN, sum(pPeriods, 2), DELTA);

        System.out.println("Values for index 3: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", Double.NaN, pPeriods[0].getPeriodValue(3), DELTA);
        assertEquals("Period 1", Double.NaN, pPeriods[1].getPeriodValue(3), DELTA);
        assertEquals("Period 2", Double.NaN, pPeriods[2].getPeriodValue(3), DELTA);
        assertEquals("Period 3", Double.NaN, pPeriods[3].getPeriodValue(3), DELTA);
        assertEquals("Period 4", Double.NaN, pPeriods[4].getPeriodValue(3), DELTA);
        assertEquals("Period 5", Double.NaN, pPeriods[5].getPeriodValue(3), DELTA);
        assertEquals("Period 6", Double.NaN, pPeriods[6].getPeriodValue(3), DELTA);
        assertEquals("Period 7", Double.NaN, pPeriods[7].getPeriodValue(3), DELTA);
        assertEquals("Sum 3", Double.NaN, sum(pPeriods, 3), DELTA);

    }

    /**
     * wie testComputeSpread_Testcase16, aber mit 1 Exclusion
     */
    @Test
    public void testComputeSpread_Testcase17() {
        System.out.println(NEWLINE + "testComputeSpread_Testcase17");

        TimeRelatedValue[] pTotalValues = {
            new TimeRelatedValue(1000D, 0, 20000, 0),
            new TimeRelatedValue(2000D, 0, 5000, 1),
            new TimeRelatedValue(3000D, 5000, 15000, 1),
            new TimeRelatedValue(4000D, 15000, 20000, 0)
        };

        long pTimespanStart = 0;
        long pTimespanFinish = 20000;

        Period[] pPeriods = {
            new Period(0, 2500),
            new Period(2500, 5000),
            new Period(5000, 7500),
            new Period(7500, 10000),
            new Period(10000, 12500),
            new Period(12500, 15000),
            new Period(15000, 17500),
            new Period(17500, 20000)
        };

        double[] pCurve = {0, 25, 75, 0};

        Period[] pExclusionPeriods = {
            new Period(5000, 10000)
        };

        SpreadCalculatorSaprimaFinal.computeSpread(pTotalValues, pTimespanStart, pTimespanFinish,
                pPeriods, pCurve, pExclusionPeriods);

        System.out.println("Values for index 0: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", 0.0, pPeriods[0].getPeriodValue(0), DELTA);
        assertEquals("Period 1", 83.333333, pPeriods[1].getPeriodValue(0), DELTA);
        assertEquals("Period 2", 0.0, pPeriods[2].getPeriodValue(0), DELTA);
        assertEquals("Period 3", 0.0, pPeriods[3].getPeriodValue(0), DELTA);
        assertEquals("Period 4", 166.666667, pPeriods[4].getPeriodValue(0), DELTA);
        assertEquals("Period 5", 500.0, pPeriods[5].getPeriodValue(0), DELTA);
        assertEquals("Period 6", 4250.0, pPeriods[6].getPeriodValue(0), DELTA); // 350.0 + 4000.0
        assertEquals("Period 7", 0.0, pPeriods[7].getPeriodValue(0), DELTA);
        assertEquals("Sum 0", 5000.0, sum(pPeriods, 0), DELTA);

        System.out.println("Values for index 1: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", 0.0, pPeriods[0].getPeriodValue(1), DELTA);
        assertEquals("Period 1", 2000.0, pPeriods[1].getPeriodValue(1), DELTA);
        assertEquals("Period 2", 0.0, pPeriods[2].getPeriodValue(1), DELTA);
        assertEquals("Period 3", 0.0, pPeriods[3].getPeriodValue(1), DELTA);
        assertEquals("Period 4", 750.0, pPeriods[4].getPeriodValue(1), DELTA);
        assertEquals("Period 5", 2250.0, pPeriods[5].getPeriodValue(1), DELTA);
        assertEquals("Period 6", Double.NaN, pPeriods[6].getPeriodValue(1), DELTA);
        assertEquals("Period 7", Double.NaN, pPeriods[7].getPeriodValue(1), DELTA);
        assertEquals("Sum 1", 5000.0, sum(pPeriods, 1), DELTA);

        System.out.println("Values for index 2: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", Double.NaN, pPeriods[0].getPeriodValue(2), DELTA);
        assertEquals("Period 1", Double.NaN, pPeriods[1].getPeriodValue(2), DELTA);
        assertEquals("Period 2", Double.NaN, pPeriods[2].getPeriodValue(2), DELTA);
        assertEquals("Period 3", Double.NaN, pPeriods[3].getPeriodValue(2), DELTA);
        assertEquals("Period 4", Double.NaN, pPeriods[4].getPeriodValue(2), DELTA);
        assertEquals("Period 5", Double.NaN, pPeriods[5].getPeriodValue(2), DELTA);
        assertEquals("Period 6", Double.NaN, pPeriods[6].getPeriodValue(2), DELTA);
        assertEquals("Period 7", Double.NaN, pPeriods[7].getPeriodValue(2), DELTA);
        assertEquals("Sum 2", Double.NaN, sum(pPeriods, 2), DELTA);

        System.out.println("Values for index 3: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", Double.NaN, pPeriods[0].getPeriodValue(3), DELTA);
        assertEquals("Period 1", Double.NaN, pPeriods[1].getPeriodValue(3), DELTA);
        assertEquals("Period 2", Double.NaN, pPeriods[2].getPeriodValue(3), DELTA);
        assertEquals("Period 3", Double.NaN, pPeriods[3].getPeriodValue(3), DELTA);
        assertEquals("Period 4", Double.NaN, pPeriods[4].getPeriodValue(3), DELTA);
        assertEquals("Period 5", Double.NaN, pPeriods[5].getPeriodValue(3), DELTA);
        assertEquals("Period 6", Double.NaN, pPeriods[6].getPeriodValue(3), DELTA);
        assertEquals("Period 7", Double.NaN, pPeriods[7].getPeriodValue(3), DELTA);
        assertEquals("Sum 3", Double.NaN, sum(pPeriods, 3), DELTA);

    }

    /**
     * wie testComputeSpread_Testcase17, aber mit 2 zusätzlichen Perioden,
     * die gemeinsam alle anderen Perioden überlappen sowie
     * mit zusätzlichen Asserts für die singleValue-Arrays
     * <br>Ohne Systemausgaben, da auch für Performancetest verwendet, s.u.
     */
    @Test
    public void testComputeSpread_Testcase18() {
//        System.out.println(NEWLINE + "testComputeSpread_Testcase18");

        TimeRelatedValue[] pTotalValues = {
            new TimeRelatedValue(1000D, 0, 20000, 0),
            new TimeRelatedValue(2000D, 0, 5000, 1),
            new TimeRelatedValue(3000D, 5000, 15000, 1),
            new TimeRelatedValue(4000D, 15000, 20000, 0)
        };

        long pTimespanStart = 0;
        long pTimespanFinish = 20000;

        Period[] pPeriods = {
            new Period(0, 2500),
            new Period(2500, 5000),
            new Period(5000, 7500),
            new Period(7500, 10000),
            new Period(10000, 12500),
            new Period(12500, 15000),
            new Period(15000, 17500),
            new Period(17500, 20000),
            new Period(0, 10000), // überlappende Periode
            new Period(10000, 20000) // überlappende Periode
        };

        double[] pCurve = {0, 25, 75, 0};

        Period[] pExclusionPeriods = {
            new Period(5000, 10000)
        };

        SpreadCalculatorSaprimaFinal.computeSpread(pTotalValues, pTimespanStart, pTimespanFinish,
                pPeriods, pCurve, pExclusionPeriods);

//        System.out.println("Values for index 0: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", 0.0, pPeriods[0].getPeriodValue(0), DELTA);
        assertEquals("Period 1", 83.333333, pPeriods[1].getPeriodValue(0), DELTA);
        assertEquals("Period 2", 0.0, pPeriods[2].getPeriodValue(0), DELTA);
        assertEquals("Period 3", 0.0, pPeriods[3].getPeriodValue(0), DELTA);
        assertEquals("Period 4", 166.666667, pPeriods[4].getPeriodValue(0), DELTA);
        assertEquals("Period 5", 500.0, pPeriods[5].getPeriodValue(0), DELTA);
        assertEquals("Period 6", 4250.0, pPeriods[6].getPeriodValue(0), DELTA); // 350.0 + 4000.0
        assertEquals("Period 7", 0.0, pPeriods[7].getPeriodValue(0), DELTA);
        assertEquals("Period 8", 83.333333, pPeriods[8].getPeriodValue(0), DELTA);
        assertEquals("Period 9", 4916.666667, pPeriods[9].getPeriodValue(0), DELTA); // 916.666667 + 4000.0
        assertEquals("Sum 0", 10000.0, sum(pPeriods, 0), DELTA);

//        System.out.println("Values for index 1: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", 0.0, pPeriods[0].getPeriodValue(1), DELTA);
        assertEquals("Period 1", 2000.0, pPeriods[1].getPeriodValue(1), DELTA);
        assertEquals("Period 2", 0.0, pPeriods[2].getPeriodValue(1), DELTA);
        assertEquals("Period 3", 0.0, pPeriods[3].getPeriodValue(1), DELTA);
        assertEquals("Period 4", 750.0, pPeriods[4].getPeriodValue(1), DELTA);
        assertEquals("Period 5", 2250.0, pPeriods[5].getPeriodValue(1), DELTA);
        assertEquals("Period 6", Double.NaN, pPeriods[6].getPeriodValue(1), DELTA);
        assertEquals("Period 7", Double.NaN, pPeriods[7].getPeriodValue(1), DELTA);
        assertEquals("Period 8", 2000.0, pPeriods[8].getPeriodValue(1), DELTA);
        assertEquals("Period 9", 3000.0, pPeriods[9].getPeriodValue(1), DELTA);
        assertEquals("Sum 1", 10000.0, sum(pPeriods, 1), DELTA);

//        System.out.println("Values for index 2: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", Double.NaN, pPeriods[0].getPeriodValue(2), DELTA);
        assertEquals("Period 1", Double.NaN, pPeriods[1].getPeriodValue(2), DELTA);
        assertEquals("Period 2", Double.NaN, pPeriods[2].getPeriodValue(2), DELTA);
        assertEquals("Period 3", Double.NaN, pPeriods[3].getPeriodValue(2), DELTA);
        assertEquals("Period 4", Double.NaN, pPeriods[4].getPeriodValue(2), DELTA);
        assertEquals("Period 5", Double.NaN, pPeriods[5].getPeriodValue(2), DELTA);
        assertEquals("Period 6", Double.NaN, pPeriods[6].getPeriodValue(2), DELTA);
        assertEquals("Period 7", Double.NaN, pPeriods[7].getPeriodValue(2), DELTA);
        assertEquals("Period 8", Double.NaN, pPeriods[8].getPeriodValue(2), DELTA);
        assertEquals("Period 9", Double.NaN, pPeriods[9].getPeriodValue(2), DELTA);
        assertEquals("Sum 2", Double.NaN, sum(pPeriods, 2), DELTA);

//        System.out.println("Values for index 3: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", Double.NaN, pPeriods[0].getPeriodValue(3), DELTA);
        assertEquals("Period 1", Double.NaN, pPeriods[1].getPeriodValue(3), DELTA);
        assertEquals("Period 2", Double.NaN, pPeriods[2].getPeriodValue(3), DELTA);
        assertEquals("Period 3", Double.NaN, pPeriods[3].getPeriodValue(3), DELTA);
        assertEquals("Period 4", Double.NaN, pPeriods[4].getPeriodValue(3), DELTA);
        assertEquals("Period 5", Double.NaN, pPeriods[5].getPeriodValue(3), DELTA);
        assertEquals("Period 6", Double.NaN, pPeriods[6].getPeriodValue(3), DELTA);
        assertEquals("Period 7", Double.NaN, pPeriods[7].getPeriodValue(3), DELTA);
        assertEquals("Period 8", Double.NaN, pPeriods[8].getPeriodValue(3), DELTA);
        assertEquals("Period 9", Double.NaN, pPeriods[9].getPeriodValue(3), DELTA);
        assertEquals("Sum 3", Double.NaN, sum(pPeriods, 3), DELTA);

//        System.out.println("Single values for index 0: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", 0.0, pPeriods[0].getPeriodSingleValue(0), DELTA);
        assertEquals("Period 1", 83.333333, pPeriods[1].getPeriodSingleValue(0), DELTA);
        assertEquals("Period 2", 0.0, pPeriods[2].getPeriodSingleValue(0), DELTA);
        assertEquals("Period 3", 0.0, pPeriods[3].getPeriodSingleValue(0), DELTA);
        assertEquals("Period 4", 166.666667, pPeriods[4].getPeriodSingleValue(0), DELTA);
        assertEquals("Period 5", 500.0, pPeriods[5].getPeriodSingleValue(0), DELTA);
        assertEquals("Period 6", 4250.0, pPeriods[6].getPeriodSingleValue(0), DELTA); // 350.0 + 4000.0
        assertEquals("Period 7", 0.0, pPeriods[7].getPeriodSingleValue(0), DELTA);
        assertEquals("Period 8", 83.333333, pPeriods[8].getPeriodSingleValue(0), DELTA);
        assertEquals("Period 9", 4916.666667, pPeriods[9].getPeriodSingleValue(0), DELTA); // 916.666667 + 4000.0
        assertEquals("Sum 0 single values", 10000.0, sum(pPeriods, 0), DELTA);

//        System.out.println("Single values for index 1: " + Arrays.asList(pPeriods));
        assertEquals("Period 0 single value", 0.0, pPeriods[0].getPeriodSingleValue(1), DELTA);
        assertEquals("Period 1 single value", 2000.0, pPeriods[1].getPeriodSingleValue(1), DELTA);
        assertEquals("Period 2 single value", 0.0, pPeriods[2].getPeriodSingleValue(1), DELTA);
        assertEquals("Period 3 single value", 0.0, pPeriods[3].getPeriodSingleValue(1), DELTA);
        assertEquals("Period 4 single value", 750.0, pPeriods[4].getPeriodSingleValue(1), DELTA);
        assertEquals("Period 5 single value", 2250.0, pPeriods[5].getPeriodSingleValue(1), DELTA);
        assertEquals("Period 6 single value", Double.NaN, pPeriods[6].getPeriodSingleValue(1), DELTA);
        assertEquals("Period 7 single value", Double.NaN, pPeriods[7].getPeriodSingleValue(1), DELTA);
        assertEquals("Period 8 single value", 2000.0, pPeriods[8].getPeriodSingleValue(1), DELTA);
        assertEquals("Period 9 single value", 3000.0, pPeriods[9].getPeriodSingleValue(1), DELTA);
        assertEquals("Sum 1 single values", 10000.0, sum(pPeriods, 1), DELTA);

//        System.out.println("Single values for index 2: " + Arrays.asList(pPeriods));
        assertEquals("Period 0 single value", Double.NaN, pPeriods[0].getPeriodSingleValue(2), DELTA);
        assertEquals("Period 1 single value", Double.NaN, pPeriods[1].getPeriodSingleValue(2), DELTA);
        assertEquals("Period 2 single value", Double.NaN, pPeriods[2].getPeriodSingleValue(2), DELTA);
        assertEquals("Period 3 single value", Double.NaN, pPeriods[3].getPeriodSingleValue(2), DELTA);
        assertEquals("Period 4 single value", Double.NaN, pPeriods[4].getPeriodSingleValue(2), DELTA);
        assertEquals("Period 5 single value", Double.NaN, pPeriods[5].getPeriodSingleValue(2), DELTA);
        assertEquals("Period 6 single value", Double.NaN, pPeriods[6].getPeriodSingleValue(2), DELTA);
        assertEquals("Period 7 single value", Double.NaN, pPeriods[7].getPeriodSingleValue(2), DELTA);
        assertEquals("Period 8 single value", Double.NaN, pPeriods[8].getPeriodSingleValue(2), DELTA);
        assertEquals("Period 9 single value", Double.NaN, pPeriods[9].getPeriodSingleValue(2), DELTA);
        assertEquals("Sum 2 single values", Double.NaN, sum(pPeriods, 2), DELTA);

//        System.out.println("Single values for index 3: " + Arrays.asList(pPeriods));
        assertEquals("Period 0 single value", Double.NaN, pPeriods[0].getPeriodSingleValue(3), DELTA);
        assertEquals("Period 1 single value", Double.NaN, pPeriods[1].getPeriodSingleValue(3), DELTA);
        assertEquals("Period 2 single value", Double.NaN, pPeriods[2].getPeriodSingleValue(3), DELTA);
        assertEquals("Period 3 single value", Double.NaN, pPeriods[3].getPeriodSingleValue(3), DELTA);
        assertEquals("Period 4 single value", Double.NaN, pPeriods[4].getPeriodSingleValue(3), DELTA);
        assertEquals("Period 5 single value", Double.NaN, pPeriods[5].getPeriodSingleValue(3), DELTA);
        assertEquals("Period 6 single value", Double.NaN, pPeriods[6].getPeriodSingleValue(3), DELTA);
        assertEquals("Period 7 single value", Double.NaN, pPeriods[7].getPeriodSingleValue(3), DELTA);
        assertEquals("Period 8 single value", Double.NaN, pPeriods[8].getPeriodSingleValue(3), DELTA);
        assertEquals("Period 9 single value", Double.NaN, pPeriods[9].getPeriodSingleValue(3), DELTA);
        assertEquals("Sum 3 single values", Double.NaN, sum(pPeriods, 3), DELTA);
    }

    /**
     * Test von fehlerhaften bzw. unsinningen Eingaben: <br>
     * - Alle Perioden außerhalb der Timespan.<br>
     * - 1 TimeRelateValue mit Länge 0<br>
     * - Kurve mit Wert 0
     */
    @Test
    public void testComputeSpread_Testcase19() {
        System.out.println(NEWLINE + "testComputeSpread_Testcase19");

        TimeRelatedValue[] pTotalValues = {
            new TimeRelatedValue(1000D, 10000, 10000, 0),
            new TimeRelatedValue(2000D, 13000, 14000, 1),
            new TimeRelatedValue(3000D, 14000, 20000, 1),
            new TimeRelatedValue(4000D, 20000, 20000, 0)
        };

        long pTimespanStart = 10000;
        long pTimespanFinish = 20000;

        Period[] pPeriods = {
            new Period(0, 2500),
            new Period(2500, 5000),
            new Period(5000, 7500),
            new Period(7500, 10000),
            new Period(20000, 22500),
            new Period(22500, 25000),
            new Period(25000, 27500),
            new Period(27500, 30000)
        };

        double[] pCurve = {0, 0, 0, 0};

        Period[] pExclusionPeriods = {
            new Period(5000, 10000)
        };

        SpreadCalculatorSaprimaFinal.computeSpread(pTotalValues, pTimespanStart, pTimespanFinish,
                pPeriods, pCurve, pExclusionPeriods);

        System.out.println("Values for index 0: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", Double.NaN, pPeriods[0].getPeriodValue(0), DELTA);
        assertEquals("Period 1", Double.NaN, pPeriods[1].getPeriodValue(0), DELTA);
        assertEquals("Period 2", Double.NaN, pPeriods[2].getPeriodValue(0), DELTA);
        assertEquals("Period 3", Double.NaN, pPeriods[3].getPeriodValue(0), DELTA);
        assertEquals("Period 4", Double.NaN, pPeriods[4].getPeriodValue(0), DELTA);
        assertEquals("Period 5", Double.NaN, pPeriods[5].getPeriodValue(0), DELTA);
        assertEquals("Period 6", Double.NaN, pPeriods[6].getPeriodValue(0), DELTA); // 350.0 + 4000.0
        assertEquals("Period 7", Double.NaN, pPeriods[7].getPeriodValue(0), DELTA);
        assertEquals("Sum 0", Double.NaN, sum(pPeriods, 0), DELTA);

        System.out.println("Values for index 1: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", Double.NaN, pPeriods[0].getPeriodValue(1), DELTA);
        assertEquals("Period 1", Double.NaN, pPeriods[1].getPeriodValue(1), DELTA);
        assertEquals("Period 2", Double.NaN, pPeriods[2].getPeriodValue(1), DELTA);
        assertEquals("Period 3", Double.NaN, pPeriods[3].getPeriodValue(1), DELTA);
        assertEquals("Period 4", Double.NaN, pPeriods[4].getPeriodValue(1), DELTA);
        assertEquals("Period 5", Double.NaN, pPeriods[5].getPeriodValue(1), DELTA);
        assertEquals("Period 6", Double.NaN, pPeriods[6].getPeriodValue(1), DELTA);
        assertEquals("Period 7", Double.NaN, pPeriods[7].getPeriodValue(1), DELTA);
        assertEquals("Sum 1", Double.NaN, sum(pPeriods, 1), DELTA);

        System.out.println("Values for index 2: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", Double.NaN, pPeriods[0].getPeriodValue(2), DELTA);
        assertEquals("Period 1", Double.NaN, pPeriods[1].getPeriodValue(2), DELTA);
        assertEquals("Period 2", Double.NaN, pPeriods[2].getPeriodValue(2), DELTA);
        assertEquals("Period 3", Double.NaN, pPeriods[3].getPeriodValue(2), DELTA);
        assertEquals("Period 4", Double.NaN, pPeriods[4].getPeriodValue(2), DELTA);
        assertEquals("Period 5", Double.NaN, pPeriods[5].getPeriodValue(2), DELTA);
        assertEquals("Period 6", Double.NaN, pPeriods[6].getPeriodValue(2), DELTA);
        assertEquals("Period 7", Double.NaN, pPeriods[7].getPeriodValue(2), DELTA);
        assertEquals("Sum 2", Double.NaN, sum(pPeriods, 2), DELTA);

        System.out.println("Values for index 3: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", Double.NaN, pPeriods[0].getPeriodValue(3), DELTA);
        assertEquals("Period 1", Double.NaN, pPeriods[1].getPeriodValue(3), DELTA);
        assertEquals("Period 2", Double.NaN, pPeriods[2].getPeriodValue(3), DELTA);
        assertEquals("Period 3", Double.NaN, pPeriods[3].getPeriodValue(3), DELTA);
        assertEquals("Period 4", Double.NaN, pPeriods[4].getPeriodValue(3), DELTA);
        assertEquals("Period 5", Double.NaN, pPeriods[5].getPeriodValue(3), DELTA);
        assertEquals("Period 6", Double.NaN, pPeriods[6].getPeriodValue(3), DELTA);
        assertEquals("Period 7", Double.NaN, pPeriods[7].getPeriodValue(3), DELTA);
        assertEquals("Sum 3", Double.NaN, sum(pPeriods, 3), DELTA);

        System.out.println("Single values for index 0: " + Arrays.asList(pPeriods));
        assertEquals("Period 0", Double.NaN, pPeriods[0].getPeriodSingleValue(0), DELTA);
        assertEquals("Period 1", Double.NaN, pPeriods[1].getPeriodSingleValue(0), DELTA);
        assertEquals("Period 2", Double.NaN, pPeriods[2].getPeriodSingleValue(0), DELTA);
        assertEquals("Period 3", Double.NaN, pPeriods[3].getPeriodSingleValue(0), DELTA);
        assertEquals("Period 4", Double.NaN, pPeriods[4].getPeriodSingleValue(0), DELTA);
        assertEquals("Period 5", Double.NaN, pPeriods[5].getPeriodSingleValue(0), DELTA);
        assertEquals("Period 6", Double.NaN, pPeriods[6].getPeriodSingleValue(0), DELTA); // 350.0 + 4000.0
        assertEquals("Period 7", Double.NaN, pPeriods[7].getPeriodSingleValue(0), DELTA);
        assertEquals("Sum 0 single values", Double.NaN, sum(pPeriods, 0), DELTA);

        System.out.println("Single values for index 1: " + Arrays.asList(pPeriods));
        assertEquals("Period 0 single value", Double.NaN, pPeriods[0].getPeriodSingleValue(1), DELTA);
        assertEquals("Period 1 single value", Double.NaN, pPeriods[1].getPeriodSingleValue(1), DELTA);
        assertEquals("Period 2 single value", Double.NaN, pPeriods[2].getPeriodSingleValue(1), DELTA);
        assertEquals("Period 3 single value", Double.NaN, pPeriods[3].getPeriodSingleValue(1), DELTA);
        assertEquals("Period 4 single value", Double.NaN, pPeriods[4].getPeriodSingleValue(1), DELTA);
        assertEquals("Period 5 single value", Double.NaN, pPeriods[5].getPeriodSingleValue(1), DELTA);
        assertEquals("Period 6 single value", Double.NaN, pPeriods[6].getPeriodSingleValue(1), DELTA);
        assertEquals("Period 7 single value", Double.NaN, pPeriods[7].getPeriodSingleValue(1), DELTA);
        assertEquals("Sum 1 single values", Double.NaN, sum(pPeriods, 1), DELTA);

        System.out.println("Single values for index 2: " + Arrays.asList(pPeriods));
        assertEquals("Period 0 single value", Double.NaN, pPeriods[0].getPeriodSingleValue(2), DELTA);
        assertEquals("Period 1 single value", Double.NaN, pPeriods[1].getPeriodSingleValue(2), DELTA);
        assertEquals("Period 2 single value", Double.NaN, pPeriods[2].getPeriodSingleValue(2), DELTA);
        assertEquals("Period 3 single value", Double.NaN, pPeriods[3].getPeriodSingleValue(2), DELTA);
        assertEquals("Period 4 single value", Double.NaN, pPeriods[4].getPeriodSingleValue(2), DELTA);
        assertEquals("Period 5 single value", Double.NaN, pPeriods[5].getPeriodSingleValue(2), DELTA);
        assertEquals("Period 6 single value", Double.NaN, pPeriods[6].getPeriodSingleValue(2), DELTA);
        assertEquals("Period 7 single value", Double.NaN, pPeriods[7].getPeriodSingleValue(2), DELTA);
        assertEquals("Sum 2 single values", Double.NaN, sum(pPeriods, 2), DELTA);

        System.out.println("Single values for index 3: " + Arrays.asList(pPeriods));
        assertEquals("Period 0 single value", Double.NaN, pPeriods[0].getPeriodSingleValue(3), DELTA);
        assertEquals("Period 1 single value", Double.NaN, pPeriods[1].getPeriodSingleValue(3), DELTA);
        assertEquals("Period 2 single value", Double.NaN, pPeriods[2].getPeriodSingleValue(3), DELTA);
        assertEquals("Period 3 single value", Double.NaN, pPeriods[3].getPeriodSingleValue(3), DELTA);
        assertEquals("Period 4 single value", Double.NaN, pPeriods[4].getPeriodSingleValue(3), DELTA);
        assertEquals("Period 5 single value", Double.NaN, pPeriods[5].getPeriodSingleValue(3), DELTA);
        assertEquals("Period 6 single value", Double.NaN, pPeriods[6].getPeriodSingleValue(3), DELTA);
        assertEquals("Period 7 single value", Double.NaN, pPeriods[7].getPeriodSingleValue(3), DELTA);
        assertEquals("Sum 3 single values", Double.NaN, sum(pPeriods, 3), DELTA);

    }

    @Test
    public void testCalculateIntersectionOfPeriods() {

        System.out.println(NEWLINE + "testCalculateIntersectionOfPeriods");

        Period timespan = new Period(20, 30);

        //komplett ausserhalb (vorher)
        Period periodBefore = new Period(0, 10);
        SpreadCalculatorSaprimaFinal.calculateIntersectionOfPeriods(periodBefore, timespan);
        assertEquals("before [start]", 0, periodBefore.getCalcStartDate());
        assertEquals("before [finish]", 10, periodBefore.getCalcFinishDate());
        assertFalse("before [flag]", periodBefore.isInsideTimespan());

        //anstossend/beruehrend (vorne)
        Period periodProceeding = new Period(15, 20);
        SpreadCalculatorSaprimaFinal.calculateIntersectionOfPeriods(periodProceeding, timespan);
        assertEquals("proceeding [start]", 15, periodProceeding.getCalcStartDate());
        assertEquals("proceeding [finish]", 20, periodProceeding.getCalcFinishDate());
        assertFalse("proceeding [flag]", periodProceeding.isInsideTimespan());

        //ueberlappend (vorne)
        Period periodOverlapBefore = new Period(15, 25);
        SpreadCalculatorSaprimaFinal.calculateIntersectionOfPeriods(periodOverlapBefore, timespan);
        assertEquals("overlapping-before [start]", 20, periodOverlapBefore.getCalcStartDate());
        assertEquals("overlapping-before [finish]", 25, periodOverlapBefore.getCalcFinishDate());
        assertTrue("overlapping-before [flag]", periodOverlapBefore.isInsideTimespan());

        //ueberlappend um nur 1 Millisekunde (vorne)
        Period periodOverlapBeforeMS = new Period(19, 21);
        SpreadCalculatorSaprimaFinal.calculateIntersectionOfPeriods(periodOverlapBeforeMS, timespan);
        assertEquals("overlapping-before-only1MS [start]", 20, periodOverlapBeforeMS.getCalcStartDate());
        assertEquals("overlapping-before-only1MS [finish]", 21, periodOverlapBeforeMS.getCalcFinishDate());
        assertTrue("overlapping-before-only1MS [flag]", periodOverlapBeforeMS.isInsideTimespan());

        //exakt
        Period periodEquals = new Period(timespan.getStartDate(), timespan.getFinishDate());
        SpreadCalculatorSaprimaFinal.calculateIntersectionOfPeriods(periodEquals, timespan);
        assertEquals("equal [start]", timespan.getStartDate(), periodEquals.getCalcStartDate());
        assertEquals("equal [finish]", timespan.getFinishDate(), periodEquals.getCalcFinishDate());
        assertTrue("equal [flag]", periodEquals.isInsideTimespan());

        //innerhalb
        Period periodInside = new Period(22, 28);
        SpreadCalculatorSaprimaFinal.calculateIntersectionOfPeriods(periodInside, timespan);
        assertEquals("inside [start]", 22, periodInside.getCalcStartDate());
        assertEquals("inside [finish]", 28, periodInside.getCalcFinishDate());
        assertTrue("inside [flag]", periodInside.isInsideTimespan());

        //ueberlappend (hinten)
        Period periodOverlapAfter = new Period(25, 35);
        SpreadCalculatorSaprimaFinal.calculateIntersectionOfPeriods(periodOverlapAfter, timespan);
        assertEquals("overlapping-after [start]", 25, periodOverlapAfter.getCalcStartDate());
        assertEquals("overlapping-after [finish]", 30, periodOverlapAfter.getCalcFinishDate());
        assertTrue("overlapping-after [flag]", periodOverlapAfter.isInsideTimespan());

        //ueberlappend - nur 1 Millisekunde (hinten)
        Period periodOverlapAfter1MS = new Period(29, 31);
        SpreadCalculatorSaprimaFinal.calculateIntersectionOfPeriods(periodOverlapAfter1MS, timespan);
        assertEquals("overlapping-after-only1MS [start]", 29, periodOverlapAfter1MS.getCalcStartDate());
        assertEquals("overlapping-after-only1MS [finish]", 30, periodOverlapAfter1MS.getCalcFinishDate());
        assertTrue("overlapping-after-only1MS [flag]", periodOverlapAfter1MS.isInsideTimespan());

        //exakt anschließed/beruehrend (hinten)
        Period periodFollowing = new Period(30, 50);
        SpreadCalculatorSaprimaFinal.calculateIntersectionOfPeriods(periodFollowing, timespan);
        assertEquals("following [start]", 30, periodFollowing.getCalcStartDate());
        assertEquals("following [finish]", 50, periodFollowing.getCalcFinishDate());
        assertFalse("following [flag]", periodFollowing.isInsideTimespan());

        //komplett ausserhalb (nachher)
        Period periodAfter = new Period(40, 50);
        SpreadCalculatorSaprimaFinal.calculateIntersectionOfPeriods(periodAfter, timespan);
        assertEquals("after [start]", 40, periodAfter.getCalcStartDate());
        assertEquals("after [finish]", 50, periodAfter.getCalcFinishDate());
        assertFalse("after [flag]", periodAfter.isInsideTimespan());

    }

    @Test
    public void testAllValuesReferToCompleteTimespan() {
        System.out.println(NEWLINE + "testAllValuesReferToCompleteTimespan");

        // Generate several TimeRelatedValues
        int totalValueCount = 30;
        Period pTimespan = new Period(0, 10000 * totalValueCount + 10000);
        TimeRelatedValue[] totalValues = new TimeRelatedValue[totalValueCount];

        for (int i = 0; i < totalValueCount; i++) {
            totalValues[i] = new TimeRelatedValue(12345, 10000 * i, 10000 * i + 10000, 6789);
        }
        assertFalse(SpreadCalculatorSaprimaFinal.allValuesReferToCompleteTimespan(totalValues, pTimespan));

        for (int i = 0; i < totalValueCount; i++) {
            totalValues[i] = new TimeRelatedValue(12345, 0, 10000 * totalValueCount + 10000, 6789);
        }
        assertTrue(SpreadCalculatorSaprimaFinal.allValuesReferToCompleteTimespan(totalValues, pTimespan));

        long originalValue = totalValues[0].getCalcStartDate();
        totalValues[0].setCalcStartDate(originalValue + 1);
        assertFalse(SpreadCalculatorSaprimaFinal.allValuesReferToCompleteTimespan(totalValues, pTimespan));

        totalValues[0].setCalcStartDate(originalValue);
        assertTrue(SpreadCalculatorSaprimaFinal.allValuesReferToCompleteTimespan(totalValues, pTimespan));

        originalValue = totalValues[totalValues.length - 1].getCalcFinishDate();
        totalValues[totalValues.length - 1].setCalcFinishDate(originalValue + 1);
        assertFalse(SpreadCalculatorSaprimaFinal.allValuesReferToCompleteTimespan(totalValues, pTimespan));

        totalValues[totalValues.length - 1].setCalcFinishDate(originalValue);
        assertTrue(SpreadCalculatorSaprimaFinal.allValuesReferToCompleteTimespan(totalValues, pTimespan));

        originalValue = totalValues[2].getCalcFinishDate();
        totalValues[2].setCalcFinishDate(originalValue + 1);
        assertFalse(SpreadCalculatorSaprimaFinal.allValuesReferToCompleteTimespan(totalValues, pTimespan));

        totalValues[2].setCalcFinishDate(originalValue);
        assertTrue(SpreadCalculatorSaprimaFinal.allValuesReferToCompleteTimespan(totalValues, pTimespan));

        originalValue = totalValues[1].getCalcStartDate();
        totalValues[2].setCalcStartDate(originalValue + 1);
        assertFalse(SpreadCalculatorSaprimaFinal.allValuesReferToCompleteTimespan(totalValues, pTimespan));

        totalValues[2].setCalcStartDate(originalValue);
        assertTrue(SpreadCalculatorSaprimaFinal.allValuesReferToCompleteTimespan(totalValues, pTimespan));

    }

    /**
     * Fünf Intervalle, die alle vollständig in der Zeitspanne des
     * TimeRelatedValue liegen.
     */
    @Test
    public void testCreateSubIntervals() {

        System.out.println(NEWLINE + "testCreateSubIntervals");

        TimeRelatedValue pSubTimespan = new TimeRelatedValue(33, 0, 20000, 0);
        Interval[] pIntervals = {
            new Interval(0, 5000, 40),
            new Interval(5000, 8000, 5),
            new Interval(8000, 12000, 5),
            new Interval(12000, 15000, 30),
            new Interval(15000, 20000, 20)};

        Interval[] subIntervals = SpreadCalculatorSaprimaFinal.createSubIntervals(pIntervals, pSubTimespan);

        assertEquals("subIntervals.length", 5, subIntervals.length);
        assertEquals("subIntervals[0].getStartDate", 0, subIntervals[0].getStartDate());
        assertEquals("subIntervals[0].getFinishDate", 5000, subIntervals[0].getFinishDate());
        assertEquals("subIntervals[1].getStartDate", 5000, subIntervals[1].getStartDate());
        assertEquals("subIntervals[1].getFinishDate", 8000, subIntervals[1].getFinishDate());
        assertEquals("subIntervals[2].getStartDate", 8000, subIntervals[2].getStartDate());
        assertEquals("subIntervals[2].getFinishDate", 12000, subIntervals[2].getFinishDate());
        assertEquals("subIntervals[3].getStartDate", 12000, subIntervals[3].getStartDate());
        assertEquals("subIntervals[3].getFinishDate", 15000, subIntervals[3].getFinishDate());
        assertEquals("subIntervals[4].getStartDate", 15000, subIntervals[4].getStartDate());
        assertEquals("subIntervals[4].getFinishDate", 20000, subIntervals[4].getFinishDate());
    }

    /**
     * Fünf Intervalle, davon 1 vor, 1 nach der Zeitspanne des TimeRelatedValue
     * 2 Intervalle werden von der Zeitspanne des TimeRelatedValue geschnitten.
     * 1 Intervall liegt vollständig in der Zeitspanne des TimeRelatedValue.
     */
    @Test
    public void testCreateSubIntervals2() {

        System.out.println(NEWLINE + "testCreateSubIntervals2");

        TimeRelatedValue pSubTimespan = new TimeRelatedValue(33, 6000, 14000, 0);
        Interval[] pIntervals = {
            new Interval(0, 5000, 40),
            new Interval(5000, 8000, 5),
            new Interval(8000, 12000, 5),
            new Interval(12000, 15000, 30),
            new Interval(15000, 20000, 20)};

        Interval[] subIntervals = SpreadCalculatorSaprimaFinal.createSubIntervals(pIntervals, pSubTimespan);

        assertEquals("subIntervals.length", 3, subIntervals.length);
        assertEquals("subIntervals[0].getStartDate", 6000, subIntervals[0].getStartDate());
        assertEquals("subIntervals[0].getFinishDate", 8000, subIntervals[0].getFinishDate());
        assertEquals("subIntervals[1].getStartDate", 8000, subIntervals[1].getStartDate());
        assertEquals("subIntervals[1].getFinishDate", 12000, subIntervals[1].getFinishDate());
        assertEquals("subIntervals[2].getStartDate", 12000, subIntervals[2].getStartDate());
        assertEquals("subIntervals[2].getFinishDate", 14000, subIntervals[2].getFinishDate());
    }

    /**
     * 9 TimeRelatedValues, davon 6 ganz oder teilweise außerhalb der
     * Gesamt-Zeitspanne, auf die die gesamte Kurve angelegt wird.
     */
    @Test
    public void checkDataCoherenceTest() {

        System.out.println(NEWLINE + "checkDataCoherenceTest");

        TimeRelatedValue[] pTotalValues = {
            new TimeRelatedValue(0, 1000, 5000, 0), // not OK
            new TimeRelatedValue(0, 5000, 10000, 0), // not OK
            new TimeRelatedValue(0, 9999, 15000, 0), // not OK
            new TimeRelatedValue(0, 10000, 15000, 0), // OK
            new TimeRelatedValue(0, 10000, 20000, 0), // OK
            new TimeRelatedValue(0, 15000, 20000, 0), // OK
            new TimeRelatedValue(0, 15000, 20001, 0), // not OK
            new TimeRelatedValue(0, 20000, 22000, 0), // not OK
            new TimeRelatedValue(0, 21000, 25000, 0) // not OK
        };
        Period pTimespan = new Period(10000, 20000);

        String errorMessage = SpreadCalculatorSaprimaFinal.checkDataCoherence(pTotalValues, pTimespan);
        assertNotNull(errorMessage);

        String[] result = errorMessage.split("exceeds");
        assertEquals("result.length", 7, result.length);
        System.out.println(errorMessage);
    }

    // Various performance tests
    @Test
    public void testComputeSpread_TestcasePerformance1() {
        System.out.println(NEWLINE + "testComputeSpread_TestcasePerformance1");
        double[] pValue = {1000D};
        int periodsCount = 30;
        Period[] pPeriods = new Period[periodsCount];
        for (int i = 0; i < periodsCount; i++) {
            pPeriods[i] = new Period(10000 * i, 10000 * i + 10000);
        }
        Period pTimespan = new Period(pPeriods[0].getStartDate(), pPeriods[periodsCount - 1].getFinishDate());

        double[] pCurve = {25, 75, 25, 75, 25, 75, 25, 75, 25, 75, 25, 75};
        Period[] pExclusionPeriods = null;

        SpreadCalculatorSaprimaFinal.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);

        long s = System.currentTimeMillis();
        final int ITERATIONS = 10000;
        for (int i = 0; i < ITERATIONS; i++) {
            SpreadCalculatorSaprimaFinal.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
        }
        final long total = System.currentTimeMillis() - s;
        System.out.printf("Total time for %d iterations: %dms\n", ITERATIONS, total);
//        assertTrue(total < 5000);
    }

    /**
     * Wie testComputeSpread_Testcase12, aber mit n Wiederholungen
     * und ohne Asserts
     */
    @Test
    public void testComputeSpread_TestcasePerformance2() {

        System.out.println(NEWLINE + "testComputeSpread_TestcasePerformance2");

        int iterations = 80000;

        long start = System.currentTimeMillis();

        for (int i = 0; i < iterations; i++) {
            double[] pValue = {1000D, 2000D, 0D};
            Period pTimespan = new Period(0, 20000);
            Period[] pPeriods = {new Period(0, 10000), new Period(5000, 15000), new Period(10000, 20000), new Period(15000, 20000)};
            double[] pCurve = {25, 25, 25, 25, 75, 75, 75, 75};
            Period[] pExclusionPeriods = {new Period(0, 1000), new Period(1000, 2000),
                new Period(2000, 3000), new Period(3000, 4000), new Period(4000, 5000),
                new Period(5000, 6000), new Period(6000, 7000), new Period(7000, 8000),
                new Period(8000, 9000), new Period(9000, 10000)};
            SpreadCalculatorSaprimaFinal.computeSpread(pValue, pTimespan.getCalcStartDate(), pTimespan.getCalcFinishDate(), pPeriods, pCurve, pExclusionPeriods);
        }

        long finish = System.currentTimeMillis();

        System.out.println("Total time for " + iterations + " iterations: " + (finish - start) + " ms");

        // Results of test run on PC of TWOSE:
        // 8000 iterations: 78 ms
        // 80000 iterations: 750 ms
        // 800.000 iterations: 7015 ms
        // 8.000.000 iterations: 69203 ms
    }

    /**
     * Wiederholter Aufruf einer bestimmten Testmethode.
     */
    @Test
    public void testComputeSpread_TestcasePerformance3() {

        System.out.println(NEWLINE + "testComputeSpread_TestcasePerformance3");

        int iterations = 80000;

        long start = System.currentTimeMillis();

        for (int i = 0; i < iterations; i++) {
//            System.out.println("*********************************\nInteration " + i);
            testComputeSpread_Testcase18();
        }

        long finish = System.currentTimeMillis();

        System.out.println("\nTotal time for " + iterations + " iterations: " + (finish - start) + " ms\n");

        // Results of test run on PC of TWOSE:
        // 800 iterations: 15 ms
        // 8000 iterations: 140 ms
        // 80000 iterations: 1094 ms
        // 800000 iterations: 10578 ms
    }
}
