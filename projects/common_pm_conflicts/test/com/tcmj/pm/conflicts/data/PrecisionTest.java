package com.tcmj.pm.conflicts.data;

import com.tcmj.pm.conflicts.data.Precision;
import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Administrator
 */
public class PrecisionTest {

    public PrecisionTest() {
    }


    @BeforeClass
    public static void setUpClass() throws Exception {
    }


    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    /**
     * Test of double2Long method, of class Precision.
     */
    @Test
    public void testDouble2Long() {
        System.out.println("double2Long");

        Precision instance3 = Precision.THREE_DIGITS;
        assertEquals("3", 124, instance3.double2Long(0.123584654));

        Precision instance4 = Precision.FOUR_DIGITS;
        assertEquals("4", 1236, instance4.double2Long(0.123584654));

        Precision instance5 = Precision.FIVE_DIGITS;
        assertEquals("5", 12358, instance5.double2Long(0.123584654));
    }


    /**
     * Test of long2Double method, of class Precision.
     */
    @Test
    public void testLong2Double() {
        System.out.println("long2Double");
        Precision instance3 = Precision.THREE_DIGITS;
        assertEquals(0.433d, instance3.long2Double(433), 0.0d);

        Precision instance5 = Precision.FIVE_DIGITS;
        assertEquals(0.43355d, instance5.long2Double(43355), 0.0d);
    }


    /**
     * Test of getValue method, of class Precision.
     */
    @Test
    public void testGetValue() {
        System.out.println("getValue");
        Precision instance5 = new Precision(100000L);
        assertEquals(100000L, instance5.getValue());
    }


    /**
     * Test of toString method, of class Precision.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Precision instance = Precision.FIVE_DIGITS;
        String expResult = "100000";
        String result = instance.toString();
        assertEquals(expResult, result);
    }


    @Test
    public void testSerialisation() throws Exception {
        System.out.println("testSerialisation");
        
        String serFilename = "testSerialisation.ser";

        Precision instance = new Precision(100000L);

        ObjectOutputStream objOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(serFilename)));
        objOut.writeObject(instance);
        objOut.close();

        ObjectInputStream objIn = new ObjectInputStream(new BufferedInputStream(new FileInputStream(serFilename)));
        Precision readedinstance = (Precision) objIn.readObject();
        objIn.close();

        assertEquals(100000, readedinstance.getValue());

    }




}