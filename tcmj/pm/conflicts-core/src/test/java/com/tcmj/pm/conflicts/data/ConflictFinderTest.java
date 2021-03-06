/*
 * Created on 03.03.2009
 * Copyright(c) 2009 tcmj.  All Rights Reserved.
 * @author TDEUT - Thomas Deutsch - 2009
 */
package com.tcmj.pm.conflicts.data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import com.tcmj.pm.conflicts.ConflictFinder;
import com.tcmj.pm.conflicts.bars.Bar;
import com.tcmj.pm.conflicts.bars.OutputBar;
import com.tcmj.pm.conflicts.bars.SimpleBar;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.tcmj.pm.junit.BarAssert.assertBar;
import static com.tcmj.pm.junit.BarAssert.assertConflict;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConflictFinderTest {

    /** Logging framework slf4j. */
    private static final Logger LOG = LoggerFactory.getLogger(ConflictFinderTest.class);

    /** Internal Dateformatter. */
    private static final DateFormat DFORM = new SimpleDateFormat("yyyy-MM-dd (H' Uhr')");


    @Before
    public void beforeEachTest() {
    }

    public static Date date(int year, int month, int day) {
        return date(year, month, day, 0);
    }
    public static Date date(int year, int month, int day, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, (month - 1));
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    /** internal helper method to display the in-/outputbars. */
    private void outputBarList(List<? extends Bar> barlist, char type) {
        String prefix;
        if (type == 'I' || type == 'i') {
            prefix = "Input";
        } else if (type == 'O' || type == 'o') {
            prefix = "Output";
        } else if (type == 'C' || type == 'c') {
            prefix = "Conflict";
        } else {
            prefix = "";
        }
        LOG.info("  >-outputBarList()------------------------------------------------------------>");
        for (Bar bar : barlist) {
            LOG.info("   {}-Bar : {}  to  {}  weight: {}", prefix, DFORM.format(bar.getStartDate()), DFORM.format(bar.getEndDate()), bar.getWeight());
        }
        LOG.info("  <----------------------------------------------------------------------------<");

    }


    @Test
    public void test_01_SetNameGetName() {
        LOG.info("test_01_SetNameGetName");
        //given
        ConflictFinder<Bar> cfinder = new ConflictFinder<>();
        assertThat("getName().initial", cfinder.getName(), equalTo("ConflictFinder"));
        //when a different name is set
        cfinder.setName("different");
        assertThat("getName().differnt", cfinder.getName(), equalTo("different"));
        //when a null name is set
        cfinder.setName(null);
        assertThat("getName().null", cfinder.getName(), nullValue());
    }


    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_02_calculate_empty_inputlist() {
        LOG.info("test_02_calculate_empty_inputlist");
        ConflictFinder<Bar> cfinder = new ConflictFinder<>();
        cfinder.setInputList(new ArrayList<>());
        cfinder.calculate();
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void test_02_calculate_null_inputlist() {
        LOG.info("test_02_calculate_null_inputlist");
        ConflictFinder<Bar> cfinder = new ConflictFinder<>();
        assertThat("getInputList().null", cfinder.getInputList(), nullValue());
        cfinder.calculate();
    }

    @Test
    public void test_03_SetInputlistGetInputlist() {
        LOG.info("test_03_SetInputlistGetInputlist");
        ConflictFinder<Bar> cfinder = new ConflictFinder<>();
        List<Bar> barlist = new ArrayList<>();
        cfinder.setInputList(barlist);
        assertThat("getInputList()", cfinder.getInputList(), is(barlist));
    }

    @Test
    public void test_00_calculate_null_inputlist() {
        LOG.info("test_00_calculate_null_inputlist");

    }

    @Test
    public void test_05_addToInputList_inputlist_not_initialized() {
        LOG.info("test_00_addToInputList_inputlist_not_initialized");

        //create some bars
        List<SimpleBar> barlist = new ArrayList<>();
        SimpleBar bar1 = new SimpleBar("1", date(2010, 1, 1), date(2010, 12, 31), 0.5);
        SimpleBar bar2 = new SimpleBar("2", date(2010, 5, 1), date(2010, 10, 31), 0.5);
        barlist.add(bar1);
        barlist.add(bar2);

        ConflictFinder<SimpleBar> cfinder = new ConflictFinder<>();

        cfinder.addToInputList(barlist);
        assertNotNull("getInputList must return a list object now", cfinder.getInputList());
        assertNotSame("barlist is not the same as the inputlist", barlist, cfinder.getInputList());
        cfinder.calculate();
        List<OutputBar> result = cfinder.getOutputBarListAll();
        outputBarList(result, 'o');
        assertEquals("expect 3 bars", 3, result.size());


        cfinder.addToInputList(barlist);
        assertEquals("expect 4 bars", 4, cfinder.getInputList().size());


    }


    @Test(expected = java.lang.NullPointerException.class)
    public void test_06_addToInputList_with_null_parameter() {
        LOG.info("test_00_addToInputList_with_null_parameter");
        ConflictFinder<Bar> cfinder = new ConflictFinder<>();
        List<Bar> nullList = null;

        cfinder.addToInputList(nullList);
    }


    @Test
    public void test_07_First_Testdata() {

        LOG.info("test_01_First_Testdata");

        List<Bar> barlist = new ArrayList<>();

        SimpleBar bar1 = new SimpleBar("1", date(2009, 1, 1), date(2009, 3, 31), 0.3);
        SimpleBar bar2 = new SimpleBar("2", date(2009, 3, 1), date(2009, 6, 30), 0.4);
        SimpleBar bar3 = new SimpleBar("3", date(2009, 6, 1), date(2009, 7, 31), 0.2);
        SimpleBar bar4 = new SimpleBar("4", date(2009, 2, 1), date(2009, 4, 30), 0.5);

        barlist.add(bar1);
        barlist.add(bar2);
        barlist.add(bar3);
        barlist.add(bar4);

        outputBarList(barlist, 'i');

        ConflictFinder<Bar> cfinder = new ConflictFinder<>();
        cfinder.setInputList(barlist);
        cfinder.calculate();

        //expect 7 bars
        List<OutputBar> result = cfinder.getOutputBarListAll();
        assertEquals("bars", 7, result.size());
        assertBar(result, 0, date(2009, 1, 1), date(2009, 2, 1), 0.3D);
        assertBar(result, 1, date(2009, 2, 1), date(2009, 3, 1), 0.8D);
        assertBar(result, 2, date(2009, 3, 1), date(2009, 3, 31), 1.2D);
        assertBar(result, 3, date(2009, 3, 31), date(2009, 4, 30), 0.9D);
        assertBar(result, 4, date(2009, 4, 30), date(2009, 6, 1), 0.4D);
        assertBar(result, 5, date(2009, 6, 1), date(2009, 6, 30), 0.6D);
        assertBar(result, 6, date(2009, 6, 30), date(2009, 7, 31), 0.2D);


        //expect one conflict
        List<OutputBar> resultC = cfinder.getOutputConflictList();
        assertEquals("conflicts", 1, resultC.size());
        assertConflict(resultC, 0, date(2009, 3, 1), date(2009, 3, 31), 1.2D);


    }


    @Test
    public void test_08_NoOverlapping_NoConflicts() {

        LOG.info("test_02_NoOverlapping_NoConflicts");

        List<Bar> barlist = new ArrayList<>();

        SimpleBar bar1 = new SimpleBar("1", date(2009, 1, 1), date(2009, 1, 30), 1.0);
        SimpleBar bar2 = new SimpleBar("2", date(2009, 3, 1), date(2009, 3, 30), 1.0);
        SimpleBar bar3 = new SimpleBar("3", date(2009, 5, 1), date(2009, 5, 30), 1.0);
        barlist.add(bar1);
        barlist.add(bar2);
        barlist.add(bar3);
        outputBarList(barlist, 'i');

        ConflictFinder<Bar> cfinder = new ConflictFinder<>();
        cfinder.setInputList(barlist);
        cfinder.calculate();

        //expect no conflict
        List<OutputBar> resultC = cfinder.getOutputConflictList();
        assertEquals("conflicts", 0, resultC.size());

        List<OutputBar> result = cfinder.getOutputBarListAll();
        assertEquals("bars", 3, result.size());
        assertBar(result, 0, date(2009, 1, 1), date(2009, 1, 30), 1D);
        assertBar(result, 1, date(2009, 3, 1), date(2009, 3, 30), 1D);
        assertBar(result, 2, date(2009, 5, 1), date(2009, 5, 30), 1D);
    }


    @Test
    public void test_09_OnlyOneConflictTask() {

        LOG.info("test_03_OnlyOneConflictTask");

        //Nur 1 Task der als Konflikt ausgegeben werden muss (130%WL)

        List<Bar> barlist = new ArrayList<>();
        SimpleBar bar1 = new SimpleBar("1", date(2009, 1, 1), date(2009, 1, 10), 1.3);
        barlist.add(bar1);
        outputBarList(barlist, 'i');
        ConflictFinder<Bar> cfinder = new ConflictFinder<>();
        cfinder.setInputList(barlist);
        cfinder.calculate();

        List<OutputBar> resultC = cfinder.getOutputConflictList();
        assertEquals("conflicts", 1, resultC.size());

        assertConflict(resultC, 0, date(2009, 1, 1), date(2009, 1, 10), 1.3D);


        List<OutputBar> result = cfinder.getOutputBarListAll();
        assertEquals("bars", 1, result.size());
        assertBar(result, 0, date(2009, 1, 1), date(2009, 1, 10), 1.3D);
        outputBarList(result, 'b');
        outputBarList(resultC, 'c');
    }


    @Test
    public void test_10_OnlyOneNormalTask() {

        LOG.info("test_04_OnlyOneNormalTask");
        //Nur 1 Task der nicht als Konflikt ausgegeben werden muss (100%WL)

        //-------Fall 1 mit exakt 1.0

        List<Bar> barlist = new ArrayList<>();
        SimpleBar bar1 = new SimpleBar("1", date(2009, 1, 1), date(2009, 1, 10), 1.0);
        barlist.add(bar1);
        outputBarList(barlist, 'i');
        ConflictFinder<Bar> cfinder = new ConflictFinder<>();
        cfinder.setInputList(barlist);
        cfinder.calculate();

        List<OutputBar> resultC = cfinder.getOutputConflictList();
        assertEquals("Fall 1 mit exakt 1.0: conflicts", 0, resultC.size());


        List<OutputBar> result = cfinder.getOutputBarListAll();
        assertEquals("Fall 1 mit exakt 1.0: bars", 1, result.size());
        assertBar(result, 0, date(2009, 1, 1), date(2009, 1, 10), 1.0D);


        //-------Fall 2 mit weniger als 0.5

        List<Bar> barlist2 = new ArrayList<>();
        SimpleBar bar21 = new SimpleBar("1", date(2009, 1, 1), date(2009, 1, 10), 0.3);
        barlist2.add(bar21);
        outputBarList(barlist2, 'i');
        ConflictFinder<Bar> cfinder2 = new ConflictFinder<>();
        cfinder2.setInputList(barlist2);
        cfinder2.calculate();
        List<OutputBar> resultC2 = cfinder2.getOutputConflictList();
        assertEquals("Fall 2 mit weniger als 0.5: conflicts", 0, resultC2.size());

        List<OutputBar> result2 = cfinder2.getOutputBarListAll();
        assertEquals("Fall 2 mit weniger als 0.5: bars", 1, result2.size());
        assertBar(result2, 0, date(2009, 1, 1), date(2009, 1, 10), 0.3D);


    }


    @Test
    public void test_11_TwoEqualBarsAsConflict() {


        LOG.info("test_05_TwoEqualBarsAsConflict");
        //2 Exakt gleich lange Buchungen mit je 1.0

        List<Bar> barlist = new ArrayList<>();
        SimpleBar bar1 = new SimpleBar("1", date(2009, 1, 1), date(2009, 1, 30), 1.0);
        SimpleBar bar2 = new SimpleBar("2", date(2009, 1, 1), date(2009, 1, 30), 1.0);
        barlist.add(bar1);
        barlist.add(bar2);
        outputBarList(barlist, 'i');

        ConflictFinder<Bar> cfinder = new ConflictFinder<>();
        cfinder.setInputList(barlist);
        cfinder.calculate();
        List<OutputBar> resultC = cfinder.getOutputConflictList();
        assertEquals("conflicts", 1, resultC.size());


        assertConflict(resultC, 0, date(2009, 1, 1), date(2009, 1, 30), 2.0D);


        List<OutputBar> result = cfinder.getOutputBarListAll();
        assertEquals("bars", 1, result.size());
        assertBar(result, 0, date(2009, 1, 1), date(2009, 1, 30), 2.0D);

    }


    @Test
    public void test_12_SameStartDatesDifferentEndDates() {

        LOG.info("test_06_SameStartDatesDifferentEndDates");
        //Gleiche Start- andere EndTermine

        List<Bar> barlist = new ArrayList<>();

        SimpleBar bar1 = new SimpleBar("1", date(2009, 10, 1), date(2009, 10, 5), 0.5);
        SimpleBar bar2 = new SimpleBar("2", date(2009, 10, 1), date(2009, 10, 10), 0.3);
        SimpleBar bar3 = new SimpleBar("3", date(2009, 10, 1), date(2009, 10, 15), 0.2);
        SimpleBar bar4 = new SimpleBar("4", date(2009, 10, 1), date(2009, 10, 20), 0.1);

        barlist.add(bar1);
        barlist.add(bar2);
        barlist.add(bar3);
        barlist.add(bar4);

        outputBarList(barlist, 'i');
        ConflictFinder<Bar> cfinder = new ConflictFinder<>();
        cfinder.setInputList(barlist);
        cfinder.calculate();
        //expect one conflict
        List<OutputBar> resultC = cfinder.getOutputConflictList();
        outputBarList(resultC, 'c');
        assertEquals("conflicts", 1, resultC.size());


        assertConflict(resultC, 0, date(2009, 10, 1), date(2009, 10, 5), 1.1D);

        //expect 7 bars
        List<OutputBar> result = cfinder.getOutputBarListAll();
        assertEquals("bars", 4, result.size());
        assertBar(result, 0, date(2009, 10, 1), date(2009, 10, 5), 1.1D);
        assertBar(result, 1, date(2009, 10, 5), date(2009, 10, 10), 0.6D);
        assertBar(result, 2, date(2009, 10, 10), date(2009, 10, 15), 0.3D);
        assertBar(result, 3, date(2009, 10, 15), date(2009, 10, 20), 0.1D);
        outputBarList(result, 'o');


    }


    @Test
    public void test_13_TimeTest() {


        LOG.info("test_07_TimeTest");
        //Problemprojekt mit Uhrzeit (88% Resource)

        List<Bar> barlist = new ArrayList<>();

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
        outputBarList(barlist, 'i');

        ConflictFinder<Bar> cfinder = new ConflictFinder<>();
        cfinder.setMaxAllowedWeight(0.88);
        cfinder.setInputList(barlist);
        cfinder.calculate();
        //expect no conflict
        List<OutputBar> resultC = cfinder.getOutputConflictList();
        assertEquals("conflicts", 1, resultC.size());
        assertConflict(resultC, 0, date(2009, 3, 18, 8), date(2009, 3, 19, 16), 1.76D);


        List<OutputBar> result = cfinder.getOutputBarListAll();
        assertEquals("bars", 4, result.size());
        assertBar(result, 0, date(2009, 3, 16, 8), date(2009, 3, 18, 8), 0.88D);
        assertBar(result, 1, date(2009, 3, 18, 8), date(2009, 3, 19, 16), 1.76D);
        assertBar(result, 2, date(2009, 3, 19, 16), date(2009, 3, 20, 16), 0.88D);
        assertBar(result, 3, date(2009, 3, 23, 8), date(2009, 3, 27, 16), 0.88D);


    }


    @Test
    public void test_14_ClassTest() {


        LOG.info("test_08_ClassTest");
        //Verwendungsmoeglichkeit der Klasse

        ConflictFinder<Bar> cfinder = new ConflictFinder<>();

        cfinder.addToInputList(new SimpleBar("TD1", date(2009, 3, 10), date(2009, 3, 20), 1.0));
        cfinder.addToInputList(new SimpleBar("TD1", date(2009, 3, 16), date(2009, 3, 30), 1.0));
        cfinder.calculate();

        //expect no conflict
        List<OutputBar> resultC = cfinder.getOutputConflictList();
        assertEquals("conflicts", 1, resultC.size());
        assertConflict(resultC, 0, date(2009, 3, 16), date(2009, 3, 20), 2.0D);


        List<OutputBar> result = cfinder.getOutputBarListAll();
        assertEquals("bars", 3, result.size());
        assertBar(result, 0, date(2009, 3, 10), date(2009, 3, 16), 1.0D);
        assertBar(result, 1, date(2009, 3, 16), date(2009, 3, 20), 2.0D);
        assertBar(result, 2, date(2009, 3, 20), date(2009, 3, 30), 1.0D);


        System.out.println("");
    }


    @Test
    public void test_15_MaxAllowedWeightTest() {

        LOG.info("test_09_MaxAllowedWeightTest");
        //Maximal erlaubt (50% Resource)

        List<Bar> barlist = new ArrayList<>();
        ConflictFinder<Bar> cfinder = new ConflictFinder<>();
        cfinder.setPrecision(new Precision(10000000000L));
        cfinder.setMaxAllowedWeight(0.50);
        SimpleBar bar1 = new SimpleBar("TD1", date(2009, 3, 10), date(2009, 3, 20), 0.50);
        SimpleBar bar2 = new SimpleBar("TD2", date(2009, 3, 15), date(2009, 3, 25), 0.50);

        barlist.add(bar1);
        barlist.add(bar2);
        cfinder.setInputList(barlist);
        outputBarList(barlist, 'i');

        cfinder.calculate();

        //expect no conflict
        List<OutputBar> resultC = cfinder.getOutputConflictList();
        assertEquals("conflictsA", 1, resultC.size());

        assertConflict(resultC, 0, date(2009, 3, 15), date(2009, 3, 20), 1.0D);


        List<OutputBar> result = cfinder.getOutputBarListAll();
        assertEquals("barsA", 3, result.size());
        assertBar(result, 0, date(2009, 3, 10), date(2009, 3, 15), 0.5D);
        assertBar(result, 1, date(2009, 3, 15), date(2009, 3, 20), 1.0D);
        assertBar(result, 2, date(2009, 3, 20), date(2009, 3, 25), 0.5D);

        //Same Input but instead of 50% UPT --> 100% UPT
        cfinder.setMaxAllowedWeight(1.0);
        cfinder.calculate();
        assertEquals("conflictsB", 0, cfinder.getOutputConflictList().size());


        List<OutputBar> resultB = cfinder.getOutputBarListAll();
        assertEquals("barsB", 3, resultB.size());
        assertBar(resultB, 0, date(2009, 3, 10), date(2009, 3, 15), 0.5D);
        assertBar(resultB, 1, date(2009, 3, 15), date(2009, 3, 20), 1.0D);
        assertBar(resultB, 2, date(2009, 3, 20), date(2009, 3, 25), 0.5D);


    }


    @Test
    public void test_16_InternalCalculations() {

        LOG.info("test_10_InternalCalculations");

        ConflictFinder<Bar> cfinder = new ConflictFinder<>();
        cfinder.setMaxAllowedWeight(1.5);
        double maw = cfinder.getMaxAllowedWeight();

        assertEquals(1.5, maw, 0);

//        cfinder.setPrecision(Precision.FOUR_DIGITS);
        assertEquals(5000L, Precision.FOUR_DIGITS.double2Long(0.5));

        assertEquals(11235L, Precision.FOUR_DIGITS.double2Long(1.123456789D));


    }


    @Test
    public void test_17_DataIntegrity1_Testdata() {


        LOG.info("test_11_DataIntegrity1_Testdata");
        //Testdata Datenintegritaet

        List<Bar> barlist = new ArrayList<>();

        barlist.add(new SimpleBar("1", date(2009, 1, 1), date(2009, 3, 31), 0.3));
        barlist.add(new SimpleBar("2", date(2009, 3, 1), date(2009, 6, 30), 0.4));
        barlist.add(new SimpleBar("3", date(2009, 6, 1), date(2009, 7, 31), 0.2));
        barlist.add(new SimpleBar("4", date(2009, 2, 1), date(2009, 4, 30), 0.5));
        outputBarList(barlist, 'i');
        ConflictFinder<Bar> cfinder = new ConflictFinder<>();
        cfinder.setInputList(barlist);
        cfinder.calculate();

        List<OutputBar> result = cfinder.getOutputBarListAll();
        int loopct = 1;
        for (Iterator<OutputBar> it = result.iterator(); it.hasNext(); loopct++) {
            Bar bar = it.next();

            switch (loopct) {

                case 1:
                    assertEquals("Bar" + loopct + "-Weight", 0.3, bar.getWeight(), 0);
                    break;
                case 2:
                    assertEquals("Bar" + loopct + "-Weight", 0.8, bar.getWeight(), 0);
                    break;
                case 3:
                    assertEquals("Bar" + loopct + "-Weight", 1.2, bar.getWeight(), 0);
                    break;
                case 4:
                    assertEquals("Bar" + loopct + "-Weight", 0.9, bar.getWeight(), 0);
                    break;
                case 5:
                    assertEquals("Bar" + loopct + "-Weight", 0.4, bar.getWeight(), 0);
                    break;
                case 6:
                    assertEquals("Bar" + loopct + "-Weight", 0.6, bar.getWeight(), 0);
                    break;
                case 7:
                    assertEquals("Bar" + loopct + "-Weight", 0.2, bar.getWeight(), 0);
                    break;

            }


        }

        assertEquals("bars", 7, result.size());

        //expect one conflict
        List<OutputBar> resultC = cfinder.getOutputConflictList();
        assertEquals("conflicts", 1, resultC.size());

        for (OutputBar conflictBar : resultC) {
            //only 1 loop!!!
            assertEquals("AmountOfCausingBars", 3, conflictBar.getCausingBars().size());
            assertEquals("StartDate", date(2009, 3, 1), conflictBar.getStartDate());
            assertEquals("EndDate", date(2009, 3, 31), conflictBar.getEndDate());
//            assertEquals("Type", 99, conflictBar.getProperty());
            assertEquals("Weight", 1.2, conflictBar.getWeight(), 0);
        }


    }


    @Test
    public void test_18_DataIntegrity2_Bizimis_Case() {

        LOG.info("test_12_DataIntegrity2_Bizimis_Case");
        //Spezieller Praxis-Sonderfall Datenintegritaet

        List<Bar> barlist = new ArrayList<>();

        int type_emp = 33, type_shift = 50;

        /*           EE    (10. - 11.)
         *  PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP  (01. - 31.)         */

        SimpleBar bar1 = new SimpleBar("Employee", date(2009, 1, 10), date(2009, 1, 11), 1.0);
        bar1.setProperty("type", type_emp);
        barlist.add(bar1);
        SimpleBar bar2 = new SimpleBar("Project", date(2009, 1, 1), date(2009, 1, 31), 1.0);
        bar2.setProperty("type", type_shift);
        barlist.add(bar2);
        outputBarList(barlist, 'i');
        ConflictFinder<Bar> cfinder = new ConflictFinder<>();
        cfinder.setInputList(barlist);
        cfinder.calculate();

        List<OutputBar> result = cfinder.getOutputBarListAll();
        int loopct = 1;
        for (Iterator<OutputBar> it = result.iterator(); it.hasNext(); loopct++) {
            Bar bar = it.next();

            switch (loopct) {

                case 1:
                    assertEquals("Bar" + loopct + "-Start", date(2009, 1, 1), bar.getStartDate());
                    assertEquals("Bar" + loopct + "-End", date(2009, 1, 10), bar.getEndDate());
                    assertEquals("Bar" + loopct + "-Weight", 1.0, bar.getWeight(), 0);
//                    System.out.println("Bar" + loopct + "-Type = " + bar.getProperty("type"));
                    assertEquals("Bar" + loopct + "-Type", type_shift, bar.getProperty("type"));
                    break;
                case 2:
                    assertEquals("Bar" + loopct + "-Start", date(2009, 1, 10), bar.getStartDate());
                    assertEquals("Bar" + loopct + "-End", date(2009, 1, 11), bar.getEndDate());
                    assertEquals("Bar" + loopct + "-Weight", 2.0, bar.getWeight(), 0);
//                    System.out.println("Bar" + loopct + "-Type = " + bar.getProperty("type"));
                    break;
                case 3:
                    assertEquals("Bar" + loopct + "-Start", date(2009, 1, 11), bar.getStartDate());
                    assertEquals("Bar" + loopct + "-End", date(2009, 1, 31), bar.getEndDate());

                    assertEquals("Bar" + loopct + "-Weight", 1.0, bar.getWeight(), 0);

//                    System.out.println("Bar" + loopct + "-Type = " + bar.getProperty("type"));
                    assertEquals("Bar" + loopct + "-Type", type_shift, bar.getProperty("type"));
                    break;


            }


        }

        assertEquals("bars", 3, result.size());

        //expect one conflict
        List<OutputBar> resultC = cfinder.getOutputConflictList();
        assertEquals("conflicts", 1, resultC.size());

        assertConflict(resultC, 0, date(2009, 1, 10), date(2009, 1, 11), 2.0D);


    }


    @Test
    public void test_19_DataIntegrity3_Four_Same_Bars_Test() {

        LOG.info("test_13_DataIntegrity3_Four_Same_Bars_Test");

        List<Bar> barlist = new ArrayList<>();

        barlist.add(new SimpleBar("1", date(2009, 1, 1), date(2009, 1, 30), 1.0));
        barlist.add(new SimpleBar("2", date(2009, 1, 1), date(2009, 1, 30), 1.0));
        barlist.add(new SimpleBar("3", date(2009, 1, 1), date(2009, 1, 30), 1.0));
        barlist.add(new SimpleBar("4", date(2009, 1, 1), date(2009, 1, 30), 1.0));
        outputBarList(barlist, 'i');
        ConflictFinder<Bar> cfinder = new ConflictFinder<>();
        cfinder.setInputList(barlist);
        cfinder.calculate();
        assertEquals("bars", 1, cfinder.getOutputBarListAll().size());

        // ?????????????????????????????????????????????
        // ??????????  TODO 5 or 1 conflict ????????????
        // ?????????????????????????????????????????????

        List<OutputBar> resultC = cfinder.getOutputConflictList();
        assertEquals("conflicts", 1, resultC.size());

        assertConflict(resultC, 0, date(2009, 1, 1), date(2009, 1, 30), 4.0D);


        assertBar(cfinder.getOutputBarListAll(), 0, date(2009, 1, 1), date(2009, 1, 30), 4.0D);


    }


    @Test
    public void test_20_SonderfallTest() {

        LOG.info("test_14_SonderfallTest");

        List<Bar> barlist = new ArrayList<>();

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
        outputBarList(barlist, 'i');


        ConflictFinder<Bar> cfinder = new ConflictFinder<>();
        cfinder.setInputList(barlist);
        cfinder.calculate();
        //expect no conflict
        List<OutputBar> resultC = cfinder.getOutputConflictList();
        assertEquals("conflicts", 3, resultC.size());
        assertConflict(resultC, 0, date(2009, 4, 12), date(2009, 4, 14), 2.0D);
        assertConflict(resultC, 1, date(2009, 4, 14), date(2009, 4, 15), 3.0D);
        assertConflict(resultC, 2, date(2009, 4, 15), date(2009, 5, 3), 2.0D);


        List<OutputBar> result = cfinder.getOutputBarListAll();
        assertEquals("bars", 5, result.size());
        assertBar(result, 0, date(2009, 1, 27), date(2009, 4, 12), 1D);
        assertBar(result, 1, date(2009, 4, 12), date(2009, 4, 14), 2.0D);
        assertBar(result, 2, date(2009, 4, 14), date(2009, 4, 15), 3.0D);
        assertBar(result, 3, date(2009, 4, 15), date(2009, 5, 3), 2.0D);
        assertBar(result, 4, date(2009, 5, 3), date(2009, 5, 29), 1D);

    }


    @Test
    public void test_21_SonderfallTest2() {


        LOG.info("test_15_SonderfallTest2");

        List<Bar> barlist = new ArrayList<>();

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
        outputBarList(barlist, 'i');


        ConflictFinder<Bar> cfinder = new ConflictFinder<>();
        cfinder.setInputList(barlist);
        cfinder.calculate();
        //expect no conflict
        List<OutputBar> resultC = cfinder.getOutputConflictList();
        assertEquals("conflicts", 1, resultC.size());
        assertConflict(resultC, 0, date(2009, 3, 23, 8), date(2009, 3, 27, 17), 1.2D);


        List<OutputBar> result = cfinder.getOutputBarListAll();
        assertBar(result, 0, date(2009, 3, 23, 8), date(2009, 3, 27, 17), 1.2D);
        assertBar(result, 1, date(2009, 3, 27, 17), date(2009, 4, 14, 10), 0.2D);


        assertEquals("bars", 2, result.size());
    }


    @Test
    public void test_22_SonderfallTest3() {


        LOG.info("test_16_SonderfallTest3");

        List<Bar> barlist = new ArrayList<>();
        barlist.add(new SimpleBar("D1", date(2009, 1, 1, 0), date(2009, 1, 1, 24), 0.8));
        barlist.add(new SimpleBar("D2", date(2009, 1, 1, 0), date(2009, 1, 1, 24), 0.8));
        barlist.add(new SimpleBar("D1", date(2009, 1, 2, 0), date(2009, 1, 2, 24), 0.8));
        barlist.add(new SimpleBar("D2", date(2009, 1, 2, 0), date(2009, 1, 2, 24), 0.8));

        outputBarList(barlist, 'i');

        ConflictFinder<Bar> cfinder = new ConflictFinder<>();
        cfinder.setInputList(barlist);
        cfinder.calculate();

        //expect 7 bars
        List<OutputBar> result = cfinder.getOutputBarListAll();
        assertEquals("bars", 2, result.size());
        assertBar(result, 0, date(2009, 1, 1, 0), date(2009, 1, 1, 24), 1.6D);
        assertBar(result, 1, date(2009, 1, 2, 0), date(2009, 1, 2, 24), 1.6D);


        //expect one conflict
        List<OutputBar> resultC = cfinder.getOutputConflictList();
        assertEquals("conflicts", 2, resultC.size());
        assertConflict(resultC, 0, date(2009, 1, 1, 0), date(2009, 1, 1, 24), 1.6D);
        assertConflict(resultC, 1, date(2009, 1, 2, 0), date(2009, 1, 2, 24), 1.6D);


    }


    @Test
    public void test_23_MassTest() {


        LOG.info("test_17_MassTest");
        //2 Exakt gleich lange Buchungen mit je 1.0

        List<Bar> barlist = new ArrayList<>();

        double amt = 500;

        for (int i = 1; i <= amt; i++) {

            SimpleBar bar1 = new SimpleBar("1", date(2009, 1, 1), date(2009, 1, 30), 1.0);
            barlist.add(bar1);

        }


        ConflictFinder<Bar> cfinder = new ConflictFinder<>();
        cfinder.setInputList(barlist);
        cfinder.calculate();

        List<OutputBar> resultC = cfinder.getOutputConflictList();
        assertEquals("conflicts", 1, resultC.size());
        assertConflict(resultC, 0, date(2009, 1, 1), date(2009, 1, 30), amt);


        List<OutputBar> result = cfinder.getOutputBarListAll();
        assertEquals("bars", 1, result.size());
        assertBar(result, 0, date(2009, 1, 1), date(2009, 1, 30), amt);

    }
}
