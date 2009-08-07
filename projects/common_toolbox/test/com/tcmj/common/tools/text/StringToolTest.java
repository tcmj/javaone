package com.tcmj.common.tools.text;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tcmj
 */
public class StringToolTest {

    public StringToolTest() {
    }

    /**
     * Test of toCamelCase method, of class StringTool.
     */
    @Test
    public void testToCamelCase() {
        System.out.println("toUpperCamelCase");
        String s = "under_my_skin";
        String expResult = "UnderMySkin";
        String result = StringTool.toCamelCase(s);
        System.out.println("\tinput: " + s + "\toutput: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of join method, of class StringTool.
     */
    @Test
    public void testJoin() {
        System.out.println("join");
        String seperator = ".";
        String[] strings = new String[]{"a", "b", "c"};
        String expResult = "a.b.c";
        String result = StringTool.join(seperator, strings);
        assertEquals(expResult, result);
        System.out.println("\texpResult: " + expResult + "\tresult: " + result);
    }

    /**
     * Test of toString method, of class StringTool.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Object[] array = new String[]{"a", "b", "c"};
        String expResult = "a, b, c";
        String result = StringTool.toString(array);
        assertEquals(expResult, result);
        System.out.println("\texpResult: " + expResult + "\tresult: " + result);
    }

    /**
     * Test of isNotEmpty method, of class StringTool.
     */
    @Test
    public void testIsNotEmpty() {
        System.out.println("isNotEmpty");
        assertTrue(StringTool.isNotEmpty("a"));
        assertFalse(StringTool.isNotEmpty(""));
        assertFalse(StringTool.isNotEmpty(null));
        System.out.println("\ttrue: StringTool.isNotEmpty(\"a\")");
    }

    /**
     * Test of isEmpty method, of class StringTool.
     */
    @Test
    public void testIsEmpty() {
        System.out.println("isEmpty");
        assertFalse(StringTool.isEmpty("a"));
        assertTrue(StringTool.isEmpty(""));
        assertTrue(StringTool.isEmpty(null));
        System.out.println("\tfalse: StringTool.isEmpty(\"a\")");
    }

}
