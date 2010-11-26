/*
 * DateToolTest.java
 * JUnit based test
 *
 * Created on 26. August 2008, 15:40
 */
package com.tcmj.common.tools.date;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import static junit.framework.Assert.*;
import org.junit.Test;

/**
 * DateToolTest.
 * @author tcmj
 */
public class DateToolTest {

    @Test
    public void testIsDateInRange() {
        System.out.println("isDateInRange");

        final boolean DO_NOT_USE_TIME = false;
        final boolean USE_TIME = true;

//USING NO TIME - JUST DATES:

        //Date before Start  (no time)
        assertEquals("case 1: date is one day before range start", false, //<- expected result
                DateTool.isDateInRange(
                DateTool.date(2007, 12, 31), //<- date to check
                DateTool.date(2008, 1, 1), DateTool.date(2008, 12, 31), DO_NOT_USE_TIME) //<- do not use time
                );

        //Date == Start (no time)
        assertEquals("case 2: date is same day as range start", true, //<- expected result
                DateTool.isDateInRange(
                DateTool.date(2008, 1, 1), //<- date to check
                DateTool.date(2008, 1, 1), DateTool.date(2008, 12, 31), DO_NOT_USE_TIME) //<- do not use time
                );

        //(Date > Start) & (Date < End)  (no time)
        assertEquals("case 3: date is in range", true, //<- expected result
                DateTool.isDateInRange(
                DateTool.date(2008, 1, 2), //<- date to check
                DateTool.date(2008, 1, 1), DateTool.date(2008, 12, 31), DO_NOT_USE_TIME) //<- do not use time
                );

        //Date == Ende (no time)
        assertEquals("case 4: date is same day as range end", true, //<- expected result
                DateTool.isDateInRange(
                DateTool.date(2008, 12, 31), //<- date to check
                DateTool.date(2008, 1, 1), DateTool.date(2008, 12, 31), DO_NOT_USE_TIME) //<- do not use time
                );

        //Date > Ende (no time)
        assertEquals("case 5: date is after range end", false, //<- expected result
                DateTool.isDateInRange(
                DateTool.date(2009, 1, 1), //<- date to check
                DateTool.date(2008, 1, 1), DateTool.date(2008, 12, 31), DO_NOT_USE_TIME) //<- do not use time
                );

//USING DATES WITH TIME:

        //Date before Start  
        assertEquals("BS-case 1: date is before range start", false, //<- expected result
                DateTool.isDateInRange(
                DateTool.date(2007, 12, 31, 13, 45, 22), //<- date to check
                DateTool.date(2008, 1, 1), DateTool.date(2008, 12, 31), USE_TIME) //<- use time!
                );

        //Date before Start  
        assertEquals("BS-case 2: date is before range start", false, //<- expected result
                DateTool.isDateInRange(
                DateTool.date(2007, 12, 31), //<- date to check
                DateTool.date(2008, 1, 1, 19, 45, 22), DateTool.date(2008, 12, 31, 3, 45, 22), USE_TIME) //<- use time!
                );

        //Date before Start 
        assertEquals("BS-case 3: date is before range start", false, //<- expected result
                DateTool.isDateInRange(
                DateTool.date(2007, 12, 31, 23, 59, 59), //<- date to check
                DateTool.date(2008, 1, 1, 19, 45, 22), DateTool.date(2008, 12, 31, 3, 45, 22), DO_NOT_USE_TIME) //<- no time!
                );
        
        //Date before Start
        assertEquals("BS-case 4: date is before range start", false, //<- expected result
                DateTool.isDateInRange(
                DateTool.date(2008, 1, 1, 0, 0, 1), //<- date to check
                DateTool.date(2008, 1, 1, 0, 0, 2), DateTool.date(2008, 12, 31, 3, 45, 22), USE_TIME) //<- use time!
                );

        //Date == Start 
        assertEquals("ES-case 1", true, //<- expected result
                DateTool.isDateInRange(
                DateTool.date(2008, 1, 1, 14, 59, 59), //<- date to check
                DateTool.date(2008, 1, 1, 19, 45, 22), DateTool.date(2008, 12, 31, 3, 45, 22), DO_NOT_USE_TIME) //<- no time!
                );

        assertEquals("ES-case 2", true, //<- expected result
                DateTool.isDateInRange(
                DateTool.date(2008, 1, 1, 14, 59, 59), //<- date to check
                DateTool.date(2008, 1, 1, 14, 59, 59), DateTool.date(2008, 12, 31, 14, 59, 59), USE_TIME) //<- with time!
                );



        //Date == End 
        assertEquals("EE-case 1", true, //<- expected result
                DateTool.isDateInRange(
                DateTool.date(2008, 12, 31, 11, 59, 59), //<- date to check
                DateTool.date(2008, 1, 1, 14, 59, 59), DateTool.date(2008, 12, 31, 14, 59, 59), DO_NOT_USE_TIME) //<- no times!
                );

        //Date > End (AfterEnd)
        assertEquals("AE-case 1", false, //<- expected result
                DateTool.isDateInRange(
                DateTool.date(2009, 12, 31, 11, 59, 59), //<- date to check
                DateTool.date(2008, 1, 1, 14, 59, 59), DateTool.date(2008, 12, 31, 14, 59, 59), USE_TIME) //<- with time!
                );


        //Date is in TimeRange (OK-case)
        assertEquals("OK-case 1", true, //<- expected result
                DateTool.isDateInRange(
                DateTool.date(2007, 3, 25), //<- date to check
                DateTool.date(2007, 3, 25), DateTool.date(2007, 3, 26), USE_TIME) //<- with time!
                );
        assertEquals("OK-case 2", true, //<- expected result
                DateTool.isDateInRange(
                DateTool.date(2007, 3, 26), //<- date to check
                DateTool.date(2007, 3, 25), DateTool.date(2007, 3, 26), USE_TIME) //<- with time!
                );



    }


