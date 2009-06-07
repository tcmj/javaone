/*
 * ClassGenerator.java
 * Created:  30. August 2005, 23:32
 * Modified:
 */

package org.tcmj.db.tools;
import generated.Typetest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Iterator;
import java.util.LinkedList;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

/**
 * Erzeugt aus einem Datenbankschema Javaklassen.
 * @author tdeut - Thomas Deutsch - tcmj
 */
public class ClassGenerator {
    
    /** Version Info */
    private static final String CLASSVERSION = "[1.0] [org.tcmj.db.tools.ClassGenerator] [30. August 2005]";
    
    /** Log4J Logger */
    private static final Logger logger = Logger.getLogger(ClassGenerator.class.getName());
    
    /** Datenbankverbindung */
    private Connection con;
    
    /** Datenbank Meta Daten */
    private DatabaseMetaData meta;
    
    private LinkedList armetas;
    /**
     * Holds value of property packageName.
     */
    private String packageName;
    
    
    private boolean dates2timestampflag = false;
    
    private static final String LINE = System.getProperty("line.separator");
    private static final String LINE2 = LINE.concat(LINE);
    
    private static final String SPACE = " ";
    private static final String TAB = "   ";
    private static final String LB = "{";
    private static final String RB = "}";
    private static final String LK = "(";
    private static final String RK = ")";
    private static final String S = ";";
    private static final String A = "\"";
    private static final String K = ", ";
    private static final String PLUS = "+";
    
    private static final String PUBLIC = "public ";
    private static final String PRIVATE = "private ";
    private static final String VOID = "void ";
    private static final String STATICFINAL = "static final ";
    private static final String GET = "get";
    private static final String SET = "set";
    private static final String RETURN = "return ";
    
    private static final String JDOCSTART = "/** ";
    private static final String JDOCEND = " */";
    private static final String JDOCLINE = " * ";
    private static final String JDOCPARAM = " * @param ";
    private static final String JDOCRETURN = " * @return ";
    
    /**
     * Konstruktor */
    public ClassGenerator(Connection c) throws ClassGeneratorException{
        
        this.con = c;
        
        if(c==null){
            throw new ClassGeneratorException("Connection can not be null!");
        }else{
            try{
                if(c.isClosed()){
                    throw new ClassGeneratorException("Connection can not be closed!");
                }else{
                    
                    this.meta = con.getMetaData();
                    
                    
                }
            }catch(SQLException ex){
                throw new ClassGeneratorException("Cannot load DatabaseMetaData: "+
                        ex.getMessage());
            }
        }
        
    }
    
    
    /** Flag to decide if 'DATE' columns shall be mapped as java.sql.Timestamp 
      * Objects instead of java.sql.Date Objects.<br>
      * This can be useful to put date and time into the cell */
    public void setMapDatesToTimestamps(boolean flag){
        this.dates2timestampflag = flag;
    }

