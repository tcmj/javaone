/*
 * Dependencies:
 * JavaDB (Derby)
 */
package com.tcmj.common.jdbc.connect;

import com.tcmj.common.tools.xml.map.XMLMap;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;

/**
 * DBQuickConnectTest.
 * @author Administrator
 */
public class DBQuickConnectTestOnline {

    /** slf4j Logging framework. */
    private static final transient Logger logger = LoggerFactory.getLogger(XMLMap.class);
    private static final DBQuickConnect.Driver DRIVER = DBQuickConnect.Driver.JAVADB_NETWORK;
    private static final String DB = "tcmj";
    private static final String HOST = "localhost";
    //    private static final String HOST = "192.168.178.25";
    private static final String PORT = "1527";
    private static final String USER = "tcmj";
    private static final String PWD = "tcmj";

    public DBQuickConnectTestOnline() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
//    DBQuickConnect.setDriverManagerLogWriter(new PrintWriter(System.out));
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void beforeEachTest() {
        logger.info("-------------------------------------------------------------------------------------------------------");

    }

    /**
     * Test of connect method, of class DBQuickConnect.
     * @throws Exception
     */
    @Test
    public void testSelectAndExecute() throws Exception {
        System.out.println("testSelectAndExecute");


        DBQuickConnect db = connect();
        System.out.println("D" + db.getInfoFull());
        DatabaseMetaData meta = db.getConnection().getMetaData();
        System.out.println("Server name: " + meta.getDatabaseProductName());
        System.out.println("Server version: " + meta.getDatabaseProductVersion());
        System.out.println("Driver name: " + meta.getDriverName());
        System.out.println("Driver version: " + meta.getDriverVersion());
        System.out.println("JDBC major version: " + meta.getJDBCMajorVersion());
        System.out.println("JDBC minor version: " + meta.getJDBCMinorVersion());



        dropTestTable(db);
        System.out.println("B" + db.getInfoFull());

        createTestTable(db);
        System.out.println("Q" + db.getInfoFull());

        createTestData(db, 20);
        System.out.println("C" + db.getInfoFull());


        ResultSet rs = db.sqlSelect("select * from TCMJ.CONTACTS");
        while (rs.next()) {
//            System.out.println("Name: " + rs.getString(4) + " " + rs.getString(2));

            ResultSet rs2 = db.sqlSelect("select * from TCMJ.CONTACTS");
            while (rs2.next()) {

                ResultSet rs3 = db.sqlSelect("select * from TCMJ.CONTACTS");
                while (rs3.next()) {
                }
                db.closeResultSet(rs3);


            }
            db.closeResultSet(rs2);


        }
        db.closeResultSet(rs);


        System.out.println(db.getInfoFull());


        db.closeConnection();
    }

    private DBQuickConnect connect() throws Exception {
        DBQuickConnect instance = new DBQuickConnect();
        instance.setDriver(DRIVER);
        String pDBurl = instance.createURL(DB, HOST, PORT);
        instance.connect(pDBurl, USER, PWD);
        assertEquals(false, instance.isClosed());
        return instance;
    }

    private void createTestTable(DBQuickConnect db) throws SQLException {

        String sql = "CREATE TABLE CONTACTS ( " +
                "    ID INTEGER not null primary key, " +
                "    FIRST_NAME VARCHAR(50), " +
                "    LAST_NAME VARCHAR(50), " +
                "    TITLE VARCHAR(50), " +
                "    NICKNAME VARCHAR(50), " +
                "    DISPLAY_FORMAT SMALLINT, " +
                "    MAIL_FORMAT SMALLINT, " +
                "    EMAIL_ADDRESS VARCHAR(500) " +
                ") ";

        try {
            db.sqlExecution(sql);
        } catch (SQLException e) {
            logger.warn("createTestTable: " + e.getSQLState() + " " + e.getMessage());
        }

    }

    private void dropTestTable(DBQuickConnect db) throws SQLException {

        String sql = "drop table CONTACTS";

        try {
            db.sqlExecution(sql);
        } catch (SQLException e) {
            logger.warn("dropTestTable: " + e.getSQLState() + " " + e.getMessage());
        }

    }

    private void createTestData(DBQuickConnect db, int amount) throws SQLException {
        String psql = "insert into CONTACTS (ID,FIRST_NAME,LAST_NAME," +
                "TITLE,NICKNAME,DISPLAY_FORMAT,MAIL_FORMAT,EMAIL_ADDRESS) " +
                "values (?,?,?,?,?,?,?,?) ";


        try {
            PreparedStatement pst = db.pstPrepareStatement(psql);
            assertNotNull(pst);
            System.out.println("N" + db.getInfoFull());

            for (int i = 1; i <= amount; i++) {

                pst.setInt(1, i);
                pst.setString(2, "First" + i);
                pst.setString(3, "Last" + i);
                pst.setString(4, i % 2 == 0 ? "Mr" : "Mrs");
                pst.setString(5, i % 5 == 0 ? "Noob" : "Boob");
                pst.setInt(6, i * 10);
                pst.setInt(7, i * 22);
                pst.setString(8, "First@Last.de");
                pst.executeUpdate();

            }

            db.closePreparedStatement(pst);

        } catch (SQLException e) {
            System.out.println("SQLState: " + e.getSQLState() + " " + e.getMessage());
        }

    }
}