    @Test
    public void testCopyTime() {
        System.out.println("copyTime");
        //copy the time information (13:59:00) to the target 
        Date targetdate = DateTool.date(2009, 12, 10, 0, 0, 0);
        Date sourcedate = DateTool.date(2009, 9, 11, 13, 59, 0);
        Date expResult = DateTool.date(2009, 12, 10, 13, 59, 0);

        Date result = DateTool.copyTime(sourcedate, targetdate);
        assertEquals("case 1", String.valueOf(expResult.getTime()), String.valueOf(result.getTime()));


    }


    @Test
    public void testremoveTime() {
        System.out.println("removeTime");
        //copy the time information (13:59:00) to the target 

        Date sourcedate = DateTool.date(2009, 9, 11, 13, 59, 0);
        Date expResult = DateTool.date(2009, 9, 11, 0, 0, 0);

        Date result = DateTool.removeTime(sourcedate);

        System.out.println("expected: " + expResult);
        System.out.println("result: " + result);

        assertEquals("case 1", String.valueOf(expResult.getTime()), String.valueOf(result.getTime()));


        //TODO add more tests: aufrunden abrunden etc!!!

    }


    @Test
    public void testDaysbetween() {
        System.out.println("daysbetween");

        Date start = DateTool.date(2009, 9, 11, 13, 59, 0);
        Date end = DateTool.date(2009, 9, 11, 0, 0, 0);
        assertEquals("case 1", 0 /* days */, DateTool.daysbetween(start, end));

        start = DateTool.date(2009, 9, 11, 0, 0, 0);
        end = DateTool.date(2009, 9, 11, 13, 59, 0);
        assertEquals("case 2", 0 /* days */, DateTool.daysbetween(start, end));

        start = DateTool.date(2008, 1, 1);
        end = DateTool.date(2008, 1, 2);
        assertEquals("case 3", 1 /* days */, DateTool.daysbetween(start, end));

        start = DateTool.date(2008, 1, 1);
        end = DateTool.date(2008, 1, 10);
        assertEquals("case 4", 9 /* days */, DateTool.daysbetween(start, end));

        start = DateTool.date(1999, 8, 27);
        end = DateTool.date(2008, 8, 29);
        assertEquals("case 5", 3290 /* days */, DateTool.daysbetween(start, end));

        start = DateTool.date(1990, 12, 31);
        end = DateTool.date(2001, 1, 4);
        assertEquals("case 6", 3657 /* days */, DateTool.daysbetween(start, end));

        start = DateTool.date(1979, 2, 11);
        end = DateTool.date(1999, 12, 31);
        assertEquals("case 7", 7628 /* days */, DateTool.daysbetween(start, end));

        start = DateTool.date(1979, 2, 11);
        end = DateTool.date(2008, 3, 27);
        assertEquals("case 8", 10637 /* days */, DateTool.daysbetween(start, end));

        System.out.println("-----case 9-----> Start = CET and End = CEST!");
        start = DateTool.date(2007, 3, 25);  //<<<== Summer/Winter Time!!!!!
        end = DateTool.date(2007, 3, 26);
        System.out.println("\tStart: " + start);
        System.out.println("\tEnd:   " + end);
        assertEquals("case 9", 1 /* days */, DateTool.daysbetween(start, end));

        start = DateTool.date(2008, 3, 30); //CET   <<<== Summer/Winter Time!!!!!
        end = DateTool.date(2008, 4, 1);  //CEST
        assertEquals("case 10", 2 /* days */, DateTool.daysbetween(start, end));

        start = DateTool.date(1979, 2, 11);
        end = DateTool.date(2008, 8, 27);
        assertEquals("case 11", 10790 /* days */, DateTool.daysbetween(start, end));

        System.out.println("-----case 12-----");
        start = DateTool.date(2008, 5, 25);  //CEST      <<<== Summer/Winter Time!!!!!
        end = DateTool.date(2008, 11, 26); //CET
        System.out.println("\tStart: " + start);
        System.out.println("\tEnd:   " + end);
        assertEquals("case 12", 185 /* days */, DateTool.daysbetween(start, end));


        start = DateTool.date(1979, 2, 11);
        end = DateTool.date(2008, 8, 27);
        assertEquals("case 13", 10790 /* days */, DateTool.daysbetween(start, end));



    }