    private static void log4jconfig(){
        try{
            
//            org.apache.log4j.BasicConfigurator.configure();
            
            PatternLayout pat = new PatternLayout("%-5p %m %n");
            ConsoleAppender conap = new ConsoleAppender(pat);
            logger.addAppender(conap);
            
            
        }catch(Exception ex){
            System.out.println("Error instantiating log4j: "+ex.getMessage());
        }
    }
    
    
    private ResultSet loadColumMetaData(String tbl) throws SQLException{
        //Load Colum Metadata: (catalog, schemapattern, tablepattern, columnpattern)
        return this.meta.getColumns(null, null, tbl.toUpperCase(), null);
    }
    
    
    private void loadMetaEntries(String table) throws Exception{
        
        armetas = new LinkedList();
        
        ResultSet rs = loadColumMetaData(table);
        
        while(rs.next()){
            
            MetaEntry me = new MetaEntry(rs);
            armetas.add(me);
        }
        rs.close();
        
        
        String sql = "Select * from "+table;
        Statement s = this.con.createStatement();
        ResultSet tres = s.executeQuery(sql);
        ResultSetMetaData rsmd = tres.getMetaData();
        
        int colcount = rsmd.getColumnCount();
        
        Iterator it = armetas.iterator();
        
        
        for(int i=1;it.hasNext();i++){
            
            MetaEntry en = (MetaEntry)it.next();
            
            int pos = en.getOrdinalposition();
            
            String cname = rsmd.getColumnClassName(pos);
            int ctype = rsmd.getColumnType(pos);
            String ctypename = rsmd.getColumnTypeName(pos);
            
            en.setClassname(cname);
            if(en.getDatatype()!=ctype){
                logger.debug("columntype is not equal! "+en.getDatatype()+" and "+ctype+" (old/new)");
                en.setDatatype(ctype);
            }
            
            if(! en.getTypename().equals(ctypename)){
                logger.debug("typename is not equal! "+en.getTypename()+" and "+ctypename+" (old/new)");
                en.setTypename(ctypename);
            }
            
            //option to use Timestamp instead of Date
            if(dates2timestampflag){
                if(en.getDatatype()==Types.DATE){
                    en.setDatatype(Types.TIMESTAMP);
                    logger.debug("Changing column '"+en.getColumnname()+"' to TimeStamp!");
                }
                
                
            }
            
            
        }
        
        tres.close();
        s.close();
        
    }
    
    
    /** Methode
     */
    public StringBuffer createClass(String table) throws ClassGeneratorException{
        
        try{
            
            loadMetaEntries(table);
            
            
            ResultSet rs88 = loadColumMetaData(table);
            debugColumnMeta(rs88);
            rs88.close();
            
            StringBuffer bu = new StringBuffer(10000);
            
            //Java Paketname (nur wenn vorher gesetzt wurde)
            appendPackage(bu);
            
            //Java Imports (java.sql)
            appendImports(bu);
            
            //Class Description:
            appendJDocClass(bu, table);
            
            //Class Declaration:
            appendClassHeader(bu, table);
            
            //Class Variables:
            appendAttributes(bu, table);
            
            //Empty Constructor:
            appendJDocConstructorEmpty(bu, table);
            appendConstructorEmpty(bu, table);
            
            //ResultSet Constructor:
            appendJDocConstructorResultSet(bu, table);
            appendConstructorResultSet(bu, table);
            
            //Select, Insert and Update Statements:
            appendStatements(bu, table);
            
            //Method to fill a PreparedStatement with values:
            appendJDocFillUpdateOrInsert(bu, table);
            appendFillUpdateOrInsert(bu, table);
            
            //Getters and Setters:
            appendGetterAndSetter(bu);
            
            //toString representation:
            appendtoString(bu, table);
            
            //Class Footer:
            appendClassFooter(bu, table);
            
            
            
            System.out.println(bu.toString());
            
            
            
            //Output to filesystem:
            output2File(new File("src/generated/"+table+".java"),bu);
            
            
            
            
        }catch(Exception ex){
            logger.error("Errare!",ex);
            throw new ClassGeneratorException("Error: "+ex.getMessage());
        }
        
        return null;
        
    }
    
    
    private void output2File(File file, StringBuffer buffer)
    throws FileNotFoundException,IOException{
        java.io.FileOutputStream o = new java.io.FileOutputStream(file);
        o.write(buffer.toString().getBytes());
        o.flush();
        o.close();
    }
    
    
    private void appendGetterAndSetter(StringBuffer bu)
    throws SQLException{
        
        Iterator it = this.armetas.iterator();
        
        while(it.hasNext()){
            
            MetaEntry e = (MetaEntry)it.next();
            
            String name = e.getColumnname();
            int datatype = e.getDatatype();
            String typename = e.getTypename();
            
            appendGetter(bu, name, datatype);
            
            appendSetter(bu, name, datatype, typename);
            
        }
        
    }
    
    
    private void appendPackage(StringBuffer bu){
        if(getPackageName()!=null){
            bu.append("package "+getPackageName()+S+LINE+LINE);
        }
    }
    
    
    private static void appendImports(StringBuffer bu){
        bu.append("import java.sql.PreparedStatement;"+LINE);
        bu.append("import java.sql.ResultSet;"+LINE);
        bu.append("import java.sql.SQLException;"+LINE);
        bu.append("import java.sql.Timestamp;"+LINE);
        bu.append("import java.sql.Time;"+LINE);
        bu.append("import java.sql.Date;"+LINE);
        bu.append("import java.sql.Blob;"+LINE);
        bu.append("import java.sql.Clob;"+LINE);
        bu.append("import java.math.BigDecimal;"+LINE);
        
        bu.append(LINE2);
    }
    
    
    private void appendJDocClass(StringBuffer bu, String tablename){
        bu.append(JDOCSTART+LINE);
        bu.append(JDOCLINE+" Java representation of table '"+tablename.toUpperCase()+"'"+LINE);
        try{
            bu.append(JDOCLINE+" DatabaseVendor:  "+this.meta.getDatabaseProductName()+LINE);
            bu.append(JDOCLINE+" DatabaseVersion: "+this.meta.getDatabaseProductVersion()+LINE);
            bu.append(JDOCLINE+" JDBC Drivername: "+this.meta.getDriverName()+LINE);
            bu.append(JDOCLINE+" JDBC URL: "+this.meta.getURL()+LINE);
            bu.append(JDOCLINE+" JDBC User: "+this.meta.getUserName()+LINE);
        }catch (SQLException sqle) {}
        
        bu.append(JDOCEND+LINE);
        
    }
    
    
    private void appendClassHeader(StringBuffer bu, String table){
        bu.append(PUBLIC+"class "+table+SPACE+LB+LINE2);
    }
    
    
    private static void appendJDocConstructorEmpty(StringBuffer bu, String tablename){
        bu.append(LINE2);
        bu.append(TAB+JDOCSTART+LINE);
        bu.append(TAB+JDOCLINE+"Empty Constructor of Class '"+tablename+"'."+LINE);
        bu.append(TAB+JDOCLINE+"Formally used to insert a completly new record into the table."+LINE);
        bu.append(TAB+JDOCEND+LINE);
    }
    
