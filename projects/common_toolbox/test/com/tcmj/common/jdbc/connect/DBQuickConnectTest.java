/*
 * Dependencies:
 * JavaDB (Derby)
 */
package com.tcmj.common.jdbc.connect;

import com.tcmj.common.jdbc.connect.DBQuickConnect.Driver;
import com.tcmj.common.tools.xml.map.XMLMap;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Observable;
import java.util.Observer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;

/**
 * DBQuickConnectTest.
 * @author Administrator
 */
public class DBQuickConnectTest {

    /** slf4j Logging framework. */
    private static final transient Logger logger = LoggerFactory.getLogger(XMLMap.class);

    private String mdburl =
    "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};" +
            "DBQ=.\\testdata\\com.tcmj.common.jdbc.connect\\DBQuickConnectTest.mdb";

    private DBQuickConnect ginstance;

    public DBQuickConnectTest() {
    }

    @Before
    public void setUp() throws Exception {
        ginstance = new DBQuickConnect();
        ginstance.setDriver(DBQuickConnect.Driver.ODBC);
        ginstance.connect(mdburl, "", "");

    }

    @After
    public void tearDown() throws Exception {
        ginstance.closeConnection();
    }

    /**
     * Test of setDriver method, of class DBQuickConnect.
     */
    @Test
    public void testSetDriverClass_DBQuickConnectDriver() throws Exception {
        System.out.println("setDriverClass");
        DBQuickConnect instance = new DBQuickConnect();
        instance.setDriver(DBQuickConnect.Driver.ODBC);
        assertEquals(DBQuickConnect.Driver.ODBC, instance.getDriver());
    }

    /**
     * Test of setDriver method, of class DBQuickConnect.
     */
    @Test
    public void testSetDriverClass_String() throws Exception {
        System.out.println("setDriverClass");
        DBQuickConnect instance = new DBQuickConnect();
        instance.setDriverClass("sun.jdbc.odbc.JdbcOdbcDriver");
        assertEquals(DBQuickConnect.Driver.NOTSELECTED, instance.getDriver());
    }

    

    /**
     * Test of createURL method, of class DBQuickConnect.
     */
    @Test
    public void testCreateURL() throws Exception {
        System.out.println("testCreateURL");
        DBQuickConnect instance = new DBQuickConnect();

        boolean exceptionThrown = false;
        try {
            String result = instance.createURL("db", "host", "port");
        } catch (Exception e) {
            logger.error(e.getMessage());
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);


        instance.setDriver(DBQuickConnect.Driver.ODBC);

        String result = instance.createURL("db", null, null);

        String expResult = "jdbc:odbc:db";
        assertEquals(expResult, result);

    }

    /**
     * Test of closeResultSet method, of class DBQuickConnect.
     */
    @Test
    public void testCloseResultSet() throws Exception {
        System.out.println("closeResultSet");
        ResultSet pResultset = ginstance.sqlSelect("select * from contacts");
        ginstance.closeResultSet(pResultset);
//        assertTrue("ResultSet not closed", pResultset.isClosed());
    }

    /**
     * Test of sqlSelect method, of class DBQuickConnect.
     */
    @Test
    public void testSqlSelect() throws Exception {
        System.out.println("sqlSelect");
        ResultSet rs = ginstance.sqlSelect("select count(*) from contacts");
        int amount;
        if (rs.next()) {
           amount  = rs.getInt(1);
        }else {
           amount  = -1;
        }
        assertTrue((amount!=-1));
        ginstance.closeResultSet(rs);
    }

    /**
     * Test of sqlExecution method, of class DBQuickConnect.
     */
    @Test
    public void testSqlExecution() throws Exception {
        System.out.println("sqlExecution");
        int amount =ginstance.sqlExecution("update contacts set firstname = 'John' where lastname = 'Wayne'");
        assertTrue((amount!=0));
    }

