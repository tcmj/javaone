package generated;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Time;
import java.sql.Date;
import java.math.BigDecimal;


/** 
 *  Java representation of table 'ADSEN'
 *  DatabaseVendor:  Microsoft SQL Server
 *  DatabaseVersion: 08.00.2039
 *  JDBC Drivername: jTDS Type 4 JDBC Driver for MS SQL Server and Sybase
 *  JDBC URL: jdbc:jtds:sqlserver://TDEUT:1433/tcmjdb;instance=TCMJMSDE
 *  JDBC User: sa
 */
public class Adsen {

   /** Column 'NAME' VARCHAR(44) NOT NULL   */
   private String name;

   /** Column 'VNAME' VARCHAR(55)    */
   private String vname;

   /** Column 'PLZ' SMALLINT(5)    */
   private short plz;

   /** Column 'ORT' TEXT(2147483647)    */
   private String ort;

   /** Column 'NOTIZEN' VARBINARY(10)    */
   private byte[] notizen;

   /** Column 'GEBDAT' DATETIME   */
   private Timestamp gebdat;

   /** Column 'MYTIMESTAMP' TIMESTAMP(8)    */
   private byte[] mytimestamp;

   /** Column 'MYBINARY' BINARY(10)    */
   private byte[] mybinary;

   /** Column 'MYIMAGE' IMAGE(2147483647)    */
   private byte[] myimage;

   /** Column 'MYDECIMA' DECIMAL(18)    */
   private BigDecimal mydecima;

   /** Column 'BITTE' BIT(1)    */
   private boolean bitte;

   /** Column 'CHAIR' CHAR(10)    */
   private String chair;



   /** 
    * Empty Constructor of Class 'Adsen'.
    * Formally used to insert a completly new record into the table.
    */
   public Adsen(){
      //Konstruktor Code
      //TODO initialization of Adsen (default settings) 
   }


   /** 
    * ResultSet Constructor of Class 'Adsen'.
    * Formally used to read and save one record from the table.
    */
   public Adsen(ResultSet rs) throws SQLException{
      this.name	 = rs.getString("name");
      this.vname	 = rs.getString("vname");
      this.plz	 = rs.getShort("plz");
      this.ort	 = rs.getString("ort");
      this.notizen	 = rs.getBytes("notizen");
      this.gebdat	 = rs.getTimestamp("gebdat");
      this.mytimestamp	 = rs.getBytes("mytimestamp");
      this.mybinary	 = rs.getBytes("mybinary");
      this.myimage	 = rs.getBytes("myimage");
      this.mydecima	 = rs.getBigDecimal("mydecima");
      this.bitte	 = rs.getBoolean("bitte");
      this.chair	 = rs.getString("chair");
   }




   /** 
    * Get the SQL Statement to select all Columns from Table 'ADSEN'.
    * NAME, VNAME, PLZ, 
    * ORT, NOTIZEN, GEBDAT, 
    * MYTIMESTAMP, MYBINARY, MYIMAGE, 
    * MYDECIMA, BITTE, CHAIR
    * @return SQL Statement to select 12 Columns!
    */
   public static final String getSQLSelect(){
      return "SELECT "+
         "NAME, VNAME, PLZ, "+
         "ORT, NOTIZEN, GEBDAT, "+
         "MYTIMESTAMP, MYBINARY, MYIMAGE, "+
         "MYDECIMA, BITTE, CHAIR "+
         "FROM ADSEN";

   }


   /** 
    * Get the Prepared SQL Statement to insert into Table 'ADSEN'.
    * NAME, VNAME, PLZ, 
    * ORT, NOTIZEN, GEBDAT, 
    * MYTIMESTAMP, MYBINARY, MYIMAGE, 
    * MYDECIMA, BITTE, CHAIR
    * @return SQL Statement to insert 12 Columns!
    */
   public static final String getSQLPreparedInsert(){
      return "INSERT INTO ADSEN("+
         "NAME, VNAME, PLZ, "+
         "ORT, NOTIZEN, GEBDAT, "+
         "MYTIMESTAMP, MYBINARY, MYIMAGE, "+
         "MYDECIMA, BITTE, CHAIR "+
         ") VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+
         "?, ? )";


   }


