/* DateHelper.java, Created on 26. August 2008, 15:08
 * CVS: $Id: DateHelper.java,v 1.1 2008/08/27 15:00:00 TDEUT Exp $
 */
package com.tcmj.common.tools.helper.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Stack;

/**
 * Helper for time and date operations.
 * <p>This class uses a static calendar pool</p>
 * <p> use following import in your classes <br>import static com.tcmj.common.tools.helper.date.DateHelper.*;</p>
 * @author tdeut - Thomas Deutsch
 * @JUnit Test available!
 */
public final class DateHelper {

    /** 24 hours in ms (24h x 60min x 60sec x 1000ms). */
    public static final long ONE_DAY = 24L * 60L * 60L * 1000L;
    /** 1 hour in ms (60min x 60sec x 1000ms). */
    public static final long ONE_HOUR = 60L * 60L * 1000L;
    /** 1 minute in ms (60sec x 1000ms). */
    public static final long ONE_MINUTE = 60L * 1000L;
    /** 1 second in ms (1000ms). */
    public static final long ONE_SECOND = 1000L;
    /** Pattern: yyyyMMdd */
    private static final DateFormat DFYYYYMMDD =
            new SimpleDateFormat("yyyy-MM-dd");
    /** Pattern: yyyy-MM-dd */
    private static final DateFormat DFYYYYMMDDHHMM =
            new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /** Calendar-Pool. */
    private static final Stack<Calendar> calpool = new Stack<Calendar>();

    /** Singleton. */
    private DateHelper() {
    }


    /**
     * Creates a date by given year, month and day parameters.<br>
     * Time information will set to '0' (pHour,minutes,sec,milis)
     * @param pYear Year (eg. 2008)
     * @param pMonth Month (1 to 12)
     * @param pDay Day (1 to 30/31) invalid days will be roled to the next pMonth
     * @return a date object
     */
    public static Date date(int pYear, int pMonth, int pDay) {
        return date(pYear, pMonth, pDay, 0, 0, 0);
    }


    /**
     * Creates a date by given year, month and day parameters.<br>
     * Time information will set to '0' (pHour,minutes,sec,milis)
     * @param pYear Year (eg. 2008)
     * @param pMonth Month (1 to 12)
     * @param pDay Day (1 to 30/31) invalid days will be roled to the next pMonth
     * @param pHour Hour (0 to 59)
     * @return a date object
     */
    public static Date date(int pYear, int pMonth, int pDay, int pHour) {
        return date(pYear, pMonth, pDay, pHour, 0, 0);
    }


    /**
     * Creates a date by given year, month and day parameters.<br>
     * Time information will set to '0' (pHour,minutes,sec,milis)
     * @param pYear Year (eg. 2008)
     * @param pMonth Month (1 to 12)
     * @param pDay Day (1 to 30/31) invalid days will be roled to the next pMonth
     * @param pHour Hour (0 to 59)
     * @param pMinute (0 to 59)
     * @return a date object
     */
    public static Date date(int pYear, int pMonth, int pDay, int pHour, int pMinute) {
        return date(pYear, pMonth, pDay, pHour, pMinute, 0);
    }


    /**
     * Creates a date by given year, month and day parameters.<br>
     * Time information will set to '0' (pHour,minutes,sec,milis)
     * @param pYear Year (eg. 2008)
     * @param pMonth Month (1 to 12)
     * @param pDay Day (1 to 30/31) invalid days will be roled to the next pMonth
     * @param pHour Hour (0 to 59)
     * @param pMinute (0 to 59)
     * @param pSecond (0 to 59)
     * @return a date object
     */
    public static Date date(int pYear, int pMonth, int pDay, int pHour, int pMinute, int pSecond) {
        Calendar calendar = getCalendar();
        calendar.set(pYear, pMonth - 1, pDay, pHour, pMinute, pSecond);
        calendar.set(Calendar.MILLISECOND, 0);
        releaseCalendar(calendar);
        return calendar.getTime();
    }


