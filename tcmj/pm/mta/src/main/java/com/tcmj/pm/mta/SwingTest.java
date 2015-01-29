/**
 * Copyright(c) 2003 - 2015 by tcmj
 * All Rights Reserved.
 */
package com.tcmj.pm.mta;

import com.tcmj.pm.mta.bo.ChartData;
import com.tcmj.pm.mta.bo.DataPoint;
import com.tcmj.pm.mta.bo.DataSeries;
import com.tcmj.pm.mta.jfree.ChartBuilder;
import static com.tcmj.common.date.DateTool.*;

import java.awt.*;
import java.util.Date;
import javax.swing.JPanel;
import org.jfree.chart.*;
import org.jfree.ui.*;

public class SwingTest extends ApplicationFrame {

    private static ChartData createTestData() {
        
        ChartData chart = new ChartData("MTA");

        Date reportD1 = date(2011, 1, 1);
        Date reportD2 = date(2011, 2, 1);
        Date reportD3 = date(2011, 3, 1);
        Date reportD4 = date(2011, 4, 23);
        
        
        DataSeries serieLa = new DataSeries("Landshut" );
        serieLa.addDataPoint(new DataPoint(reportD1, date(2011, 2, 1)));
        serieLa.addDataPoint(new DataPoint(reportD2, date(2011, 2, 1)));
        chart.addTaskSeries(serieLa);
        
        
        DataSeries serieFrankfurt = new DataSeries("Frankfurt" );
        serieFrankfurt.addDataPoint(new DataPoint(reportD1, date(2011, 3, 10)));
        serieFrankfurt.addDataPoint(new DataPoint(reportD2, date(2011, 3, 10)));
        serieFrankfurt.addDataPoint(new DataPoint(reportD3, date(2011, 3, 1)));
        chart.addTaskSeries(serieFrankfurt);
        
        DataSeries serieBo = new DataSeries("Bonn" );
        serieBo.addDataPoint(new DataPoint(reportD1, date(2011, 4, 1)));
        serieBo.addDataPoint(new DataPoint(reportD2, date(2011, 4, 1)));
        serieBo.addDataPoint(new DataPoint(reportD3, date(2011, 4, 15)));
        chart.addTaskSeries(serieBo);
        
        DataSeries serieMu = new DataSeries("München" );
        serieMu.addDataPoint(new DataPoint(reportD1, date(2011, 6, 1)));
        serieMu.addDataPoint(new DataPoint(reportD2, date(2011, 6, 1)));
        serieMu.addDataPoint(new DataPoint(reportD3, date(2011, 6, 1)));
        serieMu.addDataPoint(new DataPoint(reportD4, reportD4));
        chart.addTaskSeries(serieMu);
        
        DataSeries serieBe = new DataSeries("Berlin" );
        serieBe.addDataPoint(new DataPoint(reportD1, date(2011, 7, 1)));
        serieBe.addDataPoint(new DataPoint(reportD4, date(2011, 7, 1)));
        chart.addTaskSeries(serieBe);

        DataSeries serieKo = new DataSeries("Köln" );
        serieKo.addDataPoint(new DataPoint(reportD1, date(2011, 8, 15)));
        serieKo.addDataPoint(new DataPoint(reportD2, date(2011, 8, 10)));
        serieKo.addDataPoint(new DataPoint(reportD3, date(2011, 8, 15)));
        serieKo.addDataPoint(new DataPoint(reportD4, date(2011, 8, 10)));
        serieKo.addDataPoint(new DataPoint(date(2011, 10, 10), date(2011, 8, 10)));
        chart.addTaskSeries(serieKo);
        
        DataSeries seriePa = new DataSeries("Passau" );
        seriePa.addDataPoint(new DataPoint(reportD1, date(2011, 9, 15)));
        seriePa.addDataPoint(new DataPoint(reportD2, date(2011, 9, 10)));
        seriePa.addDataPoint(new DataPoint(reportD3, date(2011, 9, 15)));
        seriePa.addDataPoint(new DataPoint(reportD4, date(2011, 9, 10)));
        seriePa.addDataPoint(new DataPoint(date(2011, 6, 10), date(2011, 9, 10)));
        seriePa.addDataPoint(new DataPoint(date(2011, 8, 1), date(2011, 9, 10)));
        chart.addTaskSeries(seriePa);
        
        DataSeries serieBr = new DataSeries("Bremen" );
        serieBr.addDataPoint(new DataPoint(reportD1, date(2011, 10, 15)));
        serieBr.addDataPoint(new DataPoint(reportD3, date(2011, 10, 15)));
        serieBr.addDataPoint(new DataPoint(reportD4, date(2011, 10, 10)));
        serieBr.addDataPoint(new DataPoint(date(2011, 8, 1), date(2011, 10, 10)));
        chart.addTaskSeries(serieBr);
        
        DataSeries serieHa = new DataSeries("Hamburg" );
        serieHa.addDataPoint(new DataPoint(reportD1, date(2011, 11, 15)));
        serieHa.addDataPoint(new DataPoint(reportD3, date(2011, 11, 15)));
        serieHa.addDataPoint(new DataPoint(reportD4, date(2011, 11, 10)));
        chart.addTaskSeries(serieHa);
        
        DataSeries serieRe = new DataSeries("Regensburg" );
        serieRe.addDataPoint(new DataPoint(reportD1, date(2011, 12, 6)));
        serieRe.addDataPoint(new DataPoint(reportD2, date(2011, 12, 6)));
        serieRe.addDataPoint(new DataPoint(reportD3, date(2011, 12, 6)));
        serieRe.addDataPoint(new DataPoint(reportD4, date(2011, 12, 6)));
        chart.addTaskSeries(serieRe);
        
        DataSeries seriePf = new DataSeries("Pfeffenhausen" );
        seriePf.addDataPoint(new DataPoint(reportD1, date(2011, 12, 24)));
        seriePf.addDataPoint(new DataPoint(reportD2, date(2011, 12, 24)));
        seriePf.addDataPoint(new DataPoint(reportD3, date(2011, 12, 24)));
        seriePf.addDataPoint(new DataPoint(reportD4, date(2011, 12, 24)));
        chart.addTaskSeries(seriePf);
        



        return chart;
    }

    public SwingTest(String s) {
        super(s);
        JPanel jpanel = createDemoPanel();
        jpanel.setPreferredSize(new Dimension(800, 700));
        setContentPane(jpanel);
    }

    public static JPanel createDemoPanel() {
        ChartData chart = createTestData();
//        MTABuilder instance = new MTABuilder(chart);
        ChartBuilder chartBuilder = new ChartBuilder(chart);
        JFreeChart jfreechart = chartBuilder.createChart();
        return new ChartPanel(jfreechart);
    }

    public static void main(String args[]) {
        SwingTest periodaxisdemo3 = new SwingTest("JFreeChart: PeriodAxisDemo3.java");
        periodaxisdemo3.pack();
        RefineryUtilities.centerFrameOnScreen(periodaxisdemo3);
        periodaxisdemo3.setVisible(true);
    }
}
