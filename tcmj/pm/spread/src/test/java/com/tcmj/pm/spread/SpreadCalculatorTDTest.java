package com.tcmj.pm.spread;

import com.tcmj.pm.spread.impl.SpreadPeriod;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
import static com.tcmj.pm.spread.impl.SpreadCalculatorTest.sum;

/**
 * Extended tests for class SpreadCalculator.
 * @author tdeut - Thomas Deutsch
 * @version $Id$
 */
public class SpreadCalculatorTDTest extends AbstractSpreadTest {


    /** Primavera Curve Linear. */
    private static double[] CURVE_LINEAR = { 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5};




    /** Internal test method used which calls the 'computeSpread' method using som fixed parameters:
     * <ul>
     * <li>Timeframe startdate = 01.01.2010 (as long value)</li>
     * <li>Timeframe finishdate = 31.12.2010 (as long value)</li>
     * </ul>
     */
    private void singleValueTestCase(double inputValue,
            long inputStart, long inputFinish,
            SpreadPeriod[] inputPeriods, double[] inputCurve, SpreadPeriod[] inputExclusionPeriods,
            double[] expectedPeriodValue, boolean testSum) {

        double[] arInputValue = {inputValue};
        
        System.out.println("Amount of curve values: "+inputCurve.length);

        spread.computeSpread(arInputValue, inputStart, inputFinish, inputPeriods, inputCurve, inputExclusionPeriods);

        output(inputPeriods);

        for (int i = 0; i < inputPeriods.length; i++) {
            assertEquals("Period " + i, expectedPeriodValue[i], inputPeriods[i].getPeriodValue(), DELTA);
        }
        
        //The sum can only be tested if you don't test more than one method call!
        if (testSum) {
            assertEquals("Sum", inputValue, sum(inputPeriods), DELTA);
        }
    }


    /** internal helper method to create a date.
     * <pre>Please note that the month will be corrected by 1 (parameter 1 = januar) for
     * better readability! Example:
     * long start = date(2010,1,1,8);</pre> */
    private long date(int year, int month, int day, int hour24) {
        Calendar calc = Calendar.getInstance();
        calc.set(Calendar.YEAR, year);
        calc.set(Calendar.MONTH, (month - 1));  //january
        calc.set(Calendar.DATE, day);
        calc.set(Calendar.HOUR_OF_DAY, hour24);
        calc.set(Calendar.MINUTE, 0);
        calc.set(Calendar.SECOND, 0);
        calc.set(Calendar.MILLISECOND, 0);
        return calc.getTimeInMillis();
    }


    private long dateS(int year, int month, int day) {
        return date(year, month, day, 0);
    }


    private long dateF(int year, int month, int day) {
        return date(year, month, day, 0);
    }


    private SpreadPeriod period(long start, long finish) {
        return new SpreadPeriod(start, finish);
    }



    /**
     * <pre>Einfacher Primavera Test:
     * Start:  03-Jan-11 08:00 AM
     * Finish: 28-Jan-11 04:00 PM
     * Hours: 800 (=100 days)
     * Spreads: Weekly
     * Lineare verteilung
     * </pre>
     */
    @Test
    public void testPrimaveraCase01() {
        System.out.println("testPrimaveraCase01");

        double value2Spread = 800D;
        long start = date(2011, 1, 3, 0);
        long finish = date(2011, 1, 29, 0);


        SpreadPeriod[] periods = {
            period(dateS(2011, 1, 2), dateF(2011, 1, 9)),
            period(dateS(2011, 1, 9), dateF(2011, 1, 16)),
            period(dateS(2011, 1, 16), dateF(2011, 1, 23)),
            period(dateS(2011, 1, 23), dateF(2011, 1, 30)),
            period(dateS(2011, 1, 30), dateF(2011, 2, 6)),
            period(dateS(2011, 2, 6), dateF(2011, 2, 13)),
            period(dateS(2011, 2, 13), dateF(2011, 2, 20))
        };

        SpreadPeriod[] exclusions = {
            period(date(2011, 1, 1, 0), date(2011, 1, 1, 24)),
            period(date(2011, 1, 2, 0), date(2011, 1, 2, 24)),

            period(date(2011, 1, 8, 0), date(2011, 1, 8, 24)),
            period(date(2011, 1, 9, 0), date(2011, 1, 9, 24)),

            period(date(2011, 1, 15, 0), date(2011, 1, 15, 24)),
            period(date(2011, 1, 16, 0), date(2011, 1, 16, 24)),

            period(date(2011, 1, 22, 0), date(2011, 1, 22, 24)),
            period(date(2011, 1, 23, 0), date(2011, 1, 23, 24)),

            period(date(2011, 1, 29, 0), date(2011, 1, 30, 24))
        };

        double[] expected = {
            200,
            200,
            200,
            200,
            0,
            0,
            0,
        };
        singleValueTestCase(value2Spread, start, finish, periods, CURVE_LINEAR, exclusions, expected, true);


    }


    /**
     * Mehrere Methodenaufrufe hintereinander muessen das uebergebene Perioden-Array
     * aufsummieren!
     */
     @Test
    public void testCaseMultipleMethodCalls() {
         System.out.println("testCaseMultipleMethodCalls");
         
        double value2Spread = 100D;
        long start = date(2011, 1, 1, 0);
        long finish = date(2011, 1, 31, 0);
        SpreadPeriod[] periods = {
            period(dateS(2011, 1, 1), dateF(2011, 1, 31)),
        };

        SpreadPeriod[] exclusions = null;
        double[] expected = { 100 };
        singleValueTestCase(value2Spread, start, finish, periods, CURVE_LINEAR, exclusions, expected, true);

        value2Spread = 200D;
        expected[0] =  300D ;
        
        singleValueTestCase(value2Spread, start, finish, periods, CURVE_LINEAR, exclusions, expected, false);

        value2Spread = 200D;
        expected[0] =  500D ;
        
        singleValueTestCase(value2Spread, start, finish, periods, CURVE_LINEAR, exclusions, expected, false);

    }





    private void output(SpreadPeriod[] data) {
        for (SpreadPeriod period : data) {
            System.out.println("::  "+period.toString());
        }
    }


}
