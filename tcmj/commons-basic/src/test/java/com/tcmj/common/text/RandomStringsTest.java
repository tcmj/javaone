package com.tcmj.common.text;

import com.tcmj.common.text.RandomStrings;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * RandomStringsTest.
 * @author tcmj
 */
public class RandomStringsTest {

    public RandomStringsTest() {
    }


    /**
     * Test of nextHex method, of class RandomStrings.
     */
    @Test
    public void testNextHex() {
        RandomStrings instance = new RandomStrings();
        String result = instance.nextHex();
        assertNotNull(result);
        System.out.println("nextHex: "+result);
        assertNotSame(result, instance.nextHex());
    }

    /**
     * Test of nextCharacter method, of class RandomStrings.
     */
    @Test
    public void testNextCharacter() {
        
        RandomStrings instance = new RandomStrings();
        char result = instance.nextCharacter();
        assertNotNull(result);
        System.out.println("nextCharacter: "+result);
//        assertNotSame(result, instance.nextCharacter());
    }

    /**
     * Test of nextCharLowerCase method, of class RandomStrings.
     */
    @Test
    public void testNextCharLowerCase() {
        RandomStrings instance = new RandomStrings();
        char result1 = instance.nextCharLowerCase();
        
        assertNotNull(result1);
        
        System.out.println("nextCharLowerCase: "+result1);
    }

    /**
     * Test of nextCharUpperCase method, of class RandomStrings.
     */
    @Test
    public void testNextCharUpperCase() {
        RandomStrings instance = new RandomStrings();
        char result = instance.nextCharUpperCase();
        assertNotNull(result);
        System.out.println("nextCharUpperCase: "+result);
//        assertNotSame(result, instance.nextCharUpperCase());

    }

    /**
     * Test of nextString method, of class RandomStrings.
     */
    @Test
    public void testNextString() {
        int len = 10;
        RandomStrings instance = new RandomStrings();
        String result = instance.nextString(len);
        assertNotNull(result);
        System.out.println("nextString: "+result);
        assertNotSame(result, instance.nextString(len));
        
    }

    /**
     * Test of nextStringLowerCase method, of class RandomStrings.
     */
    @Test
    public void testNextStringLowerCase() {
        int len = 10;
        RandomStrings instance = new RandomStrings();
        String result = instance.nextStringLowerCase(len);
        assertNotNull(result);
        System.out.println("nextStringLowerCase: "+result);
        assertNotSame(result, instance.nextStringLowerCase(len));
    }

    /**
     * Test of nextStringUpperCase method, of class RandomStrings.
     */
    @Test
    public void testNextStringUpperCase() {
        int len = 10;
        RandomStrings instance = new RandomStrings();
        String result = instance.nextStringUpperCase(len);
        assertNotNull(result);
        System.out.println("nextStringUpperCase: "+result);
        assertNotSame(result, instance.nextStringUpperCase(len));
    }

    /**
     * Test of nextStringCapitalized method, of class RandomStrings.
     */
    @Test
    public void testNextStringCapitalized() {
        int len = 10;
        RandomStrings instance = new RandomStrings();
        String result = instance.nextStringCapitalized(len);
        assertNotNull(result);
        System.out.println("nextStringCapitalized: "+result);
        assertNotSame(result, instance.nextStringCapitalized(len));
    }

    /**
     * Test of randomWord method, of class RandomStrings.
     */
    @Test
    public void testRandomWord() {
        int length = 10;
        RandomStrings instance = new RandomStrings();
        String result = instance.randomWord(length);
        assertNotNull(result);
        System.out.println("randomWord: "+result);
        assertNotSame(result, instance.randomWord(length));
    }


    /**
     * Test of randomWord method, of class RandomStrings.
     */
    @Test
    public void testRandomWordCapitalized() {
        int length = 10;
        RandomStrings instance = new RandomStrings();
        String result = instance.randomWordCapitalized(length);
        assertNotNull(result);
        System.out.println("randomWordCapitalized: "+result);
        assertNotSame(result, instance.randomWordCapitalized(length));

    }
    


}