    private static void appendJDocConstructorResultSet(StringBuffer bu, String tablename){
        bu.append(TAB+JDOCSTART+LINE);
        bu.append(TAB+JDOCLINE+"ResultSet Constructor of Class '"+tablename+"'."+LINE);
        bu.append(TAB+JDOCLINE+"Formally used to read and store one record from the table."+LINE);
        bu.append(TAB+JDOCEND+LINE);
    }
    
    
    private static void appendJDocFillUpdateOrInsert(StringBuffer bu, String tablename){
        bu.append(TAB+JDOCSTART+LINE);
        bu.append(TAB+JDOCLINE+"Method to set all column values of this instance."+LINE);
        bu.append(TAB+JDOCLINE+"in a given PreparedStatement object."+LINE);
        bu.append(TAB+JDOCPARAM+"pst allready prepared PreparedStatement object with all columns."+LINE);
        bu.append(TAB+JDOCEND+LINE);
    }
    
    
    private void appendClassFooter(StringBuffer bu, String table){
        bu.append(RB+"//end class: "+table);
    }
    
    private void appendFillUpdateOrInsert(StringBuffer bu, String table)
    throws SQLException{
        
        //fillUpdate:***************************************************
        bu.append(TAB+PUBLIC+VOID+"fillUpdateOrInsert(PreparedStatement pst)"+LINE);
        bu.append(TAB+" throws SQLException"+LB+LINE);
        
        int paramindex = 1;
        Iterator it = this.armetas.iterator();
        
        while(it.hasNext()){
            
            MetaEntry e = (MetaEntry)it.next();
            String name = e.getColumnname();
            int datatype = e.getDatatype();
            
            bu.append(TAB+TAB+"pst.set"+getJavaType(datatype,true)+"(");
            bu.append(paramindex+K+"this."+name.toLowerCase()+RK+S+LINE);
            paramindex++;
        }
        
        bu.append(TAB+RB+LINE);
        bu.append(LINE);
        bu.append(LINE);
        //end: fillUpdate:***************************************************
        
    }
    
