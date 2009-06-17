

package com.tcmj.common.tools.text;

import com.tcmj.common.tools.text.RandomStrings;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
public class RandomStringsTest {

    public RandomStringsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of nextHex method, of class RandomStrings.
     */
    @Test
    public void testNextHex() {
        RandomStrings instance = new RandomStrings();
        String result = instance.nextHex();
        assertNotNull(result);
        System.out.println("nextHex "+result);
    }

    /**
     * Test of nextCharacter method, of class RandomStrings.
     */
    @Test
    public void testNextCharacter() {
        
        RandomStrings instance = new RandomStrings();
        char result = instance.nextCharacter();
        assertNotNull(result);
        System.out.println("nextCharacter "+result);
    }

    /**
     * Test of nextCharLowerCase method, of class RandomStrings.
     */
    @Test
    public void testNextCharLowerCase() {
        RandomStrings instance = new RandomStrings();
        char result = instance.nextCharLowerCase();
        assertNotNull(result);
        System.out.println("nextCharLowerCase "+result);
    }

    /**
     * Test of nextCharUpperCase method, of class RandomStrings.
     */
    @Test
    public void testNextCharUpperCase() {
        RandomStrings instance = new RandomStrings();
        char result = instance.nextCharUpperCase();
        assertNotNull(result);
        System.out.println("nextCharUpperCase "+result);

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
        System.out.println("nextString "+result);
        
    }

    /**
     * Test of nextStringLowerCase method, of class RandomStrings.
     */
    @Test
    public void testNextStringLowerCase() {
        int len = 10;
        RandomStrings instance = new RandomStrings();
        String result = instance.nextStringLowerCase(len);
        System.out.println("nextStringLowerCase "+result);
        assertNotNull(result);
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
        System.out.println("nextStringUpperCase "+result);
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
        System.out.println("nextStringCapitalized "+result);
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
        System.out.println("randomWord "+result);
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
        System.out.println("randomWordCapitalized "+result);

    }
    


}