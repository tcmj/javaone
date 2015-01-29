/**
 * Copyright(c) 2003 - 2015 by tcmj
 * All Rights Reserved.
 */
package com.tcmj.pm.mta.bo;

import com.tcmj.common.date.DateTool;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.tcmj.common.lang.Check.notNull;

/**
 * IMTAChart.
 * @author tdeut - Thomas Deutsch
 * @version $Id$
 */
public class ChartData {

    /** slf4j Logging Framework. */
    private static final Logger logger = LoggerFactory.getLogger(ChartData.class);

    /** Name of the chart. */
    private String chartName;

    /** Task series (lines). */
    private List<DataSeries> taskSeriesList = new ArrayList<DataSeries>();

    /** min report date. */
    private Date minReportDate;

    /** max report date. */
    private Date maxReportDate;

    /** Standard constructor.
     * @param name chartName
     */
    public ChartData(String name) {
        this.chartName = notNull(name, "Chartname may not be null");
    }

    /**
     * Name of the chart. Has no further meaning.
     * @return the chartName
     */
    public String getChartName() {
        return chartName;
    }

    /**
     * Name of the chart. Has no further meaning.
     * @param chartName the chartName to set
     */
    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    /**
     * Returns a unmodifieable view to the series-list!
     * <p>
     * A serie is a single line in the chart
     * @return the taskSeriesList
     */
    public List<DataSeries> getTaskSeriesList() {
        return Collections.unmodifiableList(this.taskSeriesList);
    }

    /**
     * Returns the amount of the series (lines)
     * @return size
     */
    public int getSeriesCount() {
        return this.taskSeriesList.size();
    }

    /**
     * Add a line to the chart.
     * @param series the task Serie to set
     */
    public void addTaskSeries(DataSeries series) {
        resetMinMaxDates();
        this.taskSeriesList.add(series);
    }

    /**
     * Invalidates the min and max date. On the next
     * call to {@link #getMinReportDate()} or {@link #getMaxReportDate()} a
     * recompution takes place!
     */
    private void resetMinMaxDates() {
        this.minReportDate = null;
        this.maxReportDate = null;
    }

    /**
     * The smallest date computed from all datapoints
     * @return the smallest milestone or report date
     */
    public Date getMinReportDate() {
        computeMinMaxDates();
        return minReportDate;
    }

    /**
     * The biggest date computed from all datapoints
     * @return the biggest milestone or report date
     */
    public Date getMaxReportDate() {
        computeMinMaxDates();
        return maxReportDate;
    }

    /**
     * compute the min and max dates.
     */
    private void computeMinMaxDates() {

        if (this.taskSeriesList == null || this.taskSeriesList.isEmpty()) {
            throw new IllegalArgumentException("No data series defined to compute the date range! Add tasks first!");
        }

        for (DataSeries series : taskSeriesList) {
            //Min-Date:
            if (this.minReportDate == null
                    || this.minReportDate.after(series.getMinDate())) {
                this.minReportDate = series.getMinDate();
            }
            //Max-Date:
            if (this.maxReportDate == null
                    || this.maxReportDate.before(series.getMaxDate())) {
                this.maxReportDate = series.getMaxDate();
            }
        }
        logger.trace("ReportRange computed! Min={}, Max={}", DateTool.formatDate(minReportDate), DateTool.formatDate(maxReportDate));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(getChartName()).toString();
    }

}
