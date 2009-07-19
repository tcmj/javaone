package com.tcmj.common.jdbc.connect;

import com.tcmj.common.jdbc.connect.intern.PreparedStmtWatcher;
import com.tcmj.common.jdbc.connect.intern.StatementCache;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JDBC database connector.
 * Extended java.sql.Connection object.
 * Contains all features of DBQuickConnect<br>
 * You can use any JDBC driver but there is a special support to:<br>
 * <code><ul><li>ODBC</li>
 * <li>Oracle</li>
 * <li>Microsoft SQL Server via JTDS</li>
 * <li>MySQl</li></ul></code><br>
 * <pre>
 * <i><b>//Declaration:</b></i>
 * DBQConnection connection = new DBQConnection();
 * <i><b>//Choose your database driver:</b></i>
 * connection.setDriver(Driver.ORACLE);
 * <i><b>//Construct your URL:</b></i>
 * String url = connection.createURL("PRIMA", "DEVORA", "1521");
 * <i><b>//Connect:</b></i>
 * connection.connect(url, "userxy", "password123");
 * <i><b>//Select-Statement:</b></i>
 * ResultSet rs = connection.sqlSelect("SELECT A,B FROM XYZ");
 * <i><b>//Loop through the result:</b></i>
 * while(rs.next()){
 *       String cell = rs.getString(1);   <i>//column: 1</i>
 * }
 * <i><b>//!!Important!! close ResultSet !!Import!!)</b></i>
 * connection.closeResultSet(rs);
 * <i><b>//Close connection</b></i>
 * connection.closeConnection();
 * </pre><br><br>
 * <p>Copyright: Copyright (c) 2003 - 2009 by tcmj</p>
 * @author Thomas Deutsch
 * @version 1.1
 * @JUnit Test available!
 */
public class DBQConnection extends Observable implements Connection {

    /** slf4j Logging framework. */
    private static final transient Logger logger = LoggerFactory.getLogger(DBQConnection.class);

    /** debug mode on/off. default = false.*/
    private boolean debug;

    /** internal driver. default = NOTSELECTED */
    protected Driver drivertype = Driver.NOTSELECTED;

    /** jdbc driver class name. */
    private String jdbcDriver;

    /** jdbc url. */
    private String jdbcURL;

    /** database username. */
    private String user;

    /** database users password. */
    private String pass;

    /** Java Connection object. */
    protected Connection connection;

    /** Statement cache.<br>contains instances of java.sql.Statement. */
    protected StatementCache mSCache;

    /** PreparedStatement Watcher. */
    protected PreparedStmtWatcher pstWatcher;

    /** Constructor. */
    public DBQConnection() {
        super();
    }