    /**
     * Checks if a date falls into a given timerange.<br>
     *       <code>Date &gt;=  StartDate AND Date &lt;=  EndDate</code>
     * @param pDateToCheck the date to check
     * @param pTimeRangeStart the pStart of the time period
     * @param pTimeRangeEnd the finish of the time period
     * @param pUseTime false will ignore all time informations (hh:mm:ss)
     *                true uses the original time informations
     * @return true if the date to check is between the period
     */
    public static boolean isDateInRange(
            Date pDateToCheck, Date pTimeRangeStart, Date pTimeRangeEnd,
            boolean pUseTime) {

        if (pDateToCheck == null || pTimeRangeStart == null || pTimeRangeEnd == null) {
            throw new IllegalArgumentException("cannot handle null parameters!");
        }

        if (pUseTime) {
            return (pDateToCheck.compareTo(pTimeRangeStart) >= 0) && (pDateToCheck.compareTo(pTimeRangeEnd) <= 0);
        } else {
            long rtdateCheck = removeTime(pDateToCheck);
            long rtdateStart = removeTime(pTimeRangeStart);
            long rtdateEnd = removeTime(pTimeRangeEnd);

            return (rtdateCheck >= rtdateStart) && (rtdateCheck <= rtdateEnd);
        }

    }


    /**
     * Removes the whole time informations from a date.
     * Resets (to '0'): hours, minutes, seconds, miliseconds
     * @param pSource Date to remove times info
     * @return a java long time value
     */
    public static long removeTime(Date pSource) {
        Calendar calendar = removeTime(pSource, ONE_DAY);
        releaseCalendar(calendar);
        return calendar.getTimeInMillis();
    }


