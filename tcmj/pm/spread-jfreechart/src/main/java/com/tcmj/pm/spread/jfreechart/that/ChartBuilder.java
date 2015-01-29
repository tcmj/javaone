/*
 *  Copyright (C) 2011 Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.tcmj.pm.spread.jfreechart.that;

import com.tcmj.common.date.DateTool;
import com.tcmj.pm.spread.impl.SpreadPeriod;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.PeriodAxis;
import org.jfree.chart.axis.PeriodAxisLabelInfo;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.ui.Layer;
import org.jfree.ui.LengthAdjustmentType;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;

/**
 * ChartBuilder.
 * @author Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 * @since 28.01.2011
 */
public class ChartBuilder {

    /**
     * default no-arg-constructor.
     */
    private ChartBuilder() {
    }


    private static PeriodAxis createDomainAxisTime() {

        PeriodAxis periodaxis = new PeriodAxis("Time");

        periodaxis.setAutoRangeTimePeriodClass(org.jfree.data.time.Day.class);
        PeriodAxisLabelInfo aperiodaxislabelinfo[] = new PeriodAxisLabelInfo[3];
        aperiodaxislabelinfo[0] = new PeriodAxisLabelInfo(org.jfree.data.time.Day.class, new SimpleDateFormat("d"));
        aperiodaxislabelinfo[1] = new PeriodAxisLabelInfo(org.jfree.data.time.Day.class, new SimpleDateFormat("E"), new RectangleInsets(2D, 2D, 2D, 2D), new Font("SansSerif", 1, 10), Color.blue, false, new BasicStroke(0.0F), Color.lightGray);
        aperiodaxislabelinfo[2] = new PeriodAxisLabelInfo(org.jfree.data.time.Month.class, new SimpleDateFormat("MMM"));
        periodaxis.setLabelInfo(aperiodaxislabelinfo);

        return periodaxis;
    }


    private static NumberAxis createDomainAxisCurveTicks() {
        NumberAxis axis = new NumberAxis("Curve value count");
        axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        axis.setAutoRange(true);
        axis.setAutoRangeIncludesZero(false);
        return axis;
    }


    private static NumberAxis createRangeAxisCurveValues() {
        NumberAxis axis = new NumberAxis("Curve value");
        axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return axis;
    }


    private static NumberAxis createRangeAxisSpreadValues() {
        NumberAxis axis = new NumberAxis("Spread value");
        axis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return axis;
    }


    private static IntervalMarker createMarker(long start, long end) {

        double d = start;
        double d1 = end;
        IntervalMarker intervalmarker = new IntervalMarker(d, d1);
//        intervalmarker.setLabelOffsetType(LengthAdjustmentType.EXPAND);
        intervalmarker.setPaint(new Color(242,239,239));
//        intervalmarker.setLabel("Automatic Cooling");
//        intervalmarker.setLabelFont(new Font("SansSerif", 0, 11));
//        intervalmarker.setLabelPaint(Color.blue);
//        intervalmarker.setLabelAnchor(RectangleAnchor.TOP_LEFT);
//        intervalmarker.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
        return intervalmarker;
    }


    /**
     * Build the chart object using the passed datasets.
     * @param spreadDataset  spread data
     * @param curveDataset curve data
     * @return jfreechart object
     */
    //public static JFreeChart createXYBarChart(IntervalXYDataset spreadDataset, IntervalXYDataset curveDataset) {
    public static JFreeChart createXYBarChart(SpreadPeriod[] myperiods , double[] curve, SpreadPeriod[] exclusions) {

        IntervalXYDataset spreadDataset = DatasetBuilder.createSpreadDataset(myperiods);
        IntervalXYDataset curveDataset = DatasetBuilder.createCurveDataset(curve);



        XYPlot xyplot = new XYPlot();       //Plotter
        xyplot.setOrientation(PlotOrientation.VERTICAL);

        //assign datasets
        xyplot.setDataset(0, spreadDataset);
        xyplot.setDataset(1, curveDataset);
        xyplot.setDatasetRenderingOrder(DatasetRenderingOrder.REVERSE);


        //set some plotter attributes
        xyplot.setBackgroundPaint(Color.WHITE);
        xyplot.setRangeGridlinePaint(Color.DARK_GRAY);//vertical
        xyplot.setDomainGridlinePaint(Color.LIGHT_GRAY);//horizontal
        xyplot.setDomainCrosshairVisible(true);
        xyplot.setRangeCrosshairVisible(true);


        //build and assign the four axes
        xyplot.setDomainAxis(0, createDomainAxisTime());
        xyplot.setRangeAxis(0, createRangeAxisSpreadValues());
        xyplot.setDomainAxis(1, createDomainAxisCurveTicks());
        xyplot.setRangeAxis(1, createRangeAxisCurveValues());

        //Axis Position (domain=x, range=y)
        xyplot.setDomainAxisLocation(0, AxisLocation.TOP_OR_LEFT);
        xyplot.setRangeAxisLocation(0, AxisLocation.TOP_OR_LEFT);
        xyplot.setDomainAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
        xyplot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);



