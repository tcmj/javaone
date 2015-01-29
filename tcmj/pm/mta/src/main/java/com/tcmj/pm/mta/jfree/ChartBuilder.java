/**
 * Copyright(c) 2003 - 2015 by tcmj
 * All Rights Reserved.
 */
package com.tcmj.pm.mta.jfree;

import com.tcmj.common.date.DateTool;
import java.awt.Paint;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import com.tcmj.pm.mta.bo.ChartData;
import com.tcmj.pm.mta.bo.DataPoint;
import com.tcmj.pm.mta.bo.DataSeries;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.jfree.chart.ChartColor;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.DrawingSupplier;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.Layer;
import org.jfree.util.ShapeUtilities;

/**
 * JFreeChart ChartBuilder.
 * @author tcmj - Thomas Deutsch
 */
public class ChartBuilder {

    protected ChartData chartdata;
    protected TaskSeriesCollection localTaskSeriesCollection = new TaskSeriesCollection();
    private Date chartStartDate;
    private Date chartFinishDate;
    private Map<ChartBuilder.Settings, String> optionalSettings;

    public enum Settings {

        CHART_TITLE(null),
        X_AXIS_TITLE("Report Date"),
        Y_AXIS_TITLE("Milestone Date"),
        DATE_FORMAT("yyyy-MM-dd"),
        SHOW_TIME_NOW_AREA("TRUE");
        String defaultvalue;

        Settings(String defval) {
            this.defaultvalue = defval;
        }
    }

    public ChartBuilder(ChartData data) {
        this.chartdata = data;
        this.chartStartDate = DateTool.addMonths(data.getMinReportDate(), -1);
        this.chartFinishDate = DateTool.addMonths(data.getMaxReportDate(), 1);
    }

    private String getSetting(Settings key) {
        if (this.optionalSettings == null) {
            return key.defaultvalue;
        } else {
            return "" + this.optionalSettings.get(key);
        }
    }

    protected XYDataset createMTASerie() {
        XYSeriesCollection seriesCollection = new XYSeriesCollection();
        for (DataSeries ds : chartdata.getTaskSeriesList()) {

            XYSeries series = new XYSeries(ds.getTaskName());
            for (DataPoint datapoint : ds.getDataList()) {
                series.add(datapoint.getReportDate().getTime(), datapoint.getMilestoneDate().getTime());
            }
            seriesCollection.addSeries(series);
        }
        return seriesCollection;
    }

    protected XYDataset createRectangleSerie() {
        XYSeriesCollection seriesCollection = new XYSeriesCollection();
        XYSeries series = new XYSeries("rectangle");
        series.add(chartStartDate.getTime(), chartStartDate.getTime());
        series.add(chartFinishDate.getTime(), chartFinishDate.getTime());
        seriesCollection.addSeries(series);
        return seriesCollection;
    }

    public LegendItemCollection createLegend(XYPlot plot) {
        LegendItemCollection items = new LegendItemCollection();
        XYDataset dataset = plot.getDataset(0);
        XYItemRenderer renderer = plot.getRenderer(0);
        if (dataset != null && renderer != null) {
            int seriesCount = dataset.getSeriesCount();
            for (int i = 0; i < seriesCount; i++) {
                if (renderer.isSeriesVisible(i) && renderer.isSeriesVisibleInLegend(i)) {
                    LegendItem item = renderer.getLegendItem(0, i);
                    if (item != null) {
                        item.setLabelPaint(Color.DARK_GRAY);
                        items.add(item);
                    }
                }
            }
        }

        return items;
    }

    public JFreeChart createChart() {
        return createChart(createMTASerie());
    }

    private DateAxis createDomainAxisReportDate() {
        DateAxis axis = new DateAxis(getSetting(Settings.X_AXIS_TITLE));
        axis.setDateFormatOverride(new SimpleDateFormat(getSetting(Settings.DATE_FORMAT)));
        axis.setVerticalTickLabels(true);
        axis.setRange(chartStartDate, chartFinishDate);
        axis.setTickUnit(new DateTickUnit(DateTickUnitType.MONTH, 1));
        return axis;
    }

    private DateAxis createRangeAxisMilestoneDate() {
        DateAxis periodaxis = new DateAxis(getSetting(Settings.Y_AXIS_TITLE));
        periodaxis.setDateFormatOverride(new SimpleDateFormat(getSetting(Settings.DATE_FORMAT)));
        periodaxis.setRange(chartStartDate, chartFinishDate);
        periodaxis.setTickUnit(new DateTickUnit(DateTickUnitType.MONTH, 1));
        return periodaxis;
    }

    public JFreeChart createChart(XYDataset dataset) {

        Color chartBGcolor = Color.WHITE;

        XYPlot plot = new XYPlot();

        plot.setDrawingSupplier(MTAColorsAndShapes.createDrawingSupplier(this.chartdata.getSeriesCount()));

        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setBackgroundPaint(chartBGcolor);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);  //vertical
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY); //horizontal
        plot.setOutlineVisible(false);

        //MTA Data
        XYItemRenderer renderer = new XYLineAndShapeRenderer(true, true);
        plot.setRenderer(0, renderer);
        plot.setDataset(0, dataset);

        //Rectangle
        XYAreaRenderer arearenderer = new XYAreaRenderer(XYAreaRenderer.AREA);
        arearenderer.setSeriesPaint(0, plot.getBackgroundPaint());
        plot.setDataset(1, createRectangleSerie());
        plot.setRenderer(1, arearenderer);

        //Axis (domain=x, range=y)
        plot.setDomainAxis(0, createDomainAxisReportDate());
        plot.setDomainAxisLocation(0, AxisLocation.TOP_OR_LEFT);
        plot.setRangeAxis(0, createRangeAxisMilestoneDate());
        plot.setRangeAxisLocation(0, AxisLocation.TOP_OR_LEFT);

        //Legend (only of MTA dataset)
        plot.setFixedLegendItems(createLegend(plot));

        //Current Time Marker
        if (Boolean.parseBoolean(getSetting(Settings.SHOW_TIME_NOW_AREA))) {
            IntervalMarker marker = createMarker();
            plot.addDomainMarker(marker, Layer.BACKGROUND);
        }

        //Diagonal
        double max = chartFinishDate.getTime();
        BasicStroke bstroke = new BasicStroke(0.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, new float[]{3, 5}, 0);
        XYLineAnnotation lineano = new XYLineAnnotation(0, 0, max, max, bstroke, Color.LIGHT_GRAY);
        plot.addAnnotation(lineano);

        DefaultDrawingSupplier supplier = (DefaultDrawingSupplier) plot.getDrawingSupplier();

        System.out.println("drawingsupplier: " + plot.getDrawingSupplier());
        JFreeChart jfreechart = new JFreeChart(null, JFreeChart.DEFAULT_TITLE_FONT, plot, true);

        jfreechart.setBackgroundPaint(chartBGcolor);

        return jfreechart;
    }

    private static IntervalMarker createMarker() {
        long w1 = 1000 * 60 * 60 * 24 * 7; //space from now = 1 week)
        double d = System.currentTimeMillis() - w1;
        double d1 = System.currentTimeMillis() + w1;
        IntervalMarker intervalmarker = new IntervalMarker(d, d1);
        intervalmarker.setPaint(new Color(244, 244, 244));//new Color(242, 239, 239));
        return intervalmarker;
    }
}