    private void appendAttributes(StringBuffer bu, String table)
    throws SQLException{
        
        logger.debug("appending Attributes of table '"+table+"'");
        
        Iterator it = armetas.iterator();
        
        while(it.hasNext()){
            
            MetaEntry e = (MetaEntry)it.next();
            
            bu.append(TAB);
            bu.append(JDOCSTART);
            bu.append("Column '"+e.getColumnname().toUpperCase()+"' ");
            bu.append(e.getTypename().toUpperCase());
            
            if(     e.getDatatype() != Types.BLOB && 
                    e.getDatatype() != Types.CLOB &&
                    e.getDatatype() != Types.DATE &&
                    e.getDatatype() != Types.TIMESTAMP &&
                    e.getDatatype() != Types.OTHER){
                bu.append(LK+e.getColumnsize());
                bu.append((e.getDecimaldigits()>0?","+e.getDecimaldigits():""));
                bu.append(RK+SPACE);
            }
            bu.append( (e.isNullable()?"":"NOT NULL") );
            
            bu.append((e.getDefaultval()==null?"  ":"  DEFAULT "+e.getDefaultval()));
            bu.append(JDOCEND);
            bu.append(LINE);
            
            bu.append(TAB);
            bu.append(PRIVATE);
            bu.append(getJavaType(e.getDatatype(), false));
            bu.append(SPACE);
            bu.append(e.getColumnname().toLowerCase());
            bu.append(S);
            bu.append(LINE2);
            
        }
        
        
    }
    
    
    private void appendConstructorEmpty(StringBuffer bu, String table){
        
        //Konstruktor:
        bu.append(TAB+PUBLIC+table+LK+RK+LB+LINE);
        bu.append(TAB+TAB+"//Konstruktor Code"+LINE);
        bu.append(TAB+TAB+"//TODO initialization of "+table+" (default settings) "+LINE);
        bu.append(TAB+RB+LINE);
        bu.append(LINE2);
        
    }
    
    
    private void appendConstructorResultSet(StringBuffer bu, String table)
    throws SQLException{
        
        //ResultSet Constructor:
        bu.append(TAB+PUBLIC+table+LK);
        bu.append("ResultSet rs");
        bu.append(RK+" throws SQLException"+LB+LINE);
        
        Iterator it = this.armetas.iterator();
        
        while(it.hasNext()){
            
            MetaEntry e = (MetaEntry)it.next();
            
            bu.append(TAB+TAB+"this."+e.getColumnname().toLowerCase()+
                    "\t = rs.get"+getJavaType(e.getDatatype(),true)+"(\""+
                    e.getColumnname()+"\""+RK+S+LINE);
            
        }
        
        bu.append(TAB+RB+LINE);
        bu.append(LINE);
        bu.append(LINE);
        
        
    }
    
    
    
    private void appendtoString(StringBuffer bu, String table)
    throws SQLException{
        
        //ResultSet Constructor:
        bu.append(TAB+PUBLIC+" String toString"+LK+RK+LB+LINE2);
        bu.append(TAB+TAB+"StringBuffer buffer = new StringBuffer();"+LINE2);
        bu.append(TAB+TAB+"buffer.append("+A+table+": "+A+");"+LINE2);
        
        
        Iterator it = this.armetas.iterator();
        
        while(it.hasNext()){
            
            MetaEntry e = (MetaEntry)it.next();
            
            String colname = e.getColumnname();
            int datatype = e.getDatatype();
            
            bu.append(TAB+TAB+"buffer.append("+A+"["+colname+"="+A+" + this."+colname.toLowerCase()+"+"+A+"]"+A+");"+LINE);
            
        }
        
        bu.append(TAB+TAB+"return buffer.toString()"+S+LINE2);
        
        bu.append(TAB+RB+LINE);
        bu.append(LINE);
        bu.append(LINE);
        
        
    }
    
    
    