    /**
     * Removes the whole time informations from a date.
     * Resets (to '0'): hours, minutes, seconds, miliseconds
     * @param pSource Date to remove times info
     * @return a java long time value
     */
    private static Calendar removeTime(Date pSource, long pUnit) {
        Calendar calendar = getCalendar();
        calendar.setTime(pSource);
        if (pUnit >= ONE_SECOND) {
            calendar.set(Calendar.MILLISECOND, 0);
        }
        if (pUnit >= ONE_MINUTE) {
            calendar.set(Calendar.SECOND, 0);
        }
        if (pUnit >= ONE_HOUR) {
            calendar.set(Calendar.MINUTE, 0);
        }
        if (pUnit >= ONE_DAY) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
        }
        return calendar;
    }


    /**
     * Removes the whole time informations from a date.
     * Resets (to '0'): hours, minutes, seconds, miliseconds
     * @param pSource Date to remove times info
     * @return a date object with no times
     */
    public static Date removeTimeAsDate(Date pSource) {
        return new Date(removeTime(pSource));
    }


    /**
     * put pHour and minutes from pSourcedate (param1) to pTargetdate (param2).
     * @param pTargetdate date to edit/change
     * @param pSourcedate date from which is being read
     * @return a new date object with the merged time and date informations
     */
    public static Date copyTime(Date pSourcedate, Date pTargetdate) {
        Calendar calendar = getCalendar();

        calendar.setTime(pSourcedate);          //read time information from pSource
        int hour_start = calendar.get(Calendar.HOUR_OF_DAY);
        int minute_start = calendar.get(Calendar.MINUTE);
        int sec_start = calendar.get(Calendar.SECOND);
        int msec_start = calendar.get(Calendar.MILLISECOND);

        calendar.setTime(pTargetdate);     //put time information to target
        calendar.set(Calendar.HOUR_OF_DAY, hour_start);
        calendar.set(Calendar.MINUTE, minute_start);
        calendar.set(Calendar.SECOND, sec_start);
        calendar.set(Calendar.MILLISECOND, msec_start);
        releaseCalendar(calendar);
        return calendar.getTime();
    }


    /**
     * computes the days <b>between</b> two dates.<br>
     * it removes the time informations, substracts the finish date from
     * the Start date and converts it from ms to days.<br>
     * <b>number of days between the same Day returns '0'<br>
     * 01.Jan to 10.Jan = 9 days (not 10!)</b><br>
     * @param pStart begin
     * @param pEnd finish
     * @return amount of days between
     */
    public static long daysbetween(Date pStart, Date pEnd) {
        if (pStart == null || pEnd == null) {
            throw new IllegalArgumentException("The function daysbetween cannot handle NULL parameters!");
        }
        return between(pStart, pEnd, ONE_DAY);
    }


    /**
     * computes the hours <b>between</b> two dates.<br>
     * minute, second and milisec. information will be deleted.
     * it substracts the finish date from
     * the pStart date and converts it from ms to hours.<br>
     * @param pStart begin
     * @param pEnd finish
     * @return amount of hours between
     */
    public static long hoursbetween(Date pStart, Date pEnd) {
        if (pStart == null || pEnd == null) {
            throw new IllegalArgumentException("The function hoursbetween cannot handle NULL parameters!");
        }
        return between(pStart, pEnd, ONE_HOUR);
    }


    private static long between(Date pStart, Date pEnd, long pUnit) {

        Calendar calendar = removeTime(pStart, pUnit);

        int dstOffsetStart = calendar.get(Calendar.DST_OFFSET) + calendar.get(Calendar.ZONE_OFFSET);
        long lnStart = calendar.getTimeInMillis();
releaseCalendar(calendar);
        calendar = removeTime(pEnd, pUnit);

        int dstOffsetEnd = calendar.get(Calendar.DST_OFFSET) + calendar.get(Calendar.ZONE_OFFSET);
        long lnFinish = calendar.getTimeInMillis();
releaseCalendar(calendar);
        //correcture needed if pStart and finish are in different timezones (CET/CEST):
        if (dstOffsetStart == dstOffsetEnd) {
            return (lnFinish - lnStart) / pUnit;
        } else { //eg.: one date is in wintertime
            return ((lnFinish + dstOffsetEnd) - (lnStart + dstOffsetStart)) / pUnit;
        }

    }


    /**
     * Date formatter.
     * Date formats are not synchronized. It is recommended to create 
     * separate format instances for each thread. If multiple threads
     * access a format concurrently, it must be synchronized externally.
     * @param date to format
     * @return "yyyy-MM-dd"
     */
    public synchronized static String formatDate(Date date) {
        return DFYYYYMMDD.format(date);
    }


    /**
     * Date and Time formatter.
     * Date formats are not synchronized. It is recommended to create
     * separate format instances for each thread. If multiple threads
     * access a format concurrently, it must be synchronized externally.
     * @param date to format
     * @return "yyyy-MM-dd HH:mm"
     */
    public synchronized static String formatDateTime(Date date) {
        return DFYYYYMMDDHHMM.format(date);
    }


    /**
     * Sets time informations to a date.
     * hours, minutes, seconds, miliseconds
     * @param pSource Date to remove times info
     * @param hours24 0 to 23
     * @param minutes 0 to 59
     * @param seconds 0 to 59
     * @param milis  0 to 999
     * @return a java long time value
     */
    public static long setTime(Date pSource, int hours24, int minutes, int seconds, int milis) {
        Calendar calendar = getCalendar();
        calendar.setTime(pSource);
        calendar.set(Calendar.HOUR_OF_DAY, hours24);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);
        calendar.set(Calendar.MILLISECOND, milis);
        releaseCalendar(calendar);
        return calendar.getTimeInMillis();
    }


    /**
     * Sets time informations to a date.
     * hours, minutes, seconds, miliseconds
     * @param pSource Date to remove times info
     * @param hours24 0 to 23
     * @param minutes 0 to 59
     * @param seconds 0 to 59
     * @param milis  0 to 999
     * @return a java util Date object
     */
    public static Date setTimeAsDate(Date pSource, int hours24, int minutes, int seconds, int milis) {
        return new Date(setTime(pSource, hours24, minutes, seconds, milis));
    }


    /** Rounds a java date to hours.
     * rounds up if seconds >= 30 and rounds down if < 30.
     * @param date 2009-04-04 23:23:43
     * @return 2009-04-04 23:24:00
     */
    public static Date roundDate(Date date) {
        long lngTime = date.getTime();
        lngTime = (lngTime + (ONE_MINUTE/2)) / ONE_MINUTE * ONE_MINUTE;
        return new Date(lngTime);
    }


    /** Method to retrieve a pooled calendar. */
    private static final Calendar getCalendar(){
        if (calpool.empty()) {
            return Calendar.getInstance();
        }else{
            return calpool.pop();
        }
    }

    /** Releases a unused calendar. */
    private static final void releaseCalendar(Calendar cal){
        if (calpool.contains(cal)) {
            throw new UnsupportedOperationException("Calendar released twice!");
        }
        calpool.push(cal);
    }



}//pEnd:class