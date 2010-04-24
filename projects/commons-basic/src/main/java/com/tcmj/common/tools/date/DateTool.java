package com.tcmj.common.tools.date;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.commons.lang.time.FastDateFormat;

/**
 * Helper for time and date operations.
 * <p>This class uses a static calendar pool</p>
 * <p> use following import in your classes <br>import static com.tcmj.common.tools.date.DateTool.*;</p>
 * @see org.apache.commons.lang.time.DateUtils
 * @author tcmj - Thomas Deutsch
 * @since 2008
 * @version $Revision: $
 * @JUnit com.tcmj.common.tools.date.DateHelperTest
 */
public final class DateTool extends DateUtils {

//    /** 24 hours in ms (24h x 60min x 60sec x 1000ms). */
//    public static final long ONE_DAY = 24L * 60L * 60L * 1000L;
//
//    /** 1 hour in ms (60min x 60sec x 1000ms). */
//    public static final long ONE_HOUR = 60L * 60L * 1000L;
//
//    /** 1 minute in ms (60sec x 1000ms). */
//    public static final long ONE_MINUTE = 60L * 1000L;
//
//    /** 1 second in ms (1000ms). */
//    public static final long ONE_SECOND = 1000L;

    /** Pattern: yyyyMMdd */
    private static final FastDateFormat DFYYYYMMDD = FastDateFormat.getInstance("yyyy-MM-dd");

    /** Pattern: yyyy-MM-dd */
    private static final FastDateFormat DFYYYYMMDDHHMM = FastDateFormat.getInstance("yyyy-MM-dd HH:mm");


    /** Singleton. */
    private DateTool() {
    }


    /**
     * Creates a date by the given parameters.<br>
     * milliseconds will be set to '0'
     * @param year Year (eg. 2008)
     * @param month Month (1 to 12)
     * @param day Day (1 to 30/31) invalid days will be roled to the next month
     * @param hour Hour (0 to 24)
     * @param minute (0 to 59)
     * @param second (0 to 59)
     * @return a date object
     */
    public static final Date date(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = getCalendar();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, (month - 1));
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    /**
     * Creates a date by the given parameters.<br>
     * seconds and milliseconds will be set to '0'
     * @param year Year (eg. 2008)
     * @param month Month (1 to 12)
     * @param day Day (1 to 30/31) invalid days will be roled to the next month
     * @param hour Hour (0 to 24)
     * @param minute (0 to 59)
     * @return a date object
     */
    public static Date date(int year, int month, int day, int hour, int minute) {
        return date(year, month, day, hour, minute, 0);
    }


    /**
     * Creates a date by the given parameters.<br>
     * minutes, seconds and milliseconds and  will be set to '0'
     * @param year Year (eg. 2008)
     * @param month Month (1 to 12)
     * @param day Day (1 to 30/31) invalid days will be roled to the next month
     * @param hour Hour (0 to 24)
     * @return a date object
     */
    public static Date date(int year, int month, int day, int hour) {
        return date(year, month, day, hour, 0, 0);
    }


    /**
     * Creates a date by given year, month and day parameters.<br>
     * Time information will set to '0' (hour,minutes,sec,milis)
     * @param year Year (eg. 2008)
     * @param month Month (1 to 12)
     * @param day Day (1 to 30/31) invalid days will be roled to the next month
     * @return a date object
     */
    public static Date date(int year, int month, int day) {
        return date(year, month, day, 0, 0, 0);
    }


    /**
     * Checks if a date falls into a given timerange.<br>
     *       <code>Date &gt;=  StartDate AND Date &lt;=  EndDate</code>
     * @param dateToCheck the date to check
     * @param timeRangeStart the pStart of the time period
     * @param timeRangeEnd the finish of the time period
     * @param useTime false will ignore all time informations (hh:mm:ss)
     *                true uses the original time informations
     * @return true if the date to check is between the period
     */
    public static boolean isDateInRange(
            Date dateToCheck, Date timeRangeStart, Date timeRangeEnd, boolean useTime) {

        if (dateToCheck == null || timeRangeStart == null || timeRangeEnd == null) {
            throw new IllegalArgumentException("cannot handle null parameters!");
        }

        if (useTime) {
            return (dateToCheck.compareTo(timeRangeStart) >= 0) && (dateToCheck.compareTo(timeRangeEnd) <= 0);
        } else {
            long rtdateCheck = removeTime(dateToCheck).getTime();
            long rtdateStart = removeTime(timeRangeStart).getTime();
            long rtdateEnd = removeTime(timeRangeEnd).getTime();

            return (rtdateCheck >= rtdateStart) && (rtdateCheck <= rtdateEnd);
        }

    }


