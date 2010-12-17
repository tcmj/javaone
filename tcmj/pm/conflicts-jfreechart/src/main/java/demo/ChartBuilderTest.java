package demo;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JPanel;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tcmj.pm.conflicts.ConflictFinder;
import com.tcmj.pm.conflicts.bars.Bar;
import com.tcmj.pm.conflicts.bars.OutputBar;
import com.tcmj.pm.conflicts.bars.SimpleBar;
import com.tcmj.pm.conflicts.jfreechart.ChartBuilder;
import static com.tcmj.common.tools.date.DateTool.date;
import static com.tcmj.common.tools.date.DateTool.formatDate;

/**
 *
 * @author Administrator
 */
public class ChartBuilderTest extends ApplicationFrame {

    protected static final Logger logger = LoggerFactory.getLogger(ChartBuilderTest.class);
    protected static final Logger logger2 = LoggerFactory.getLogger(ConflictFinder.class);

    public ChartBuilderTest(String paramString) {
        super(paramString);
        JPanel localJPanel = createDemoPanel();
        localJPanel.setPreferredSize(new Dimension(800, 300));
        setContentPane(localJPanel);

        
        

    }






    public JPanel createDemoPanel() {
        return new ChartPanel(createChart());
    }

    protected List<Bar> createInputList1() {
        List<Bar> barlist = new ArrayList();


        SimpleBar bar1 = new SimpleBar("Project A", date(2009, 3, 16, 8), date(2009, 3, 19, 16), 0.5);
//        bar1.setOrigin(this);

        SimpleBar bar2 = new SimpleBar("Project B", date(2009, 3, 17, 8), date(2009, 3, 20, 16), 0.5);

        SimpleBar bar3 = new SimpleBar("Urlaub", date(2009, 3, 19, 8), date(2009, 3, 19, 16), 1.0);
        bar3.setProperty("type", 20);


        SimpleBar bar4 = new SimpleBar("TD4", date(2009, 3, 22, 8), date(2009, 3, 22, 16), 0.5);

        barlist.add(bar1);
        barlist.add(bar2);
        barlist.add(bar3);
        barlist.add(bar4);


        return barlist;

    }

//    protected static Date date(int y, int m, int d) {
//        return date(y, m, d, 0, 0, 0);
//    }

    protected static Date dateS(int y, int m, int d) {
        return date(y, m, d, 8, 0, 0);
    }

    protected static Date dateE(int y, int m, int d) {
        return date(y, m, d, 17, 0, 0);
    }

//    protected static Date date(int y, int m, int d, int h) {
//
//        return date(y, m, d, h, 0, 0);
//    }

    protected List<Bar> createInputList() {

        return createInputList5();


    }

    protected List<Bar> createInputList5() {
        List<Bar> barlist = new ArrayList();

        barlist.add(new SimpleBar("D1", date(2009, 1, 1, 0), date(2009, 1, 1, 24), 0.8));
        barlist.add(new SimpleBar("D2", date(2009, 1, 1, 0), date(2009, 1, 1, 24), 0.8));
        barlist.add(new SimpleBar("D1", date(2009, 1, 2, 0), date(2009, 1, 2, 24), 0.8));
        barlist.add(new SimpleBar("D2", date(2009, 1, 2, 0), date(2009, 1, 2, 24), 0.8));

//        barlist.add(new SimpleBar("D2", dateS(2009, 1, 1), dateE(2009, 1, 1), 0.8));
//        barlist.add(new SimpleBar("D3", dateS(2009, 1, 2), dateE(2009, 1, 2), 0.8));
//        barlist.add(new SimpleBar("D4", dateS(2009, 1, 2), dateE(2009, 1, 2), 0.8));


        SimpleBar bar1 = new SimpleBar("A1", date(2009, 1, 1,0), date(2009, 1, 2,24), 0.3);
        SimpleBar bar2 = new SimpleBar("B2", date(2009, 1, 1,0), dateE(2009, 1, 5), 0.4);
//        bar2.setType(10);
        SimpleBar bar3 = new SimpleBar("C3", dateS(2009, 1, 4), dateE(2009, 1, 5), 0.2);
//        bar3.setType(20);
        SimpleBar bar4 = new SimpleBar("D4", dateS(2009, 1, 5), dateE(2009, 1, 5), 0.5);


        barlist.add(bar1);
        barlist.add(bar2);
        barlist.add(bar3);
        barlist.add(bar4);
        return barlist;

    }

    protected List<Bar> createInputList2() {
        List<Bar> barlist = new ArrayList();

        SimpleBar bar1 = new SimpleBar("A1", dateS(2009, 1, 1), dateE(2009, 3, 31), 0.3);
        SimpleBar bar2 = new SimpleBar("B2", dateS(2009, 3, 1), dateE(2009, 6, 30), 0.4);
//        bar2.setType(10);
        SimpleBar bar3 = new SimpleBar("C3", dateS(2009, 6, 1), dateE(2009, 7, 31), 0.2);
//        bar3.setType(20);
        SimpleBar bar4 = new SimpleBar("D4", dateS(2009, 2, 1), dateE(2009, 4, 30), 0.5);


        barlist.add(bar1);
        barlist.add(bar2);
        barlist.add(bar3);
        barlist.add(bar4);
        return barlist;

    }

    protected List<Bar> createInputList3() {
        List<Bar> barlist = new ArrayList();

        SimpleBar bar1 = new SimpleBar("94891", date(2009, 3, 23, 8), date(2009, 3, 27, 17), 1.0);
        SimpleBar bar2 = new SimpleBar("95588", date(2009, 3, 23, 8), date(2009, 4, 14, 10), 0.1);
        SimpleBar bar3 = new SimpleBar("77372", date(2009, 3, 23, 8), date(2009, 4, 14, 10), 0.1);


        barlist.add(bar1);
        barlist.add(bar2);
        barlist.add(bar3);
        return barlist;

    }

    protected List<Bar> createInputList4() {
        List<Bar> barlist = new ArrayList();

        barlist.add(new SimpleBar("1", date(2009, 1, 1), date(2009, 1, 30), 1.0));
        barlist.add(new SimpleBar("2", date(2009, 1, 1), date(2009, 1, 30), 1.0));
        barlist.add(new SimpleBar("3", date(2009, 1, 1), date(2009, 1, 30), 1.0));
        barlist.add(new SimpleBar("4", date(2009, 1, 1), date(2009, 1, 30), 1.0));

        return barlist;

    }

    protected ConflictFinder createConflictFinder() {

        List<Bar> barlist = createInputList();

        outputBars(barlist);
        ConflictFinder cfinder = new ConflictFinder();
        cfinder.setInputList(barlist);
        cfinder.calculate();

        List<OutputBar> result = cfinder.getOutputBarListAll();
        //outputBars(result);

        List<OutputBar> resultC = cfinder.getOutputConflictList();

        return cfinder;

    }

    public JFreeChart createChart() {


        ConflictFinder cfinder = createConflictFinder();

        ChartBuilder cb = new ChartBuilder(cfinder);




        JFreeChart jf = cb.createChart();


        return jf;


    }

    public void outputBars(List<Bar> barlist) {

        int i = 0;
        for (Bar bar : barlist) {
            i++;

            logger.debug("Bar-" + i + ": " + formatDate(bar.getStartDate()) +
                    " to: " + formatDate(bar.getEndDate()));


        }

    }

    public static void main(String[] paramArrayOfString) {
        ChartBuilderTest demo = new ChartBuilderTest("JFreeChart : ChartBuilderTest.java");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}