   /** 
    * Get the Prepared SQL Statement to update Table 'ADSEN'.
    * NAME, VNAME, PLZ, 
    * ORT, NOTIZEN, GEBDAT, 
    * MYTIMESTAMP, MYBINARY, MYIMAGE, 
    * MYDECIMA, BITTE, CHAIR
    * Use {@link fillUpdateOrInsert(PreparedStatement)} to fill your Prepared Statement!
    * @return SQL Statement to update 12 Columns!
    */
   public static final String getSQLPreparedUpdate(){
      return "UPDATE ADSEN( "+
         " SET NAME = ?, "+
         " VNAME = ?, "+
         " PLZ = ?, "+
         " ORT = ?, "+
         " NOTIZEN = ?, "+
         " GEBDAT = ?, "+
         " MYTIMESTAMP = ?, "+
         " MYBINARY = ?, "+
         " MYIMAGE = ?, "+
         " MYDECIMA = ?, "+
         " BITTE = ?, "+
         " CHAIR = ? )";
   }





   public void fillUpdateOrInsert(PreparedStatement pst)
    throws SQLException{
      pst.setString(1, this.name);
      pst.setString(2, this.vname);
      pst.setShort(3, this.plz);
      pst.setString(4, this.ort);
      pst.setBytes(5, this.notizen);
      pst.setTimestamp(6, this.gebdat);
      pst.setBytes(7, this.mytimestamp);
      pst.setBytes(8, this.mybinary);
      pst.setBytes(9, this.myimage);
      pst.setBigDecimal(10, this.mydecima);
      pst.setBoolean(11, this.bitte);
      pst.setString(12, this.chair);
   }


   /** Getter for Column 'NAME'.
     * @return Column Value (String)             */
   public String getName(){
      return this.name;
   }

   public void setName(String name){
      this.name = name;
   }

   /** Getter for Column 'VNAME'.
     * @return Column Value (String)             */
   public String getVname(){
      return this.vname;
   }

   public void setVname(String vname){
      this.vname = vname;
   }

   /** Getter for Column 'PLZ'.
     * @return Column Value (short)             */
   public short getPlz(){
      return this.plz;
   }

   public void setPlz(short plz){
      this.plz = plz;
   }

   /** Getter for Column 'ORT'.
     * @return Column Value (String)             */
   public String getOrt(){
      return this.ort;
   }

   public void setOrt(String ort){
      this.ort = ort;
   }

   /** Getter for Column 'NOTIZEN'.
     * @return Column Value (byte[])             */
   public byte[] getNotizen(){
      return this.notizen;
   }

   public void setNotizen(byte[] notizen){
      this.notizen = notizen;
   }

   /** Getter for Column 'GEBDAT'.
     * @return Column Value (Timestamp)             */
   public Timestamp getGebdat(){
      return this.gebdat;
   }

   public void setGebdat(Timestamp gebdat){
      this.gebdat = gebdat;
   }

   /** Getter for Column 'MYTIMESTAMP'.
     * @return Column Value (byte[])             */
   public byte[] getMytimestamp(){
      return this.mytimestamp;
   }

   public void setMytimestamp(byte[] mytimestamp){
      this.mytimestamp = mytimestamp;
   }

   /** Getter for Column 'MYBINARY'.
     * @return Column Value (byte[])             */
   public byte[] getMybinary(){
      return this.mybinary;
   }

   public void setMybinary(byte[] mybinary){
      this.mybinary = mybinary;
   }

   /** Getter for Column 'MYIMAGE'.
     * @return Column Value (byte[])             */
   public byte[] getMyimage(){
      return this.myimage;
   }

   public void setMyimage(byte[] myimage){
      this.myimage = myimage;
   }

   /** Getter for Column 'MYDECIMA'.
     * @return Column Value (BigDecimal)             */
   public BigDecimal getMydecima(){
      return this.mydecima;
   }

   public void setMydecima(BigDecimal mydecima){
      this.mydecima = mydecima;
   }

   /** Getter for Column 'BITTE'.
     * @return Column Value (boolean)             */
   public boolean getBitte(){
      return this.bitte;
   }

   public void setBitte(boolean bitte){
      this.bitte = bitte;
   }

   /** Getter for Column 'CHAIR'.
     * @return Column Value (String)             */
   public String getChair(){
      return this.chair;
   }

   public void setChair(String chair){
      this.chair = chair;
   }

}//end class: Adsen
