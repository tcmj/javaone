/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcmj.pm.spread.jfreechart;

import com.tcmj.pm.spread.impl.SpreadPeriod;
import java.awt.BasicStroke;
import java.awt.Color;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

/**
 *
 * @author Administrator
 */
public class ChartBuilderDual extends ChartBuilder {

//    private TaskSeriesCollection localTaskSeriesCollection2 = new TaskSeriesCollection();
    public ChartBuilderDual(SpreadPeriod[] periods) {
        super(periods);
    }

    public JFreeChart createChart2() {

        createSerie1();

        IntervalXYDataset paramIntervalXYDataset = createDataset();
        XYDataset paramIntervalXYDataset2 = createDatasetMs();


        JFreeChart jfreechart = createChart(paramIntervalXYDataset);


        XYPlot xyplot = (XYPlot) jfreechart.getPlot();
        DateAxis xaxis = new DateAxis();
//        xaxis.setVerticalTickLabels(true);
        xyplot.setRangeAxis(xaxis);

        xyplot.setDataset(1, paramIntervalXYDataset2);
//        xyplot.mapDatasetToRangeAxis(1, 0);
//        xyplot.mapDatasetToDomainAxis(1, 0);



//Linien-Renderer erzeugen und dem zweiten DataSet zuweisen:
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        xyplot.setRenderer(1, renderer);
        xyplot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

//Linien-Renderer konfigurieren:
//        renderer.setLinesVisible(false);     //keine Verbindungslinien
        renderer.setSeriesLinesVisible(0, false);     //keine Verbindungslinien
        renderer.setUseOutlinePaint(true);   //Umrandung bei den Shapes zeichnen
//        renderer.setBaseShape(ShapeUtilities.createDiamond(10f));
//        renderer.setShape(ShapeUtilities.createDiamond(12f));
        renderer.setSeriesShape(0, ShapeUtilities.createDiamond(12f));
        renderer.setSeriesPaint(0, Color.RED);
//        renderer.setSeriesShape(4, ShapeUtilities.createDownTriangle(12f));
        renderer.setBaseOutlinePaint(Color.BLACK);
//        renderer.setShapesFilled(true);
//        renderer.setBaseFillPaint(Color.RED);
//        renderer.setPaint(Color.RED);
        renderer.setSeriesShape(1, ShapeUtilities.createDownTriangle(5f));
        renderer.setSeriesPaint(1, Color.BLACK);

        renderer.setSeriesStroke(1, new BasicStroke(7));
        renderer.setSeriesOutlineStroke(1, new BasicStroke(6f));
        renderer.setSeriesOutlinePaint(1, Color.RED);

//Item-Label:

        
        
renderer.setBaseToolTipGenerator(createTooltipGeneratorMS());

//        ChartUtilities.applyCurrentTheme(jfreechart);

        return jfreechart;
    }

    private XYDataset createDatasetMs() {
        XYSeries localXYSeries1 = new XYSeries("Series 1");
        localXYSeries1.add(1D, new Day(14, 2, 2009).getMiddleMillisecond());
        localXYSeries1.add(3D, new Day(20, 6, 2009).getMiddleMillisecond());
        localXYSeries1.add(3D, new Day(28, 5, 2009).getMiddleMillisecond());
        localXYSeries1.add(4D, new Day(15, 3, 2009).getMiddleMillisecond());

        XYSeriesCollection localXYSeriesCollection = new XYSeriesCollection(localXYSeries1);
        XYSeries localXYSeries2 = new XYSeries("Series 2");

        localXYSeries2.add(1.5D, new Day(17, 3, 2009).getMiddleMillisecond());
        localXYSeries2.add(1.5D, new Day(21, 4, 2009).getMiddleMillisecond());

        localXYSeriesCollection.addSeries(localXYSeries2);
        return localXYSeriesCollection;
    }

    @Override
    public LegendItemCollection createLegend() {
        LegendItemCollection items = new LegendItemCollection();

        LegendItem a =
                new LegendItem("Conflict Task", MyXYBarPainter.COLOR_CONFLICT);
        a.setOutlinePaint(Color.BLACK);
        a.setLinePaint(Color.BLACK);
        items.add(a);
        items.add(new LegendItem("Normal Task", MyXYBarPainter.COLOR_NO_CONFLICT));
        items.add(new LegendItem("InputItem", MyXYBarPainter.COLOR_INPUTBAR));
//        items.add(new LegendItem("Milestone", MyXYBarPainter.COLOR_INPUTBAR));
        items.add(new LegendItem("Milestone", "sss", "toolTipText", "urlText", ShapeUtilities.createDiamond(5f), Color.RED, new BasicStroke(1f), Color.BLACK));
        items.add(new LegendItem("Marker", "sss", "toolTipText", "urlText", ShapeUtilities.createDownTriangle(5f), Color.RED, new BasicStroke(1f), Color.BLACK));




        return items;
    }



     
    public XYToolTipGenerator createTooltipGeneratorMS() {



        return new MilestoneToolTipGenerator();


    }


}