    /**
     * Removes the whole time informations from a date.
     * Resets (to '0'): hours, minutes, seconds, miliseconds
     * @param source Date to remove times info
     * @return DateUtils.truncate(source, Calendar.DATE);
     */
    public static Date removeTime(Date source) {
        return DateUtils.truncate(source, Calendar.DATE);
    }


    /**
     * put hour and minutes from sourcedate (param1) to targetdate (param2).
     * @param sourcedate date from which is being read
     * @param targetdate date to edit/change
     * @return a new date object with the merged time and date informations
     */
    public static Date copyTime(Date sourcedate, Date targetdate) {

        Calendar calendar = getCalendar(sourcedate);
        int hour_start = calendar.get(Calendar.HOUR_OF_DAY);
        int minute_start = calendar.get(Calendar.MINUTE);
        int sec_start = calendar.get(Calendar.SECOND);
        int msec_start = calendar.get(Calendar.MILLISECOND);

        calendar.setTime(targetdate);     //put time information to target
        calendar.set(Calendar.HOUR_OF_DAY, hour_start);
        calendar.set(Calendar.MINUTE, minute_start);
        calendar.set(Calendar.SECOND, sec_start);
        calendar.set(Calendar.MILLISECOND, msec_start);

        Date adjustedDate = calendar.getTime();


        return adjustedDate;
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
        return between(pStart, pEnd, Calendar.DATE);
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
        return between(pStart, pEnd, Calendar.HOUR_OF_DAY);
    }


    /** Internal method to compute the time between two dates. */
    private static long between(Date pStart, Date pEnd, int calendarConstant) {

        long pUnit;
        if (Calendar.DATE == calendarConstant) {
            pUnit = MILLIS_PER_DAY;
        } else if (Calendar.HOUR_OF_DAY == calendarConstant) {
            pUnit = MILLIS_PER_HOUR;
        } else {
            pUnit = 1L;
        }

        Calendar calendar = getCalendar(DateUtils.truncate(pStart, calendarConstant));

        int dstOffsetStart = calendar.get(Calendar.DST_OFFSET) + calendar.get(Calendar.ZONE_OFFSET);
        long lnStart = calendar.getTimeInMillis();

        calendar.setTime(DateUtils.truncate(pEnd, calendarConstant));

        int dstOffsetEnd = calendar.get(Calendar.DST_OFFSET) + calendar.get(Calendar.ZONE_OFFSET);
        long lnFinish = calendar.getTimeInMillis();
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
     * Formats miliseconds to a human readable format.
     * @param timeinmillis (eg '10020000')
     * @return eg '2 hours 47 minutes'
     */
    public static final String formatDuration(long durationMillis) {
//        String formatedDuration = DurationFormatUtils.formatDurationWords(timeinmillis, true, true);
//        return formatedDuration;


        if (durationMillis < 1000) {
            return String.valueOf(durationMillis).concat(" ms");
        }
        if (durationMillis > MILLIS_PER_HOUR) {
            durationMillis = roundDate(new Date(durationMillis)).getTime();
        }


        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = true;

        // This method is generally replacable by the format method, but
        // there are a series of tweaks and special cases that require
        // trickery to replicate.
        String duration = DurationFormatUtils.formatDuration(durationMillis, "d' days 'H' hours 'm' min 's' sec'");
        if (suppressLeadingZeroElements) {
            // this is a temporary marker on the front. Like ^ in regexp.
            duration = " " + duration;
            String tmp = StringUtils.replaceOnce(duration, " 0 days", "");
            if (tmp.length() != duration.length()) {
                duration = tmp;
                tmp = StringUtils.replaceOnce(duration, " 0 hours", "");
                if (tmp.length() != duration.length()) {
                    duration = tmp;
                    tmp = StringUtils.replaceOnce(duration, " 0 min", "");
                    duration = tmp;
                    if (tmp.length() != duration.length()) {
                        duration = StringUtils.replaceOnce(tmp, " 0 sec", "");
                    }
                }
            }
            if (duration.length() != 0) {
                // strip the space off again
                duration = duration.substring(1);
            }
        }
        if (suppressTrailingZeroElements) {
            String tmp = StringUtils.replaceOnce(duration, " 0 sec", "");
            if (tmp.length() != duration.length()) {
                duration = tmp;
                tmp = StringUtils.replaceOnce(duration, " 0 min", "");
                if (tmp.length() != duration.length()) {
                    duration = tmp;
                    tmp = StringUtils.replaceOnce(duration, " 0 hours", "");
                    if (tmp.length() != duration.length()) {
                        duration = StringUtils.replaceOnce(tmp, " 0 days", "");
                    }
                }
            }
        }
        // handle plurals
        duration = " " + duration;
//        duration = StringUtils.replaceOnce(duration, " 1 seconds", " 1 second");
//        duration = StringUtils.replaceOnce(duration, " 1 minutes", " 1 minute");
        duration = StringUtils.replaceOnce(duration, " 1 hours", " 1 hour");
        duration = StringUtils.replaceOnce(duration, " 1 days", " 1 day");
        return duration.trim();

    }


    /**
     * Sets time informations to a date.
     * hours, minutes, seconds, miliseconds
     * @param source Date to remove times info
     * @param hours24 0 to 23
     * @param minutes 0 to 59
     * @param seconds 0 to 59
     * @param milis  0 to 999
     * @return a java long time value
     */
    public static long setTime(Date source, int hours24, int minutes, int seconds, int milis) {
        Calendar calendar = getCalendar(source);
        calendar.set(Calendar.HOUR_OF_DAY, hours24);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, seconds);
        calendar.set(Calendar.MILLISECOND, milis);
        long newtime = calendar.getTimeInMillis();
        return newtime;
    }


