/*
 * Created on 03.03.2009
 * Copyright(c) 2009 tcmj.  All Rights Reserved.
 * @author TDEUT - Thomas Deutsch - 2009
 */
package com.tcmj.pm.conflicts.data;

import org.slf4j.Logger;

import com.tcmj.pm.conflicts.ConflictFinder;
import com.tcmj.pm.conflicts.bars.Bar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.LoggerFactory;


/**
 *
 * @author tdeut
 */
public class ConflictFinderTest{

    /** Logging framework. */
    protected static final transient Logger logger = LoggerFactory.getLogger(ConflictFinderTest.class);

    /** Internal Dateformatter. */
    private static final DateFormat DFORM = new SimpleDateFormat("yyyy-MM-dd (H' Uhr')");

    public ConflictFinderTest() {
    	System.out.println("class.path: "+System.getProperty("java.class.path"));
    }

    

    
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    private void outputBarList(List<Bar> barlist) {
        logger.debug("------------------------------------------------------------------------------");
        for (Bar bar : barlist) {
            logger.debug("Input : " + DFORM.format(bar.getStartDate()) + "  to  " +
                    DFORM.format(bar.getEndDate()) +"  weight: "+bar.getWeight());
        }
        logger.debug("------------------------------------------------------------------------------");

    }

    public void assertBar(List<OutputBar> result, int index, Date start, Date end, double pct) {
        Assert.assertEquals("B" + index + "-Start", start, result.get(index).getStartDate());
        Assert.assertEquals("B" + index + "-End", end, result.get(index).getEndDate());
        Assert.assertEquals("B" + index + "-Pct", pct, result.get(index).getWeight(), 0D);
    }

    public void assertConflict(List<OutputBar> result, int index, Date start, Date end, double pct) {
        Assert.assertEquals("C" + index + "-Start", start, result.get(index).getStartDate());
        Assert.assertEquals("C" + index + "-End", end, result.get(index).getEndDate());
        Assert.assertEquals("C" + index + "-Pct", pct, result.get(index).getWeight(), 0D);
    }

    @Test
    public void test_01_First_Testdata() {

        logger.info("\n\n....testConflictFinder01.....AREVA_Testdata\n");

        List<Bar> barlist = new ArrayList<Bar>();

        SimpleBar bar1 = new SimpleBar("1", date(2009, 1, 1), date(2009, 3, 31), 0.3);
        SimpleBar bar2 = new SimpleBar("2", date(2009, 3, 1), date(2009, 6, 30), 0.4);
        SimpleBar bar3 = new SimpleBar("3", date(2009, 6, 1), date(2009, 7, 31), 0.2);
        SimpleBar bar4 = new SimpleBar("4", date(2009, 2, 1), date(2009, 4, 30), 0.5);

        barlist.add(bar1);
        barlist.add(bar2);
        barlist.add(bar3);
        barlist.add(bar4);

        outputBarList(barlist);

        ConflictFinder cfinder = new ConflictFinder();
        cfinder.setInputList(barlist);
        cfinder.calculate();

        //expect 7 bars
        List<OutputBar> result = cfinder.getOutputBarListAll();
        Assert.assertEquals("bars", 7, result.size());
        assertBar(result, 0, date(2009, 1, 1), date(2009, 2, 1), 0.3D);
        assertBar(result, 1, date(2009, 2, 1), date(2009, 3, 1), 0.8D);
        assertBar(result, 2, date(2009, 3, 1), date(2009, 3, 31), 1.2D);
        assertBar(result, 3, date(2009, 3, 31), date(2009, 4, 30), 0.9D);
        assertBar(result, 4, date(2009, 4, 30), date(2009, 6, 1), 0.4D);
        assertBar(result, 5, date(2009, 6, 1), date(2009, 6, 30), 0.6D);
        assertBar(result, 6, date(2009, 6, 30), date(2009, 7, 31), 0.2D);



        //expect one conflict 
        List<OutputBar> resultC = cfinder.getOutputConflictList();
        Assert.assertEquals("conflicts", 1, resultC.size());
        assertConflict(resultC, 0, date(2009, 3, 1), date(2009, 3, 31), 1.2D);



    }