    /** Constructor.
     * @param jdbcdriver eg.: com.oracle.OracleDriver
     * @param url use createURL(String,String,String)
     * @param user database user name
     * @param pass password of the database user
     * @throws IllegalAccessException {@inheritDoc} 
     * @throws ClassNotFoundException if the jdbc driver is not in the classpath
     * @throws InstantiationException {@inheritDoc}
     */
    public DBQConnection(String jdbcdriver, String url, String user, String pass)
            throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        super();
        setDriver(jdbcdriver);
        setURL(url);
        setUser(user);
        setPassword(pass);
    }

    public DBQConnection(Driver driver)
            throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        super();
        setDriver(driver);
    }

    public DBQConnection(Driver driver, String url)
            throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        super();
        setDriver(driver);
        setURL(url);
    }

    public DBQConnection(Driver driver, String url, String user, String pass)
            throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        super();
        setDriver(driver);
        setURL(url);
        setUser(user);
        setPassword(pass);
    }

    /**Set the database driver class used to connect.
     * <b>do not use Driver.NOTSELECTED!<(b>
     * <b>do not use Driver.CUSTOM - use setDriver(String) instead!<(b>
     * @param driverclass use the Driver enum for this parameter
     * @see Driver
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public void setDriver(Driver driver)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (Driver.NOTSELECTED == driver) {
            throw new IllegalArgumentException("NOTSELECTED cannot be used as Driver !");
        } else if (Driver.CUSTOM == driver) {
            throw new IllegalArgumentException("The CUSTOM-Driver has to be used with setCustomDriver(String)!");
        } else {
            Class.forName(driver.getDriverClassName()).newInstance();
            this.drivertype = driver;
            this.jdbcDriver = driver.getDriverClassName();
        }
    }

    /** Getter for the current Driver (enum).
     * @return one of the predefined Driver enum values      */
    public Driver getDriver() {
        return drivertype;
    }

    /**Configures the Driver.CUSTOM enum with userdefined value.<br>
     * Also calls the class.forname method to register the driver.<br>
     * <b>After calling this methd you have to use the setURL method!</b>
     * @param customDriver classname of your jdbc driver as string
     */
    public void setDriver(String customDriver)
            throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        this.jdbcDriver = customDriver;
        this.drivertype = Driver.CUSTOM;
        Class.forName(this.jdbcDriver).newInstance();
    }

    /** Getter for the current used driver class name.
     * @return jdbc driver class as string  - and null if Driver.NOTSELECTED*/
    public String getDriverClass() {
        return this.jdbcDriver;
    }

    /** setter for the jdbc URL used to connect to your database. <br>
     * <b>use the createURL() method to get help building your URL</b>
     * @param myurl full qualified URL */
    public void setURL(String myurl) {
        if (this.drivertype == Driver.NOTSELECTED) {
            throw new UnsupportedOperationException("Please specify a Driver first!");
        } else {
            this.jdbcURL = myurl;
        }
    }

    /** getter for the current used url.
     * <b>if you didn't call the setURL(String) function in combination with
     * the createURL(String,String,String) method you will only get a template
     * part of the URL (which is not usable for connecting).
     * @return jdbc url - and null if no driver was selected */
    public String getUrl() {
        return this.jdbcURL;
    }

    /** @see Driver#createURL(String,String,String). */
    public String createURL(String pDB_Alias_SID, String pHost, String pPort) {
        return Driver.createURL(pDB_Alias_SID, pHost, pPort, getDriver());
    }

    /** Connects to the specified database.
     * @return true/false on success/failure (mostly true or an exception is thrown)
     * @throws java.sql.SQLException any
     */
    public boolean connect() throws java.sql.SQLException {
        return connect(getUser(), getPassword());
    }

    /** Connects to the specified database.
     * @param pUser database user (stored in a global field)
     * @param pPass password of the database user (stored in a global field)
     * @return true/false on success/failure (mostly true or an exception is thrown)
     * @throws java.sql.SQLException any
     */
    public boolean connect(String pUser, String pPass) throws java.sql.SQLException {
        this.setUser(pUser);
        this.setPassword(pPass);

        if (Driver.NOTSELECTED == this.drivertype) {
            throw new IllegalArgumentException("JDBC-Driver not set!");
        }

        if (getUrl() == null) {
            throw new UnsupportedOperationException("JDBC-URL is not set!");
        }

        if (pUser == null && pPass == null) {
            connection = DriverManager.getConnection(getUrl());
        } else {
            connection = DriverManager.getConnection(getUrl(), pUser, pPass);
        }

        logger.debug(this.drivertype + "-Connection established! (" + connection + ")");
        logger.info("JDBC connection successfully opened: '" + getUrl() + "' with user '" + pUser + "'");


        //Close previous statement objects if exists:
        if (mSCache != null) {
            mSCache.closeall();
        }

        //Create a new statement cache:
        mSCache = new StatementCache(connection);
        mSCache.setDebug(debug);    //pass-through of the debug mode:
        logger.trace(this.drivertype + "-StatementCache created! (" + mSCache + ")");

        //create a new watcher for PreparedStatement objects:
        pstWatcher = new PreparedStmtWatcher(connection);
        pstWatcher.setDebug(debug); //pass-through of the debug mode:
        logger.trace(this.drivertype + "-PreparedtStatementWatcher created! (" + pstWatcher + ")");

        //Observer-Handling:
        setChanged();
        notifyObservers(connection);

        return isClosed();
    }

    /**<b>DDL</b>Select SQL Operation.
     * @param pSQL SQL Select-Query
     * @return ResultSet which can be looped through
     * @throws SQLException any database errors
     */
    public ResultSet sqlSelect(String pSQL) throws SQLException {
        Statement stat = mSCache.getStatement();
        return stat.executeQuery(pSQL);
    }

    /**
     * Close the passed ResultSet object. The underlying Statement object will
     * be put into the StatementCache {@link #mSCache} for reuseability.
     * @param pResultset to be closed
     * @throws SQLException any
     */
    public void closeResultSet(ResultSet pResultset) throws java.sql.SQLException {
        if (pResultset != null) {
            Statement stmt = pResultset.getStatement();

            //Don't put Prepared Statements into the Cache!
            if (!(stmt instanceof java.sql.PreparedStatement)) {
                mSCache.releaseStatement(stmt);
            }

            pResultset.close();
        }
    }

    /**<b>DML</b> Insert-, Update- and Delete- SQL operations.<br>
     * <ul><li>INSERT INTO table (columns) values (a,b,c)</li>
     * <li>UPDATE table SET column = value WHERE...</li>
     * <li>DELETE FROM table WHERE...</li></ul>
     * @param pSQL SQL Statement (do not put a ';' at the end!).
     * @return amount of changed data rows (-1 on errors))
     * @throws SQLException any
     */
    public int sqlExecution(String pSQL) throws SQLException {
        //obtain a Statement object (cached or newly created):
        Statement mStat = mSCache.getStatement();

        //Execution:
        int result = mStat.executeUpdate(pSQL);

        //release resources:
        mSCache.releaseStatement(mStat);

        //return the amount of changed rows:
        return result;
    }

    /**Closes a PreparedStatement object.<br>
     * <b>Using this method you can access further statistics about cleaning up resources </b>
     * @param pst Prepared Statement Object
     * @throws SQLException on errors during closing.
     */
    public void closePreparedStatement(PreparedStatement pst) throws SQLException {
        pstWatcher.closePreparedStatement(pst);
    }

    /**Sets the fetch size of rows retrieved by the database.<br>
     * <b>The fetch size will be set on the ResultSet and on the Statement</b>
     * @param pResultset ResultSet-Objekt
     * @param pFetchSize new amount
     * @throws SQLException should be caught (expecially for odbc drivers)
     */
    public void setStmtFetchSize(ResultSet pResultset, int pFetchSize) throws SQLException {
        Statement stmt = pResultset.getStatement();
        pResultset.setFetchSize(pFetchSize); //am Resultset einstellen
        stmt.setFetchSize(pFetchSize);          //am Statement einstellen
    }

    /** Calls close() on the connection object and on all open statement objects.
     */
    public void closeConnection() {

        try {
            //Close Statement cache:
            if (mSCache != null) {
                //check if all resultsets were closed:
                int stmt_created = mSCache.getCountCreated();
                int stmt_used = mSCache.getCountReused();
                int stmt_closed = mSCache.getCountReleased();
                int totalstmt = (stmt_created + stmt_used);
                if (stmt_closed < totalstmt) {
                    logger.warn((totalstmt-stmt_closed)+" ResultSets have not been closed using closeResultSet()!");
                }
                mSCache.closeall();
                mSCache = null;
            }
        } catch (Exception e) {
            logger.debug("Exception closing StatementCache", e);
        }

        try {
            //Close PreparedStatement Watcher:
            if (pstWatcher != null) {
                int pstmt_created = pstWatcher.getCountCreated();
                int pstmt_closed = pstWatcher.getCountClosed();
                if (pstmt_closed < (pstmt_created)) {
                    logger.warn((pstmt_created-pstmt_closed)+" PreparedStatements have not been closed using closePreparedStatement()!");
                }
                pstWatcher.closeall();
                pstWatcher = null;
            }
        } catch (Exception e) {
            logger.debug("Exception closing PreparedStatementWatcher", e);
        }

        try {
            //Last but not least - close the database connection:
            if (connection != null) {
                connection.close();
                logger.debug(this.drivertype + "-Connection closed! (" + connection + ")");

                connection = null;
            }
        } catch (SQLException e) {
            logger.error("Exception closing JDBC connection: "+e.getMessage(), e);
        }


        //ObserverHandling:
        setChanged();
        notifyObservers(connection);

    }

    /**Returns all currently loaded Drivers through the DriverManager (Strings).
     * @return DriverManager.getDrivers()...getClass().getName()
     */
    public Collection<String> getAllLoadedDrivers() {
        Collection<String> drivers = new HashSet<String>();
        for (Enumeration<java.sql.Driver> enm = DriverManager.getDrivers(); enm.hasMoreElements();) {
            drivers.add(enm.nextElement().getClass().getName());
        }
        return drivers;
    }

    /** can be used to get further debugging informations. 
     * @param pw PrintWriter
     * @example setDriverManagerLogWriter(new PrintWriter(System.out));
     * */
    public static void setDriverManagerLogWriter(PrintWriter pw) {
        DriverManager.setLogWriter(pw);
    }

    /**set debug mode. <br>
     * do this before calling connect because
     * this property will also passed through into
     * the statement cache class.
     * @param truefalse on/off
     */
    public void setDebug(boolean truefalse) {
        this.debug = truefalse;
        if (this.mSCache != null) {
            this.mSCache.setDebug(truefalse);
        }
    }

    /** Returns the java.sql.Connection object.
     * @return java.sql.Connection object.
     */
    public Connection getConnection() {
        return connection;
    }

    /**String-Representation.
     * @return Classname/Hashcode/URL
     */
    @Override
    public String toString() {
        return "DBQuickConnect@" + Integer.toHexString(hashCode()) + " URL=" + getUrl();
    }

    /** Starts a database transaction.
     *  It ensures that AutoCommit is turned off and commits previous
     *  databse operations.
     * @throws SQLException any.
     */
    public void startTransaction() throws java.sql.SQLException {
        connection.setAutoCommit(false);
        connection.commit();
    }

    /** try to commit the current transaction and disable the transaction mode.
     ** @throws SQLException any.
     */
    public void saveTransaction() throws java.sql.SQLException {
        connection.commit();
        connection.setAutoCommit(true);
    }

    /** try to undo the current transaction.
     *  Calls rollback() and then enables the Autocommit mode.
     * @throws SQLException any.
     */
    public void undoTransaction() throws java.sql.SQLException {
        connection.rollback();
        connection.setAutoCommit(true);
    }

    /** Regular expression used to replace the '-character (replaceQuotes(String)). */
    private static final Pattern PATTERNQUOTE = Pattern.compile("'");

    /**
     * Replace single quotes to double quotes needed for valid SQL.
     * @param your original sql string
     * @return your sql string made valid
     */
    public static String replaceQuotes(String value) {
        return PATTERNQUOTE.matcher(value).replaceAll("''");
    }

    /**DB Username.
     * @return user
     */
    public String getUser() {
        return user;
    }

    /**getter for the database users password.
     * @return password string of the user
     */
    public String getPassword() {
        return pass;
    }

    /**
     * database username.
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * database users password.
     * @param pass the pass to set
     */
    public void setPassword(String pass) {
        this.pass = pass;
    }

    /** get informations about created, reused and released statements.
     * @return ec.: 'Statements closed: 4 of total 5... '
     */
    public String getInfoFull() {

        int stmt_created = mSCache.getCountCreated();
        int stmt_used = mSCache.getCountReused();
        int stmt_closed = mSCache.getCountReleased();

        int pstmt_created = pstWatcher.getCountCreated();
        int pstmt_closed = pstWatcher.getCountClosed();

        StringBuilder resb = new StringBuilder(120);
        int totalstmt = (stmt_created + stmt_used);
        resb.append("ResultSets closed: ").append(stmt_closed);
        resb.append(" of total ").append(totalstmt);
        resb.append(" (created ").append(stmt_created);
        resb.append(", reused ").append(stmt_used).append(")");
        resb.append("  PreparedStatements closed: ").append(pstmt_closed);
        resb.append(" of total ").append(pstmt_created);

        if (stmt_closed < totalstmt) {
            resb.append(System.getProperty("line.separator"));
            resb.append(">>> Try to close ").append((totalstmt-stmt_closed));
            resb.append(" more ResultSets using DBQConnection.closeResultSet(rs) <<<");
        }


        String restr = "Statements closed: " + stmt_closed + " of total " +
                (stmt_created + stmt_used) + " (created " +
                stmt_created + ", reused " + stmt_used + ")" +
                "  PreparedStatements closed: " + pstmt_closed + " of total " + pstmt_created;
        return resb.toString();
    }

    
    
