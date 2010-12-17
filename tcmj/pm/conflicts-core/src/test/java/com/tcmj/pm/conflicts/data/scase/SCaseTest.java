/*
 * Created on 03.05.2009
 * @author TDEUT - Thomas Deutsch - 2009
 */
package com.tcmj.pm.conflicts.data.scase;

import com.tcmj.pm.conflicts.bars.Bar;
import com.tcmj.pm.conflicts.ConflictFinder;
import com.tcmj.pm.conflicts.bars.OutputBar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author tdeut
 */
public class SCaseTest {

    /** Logging framework. */
    protected static final transient Logger logger = LoggerFactory.getLogger("com.tcmj.pm.conflicts");

    /** Internal Dateformatter. */
    private static final DateFormat DFORM = new SimpleDateFormat("yyyy-MM-dd (H' Uhr')");


    public SCaseTest() {
    }

//    @BeforeClass
//    public static final void log4jconfig() {
//            PatternLayout pl = new PatternLayout(" %m %n");
//            ConsoleAppender console = new ConsoleAppender(pl);
//            console.setName("unique");
//            logger.addAppender(console);
//
//    }

    public static void setUpClass() throws Exception {
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    private void outputBarList(List<Bar> barlist) {
        logger.debug("------------------------------------------------------------------------------");
        for (Bar bar : barlist) {
            logger.debug("Input : " + DFORM.format(bar.getStartDate()) + "  to  "
                    + DFORM.format(bar.getEndDate()) + "  weight: " + bar.getWeight());
        }
        logger.debug("------------------------------------------------------------------------------");

    }


    public void assertBar(List<OutputBar> result, int index, Date start, Date end, double pct) {
        assertEquals("B" + index + "-Start", start, result.get(index).getStartDate());
        assertEquals("B" + index + "-End", end, result.get(index).getEndDate());
        assertEquals("B" + index + "-Pct", pct, result.get(index).getWeight(), 0D);
    }


    @Test
    public void test_01_STestcase() {

        logger.info("\n\n....test_01_STestcase....\n");

        List<Bar> barlist = new ArrayList<Bar>();

        SpecialBar bar1 = new SpecialBar("1", date(2009, 1, 1), date(2009, 3, 31), 1.0);
        bar1.setProperty("description", "Januar bis M�rz");
        bar1.setProperty("color", "green");
        barlist.add(bar1);

        SpecialBar bar2 = new SpecialBar("2", date(2009, 3, 1), date(2009, 6, 30), 1.0);
        bar2.setProperty("description", "M�rz bis Juni");
        bar1.setProperty("color", "red");
        barlist.add(bar2);


        outputBarList(barlist);

        ConflictFinder cfinder = new ConflictFinder();
        cfinder.setInputList(barlist);
        cfinder.calculate();

        //expect 3 bars
        List<OutputBar> result = cfinder.getOutputBarListAll();
        assertEquals("bars", 3, result.size());
        assertBar(result, 0, date(2009, 1, 1), date(2009, 3, 1), 1D);
        assertBar(result, 1, date(2009, 3, 1), date(2009, 3, 31), 2D);
        assertBar(result, 2, date(2009, 3, 31), date(2009, 6, 30), 1D);



        //expect one conflict 
        List<OutputBar> resultC = cfinder.getOutputConflictList();
        assertEquals("conflicts", 1, resultC.size());
        assertBar(result, 1, date(2009, 3, 1), date(2009, 3, 31), 2D);

        OutputBar conflictbar = resultC.get(0);

        Bar bar = conflictbar.getCausingBars().get(0);

        String desc = (String) conflictbar.getCausingBars().get(0).getProperty("description");

        assertEquals("description", "Januar bis M�rz", desc);



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