    @Test
    public void testHourssbetween() {
        System.out.println("hoursbetween");

        Date start = DateTool.date(2009, 9, 11, 13, 59, 0);
        Date end = DateTool.date(2009, 9, 11, 0, 0, 0);
        assertEquals("case 1", -13 /* hours */, DateTool.hoursbetween(start, end));

        start = DateTool.date(2009, 9, 11, 0, 0, 0);
        end = DateTool.date(2009, 9, 11, 13, 59, 0);
        assertEquals("case 2", 13 /* hours */, DateTool.hoursbetween(start, end));

        start = DateTool.date(2008, 1, 1);
        end = DateTool.date(2008, 1, 2);
        assertEquals("case 3", 24 /* hours */, DateTool.hoursbetween(start, end));

        start = DateTool.date(2008, 1, 1);
        end = DateTool.date(2008, 1, 10);
        assertEquals("case 4", 9 * 24 /* hours */, DateTool.hoursbetween(start, end));

        start = DateTool.date(1999, 8, 27);
        end = DateTool.date(2008, 8, 29);
        assertEquals("case 5", 3290 * 24 /* hours */, DateTool.hoursbetween(start, end));

        start = DateTool.date(1990, 12, 31);
        end = DateTool.date(2001, 1, 4);
        assertEquals("case 6", 3657 * 24 /* hours */, DateTool.hoursbetween(start, end));

        start = DateTool.date(1979, 2, 11);
        end = DateTool.date(1999, 12, 31);
        assertEquals("case 7", 7628 * 24 /* hours */, DateTool.hoursbetween(start, end));

        start = DateTool.date(1979, 2, 11);
        end = DateTool.date(2008, 3, 27);
        assertEquals("case 8", 10637 * 24 /* hours */, DateTool.hoursbetween(start, end));

        System.out.println("-----case 9-----> Start = CET and End = CEST!");
        start = DateTool.date(2007, 3, 24, 23);  //<<<== Summer/Winter Time!!!!!
        end = DateTool.date(2007, 3, 25, 0);
        System.out.println("\tStart: " + start);
        System.out.println("\tEnd:   " + end);
        assertEquals("case 9", 1 /* hours */, DateTool.hoursbetween(start, end));

        start = DateTool.date(2008, 3, 31, 23); //CET   <<<== Summer/Winter Time!!!!!
        end = DateTool.date(2008, 4, 1, 0);  //CEST
        assertEquals("case 10", 1 /* hours */, DateTool.hoursbetween(start, end));

        start = DateTool.date(1979, 2, 11);
        end = DateTool.date(2008, 8, 27);
        assertEquals("case 11", 10790 * 24 /* hours */, DateTool.hoursbetween(start, end));

        System.out.println("-----case 12-----");
        start = DateTool.date(2008, 5, 25);  //CEST      <<<== Summer/Winter Time!!!!!
        end = DateTool.date(2008, 11, 26); //CET
        System.out.println("\tStart: " + start);
        System.out.println("\tEnd:   " + end);
        assertEquals("case 12", 185 * 24 /* hours */, DateTool.hoursbetween(start, end));


        start = DateTool.date(1979, 2, 11);
        end = DateTool.date(2008, 8, 27);
        assertEquals("case 13", 10790 * 24 /* hours */, DateTool.hoursbetween(start, end));



    }


