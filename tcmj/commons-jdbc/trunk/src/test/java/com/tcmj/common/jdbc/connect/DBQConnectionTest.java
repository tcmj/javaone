/*
 * Dependencies:
 * JavaDB (Derby)
 */
package com.tcmj.common.jdbc.connect;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * DBQConnectionTest.
 * @author Administrator
 */
public class DBQConnectionTest {

    private static String mdburl =
            "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};" +
            "DBQ=.\\testdata\\DBQConnectionTest.mdb";

    private DBQConnection ginstance;

    private static DBQConnection sinstance;

    public DBQConnectionTest() {
    }

    @BeforeClass
    public static void setUpOnce() throws Exception {
        System.out.println("-----------------------------------------------------------------");
        sinstance = new DBQConnection(Driver.ACCESS_MDB);
        sinstance.setURL(mdburl);
        sinstance.connect();

        createTestTables(sinstance);
        createTestData(sinstance, 20);

        sinstance.closeConnection();
    }

    @Before
    public void setUp() throws Exception {
        System.out.println("-----------------------------------------------------------------");
        ginstance = new DBQConnection(Driver.ACCESS_MDB);
        ginstance.setURL(mdburl);
        ginstance.setDebug(true);
        ginstance.connect();
    }

    @After
    public void tearDown() throws Exception {
        ginstance.closeConnection();
    }

    private static void createTestTables(DBQConnection db) throws SQLException {


        try {
            db.sqlExecution("drop table DBQCTest");
        } catch (SQLException e) {
            System.out.println("E:dropTestTable: " + e.getSQLState() + " " + e.getMessage());
        }

        try {
            String sql = "CREATE TABLE DBQCTest ( " +
                    "    ID INTEGER not null primary key, " +
                    "    FIRST_NAME VARCHAR(50), " +
                    "    LAST_NAME VARCHAR(50), " +
                    "    BIRTH DATE" +
                    ") ";
            db.sqlExecution(sql);
        } catch (SQLException e) {
            System.out.println("E:createTestTable: " + e.getSQLState() + " " + e.getMessage());
        }

    }

    private static void createTestData(DBQConnection db, int amount) throws SQLException {
        String psql = "insert into DBQCTest (ID,FIRST_NAME,LAST_NAME,BIRTH) " +
                "values(?,?,?,?)";


        try {
            PreparedStatement pst = db.prepareStatement(psql);
            assertNotNull(pst);
            System.out.println("N" + db.getInfoFull());

            for (int i = 1; i <= amount; i++) {

                pst.setInt(1, i);
                pst.setString(2, "First" + i);
                pst.setString(3, "Last" + i);
                pst.setDate(4, new java.sql.Date(System.currentTimeMillis()));

                pst.executeUpdate();

            }

            db.closePreparedStatement(pst);

        } catch (SQLException e) {
            System.out.println("SQLState: " + e.getSQLState() + " " + e.getMessage());
        }

    }

    /**
     * Test of setDriver method, of class DBQConnection.
     */
    @Test
    public void testSetDriverClass_DBQConnectionDriver() throws Exception {
        System.out.println("setDriverClass");
        DBQConnection instance = new DBQConnection();
        instance.setDriver(Driver.ODBC);
        assertEquals(Driver.ODBC, instance.getDriver());
    }

    /**
     * Test of setDriver method, of class DBQConnection.
     */
    @Test
    public void testSetDriver_String() throws Exception {
        System.out.println("setDriverClass");
        DBQConnection instance = new DBQConnection();
        instance.setDriver("sun.jdbc.odbc.JdbcOdbcDriver");
        assertEquals(Driver.CUSTOM, instance.getDriver());
    }

    /**
     * Test of createURL method, of class DBQConnection.
     */
    @Test
    public void testCreateURL() throws Exception {
        System.out.println("testCreateURL");
        DBQConnection instance = new DBQConnection();

        boolean exceptionThrown = false;
        try {
            instance.createURL("db", "host", "port");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            exceptionThrown = true;
        }
        assertTrue("Exception because of missing driver",exceptionThrown);

        instance.setDriver(Driver.ODBC);

        String result = instance.createURL("db", null, null);

        String expResult = "jdbc:odbc:db";
        assertEquals(expResult, result);

    }

    /**
     * Test of closeResultSet method, of class DBQConnection.
     */
    @Test
    public void testCloseResultSet() throws Exception {
        System.out.println("closeResultSet");
        ResultSet pResultset = ginstance.sqlSelect("select * from contacts");
        ginstance.closeResultSet(pResultset);
//        assertTrue("ResultSet not closed", pResultset.isClosed());
    }

