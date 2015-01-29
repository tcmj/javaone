package com.tcmj.common.lang;

import java.io.PrintStream;
import java.io.PrintWriter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * ExceptionAdapter Test.
 */
public class ExceptionAdapterTest {

    /**
     * Test of printStackTrace method, of class ExceptionAdapter.
     */
    @Test
    public void testPrintStackTrace() {
        System.out.println("printStackTrace");
        
        ExceptionAdapter instance = new ExceptionAdapter(new Exception("Checked!"));
        instance.printStackTrace();
        
    }

    
}
