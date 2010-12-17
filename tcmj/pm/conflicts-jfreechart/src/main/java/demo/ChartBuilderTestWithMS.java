package demo;

import com.tcmj.pm.conflicts.ConflictFinder;
import com.tcmj.pm.conflicts.bars.Bar;
import com.tcmj.pm.conflicts.bars.SimpleBar;
import com.tcmj.pm.conflicts.jfreechart.ChartBuilderDual;
import java.util.ArrayList;
import java.util.List;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author Administrator
 */
public class ChartBuilderTestWithMS extends ChartBuilderTest {

    public ChartBuilderTestWithMS(String paramString) {
        super(paramString);
    }

    @Override
    public JFreeChart createChart() {


        ConflictFinder cfinder = createConflictFinder();

        ChartBuilderDual cb = new ChartBuilderDual(cfinder);

        JFreeChart jf = cb.createChart2();


        return jf;


    }



    protected List<Bar> createInputList() {
        return createInputList2();
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

    public static void main(String[] paramArrayOfString) {
        ChartBuilderTestWithMS demo = new ChartBuilderTestWithMS("JFreeChart : ChartBuilderTestWithMS.java");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
    }
}
