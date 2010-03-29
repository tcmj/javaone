package com.tcmj.common.tools.text;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * CamelCaseTest.
 * @author tcmj
 */
public class CamelCaseTest {

    public CamelCaseTest() {
    }

    /**
     * Test of toCamelCase method, of class CamelCase.
     */
    @Test
    public void toCamelCase() {
        System.out.println("Testing method toCamelCase(String)...");

        String inputstring = "under_my_skin";
        String result = CamelCase.toCamelCase(inputstring);
        System.out.println("\tinput: " + inputstring + "\toutput: " + result);
        assertEquals("UnderMySkin", result);

        inputstring = "FRAME_WORK";
        result = CamelCase.toCamelCase(inputstring);
        System.out.println("\tinput: " + inputstring + "\toutput: " + result);
        assertEquals("FrameWork", result);

    }

    /**
     * Test of toGetter method, of class CamelCase.
     */
    @Test
    public void toGetter() {
        System.out.println("Testing method toGetter(String)...");

        String input = "allinlowercase";
        String result = CamelCase.toGetter(input);
        System.out.println("\tinput: " + input + "\toutput: " + result);
        assertEquals("getAllinlowercase", result);

        input = "all_in_lower_case";
        result = CamelCase.toGetter(input);
        System.out.println("\tinput: " + input + "\toutput: " + result);
        assertEquals("getAllInLowerCase", result);

        input = "ALL_IN_UPPER_CASE";
        result = CamelCase.toGetter(input);
        System.out.println("\tinput: " + input + "\toutput: " + result);
        assertEquals("getAllInUpperCase", result);

    }

    /**
     * Test of toSetter method, of class CamelCase.
     */
    @Test
    public void toSetter() {
        System.out.println("Testing method toSetter(String)...");

        String input = "codenames";
        String result = CamelCase.toSetter(input);
        System.out.println("\tinput: " + input + "\toutput: " + result);
        assertEquals("setCodenames", result);

        input = "last_name";
        result = CamelCase.toSetter(input);
        System.out.println("\tinput: " + input + "\toutput: " + result);
        assertEquals("setLastName", result);

        input = "firstName";
        result = CamelCase.toSetter(input);
        System.out.println("\tinput: " + input + "\toutput: " + result);
        assertEquals("setFirstName", result);



    }
}