    private void appendStatements(StringBuffer bu, String table)
    throws SQLException{
//------------------------------//------------------------------
        //SQL Statements (static)
        
        StringBuffer bu_sqlsel = new StringBuffer(333);
        StringBuffer bu_sqlins = new StringBuffer(333);
        StringBuffer bu_sqlupd = new StringBuffer(333);
        StringBuffer bu_columns = new StringBuffer(333);
        StringBuffer bu_column_names = new StringBuffer(333);
        
        bu_sqlsel.append(TAB+PUBLIC+STATICFINAL+"String getSQLSelect()"+LB+LINE);
        bu_sqlins.append(TAB+PUBLIC+STATICFINAL+"String getSQLPreparedInsert()"+LB+LINE);
        bu_sqlupd.append(TAB+PUBLIC+STATICFINAL+"String getSQLPreparedUpdate()"+LB+LINE);
        
        bu_sqlsel.append(TAB+TAB+RETURN+A+"SELECT "+A+PLUS+LINE);
        bu_sqlins.append(TAB+TAB+RETURN+A+"INSERT INTO "+table.toUpperCase()+LK+A+PLUS+LINE);
        bu_sqlupd.append(TAB+TAB+RETURN+A+"UPDATE "+table.toUpperCase()+SPACE);
        
        int immer3 = 0;
        
        
        int counter = 0;
        Iterator it = this.armetas.iterator();
        
        while(it.hasNext()){
            
            MetaEntry e = (MetaEntry)it.next();
            
            
            
            
            
            String name = e.getColumnname().toUpperCase();
            
            if(immer3 != 0){
                bu_column_names.append(K);
                bu_columns.append(K);
                bu_sqlupd.append(K);
            }
            
            if(immer3 % 3 == 0){
                if(immer3 != 0){
                    bu_columns.append(A+PLUS+LINE);
                    bu_column_names.append(LINE+TAB+JDOCLINE);
                }
                
                bu_columns.append(TAB+TAB+TAB+A);
            }
            bu_column_names.append(e.getColumnname());
            bu_columns.append(e.getColumnname());
            
            //update:
            bu_sqlupd.append(A+PLUS+LINE+TAB+TAB+TAB+A+" ");
            
            if(++counter==1){
                bu_sqlupd.append("SET ");
            }
            
            bu_sqlupd.append(e.getColumnname()+" = ?");
            
            immer3++;
        }
        
        
        bu_sqlsel.append(bu_columns);
        bu_sqlsel.append(SPACE+A+PLUS+LINE+TAB+TAB+TAB+A+"FROM "+table.toUpperCase()+A+S+LINE+LINE);
        bu_sqlsel.append(TAB+RB+LINE+LINE);
        
        bu_sqlins.append(bu_columns);
        bu_sqlins.append(SPACE+A+PLUS+LINE+TAB+TAB+TAB+A+") VALUES ( ");
        
        for(int i=0;i<immer3;i++){
            if(i % 10 == 0 && (i != 0)){
                bu_sqlins.append(K+A+PLUS+LINE+TAB+TAB+TAB+A);
            }
            if(i!=0 && !(i % 10 == 0)){
                bu_sqlins.append(K);
            }
            bu_sqlins.append("?");
        }
        bu_sqlins.append(SPACE+RK+A+S+LINE+LINE+LINE);
        bu_sqlins.append(TAB+RB+LINE+LINE);
        
        
        bu_sqlupd.append(SPACE+A+S+LINE);
        bu_sqlupd.append(TAB+RB+LINE+LINE+LINE);
        
        
        bu.append(LINE);
        bu.append(LINE);
        
        
        bu.append(TAB+JDOCSTART+LINE);
        bu.append(TAB+JDOCLINE+"Get the SQL Statement to select all Columns from Table '"+table.toUpperCase()+"'."+LINE);
        bu.append(TAB+JDOCLINE+bu_column_names+LINE);
        bu.append(TAB+JDOCRETURN+"SQL Statement to select "+immer3+" Columns!"+LINE);
        bu.append(TAB+JDOCEND+LINE);
        bu.append(bu_sqlsel);
        bu.append(LINE);
        
        
        bu.append(TAB+JDOCSTART+LINE);
        bu.append(TAB+JDOCLINE+"Get the Prepared SQL Statement to insert into Table '"+table.toUpperCase()+"'."+LINE);
        bu.append(TAB+JDOCLINE+bu_column_names+LINE);
        bu.append(TAB+JDOCRETURN+"SQL Statement to insert "+immer3+" Columns!"+LINE);
        bu.append(TAB+JDOCEND+LINE);
        bu.append(bu_sqlins);
        bu.append(LINE);
        
        
        bu.append(TAB+JDOCSTART+LINE);
        bu.append(TAB+JDOCLINE+"Get the Prepared SQL Statement to update Table '"+table.toUpperCase()+"'."+LINE);
        bu.append(TAB+JDOCLINE+bu_column_names+LINE);
        bu.append(TAB+JDOCLINE+"Use {@link fillUpdateOrInsert(PreparedStatement)} to fill your Prepared Statement!"+LINE);
        bu.append(TAB+JDOCRETURN+"SQL Statement to update "+immer3+" Columns!"+LINE);
        bu.append(TAB+JDOCEND+LINE);
        bu.append(bu_sqlupd);
        
        
        bu.append(LINE);
        bu.append(LINE);
        bu.append(LINE);
        //------------------------------//------------------------------
        
    }
    
    
    private void appendGetter(StringBuffer bu, String name, int datatype){
        
        //JDOC:
        bu.append(TAB+JDOCSTART);
        bu.append("Getter for Column '"+name.toUpperCase()+"'."+LINE);
        bu.append(TAB+SPACE+JDOCRETURN+"Column Value ("+getJavaType(datatype, false)+")");
        bu.append(TAB+TAB+TAB+TAB+JDOCEND+LINE);
        
        
        //Getter:
        bu.append(TAB);
        bu.append(PUBLIC);
        bu.append(getJavaType(datatype,false));
        bu.append(SPACE);
        bu.append("get");
        bu.append(toCapitalizedString(getJavaBeanName(name)));
        bu.append(LK);
        bu.append(RK);
        bu.append(LB);
        bu.append(LINE);
        bu.append(TAB);
        bu.append(TAB);
        bu.append("return this.");
        bu.append(name.toLowerCase());
        bu.append(S);
        bu.append(LINE);
        bu.append(TAB);
        bu.append(RB);
        bu.append(LINE);
        bu.append(LINE);
        
    }
    