    @Test
    public void test_02_NoOverlapping_NoConflicts() {


        logger.info("\n\n....testLoadBars2_NoOverlapping_NoConflicts.....Keine Ueberlappungen\n");

        List<Bar> barlist = new ArrayList<Bar>();

        SimpleBar bar1 = new SimpleBar("1", date(2009, 1, 1), date(2009, 1, 30), 1.0);
        SimpleBar bar2 = new SimpleBar("2", date(2009, 3, 1), date(2009, 3, 30), 1.0);
        SimpleBar bar3 = new SimpleBar("3", date(2009, 5, 1), date(2009, 5, 30), 1.0);
        barlist.add(bar1);
        barlist.add(bar2);
        barlist.add(bar3);
        outputBarList(barlist);

        ConflictFinder cfinder = new ConflictFinder();
        cfinder.setInputList(barlist);
        cfinder.calculate();

        //expect no conflict 
        List<OutputBar> resultC = cfinder.getOutputConflictList();
        Assert.assertEquals("conflicts", 0, resultC.size());

        List<OutputBar> result = cfinder.getOutputBarListAll();
        Assert.assertEquals("bars", 3, result.size());
        assertBar(result, 0, date(2009, 1, 1), date(2009, 1, 30), 1D);
        assertBar(result, 1, date(2009, 3, 1), date(2009, 3, 30), 1D);
        assertBar(result, 2, date(2009, 5, 1), date(2009, 5, 30), 1D);
    }

    @Test
    public void test_03_OnlyOneConflictTask() {

        logger.info("\n\n....testLoadBars3_OnlyOneConflictTask.....Nur 1 Task der als Konflikt ausgegeben werden muss (130%WL)\n");

        List<Bar> barlist = new ArrayList<Bar>();
        SimpleBar bar1 = new SimpleBar("1", date(2009, 1, 1), date(2009, 1, 10), 1.3);
        barlist.add(bar1);
        outputBarList(barlist);
        ConflictFinder cfinder = new ConflictFinder();
        cfinder.setInputList(barlist);
        cfinder.calculate();

        List<OutputBar> resultC = cfinder.getOutputConflictList();
        Assert.assertEquals("conflicts", 1, resultC.size());

        assertConflict(resultC, 0, date(2009, 1, 1), date(2009, 1, 10), 1.3D);


        List<OutputBar> result = cfinder.getOutputBarListAll();
        Assert.assertEquals("bars", 1, result.size());
        assertBar(result, 0, date(2009, 1, 1), date(2009, 1, 10), 1.3D);



    }

    @Test
    public void test_04_OnlyOneNormalTask() {

        logger.info("\n\n....testLoadBars3_OnlyOneNormalTask.....Nur 1 Task der nicht als Konflikt ausgegeben werden muss (100%WL)\n");

        //-------Fall 1 mit exakt 1.0

        List<Bar> barlist = new ArrayList<Bar>();
        SimpleBar bar1 = new SimpleBar("1", date(2009, 1, 1), date(2009, 1, 10), 1.0);
        barlist.add(bar1);
        outputBarList(barlist);
        ConflictFinder cfinder = new ConflictFinder();
        cfinder.setInputList(barlist);
        cfinder.calculate();

        List<OutputBar> resultC = cfinder.getOutputConflictList();
        Assert.assertEquals("Fall 1 mit exakt 1.0: conflicts", 0, resultC.size());



        List<OutputBar> result = cfinder.getOutputBarListAll();
        Assert.assertEquals("Fall 1 mit exakt 1.0: bars", 1, result.size());
        assertBar(result, 0, date(2009, 1, 1), date(2009, 1, 10), 1.0D);


        //-------Fall 2 mit weniger als 0.5

        List<Bar> barlist2 = new ArrayList<Bar>();
        SimpleBar bar21 = new SimpleBar("1", date(2009, 1, 1), date(2009, 1, 10), 0.3);
        barlist2.add(bar21);
        outputBarList(barlist2);
        ConflictFinder cfinder2 = new ConflictFinder();
        cfinder2.setInputList(barlist2);
        cfinder2.calculate();
        List<OutputBar> resultC2 = cfinder2.getOutputConflictList();
        Assert.assertEquals("Fall 2 mit weniger als 0.5: conflicts", 0, resultC2.size());

        List<OutputBar> result2 = cfinder2.getOutputBarListAll();
        Assert.assertEquals("Fall 2 mit weniger als 0.5: bars", 1, result2.size());
        assertBar(result2, 0, date(2009, 1, 1), date(2009, 1, 10), 0.3D);


    }

