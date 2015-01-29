package com.tcmj.pm.common.unit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 *
 * @author Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 */
public class RangeTest {

   

    @BeforeClass
    public static void setUpClass() throws Exception {
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    @Before
    public void setUp() {
    }


    @After
    public void tearDown() {
    }


    /**
     * Test of getStartMillis method, of class Range.
     */
    @Test
    public void testGetStartMillis() {
        System.out.println("getStartMillis");
        assertThat("(300, 500)", new Range(300, 500).getStartMillis(), is(300L));
        assertThat("(0, 12345)", new Range(0, 12345).getStartMillis(), is(0L));
    }


    /**
     * Test of getEndMillis method, of class Range.
     */
    @Test
    public void testGetEndMillis() {
        System.out.println("getEndMillis");
        assertThat("(300, 500)", new Range(300, 500).getEndMillis(), is(500L));
        assertThat("(0, 12345)", new Range(0, 12345).getEndMillis(), is(12345L));
    }


    /**
     * Test of getDuration method, of class Range.
     */
    @Test
    public void testGetDuration() {
        System.out.println("getDuration");
        assertThat("(300, 500)", new Range(300, 500).getDuration(), is(200L));
        assertThat("(0, 12345)", new Range(0, 12345).getDuration(), is(12345L));
    }


    /**
     * Test of contains method, of class Range.
     */
    @Test
    public void testContains_long() {
        System.out.println("contains");
        assertThat("(1, 3).contains(0)", new Range(1, 3).contains(0), is(false));
        assertThat("(1, 3).contains(1)", new Range(1, 3).contains(1), is(true));
        assertThat("(1, 3).contains(2)", new Range(1, 3).contains(2), is(true));
        assertThat("(1, 3).contains(3)", new Range(1, 3).contains(3), is(false));
        assertThat("(1, 3).contains(4)", new Range(1, 3).contains(4), is(false));
    }


    @Test(expected = NullPointerException.class)
    public final void containsNull() {
        new Range(10000, 20000).contains(null);
    }


    /**
     * Test of contains method, of class Range.
     */
    @Test
    public void testContains_Range() {
        System.out.println("contains");
        final Range range = new Range(0, 10);
        assertThat("Range contains smaller Range", range.contains(new Range(2, 3)), is(true));
        assertThat("Range contains equal Range", range.contains(new Range(0, 10)), is(true));
        assertThat("Range contains smaller Range aligned left", range.contains(new Range(0, 8)), is(true));
        assertThat("Range contains smaller Range aligned right", range.contains(new Range(2, 10)), is(true));
        assertThat("Range contains larger Range", range.contains(new Range(5, 11)), is(false));
        assertThat("Range contains smaller Range overlapping left", range.contains(new Range(-1, 4)), is(false));

    }


    /**
     * Test of overlaps method, of class Range.
     */
    @Test
    public void testOverlaps() {
        System.out.println("overlaps");
        final Range range = new Range(0, 10);
        assertThat("Range overlaps smaller Range", range.overlaps(new Range(2, 3)), is(true));
        assertThat("Range overlaps smaller Range overlapping right", range.overlaps(new Range(9, 11)), is(true));
        assertThat("Range overlaps smaller Range overlapping left", range.overlaps(new Range(-1, 1)), is(true));
        assertThat("Range overlaps Range located outside left", range.overlaps(new Range(-5, -1)), is(false));

        assertThat("Range abuts left", range.overlaps(new Range(-10, 0)), is(false));
        assertThat("Range abuts right", range.overlaps(new Range(10, 20)), is(false));

        assertThat("Range overlaps outside right", range.overlaps(new Range(11, 14)), is(false));

    }


    /**
     * Test of overlapsBefore method, of class Range.
     * //                  xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
     * //        xxxxx  xxxxx  xxxxx  xxxxx  xxxxx  xxxxx  xxxxx  xxxxx
     *           0   5  5   9

     *
     */
    @Test
    public void testOverlapsBefore() {
        System.out.println("overlapsBefore");
        final Range range = new Range(7, 20);
        assertThat("a", new Range(0, 5).overlapsBefore(range), is(false));
        assertThat("b", new Range(5, 9).overlapsBefore(range), is(true));
        assertThat("c", new Range(9, 12).overlapsBefore(range), is(false));
        assertThat("d", new Range(15, 19).overlapsBefore(range), is(false));
        assertThat("b", new Range(19, 22).overlapsBefore(range), is(false));

//        assertThat("Range abuts left", range.overlapsBefore(new Range(-10, 0)), is(false));
//        assertThat("Range abuts right", range.overlapsBefore(new Range(10, 20)), is(false));

    }


    /**
     * Test of overlapsAfter method, of class Range.
     */
    @Test
    public void testOverlapsAfter() {
        System.out.println("overlapsAfter");
        final Range range = new Range(7, 20);
        assertThat("a", new Range(0, 5).overlapsAfter(range), is(false));
        assertThat("b", new Range(5, 9).overlapsAfter(range), is(false));
        assertThat("c", new Range(9, 12).overlapsAfter(range), is(false));
        assertThat("d", new Range(15, 19).overlapsAfter(range), is(false));
        assertThat("b", new Range(19, 22).overlapsAfter(range), is(true));
        assertThat("d", new Range(22, 25).overlapsAfter(range), is(false));
    }


    /**
     * Test of overlapDuration method, of class Range.
     */
    @Test
    public void testOverlapDuration() {
        System.out.println("overlapDuration");
        assertThat("a", new Range(0, 5).overlapDuration(new Range(0, 5)), is(5L));
        assertThat("b", new Range(0, 10).overlapDuration(new Range(5, 15)), is(5L));
        assertThat("c", new Range(5, 15).overlapDuration(new Range(0, 10)), is(5L));
        assertThat("d", new Range(0, 10).overlapDuration(new Range(20, 30)), is(0L));
        assertThat("e", new Range(20, 30).overlapDuration(new Range(0, 10)), is(0L));
    }


    /**
     * Test of overlapRange method, of class Range.
     */
    @Test
    public void testOverlapRange() {
        System.out.println("overlapRange");
        assertThat("a", new Range(0, 5).overlapRange(new Range(0, 5)), equalTo(new Range(0, 5)));
        assertThat("b", new Range(0, 10).overlapRange(new Range(5, 15)), equalTo(new Range(5, 10)));
        assertThat("c", new Range(5, 15).overlapRange(new Range(0, 10)), equalTo(new Range(5, 10)));
        assertThat("d", new Range(0, 10).overlapRange(new Range(20, 30)), is((Range) null));
        assertThat("e", new Range(20, 30).overlapRange(new Range(0, 10)), is((Range) null));
    }


    /**
     * Test of isBefore method, of class Range.
     */
    @Test
    public void testIsBefore_long() {
        System.out.println("isBefore");
        assertThat("a", new Range(10, 50).isBefore(5), is(false));
        assertThat("b", new Range(10, 50).isBefore(10), is(false));
        assertThat("c", new Range(10, 50).isBefore(15), is(false));
        assertThat("d", new Range(10, 50).isBefore(50), is(true));
        assertThat("e", new Range(10, 50).isBefore(55), is(true));
    }


    /**
     * Test of isBefore method, of class Range.
     */
    @Test
    public void testIsBefore_Range() {
        System.out.println("isBefore");
        final Range range = new Range(7, 20);
        assertThat("a", new Range(10, 50).isBefore(range), is(false));
        assertThat("b", new Range(33, 50).isBefore(range), is(false));
        assertThat("c", new Range(10, 20).isBefore(range), is(false));
        assertThat("d", new Range(1, 7).isBefore(range), is(true));
        assertThat("e", new Range(1, 2).isBefore(range), is(true));
    }


    /**
     * Test of isAfter method, of class Range.
     */
    @Test
    public void testIsAfter_long() {
        System.out.println("isAfter");
        assertThat("a", new Range(10, 50).isAfter(5), is(true));
        assertThat("b", new Range(10, 50).isAfter(10), is(false));
        assertThat("c", new Range(10, 50).isAfter(15), is(false));
        assertThat("d", new Range(10, 50).isAfter(50), is(false));
        assertThat("e", new Range(10, 50).isAfter(55), is(false));
    }


    /**
     * Test of isAfter method, of class Range.
     */
    @Test
    public void testIsAfter_Range() {
        System.out.println("isAfter");
        final Range range = new Range(7, 20);
        assertThat("a", new Range(10, 50).isAfter(range), is(false));
        assertThat("b", new Range(33, 50).isAfter(range), is(true));
        assertThat("c", new Range(10, 20).isAfter(range), is(false));
        assertThat("d", new Range(1, 7).isAfter(range), is(false));
        assertThat("e", new Range(1, 2).isAfter(range), is(false));
    }


    /**
     * Test of abuts method, of class Range.
     */
    @Test
    public void testAbuts() {
        System.out.println("abuts");

        assertThat("a", Range.abuts(new Range(10, 20), new Range(20, 30)), is(true));
        assertThat("b", Range.abuts(new Range(20, 30), new Range(10, 20)), is(false));
        assertThat("c", Range.abuts(new Range(10, 20), new Range(10, 50)), is(false));
        assertThat("d", Range.abuts(new Range(1, 7), new Range(10, 50)), is(false));
        assertThat("e", Range.abuts(new Range(1, 2), new Range(10, 50)), is(false));
    }


    /**
     * Test of equals method, of class Range.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        assertThat("(1, 2).equals(1, 2)", new Range(1, 2).equals(new Range(1, 2)), is(true));
        assertThat("(1, 2).equals(1, 2)", new Range(0, 1).equals(new Range(0, 1)), is(true));
        assertThat("(1, 2).equals(1, 2)", new Range(-1, 0).equals(new Range(-1, 0)), is(true));
        assertThat("(1, 2).equals(1, 3)", new Range(1, 2).equals(new Range(1, 3)), is(false));
        assertThat("(1, 2).equals(0, 2)", new Range(1, 2).equals(new Range(0, 2)), is(false));
    }


    /**
     * Test of hashCode method, of class Range.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        final Integer hashCodeOf7 = Integer.valueOf(7).hashCode();
        final Integer hashCodeOf11 = Integer.valueOf(11).hashCode();
        assertThat("(7, 11).hashCode()", new Range(7, 11).hashCode(), is(hashCodeOf7 << 16 ^ hashCodeOf11));
    }


    /**
     * Test of toString method, of class Range.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        assertThat("(300, 500).toString()", new Range(300, 500).toString(), is("[300,500]"));
        assertThat("(0, 1000).toString()", new Range(0, 1000).toString(), is("[0,1000]"));

    }
}