    private static final void appendSetter(StringBuffer bu, String name,
            int datatype, String typename){
        
        
        //JDOC:
        bu.append(TAB+JDOCSTART);
        bu.append("Setter for Column '"+name.toUpperCase()+"'."+LINE);
        bu.append(TAB+SPACE+JDOCPARAM+"value new Column Value ("+typename+")");
        bu.append(TAB+TAB+TAB+TAB+JDOCEND+LINE);
        
        
        //Setter:
        bu.append(TAB);
        bu.append(PUBLIC);
        bu.append(VOID);
        bu.append("set");
        bu.append(toCapitalizedString(getJavaBeanName(name)));
        bu.append(LK);
        bu.append(getJavaType(datatype,false));
        bu.append(SPACE);
        bu.append("value");
        bu.append(RK);
        bu.append(LB);
        bu.append(LINE);
        
        bu.append(TAB);
        bu.append(TAB);
        bu.append("this.");
        bu.append(name.toLowerCase());
        bu.append(" = ");
        bu.append("value");
        bu.append(S);
        bu.append(LINE);
        bu.append(TAB+RB+LINE);
        bu.append(LINE);
        
        
    }
    
    
    
    
    
    
    private static final String getJavaType(int type, boolean capitalize){
        String tp = "Object";
        switch(type){
            case Types.BLOB:
                tp = "Blob";
                break;
            case Types.CLOB:
                tp = "Clob";
                break;
            case Types.BIT:
                tp = (capitalize)?"Boolean":"boolean";
                break;
            case Types.DATE:
                tp = "Date";
                break;
            case Types.TIME:
                tp = "Time";
                break;
            case Types.INTEGER:
                tp = (capitalize)?"Int":"int";
                break;
            case Types.FLOAT:
            case Types.DOUBLE:
                tp = (capitalize)?"Double":"double";
                break;
            case Types.NUMERIC:
            case Types.DECIMAL:
                tp = "BigDecimal";
                break;
            case Types.BIGINT:
                
                tp = "long";
                tp = (capitalize)?"Long":"long";
                break;
            case Types.REAL:
                tp = "float";
                tp = (capitalize)?"Float":"float";
                break;
            case Types.TIMESTAMP:
                tp = "Timestamp";
                break;
            case Types.TINYINT:
                tp = "byte";
                tp = (capitalize)?"Byte":"byte";
                break;
            case Types.SMALLINT:
                tp = "short";
                tp = (capitalize)?"Short":"short";
                break;
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
                tp = "String";
                break;
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
//                tp = "byte[]";
                tp = (capitalize)?"Bytes":"byte[]";
                break;
            default:
                tp = "Object";
                
                
                logger.debug("Type with number: "+type+" not found!");
                break;
        }
        return tp;
        
    }
    
    
    
    
    
    
    
    
    private static final String getJavaBeanName(String attribute){
        String first = attribute.substring(0,1);
        String rest  = attribute.substring(1);
        return first.toUpperCase().concat(rest);
    }
    
    
    /**
     * Gibt Informationen der Klasse zurueck.
     * @return Version Datum und Klassenname
     */
    public static final String getVersion(){
        return CLASSVERSION;
    }
    
    
    /** Example Start Methode.
     * @param args Kommandozeilenparameter
     */
    public static void main(String[] args) {
        log4jconfig();
        
        Connection co = null;
        try{
            /*
            //MS SQL Connection:
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String user="privuser", pwd="privuser";
            //String url = "jdbc:jtds:sqlserver://localhost:1433/pm41";
            //String user="sa", pwd="";
            //String url = "jdbc:jtds:sqlserver://TDEUT:1433/tcmjdb;instance=TCMJMSDE";
            String url = "jdbc:jtds:sqlserver://TDEUT:1433/inteco;instance=TCMJMSDE";
             */
            
            //Oracle Connection:
            //Class.forName("oracle.jdbc.driver.OracleDriver");
            //String user="privuser4", pwd="privuser4";
            //String url ="jdbc:oracle:thin:@tdeut:1521:xe";
            
            
            Class.forName("oracle.jdbc.driver.OracleDriver");
            String user="tcmj", pwd="tcmj";
            String url ="jdbc:oracle:thin:@tdeut:1521:xe";
            
            
            co = DriverManager.getConnection(url,user,pwd);
            
            ClassGenerator app = new ClassGenerator(co);
            app.setPackageName("generated");
            app.setMapDatesToTimestamps(true);
            app.createClass("Typetest");
//            ResultSet rs;
            
            
//*
            //TODO TestCase anlegen!
            Statement s = co.createStatement();
            
            String wheresuffix = " where tnumber=100";
            
            String sql = Typetest.getSQLSelect().concat(wheresuffix);
            System.out.println("sql: "+sql);
            ResultSet rs = s.executeQuery(sql);
            int count = 0;
            while(rs.next()){
                count++;
                ResultSetMetaData rsm = rs.getMetaData();
                
                
                System.out.println("1: "+rsm.getColumnTypeName(1));
                System.out.println("2: "+rsm.getColumnTypeName(2));
                System.out.println("3: "+rsm.getColumnTypeName(3));
                System.out.println("4: "+rsm.getColumnTypeName(4));
                System.out.println("5: "+rsm.getColumnTypeName(5));
                System.out.println("6: "+rsm.getColumnTypeName(6));
                System.out.println("7: "+rsm.getColumnTypeName(7));
                System.out.println("1: "+rsm.getColumnClassName(1));
                System.out.println("2: "+rsm.getColumnClassName(2));
                System.out.println("3: "+rsm.getColumnClassName(3));
                System.out.println("4: "+rsm.getColumnClassName(4));
                System.out.println("5: "+rsm.getColumnClassName(5));
                System.out.println("6: "+rsm.getColumnClassName(6));
                System.out.println("7: "+rsm.getColumnClassName(7));
                for(int i=1;i<8;i++){
                    System.out.println("-"+i+"-> "+rsm.getColumnType(i));
                }
                
                
                
                
                Typetest p = new Typetest(rs);
                String sel = p.toString();
                System.out.println(sel);
                p.setTlong("444");
                PreparedStatement upd = co.prepareStatement(Typetest.getSQLPreparedUpdate().concat(wheresuffix));
                p.fillUpdateOrInsert(upd);
                int pc = upd.executeUpdate();
                System.out.println("equal: "+pc+"| "+p);
                
            }
            System.out.println("instances: "+count);
            s.close();
            // */
        }catch(Exception ex){
            logger.error("An Error Occured: ",ex);
        }
        if(co!=null){
            try{
                co.close();
            }catch(Exception ex){
                System.out.println("Error in closing the Connection: "+ex.getMessage());
            }
        }
        
    }
    
