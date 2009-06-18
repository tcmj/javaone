/*
 * TaskTest.java
 * JUnit based test
 *
 * Created on 22. November 2005, 23:04
 */

package generated;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import junit.framework.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author tcmj
 */
public class TaskTest extends TestCase {
    Connection co = null;
    public TaskTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String user="privuser4", pwd="privuser4";
        String url ="jdbc:oracle:thin:@tdeut:1521:xe";
        
        co = DriverManager.getConnection(url,user,pwd);
    }
    
    protected void tearDown() throws Exception {
        co.close();
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(TaskTest.class);
        return suite;
    }
    
    public void testLadenUndUpdatenMitGleichenWerten() throws SQLException {
        Statement s = co.createStatement();
        
        String wheresuffix = " where task_id=35846";
        
        String e_orig = null, e_after = null;
        
        ResultSet rsA = s.executeQuery(Task.getSQLSelect().concat(wheresuffix));
        if(rsA.next()){
            Task t = new Task(rsA);
            e_orig = t.toString();
            
            System.out.println("Lade Record das erste mal:   "+e_orig);
            
            
            PreparedStatement upd = co.prepareStatement(Task.getSQLPreparedUpdate().concat(wheresuffix));
            t.fillUpdateOrInsert(upd);
            int c = upd.executeUpdate();
            
            assertEquals("Keine Zeilen upgedated oder mehr als eine!",1,c);
            
        }
        rsA.close();
        
        
        ResultSet rsB = s.executeQuery(Task.getSQLSelect().concat(wheresuffix));
        if(rsB.next()){
            Task t = new Task(rsB);
            e_after = t.toString();
            System.out.println("Lade Record nach dem update: "+e_after);
        }
        rsB.close();
        
        assertEquals("Ergebnis FALSCH! Datensätze ungleich!",e_orig,e_after);
        s.close();
    }
    
     public void testLadenUndUpdatenMitUnterschiedlichenWerten() throws SQLException {
        Statement s = co.createStatement();
        
        String wheresuffix = " where task_id=35846";
        
        String e_orig = null, e_after = null;
        
        BigDecimal awq1=null, awq2=null; 
        
        
        ResultSet rsA = s.executeQuery(Task.getSQLSelect().concat(wheresuffix));
        if(rsA.next()){
            Task t = new Task(rsA);
            e_orig = t.toString();
            
            System.out.println("Lade Record das erste mal:   "+e_orig);
            
            awq1 = t.getAct_work_qty();
            
            t.setAct_work_qty(new BigDecimal(55D));
            
            PreparedStatement upd = co.prepareStatement(Task.getSQLPreparedUpdate().concat(wheresuffix));
            t.fillUpdateOrInsert(upd);
            int c = upd.executeUpdate();
            
            assertEquals("Keine Zeilen upgedated oder mehr als eine!",1,c);
            
        }
        rsA.close();
        
        
        ResultSet rsB = s.executeQuery(Task.getSQLSelect().concat(wheresuffix));
        if(rsB.next()){
            Task t = new Task(rsB);
            awq2 = t.getAct_work_qty();
            
        }
        rsB.close();
        
        assertNotSame(awq1,awq2);
        
        assertEquals(awq2.doubleValue(),55D,1D);
        
        
        s.close();
    }
    
}
