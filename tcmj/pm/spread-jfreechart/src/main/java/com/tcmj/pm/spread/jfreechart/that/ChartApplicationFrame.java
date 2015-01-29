package com.tcmj.pm.spread.jfreechart.that;

import com.tcmj.pm.spread.SpreadCalculator;
import com.tcmj.pm.spread.impl.SpreadDoubleImpl;
import com.tcmj.pm.spread.impl.SpreadPeriod;
import java.awt.Dimension;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * ChartApplicationFrame.
 * @author Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 * @since 28.01.2011
 */
public class ChartApplicationFrame extends ApplicationFrame {

    public ChartApplicationFrame(JFreeChart jfreechart) {
        super("JFreeChart: tcmj's spread lib --> com.tcmj.pm.spread.Spread <--");
        createJFreeChartPanel(jfreechart);
    }

    public final void createJFreeChartPanel(JFreeChart jfreechart) {
        ChartPanel chartPanel = new ChartPanel(jfreechart);
        chartPanel.setPreferredSize(new Dimension(800, 500));
        setContentPane(chartPanel);
    }

    /**
     * Start entry point.
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {

            System.out.println("Creating spread periods...");
//            SpreadPeriod[] myperiods = PeriodExampleDataFactory.createDaily(2011, 2, 10, 2011, 2, 20);
            SpreadPeriod[] myperiods = PeriodExampleDataFactory.createDaily(2011, 6 );

            long curveStartDate = myperiods[0].getStartMillis();
            long curveEndDate = myperiods[myperiods.length - 1].getEndMillis();

//            double[] curve = {1};
            double[] curve = {1, 2, 5, 5, 2};
//            double [] curve = {1,0,0,2,0,0,0,5,0,2};
//            double [] curve = {1,2,3,4,4,4,5,6,6,6,7,7,8,8,9,9,10,11,12,13,13,14,15,16,17,18,19,20,20,20,21,20,20,20,19,18,17,16,15,14,13,12,11,10,9,9,8,7,6,5,4,4,3};
            double[] value = {1000};

            SpreadPeriod[] exclusions = PeriodExampleDataFactory.createWeekendExclusions(curveStartDate, curveEndDate);
//exclusions = null;

            SpreadCalculator spread = new SpreadDoubleImpl();
            spread.computeSpread(value, curveStartDate, curveEndDate, myperiods, curve, exclusions);

            System.out.println("Creating chart...");
            JFreeChart mychart = ChartBuilder.createXYBarChart(myperiods, curve, exclusions);

            System.out.println("Creating chart application frame...");
            ChartApplicationFrame chartappframe = new ChartApplicationFrame(mychart);

            chartappframe.pack();
            RefineryUtilities.centerFrameOnScreen(chartappframe);
            chartappframe.setVisible(true);

            System.out.println("Finished");
        } catch (Exception exc) {
            System.out.println("Error: " + exc.getMessage());
            exc.printStackTrace();
        }

    }
}
