/*
 * ClassGeneratorTest.java
 * JUnit based test
 *
 * Created on 1. November 2005, 03:37
 */

package org.tcmj.db.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import junit.framework.*;


/**
 *
 * @author tcmj
 */
public class ClassGeneratorTest extends TestCase {
    ClassGenerator ass;Connection co = null;
    public ClassGeneratorTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        String user="privuser", pwd="privuser";
        //String url = "jdbc:jtds:sqlserver://localhost:1433/pm41";
        //String user="sa", pwd="";
        //String url = "jdbc:jtds:sqlserver://TDEUT:1433/tcmjdb;instance=TCMJMSDE";
        String url = "jdbc:jtds:sqlserver://TDEUT:1433/inteco;instance=TCMJMSDE";
        co = DriverManager.getConnection(url,user,pwd);
        System.out.println("setUp()");
//        ClassGenerator ass = new ClassGenerator(co);
//        ass.setPackageName("generated");
        
        ass= new ClassGenerator(co);
    }
    
    protected void tearDown() throws Exception {
        if(co!=null){
            try{
                co.close();
            }catch(Exception ex){
                System.out.println("Error in closing the Connection: "+ex.getMessage());
            }
        }}
    
    public static Test suite() {
        TestSuite suite = new TestSuite(ClassGeneratorTest.class);
        
        return suite;
    }
    
    public void testCreateClass() throws Exception{
        System.out.println("testCreateClass");
        ass.createClass("Task");
    }
    
    public void testGetVersion() {
        System.out.println("testGetVersion");
        assertTrue( "GetVersion", ass != null );
    }
    
    
    public void testGetPackageName() {
        System.out.println("testGetPackageName");
        ass.setPackageName("generated");
        assertEquals("generated", ass.getPackageName());
    }
    
    
}