//start of interface java.sql.Connection --->

    public Statement createStatement() throws SQLException {
        return mSCache.getStatement();
    }

    /**Creates a new PreparedStatement object.<br>
     * <b>Using this method you can access further statistics about cleaning up resources </b>
     * @param pSQL DML or DDL Sql statement with '?' as parameter placeholders.
     * @return Prepared Statement Object. (forward-only, connections default-holdability)
     * @throws SQLException maybe on errors during preparation.
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return pstWatcher.getPreparedStatement(sql);
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        return getConnection().prepareCall(sql);
    }

    public String nativeSQL(String sql) throws SQLException {
        return getConnection().nativeSQL(sql);
    }

    public boolean getAutoCommit() throws SQLException {
        return getConnection().getAutoCommit();
    }

    public void commit() throws SQLException {
        getConnection().commit();
    }

    public void rollback() throws SQLException {
        getConnection().rollback();
    }

    public void close() throws SQLException {
        closeConnection();
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        return getConnection().getMetaData();
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        getConnection().setReadOnly(readOnly);
    }

    public boolean isReadOnly() throws SQLException {
        return getConnection().isReadOnly();
    }

    public void setCatalog(String catalog) throws SQLException {
        getConnection().setCatalog(catalog);
    }

    public String getCatalog() throws SQLException {
        return getConnection().getCatalog();
    }

    public void setTransactionIsolation(int level) throws SQLException {
        getConnection().setTransactionIsolation(level);
    }

    public int getTransactionIsolation() throws SQLException {
        return getConnection().getTransactionIsolation();
    }

    public SQLWarning getWarnings() throws SQLException {
        return getConnection().getWarnings();
    }

    public void clearWarnings() throws SQLException {
        getConnection().clearWarnings();
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return mSCache.getStatement(resultSetType, resultSetConcurrency, getConnection().getHoldability());
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return pstWatcher.getPreparedStatement(sql, resultSetType, resultSetConcurrency, connection.getHoldability());
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return getConnection().prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return getConnection().getTypeMap();
    }

    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        getConnection().setTypeMap(map);
    }

    public void setHoldability(int holdability) throws SQLException {
        getConnection().setHoldability(holdability);
    }

    public int getHoldability() throws SQLException {
        return getConnection().getHoldability();
    }

    public Savepoint setSavepoint() throws SQLException {
        return getConnection().setSavepoint();
    }

    public Savepoint setSavepoint(String name) throws SQLException {
        return getConnection().setSavepoint(name);
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        getConnection().rollback(savepoint);
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        getConnection().releaseSavepoint(savepoint);
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return mSCache.getStatement(resultSetType, resultSetConcurrency, resultSetConcurrency);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return pstWatcher.getPreparedStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getConnection().prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return pstWatcher.getPreparedStatement(sql, autoGeneratedKeys);
    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return pstWatcher.getPreparedStatement(sql, columnIndexes);
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return pstWatcher.getPreparedStatement(sql, columnNames);
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        connection.setAutoCommit(autoCommit);
    }

    public boolean isClosed() throws SQLException {
        return (connection == null) || (connection.isClosed());
    }


//start of interface java.sql.Connection JDK 6 features --->

    public java.sql.Clob createClob() throws SQLException {
        return getConnection().createClob();
    }

    public java.sql.Blob createBlob() throws SQLException {
        return getConnection().createBlob();
    }

    public java.sql.NClob createNClob() throws SQLException {
        return getConnection().createNClob();
    }

    public java.sql.SQLXML createSQLXML() throws SQLException {
        return getConnection().createSQLXML();
    }

    public boolean isValid(int timeout) throws SQLException {
        return (connection != null && connection.isValid(timeout));
    }

    public void setClientInfo(String name, String value) throws java.sql.SQLClientInfoException {
        getConnection().setClientInfo(name, value);
    }

    public void setClientInfo(java.util.Properties properties) throws java.sql.SQLClientInfoException {
        getConnection().setClientInfo(properties);
    }

    public String getClientInfo(String name) throws SQLException {
        return getConnection().getClientInfo(name);
    }

    public java.util.Properties getClientInfo() throws SQLException {
        return getConnection().getClientInfo();
    }

    public java.sql.Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return getConnection().createArrayOf(typeName, elements);
    }

    public java.sql.Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return getConnection().createStruct(typeName, attributes);
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return getConnection().unwrap(iface);
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return getConnection().isWrapperFor(iface);
    }
}
