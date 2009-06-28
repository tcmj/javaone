package com.tcmj.common.jdbc.connect;

/**
 * Predefined JDBC drivers and URLs.
 * @author tcmj - Thomas Deutsch
 */
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

    /** eg.: Driver.CUSTOM.setUrl("jdbc:ooo:xxx") */
    public void setUrl(String urlprefix) {
        this.urlprefix = urlprefix;
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
    public static String createURL(String pDB_Alias_SID,
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

}