    public class ClassGeneratorException extends SQLException{
        public ClassGeneratorException(String msg){
            super(msg);
        }
    }
    
    
    
    /**
     * Getter for property packageName.
     * @return Value of property packageName.
     */
    public String getPackageName() {
        
        return this.packageName;
    }
    
    /**
     * Setter for property packageName.
     * @param packageName New value of property packageName.
     */
    public void setPackageName(String packageName) {
        
        this.packageName = packageName;
    }
    
    
    private static final String toCapitalizedString(String in){
        String lower = in.toLowerCase();
        return (lower.substring(0,1)).toUpperCase().concat(lower.substring(1));
    }
    
    
    private void debugColumnMeta(ResultSet rs)throws SQLException{
        logger.debug("ColumCount: "+rs.getMetaData().getColumnCount());
        while(rs.next()){
            
            
//        logger.debug("TABLE_CAT: "+rs.getString(1));  //=> table catalog (may be null)
//        logger.debug("TABLE_SCHEM: "+rs.getString(2));  //=> table schema (may be null)
//        logger.debug("TABLE_NAME: "+rs.getString(3)); //=> table name
            logger.debug("COLUMN_NAME: "+rs.getString("COLUMN_NAME")); //=> column name
            logger.debug("DATA_TYPE: "+rs.getShort("DATA_TYPE") /*=> SQL type from java.sql.Types*/
            +" TYPE_NAME: "+rs.getString("TYPE_NAME")); //=> Data source dependent type name, for a UDT the type name is fully qualified
            logger.debug("COLUMN_SIZE: "+rs.getInt(7)); //=> column size. For char or date types this is the maximum number of characters, for numeric or decimal types this is precision.
//logger.debug("BUFFER_LENGTH: "+rs.getis not used.
            logger.debug("DECIMAL_DIGITS: "+rs.getInt("DECIMAL_DIGITS")); //=> the number of fractional digits
            logger.debug("NUM_PREC_RADIX: "+rs.getInt("NUM_PREC_RADIX")); //=> Radix (typically either 10 or 2)
            logger.debug("NULLABLE: "+rs.getInt("NULLABLE")); //=> is NULL allowed?
//        logger.debug("columnNoNulls: "+rs.getString(12)); // might not allow NULL values
//        logger.debug("columnNullable: "+rs.getString(13)); //- definitely allows NULL values
//        logger.debug("columnNullableUnknown: "+rs.getString(14)); //- nullability unknown
            logger.debug("REMARKS: "+rs.getString("REMARKS")); //=> comment describing column (may be null)
            logger.debug("COLUMN_DEF: "+rs.getString("COLUMN_DEF")); //=> default value (may be null)
//        logger.debug("SQL_DATA_TYPE: "+rs.getInt("SQL_DATA_TYPE")); //=> unused
//        logger.debug("SQL_DATETIME_SUB: "+rs.getString(18)); //=> unused
            logger.debug("CHAR_OCTET_LENGTH: "+rs.getInt("CHAR_OCTET_LENGTH")); //=> for char types the maximum number of bytes in the column
            logger.debug("ORDINAL_POSITION: "+rs.getInt("ORDINAL_POSITION")); //=> index of column in table (starting at 1)
//        logger.debug("IS_NULLABLE: "+rs.getString(21)); //=> "NO" means column definitely does not allow NULL values; "YES" means the column might allow NULL values. An empty string means nobody knows.
            
            logger.debug("***********************************");
            
        }
        logger.debug("oracle.jdbc.OracleTypes.BLOB = "+oracle.jdbc.OracleTypes.BLOB);
        logger.debug("oracle.jdbc.OracleTypes.CLOB = "+oracle.jdbc.OracleTypes.CLOB);
    }
    
    
}
