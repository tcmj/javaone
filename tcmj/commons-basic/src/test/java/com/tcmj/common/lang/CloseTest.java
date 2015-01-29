package com.tcmj.common.lang;

import com.tcmj.common.lang.Close;
import java.io.Closeable;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * CloseTest.
 * 
 * TODO think about how to test them!
 * 
 * @author tcmj - Thomas Deutsch
 */
public class CloseTest {
    
    public CloseTest() {
    }

    /**
     * Test of quiet method, of class Close.
     */
    @Test
    public void testQuiet() {
        System.out.println("quiet");
        StringReader reader = new StringReader("abc");
        Close.quiet(reader);
        // TODO review the generated test code and remove the default call to fail.
         
    }

    /**
     * Test of unchecked method, of class Close.
     */
    @Test
    public void testUnchecked() {
        System.out.println("unchecked");
        StringReader reader = new StringReader("abc");
        Close.unchecked(reader);
        // TODO review the generated test code and remove the default call to fail.
    }
}
