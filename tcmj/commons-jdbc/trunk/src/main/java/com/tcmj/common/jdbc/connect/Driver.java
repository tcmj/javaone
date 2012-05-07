package com.tcmj.common.jdbc.connect;

import java.util.EnumSet;

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

    public static final EnumSet<Driver> NO_DRIVER = EnumSet.of(NOTSELECTED,CUSTOM);
    public static final EnumSet<Driver> VALID_DRIVERS = EnumSet.range(ODBC,JAVADB_EMBEDDED);
    
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

    public final boolean isUrlBuildable() {
        return VALID_DRIVERS.contains(this);
    }
    
//    /** eg.: Driver.CUSTOM.setDriverClassName("com.xy.Driver") */
//    public void setDriverClassName(String driver) {
//        this.driver = driver;
//    }
//
//    /** eg.: Driver.CUSTOM.setUrl("jdbc:ooo:xxx") */
//    public void setUrl(String urlprefix) {
//        this.urlprefix = urlprefix;
//    }

    /**Helper to create an URL for your jdbc connection depending of the Driver.<br>
     * Examples:<br>
     * Driver.ODBC: createURL(odbcname,null,null)<br>
     * Driver.ACCESS_MDB: createURL(".\dbpath\Accesfile.mdb")<br>
     * Driver.ORACLE: createURL(SID,OraHost,1521)<br>
     * Driver.MSSQL: createURL(DBName,HostName,1433)<br>
     * Driver.MYSQL: createURL(DBName,HostName,null)<br>
     * Driver.HSQLDB: createURL(DBName,HostName,null)<br>
     * Driver.JAVADB_NETWORK: createURL(DBName,HostName,Port)<br>
     * Driver.JAVADB_EMBEDDED: createURL(DBName,null,null)<br>
     * @param dbname - DBName or ODBC-Alias or Oracle-SID.
     * @param host HostName of the database server.
     * @param port Port of the database server.
     * @return ready to use URL which can pe passed to the setURL(String) method.
     */
    public static String createURL(String dbname,
            String host, String port, Driver driver) {

        if (driver ==null || NO_DRIVER.contains(driver)) {
            throw new IllegalArgumentException("Cannot construct URL from "+driver);
        } else {

            String url = driver.getUrl();

            switch (driver) {
                case ODBC:
                case ACCESS_MDB:
                    url = url.concat(dbname);
                    break;
                case ORACLE:
                    url = url.concat(host + ":" + port + ":" + dbname);
                    break;
                case MSSQL:
                    url = url.concat(host + ":" + port + "/" + dbname);
                    break;
                case MYSQL:
                    url = url.concat(host + "/" + dbname);
                    break;
                case HSQLDB:
                    url = url.concat(dbname + "/" + host);
                    break;
                case JAVADB_NETWORK:
                    url = url.concat(host + ":" + port + "/" + dbname);
                    break;
                case JAVADB_EMBEDDED:
                    url = url.concat(dbname);
                    break;                                                                
                default:
                    throw new UnsupportedOperationException("URL creation is currently not implemented for "+driver);
            }
            return url;
        }
    }

}
