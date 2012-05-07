/**
 * Copyright(c) 2003 - 2010 by INTECO GmbH
 * All Rights Reserved.
 */
package com.tcmj.pm.mta.bo;

import com.tcmj.common.tools.date.DateTool;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.apache.commons.lang.Validate.notNull;
/**
 * Represents a single point in the MTA diagram. To define such a point there are
 * two dates necessary the report and the milestone date.
 * <p>The report date is point in time when we 'look' at my milestone
 * <p>The milestone date is the point in time what my milestone is atm.
 * @author tdeut - Thomas Deutsch
 */
public class DataPoint implements Comparable<DataPoint> {

    /** slf4j Logging Framework. */
    private static final Logger logger = LoggerFactory.getLogger(DataPoint.class);

    
    private final Date reportDate;

    private final Date milestoneDate;


    /** Standard constructor.
     * @param reportDate report date (x-axis)
     * @param milestoneDate milestone date (y-axis)
     */
    public DataPoint(Date reportDate, Date milestoneDate) {
        notNull(reportDate, "Report date may not be null");
        notNull(milestoneDate, "Milestone date may not be null");
        this.reportDate = reportDate;
        this.milestoneDate = milestoneDate;
        
        if (logger.isTraceEnabled()) {//needed to avoid the call to formatDate
            if (isValid()) {
                logger.trace("Invalid MTA DataPoint R:{}/M:{}! (ReportDate is after MilestoneDate)", DateTool.formatDate(reportDate), DateTool.formatDate(milestoneDate));
            }
        }
    }


    /**
     * A datapoint is so called 'invalid' if the reporting date is after the milestone date.
     * <p>Invalid datapoints are points in the lower right half of the chart</p>
     * <p>Per definition does a MTA chart only have points on the left upper half</p>
     * @return reportDate.after(milestoneDate)?
     */
    public final boolean isValid() {
        return milestoneDate.compareTo(reportDate) >= 0;
    }
    
    /**
     * Report Date.
     * @return the reportDate
     */
    public Date getReportDate() {
        return reportDate;
    }


    /**
     * Milestone Date.
     * @return the milestoneDate
     */
    public Date getMilestoneDate() {
        return milestoneDate;
    }


    /**
     * Compares using the compare method of the report date!
     */
    public int compareTo(DataPoint anotherDate) {
        return this.reportDate.compareTo(anotherDate.getReportDate());
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this).append("reportdate", getReportDate()).append("milestonedate", getMilestoneDate()).toString();
    }


}