    @Test
    public void test_05_TwoEqualBarsAsConflict() {


        logger.info("\n\n....testLoadBars4_TwoEqualBarsAsConflict.....2 Exakt gleich lange Buchungen mit je 1.0\n");

        List<Bar> barlist = new ArrayList<Bar>();
        SimpleBar bar1 = new SimpleBar("1", date(2009, 1, 1), date(2009, 1, 30), 1.0);
        SimpleBar bar2 = new SimpleBar("2", date(2009, 1, 1), date(2009, 1, 30), 1.0);
        barlist.add(bar1);
        barlist.add(bar2);
        outputBarList(barlist);

        ConflictFinder cfinder = new ConflictFinder();
        cfinder.setInputList(barlist);
        cfinder.calculate();
        List<OutputBar> resultC = cfinder.getOutputConflictList();
        Assert.assertEquals("conflicts", 1, resultC.size());


        assertConflict(resultC, 0, date(2009, 1, 1), date(2009, 1, 30), 2.0D);


        List<OutputBar> result = cfinder.getOutputBarListAll();
        Assert.assertEquals("bars", 1, result.size());
        assertBar(result, 0, date(2009, 1, 1), date(2009, 1, 30), 2.0D);

    }

    @Test
    public void test_06_SameStartDatesDifferentEndDates() {

        logger.info("\n\n....testLoadBars6_SameStartDatesDifferentEndDates.....Gleiche Start- andere EndTermine\n");

        List<Bar> barlist = new ArrayList<Bar>();

        SimpleBar bar1 = new SimpleBar("1", date(2009, 10, 1), date(2009, 10, 5), 0.5);
        SimpleBar bar2 = new SimpleBar("2", date(2009, 10, 1), date(2009, 10, 10), 0.3);
        SimpleBar bar3 = new SimpleBar("3", date(2009, 10, 1), date(2009, 10, 15), 0.2);
        SimpleBar bar4 = new SimpleBar("4", date(2009, 10, 1), date(2009, 10, 20), 0.1);

        barlist.add(bar1);
        barlist.add(bar2);
        barlist.add(bar3);
        barlist.add(bar4);

        outputBarList(barlist);
        ConflictFinder cfinder = new ConflictFinder();
        cfinder.setInputList(barlist);
        cfinder.calculate();
        //expect one conflict 
        List<OutputBar> resultC = cfinder.getOutputConflictList();
        Assert.assertEquals("conflicts", 1, resultC.size());


        assertConflict(resultC, 0, date(2009, 10, 1), date(2009, 10, 5), 1.1D);

        //expect 7 bars
        List<OutputBar> result = cfinder.getOutputBarListAll();
        Assert.assertEquals("bars", 4, result.size());
        assertBar(result, 0, date(2009, 10, 1), date(2009, 10, 5), 1.1D);
        assertBar(result, 1, date(2009, 10, 5), date(2009, 10, 10), 0.6D);
        assertBar(result, 2, date(2009, 10, 10), date(2009, 10, 15), 0.3D);
        assertBar(result, 3, date(2009, 10, 15), date(2009, 10, 20), 0.1D);



    }

