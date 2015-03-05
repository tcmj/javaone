package com.tcmj.common.lang;

import com.tcmj.common.lang.Close;
import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.ResultSet;
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
        System.out.println("* quiet");
        StringReader reader = new StringReader("abc");
        Close.quiet(reader);
    }

    /**
     * Successful close test of a StringReader.
     */
    @Test
    public void testSuccessful()throws Exception {
        System.out.println("* successful");
        Close.unchecked(new StringReader("abc"));
        Close.checked(new StringReader("abc"));
        Close.quiet(new StringReader("abc"));
    }
    
    @Test
    public void testNullCheckedA()throws Exception{
        System.out.println("* checked null A");
        AutoCloseable acble = null;
        Close.checked(acble);
    }
    
    @Test
    public void testNullCheckedC()throws Exception{
        System.out.println("* checked null C");
        Closeable clble = null;
        Close.checked(clble);
    }
    
    @Test
    public void testNullUnchecked(){
        System.out.println("* unchecked null");
        Close.unchecked(null);
    }
    
    @Test
    public void testNullQuiet(){
        System.out.println("* quiet null");
        Close.quiet(null);
    }
    
    @Test(expected = RuntimeException.class)
    public void testUncheckedClose() {
        System.out.println("* uncheck a unchecked close");
        Close.unchecked(new UncheckedCloseResource());
    }
    
    @Test(expected = RuntimeException.class)
    public void testUncheckedClose2() {
        System.out.println("* uncheck a checked close");
        Close.unchecked(new CheckedCloseResource());
    }
    
    @Test(expected = IOException.class)
    public void testCheckedClose()throws Exception {
        System.out.println("* check a checked close");
        Close.checked(new CheckedCloseResource());
    }
    
    @Test(expected = Exception.class)
    public void testCheckedClose2()throws Exception {
        System.out.println("* check a unchecked close");
        Close.checked(new UncheckedCloseResource());
    }
    
    @Test
    public void testCheckedQuietClose()throws Exception {
        System.out.println("* quiet close");
        Close.quiet(new CheckedCloseResource());
        Close.quiet(new UncheckedCloseResource());
    }
    
    class UncheckedCloseResource implements AutoCloseable{
        @Override
        public void close(){
            throw new UnsupportedOperationException("Unchecked-Close-Error!");
        }
    }
    
    class CheckedCloseResource implements Closeable{
        @Override
        public void close() throws IOException {
            throw new IOException("Checked-Close-Error!");
        }
        
    }
    
}
