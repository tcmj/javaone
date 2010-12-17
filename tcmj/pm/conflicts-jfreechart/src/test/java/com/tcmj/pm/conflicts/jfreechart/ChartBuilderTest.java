package com.tcmj.pm.conflicts.jfreechart;

import com.tcmj.pm.conflicts.ConflictFinder;
import com.tcmj.pm.conflicts.bars.Bar;
import com.tcmj.pm.conflicts.bars.OutputBar;
import com.tcmj.pm.conflicts.bars.SimpleBar;
import java.util.ArrayList;
import java.util.List;
import static junit.framework.Assert.*;
import org.jfree.chart.JFreeChart;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.tcmj.common.tools.date.DateTool.date;
import static com.tcmj.common.tools.date.DateTool.formatDate;


/**
 *
 * @author Administrator
 */
public class ChartBuilderTest {

    protected static final Logger logger = LoggerFactory.getLogger(ChartBuilderTest.class);
    ;

    public ChartBuilderTest() {
    }



    @Test
    public void testBars1() {


        logger.info("\n\n....testBars1\n");

        List<Bar> barlist = new ArrayList();

        SimpleBar bar1 = new SimpleBar("1", date(2009, 1, 1), date(2009, 3, 31), 0.3);
        SimpleBar bar2 = new SimpleBar("2", date(2009, 3, 1), date(2009, 6, 30), 0.4);
        SimpleBar bar3 = new SimpleBar("3", date(2009, 6, 1), date(2009, 7, 31), 0.2);
        SimpleBar bar4 = new SimpleBar("4", date(2009, 2, 1), date(2009, 4, 30), 0.5);

        barlist.add(bar1);
        barlist.add(bar2);
        barlist.add(bar3);
        barlist.add(bar4);
        outputBars(barlist);
        ConflictFinder cfinder = new ConflictFinder();
        cfinder.setInputList(barlist);
        cfinder.calculate();
        //expect 7 bars
        List<OutputBar> result = cfinder.getOutputBarListAll();
        assertEquals("bars", 7, result.size());
//outputBars(result);
        //expect one conflict
        List<OutputBar> resultC = cfinder.getOutputConflictList();
        assertEquals("conflicts", 1, resultC.size());




        ChartBuilder cb = new ChartBuilder(cfinder);

        JFreeChart jf = cb.createChart();





    }

    public void outputBars(List<Bar> barlist) {

        int i = 0;
        for (Bar bar : barlist) {
            i++;

            logger.debug("Bar-" + i + ": " + formatDate(bar.getStartDate()) +
                    " to: " + formatDate(bar.getEndDate()));


        }

    }
}