    @Test
    public void test_07_TimeTest() {


        logger.info("\n\n....testLoadBars7_TimeTest.....Problemprojekt mit Uhrzeit (88% Resource)\n");

        List<Bar> barlist = new ArrayList<Bar>();

        SimpleBar bar1 = new SimpleBar("TD1", date(2009, 3, 16, 8), date(2009, 3, 19, 16), 0.88);
        SimpleBar bar2 = new SimpleBar("TD2", date(2009, 3, 18, 8), date(2009, 3, 20, 16), 0.88);
        SimpleBar bar3 = new SimpleBar("TD1", date(2009, 3, 23, 8), date(2009, 3, 27, 16), 0.44);
        SimpleBar bar4 = new SimpleBar("TD1", date(2009, 3, 23, 8), date(2009, 3, 27, 16), 0.44);

        /*
         *_______________XXXX
         *_________________XXX  
         *______________________XXXXX
         *______________________XXXXX 
         */




        barlist.add(bar1);
        barlist.add(bar2);
        barlist.add(bar3);
        barlist.add(bar4);
        outputBarList(barlist);

        ConflictFinder cfinder = new ConflictFinder();
        cfinder.setMaxAllowedWeight(0.88);
        cfinder.setInputList(barlist);
        cfinder.calculate();
        //expect no conflict 
        List<OutputBar> resultC = cfinder.getOutputConflictList();
        Assert.assertEquals("conflicts", 1, resultC.size());
        assertConflict(resultC, 0, date(2009, 3, 18, 8), date(2009, 3, 19, 16), 1.76D);




        List<OutputBar> result = cfinder.getOutputBarListAll();
        Assert.assertEquals("bars", 4, result.size());
        assertBar(result, 0, date(2009, 3, 16, 8), date(2009, 3, 18, 8), 0.88D);
        assertBar(result, 1, date(2009, 3, 18, 8), date(2009, 3, 19, 16), 1.76D);
        assertBar(result, 2, date(2009, 3, 19, 16), date(2009, 3, 20, 16), 0.88D);
        assertBar(result, 3, date(2009, 3, 23, 8), date(2009, 3, 27, 16), 0.88D);


    }

    @Test
    public void test_08_ClassTest() {


        logger.info("\n\n....testLoadBars8_ClassTest.....Verwendungsmöglichkeit der Klasse\n");

        ConflictFinder cfinder = new ConflictFinder();

        cfinder.addToInputList(new SimpleBar("TD1", date(2009, 3, 10), date(2009, 3, 20), 1.0));
        cfinder.addToInputList(new SimpleBar("TD1", date(2009, 3, 16), date(2009, 3, 30), 1.0));
        cfinder.calculate();

        //expect no conflict 
        List<OutputBar> resultC = cfinder.getOutputConflictList();
        Assert.assertEquals("conflicts", 1, resultC.size());
        assertConflict(resultC, 0, date(2009, 3, 16), date(2009, 3, 20), 2.0D);




        List<OutputBar> result = cfinder.getOutputBarListAll();
        Assert.assertEquals("bars", 3, result.size());
        assertBar(result, 0, date(2009, 3, 10), date(2009, 3, 16), 1.0D);
        assertBar(result, 1, date(2009, 3, 16), date(2009, 3, 20), 2.0D);
        assertBar(result, 2, date(2009, 3, 20), date(2009, 3, 30), 1.0D);

        
        System.out.println("");
    }

    @Test
    public void test_09_MaxAllowedWeightTest() {


        logger.info("\n\n....testLoadBars9_MaxAllowedWeightTest.....Maximal erlaubt (50% Resource)\n");

        List<Bar> barlist = new ArrayList<Bar>();
        ConflictFinder cfinder = new ConflictFinder();
        cfinder.setPrecision(new Precision(10000000000L));
        cfinder.setMaxAllowedWeight(0.50);
        SimpleBar bar1 = new SimpleBar("TD1", date(2009, 3, 10), date(2009, 3, 20), 0.50);
        SimpleBar bar2 = new SimpleBar("TD2", date(2009, 3, 15), date(2009, 3, 25), 0.50);

        barlist.add(bar1);
        barlist.add(bar2);
        cfinder.setInputList(barlist);
        outputBarList(barlist);

        cfinder.calculate();

        //expect no conflict 
        List<OutputBar> resultC = cfinder.getOutputConflictList();
        Assert.assertEquals("conflictsA", 1, resultC.size());

        assertConflict(resultC, 0, date(2009, 3, 15), date(2009, 3, 20), 1.0D);


        List<OutputBar> result = cfinder.getOutputBarListAll();
        Assert.assertEquals("barsA", 3, result.size());
        assertBar(result, 0, date(2009, 3, 10), date(2009, 3, 15), 0.5D);
        assertBar(result, 1, date(2009, 3, 15), date(2009, 3, 20), 1.0D);
        assertBar(result, 2, date(2009, 3, 20), date(2009, 3, 25), 0.5D);

        //Same Input but instead of 50% UPT --> 100% UPT
        cfinder.setMaxAllowedWeight(1.0);
        cfinder.calculate();
        Assert.assertEquals("conflictsB", 0, cfinder.getOutputConflictList().size());


        List<OutputBar> resultB = cfinder.getOutputBarListAll();
        Assert.assertEquals("barsB", 3, resultB.size());
        assertBar(resultB, 0, date(2009, 3, 10), date(2009, 3, 15), 0.5D);
        assertBar(resultB, 1, date(2009, 3, 15), date(2009, 3, 20), 1.0D);
        assertBar(resultB, 2, date(2009, 3, 20), date(2009, 3, 25), 0.5D);



    }

