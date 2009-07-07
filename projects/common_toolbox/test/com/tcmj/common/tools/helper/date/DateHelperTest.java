/*
 * DateHelperTest.java
 * JUnit based test
 *
 * Created on 26. August 2008, 15:40
 */

package com.tcmj.common.tools.helper.date;


import com.tcmj.common.tools.helper.date.DateHelper;
import java.util.Date;
import static junit.framework.Assert.*;
import org.junit.Test;


/**
 *
 * @author tdeut
 */
public class DateHelperTest {
    

 
    
    @Test
    public void testIsDateInRange() {
        System.out.println("isDateInRange");

//USING NO TIME - JUST DATES:
        
        //Date before Start  (no time)
        assertEquals("case 1", false, //<- expected result
                DateHelper.isDateInRange(
                    DateHelper.date(2007,12,31), //<- date to check
                    DateHelper.date(2008,1,1), DateHelper.date(2008,12,31), false)  //<- do not use time
        );
        
        //Date == Start (no time)
        assertEquals("case 2", true, //<- expected result
                DateHelper.isDateInRange(
                    DateHelper.date(2008,1,1), //<- date to check
                    DateHelper.date(2008,1,1), DateHelper.date(2008,12,31), false)  //<- do not use time
        ); 
        
        //(Date > Start) & (Date < End)  (no time)
        assertEquals("case 3", true, //<- expected result
                DateHelper.isDateInRange(
                    DateHelper.date(2008,1,2), //<- date to check
                    DateHelper.date(2008,1,1), DateHelper.date(2008,12,31), false)  //<- do not use time
        );
        
        //Date == Ende (no time)
        assertEquals("case 4", true, //<- expected result
                DateHelper.isDateInRange(
                    DateHelper.date(2008,12,31), //<- date to check
                    DateHelper.date(2008,1,1), DateHelper.date(2008,12,31), false)  //<- do not use time
        );
        
        //Date > Ende (no time)
        assertEquals("case 5", false, //<- expected result
                DateHelper.isDateInRange(
                    DateHelper.date(2009,1,1), //<- date to check
                    DateHelper.date(2008,1,1), DateHelper.date(2008,12,31), false)  //<- do not use time
        );
        
//USING DATES WITH TIME:
        
        //Date before Start  
        assertEquals("BS-case 1", false, //<- expected result
                DateHelper.isDateInRange(
                    DateHelper.date(2007,12,31,13,45,22), //<- date to check
                    DateHelper.date(2008,1,1), DateHelper.date(2008,12,31), true)  //<- use time!
        );

        //Date before Start  
        assertEquals("BS-case 2", false, //<- expected result
                DateHelper.isDateInRange(
                    DateHelper.date(2007,12,31), //<- date to check
                    DateHelper.date(2008,1,1,19,45,22), DateHelper.date(2008,12,31,3,45,22), true)  //<- use time!
        );
         
        //Date before Start 
        assertEquals("BS-case 3", false, //<- expected result
                DateHelper.isDateInRange(
                    DateHelper.date(2007,12,31,23,59,59), //<- date to check
                    DateHelper.date(2008,1,1,19,45,22), DateHelper.date(2008,12,31,3,45,22), false)  //<- no time!
        );
        
        //Date == Start 
        assertEquals("ES-case 1", true, //<- expected result
                DateHelper.isDateInRange(
                    DateHelper.date(2008,1,1, 14,59,59), //<- date to check
                    DateHelper.date(2008,1,1, 19,45,22), DateHelper.date(2008,12,31, 3,45,22), false)  //<- no time!
        );
         
        assertEquals("ES-case 2", true, //<- expected result
                DateHelper.isDateInRange(
                    DateHelper.date(2008,1,1, 14,59,59), //<- date to check
                    DateHelper.date(2008,1,1, 14,59,59), DateHelper.date(2008,12,31, 14,59,59), true)  //<- with time!
        );
        
        
        
        //Date == End 
        assertEquals("EE-case 1", true, //<- expected result
                DateHelper.isDateInRange(
                    DateHelper.date(2008,12,31,11,59,59), //<- date to check
                    DateHelper.date(2008,1,1,14,59,59), DateHelper.date(2008,12,31,14,59,59), false)  //<- no times!
        );
        
        //Date > End (AfterEnd)
        assertEquals("AE-case 1", false, //<- expected result
                DateHelper.isDateInRange(
                    DateHelper.date(2009,12,31,11,59,59), //<- date to check
                    DateHelper.date(2008,1,1,14,59,59), DateHelper.date(2008,12,31,14,59,59), false)  //<- with time!
        );
        
        
        //Date is in TimeRange (OK-case)
        assertEquals("OK-case 1", true, //<- expected result
                DateHelper.isDateInRange(
                    DateHelper.date(2007, 3, 25), //<- date to check
                    DateHelper.date(2007, 3, 25), DateHelper.date(2007, 3, 26), true)  //<- with time!
        );
        assertEquals("OK-case 2", true, //<- expected result
                DateHelper.isDateInRange(
                    DateHelper.date(2007, 3, 26), //<- date to check
                    DateHelper.date(2007, 3, 25), DateHelper.date(2007, 3, 26), true)  //<- with time!
        );  
        
        
        
    }