    /**
     * Test of pstPrepareStatement method, of class DBQuickConnect.
     */
    @Test
    public void testPstPrepareStatement() throws Exception {
        System.out.println("pstPrepareStatement");
        String pSQL = "select * from contacts where lastname = ?";
        
        PreparedStatement pst = ginstance.pstPrepareStatement(pSQL);

        pst.setString(1, "Love");

        ResultSet rs = pst.executeQuery();

        rs.next();
        
        assertEquals("Foxxy", rs.getString("firstname"));

        ginstance.closeResultSet(rs);
        ginstance.closePreparedStatement(pst);


    }

    

    


    /**
     * Test of getConnection method, of class DBQuickConnect.
     */
    @Test
    public void testGetConnection() {
        System.out.println("getConnection");
        DBQuickConnect instance = new DBQuickConnect();
        assertNull(instance.getConnection());
        assertNotNull(ginstance.getConnection());
    }

    /**
     * Test of getDriver method, of class DBQuickConnect.
     */
    @Test
    public void testGetDriver() throws Exception{
        System.out.println("getDriver");
        DBQuickConnect instance = new DBQuickConnect();
        Driver expResult = Driver.NOTSELECTED;
        assertEquals(expResult, instance.getDriver());
        instance.setDriver(Driver.ODBC);
        assertEquals(Driver.ODBC, instance.getDriver());
    }

    /**
     * Test of toString method, of class DBQuickConnect.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        DBQuickConnect instance = new DBQuickConnect();
        String result = ginstance.toString();
        System.out.println(result);
        assertNotNull(result);
    }

    /**
     * Test of getUrl method, of class DBQuickConnect.
     */
    @Test
    public void testGetUrl() {
        System.out.println("getUrl");
        DBQuickConnect instance = new DBQuickConnect();
        assertNull(instance.getUrl());
        assertNotNull(ginstance.getUrl());
    }

    /**
     * Test of getUser method, of class DBQuickConnect.
     */
    @Test
    public void testGetUser() {
        System.out.println("getUser");
        String expResult = "";
        String result = ginstance.getUser();
        assertEquals(expResult, result);
    }

    

    /**
     * Test of startTransaction method, of class DBQuickConnect.
     */
    @Test
    public void testTransaction() throws Exception {
        System.out.println("Transaction");
        ginstance.startTransaction();
        ginstance.saveTransaction();
        ginstance.startTransaction();
        ginstance.undoTransaction();
    }

    

    

    /**
     * Test of replaceQuotes method, of class DBQuickConnect.
     */
    @Test
    public void testReplaceQuotes() {
        System.out.println("replaceQuotes");
        String value = "tcmj's shop";
        String expResult = "tcmj''s shop";
        String result = DBQuickConnect.replaceQuotes(value);
        assertEquals(expResult, result);
    }

    /**
     * Test of getPassword method, of class DBQuickConnect.
     */
    @Test
    public void testGetPassword() {
        System.out.println("getPassword");
        String expResult = "";
        String result = ginstance.getPassword();
        assertEquals(expResult, result);
    }

    

    /**
     * Test of getReleaseInfo method, of class DBQuickConnect.
     */
    @Test
    public void testGetReleaseInfo() {
        System.out.println("getReleaseInfo");
        String result = ginstance.getReleaseInfo();
        System.out.println(result);
        assertNotNull( result);
    }

    /**
     * Test of Observer, of class DBQuickConnect.
     */
    @Test
    public void testObserver() throws Exception {
        System.out.println("Observer");


        assertEquals(0, ginstance.countObservers());
        ObserverImpl obse = createObserver();
        ginstance.addObserver(obse);
        assertEquals(1, ginstance.countObservers());



        ginstance.setDriver(DBQuickConnect.Driver.ODBC);
        assertEquals(0, obse.getCount());
        String pDBurl = ginstance.createURL("test", null, null);

        assertEquals(0, obse.getCount());

        ginstance.deleteObserver(obse);

        assertEquals(0, ginstance.countObservers());




    }

    private ObserverImpl createObserver() {
        return new ObserverImpl();
    }

    private class ObserverImpl implements Observer {

        public ObserverImpl() {
        }
        private int count = 0;

        public void update(Observable beobachtbarer, Object text) {
            System.out.println("Observer: " + beobachtbarer + " Object = " + text);
            count++;
        }

        /**
         * @return the count
         */
        public int getCount() {
            return count;
        }
    }
}