    @Test
    public void test_10_InternalCalculations() {

        logger.info("\n\n....testInternalCalculations.....Berechnungen\n");

        ConflictFinder cfinder = new ConflictFinder();
        cfinder.setMaxAllowedWeight(1.5);
        double maw = cfinder.getMaxAllowedWeight();

        Assert.assertEquals(1.5, maw, 0);

//        cfinder.setPrecision(Precision.FOUR_DIGITS);
        Assert.assertEquals(5000L, Precision.FOUR_DIGITS.double2Long(0.5));

        Assert.assertEquals(11235L, Precision.FOUR_DIGITS.double2Long(1.123456789D));


    }

    @Test
    public void test_11_DataIntegrity1_AREVA_Testdata() {


        logger.info("\n\n....testDataIntegrity1_AREVA_Testdata.....AREVA_Testdata Datenintegrität\n");

        List<Bar> barlist = new ArrayList<Bar>();

        barlist.add(new SimpleBar("1", date(2009, 1, 1), date(2009, 3, 31), 0.3));
        barlist.add(new SimpleBar("2", date(2009, 3, 1), date(2009, 6, 30), 0.4));
        barlist.add(new SimpleBar("3", date(2009, 6, 1), date(2009, 7, 31), 0.2));
        barlist.add(new SimpleBar("4", date(2009, 2, 1), date(2009, 4, 30), 0.5));
        outputBarList(barlist);
        ConflictFinder cfinder = new ConflictFinder();
        cfinder.setInputList(barlist);
        cfinder.calculate();

        List<OutputBar> result = cfinder.getOutputBarListAll();
        int loopct = 1;
        for (Iterator<OutputBar> it = result.iterator(); it.hasNext(); loopct++) {
            Bar bar = it.next();

            switch (loopct) {

                case 1:
                    Assert.assertEquals("Bar" + loopct + "-Weight", 0.3, bar.getWeight(), 0);
                    break;
                case 2:
                    Assert.assertEquals("Bar" + loopct + "-Weight", 0.8, bar.getWeight(), 0);
                    break;
                case 3:
                    Assert.assertEquals("Bar" + loopct + "-Weight", 1.2, bar.getWeight(), 0);
                    break;
                case 4:
                    Assert.assertEquals("Bar" + loopct + "-Weight", 0.9, bar.getWeight(), 0);
                    break;
                case 5:
                    Assert.assertEquals("Bar" + loopct + "-Weight", 0.4, bar.getWeight(), 0);
                    break;
                case 6:
                    Assert.assertEquals("Bar" + loopct + "-Weight", 0.6, bar.getWeight(), 0);
                    break;
                case 7:
                    Assert.assertEquals("Bar" + loopct + "-Weight", 0.2, bar.getWeight(), 0);
                    break;

            }


        }

        Assert.assertEquals("bars", 7, result.size());

        //expect one conflict 
        List<OutputBar> resultC = cfinder.getOutputConflictList();
        Assert.assertEquals("conflicts", 1, resultC.size());

        for (OutputBar conflictBar : resultC) {
            //only 1 loop!!!
            Assert.assertEquals("AmountOfCausingBars", 3, conflictBar.getCausingBars().size());
            Assert.assertEquals("StartDate", date(2009, 3, 1), conflictBar.getStartDate());
            Assert.assertEquals("EndDate", date(2009, 3, 31), conflictBar.getEndDate());
//            Assert.assertEquals("Type", 99, conflictBar.getProperty());
            Assert.assertEquals("Weight", 1.2, conflictBar.getWeight(), 0);
        }


    }

