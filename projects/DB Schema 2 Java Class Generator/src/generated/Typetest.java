package generated;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Time;
import java.sql.Date;
import java.sql.Blob;
import java.sql.Clob;
import java.math.BigDecimal;


/** 
 *  Java representation of table 'TYPETEST'
 *  DatabaseVendor:  Oracle
 *  DatabaseVersion: Oracle Database 10g Express Edition Release 10.2.0.1.0 - Beta
 *  JDBC Drivername: Oracle JDBC driver
 *  JDBC URL: jdbc:oracle:thin:@tdeut:1521:xe
 *  JDBC User: TCMJ
 */
public class Typetest {

   /** Column 'TNUMBER' NUMBER(10)    */
   private BigDecimal tnumber;

   /** Column 'TBLOB' BLOB   */
   private Blob tblob;

   /** Column 'TCHAR' CHAR(2)    */
   private String tchar;

   /** Column 'TDATE' DATE   */
   private Timestamp tdate;

   /** Column 'TFLOAT' NUMBER(126)    */
   private BigDecimal tfloat;

   /** Column 'TLONG' LONG(0)    */
   private String tlong;

   /** Column 'TCLOB' CLOB   */
   private Clob tclob;

   /** Column 'TMLSLABELJKJK' VARCHAR2(1)    */
   private String tmlslabeljkjk;

   /** Column 'UUUU' NUMBER(63)    */
   private BigDecimal uuuu;



   /** 
    * Empty Constructor of Class 'Typetest'.
    * Formally used to insert a completly new record into the table.
    */
   public Typetest(){
      //Konstruktor Code
      //TODO initialization of Typetest (default settings) 
   }


   /** 
    * ResultSet Constructor of Class 'Typetest'.
    * Formally used to read and store one record from the table.
    */
   public Typetest(ResultSet rs) throws SQLException{
      this.tnumber	 = rs.getBigDecimal("TNUMBER");
      this.tblob	 = rs.getBlob("TBLOB");
      this.tchar	 = rs.getString("TCHAR");
      this.tdate	 = rs.getTimestamp("TDATE");
      this.tfloat	 = rs.getBigDecimal("TFLOAT");
      this.tlong	 = rs.getString("TLONG");
      this.tclob	 = rs.getClob("TCLOB");
      this.tmlslabeljkjk	 = rs.getString("TMLSLABELJKJK");
      this.uuuu	 = rs.getBigDecimal("UUUU");
   }




   /** 
    * Get the SQL Statement to select all Columns from Table 'TYPETEST'.
    * TNUMBER, TBLOB, TCHAR, 
    * TDATE, TFLOAT, TLONG, 
    * TCLOB, TMLSLABELJKJK, UUUU
    * @return SQL Statement to select 9 Columns!
    */
   public static final String getSQLSelect(){
      return "SELECT "+
         "TNUMBER, TBLOB, TCHAR, "+
         "TDATE, TFLOAT, TLONG, "+
         "TCLOB, TMLSLABELJKJK, UUUU "+
         "FROM TYPETEST";

   }


   /** 
    * Get the Prepared SQL Statement to insert into Table 'TYPETEST'.
    * TNUMBER, TBLOB, TCHAR, 
    * TDATE, TFLOAT, TLONG, 
    * TCLOB, TMLSLABELJKJK, UUUU
    * @return SQL Statement to insert 9 Columns!
    */
   public static final String getSQLPreparedInsert(){
      return "INSERT INTO TYPETEST("+
         "TNUMBER, TBLOB, TCHAR, "+
         "TDATE, TFLOAT, TLONG, "+
         "TCLOB, TMLSLABELJKJK, UUUU "+
         ") VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? )";


   }


   /** 
    * Get the Prepared SQL Statement to update Table 'TYPETEST'.
    * TNUMBER, TBLOB, TCHAR, 
    * TDATE, TFLOAT, TLONG, 
    * TCLOB, TMLSLABELJKJK, UUUU
    * Use {@link fillUpdateOrInsert(PreparedStatement)} to fill your Prepared Statement!
    * @return SQL Statement to update 9 Columns!
    */
   public static final String getSQLPreparedUpdate(){
      return "UPDATE TYPETEST "+
         " SET TNUMBER = ?, "+
         " TBLOB = ?, "+
         " TCHAR = ?, "+
         " TDATE = ?, "+
         " TFLOAT = ?, "+
         " TLONG = ?, "+
         " TCLOB = ?, "+
         " TMLSLABELJKJK = ?, "+
         " UUUU = ? ";
   }





