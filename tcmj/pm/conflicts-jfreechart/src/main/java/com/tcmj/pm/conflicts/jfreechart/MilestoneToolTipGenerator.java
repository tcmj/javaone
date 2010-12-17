/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcmj.pm.conflicts.jfreechart;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.gantt.XYTaskDataset;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Administrator
 */
public class MilestoneToolTipGenerator extends StandardXYToolTipGenerator {

    public MilestoneToolTipGenerator(String formatString, DateFormat xFormat, DateFormat yFormat) {
        super(formatString, xFormat, yFormat);
    }

    public MilestoneToolTipGenerator(String formatString, DateFormat xFormat, NumberFormat yFormat) {
        super(formatString, xFormat, yFormat);
    }

    public MilestoneToolTipGenerator() {

        super("{0}: Date=({1,date,yyyy-MM-dd HH:mm})", new SimpleDateFormat("yyyyMMdd"), new SimpleDateFormat("yyyyMMdd"));
    }

    @Override
    public String generateToolTip(XYDataset dataset, int series, int item) {

        return generateString(dataset, series, item);
    }

    public String generateString(XYDataset dataset, int series, int item) {
        String result = null;
        Object[] items = createItemArray(dataset, series, item);
        result = MessageFormat.format(getFormatString(), items);
//        System.out.println("xxx-"+result+"-xxx");
        return result;
    }

    protected Object[] createItemArray(XYDataset dataset, int series, int item) {


        Object[] result = new Object[3];


             XYSeriesCollection taskdataset = (XYSeriesCollection) dataset;
             XYSeries serie = taskdataset.getSeries(series);
             XYDataItem xyitem = serie.getDataItem(item);




        
        result[0] = dataset.getSeriesKey(series).toString();
        long ddd = (long)xyitem.getYValue();
        result[1] = new Date(ddd);
        result[2] = xyitem.getXValue();
            



//        double x = dataset.getXValue(series, item);
//        if (this.xDateFormat != null) {
//            result[1] = this.xDateFormat.format(new Date((long) x));
//        } else {
//            result[1] = this.xFormat.format(x);
//        }
//
//        double y = dataset.getYValue(series, item);
//        if (Double.isNaN(y) && dataset.getY(series, item) == null) {
//            result[2] = this.nullYString;
//        } else {
//            if (this.yDateFormat != null) {
//                result[2] = this.yDateFormat.format(new Date((long) y));
//            } else {
//                result[2] = this.yFormat.format(y);
//            }
//        }
        return result;
    }
}
