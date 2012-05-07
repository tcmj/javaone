/**
 * Copyright(c) 2003 - 2010 by INTECO GmbH
 * All Rights Reserved.
 */
package com.tcmj.pm.mta;

import com.tcmj.pm.mta.bo.DataPoint;
import com.tcmj.pm.mta.bo.ChartData;
import com.tcmj.pm.mta.bo.DataSeries;
import static com.tcmj.common.tools.date.DateTool.*;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author TDEUT
 */
public class ChartDataTest {

    public ChartDataTest() {
    }


    @Test
    public void shouldGetSameChartNameAsConstructed() {
        //given
        ChartData instance = new ChartData("any");
        //when
        String result = instance.getChartName();
        //then
        String expResult = "any";
        assertEquals(expResult, result);
    }


    @Test
    public void shouldFindMinAndMaxReportDate_TestCase1() {

        //given
        ChartData imtaChart = new ChartData("TestCase - 001");
        DataSeries task = new DataSeries("001-Milestone-1");
        task.addDataPoint(new DataPoint(date(2010, 1, 1), date(2009, 1, 15)));
        task.addDataPoint(new DataPoint(date(2010, 2, 1), date(2009, 3, 30)));
        task.addDataPoint(new DataPoint(date(2010, 4, 1), date(2009, 10, 20)));
        imtaChart.addTaskSeries(task);
        //when
        Date minReportDate = imtaChart.getMinReportDate();
        Date maxReportDate = imtaChart.getMaxReportDate();
        //then
        assertEquals("min", date(2009, 1, 15), minReportDate);
        assertEquals("max", date(2010, 4, 1), maxReportDate);
    }


    @Test
    public void shouldFindMinAndMaxReportDate_TestCase2() {
        //given
        ChartData imtaChart = new ChartData("TestCase - 002");
        DataSeries task = new DataSeries("001-Milestone-1");
        task.addDataPoint(new DataPoint(date(2010, 1, 1), date(2010, 1, 1)));
        task.addDataPoint(new DataPoint(date(2010, 1, 1), date(2010, 1, 1)));
        task.addDataPoint(new DataPoint(date(2010, 1, 1), date(2010, 1, 1)));
        imtaChart.addTaskSeries(task);
        //when
        Date minReportDate = imtaChart.getMinReportDate();
        Date maxReportDate = imtaChart.getMaxReportDate();
        //then
        assertEquals("min", date(2010, 1, 1), minReportDate);
        assertEquals("max", date(2010, 1, 1), maxReportDate);

    }


    @Test
    public void shouldFindMinAndMaxReportDate_TestCase3() {
        //given
        ChartData imtaChart = new ChartData("TestCase - 003");
        DataSeries task = new DataSeries("001-Milestone-1");
        task.addDataPoint(new DataPoint(date(2005, 1, 1), date(2010, 1, 1)));
        task.addDataPoint(new DataPoint(date(2005, 1, 1), date(2010, 1, 1)));
        task.addDataPoint(new DataPoint(date(2005, 1, 1), date(2010, 1, 1)));
        imtaChart.addTaskSeries(task);
        //when
        Date minReportDate = imtaChart.getMinReportDate();
        Date maxReportDate = imtaChart.getMaxReportDate();
        //then
        assertEquals("min", date(2005, 1, 1), minReportDate);
        assertEquals("max", date(2010, 1, 1), maxReportDate);
    }


    @Test
    public void shouldFindMinAndMaxReportDate_TestCase4() {
        //given
        ChartData imtaChart = new ChartData("TestCase - 004");
        DataSeries task = new DataSeries("001-Milestone-1");
        task.addDataPoint(new DataPoint(date(2005, 1, 1), date(2004, 8, 1)));
        task.addDataPoint(new DataPoint(date(2006, 1, 1), date(2004, 5, 1)));
        task.addDataPoint(new DataPoint(date(2007, 1, 1), date(2004, 3, 1)));
        imtaChart.addTaskSeries(task);
        //when
        Date minReportDate = imtaChart.getMinReportDate();
        Date maxReportDate = imtaChart.getMaxReportDate();
        //then
        assertEquals("min", date(2004, 3, 1), minReportDate);
        assertEquals("max", date(2007, 1, 1), maxReportDate);
    }


    @Test
    public void shouldFindMinAndMaxReportDate_TestCase5() {
        //given
        ChartData imtaChart = new ChartData("TestCase - 005");
        DataSeries task = new DataSeries("001-Milestone-1");
        task.addDataPoint(new DataPoint(date(2011, 1, 1), date(2012, 4, 10)));
        task.addDataPoint(new DataPoint(date(2011, 2, 1), date(2012, 4, 30)));
        task.addDataPoint(new DataPoint(date(2011, 3, 1), date(2012, 3, 20)));
        imtaChart.addTaskSeries(task);
        //when
        Date minReportDate = imtaChart.getMinReportDate();
        Date maxReportDate = imtaChart.getMaxReportDate();
        //then
        assertEquals("min", date(2011, 1, 1), minReportDate);
        assertEquals("max", date(2012, 4, 30), maxReportDate);

        //oh we forgot one point - calculation must be renewed!
        task.addDataPoint(new DataPoint(date(2011, 3, 1), date(2012, 5, 20)));
        assertEquals("recalc-min-failed", date(2011, 1, 1), imtaChart.getMinReportDate());
        assertEquals("recalc-max-failed", date(2012, 5, 20), imtaChart.getMaxReportDate());


    }


    @Test(expected = java.lang.IllegalArgumentException.class)
    public void shouldFindMinAndMaxReportDate_TestCase6() {
        //given -- with an empty series
        ChartData imtaChart = new ChartData("TestCase - 006");
        DataSeries task = new DataSeries("006-Milestone-1");
        task.addDataPoint(new DataPoint(date(2005, 1, 1), date(2004, 8, 1)));
        imtaChart.addTaskSeries(task);
        imtaChart.addTaskSeries(new DataSeries("006-Milestone-2"));
        //when
        imtaChart.getMinReportDate();

    }


    @Test
    public void shouldKnowValidDataPoints() {

        //                      |ReportDate     |   MilestoneDate|
        assertThat(new DataPoint(date(2013, 10, 1), date(2013, 10, 1)).isValid(), is(true));
        
        
        assertThat(new DataPoint(date(2013, 10, 1), date(2013, 7, 1)).isValid(), is(false));
        
        assertThat(new DataPoint(date(2013, 10, 1), date(2014, 4, 1)).isValid(), is(true));
        
        
        assertThat(new DataPoint(date(2013, 4, 1), date(2013, 10, 1)).isValid(), is(true));
        
        assertThat(new DataPoint(date(2013, 11, 1), date(2013, 10, 1)).isValid(), is(false));
        
        
    }


}