    @Test
    public void test_12_DataIntegrity2_Bizimis_Case() {


        logger.info("\n\n....testDataIntegrity2_Bizimis_Case.....Sonderfall Datenintegrität\n");

        List<Bar> barlist = new ArrayList<Bar>();

        int type_emp = 33, type_shift = 50;

        /*           EE    (10. - 11.)
         *  PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP  (01. - 31.)         */

        SimpleBar bar1 = new SimpleBar("Employee", date(2009, 1, 10), date(2009, 1, 11), 1.0);
        bar1.setProperty("type", type_emp);
        barlist.add(bar1);
        SimpleBar bar2 = new SimpleBar("Project", date(2009, 1, 1), date(2009, 1, 31), 1.0);
        bar2.setProperty("type", type_shift);
        barlist.add(bar2);
        outputBarList(barlist);
        ConflictFinder cfinder = new ConflictFinder();
        cfinder.setInputList(barlist);
        cfinder.calculate();

        List<OutputBar> result = cfinder.getOutputBarListAll();
        int loopct = 1;
        for (Iterator<OutputBar> it = result.iterator(); it.hasNext(); loopct++) {
            Bar bar = it.next();

            switch (loopct) {

                case 1:
                    Assert.assertEquals("Bar" + loopct + "-Start", date(2009, 1, 1), bar.getStartDate());
                    Assert.assertEquals("Bar" + loopct + "-End", date(2009, 1, 10), bar.getEndDate());
                    Assert.assertEquals("Bar" + loopct + "-Weight", 1.0, bar.getWeight(), 0);
                    System.out.println("Bar" + loopct + "-Type = " + bar.getProperty("type"));
                    Assert.assertEquals("Bar" + loopct + "-Type", type_shift, bar.getProperty("type"));
                    break;
                case 2:
                    Assert.assertEquals("Bar" + loopct + "-Start", date(2009, 1, 10), bar.getStartDate());
                    Assert.assertEquals("Bar" + loopct + "-End", date(2009, 1, 11), bar.getEndDate());
                    Assert.assertEquals("Bar" + loopct + "-Weight", 2.0, bar.getWeight(), 0);
                    System.out.println("Bar" + loopct + "-Type = " + bar.getProperty("type"));
                    break;
                case 3:
                    Assert.assertEquals("Bar" + loopct + "-Start", date(2009, 1, 11), bar.getStartDate());
                    Assert.assertEquals("Bar" + loopct + "-End", date(2009, 1, 31), bar.getEndDate());

                    Assert.assertEquals("Bar" + loopct + "-Weight", 1.0, bar.getWeight(), 0);

                    System.out.println("Bar" + loopct + "-Type = " + bar.getProperty("type"));
                    Assert.assertEquals("Bar" + loopct + "-Type", type_shift, bar.getProperty("type"));
                    break;


            }


        }

        Assert.assertEquals("bars", 3, result.size());

        //expect one conflict 
        List<OutputBar> resultC = cfinder.getOutputConflictList();
        Assert.assertEquals("conflicts", 1, resultC.size());

        assertConflict(resultC, 0, date(2009, 1, 10), date(2009, 1, 11), 2.0D);



    }

