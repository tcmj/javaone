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
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * PeriodExampleDataFactory.
 * @author Thomas Deutsch
 * @since 28.01.2011
 */
public class PeriodExampleDataFactory {

    DateTool datetool;

    /**
     * default no-arg-constructor.
     */
    private PeriodExampleDataFactory() {
    }

    /**
     * Inclusive start date and exclusive finish date.
     * @param startYear
     * @param startMonth
     * @param startDay
     * @param endYear
     * @param endMonth
     * @param endDay
     * @return
     */
    public static SpreadPeriod[] createDaily(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {

        Date dateStart = DateTool.date(startYear, startMonth, startDay);
        Date dateEnd = DateTool.date(endYear, endMonth, endDay);
        return doDaily(dateStart, dateEnd);
    }

    public static SpreadPeriod[] createDaily(int startYear, int startMonth) {
        final int startDay = 1;
        Date dateStart = DateTool.date(startYear, startMonth, startDay);
        Date dateEnd = DateTool.addMonths(dateStart, 1);
        return doDaily(dateStart, dateEnd);
    }

    public static SpreadPeriod[] createWeekendExclusions(long start, long end) {

        Date dateStart = new Date(start);
        Date dateEnd = new Date(end);
        Set<SpreadPeriod> exclList = new LinkedHashSet<SpreadPeriod>();
//        int daysbetween = (int)DateTool.daysbetween(dateStart, dateEnd);
//        System.out.println("daysbetween: "+daysbetween);
//        SpreadPeriod[] periods = new SpreadPeriod[daysbetween];

        for (Iterator<Date> it = DateTool.iterate(dateStart, dateEnd); it.hasNext();) {
            Date date = it.next();
            Calendar calendar = DateTool.getCalendar(date);
            final int dow = calendar.get(Calendar.DAY_OF_WEEK);

            if (dow == Calendar.SATURDAY || dow == Calendar.SUNDAY) {

                System.out.println("createWeekendExclusions: " + date);

                exclList.add(new SpreadPeriod(date.getTime(), DateTool.addDays(date, 1).getTime()));
            }

        }

        return exclList.toArray(new SpreadPeriod[exclList.size()]);
    }

    private static SpreadPeriod[] doDaily(Date start, Date finish) {

        int daysbetween = (int) DateTool.daysbetween(start, finish);
        System.out.println("daysbetween: " + daysbetween);
        SpreadPeriod[] periods = new SpreadPeriod[daysbetween];

        int indx = 0;
        for (Iterator<Date> it = DateTool.iterate(start, finish); it.hasNext() && indx < periods.length;) {
            Date date = it.next();

            System.out.println("dateiterator: " + date);

            periods[indx] = new SpreadPeriod(date.getTime(), DateTool.addDays(date, 1).getTime());

            indx++;
        }

        return periods;

    }

}
