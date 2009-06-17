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
import java.util.Observable;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/** * Database Connectivity!
 * Es werden folgende Datenbanktypen unterstützt:<br>
 * <code><ul><li>sun.jdbc.odbc.JdbcOdbcDriver<br>
 * <li>oracle.jdbc.jdbcdriver.OracleDriver   <br>
 * <li>net.sourceforge.jtds.jdbc.Driver  <br>
 * <li>org.gjt.mm.mysql.Driver</ul></code><br>
 * <pre>
 * <i><b>//Deklaration und Initialisierung:</b></i>
 * tcmj.database.quick.DBQuickConnect dbcon = new tcmj.database.quick.DBQuickConnect();
 * <i><b>//Datenbank-Treiber uerber Konstante waehlen:</b></i>
 * dbcon.setDriverClass(dbcon._ORACLE);
 * <i><b>//Datenbankspezifische URL zusammenbauen</b></i>
 * String url = dbcon.createURL("PRIMA", "DEVORA", "1521", dbcon._ORACLE);
 * <i><b>//Verbindung aufbauen</b></i>
 * dbcon.connecturl(url, "privuser1", "privuser1");
 * <i><b>//Select-Statement absetzen</b></i>
 * ResultSet rs = dbcon.sqlSelect("SELECT A,B FROM XYZ");
 * <i><b>//Ergebnismenge durchlaufen und auslesen:</b></i>
 * while(rs.next()){
 *       String zelle = rs.getString(1);   <i>//Spalte: A</i>
 * }
 * <i><b>//ResultSet-Objekt recyclen (wichtig!!!)</b></i>
 * dbcon.closeResultSet(rs);
 * </pre><br><br>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Organisation: tcmj</p>
 * @author Thomas Deutsch
 * @version 3.0
 * @JUnit Test available!
 */
public class DBQuickConnect extends Observable {

    /** slf4j Logging framework. */
    private static final transient Logger logger = LoggerFactory.getLogger(DBQuickConnect.class);

    /**Schaltet den DebugWriter ein/aus. */
    private boolean debug = false;
    
    /**JDBC Treiber der mit der Class.forName() Methode geladen wird. */
    protected String jdbcdriver;
    /**Interne Nummer des gewählten Treibers. */
    protected Driver internalDriver = Driver.NOTSELECTED;
    /**URL der Datenbankverbindung. */
    protected String url;
    /**Datenbank Benutzername (ID). */
    protected String user;
    /**Passwort des Datenbank Benutzers. */
    private String pass;
    /**Java Connection Objekt. */
    protected Connection connection;
    
    /**Die Klasse 'StmtCache' stellt eine verkettete Liste zur Verfügung.,
     * um nicht mehr verwendete Statement-Objekte zur Wiederverwendung
     * bereitzuhalten.<br>Dies betrifft normale java.sql.Statement - Objekte.*/
    private StmtCache mSCache;


    private PstStmtWatcher pstWatcher;

    
    /**Array, welches die Namen der Datenbanktreiberklassen und Datenbank-URLs beinhaltet. */
    private static final String[][] DRV_AND_URLS = {
        {
            "sun.jdbc.odbc.JdbcOdbcDriver",
            "oracle.jdbc.driver.OracleDriver",
            "net.sourceforge.jtds.jdbc.Driver",
            "org.gjt.mm.mysql.Driver",
            "org.hsqldb.jdbcDriver",
            "org.apache.derby.jdbc.ClientDriver",
            "org.apache.derby.jdbc.EmbeddedDriver"
        },
        {
            "jdbc:odbc:",
            "jdbc:oracle:thin:@",
            "jdbc:jtds:sqlserver://",
            "jdbc:mysql://",
            "jdbc:hsqldb:",
            "jdbc:derby://",
            "jdbc:derby:"
        }
    };

    private static final int DRIVERS = 0, URLS = 1;

    

    public enum Driver { NOTSELECTED,
    ODBC, ORACLE, MSSQL, MYSQL, HSQLDB,
    JAVADB_NETWORK, JAVADB_EMBEDDED };
    