    @Test
    public void test_13_DataIntegrity3_Four_Same_Bars_Test() {


        logger.info("\n\n....testDataIntegrity3_Four_Same_Bars_Test.....4 Gleiche\n");

        List<Bar> barlist = new ArrayList<Bar>();

        barlist.add(new SimpleBar("1", date(2009, 1, 1), date(2009, 1, 30), 1.0));
        barlist.add(new SimpleBar("2", date(2009, 1, 1), date(2009, 1, 30), 1.0));
        barlist.add(new SimpleBar("3", date(2009, 1, 1), date(2009, 1, 30), 1.0));
        barlist.add(new SimpleBar("4", date(2009, 1, 1), date(2009, 1, 30), 1.0));
        outputBarList(barlist);
        ConflictFinder cfinder = new ConflictFinder();
        cfinder.setInputList(barlist);
        cfinder.calculate();
        Assert.assertEquals("bars", 1, cfinder.getOutputBarListAll().size());

        // ?????????????????????????????????????????????
        // ??????????  TODO 5 or 1 conflict ????????????
        // ?????????????????????????????????????????????

        List<OutputBar> resultC = cfinder.getOutputConflictList();
        Assert.assertEquals("conflicts", 1, resultC.size());

        assertConflict(resultC, 0, date(2009, 1, 1), date(2009, 1, 30), 4.0D);


        assertBar(cfinder.getOutputBarListAll(), 0, date(2009, 1, 1), date(2009, 1, 30), 4.0D);



    }

    @Test
    public void test_14_SonderfallTest() {


        logger.info("\n\n....testLoadBars5_SonderfallTest.....Urbansky\n");

        List<Bar> barlist = new ArrayList<Bar>();

        SimpleBar bar1 = new SimpleBar("KWB", date(2009, 1, 27), date(2009, 5, 29), 1.0);
        SimpleBar bar2 = new SimpleBar("KKG", date(2009, 4, 14), date(2009, 5, 3), 1.0);
        SimpleBar bar3 = new SimpleBar("KCB", date(2009, 4, 12), date(2009, 4, 15), 1.0);

        /*
         *_XXXXXXXXXXXXXXXXXXXXXXXXXXXX KWB 
         *___________XXXXXXX KKG 
         *_________XXXXX KCB
         *_0000000011222333344444444444  
         */

        barlist.add(bar1);
        barlist.add(bar2);
        barlist.add(bar3);
        outputBarList(barlist);


        ConflictFinder cfinder = new ConflictFinder();
        cfinder.setInputList(barlist);
        cfinder.calculate();
        //expect no conflict 
        List<OutputBar> resultC = cfinder.getOutputConflictList();
        Assert.assertEquals("conflicts", 3, resultC.size());
        assertConflict(resultC, 0, date(2009, 4, 12), date(2009, 4, 14), 2.0D);
        assertConflict(resultC, 1, date(2009, 4, 14), date(2009, 4, 15), 3.0D);
        assertConflict(resultC, 2, date(2009, 4, 15), date(2009, 5, 3), 2.0D);



        List<OutputBar> result = cfinder.getOutputBarListAll();
        Assert.assertEquals("bars", 5, result.size());
        assertBar(result, 0, date(2009, 1, 27), date(2009, 4, 12), 1D);
        assertBar(result, 1, date(2009, 4, 12), date(2009, 4, 14), 2.0D);
        assertBar(result, 2, date(2009, 4, 14), date(2009, 4, 15), 3.0D);
        assertBar(result, 3, date(2009, 4, 15), date(2009, 5, 3), 2.0D);
        assertBar(result, 4, date(2009, 5, 3), date(2009, 5, 29), 1D);

    }

