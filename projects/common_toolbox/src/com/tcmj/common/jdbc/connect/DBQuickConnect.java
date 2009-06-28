package com.tcmj.common.jdbc.connect;

import java.util.Collection;
import java.util.HashSet;
import java.util.Enumeration;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Observable;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Database Connectivity!
 * You can use any JDBC driver but there is a special support to:<br>
 * <code><ul><li>ODBC</li>
 * <li>Oracle</li>
 * <li>Microsoft SQL Server via JTDS</li>
 * <li>MySQl</li></ul></code><br>
 * <pre>
 * <i><b>//Declaration:</b></i>
 * DBQuickConnect dbcon = new DBQuickConnect();
 * <i><b>//Choose your database driver:</b></i>
 * dbcon.setDriver(DBQuickConnect.Driver.ORACLE);
 * <i><b>//Construct your URL:</b></i>
 * String url = dbcon.createURL("PRIMA", "DEVORA", "1521");
 * <i><b>//Connect:</b></i>
 * dbcon.connect(url, "userxy", "password123");
 * <i><b>//Select-Statement:</b></i>
 * ResultSet rs = dbcon.sqlSelect("SELECT A,B FROM XYZ");
 * <i><b>//Loop through the result:</b></i>
 * while(rs.next()){
 *       String cell = rs.getString(1);   <i>//column: 1</i>
 * }
 * <i><b>//!!Important!! close ResultSet !!Import!!)</b></i>
 * dbcon.closeResultSet(rs);
 * <i><b>//Close connection</b></i>
 * dbcon.closeConnection();
 * </pre><br><br>
 * <p>Copyright: Copyright (c) 2003 - 2009 by tcmj</p>
 * @author Thomas Deutsch
 * @version 3.1
 * @JUnit Test available!
 */
public class DBQuickConnect extends Observable {

    /** slf4j Logging framework. */
    private static final transient Logger logger = LoggerFactory.getLogger(DBQuickConnect.class);

    /** debugmode on/off. default = false.*/
    private boolean debug;

    /** internal driver. default = NOTSELECTED */
    protected Driver internalDriver = Driver.NOTSELECTED;

    /** jdbc driver class. */
    protected String jdbcdriver;

    /** jdbc URL. */
    protected String url;

    /** database username. */
    protected String user;

    /** database users password. */
    private String pass;

    /** Java Connection object. */
    protected Connection connection;

    /** Statement cache.<br>contains instances of java.sql.Statement. */
    protected StmtCache mSCache;

    /** PreparedStatement Watcher. */
    protected PstStmtWatcher pstWatcher;

    public enum Driver {

        NOTSELECTED(null, null),
        CUSTOM(null, null),
        ODBC("sun.jdbc.odbc.JdbcOdbcDriver", "jdbc:odbc:"),
        ACCESS_MDB("sun.jdbc.odbc.JdbcOdbcDriver", "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ="),
        ORACLE("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@"),
        MSSQL("net.sourceforge.jtds.jdbc.Driver", "jdbc:jtds:sqlserver://"),
        MYSQL("org.gjt.mm.mysql.Driver", "jdbc:mysql://"),
        HSQLDB("org.hsqldb.jdbcDriver", "jdbc:hsqldb:"),
        JAVADB_NETWORK("org.apache.derby.jdbc.ClientDriver", "jdbc:derby://"),
        JAVADB_EMBEDDED("org.apache.derby.jdbc.EmbeddedDriver", "jdbc:derby:");

        private String driver;

        private String urlprefix;

        Driver(String drv, String url) {
            this.driver = drv;
            this.urlprefix = url;
        }

        public String getDriverClassName() {
            return driver;
        }

        public String getUrl() {
            return urlprefix;
        }

        /** eg.: Driver.CUSTOM.setDriverClassName("com.xy.Driver") */
        public void setDriverClassName(String driver) {
            this.driver = driver;
        }

        public void setUrl(String urlprefix) {
            this.urlprefix = urlprefix;
        }

    };

