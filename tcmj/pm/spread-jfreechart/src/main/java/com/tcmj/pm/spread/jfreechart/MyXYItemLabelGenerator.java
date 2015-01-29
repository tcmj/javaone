/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcmj.pm.spread.jfreechart;

import java.text.MessageFormat;
import java.text.NumberFormat;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.gantt.XYTaskDataset;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author Administrator
 */
public class MyXYItemLabelGenerator implements XYItemLabelGenerator {

    private String formatString = "{4,number,percent} \n{1}";

    
    public String generateLabel(XYDataset dataset, int series, int item) {

        return generateLabelString(dataset, series, item);

    }

    public String generateLabelString(XYDataset dataset, int series, int item) {
        String result = null;
        Object[] items = createItemArray(dataset, series, item);
        result = MessageFormat.format(this.formatString, items);
//        System.out.println("xxx-"+result+"-xxx");
        return result;
    }

    protected Object[] createItemArray(XYDataset dataset, int series, int item) {


        XYTaskDataset taskdataset = (XYTaskDataset)dataset;
        TaskSeriesCollection tcol = taskdataset.getTasks();
        TaskSeries taskseries = tcol.getSeries(series);
        Task task = taskseries.get(item);


        Object[] result = new Object[5];
        result[0] = dataset.getSeriesKey(series).toString();
        result[1] = task.getDescription();
        result[2] = task.getDuration().getStart();
        result[3] = task.getDuration().getEnd();
        result[4] = task.getPercentComplete();
     
        


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