    @Test
    public void testCopyTime() {
        System.out.println("copyTime");
        //copy the time information (13:59:00) to the target 
        Date targetdate = DateHelper.date(2009,12,10,   0, 0,0);
        Date sourcedate = DateHelper.date(2009, 9,11,  13,59,0);
        Date expResult  = DateHelper.date(2009,12,10,  13,59,0);
        
        Date result = DateHelper.copyTime( sourcedate, targetdate);
        assertEquals("case 1",String.valueOf(expResult.getTime()), String.valueOf(result.getTime()));
         
        
    }

    @Test
    public void testremoveTimeAsDate() {
        System.out.println("removeTimeAsDate");
        //copy the time information (13:59:00) to the target 
        
        Date sourcedate = DateHelper.date(2009,9,11,  13,59,0);
        Date expResult  = DateHelper.date(2009,9,11,   0, 0,0);
        
        Date result = DateHelper.removeTimeAsDate( sourcedate );
        assertEquals("case 1",String.valueOf(expResult.getTime()), String.valueOf(result.getTime()));
         
        
    }
    
    
    @Test
    public void testDaysbetween() {
        System.out.println("daysbetween");
        
        Date start = DateHelper.date(2009,9,11,  13,59,0);
        Date end   = DateHelper.date(2009,9,11,   0, 0,0);
        assertEquals("case 1",   0 /* days */ ,    DateHelper.daysbetween(start,end));
         
        start = DateHelper.date(2009,9,11,   0, 0,0);
        end   = DateHelper.date(2009,9,11,  13,59,0);
        assertEquals("case 2",   0 /* days */ ,    DateHelper.daysbetween(start,end));
        
        start = DateHelper.date(2008,1,1);
        end   = DateHelper.date(2008,1,2);
        assertEquals("case 3",   1 /* days */ ,    DateHelper.daysbetween(start,end));
        
        start = DateHelper.date(2008,1, 1);
        end   = DateHelper.date(2008,1,10);
        assertEquals("case 4",   9 /* days */ ,    DateHelper.daysbetween(start,end));
        
        start = DateHelper.date(1999, 8,27);
        end   = DateHelper.date(2008, 8,29);
        assertEquals("case 5",   3290 /* days */ ,    DateHelper.daysbetween(start,end));
        
        start = DateHelper.date(1990,12,31);
        end   = DateHelper.date(2001, 1, 4);
        assertEquals("case 6",   3657 /* days */ ,    DateHelper.daysbetween(start,end));
        
        start = DateHelper.date(1979, 2,11);
        end   = DateHelper.date(1999,12,31);
        assertEquals("case 7",   7628 /* days */ ,    DateHelper.daysbetween(start,end));
        
        start = DateHelper.date(1979, 2, 11);
        end   = DateHelper.date(2008, 3, 27);
        assertEquals("case 8",   10637 /* days */ ,    DateHelper.daysbetween(start,end));
        
        System.out.println("-----case 9-----> Start = CET and End = CEST!");
        start = DateHelper.date(2007, 3, 25);  //<<<== Summer/Winter Time!!!!!
        end   = DateHelper.date(2007, 3, 26);
        System.out.println("\tStart: "+start);
        System.out.println("\tEnd:   "+end);
        assertEquals("case 9",   1 /* days */ ,    DateHelper.daysbetween(start,end));
        
        start = DateHelper.date(2008, 3, 30); //CET   <<<== Summer/Winter Time!!!!!
        end   = DateHelper.date(2008, 4, 1);  //CEST
        assertEquals("case 10",   2 /* days */ ,    DateHelper.daysbetween(start,end));
  
        start = DateHelper.date(1979, 2, 11);
        end   = DateHelper.date(2008, 8, 27);
        assertEquals("case 11",   10790 /* days */ ,    DateHelper.daysbetween(start,end));
        
        System.out.println("-----case 12-----");
        start = DateHelper.date(2008, 5, 25);  //CEST      <<<== Summer/Winter Time!!!!!
        end   = DateHelper.date(2008, 11, 26); //CET
        System.out.println("\tStart: "+start);
        System.out.println("\tEnd:   "+end);
        assertEquals("case 12",   185 /* days */ ,    DateHelper.daysbetween(start,end));
        
        
        start = DateHelper.date(1979, 2, 11);
        end   = DateHelper.date(2008, 8, 27);
        assertEquals("case 13",   10790 /* days */ ,    DateHelper.daysbetween(start,end));


        
    }