    /**Standardkonstruktor.
     */
    public DBQuickConnect() {
    }

    


    
    /**Setzt die zu verwendende interne Datenbanktreiberklasse.
     * Für den Parameter sollten die Klassenvariablen verwendet werden:<br>
     * <code>ODBC<br>ORACLE<br>MSSQL<br>MYSQL<br></code>
     * @param driverclass Klassen der Datenbanktreiber als String
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public void setDriverClass(Driver driverclass)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        this.internalDriver = driverclass;
        String drivername = getDriverClassName(driverclass);
        registerDriver(drivername);
    }

    private static int mapDrvNums(Driver drvnum) {
        switch (drvnum) {
            case ODBC:
                return 0;
            case ORACLE:
                return 1;
            case MSSQL:
                return 2;
            case MYSQL:
                return 3;
            case HSQLDB:
                return 4;
            case JAVADB_NETWORK:
                return 5;
            case JAVADB_EMBEDDED:
                return 6;
            default:
                return -1;
        }
    }
    

    /**Setzt die zu verwendende Datenbanktreiberklasse als Klassenpfad.
     * <b>um alternative Treiber zu verwenden</b>
     * @param driverclass Klassen der Datenbanktreiber als String
     */
    public void setDriverClass(String driverclass)
            throws IllegalAccessException, ClassNotFoundException, InstantiationException {

        this.internalDriver = Driver.NOTSELECTED; //Interne Treibernummer ausschalten

        registerDriver(driverclass);

    }

    private void registerDriver(String driverclass)
            throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        
        Class.forName(driverclass).newInstance();