    @Test
    public void testFormatDate() {
        System.out.println("formatDate");
        //copy the time information (13:59:00) to the target
        Date date1 = DateTool.date(2009, 12, 10, 0, 0, 0);
        Date date2 = DateTool.date(2009, 9, 11, 13, 59, 0);

        assertEquals("formatDate 1", "2009-12-10", DateTool.formatDate(date1));
        assertEquals("formatDate 2", "2009-09-11", DateTool.formatDate(date2));

    }


    @Test
    public void testFormatDateTime() {
        System.out.println("formatDateTime");
        //copy the time information (13:59:00) to the target
        Date date1 = DateTool.date(2009, 12, 10, 0, 0, 0);
        Date date2 = DateTool.date(2009, 9, 11, 13, 59, 0);

        assertEquals("formatDateTime 1", "2009-12-10 00:00", DateTool.formatDateTime(date1));
        assertEquals("formatDateTime 2", "2009-09-11 13:59", DateTool.formatDateTime(date2));

    }

    @Test
    public void testFormatDuration() {
        System.out.println("formatDuration");

        assertEquals("-10 ms", DateTool.formatDuration(-10L));
        assertEquals("0 ms", DateTool.formatDuration(0L));
        assertEquals("1 ms", DateTool.formatDuration(1L));
        assertEquals("50 ms", DateTool.formatDuration(50L));
        assertEquals("900 ms", DateTool.formatDuration(900L));
        assertEquals("1 sec", DateTool.formatDuration(1000L));
        assertEquals("1 sec", DateTool.formatDuration(1500L));
        assertEquals("10 sec", DateTool.formatDuration(10000L));
        assertEquals("59 sec", DateTool.formatDuration(59000L));
        assertEquals("1 min", DateTool.formatDuration(60000L));
        assertEquals("1 min 5 sec", DateTool.formatDuration(65000L));
        assertEquals("10 min 50 sec", DateTool.formatDuration(650000L));
        assertEquals("1 hour 49 min", DateTool.formatDuration(6540000L));
        assertEquals("1 hour 50 min", DateTool.formatDuration(6590000L));
        assertEquals("1 hour 50 min", DateTool.formatDuration(6600000L));

    }


    @Test(expected = IllegalArgumentException.class)
    public void testFormatDurationCustom() {
        System.out.println("testFormatDurationCustom");

        String[] labels = new String[]{"ms","s","m","h","d"};
        
        assertEquals("-10ms", DateTool.formatDuration(-10L, labels));
        assertEquals("0ms", DateTool.formatDuration(0L, labels));
        assertEquals("1ms", DateTool.formatDuration(1L, labels));
        assertEquals("50ms", DateTool.formatDuration(50L, labels));
        assertEquals("900ms", DateTool.formatDuration(900L, labels));
        assertEquals("1s", DateTool.formatDuration(1000L, labels));
        assertEquals("1s", DateTool.formatDuration(1500L, labels));
        assertEquals("10s", DateTool.formatDuration(10000L, labels));
        assertEquals("59s", DateTool.formatDuration(59000L, labels));
        assertEquals("1m", DateTool.formatDuration(60000L, labels));
        assertEquals("1m 5s", DateTool.formatDuration(65000L, labels));
        assertEquals("10m 50s", DateTool.formatDuration(650000L, labels));
        assertEquals("1h 49m", DateTool.formatDuration(6540000L, labels));
        assertEquals("1h 50m", DateTool.formatDuration(6590000L, labels));
        assertEquals("1h 50m", DateTool.formatDuration(6600000L, labels));


        //Throw an exception now
        DateTool.formatDuration(6600000L, "ms","s");

    }