    /**Standardconstructor. */
    public DBQuickConnect() {
        super();
    }

    public DBQuickConnect(boolean debug, String jdbcdriver, String url, String user, String pass, Connection connection, StmtCache mSCache, PstStmtWatcher pstWatcher) {
        this.debug = debug;
        this.jdbcdriver = jdbcdriver;
        this.url = url;
        this.user = user;
        this.pass = pass;
        this.connection = connection;
        this.mSCache = mSCache;
        this.pstWatcher = pstWatcher;
    }

    /**Set the datenbase driver class used to connect.
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
            this.internalDriver = driver;
        }
    }

    /** Getter for the current Driver (enum).
     * @return one of the predefined Driver enum values      */
    public Driver getDriver() {
        return internalDriver;
    }

    /**Configures the Driver.CUSTOM enum with userdefined value.<br>
     * Also calls the class.forname method to register the driver.<br>
     * <b>After calling this methd you have to use the setURL method!</b>
     * @param customDriver classname of your jdbc driver as string
     */
    public void setDriver(String customDriver)
            throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        Driver.CUSTOM.setDriverClassName(customDriver);
        this.internalDriver = Driver.CUSTOM;
        Class.forName(Driver.CUSTOM.getDriverClassName()).newInstance();
    }

    /** Getter for the current used driver class name.
     * @return jdbc driver class as string  - and null if Driver.NOTSELECTED*/
    public String getDriverClass() {
        return this.internalDriver.getDriverClassName();
    }

    /** setter for the jdbc URL used to connect to your database. <br>
     * <b>use the createURL() method to get help building your URL</b>
     * @param myurl full qualified URL */
    public void setURL(String myurl) {
        if (this.internalDriver == Driver.NOTSELECTED) {
            throw new UnsupportedOperationException("Please specify a Driver first!");
        } else {
            this.internalDriver.setUrl(myurl);
        }
    }

    /** getter for the current used url.
     * <b>if you didn't call the setURL(String) function in combination with
     * the createURL(String,String,String) method you will only get a template
     * part of the URL (which is not usable for connecting).
     * @return jdbc url - and null if no driver was selected */
    public String getUrl() {
        return this.internalDriver.getUrl();
    }

    /**Helper to create an URL for your jdbc connection depending of the Driver.<br>
     * Examples:<br>
     * Driver.NOTSELECTED: -not supported-<br>
     * Driver.CUSTOM: -not supported-<br>
     * Driver.ODBC: createURL(odbcname,null,null)<br>
     * Driver.ACCESS_MDB: createURL(".\dbpath\Accesfile.mdb")<br>
     * Driver.ORACLE: createURL(SID,OraHost,1521)<br>
     * Driver.MSSQL: createURL(DBName,HostName,1433)<br>
     * Driver.MYSQL: createURL(DBName,HostName,null)<br>
     * Driver.HSQLDB: createURL(DBName,HostName,null)<br>
     * Driver.JAVADB_NETWORK: createURL(DBName,HostName,Port)<br>
     * Driver.JAVADB_EMBEDDED: createURL(DBName,null,null)<br>
     * @param pDB_Alias_SID - DBName or ODBC-Alias or Oracle-SID.
     * @param pHost HostName of the database server.
     * @param pPort Port of the database server.
     * @return ready to use URL which can pe passed to the setURL(String) method.
     */
    public String createURL(String pDB_Alias_SID, String pHost, String pPort) {
        return DBQuickConnect.createURL(pDB_Alias_SID, pHost, pPort, getDriver());
    }

    /** @see createURL(String,String,String). */
    private static String createURL(String pDB_Alias_SID,
            String pHost, String pPort, Driver pDriver) {

        if (pDriver == Driver.NOTSELECTED) {
            throw new IllegalArgumentException("Driver not set!");
        } else if (pDriver == Driver.CUSTOM) {
            throw new IllegalArgumentException("Cannot construct a CUSTOM Driver!");
        } else {

            String url = pDriver.getUrl();

            switch (pDriver) {
                case ODBC:
                case ACCESS_MDB:
                    url = url.concat(pDB_Alias_SID);
                    break;
                case ORACLE:
                    url = url.concat(pHost + ":" + pPort + ":" + pDB_Alias_SID);
                    break;
                case MSSQL:
                    url = url.concat(pHost + ":" + pPort + "/" + pDB_Alias_SID);
                    break;
                case MYSQL:
                    url = url.concat(pHost + "/" + pDB_Alias_SID);
                    break;
                case HSQLDB:
                    url = url.concat(pDB_Alias_SID + "/" + pHost);
                    break;
                case JAVADB_NETWORK:
                    url = url.concat(pHost + ":" + pPort + "/" + pDB_Alias_SID);
                    break;
                case JAVADB_EMBEDDED:
                    url = url.concat(pDB_Alias_SID);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown Driver!");
            }
            return url;
        }
    }

    /** Connects to the specified database.
     * if you use the predefined Drivers from the Driver enum you should have to use
     * the <b>createURL</b> method to insert the missing parts like hostname,
     * databasename and so on.
     * @param pDBurl JDBC URL
     * @param pUser database user (stored in a global field)
     * @param pPass password of the database user (stored in a global field)
     * @return true/false on success/failure (mostly true or an exception is thrown)
     * @throws java.sql.SQLException any
     */
    public boolean connect(String pUser, String pPass) throws java.sql.SQLException {
        this.user = pUser;
        this.pass = pPass;

        if (Driver.NOTSELECTED == this.internalDriver) {
            throw new IllegalArgumentException("JDBC-Driver not set!");
        }

        String myurl = getUrl();
        if (myurl == null) {
            throw new UnsupportedOperationException("JDBC-URL is not set!");
        }

        if (pUser == null && pPass == null) {
            connection = DriverManager.getConnection(getUrl());
        } else {
            connection = DriverManager.getConnection(getUrl(), pUser, pPass);
        }

        logger.debug(this.internalDriver + "-Connection established! (" + connection + ")");
        logger.info("JDBC connection successfully opened to '" + url + "' with user '" + pUser + "'");


        //Close previous statement objects if exists:
        if (mSCache != null) {
            mSCache.closeall();
        }

        //Create a new statement cache:
        mSCache = new StmtCache(connection);
        mSCache.setDebug(debug);    //pass-through of the debug mode:
        logger.debug(this.internalDriver + "-StatementCache created! (" + mSCache + ")");


        //create a new watcher for PreparedStatement objects:
        pstWatcher = new PstStmtWatcher(connection);
        pstWatcher.setDebug(debug); //pass-through of the debug mode:
        logger.debug(this.internalDriver + "-PreparedtStatementWatcher created! (" + pstWatcher + ")");


        //Observer-Handling:
        setChanged();
        notifyObservers(this);

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

    /**Create a new PreparedStatement object.<br>
     * <b>Using this method you can access further statistics about cleaning up resources </b>
     * @param pSQL DML or DDL Sql statement with '?' as parameter placeholders.
     * @return Prepared Statement Object. (forward-only, connections default-holdability)
     * @throws SQLException maybe on errors during preparation.
     */
    public PreparedStatement pstPrepareStatement(String pSQL) throws SQLException {
        return pstWatcher.getPreparedStatement(pSQL, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, connection.getHoldability());
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
                mSCache.closeall();
                mSCache = null;
            }
        } catch (Exception e) {
            logger.debug("Exception closing StatementCache", e);
        }

        try {
            //Close PreparedStatement Watcher:
            if (pstWatcher != null) {
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
                connection = null;
            }
            logger.debug(this.internalDriver + "-Connection closed: " + getDriver());
        } catch (SQLException e) {
            logger.debug("Exception closing PreparedStatementWatcher", e);
        }


        //ObserverHandling:
        setChanged();
        notifyObservers(this);

    }

    /**Returns all currently loaded Drivers through the DriverManager (Strings).
     * @return DriverManager.getDrivers()...getClass().getName()
     */
    public Collection getAllLoadedDrivers() {
        Collection drivers = new HashSet();
        for (Enumeration e = DriverManager.getDrivers(); e.hasMoreElements();) {
            drivers.add(e.nextElement().getClass().getName());
        }
        return drivers;
    }

    /** Getter if the connection object is null or closed.
     * @return true or false
     */
    public boolean isClosed() throws SQLException {
        return (connection == null) || (connection.isClosed());
    }

    /** setLogWriter() speichert das PrintWriter-Objekt in einer privaten Variable
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
        return "DBQuickConnect@" + Integer.toHexString(hashCode()) + " URL=" + this.url;
    }

    /**DB Username.
     * @return user
     */
    public String getUser() {
        return user;
    }

    /**Transaction Mode.
     * connection.setAutoCommit(value);
     * @param OnOff 
     * @throws SQLException Error
     */
    public void setAutoCommit(boolean value) throws java.sql.SQLException {
        connection.setAutoCommit(value);
    }

    /** Starts a Databasetransaction.
     *  Setzt den AutoCommit-Modus auf false und ruft die Methode
     *  CommitTransaction() auf um vorherige Änderungen zu übermitteln.
     ** @throws SQLException bei Datenbankfehler.
     */
    public void startTransaction() throws java.sql.SQLException {
        connection.setAutoCommit(false);
        connection.commit();
    }

    /** Speichert eine Datenbanktransaktion.
     *  Ruft die Methode CommitTransaction() auf um die Änderungen
     *  festzuschreiben und setzt dann den AutoCommit-Modus auf true.a
     ** @throws SQLException bei Datenbankfehler.
     */
    public void saveTransaction() throws java.sql.SQLException {
        connection.commit();
        connection.setAutoCommit(true);
    }

    /** Macht eine Datenbanktransaktion rueckgaengig.
     *  Ruft die die Funktion RollBackTransaction()
     *  auf und setzt dann den AutoCommit-Modus auf true;
     ** @throws SQLException bei Datenbankfehler.
     */
    public void undoTransaction() throws java.sql.SQLException {
        connection.rollback();
        connection.setAutoCommit(true);
    }

    /** Regulärer Ausdruck zum ersetzen von Hochkommata. */
    private static final Pattern patternA = Pattern.compile("'");

    /**
     * Ersetzt das Zeichen Apostroph SQL-Konform
     * in dopellte Hochkommata.
     * @return neue Zeichenfolge mit den ersetzten Sonderzeichen
     * @param value Zeichenfolge mit zu ersetzenden Sonderzeichen
     */
    public static String replaceQuotes(String value) {
        String erg = patternA.matcher(value).replaceAll("''");
        return erg;
    }

    /**Gibt das unverschlüsselte Passwort zurück.
     * @return Benutzernamen
     */
    public String getPassword() {
        return pass;
    }

    /** Gibt Informationen ueber den Cache zurueck.
     * @return 'created=2  reused=34  released=36/36   (ratio: 1700%)''
     */
    public String getReleaseInfo() {

        int stmt_created = mSCache.getCountCreated();
        int stmt_used = mSCache.getCountReused();
        int stmt_closed = mSCache.getCountReleased();

        int pstmt_created = pstWatcher.getCountCreated();
        int pstmt_closed = pstWatcher.getCountClosed();

//        String restr =  "Statements " +stmt_closed + "/(" +
//                stmt_created +"+" + stmt_used +")" +
//                    "  PreparedStatements " +  pstmt_closed+ "/" +pstmt_created;
        String restr = "Statements closed: " + stmt_closed + " of total " +
                (stmt_created + stmt_used) + " (created " +
                stmt_created + ", reused " + stmt_used + ")" +
                "  PreparedStatements closed: " + pstmt_closed + " of total " + pstmt_created;

        return restr;
//        return mSCache.getReleaseInfo();
    }

    public class StmtCache {

        private Connection con;

        private Stack<Statement> cache;

        private int count_reused;

        private int count_created;

        private int count_released;

        private boolean debug = false;

        public StmtCache(Connection con) {
            this.con = con;
            this.cache = new Stack();
            this.count_reused = 0;
            this.count_created = 0;
            this.count_released = 0;
        }

        public Statement getStatement() throws SQLException {
            if (cache.isEmpty()) {

                Statement st = con.createStatement();
                count_created++;

                if (debug && st != null) {
                    logger.trace("creating a new Statement() " + Integer.toHexString(st.hashCode()));
                }
                return st;

            } else {

                Statement st = (Statement) cache.pop();
                this.count_reused++;

                if (debug && st != null) {
                    logger.trace("using a cached Statement() " + Integer.toHexString(st.hashCode()) +
                            " (" + cache.size() + " left)");
                }
                return st;

            }
        }

        public Statement getStatement(int resultSetType, int resultSetConcurrency,
                int resultSetHoldability) throws SQLException {
            if (cache.isEmpty()) {

                Statement st = con.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
                count_created++;

                if (debug && st != null) {
                    logger.trace("creating a new Statement(emptycache) " + Integer.toHexString(st.hashCode()));
                }
                return st;

            } else {

                //search if a stmt exists with the needed behavour:
                Statement statement = null;
                for (Iterator<Statement> it = cache.iterator(); it.hasNext();) {
                    statement = it.next();

                    int resultsettype = statement.getResultSetType();
                    int resultsetconc = statement.getResultSetConcurrency();
                    int resultsethold = statement.getResultSetHoldability();

                    if (resultsettype == resultSetType &&
                            resultsetconc == resultSetConcurrency &&
                            resultsethold == resultSetHoldability) {

                        this.count_reused++;

                        it.remove();

                        if (debug && statement != null) {
                            logger.trace("using a cached Statement(match) " + Integer.toHexString(statement.hashCode()) +
                                    " (" + cache.size() + " left)");
                        }

                        return statement;
                    }

                }
                //nothing suitable found...
                if (statement == null) {
                    statement = con.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
                    count_created++;

                    if (debug && statement != null) {
                        logger.trace("creating a new Statement(nsfound) " + Integer.toHexString(statement.hashCode()));
                    }

                }
                return statement;
            }
        }

        public void releaseStatement(Statement statement) throws SQLException {
            if (!cache.contains(statement)) {
                cache.add(statement);
                count_released++;
            } else {
                throw new SQLException("Statement " + statement + " has already been released! Do not release it twice!");
            }

            if (debug && statement != null) {
                logger.trace("releasing Statement " + Integer.toHexString(statement.hashCode()) +
                        " (cachesize = " + cache.size() + ")");
            }

        }

        public void closeall() {
            java.util.Iterator it = cache.iterator();
            while (it.hasNext()) {
                java.sql.Statement stmt = (java.sql.Statement) it.next();

                try {
                    if (debug) {
                        logger.debug("closing Statement " + stmt);
                    }
                    stmt.close();
                    stmt = null;
                } catch (Exception e) {
                    if (debug) {
                        logger.debug("error closing Statement " +
                                stmt + ": " + e.getMessage());
                    }
                }
            }
            cache.clear();

        }

        /**
         * Getter for property debug.
         * @return Value of property debug.
         */
        public boolean isDebug() {
            return debug;
        }

        /**
         * Setter for property debug.
         * @param debug New value of property debug.
         */
        public void setDebug(boolean debug) {
            this.debug = debug;
            if (debug) {
                logger.debug("cache.connection " + con);
            }
        }

        public int getCountCreated() {
            return this.count_created;
        }

        public int getCountReleased() {
            return this.count_released;
        }

        public int getCountReused() {
            return this.count_reused;
        }

        public String getReleaseInfo() {

//            float ratio = (float) (count_reused / count_created) * 100F;

            return "created=" + count_created +
                    "  reused=" + count_reused +
                    "  released=" + count_released + "/" + (count_created + count_reused);
//                    "   (ratio: " + DECF.format(ratio) + " %)";
        }

        @Override
        public String toString() {
            return "StmtCache@" + Integer.toHexString(hashCode());
        }

    }//class StmtCache

    public class PstStmtWatcher {

        private Connection con;

        private Set<PreparedStatement> pstObjectSet;

        private int count_created;

        private int count_closed;

        private boolean debug = false;

        public PstStmtWatcher(Connection con) {
            this.con = con;
            this.pstObjectSet = new HashSet();
            this.count_created = 0;
            this.count_closed = 0;
        }

        public PreparedStatement getPreparedStatement(String sql, int resultSetType,
                int resultSetConcurrency, int resultSetHoldability) throws SQLException {

            PreparedStatement pstmt = con.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
            pstObjectSet.add(pstmt);
            count_created++;

            if (debug && pstmt != null) {
                logger.debug("creating an new PreparedStatement " + Integer.toHexString(pstmt.hashCode()));
            }
            return pstmt;

        }

        public PreparedStatement getPreparedStatement(String sql, int autoGeneratedKeys) throws SQLException {

            PreparedStatement pstmt = con.prepareStatement(sql, autoGeneratedKeys);
            pstObjectSet.add(pstmt);
            count_created++;

            if (debug && pstmt != null) {
                logger.debug("creating an new AGK-PreparedStatement " + Integer.toHexString(pstmt.hashCode()));
            }
            return pstmt;

        }

        public PreparedStatement getPreparedStatement(String sql, int columnIndexes[]) throws SQLException {

            PreparedStatement pstmt = con.prepareStatement(sql, columnIndexes);
            pstObjectSet.add(pstmt);
            count_created++;

            if (debug && pstmt != null) {
                logger.debug("creating an new AGK[]-PreparedStatement " + Integer.toHexString(pstmt.hashCode()));
            }
            return pstmt;

        }

        public PreparedStatement getPreparedStatement(String sql, String columnNames[]) throws SQLException {

            PreparedStatement pstmt = con.prepareStatement(sql, columnNames);
            pstObjectSet.add(pstmt);
            count_created++;

            if (debug && pstmt != null) {
                logger.debug("creating an new AGK[]-PreparedStatement " + Integer.toHexString(pstmt.hashCode()));
            }
            return pstmt;

        }

        public void closePreparedStatement(PreparedStatement statement) throws SQLException {
            logger.debug("closing PreparedStatement " + statement);
            pstObjectSet.remove(statement);
            statement.close();
            count_closed++;
        }

        public void closeall() {

            for (PreparedStatement prepStmt : pstObjectSet) {

                try {
//                    if (!prepStmt.isClosed()) {
                    if (debug) {
                        logger.debug("closing PreparedStatement " + prepStmt);
                    }
                    prepStmt.close();
                    count_closed++;
//                    }
                } catch (Exception e) {
                    if (debug) {
                        logger.debug("error closing PreparedStatement " +
                                prepStmt + ": " + e.getMessage());
                    }
                }

            }
//        pstObjectSet.clear();

        }

        /**
         * Getter for property debug.
         * @return Value of property debug.
         */
        public boolean isDebug() {
            return debug;
        }

        /**
         * Setter for property debug.
         * @param debug New value of property debug.
         */
        public void setDebug(boolean debug) {
            this.debug = debug;
        }

        public int getCountCreated() {
            return this.count_created;
        }

        public int getCountClosed() {
            return this.count_closed;
        }

        public String getCloseInfo() {
            return "closed: " + count_closed + "/" + count_created;
        }

        @Override
        public String toString() {
            return "PstStmtWatcher@" + Integer.toHexString(hashCode());
        }

    }//class PstStmtWatcher
}