    /**
     * Sets time informations to a date.
     * hours, minutes, seconds, miliseconds
     * @param source Date to remove times info
     * @param hours24 0 to 23
     * @param minutes 0 to 59
     * @param seconds 0 to 59
     * @param milis  0 to 999
     * @return a java util Date object
     */
    public static Date setTimeAsDate(Date source, int hours24, int minutes, int seconds, int milis) {
        return new Date(setTime(source, hours24, minutes, seconds, milis));
    }


    /** Rounds a java date (up/down) to minutes.
     * rounds up if seconds >= 30 and rounds down if < 30.
     * @param date 2009-04-04 23:23:43
     * @return 2009-04-04 23:24:00
     */
    public static Date roundDate(Date date) {
        return DateUtils.round(date, Calendar.MINUTE);
    }


    /** Method to retrieve a calendar.
     * @return a calendar object
     */
    public static final Calendar getCalendar() {
        return Calendar.getInstance();
    }


    /** Method to retrieve a calendar.
     * @param initialdate date to set to the calendar
     * @return a calendar object
     */
    public static final Calendar getCalendar(Date initialdate) {
        Calendar calendar = getCalendar();
        calendar.setTime(initialdate);
        return calendar;
    }


    /***
     * Iterate over the days between start and enddate (both inclusive)
     * @param from 2010-01-01
     * @param until 2010-01-03
     * @return 2010-01-01, 2010-01-02, 2010-01-03
     */
    public static final Iterator<Date> iterate(Date from, Date until) {
        if (from == null || until == null) {
            throw new UnsupportedOperationException("The function iterate cannot handle NULL parameters!");
        }
        return new DateIterator(from, until);
    }

    /**
     * <p>Date iterator.</p>
     */
    static class DateIterator implements Iterator<Date> {

        private Calendar calendar = Calendar.getInstance();

        private final Date endDate;


        /**
         * Constructs a DateIterator that steps throug all days beginning
         * from a given start date and ending at a given end date.
         * @param startDay start date (inclusive)
         * @param endDay end date (inclusive)
         */
        DateIterator(java.util.Date startDay, java.util.Date endDay) {
            super();
            this.calendar.setTime(DateTool.removeTime(startDay));
            endDate = DateTool.removeTime(endDay);
        }


        /**
         * Has the iterator not reached the end date yet?
         * @return <code>true</code> if the iterator has yet to reach the end date
         */
        public boolean hasNext() {
            return calendar.getTimeInMillis() <= endDate.getTime();
        }


        /**
         * Return the next date in the iteration
         * @return a Date object without time (time is set to 00:00:00)
         */
        public Date next() {
            if (calendar.getTimeInMillis() > endDate.getTime()) {
                throw new NoSuchElementException();
            }
            Date currentDate = calendar.getTime();
            calendar.add(Calendar.DATE, 1);
            return currentDate;
        }


        /**
         * Unsupported Operation.
         *
         * @throws UnsupportedOperationException
         * @see java.util.Iterator#remove()
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}