    @Test
    public void testRoundDate() {
        System.out.println("roundDate");
        //round the time information (13:59:00) to the target
        Date given = DateTool.date(2009, 12, 10, 0, 0, 0);
        Date expected = DateTool.date(2009, 12, 10, 0, 0, 0);
        Date result = DateTool.roundDate(given);
        System.out.println("given=" + (given) + " result=" + (result));
        assertEquals("case 1", expected, result);

        given = DateTool.date(2009, 12, 10, 13, 59, 50);
        expected = DateTool.date(2009, 12, 10, 14, 0, 0);
        result = DateTool.roundDate(given);
        System.out.println("given=" + (given) + " result=" + (result));
        assertEquals("case 2", expected, result);

        given = DateTool.date(2009, 12, 10, 13, 59, 30);
        expected = DateTool.date(2009, 12, 10, 14, 0, 0);
        result = DateTool.roundDate(given);
        System.out.println("given=" + (given) + " result=" + (result));
        assertEquals("case 3", expected, result);

        given = DateTool.date(2009, 12, 10, 13, 59, 29);
        expected = DateTool.date(2009, 12, 10, 13, 59, 0);
        result = DateTool.roundDate(given);
        System.out.println("given=" + (given) + " result=" + (result));
        assertEquals("case 4", expected, result);
    }


    @Test
    public void testSomeBasicCalendarFunctions() {
        System.out.println("testSomeBasicCalendarFunctions");

        Calendar cal = Calendar.getInstance();
        System.out.println("InitialCalendarValue: "+cal.getTime().toString());
        cal.set(Calendar.MILLISECOND, 433);
        cal.set(Calendar.SECOND, 55);
        cal.set(Calendar.HOUR_OF_DAY, 13);
        cal.set(Calendar.MINUTE, 20);

        System.out.println("CalendarValueSetToSpecificDate: " + cal.getTime().toString());
        cal.clear();
        System.out.println("CalendarValueAfterClear: " + cal.getTime().toString());
        System.out.println("CalendarValueAfterClear: " + cal.getTimeInMillis());

        cal.setTimeInMillis(0L);
        System.out.println("Time if you set millies to 0: "+cal.getTime().toString());
        System.out.println("Time if you set millies to 0: " + cal.getTimeInMillis());
    }




    @Test
    public void testIterate() {
        System.out.println("testIterate");
        Date dateS = DateTool.date(2010, 1, 1, 0, 0, 2);
        Date dateE = DateTool.date(2010, 1, 5, 3, 45, 22);
        Iterator<Date> dayIterator = DateTool.iterate(dateS, dateE);
        Date day1 = dayIterator.next();
        System.out.println(""+day1);
        assertEquals("case 1", String.valueOf(DateTool.date(2010, 1, 1)), String.valueOf(day1));

        Date day2 = dayIterator.next();
        System.out.println(""+day2);
        assertEquals("case 2", String.valueOf(DateTool.date(2010, 1, 2)), String.valueOf(day2));

        Date day3 = dayIterator.next();
        System.out.println(""+day3);
        assertEquals("case 3", String.valueOf(DateTool.date(2010, 1, 3)), String.valueOf(day3));

        Date day4 = dayIterator.next();
        System.out.println(""+day4);
        assertEquals("case 4", String.valueOf(DateTool.date(2010, 1, 4)), String.valueOf(day4));

        Date day5 = dayIterator.next();
        System.out.println(""+day5);
        assertEquals("case 5", String.valueOf(DateTool.date(2010, 1, 5)), String.valueOf(day5));

        try {
            Date day6 = dayIterator.next();
            fail("there is no day 6 - a exception should be thrown!");
        } catch (Exception e) {
            System.out.println("Expected Exception: " + e );
        }




    }

@Test
    public void testIsDateEqual() {

        Calendar cal = Calendar.getInstance();
        Date target = cal.getTime();
        cal.set(Calendar.DATE, 24);
        cal.set(Calendar.MONTH, 11);
        Date source = cal.getTime();


        assertTrue("c1",DateTool.isNotDateEqual(source, target));
        assertTrue("c2",DateTool.isNotDateEqual(source, null));
        assertTrue("c3",DateTool.isNotDateEqual(null, target));
        assertFalse("c4",DateTool.isNotDateEqual(source, source));
        assertFalse("c5",DateTool.isNotDateEqual(null, null));
        
        assertFalse("c6",DateTool.isDateEqual(source, target));
        assertFalse("c7",DateTool.isDateEqual(source, null));
        assertFalse("c8",DateTool.isDateEqual(null, target));
        assertTrue("c9",DateTool.isDateEqual(source, source));
        assertTrue("c10",DateTool.isDateEqual(null, null));

    }
}