    /**
     * Test of sqlSelect method, of class DBQConnection.
     */
    @Test
    public void testSqlSelect() throws Exception {
        System.out.println("sqlSelect");
        ResultSet rs = ginstance.sqlSelect("select count(*) from contacts");
        int amount;
        if (rs.next()) {
            amount = rs.getInt(1);
        } else {
            amount = -1;
        }
        assertTrue((amount != -1));
        ginstance.closeResultSet(rs);
    }

    /**
     * Test of sqlExecution method, of class DBQConnection.
     */
    @Test
    public void testSqlExecution() throws Exception {
        System.out.println("sqlExecution");
        int amount = ginstance.sqlExecution("update DBQCTest set first_name = 'John' where last_name = 'Last3'");
        assertTrue((amount != 0));
    }

    /**
     * Test of pstPrepareStatement method, of class DBQConnection.
     */
    @Test
    public void testPstPrepareStatement() throws Exception {
        System.out.println("pstPrepareStatement");
        String pSQL = "select * from DBQCTest where last_name = ?";

        PreparedStatement pst = ginstance.prepareStatement(pSQL);

        pst.setString(1, "Last5");

        ResultSet rs = pst.executeQuery();

        rs.next();

        assertEquals("First5", rs.getString("first_name"));

        ginstance.closeResultSet(rs);
        ginstance.closePreparedStatement(pst);


    }

    /**
     * Test of getConnection method, of class DBQConnection.
     */
    @Test
    public void testGetConnection() {
        System.out.println("getConnection");
        DBQConnection instance = new DBQConnection();
        assertNull(instance.getConnection());
        assertNotNull(ginstance.getConnection());
    }

    /**
     * Test of getDriver method, of class DBQConnection.
     */
    @Test
    public void testGetDriver() throws Exception {
        System.out.println("getDriver");
        DBQConnection instance = new DBQConnection();
        Driver expResult = Driver.NOTSELECTED;
        assertEquals(expResult, instance.getDriver());
        instance.setDriver(Driver.ODBC);
        assertEquals(Driver.ODBC, instance.getDriver());
    }

    /**
     * Test of toString method, of class DBQConnection.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        String result = ginstance.toString();
        System.out.println(result);
        assertNotNull(result);
    }

    /**
     * Test of getUrl method, of class DBQConnection.
     */
    @Test
    public void testGetUrl() {
        System.out.println("getUrl");
        DBQConnection instance = new DBQConnection();
        assertNull(instance.getUrl());
        assertNotNull(ginstance.getUrl());
    }

    /**
     * Test of getUser method, of class DBQConnection.
     */
    @Test
    public void testGetUser() {
        System.out.println("getUser");
        DBQConnection instance = new DBQConnection();
        assertEquals(null, instance.getUser());
        instance.setUser("warren");
        assertEquals("warren", instance.getUser());
    }

    /**
     * Test of startTransaction method, of class DBQConnection.
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
     * Test of replaceQuotes method, of class DBQConnection.
     */
    @Test
    public void testReplaceQuotes() {
        System.out.println("replaceQuotes");
        String value = "tcmj's shop";
        String expResult = "tcmj''s shop";
        String result = DBQConnection.replaceQuotes(value);
        assertEquals(expResult, result);
    }

    /**
     * Test of getPassword method, of class DBQConnection.
     */
    @Test
    public void testGetPassword() {
        System.out.println("getPassword");
        DBQConnection instance = new DBQConnection();
        assertEquals(null, instance.getPassword());
        instance.setPassword("as$!");
        assertEquals("as$!", instance.getPassword());
    }

    /**
     * Test of getInfoFull method, of class DBQConnection.
     */
    @Test
    public void testGetReleaseInfo() {
        System.out.println("getReleaseInfo");
        String result = ginstance.getInfoFull();
        System.out.println(result);
        assertNotNull(result);
    }

    /**
     * Test of Observer, of class DBQConnection.
     */
    @Test
    public void testObserver() throws Exception {
        System.out.println("Observer");
        DBQConnection instance = new DBQConnection(Driver.ACCESS_MDB);
        assertEquals(0, instance.countObservers());
        ObserverImpl obse = createObserver();
        instance.addObserver(obse);
        assertEquals(1, instance.countObservers());

        instance.setURL(mdburl);


        instance.connect();
        instance.connect();
        instance.connect();



        instance.deleteObserver(obse);

        assertEquals("messages retrieved", 3, obse.getCount());

        instance.deleteObserver(obse);

        assertEquals(0, instance.countObservers());




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