        //create the renderer of the spread dataset
        XYBarRenderer spreadRenderer = new XYBarRenderer();
        xyplot.setRenderer(0, spreadRenderer);


        //Gradient-/Flat bar painter
//       spreadRenderer.setBarPainter(new StandardXYBarPainter());

        XYItemLabelGenerator generator = new StandardXYItemLabelGenerator(
                "{2}", new DecimalFormat("0.00"), new DecimalFormat("0.00"));
//        ItemLabelPosition itemLabelPosition = new ItemLabelPosition(ItemLabelAnchor.CENTER  , TextAnchor.HALF_ASCENT_CENTER);
        ItemLabelPosition itemLabelPosition = new ItemLabelPosition(ItemLabelAnchor.INSIDE6, TextAnchor.BOTTOM_CENTER);
        spreadRenderer.setBasePositiveItemLabelPosition(itemLabelPosition);
        spreadRenderer.setBaseItemLabelGenerator(generator);
        spreadRenderer.setBaseItemLabelsVisible(true);
        spreadRenderer.setBaseItemLabelPaint(new Color(113, 38, 51));

        Color mycolor = new Color(178, 101, 88, 255);
        spreadRenderer.setSeriesPaint(0, mycolor);
        spreadRenderer.setDrawBarOutline(true);   //border of the bars
        spreadRenderer.setSeriesOutlinePaint(0, Color.BLACK);
        spreadRenderer.setSeriesOutlineStroke(0, new BasicStroke(1));
        spreadRenderer.setShadowVisible(false);





        XYItemRenderer linerenderer = new StandardXYItemRenderer(StandardXYItemRenderer.LINES);
        //xyplot.setRenderer(1, new XYLineAndShapeRenderer(true, false));


        XYBarRenderer curveRenderer = new XYBarRenderer();
        xyplot.setRenderer(1, curveRenderer);

        //use the standard instead of gradient barpainter
        curveRenderer.setBarPainter(new StandardXYBarPainter());

        curveRenderer.setDrawBarOutline(false);

        Color mycolor2 = new Color(201, 210, 198, 128);
        curveRenderer.setSeriesPaint(0, mycolor2);

        curveRenderer.setShadowVisible(false);



//        xyplot.setRenderer(1, linerenderer);

        //merge the datasets
        xyplot.mapDatasetToDomainAxis(1, 1);
        xyplot.mapDatasetToRangeAxis(1, 1);









//space between the plot area and the axes (top-axis, left-axis, bottom-axis, right-axis)
        xyplot.setAxisOffset(new RectangleInsets(2, 2, 10, 10));

        System.out.println("lower: " + spreadDataset.getStartX(0, 0));
//        Double kkk = intervalxydataset.getStartX(0, 0).;
//        xyplot.getDomainAxis(0).setLowerBound(intervalxydataset.getStartX(0, 0).longValue()-1000);
//        xyplot.getDomainAxis(0).setLowerMargin(1000000);
//        xyplot.getDomainAxis(1).setLowerBound(1);
//        xyplot.getDomainAxis(1).setFixedAutoRange(2);
        xyplot.getDomainAxis(0).setLowerMargin(0.0);
//        xyplot.getDomainAxis(1).setAutoRange(true);
        xyplot.getDomainAxis(1).setLowerMargin(0.0);
        xyplot.getDomainAxis(0).setUpperMargin(0.0);
        xyplot.getDomainAxis(1).setUpperMargin(0.0);
//        xyplot.getRangeAxis(0).setLowerMargin(0.1);
//        xyplot.getRangeAxis(1).setLowerMargin(0.1);

        xyplot.getRangeAxis(0).setUpperMargin(0.3);  //bar-axis should have space to top
//        xyplot.getRangeAxis(1).setUpperMargin(0.1); //line-axis not ?!


        //add exclusions as markers
        if (exclusions!=null && exclusions.length > 0) {
            for (int i = 0; i < exclusions.length; i++) {
                SpreadPeriod exclusion = exclusions[i];
                if (exclusion!=null) {
                    IntervalMarker marker = createMarker(exclusion.getStartMillis(), exclusion.getEndMillis());
                    xyplot.addDomainMarker(marker, Layer.BACKGROUND);
                }
            }
        }





        JFreeChart chart = new JFreeChart("com.tcmj.pm.spread.Spread", JFreeChart.DEFAULT_TITLE_FONT,
                xyplot, true);


        Paint p = new GradientPaint(0, 0, Color.WHITE, 1000, 0, Color.LIGHT_GRAY);
        chart.setBackgroundPaint(p);

//        ChartUtilities.applyCurrentTheme(jfreechart);
        return chart;
    }
}