        this.jdbcdriver = driverclass;       //neuen Treiber eintragen;

    }

    /**Verbindet mit der übergebenen URL.
     * Die URL kann mit der Funktion <b>createURL</b> erzeugt werden.
     * @param pDBurl JDBC URL
     * @param pUser  Datenbankbenutzer
     * @param pPass  Passwort des Datenbankbenutzers
     * @return true/false on success/failure
     * @throws java.sql.SQLException 
     */
    public boolean connect(String pDBurl, String pUser, String pPass)
            throws java.sql.SQLException {
        this.url = pDBurl;
        this.user = pUser;
        this.pass = pPass;


        connection = DriverManager.getConnection(url, user, pass);
        logger.debug("Connection established: " + connection);


        //Falls es noch einen gefuellten Cache gibt, dann werden alle
        //StatementObjekte ordnungsgemaess geschlossen (.close())
        if (mSCache != null) {
            mSCache.closeall();
        }
        //Neuen Cache erzeugen (die StmtObj. gehoeren zu genau einer Connection)
        mSCache = new StmtCache(connection); //Statementchache init
        mSCache.setDebug(debug);
        logger.debug("StatementCache created: " + mSCache);

        pstWatcher = new PstStmtWatcher(connection);
        pstWatcher.setDebug(debug);
        logger.debug("PstStmtWatcher created: " + pstWatcher);

        setChanged();
        notifyObservers(connection);


        return (connection != null);
    }

    /**Erstellt die URL fuer die Verbindung.
     * Fuer ODBC wird nur der erste Parameter ausgewertet.<br>
     * Fuer Oracle/Microsof werden alle Parameter ausgewertet.<br>
     * Fuer MySQL wird nur der Datenbankname und der Rechnername ausgewertet.<br>
     * @param pDB_Alias_SID - DatenbankName oder ODBC-Alias oder Oracle-SID.
     * @param pHost RechnerName des DatenbankServers.
     * @param pPort Port auf dem der DatenbankServer zu erreichen ist.
     * @param drivernumber Treibernamen-Konstante der Klasse DBConnect.
     * @return Gibt den fertigen URL zurueck.
     */
    private static String createURL(String pDB_Alias_SID, String pHost,
            String pPort, Driver drivernumber) {

        if (drivernumber == Driver.NOTSELECTED) {
            throw new UnsupportedOperationException("Driver not set!");
        }


        //String cone = URLS[mapDrvNums(drivernumber)];
        String cone = DRV_AND_URLS[URLS][mapDrvNums(drivernumber)];

        switch (drivernumber) {
            case ODBC:
                cone = cone.concat(pDB_Alias_SID);
                break;
            case ORACLE:
                cone = cone.concat(pHost + ":" + pPort + ":" + pDB_Alias_SID);
                break;
            case MSSQL:
                cone = cone.concat(pHost + ":" + pPort + "/" + pDB_Alias_SID);
                break;
            case MYSQL:
                cone = cone.concat(pHost + "/" + pDB_Alias_SID);
                break;
            case HSQLDB:
                cone = cone.concat(pDB_Alias_SID + "/" + pHost);
                break;
            case JAVADB_NETWORK:
                cone = cone.concat(pHost + ":" + pPort + "/" + pDB_Alias_SID);
                break;
            case JAVADB_EMBEDDED:
                cone = cone.concat(pDB_Alias_SID);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Driver!");
        }
        return cone;
    }

    /**Erstellt die URL fuer die Verbindung.
     * Fuer ODBC wird nur der erste Parameter ausgewertet.<br>
     * Fuer Oracle/Microsof werden alle Parameter ausgewertet.<br>
     * Fuer MySQL wird nur der Datenbankname und der Rechnername ausgewertet.<br>
     * @param pDB_Alias_SID - DatenbankName oder ODBC-Alias oder Oracle-SID.
     * @param pHost RechnerName des DatenbankServers.
     * @param pPort Port auf dem der DatenbankServer zu erreichen ist.
     * @return Gibt den fertigen URL zurueck.
     */
    public String createURL(String pDB_Alias_SID, String pHost, String pPort) {

        return DBQuickConnect.createURL(pDB_Alias_SID, pHost, pPort, getDriver());

    }

    /**Recycelt über das ResultSet das Statement Objekt <b>Statement-Pool!</b>.<br>
     * Holt &uuml;ber das &uuml;bergebene das ResultSet-Objekt das Statementobjekt und
     * stellt es in die verkettete Liste {@link #mSCache}
     * @param pResultset Ergebnismenge (wird automatisch geschlossen!)
     * @throws SQLException
     */
    public void closeResultSet(ResultSet pResultset) throws java.sql.SQLException {
        if (pResultset != null) {
            Statement stmt = pResultset.getStatement();

            //Keine Prepared Statements in den Cache!
            if (!(stmt instanceof java.sql.PreparedStatement)) {
                mSCache.releaseStatement(stmt);
            }

            pResultset.close();
        }
    }

    
    /**SELECT Operation.
     * @param pSQL SQL Select-Query
     * @return ResultSet
     * @throws SQLException
     */
    public ResultSet sqlSelect(String pSQL) throws SQLException {
        Statement stat = mSCache.getStatement();
        return stat.executeQuery(pSQL);
    }


    
    /**<b>Datenbank&auml;nderungen</b> die kein ResultSet zur&uuml;ckgeben, sondern
     * die Anzahl der betroffenen Datens&auml;tze.<br>
     * <ul><li>INSERT INTO</li><li>UPDATE</li><li>DELETE FROM</li></ul>
     * @param pSQL SQL Statement (ohne abschlie&szlig;endes Semikolon ';').
     * @return (int) Anzahl der ge&auml;nderten Datens&auml;tze oder -1 bei Error
     * @throws SQLException 
     */
    public int sqlExecution(String pSQL) throws SQLException {
        //Statementobjekt aus dem Cache holen bzw. neu anlegen:
        Statement mStat = mSCache.getStatement();

        //Das Uebergebene SQL-Statement ausfuehren:
        int result = mStat.executeUpdate(pSQL);

        //Das nicht mehr benoetigte Statementobjekt zurueck in den Cache schreiben:
        mSCache.releaseStatement(mStat);

        //Das Ergebnis (Anzahl) zurueckgeben:
        return result;
    }


    
    /**Parametrisiertes Statement.
     * @param pSQL SQL Code mit Fragezeichen als Platzhalter.
     * @return Prepared Statement Objekt.
     * @throws SQLException beim Senden des Prototyps an die Datenbank.
     */
    public PreparedStatement pstPrepareStatement(String pSQL) throws SQLException {
        //create a new PreparedStatement object:
        return pstWatcher.getPreparedStatement(pSQL);
    }

    
    public void closePreparedStatement(PreparedStatement pst) throws SQLException {
        //close a PreparedStatement object:
        pstWatcher.closePreparedStatement(pst);
    }
    
    
    /**Fetchsize anpassen.
     * @param pResultset ResultSet-Objekt
     * @param pFetchSize Neue Grösse
     * @throws SQLException 
     */
    public void setStmtFetchSize(ResultSet pResultset, int pFetchSize) throws SQLException {
        Statement s = pResultset.getStatement();
        pResultset.setFetchSize(pFetchSize); //am Resultset einstellen
        s.setFetchSize(pFetchSize);          //am Statement einstellen
    }


    /** Calls close() on the connection object and on all open statement objects.
     */
    public void closeConnection() {
        try {
            setChanged();

            //Linked List leeren
            if (mSCache != null) {
                mSCache.closeall();
                mSCache = null;
            }

            //Linked List leeren
            if (pstWatcher != null) {
                pstWatcher.closeall();
                pstWatcher = null;
            }
            
            //Verbindung trennne
            if (connection != null) {
                connection.close();
                connection = null;
            }

            
            notifyObservers(connection);
            logger.debug("Connection closed: " + getDriver());

        } catch (SQLException ex) {
            notifyObservers(ex);
        }
    }

    private static final String getDriverClassName(Driver pDrvNo) {
        return DRV_AND_URLS[DRIVERS][mapDrvNums(pDrvNo)];
    }

    public String getDriverClass() {
        return getDriverClassName(getDriver());
    }


    /**Rückgabe eines Arrays aller geladenen Treibernamen (Strings).
     * @return DriverManager.getDrivers()...getClass().getName()
     */
    public Collection getAllLoadedDrivers() {
        Collection drivers = new HashSet();
        for (Enumeration e = DriverManager.getDrivers(); e.hasMoreElements();) {
            drivers.add(e.nextElement().getClass().getName());
        }
        return drivers;
    }

    
    public boolean isClosed() throws SQLException {
        return (connection!=null && connection.isClosed());
    }


    
    /** setLogWriter() speichert das PrintWriter-Objekt in einer privaten Variable
     * @param pw PrintWriter
     * @example setDriverManagerLogWriter(new PrintWriter(System.out));
     * */
    public static void setDriverManagerLogWriter(PrintWriter pw) {
        DriverManager.setLogWriter(pw);
    }


    
   

    /**Schaltet den DebugModus ein oder aus.
     * @param truefalse an/aus
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
    

    /** Gibt die aktuelle DriverNummber zurück.
     * @return Ganzzahl
     */
    public Driver getDriver() {
        return internalDriver;
    }

    
    /**Die toString-Methode wurde überschrieben.
     * @return  Treiber/URL/User
     */
    @Override
    public String toString() {
        return "DBQuickConnect@"+Integer.toHexString(hashCode())+" URL=" + this.url;
    }


    
    /**Gibt die derzeitige URL zurück.
     * @return Connectionstring
     */
    public String getUrl() {
        return url;
    }


    
    /**Gibt den Benutzer zurück.
     * @return Benutzernamen
     */
    public String getUser() {
        return user;
    }


    
    /**Transaktions Modus setzen.
     * Einschalten / Ausschalten des Autocommit modus.
     * @param OnOff Ein Aus.
     * @throws SQLException Error
     */
    public void setAutoCommit(boolean OnOff) throws java.sql.SQLException {
        connection.setAutoCommit(OnOff);
    }


    
    /** Startet eine Datenbanktransaktion.
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

    /**
     * Setzt das Passwort
     * @param pass Passwort
     */
    public void setPassword(String pass) {
        this.pass = pass;
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
        String restr =  "Statements closed: " +stmt_closed + " of total " +
                (stmt_created+stmt_used)+" (created " +
                stmt_created +", reused " + stmt_used +")" +
                    "  PreparedStatements closed: " +  pstmt_closed+ " of total " +pstmt_created;

        return restr;
//        return mSCache.getReleaseInfo();
    }




    public class StmtCache {

        private Connection con;
        private Stack cache;
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
                    logger.debug("creating an new Statement " + Integer.toHexString(st.hashCode()));
                }
                return st;

            } else {

                Statement st = (Statement) cache.pop();
                this.count_reused++;

                if (debug && st != null) {
                    logger.debug("using a cached Statement " + Integer.toHexString(st.hashCode()) +
                            " (" + cache.size() + " left)");
                }
                return st;

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
                logger.debug("releasing Statement " + Integer.toHexString(statement.hashCode()) +
                        " (cachesize = " + cache.size() + ")");
            }

        }

        

        public void closeall() throws java.sql.SQLException {
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
            return "StmtCache@"+Integer.toHexString(hashCode());
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

    public PreparedStatement getPreparedStatement(String sql) throws SQLException {

        PreparedStatement st = con.prepareStatement(sql);
        pstObjectSet.add(st);
        count_created++;

        if (debug && st != null) {
            logger.debug("creating an new PreparedStatement " + Integer.toHexString(st.hashCode()));
        }
        return st;

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

                if (!prepStmt.isClosed()) {
                    if (debug) {
                        logger.debug("closing PreparedStatement " + prepStmt);
                    }
                    prepStmt.close();
                    count_closed++;
                }



            } catch (Exception e) {
                if (debug) {
                    logger.debug("error closing PreparedStatement " +
                            prepStmt+": "+e.getMessage());
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
            return "PstStmtWatcher@"+Integer.toHexString(hashCode());
        }



    }//class PstStmtWatcher



}
