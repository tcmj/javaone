/*
 * MetaEntry.java
 * Created:  23. November 2005, 22:26
 * Modified:
 */

package org.tcmj.db.tools;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author tdeut - Thomas Deutsch - tcmj
 */
public class MetaEntry {
    
    /** Version Info */
    private static final String CLASSVERSION = "[1.0] [org.tcmj.db.tools.MetaEntry] [23. November 2005]";
    
    private String columnname;
    
    private int datatype;
    
    private String typename;
    
    private int columnsize;
    
    private int decimaldigits;
    
    private int numprecradix;
    
    private boolean nullable;
    
    private String remarks;
    
    private int charoctetlength;
    
    private int ordinalposition;
    
    private String defaultval;
    
    
    private String classname;
    
    /** Konstruktor */
    public MetaEntry(ResultSet rs) throws  SQLException{
        this.columnname = rs.getString("COLUMN_NAME");
        this.datatype = rs.getInt("DATA_TYPE");
        this.typename = rs.getString("TYPE_NAME");
        
        this.columnsize  = rs.getInt("COLUMN_SIZE");
        this.decimaldigits  = rs.getInt("DECIMAL_DIGITS");
        this.numprecradix = rs.getInt("NUM_PREC_RADIX");
        this.nullable = rs.getBoolean("NULLABLE");
        this.remarks = rs.getString("REMARKS");
        this.defaultval = rs.getString("COLUMN_DEF");
//        String isnullable = rs.getString("IS_NULLABLE");
        this.charoctetlength = rs.getInt("CHAR_OCTET_LENGTH");
        this.ordinalposition = rs.getInt("ORDINAL_POSITION");
        
        
        
    }
    
    /** Methode
     */
    private void me() throws Exception{
        
    }
    
    
    /**
     * Gibt Informationen der Klasse zurueck.
     * @return Version Datum und Klassenname
     */
    public static final String getVersion(){
        return CLASSVERSION;
    }
    
    
    
    
    public String getColumnname() {
        return columnname;
    }
    
    public void setColumnname(String columnname) {
        this.columnname = columnname;
    }
    
    public int getDatatype() {
        return datatype;
    }
    
    public void setDatatype(int datatype) {
        this.datatype = datatype;
    }
    
    public String getTypename() {
        return typename;
    }
    
    public void setTypename(String typename) {
        this.typename = typename;
    }
    
    public int getDecimaldigits() {
        return decimaldigits;
    }
    
    public void setDecimaldigits(int decimaldigits) {
        this.decimaldigits = decimaldigits;
    }
    
    public int getNumprecradix() {
        return numprecradix;
    }
    
    public void setNumprecradix(int numprecradix) {
        this.numprecradix = numprecradix;
    }
    
    public boolean isNullable() {
        return nullable;
    }
    
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    public int getCharoctetlength() {
        return charoctetlength;
    }
    
    public void setCharoctetlength(int charoctetlength) {
        this.charoctetlength = charoctetlength;
    }
    
    public int getOrdinalposition() {
        return ordinalposition;
    }
    
    public void setOrdinalposition(int ordinalposition) {
        this.ordinalposition = ordinalposition;
    }

    public String getDefaultval() {
        return defaultval;
    }

    public void setDefaultval(String defaultval) {
        this.defaultval = defaultval;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public int getColumnsize() {
        return columnsize;
    }

    public void setColumnsize(int columnsize) {
        this.columnsize = columnsize;
    }
    
}
