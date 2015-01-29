package com.tcmj.pm.spread.performance;

import com.tcmj.pm.spread.SpreadCalculator;
import com.tcmj.pm.spread.impl.SpreadCalculatorPrimaImpl;
import com.tcmj.pm.spread.impl.SpreadPeriod;
import com.tcmj.pm.spread.impl.SpreadDoubleImpl;
import com.tcmj.pm.spread.prima.SpreadCalculatorSaprimaFinal;
import java.util.Random;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Before;
import org.junit.Test;

/**
 * 2011-01-30:
 * Total time for 300000 iterations: 0:00:23.532  ms with class SpreadDoubleImpl
 * Total time for 300000 iterations: 0:00:04.922  ms with class SpreadCalculatorSaprimaImpl
 *
 * Total time for 300000 iterations: 0:00:06.375  ms with class SpreadDoubleImpl
 *
 * Total time for 300000 iterations: 0:00:05.812  ms with class SpreadDoubleImpl
 *
 *
 * @author Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 */
public class SpreadPerformance {

    private static final String PRINTF_FORMAT_STRING = "Values: %1d, Periods: %2d, CurveTicks: %3d, Iterations: %4d\n";


     @Before
    public void beforeEachTest() {
         System.out.println();
    }


    @Test
    public void overalPerformance() {
        System.out.println(">> OveralPerformance with manually defined parameters");
        performanceTest(new SpreadDoubleImpl());
        performanceTest(new SpreadCalculatorPrimaImpl());
        performanceTest(new SpreadCalculatorSaprimaFinal());
    }


    @Test
    public void manyValues() {
        System.out.println(">> Many values to spread Test (random values)");

        int amountValues = 10000,
            amountPeriods = 50,
            amountCurveValues = 20,
            amountExclusions = 0,
            amountIterations = 1000;
        System.out.printf(PRINTF_FORMAT_STRING, amountValues, amountPeriods, amountCurveValues, amountIterations);

        performanceTestDynamic(new SpreadDoubleImpl(), amountValues, amountPeriods, amountCurveValues, amountExclusions, amountIterations);
        performanceTestDynamic(new SpreadCalculatorPrimaImpl(), amountValues, amountPeriods, amountCurveValues, amountExclusions, amountIterations);
        performanceTestDynamic(new SpreadCalculatorSaprimaFinal(), amountValues, amountPeriods, amountCurveValues, amountExclusions, amountIterations);

    }


    @Test
    public void manyPeriods() {
        System.out.println(">> Many periods Test (random values)");

        int amountValues = 1,
            amountPeriods = 100000,
            amountCurveValues = 5,
            amountExclusions = 0,
            amountIterations = 1000;
        System.out.printf(PRINTF_FORMAT_STRING, amountValues, amountPeriods, amountCurveValues, amountIterations);

        performanceTestDynamic(new SpreadDoubleImpl(), amountValues, amountPeriods, amountCurveValues, amountExclusions, amountIterations);
        performanceTestDynamic(new SpreadCalculatorPrimaImpl(), amountValues, amountPeriods, amountCurveValues, amountExclusions, amountIterations);
        performanceTestDynamic(new SpreadCalculatorSaprimaFinal(), amountValues, amountPeriods, amountCurveValues, amountExclusions, amountIterations);

    }

    @Test
    public void manyCurveValues() {
        System.out.println(">> Many CurveValues Test  (random values)");

        int amountValues = 1,
            amountPeriods = 365,
            amountCurveValues = 10000,
            amountExclusions = 0,
            amountIterations = 100;
        System.out.printf(PRINTF_FORMAT_STRING, amountValues, amountPeriods, amountCurveValues, amountIterations);

        performanceTestDynamic(new SpreadDoubleImpl(), amountValues, amountPeriods, amountCurveValues, amountExclusions, amountIterations);
        performanceTestDynamic(new SpreadCalculatorPrimaImpl(), amountValues, amountPeriods, amountCurveValues, amountExclusions, amountIterations);
        performanceTestDynamic(new SpreadCalculatorSaprimaFinal(), amountValues, amountPeriods, amountCurveValues, amountExclusions, amountIterations);

    }


    private void performanceTest(SpreadCalculator spread) {

        final int ITERATIONS = 30000;

        double[] values = {1000000, 5000};
        int periodsCount = 364;
        SpreadPeriod[] periods = new SpreadPeriod[periodsCount];
        long day = 1000 * 60 * 60 * 24;

        for (int i = 0; i < periodsCount; i++) {
            periods[i] = new SpreadPeriod(day * i, day * i + day);
        }
        SpreadPeriod timespan = new SpreadPeriod(periods[0].getStartMillis(), periods[periodsCount - 1].getEndMillis());

        double[] curve = {0,1,2,3,3,3,3,3,2,1};
        
        
        //exclusions
        SpreadPeriod[] exclusions = new SpreadPeriod[52];
        for (int i = 0, k = 0; i < periodsCount; i++) {
            if (i % 7 == 0) {
                exclusions[k++] = new SpreadPeriod(day * i, day * i + day);
            }
        }

        spread.computeSpread(values, 0, 1, periods, curve, exclusions);

        StopWatch stopwatch = new StopWatch();


        stopwatch.start();
        for (int i = 0; i < ITERATIONS; i++) {
            spread.computeSpread(values, timespan.getStartMillis(), timespan.getEndMillis(), periods, curve, exclusions);
        }

        stopwatch.stop();

        System.out.printf("Total time for %1d iterations: %2s  ms with class %3s\n", ITERATIONS, stopwatch.toString(), spread.getClass().getSimpleName());



    }


    private void performanceTestDynamic(SpreadCalculator spread,
            final int amountValues,
            final int amountPeriods,
            final int amountCurveValues,
            final int amountExclusions,
            final int iterations) {

        Random rnd = new Random();

        //Values to spread
        double[] values = new double[amountValues];
        for (int i = 0; i < amountValues; i++) {
            values[i] = rnd.nextDouble() * 5000;
        }


        //Periods
        SpreadPeriod[] pPeriods = new SpreadPeriod[amountPeriods];
        for (int i = 0; i < amountPeriods; i++) {
            pPeriods[i] = new SpreadPeriod(10000 * i, 10000 * i + 10000);
        }


        //Time-Window
        SpreadPeriod timespan = new SpreadPeriod(pPeriods[20].getStartMillis(), pPeriods[amountPeriods - 1].getEndMillis());


        //Curve
        double[] curve = new double[amountCurveValues];
        for (int i = 0; i < amountCurveValues; i++) {
            curve[i] = rnd.nextDouble();  //TODO other method
        }


        //exclusions
        SpreadPeriod[] exclusions = new SpreadPeriod[amountExclusions];
        for (int i = 0; i < amountExclusions; i++) {
            exclusions[i] = new SpreadPeriod(1000 * i, 1000 * i + 10000);
        }



        spread.computeSpread(values, timespan.getStartMillis(), timespan.getEndMillis(), pPeriods, curve, exclusions);

        StopWatch stopwatch = new StopWatch();


        stopwatch.start();
        for (int i = 0; i < iterations; i++) {
            spread.computeSpread(values, timespan.getStartMillis(), timespan.getEndMillis(), pPeriods, curve, exclusions);
        }

        stopwatch.stop();


        System.out.printf("Total time for %1d iterations: %2s  ms with class %3s\n", iterations, stopwatch.toString(), spread.getClass().getSimpleName());



    }
}