    @Test
    public void testHourssbetween() {
        System.out.println("hoursbetween");

        Date start = DateHelper.date(2009, 9, 11, 13, 59, 0);
        Date end = DateHelper.date(2009, 9, 11, 0, 0, 0);
        assertEquals("case 1", -13 /* hours */, DateHelper.hoursbetween(start, end));

        start = DateHelper.date(2009, 9, 11, 0, 0, 0);
        end = DateHelper.date(2009, 9, 11, 13, 59, 0);
        assertEquals("case 2", 13 /* hours */, DateHelper.hoursbetween(start, end));

        start = DateHelper.date(2008, 1, 1);
        end = DateHelper.date(2008, 1, 2);
        assertEquals("case 3", 24 /* hours */, DateHelper.hoursbetween(start, end));

        start = DateHelper.date(2008, 1, 1);
        end = DateHelper.date(2008, 1, 10);
        assertEquals("case 4", 9*24 /* hours */, DateHelper.hoursbetween(start, end));

        start = DateHelper.date(1999, 8, 27);
        end = DateHelper.date(2008, 8, 29);
        assertEquals("case 5", 3290*24 /* hours */, DateHelper.hoursbetween(start, end));

        start = DateHelper.date(1990, 12, 31);
        end = DateHelper.date(2001, 1, 4);
        assertEquals("case 6", 3657*24 /* hours */, DateHelper.hoursbetween(start, end));

        start = DateHelper.date(1979, 2, 11);
        end = DateHelper.date(1999, 12, 31);
        assertEquals("case 7", 7628*24 /* hours */, DateHelper.hoursbetween(start, end));

        start = DateHelper.date(1979, 2, 11);
        end = DateHelper.date(2008, 3, 27);
        assertEquals("case 8", 10637*24 /* hours */, DateHelper.hoursbetween(start, end));

        System.out.println("-----case 9-----> Start = CET and End = CEST!");
        start = DateHelper.date(2007, 3, 24, 23);  //<<<== Summer/Winter Time!!!!!
        end = DateHelper.date(2007, 3, 25, 0);
        System.out.println("\tStart: " + start);
        System.out.println("\tEnd:   " + end);
        assertEquals("case 9", 1 /* hours */, DateHelper.hoursbetween(start, end));

        start = DateHelper.date(2008, 3, 31, 23); //CET   <<<== Summer/Winter Time!!!!!
        end = DateHelper.date(2008, 4, 1, 0);  //CEST
        assertEquals("case 10", 1 /* hours */, DateHelper.hoursbetween(start, end));

        start = DateHelper.date(1979, 2, 11);
        end = DateHelper.date(2008, 8, 27);
        assertEquals("case 11", 10790*24 /* hours */, DateHelper.hoursbetween(start, end));

        System.out.println("-----case 12-----");
        start = DateHelper.date(2008, 5, 25);  //CEST      <<<== Summer/Winter Time!!!!!
        end = DateHelper.date(2008, 11, 26); //CET
        System.out.println("\tStart: " + start);
        System.out.println("\tEnd:   " + end);
        assertEquals("case 12", 185*24 /* hours */, DateHelper.hoursbetween(start, end));


        start = DateHelper.date(1979, 2, 11);
        end = DateHelper.date(2008, 8, 27);
        assertEquals("case 13", 10790*24 /* hours */, DateHelper.hoursbetween(start, end));



    }

    @Test
    public void testFormatDate() {
        System.out.println("formatDate");
        //copy the time information (13:59:00) to the target
        Date date1 = DateHelper.date(2009,12,10,   0, 0,0);
        Date date2 = DateHelper.date(2009, 9,11,  13,59,0);

        assertEquals("formatDate 1","2009-12-10", DateHelper.formatDate(date1));
        assertEquals("formatDate 2","2009-09-11", DateHelper.formatDate(date2));

    }

    @Test
    public void testFormatDateTime() {
        System.out.println("formatDateTime");
        //copy the time information (13:59:00) to the target
        Date date1 = DateHelper.date(2009, 12, 10, 0, 0, 0);
        Date date2 = DateHelper.date(2009, 9, 11, 13, 59, 0);

        assertEquals("formatDateTime 1", "2009-12-10 00:00", DateHelper.formatDateTime(date1));
        assertEquals("formatDateTime 2", "2009-09-11 13:59", DateHelper.formatDateTime(date2));

    }

}