   /** 
    * Method to set all column values of this instance.
    * in a given PreparedStatement object.
    * @param pst allready prepared PreparedStatement object with all columns.
    */
   public void fillUpdateOrInsert(PreparedStatement pst)
    throws SQLException{
      pst.setBigDecimal(1, this.tnumber);
      pst.setBlob(2, this.tblob);
      pst.setString(3, this.tchar);
      pst.setTimestamp(4, this.tdate);
      pst.setBigDecimal(5, this.tfloat);
      pst.setString(6, this.tlong);
      pst.setClob(7, this.tclob);
      pst.setString(8, this.tmlslabeljkjk);
      pst.setBigDecimal(9, this.uuuu);
   }


   /** Getter for Column 'TNUMBER'.
     * @return Column Value (BigDecimal)             */
   public BigDecimal getTnumber(){
      return this.tnumber;
   }

   /** Setter for Column 'TNUMBER'.
     * @param value new Column Value (NUMBER)             */
   public void setTnumber(BigDecimal value){
      this.tnumber = value;
   }

   /** Getter for Column 'TBLOB'.
     * @return Column Value (Blob)             */
   public Blob getTblob(){
      return this.tblob;
   }

   /** Setter for Column 'TBLOB'.
     * @param value new Column Value (BLOB)             */
   public void setTblob(Blob value){
      this.tblob = value;
   }

   /** Getter for Column 'TCHAR'.
     * @return Column Value (String)             */
   public String getTchar(){
      return this.tchar;
   }

   /** Setter for Column 'TCHAR'.
     * @param value new Column Value (CHAR)             */
   public void setTchar(String value){
      this.tchar = value;
   }

   /** Getter for Column 'TDATE'.
     * @return Column Value (Timestamp)             */
   public Timestamp getTdate(){
      return this.tdate;
   }

   /** Setter for Column 'TDATE'.
     * @param value new Column Value (DATE)             */
   public void setTdate(Timestamp value){
      this.tdate = value;
   }

   /** Getter for Column 'TFLOAT'.
     * @return Column Value (BigDecimal)             */
   public BigDecimal getTfloat(){
      return this.tfloat;
   }

   /** Setter for Column 'TFLOAT'.
     * @param value new Column Value (NUMBER)             */
   public void setTfloat(BigDecimal value){
      this.tfloat = value;
   }

   /** Getter for Column 'TLONG'.
     * @return Column Value (String)             */
   public String getTlong(){
      return this.tlong;
   }

   /** Setter for Column 'TLONG'.
     * @param value new Column Value (LONG)             */
   public void setTlong(String value){
      this.tlong = value;
   }

   /** Getter for Column 'TCLOB'.
     * @return Column Value (Clob)             */
   public Clob getTclob(){
      return this.tclob;
   }

   /** Setter for Column 'TCLOB'.
     * @param value new Column Value (CLOB)             */
   public void setTclob(Clob value){
      this.tclob = value;
   }

   /** Getter for Column 'TMLSLABELJKJK'.
     * @return Column Value (String)             */
   public String getTmlslabeljkjk(){
      return this.tmlslabeljkjk;
   }

   /** Setter for Column 'TMLSLABELJKJK'.
     * @param value new Column Value (VARCHAR2)             */
   public void setTmlslabeljkjk(String value){
      this.tmlslabeljkjk = value;
   }

   /** Getter for Column 'UUUU'.
     * @return Column Value (BigDecimal)             */
   public BigDecimal getUuuu(){
      return this.uuuu;
   }

   /** Setter for Column 'UUUU'.
     * @param value new Column Value (NUMBER)             */
   public void setUuuu(BigDecimal value){
      this.uuuu = value;
   }

   public  String toString(){

      StringBuffer buffer = new StringBuffer();

      buffer.append("Typetest: ");

      buffer.append("[TNUMBER=" + this.tnumber+"]");
      buffer.append("[TBLOB=" + this.tblob+"]");
      buffer.append("[TCHAR=" + this.tchar+"]");
      buffer.append("[TDATE=" + this.tdate+"]");
      buffer.append("[TFLOAT=" + this.tfloat+"]");
      buffer.append("[TLONG=" + this.tlong+"]");
      buffer.append("[TCLOB=" + this.tclob+"]");
      buffer.append("[TMLSLABELJKJK=" + this.tmlslabeljkjk+"]");
      buffer.append("[UUUU=" + this.uuuu+"]");
      return buffer.toString();

   }


}//end class: Typetest