package com.tcmj.custjar;

import junit.framework.TestCase;

/**
 * CustJarTest
 * @author Thomas-Deutsch [at] tcmj [dot] de
 */
public class CustJarTest extends TestCase {
    
    public CustJarTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of main method, of class CustJar.
     */
    public void testMain() {
        System.out.println("main");
        String[] args = new String[]{
        
         "-out", "ausgabe.jar",
         "-mc",  "inteco.Saukopf",
//       "-m manifest.mf " 
//       "-v",
         "-l",
         "-lib", "CustJar.jar", "./asm.jar", "D:\\Development\\Java\\Componenten\\Hibernate\\hibernate-3.0.2\\hibernate-3.0\\lib"
        //  -mainclass
        };
        CustJar.main(args);

        
    }

}
