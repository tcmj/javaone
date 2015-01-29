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

import com.tcmj.pm.spread.impl.SpreadPeriod;
import java.util.Date;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * DatasetBuilder.
 * @author Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 * @since 28.01.2011
 */
public class DatasetBuilder {

    /**
     * default no-arg-constructor.
     */
    private DatasetBuilder() {
    }


    public static IntervalXYDataset createSpreadDataset(SpreadPeriod[] myperiods) {

        TimeSeries timeseries = new TimeSeries("Periods");


        for (SpreadPeriod period : myperiods) {
            
            Day day = new Day(new Date(period.getStartMillis()));
            timeseries.add(day, period.getPeriodValue(0));

        }

//
//        timeseries.add(new Day(2, 4, 2006), 11.5D);
//        timeseries.add(new Day(3, 4, 2006), 13.699999999999999D);
//        timeseries.add(new Day(4, 4, 2006), 10.5D);
//        timeseries.add(new Day(5, 4, 2006), 14.9D);
//        timeseries.add(new Day(6, 4, 2006), 15.699999999999999D);
//        timeseries.add(new Day(7, 4, 2006), 11.5D);
//        timeseries.add(new Day(8, 4, 2006), 9.5D);
//        timeseries.add(new Day(9, 4, 2006), 10.9D);
//        timeseries.add(new Day(10, 4, 2006), 14.1D);
//        timeseries.add(new Day(11, 4, 2006), 12.300000000000001D);
//        timeseries.add(new Day(12, 4, 2006), 14.300000000000001D);
//        timeseries.add(new Day(13, 4, 2006), 19D);
//        timeseries.add(new Day(14, 4, 2006), 17.899999999999999D);

//        TimeSeries timeseries2 = new TimeSeries("Month");
//        timeseries2.add(new Month(4, 2006), 44);

        TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
        timeseriescollection.addSeries(timeseries);


//        timeseriescollection.setXPosition(TimePeriodAnchor.END);

//        timeseriescollection.addSeries(timeseries2);
        return timeseriescollection;

    }



    public static IntervalXYDataset createCurveDataset(double[] curve) {

        XYSeries xyseries = new XYSeries("Curve");

        for (int i = 0; i < curve.length; i++) {
            double value = curve[i];
            xyseries.add(i+1, value);
        }

        XYSeriesCollection xyseriescollection = new XYSeriesCollection();
        xyseriescollection.addSeries(xyseries);

        return xyseriescollection;
    }


//
//    public static XYDataset createNormalDistributionCurve() {
//
//         NormalDistributionFunction2D nd = new NormalDistributionFunction2D(0.0D, 1.0D);
//
//  //      LineFunction2D lf = new LineFunction2D(0,100);
//    //    PowerFunction2D gg = new PowerFunction2D(2,4);
////
//        XYDataset xydataset = DatasetUtilities.sampleFunction2D(gg, 0D, 5D, 10, "Normal");
//
//        return xydataset;
//    }




}
