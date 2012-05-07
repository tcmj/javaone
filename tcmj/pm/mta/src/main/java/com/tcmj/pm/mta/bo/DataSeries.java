/**
 * Copyright(c) 2003 - 2010 by INTECO GmbH
 * All Rights Reserved.
 */
package com.tcmj.pm.mta.bo;

import com.tcmj.pm.mta.dots.Symbol;
import com.tcmj.common.tools.date.DateTool;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A single line in the MTA chart is called serie.
 * @author tdeut - Thomas Deutsch
 */
public class DataSeries {

    /** slf4j Logging Framework. */
    private static final Logger logger = LoggerFactory.getLogger(DataSeries.class);

    /** Default Colors of the series. */
    private static final String DEFAULT_COLORS[] = {"BLACK", "BLACK", "BLACK", "BLUE", "RED",
        "GREEN", "MAGENTA", "BLACK", "RED", "BLUE", "GREEN", "GRAY", "RED", "BLACK",
        "ORANGE", "GREEN", "BLACK", "BLUE", "RED", "BLACK"
    };

    /** Name of the series which will displayed in the legend. */
    private String taskName;

    /** List containing all data points. */
    private final List<DataPoint> dataList = new ArrayList<DataPoint>();

    /** min report date. */
    private Date minTaskDate;

    /** max report date. */
    private Date maxTaskDate;

    /** Symbol to use if non default */
    private Symbol symbol = Symbol.DEFAULT_BY_ORDER;

    /** Color to use if non default */
    private String color;


    /** Standard constructor.
     * @param name 
     */
    public DataSeries(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Taskname may not be null");
        }
        this.taskName = name;
    }


    /**
     * Returns a unmodifieable view to the datalist!
     * @return the dataList
     */
    public List<DataPoint> getDataList() {
        return Collections.unmodifiableList(this.dataList);
    }


    /**
     * Adds a data point to this serie.
     * @param point the dataList to set
     */
    public void addDataPoint(DataPoint point) {
        resetMinMaxDates();
        this.dataList.add(point);
    }


    /**
     * Name of the serie/line displayed in the legend.
     * @return the taskName
     */
    public String getTaskName() {
        return taskName;
    }


    /**
     * Name of the serie/line displayed in the legend.
     * @param taskName the taskName to set
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }


    /**
     * Returns the smallest date (either milestone or report date!)
     * @return 
     */
    public Date getMinDate() {
        if (minTaskDate == null) {
            computeMinMaxDates();
        }
        return minTaskDate;
    }


    /**
     * Returns the biggest date (either milestone or report date!)
     * @return 
     */
    public Date getMaxDate() {
        if (maxTaskDate == null) {
            computeMinMaxDates();
        }
        return maxTaskDate;
    }


    /**
     * Invalidates the min and max date. On the next
     * call to {@link #getMinDate()} or {@link #getMaxDate()} a 
     * recompution takes place!
     */
    private void resetMinMaxDates() {
        this.minTaskDate = null;
        this.maxTaskDate = null;
    }
    
    
    private void computeMinMaxDates() {
        
        if (this.dataList == null || this.dataList.isEmpty()) {
            throw new IllegalArgumentException("No data points defined to compute the date range for task '" + getTaskName() + "'! Add datapoints first!");
        }
        
        
        for (DataPoint point : dataList) {

            /* MINIMAL DATE */
            //wenn es noch nie gesetzt wurde:
            if (this.minTaskDate == null) {
                //setze das kleinste von beiden (entweder report oder milestone date)
                if (point.getMilestoneDate().before(point.getReportDate())) {
                    this.minTaskDate = point.getMilestoneDate();
                } else {
                    this.minTaskDate = point.getReportDate();
                }
            } else {
                //ermittle zuerst das kleinste datum von beiden (report oder milestone date)
                Date smallest;
                if (point.getMilestoneDate().before(point.getReportDate())) {
                    smallest = point.getMilestoneDate();
                } else {
                    smallest = point.getReportDate();
                }

                //wenn das (kleinste) Punkt-Datum noch kleiner ist als das bereits angenommene minDate
                if (minTaskDate.after(smallest)) {
                    minTaskDate = smallest;
                }
                
            }

            /* MAXIMAL DATE */
            //wenn es noch nie gesetzt wurde:
            if (this.maxTaskDate == null) {
                //setze das kleinste von beiden (entweder report oder milestone date)
                if (point.getMilestoneDate().after(point.getReportDate())) {
                    this.maxTaskDate = point.getMilestoneDate();
                } else {
                    this.maxTaskDate = point.getReportDate();
                }
            } else {
                //ermittle zuerst das groesste datum von beiden (report oder milestone date)
                Date biggest;
                if (point.getMilestoneDate().after(point.getReportDate())) {
                    biggest = point.getMilestoneDate();
                } else {
                    biggest = point.getReportDate();
                }

                //wenn das (groesste) Punkt-Datum noch groesser ist als das bereits angenommene maxDate
                if (maxTaskDate.before(biggest)) {
                    maxTaskDate = biggest;
                }
                
            }
            
        }
        
        
        if (logger.isTraceEnabled()) {//needed to avoid the call to formatDate
            logger.trace("Series Range computed for Task '{}': Min={}, Max={}", new Object[]{getTaskName(), DateTool.formatDate(minTaskDate), DateTool.formatDate(maxTaskDate)});
        }
        
        
        
    }
    
    
    @Override
    public String toString() {
        return new ToStringBuilder(this).append(getTaskName()).toString();
    }


    /**
     * Override the default symbol.
     * @return the symbol
     */
    public Symbol getSymbol() {
        return symbol;
    }


    /**
     * Current symbol used to draw for each point.
     * @param symbol the symbol to set
     */
    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }


    /**
     * Set a custom color for the line.
     * You can use constants (BLACK, BLUE, RED, ..) or a hex value
     * @param hex eg. 0xff00ff, BLACK ...
     */
    public void setColor(String hex) {
        this.color = hex;
    }


    /**
     * If the color is not set on this task it is necessary to provide your 
     * series number in order to use default line colors instead.
     * <p>The first serie will get BLACK as line color, the second BLUE and soo on.
     *    The color fits to the symbol!
     * @param currentIndex series index (only used if the default color should be used)
     * @return the color eg. BLUE or RED or 0x00ff00
     */
    public String getColor(int currentIndex) {
        String colorString = null;
        if (this.color == null) {
            colorString = DEFAULT_COLORS[currentIndex % 20];
        } else {
            colorString = color;
        }
        return colorString;
    }
    
    
}