    @Test
    public void test_15_SonderfallTest2() {


        logger.info("\n\n....testLoadBars6_SonderfallTest2.....Kratz Peter\n");

        List<Bar> barlist = new ArrayList<Bar>();

        SimpleBar bar1 = new SimpleBar("94891", date(2009, 3, 23, 8), date(2009, 3, 27, 17), 1.0);
        SimpleBar bar2 = new SimpleBar("95588", date(2009, 3, 23, 8), date(2009, 4, 14, 10), 0.1);
        SimpleBar bar3 = new SimpleBar("77372", date(2009, 3, 23, 8), date(2009, 4, 14, 10), 0.1);

        /*
         *__XXXXXXXXXX 100%
         *__XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX  10%
         *__XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX  10%
         */



        barlist.add(bar1);
        barlist.add(bar2);
        barlist.add(bar3);
        outputBarList(barlist);


        ConflictFinder cfinder = new ConflictFinder();
        cfinder.setInputList(barlist);
        cfinder.calculate();
        //expect no conflict 
        List<OutputBar> resultC = cfinder.getOutputConflictList();
        Assert.assertEquals("conflicts", 1, resultC.size());
        assertConflict(resultC, 0, date(2009, 3, 23, 8), date(2009, 3, 27, 17), 1.2D);


        List<OutputBar> result = cfinder.getOutputBarListAll();
        assertBar(result, 0, date(2009, 3, 23, 8), date(2009, 3, 27, 17), 1.2D);
        assertBar(result, 1, date(2009, 3, 27, 17), date(2009, 4, 14, 10), 0.2D);



        Assert.assertEquals("bars", 2, result.size());
    }

    @Test
    public void test_16_SonderfallTest3() {


        logger.info("\n\n....testLoadBars5_SonderfallTest3.....dd\n");

        List<Bar> barlist = new ArrayList<Bar>();



        barlist.add(new SimpleBar("D1", date(2009, 1, 1, 0), date(2009, 1, 1, 24), 0.8));
        barlist.add(new SimpleBar("D2", date(2009, 1, 1, 0), date(2009, 1, 1, 24), 0.8));
        barlist.add(new SimpleBar("D1", date(2009, 1, 2, 0), date(2009, 1, 2, 24), 0.8));
        barlist.add(new SimpleBar("D2", date(2009, 1, 2, 0), date(2009, 1, 2, 24), 0.8));

        outputBarList(barlist);

        ConflictFinder cfinder = new ConflictFinder();
        cfinder.setInputList(barlist);
        cfinder.calculate();

        //expect 7 bars
        List<OutputBar> result = cfinder.getOutputBarListAll();
        Assert.assertEquals("bars", 2, result.size());
        assertBar(result, 0, date(2009, 1, 1, 0), date(2009, 1, 1, 24), 1.6D);
        assertBar(result, 1, date(2009, 1, 2, 0), date(2009, 1, 2, 24), 1.6D);



        //expect one conflict
        List<OutputBar> resultC = cfinder.getOutputConflictList();
        Assert.assertEquals("conflicts", 2, resultC.size());
        assertConflict(resultC, 0, date(2009, 1, 1, 0), date(2009, 1, 1, 24), 1.6D);
        assertConflict(resultC, 1, date(2009, 1, 2, 0), date(2009, 1, 2, 24), 1.6D);



    }

     @Test
    public void test_17_MassTest() {


        logger.info("\n\n....testLoadBars4_TwoEqualBarsAsConflict.....2 Exakt gleich lange Buchungen mit je 1.0\n");

        List<Bar> barlist = new ArrayList<Bar>();
//logger.setLevel(Level.INFO);
        double amt = 500;

         for (int i = 1; i <= amt; i++) {
             
             SimpleBar bar1 = new SimpleBar("1", date(2009, 1, 1), date(2009, 1, 30), 1.0);
             barlist.add(bar1);

         }
//        logger.setLevel(Level.DEBUG);
        
        outputBarList(barlist);

        ConflictFinder cfinder = new ConflictFinder();
        cfinder.setInputList(barlist);
        cfinder.calculate();

        List<OutputBar> resultC = cfinder.getOutputConflictList();
        Assert.assertEquals("conflicts", 1, resultC.size());
        assertConflict(resultC, 0, date(2009, 1, 1), date(2009, 1, 30), amt);


        List<OutputBar> result = cfinder.getOutputBarListAll();
        Assert.assertEquals("bars", 1, result.size());
        assertBar(result, 0, date(2009, 1, 1), date(2009, 1, 30), amt);

    }
    
    
    private static Date date(int y, int m, int d) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(y, m - 1, d);
        Date date1 = calendar.getTime();
        return date1;
    }

    private static Date date(int y, int m, int d, int h) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(y, m - 1, d);
        calendar.set(Calendar.HOUR_OF_DAY, h);
        Date date1 = calendar.getTime();
        return date1;
    }
}