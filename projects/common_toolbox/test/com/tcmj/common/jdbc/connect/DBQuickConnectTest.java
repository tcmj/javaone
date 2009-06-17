/*
 * Dependencies:
 * JavaDB (Derby)
 */
package com.tcmj.common.jdbc.connect;

import com.tcmj.common.jdbc.connect.DBQuickConnect.Driver;
import com.tcmj.common.tools.xml.map.XMLMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Observable;
import java.util.Observer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
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

    public DBQuickConnectTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of setDriverClass method, of class DBQuickConnect.
     */
    @Test
    public void testSetDriverClass_DBQuickConnectDriver() throws Exception {
        System.out.println("setDriverClass");
        DBQuickConnect instance = new DBQuickConnect();
        instance.setDriverClass(DBQuickConnect.Driver.ODBC);
        assertEquals(DBQuickConnect.Driver.ODBC, instance.getDriver());

    }

    /**
     * Test of setDriverClass method, of class DBQuickConnect.
     */
    @Test
    public void testSetDriverClass_String() throws Exception {
        System.out.println("setDriverClass");
        DBQuickConnect instance = new DBQuickConnect();
        instance.setDriverClass("sun.jdbc.odbc.JdbcOdbcDriver");
        assertEquals(DBQuickConnect.Driver.NOTSELECTED, instance.getDriver());
    }

    /**
     * Test of connect method, of class DBQuickConnect.
     */
    @Test
    public void testConnect() throws Exception {
        System.out.println("connect");

        String pUser = "";
        String pPass = "";
        DBQuickConnect instance = new DBQuickConnect();

        instance.setDriverClass(DBQuickConnect.Driver.ODBC);
        String pDBurl = instance.createURL("test", null, null);

        boolean expResult = true;
        boolean result = instance.connect(pDBurl, pUser, pPass);
        assertEquals(expResult, result);

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


        instance.setDriverClass(DBQuickConnect.Driver.ODBC);

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
        ResultSet pResultset = null;
        DBQuickConnect instance = new DBQuickConnect();
        instance.closeResultSet(pResultset);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of sqlSelect method, of class DBQuickConnect.
     */
    @Test
    public void testSqlSelect() throws Exception {
        System.out.println("sqlSelect");
        String pSQL = "";
        DBQuickConnect instance = new DBQuickConnect();
        ResultSet expResult = null;
        ResultSet result = instance.sqlSelect(pSQL);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of sqlExecution method, of class DBQuickConnect.
     */
    @Test
    public void testSqlExecution() throws Exception {
        System.out.println("sqlExecution");
        String pSQL = "";
        DBQuickConnect instance = new DBQuickConnect();
        int expResult = 0;
        int result = instance.sqlExecution(pSQL);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of pstPrepareStatement method, of class DBQuickConnect.
     */
    @Test
    public void testPstPrepareStatement() throws Exception {
        System.out.println("pstPrepareStatement");
        String pSQL = "";
        DBQuickConnect instance = new DBQuickConnect();
        PreparedStatement expResult = null;
        PreparedStatement result = instance.pstPrepareStatement(pSQL);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setStmtFetchSize method, of class DBQuickConnect.
     */
    @Test
    public void testSetStmtFetchSize() throws Exception {
        System.out.println("setStmtFetchSize");
        ResultSet pResultset = null;
        int pFetchSize = 0;
        DBQuickConnect instance = new DBQuickConnect();
        instance.setStmtFetchSize(pResultset, pFetchSize);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of closeConnection method, of class DBQuickConnect.
     */
    @Test
    public void testCloseConnection() {
        System.out.println("closeConnection");
        DBQuickConnect instance = new DBQuickConnect();
        instance.closeConnection();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setDebug method, of class DBQuickConnect.
     */
    @Test
    public void testSetDebug_boolean() {
        System.out.println("setDebug");
        boolean truefalse = false;
        DBQuickConnect instance = new DBQuickConnect();
        instance.setDebug(truefalse);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getConnection method, of class DBQuickConnect.
     */
    @Test
    public void testGetConnection() {
        System.out.println("getConnection");
        DBQuickConnect instance = new DBQuickConnect();
        Connection expResult = null;
        Connection result = instance.getConnection();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDriver method, of class DBQuickConnect.
     */
    @Test
    public void testGetDriver() {
        System.out.println("getDriver");
        DBQuickConnect instance = new DBQuickConnect();
        Driver expResult = null;
        Driver result = instance.getDriver();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class DBQuickConnect.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        DBQuickConnect instance = new DBQuickConnect();
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUrl method, of class DBQuickConnect.
     */
    @Test
    public void testGetUrl() {
        System.out.println("getUrl");
        DBQuickConnect instance = new DBQuickConnect();
        String expResult = "";
        String result = instance.getUrl();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUser method, of class DBQuickConnect.
     */
    @Test
    public void testGetUser() {
        System.out.println("getUser");
        DBQuickConnect instance = new DBQuickConnect();
        String expResult = "";
        String result = instance.getUser();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setAutoCommit method, of class DBQuickConnect.
     */
    @Test
    public void testSetAutoCommit() throws Exception {
        System.out.println("setAutoCommit");
        boolean OnOff = false;
        DBQuickConnect instance = new DBQuickConnect();
        instance.setAutoCommit(OnOff);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of startTransaction method, of class DBQuickConnect.
     */
    @Test
    public void testStartTransaction() throws Exception {
        System.out.println("startTransaction");
        DBQuickConnect instance = new DBQuickConnect();
        instance.startTransaction();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of saveTransaction method, of class DBQuickConnect.
     */
    @Test
    public void testSaveTransaction() throws Exception {
        System.out.println("saveTransaction");
        DBQuickConnect instance = new DBQuickConnect();
        instance.saveTransaction();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of undoTransaction method, of class DBQuickConnect.
     */
    @Test
    public void testUndoTransaction() throws Exception {
        System.out.println("undoTransaction");
        DBQuickConnect instance = new DBQuickConnect();
        instance.undoTransaction();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of replaceQuotes method, of class DBQuickConnect.
     */
    @Test
    public void testReplaceQuotes() {
        System.out.println("replaceQuotes");
        String value = "";
        String expResult = "";
        String result = DBQuickConnect.replaceQuotes(value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPassword method, of class DBQuickConnect.
     */
    @Test
    public void testGetPassword() {
        System.out.println("getPassword");
        DBQuickConnect instance = new DBQuickConnect();
        String expResult = "";
        String result = instance.getPassword();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPassword method, of class DBQuickConnect.
     */
    @Test
    public void testSetPassword() {
        System.out.println("setPassword");
        String pass = "";
        DBQuickConnect instance = new DBQuickConnect();
        instance.setPassword(pass);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getReleaseInfo method, of class DBQuickConnect.
     */
    @Test
    public void testGetReleaseInfo() {
        System.out.println("getReleaseInfo");
        DBQuickConnect instance = new DBQuickConnect();
        String expResult = "";
        String result = instance.getReleaseInfo();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of Observer, of class DBQuickConnect.
     */
    @Test
    public void testObserver() throws Exception {
        System.out.println("Observer");


        DBQuickConnect instance = new DBQuickConnect();
        assertEquals(0, instance.countObservers());
        ObserverImpl obse = createObserver();
        instance.addObserver(obse);
        assertEquals(1, instance.countObservers());



        instance.setDriverClass(DBQuickConnect.Driver.ODBC);
        assertEquals(0, obse.getCount());
        String pDBurl = instance.createURL("test", null, null);
        instance.connect(pDBurl, "", "");


        assertEquals(1, obse.getCount());

        instance.deleteObserver(obse);

        assertEquals(0, instance.countObservers());


        instance.closeConnection();


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