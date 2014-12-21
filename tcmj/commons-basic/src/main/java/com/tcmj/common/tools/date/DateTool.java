package com.tcmj.common.tools.date;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import static com.tcmj.common.tools.lang.Check.*;

/**
 * Helper for simple time and date operations.
 * <p> use following import in your classes <br>import static com.tcmj.common.tools.date.DateTool.*;</p>
 * For professional date and time operations consider using the Joda time library.
 * @see org.apache.commons.lang.time.DateUtils
 * @author tcmj - Thomas Deutsch
 * @since 2008
 * @version $Revision: $
 */
public final class DateTool extends DateUtils {

    /** Pattern: yyyyMMdd. */
    private static final FastDateFormat DFYYYYMMDD = FastDateFormat.getInstance("yyyy-MM-dd");

    /** Pattern: yyyy-MM-dd. */
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
    public static Date date(int year, int month, int day, int hour, int minute, int second) {
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
     * @param day Day (1 to 30/31) invalid days will be rolled to the next month
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
     * @param day Day (1 to 30/31) invalid days will be rolled to the next month
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
     * @param day Day (1 to 30/31) invalid days will be rolled to the next month
     * @return a date object
     */
    public static Date date(int year, int month, int day) {
        return date(year, month, day, 0, 0, 0);
    }

    /**
     * Checks if a date falls into a given time range.<br>
     *       <code>Date &gt;=  StartDate AND Date &lt;=  EndDate</code>
     * @param dateToCheck the date to check
     * @param timeRangeStart the pStart of the time period
     * @param timeRangeEnd the finish of the time period
     * @param useTime false will ignore all time informations (hh:mm:ss)
     *                true uses the original time informations
     * @return true if the date to check is between the period
     */
    public static boolean isDateInRange(Date dateToCheck, Date timeRangeStart, Date timeRangeEnd, boolean useTime) {
        if (dateToCheck == null || timeRangeStart == null || timeRangeEnd == null) {
            throw new IllegalArgumentException("isDateInRange cannot handle null parameters!");
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
     * Null safe equal method to compare dates.
     * @param source first date to check
     * @param target second date to check
     * @return only true if the dates are both null or equal
     */
    public static boolean isDateEqual(Date source, Date target) {
        return !((source == null && target != null)
                || (source != null && target == null)
                || (source != null && target != null && !source.equals(target))); 
    }

    /**
     * Null safe equal method to compare dates.
     * @param source first date to check
     * @param target second date to check
     * @return only false if the dates are both null or equal
     */
    public static boolean isNotDateEqual(Date source, Date target) {
        return !isDateEqual(source, target);
    }

    /**
     * Removes the whole time informations from a date.
     * Resets (to '0'): hours, minutes, seconds, milliseconds
     * @param source Date to remove times info
     * @return DateUtils.truncate(source, Calendar.DATE);
     * @throws java.lang.IllegalArgumentException if the Date parameter is null
     */
    public static Date removeTime(Date source) {
        return DateUtils.truncate(source, Calendar.DATE);
    }

    /**
     * put hour and minutes from source date (param1) to target date (param2).
     * @param sourcedate date from which is being read
     * @param targetdate date to edit/change
     * @return a new date object with the merged time and date informations
     */
    public static Date copyTime(Date sourcedate, Date targetdate) {
        notNull(targetdate, "Your target date parameter may not be null!");

        Calendar calendar = getCalendar(sourcedate);
        int hourStart = calendar.get(Calendar.HOUR_OF_DAY);
        int minuteStart = calendar.get(Calendar.MINUTE);
        int secStart = calendar.get(Calendar.SECOND);
        int msecStart = calendar.get(Calendar.MILLISECOND);

        calendar.setTime(targetdate);     //put time information to target
        calendar.set(Calendar.HOUR_OF_DAY, hourStart);
        calendar.set(Calendar.MINUTE, minuteStart);
        calendar.set(Calendar.SECOND, secStart);
        calendar.set(Calendar.MILLISECOND, msecStart);

        Date adjustedDate = calendar.getTime();

        return adjustedDate;
    }

    /**
     * computes the days <b>between</b> two dates.<br>
     * it removes the time informations, subtracts the finish date from
     * the Start date and converts it from ms to days.<br>
     * <b>number of days between the same Day returns '0'<br>
     * 01.Jan to 10.Jan = 9 days (not 10!)</b><br>
     * @param start begin
     * @param end finish
     * @return amount of days between
     * @throws java.lang.IllegalArgumentException if any of the both parameters is null
     */
    public static int daysbetween(Date start, Date end) {
        return (int) between(start, end, Calendar.DATE);
    }

    /**
     * computes the hours <b>between</b> two dates.<br>
     * minute, second and millisecond. information will be deleted.
     * it subtracts the finish date from
     * the pStart date and converts it from ms to hours.<br>
     * @param start begin
     * @param end finish
     * @return amount of hours between
     * @throws java.lang.IllegalArgumentException if any of the Date parameters are null
     */
    public static long hoursbetween(Date start, Date end) {
        return between(start, end, Calendar.HOUR_OF_DAY);
    }

    /**
     * Internal method to compute the time between two dates.
     * @param start begin
     * @param end finish
     * @param calendarConstant {@link java.util.Calendar}
     * @return milliseconds
     * @throws java.lang.IllegalArgumentException if any of the Date parameters are null
     */
    private static long between(Date start, Date end, int calendarConstant) {
        notNull(start, "Parameter 'start' may not be null!");
        notNull(start, "Parameter 'end' may not be null!");
        long pUnit;
        if (Calendar.DATE == calendarConstant) {
            pUnit = MILLIS_PER_DAY;
        } else if (Calendar.HOUR_OF_DAY == calendarConstant) {
            pUnit = MILLIS_PER_HOUR;
        } else {
            pUnit = 1L;
        }

        Calendar calendar = getCalendar(DateUtils.truncate(start, calendarConstant));

        int dstOffsetStart = calendar.get(Calendar.DST_OFFSET) + calendar.get(Calendar.ZONE_OFFSET);
        long lnStart = calendar.getTimeInMillis();

        calendar.setTime(DateUtils.truncate(end, calendarConstant));

        int dstOffsetEnd = calendar.get(Calendar.DST_OFFSET) + calendar.get(Calendar.ZONE_OFFSET);
        long lnFinish = calendar.getTimeInMillis();
        //correcture needed if pStart and finish are in different timezones (CET/CEST):
        if (dstOffsetStart == dstOffsetEnd) {
            return (lnFinish - lnStart) / pUnit;
        } else { //eg.: one date is in winter time
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
     * @throws java.lang.IllegalArgumentException if the Date parameter is null
     */
    public static String formatDate(Date date) {
        notNull(date, "Cannot format a null Date object!");
        return DFYYYYMMDD.format(date);
    }

    /**
     * Date and Time formatter.
     * Date formats are not synchronized. It is recommended to create
     * separate format instances for each thread. If multiple threads
     * access a format concurrently, it must be synchronized externally.
     * @param date to format
     * @return "yyyy-MM-dd HH:mm"
     * @throws java.lang.IllegalArgumentException if the Date parameter is null
     */
    public static String formatDateTime(Date date) {
        notNull(date, "Cannot format a null Date object!");
        return DFYYYYMMDDHHMM.format(date);
    }

    /**
     * Formats milliseconds to a human readable format.
     * @param durationMillis (eg '10020000')
     * @param customlabels
     * @return eg '2 hours 47 minutes'
     */
    public static String formatDuration(long durationMillis, String... customlabels) {
//        String formatedDuration = DurationFormatUtils.formatDurationWords(timeinmillis, true, true);
//        return formatedDuration;

        String[] labels;
        if (customlabels == null || customlabels.length==0 ) {
            labels = new String[]{" ms"," sec"," min"," hours"," days"};
        }else if (customlabels.length!=5) {
            throw new IllegalArgumentException("Please provide 4 abbreviations! (eg.: {\" ms\",\" sec\",\" min\",\" hours\",\" days\"})");
        }else{
            labels = customlabels;
        }

        if (durationMillis < MILLIS_PER_SECOND) {
            return String.valueOf(durationMillis).concat(labels[0]);  //" ms"
        }
        if (durationMillis > MILLIS_PER_HOUR) {
            durationMillis = roundDate(new Date(durationMillis)).getTime();
        }


        boolean suppressLeadingZeroElements = true;
        boolean suppressTrailingZeroElements = true;


//        final String zeroMs = StringUtils.join(new String[]{"0", labels[0]});
        final String zeroSec = StringUtils.join(new String[]{" 0", labels[1]});
        final String zeroMins = StringUtils.join(new String[]{" 0", labels[2]});
        final String zeroHours = StringUtils.join(new String[]{" 0", labels[3]});
        final String zeroDays = StringUtils.join(new String[]{" 0", labels[4]});

        // This method is generally replaceable by the format method, but
        

        final String formatStrg =
                StringUtils.join(new String[]{"d'", labels[4],"' H'", labels[3],"' m'", labels[2],"' s'", labels[1],"'"});
        System.out.println("formatStrg = "+formatStrg);

//        String duration = DurationFormatUtils.formatDuration(durationMillis, "d' days 'H' hours 'm' min 's' sec'");
        String duration = DurationFormatUtils.formatDuration(durationMillis, formatStrg);
        if (suppressLeadingZeroElements) {
            // this is a temporary marker on the front. Like ^ in regexp.
            duration = " " + duration;
            String tmp = StringUtils.replaceOnce(duration, zeroDays, "");
            if (tmp.length() != duration.length()) {
                duration = tmp;
                tmp = StringUtils.replaceOnce(duration, zeroHours, "");
                if (tmp.length() != duration.length()) {
                    duration = tmp;
                    tmp = StringUtils.replaceOnce(duration, zeroMins, "");
                    duration = tmp;
                    if (tmp.length() != duration.length()) {
                        duration = StringUtils.replaceOnce(tmp, zeroSec, "");
                    }
                }
            }
            if (duration.length() != 0) {
                // strip the space off again
                duration = duration.substring(1);
            }
        }
        if (suppressTrailingZeroElements) {
            String tmp = StringUtils.replaceOnce(duration, zeroSec, "");
            if (tmp.length() != duration.length()) {
                duration = tmp;
                tmp = StringUtils.replaceOnce(duration, zeroMins, "");
                if (tmp.length() != duration.length()) {
                    duration = tmp;
                    tmp = StringUtils.replaceOnce(duration, zeroHours, "");
                    if (tmp.length() != duration.length()) {
                        duration = StringUtils.replaceOnce(tmp, zeroDays, "");
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
     * hours, minutes, seconds, milliseconds
     * @param source Date to remove times info
     * @param hours24 0 to 23
     * @param minutes 0 to 59
     * @param seconds 0 to 59
     * @param milis  0 to 999
     * @return a java long time value
     * @throws java.lang.IllegalArgumentException if the Date parameter is null
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
     * hours, minutes, seconds, milliseconds
     * @param source Date to remove times info
     * @param hours24 0 to 23
     * @param minutes 0 to 59
     * @param seconds 0 to 59
     * @param milis  0 to 999
     * @return a java util Date object
     * @throws java.lang.IllegalArgumentException if the source date parameter is null
     */
    public static Date setTimeAsDate(Date source, int hours24, int minutes, int seconds, int milis) {
        return new Date(setTime(source, hours24, minutes, seconds, milis));
    }

    /** Rounds a java date (up/down) to minutes.
     * rounds up if seconds >= 30 and rounds down if < 30.
     * @param date 2009-04-04 23:23:43
     * @return 2009-04-04 23:24:00
     * @throws java.lang.IllegalArgumentException if the Date parameter is null
     */
    public static Date roundDate(Date date) {
        return DateUtils.round(date, Calendar.MINUTE);
    }

    /** Method to retrieve a calendar.
     * @return a calendar object
     */
    public static Calendar getCalendar() {
        return Calendar.getInstance();
    }

    /** Method to retrieve a calendar.
     * @param initialdate date to set to the calendar
     * @return a calendar object
     * @throws java.lang.IllegalArgumentException if the Date parameter is null
     */
    public static Calendar getCalendar(Date initialdate) {
        notNull(initialdate, "Parameter 'initialdate' may not be null!");
        Calendar calendar = getCalendar();
        calendar.setTime(initialdate);
        return calendar;
    }

    /***
     * Iterate over the days between start and end date (both inclusive).
     * @param from 2010-01-01
     * @param until 2010-01-03
     * @return 2010-01-01, 2010-01-02, 2010-01-03
     * @throws java.lang.IllegalArgumentException if the Date parameters are null
     */
    public static Iterator<Date> iterate(Date from, Date until) {
        return new DateIterator(from, until);
    }

    /**
     * <p>Date iterator.</p>
     */
    static class DateIterator implements Iterator<Date> {

        /** Calendar. */
        private final Calendar calendar = Calendar.getInstance();

        /** End Date. */
        private final Date endDate;

        /**
         * Constructs a DateIterator that steps through all days beginning
         * from a given start date and ending at a given end date.
         * @param startDay start date (inclusive)
         * @param endDay end date (inclusive)
         * @throws java.lang.IllegalArgumentException if the Date parameters are null
         */
        DateIterator(final Date startDay, final Date endDay) {
            super();
            notNull(startDay, "Parameter startDay is null!");
            notNull(endDay, "Parameter endDay is null!");
            this.calendar.setTime(DateTool.removeTime(startDay));
            endDate = DateTool.removeTime(endDay);
        }

        /**
         * Has the iterator not reached the end date yet?
         * @return <code>true</code> if the iterator has yet to reach the end date
         */
        @Override
        public boolean hasNext() {
            return calendar.getTimeInMillis() <= endDate.getTime();
        }

        /**
         * Return the next date in the iteration.
         * @return a Date object without time (time is set to 00:00:00)
         */
        @Override
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
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}