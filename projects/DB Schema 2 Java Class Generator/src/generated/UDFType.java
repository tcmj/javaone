package generated;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


class UDFType {



   public UDFType(){
      //Konstruktor Code
   }


   public UDFType(ResultSet rs) throws SQLException{
   }




   /** 
    * Get the SQL Statement to select all Columns from Table 'UDFTYPE'.
    * @return SQL Statement to select 0 Columns!
    */
   public static final String getSQLSelect(){
      return "SELECT "+
 
         "FROM UDFTYPE";

   }


   /** 
    * Get the Prepared SQL Statement to insert into Table 'UDFTYPE'.
    * @return SQL Statement to insert 0 Columns!
    */
   public static final String getSQLPreparedInsert(){
      return "INSERT INTO UDFTYPE("+
 
         ") VALUES (  )";


   }


   /** 
    * Get the Prepared SQL Statement to update Table 'UDFTYPE'.
    * @return SQL Statement to update 0 Columns!
    */
   public static final String getSQLPreparedUpdate(){
      return "UPDATE UDFTYPE(  )";
   }





   public void fillUpdateOrInsert(PreparedStatement pst)
    throws SQLException{
   }


}//end class