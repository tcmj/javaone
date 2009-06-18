package generated;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Time;
import java.sql.Date;
import java.math.BigDecimal;


/** 
 *  Java representation of table 'TASK'
 *  DatabaseVendor:  Oracle
 *  DatabaseVersion: Oracle Database 10g Express Edition Release 10.2.0.1.0 - Beta
 *  JDBC Drivername: Oracle JDBC driver
 *  JDBC URL: jdbc:oracle:thin:@tdeut:1521:xe
 *  JDBC User: PRIVUSER4
 */
public class Task {

   /** Column 'TASK_ID' NUMBER(10) NOT NULL   */
   private BigDecimal task_id;

   /** Column 'PROJ_ID' NUMBER(10) NOT NULL   */
   private BigDecimal proj_id;

   /** Column 'WBS_ID' NUMBER(10) NOT NULL   */
   private BigDecimal wbs_id;

   /** Column 'CLNDR_ID' NUMBER(10) NOT NULL   */
   private BigDecimal clndr_id;

   /** Column 'EST_WT' NUMBER(10,2) NOT NULL   */
   private BigDecimal est_wt;

   /** Column 'PHYS_COMPLETE_PCT' NUMBER(10,2) NOT NULL  DEFAULT 0  */
   private BigDecimal phys_complete_pct;

   /** Column 'REV_FDBK_FLAG' VARCHAR2(1) NOT NULL  DEFAULT 'N'  */
   private String rev_fdbk_flag;

   /** Column 'LOCK_PLAN_FLAG' VARCHAR2(1) NOT NULL  DEFAULT 'N'  */
   private String lock_plan_flag;

   /** Column 'AUTO_COMPUTE_ACT_FLAG' VARCHAR2(1) NOT NULL  DEFAULT 'N'  */
   private String auto_compute_act_flag;

   /** Column 'COMPLETE_PCT_TYPE' VARCHAR2(10) NOT NULL  DEFAULT 'CP_Drtn'  */
   private String complete_pct_type;

   /** Column 'TASK_TYPE' VARCHAR2(10) NOT NULL   */
   private String task_type;

   /** Column 'DURATION_TYPE' VARCHAR2(12) NOT NULL   */
   private String duration_type;

   /** Column 'REVIEW_TYPE' VARCHAR2(12) NOT NULL   */
   private String review_type;

   /** Column 'STATUS_CODE' VARCHAR2(12) NOT NULL   */
   private String status_code;

   /** Column 'TASK_CODE' VARCHAR2(40) NOT NULL   */
   private String task_code;

   /** Column 'TASK_NAME' VARCHAR2(120) NOT NULL   */
   private String task_name;

   /** Column 'RSRC_ID' NUMBER(10)    */
   private BigDecimal rsrc_id;

   /** Column 'TOTAL_FLOAT_HR_CNT' NUMBER(14,6)    */
   private BigDecimal total_float_hr_cnt;

   /** Column 'FREE_FLOAT_HR_CNT' NUMBER(14,6)    */
   private BigDecimal free_float_hr_cnt;

   /** Column 'REMAIN_DRTN_HR_CNT' NUMBER(14,6)    */
   private BigDecimal remain_drtn_hr_cnt;

   /** Column 'ACT_WORK_QTY' NUMBER(14,6)    */
   private BigDecimal act_work_qty;

   /** Column 'REMAIN_WORK_QTY' NUMBER(14,6)    */
   private BigDecimal remain_work_qty;

   /** Column 'TARGET_WORK_QTY' NUMBER(14,6)    */
   private BigDecimal target_work_qty;

   /** Column 'TARGET_DRTN_HR_CNT' NUMBER(14,6)    */
   private BigDecimal target_drtn_hr_cnt;

   /** Column 'TARGET_EQUIP_QTY' NUMBER(14,6)    */
   private BigDecimal target_equip_qty;

   /** Column 'ACT_EQUIP_QTY' NUMBER(14,6)    */
   private BigDecimal act_equip_qty;

   /** Column 'REMAIN_EQUIP_QTY' NUMBER(14,6)    */
   private BigDecimal remain_equip_qty;

   /** Column 'CSTR_DATE' DATE   */
   private Timestamp cstr_date;

   /** Column 'ACT_START_DATE' DATE   */
   private Timestamp act_start_date;

   /** Column 'ACT_END_DATE' DATE   */
   private Timestamp act_end_date;

   /** Column 'LATE_START_DATE' DATE   */
   private Timestamp late_start_date;

   /** Column 'LATE_END_DATE' DATE   */
   private Timestamp late_end_date;

   /** Column 'EXPECT_END_DATE' DATE   */
   private Timestamp expect_end_date;

   /** Column 'EARLY_START_DATE' DATE   */
   private Timestamp early_start_date;

   /** Column 'EARLY_END_DATE' DATE   */
   private Timestamp early_end_date;

   /** Column 'RESTART_DATE' DATE   */
   private Timestamp restart_date;

   /** Column 'REEND_DATE' DATE   */
   private Timestamp reend_date;

   /** Column 'TARGET_START_DATE' DATE   */
   private Timestamp target_start_date;

   /** Column 'TARGET_END_DATE' DATE   */
   private Timestamp target_end_date;

   /** Column 'REVIEW_END_DATE' DATE   */
   private Timestamp review_end_date;

   /** Column 'REM_LATE_START_DATE' DATE   */
   private Timestamp rem_late_start_date;

   /** Column 'REM_LATE_END_DATE' DATE   */
   private Timestamp rem_late_end_date;

   /** Column 'CSTR_TYPE' VARCHAR2(12)    */
   private String cstr_type;

   /** Column 'PRIORITY_TYPE' VARCHAR2(12)    */
   private String priority_type;

   /** Column 'GUID' VARCHAR2(22)    */
   private String guid;

   /** Column 'TMPL_GUID' VARCHAR2(22)    */
   private String tmpl_guid;

   /** Column 'CSTR_DATE2' DATE   */
   private Timestamp cstr_date2;

   /** Column 'CSTR_TYPE2' VARCHAR2(12)    */
   private String cstr_type2;

   /** Column 'ACT_THIS_PER_WORK_QTY' NUMBER(14,6)    */
   private BigDecimal act_this_per_work_qty;

   /** Column 'ACT_THIS_PER_EQUIP_QTY' NUMBER(14,6)    */
   private BigDecimal act_this_per_equip_qty;

   /** Column 'DRIVING_PATH_FLAG' VARCHAR2(1) NOT NULL  DEFAULT 'N'  */
   private String driving_path_flag;



   /** 
    * Empty Constructor of Class 'Task'.
    * Formally used to insert a completly new record into the table.
    */
   public Task(){
      //Konstruktor Code
      //TODO initialization of Task (default settings) 
   }


   /** 
    * ResultSet Constructor of Class 'Task'.
    * Formally used to read and store one record from the table.
    */
   public Task(ResultSet rs) throws SQLException{
      this.task_id	 = rs.getBigDecimal("TASK_ID");
      this.proj_id	 = rs.getBigDecimal("PROJ_ID");
      this.wbs_id	 = rs.getBigDecimal("WBS_ID");
      this.clndr_id	 = rs.getBigDecimal("CLNDR_ID");
      this.est_wt	 = rs.getBigDecimal("EST_WT");
      this.phys_complete_pct	 = rs.getBigDecimal("PHYS_COMPLETE_PCT");
      this.rev_fdbk_flag	 = rs.getString("REV_FDBK_FLAG");
      this.lock_plan_flag	 = rs.getString("LOCK_PLAN_FLAG");
      this.auto_compute_act_flag	 = rs.getString("AUTO_COMPUTE_ACT_FLAG");
      this.complete_pct_type	 = rs.getString("COMPLETE_PCT_TYPE");
      this.task_type	 = rs.getString("TASK_TYPE");
      this.duration_type	 = rs.getString("DURATION_TYPE");
      this.review_type	 = rs.getString("REVIEW_TYPE");
      this.status_code	 = rs.getString("STATUS_CODE");
      this.task_code	 = rs.getString("TASK_CODE");
      this.task_name	 = rs.getString("TASK_NAME");
      this.rsrc_id	 = rs.getBigDecimal("RSRC_ID");
      this.total_float_hr_cnt	 = rs.getBigDecimal("TOTAL_FLOAT_HR_CNT");
      this.free_float_hr_cnt	 = rs.getBigDecimal("FREE_FLOAT_HR_CNT");
      this.remain_drtn_hr_cnt	 = rs.getBigDecimal("REMAIN_DRTN_HR_CNT");
      this.act_work_qty	 = rs.getBigDecimal("ACT_WORK_QTY");
      this.remain_work_qty	 = rs.getBigDecimal("REMAIN_WORK_QTY");
      this.target_work_qty	 = rs.getBigDecimal("TARGET_WORK_QTY");
      this.target_drtn_hr_cnt	 = rs.getBigDecimal("TARGET_DRTN_HR_CNT");
      this.target_equip_qty	 = rs.getBigDecimal("TARGET_EQUIP_QTY");
      this.act_equip_qty	 = rs.getBigDecimal("ACT_EQUIP_QTY");
      this.remain_equip_qty	 = rs.getBigDecimal("REMAIN_EQUIP_QTY");
      this.cstr_date	 = rs.getTimestamp("CSTR_DATE");
      this.act_start_date	 = rs.getTimestamp("ACT_START_DATE");
      this.act_end_date	 = rs.getTimestamp("ACT_END_DATE");
      this.late_start_date	 = rs.getTimestamp("LATE_START_DATE");
      this.late_end_date	 = rs.getTimestamp("LATE_END_DATE");
      this.expect_end_date	 = rs.getTimestamp("EXPECT_END_DATE");
      this.early_start_date	 = rs.getTimestamp("EARLY_START_DATE");
      this.early_end_date	 = rs.getTimestamp("EARLY_END_DATE");
      this.restart_date	 = rs.getTimestamp("RESTART_DATE");
      this.reend_date	 = rs.getTimestamp("REEND_DATE");
      this.target_start_date	 = rs.getTimestamp("TARGET_START_DATE");
      this.target_end_date	 = rs.getTimestamp("TARGET_END_DATE");
      this.review_end_date	 = rs.getTimestamp("REVIEW_END_DATE");
      this.rem_late_start_date	 = rs.getTimestamp("REM_LATE_START_DATE");
      this.rem_late_end_date	 = rs.getTimestamp("REM_LATE_END_DATE");
      this.cstr_type	 = rs.getString("CSTR_TYPE");
      this.priority_type	 = rs.getString("PRIORITY_TYPE");
      this.guid	 = rs.getString("GUID");
      this.tmpl_guid	 = rs.getString("TMPL_GUID");
      this.cstr_date2	 = rs.getTimestamp("CSTR_DATE2");
      this.cstr_type2	 = rs.getString("CSTR_TYPE2");
      this.act_this_per_work_qty	 = rs.getBigDecimal("ACT_THIS_PER_WORK_QTY");
      this.act_this_per_equip_qty	 = rs.getBigDecimal("ACT_THIS_PER_EQUIP_QTY");
      this.driving_path_flag	 = rs.getString("DRIVING_PATH_FLAG");
   }




   /** 
    * Get the SQL Statement to select all Columns from Table 'TASK'.
    * TASK_ID, PROJ_ID, WBS_ID, 
    * CLNDR_ID, EST_WT, PHYS_COMPLETE_PCT, 
    * REV_FDBK_FLAG, LOCK_PLAN_FLAG, AUTO_COMPUTE_ACT_FLAG, 
    * COMPLETE_PCT_TYPE, TASK_TYPE, DURATION_TYPE, 
    * REVIEW_TYPE, STATUS_CODE, TASK_CODE, 
    * TASK_NAME, RSRC_ID, TOTAL_FLOAT_HR_CNT, 
    * FREE_FLOAT_HR_CNT, REMAIN_DRTN_HR_CNT, ACT_WORK_QTY, 
    * REMAIN_WORK_QTY, TARGET_WORK_QTY, TARGET_DRTN_HR_CNT, 
    * TARGET_EQUIP_QTY, ACT_EQUIP_QTY, REMAIN_EQUIP_QTY, 
    * CSTR_DATE, ACT_START_DATE, ACT_END_DATE, 
    * LATE_START_DATE, LATE_END_DATE, EXPECT_END_DATE, 
    * EARLY_START_DATE, EARLY_END_DATE, RESTART_DATE, 
    * REEND_DATE, TARGET_START_DATE, TARGET_END_DATE, 
    * REVIEW_END_DATE, REM_LATE_START_DATE, REM_LATE_END_DATE, 
    * CSTR_TYPE, PRIORITY_TYPE, GUID, 
    * TMPL_GUID, CSTR_DATE2, CSTR_TYPE2, 
    * ACT_THIS_PER_WORK_QTY, ACT_THIS_PER_EQUIP_QTY, DRIVING_PATH_FLAG
    * @return SQL Statement to select 51 Columns!
    */
   public static final String getSQLSelect(){
      return "SELECT "+
         "TASK_ID, PROJ_ID, WBS_ID, "+
         "CLNDR_ID, EST_WT, PHYS_COMPLETE_PCT, "+
         "REV_FDBK_FLAG, LOCK_PLAN_FLAG, AUTO_COMPUTE_ACT_FLAG, "+
         "COMPLETE_PCT_TYPE, TASK_TYPE, DURATION_TYPE, "+
         "REVIEW_TYPE, STATUS_CODE, TASK_CODE, "+
         "TASK_NAME, RSRC_ID, TOTAL_FLOAT_HR_CNT, "+
         "FREE_FLOAT_HR_CNT, REMAIN_DRTN_HR_CNT, ACT_WORK_QTY, "+
         "REMAIN_WORK_QTY, TARGET_WORK_QTY, TARGET_DRTN_HR_CNT, "+
         "TARGET_EQUIP_QTY, ACT_EQUIP_QTY, REMAIN_EQUIP_QTY, "+
         "CSTR_DATE, ACT_START_DATE, ACT_END_DATE, "+
         "LATE_START_DATE, LATE_END_DATE, EXPECT_END_DATE, "+
         "EARLY_START_DATE, EARLY_END_DATE, RESTART_DATE, "+
         "REEND_DATE, TARGET_START_DATE, TARGET_END_DATE, "+
         "REVIEW_END_DATE, REM_LATE_START_DATE, REM_LATE_END_DATE, "+
         "CSTR_TYPE, PRIORITY_TYPE, GUID, "+
         "TMPL_GUID, CSTR_DATE2, CSTR_TYPE2, "+
         "ACT_THIS_PER_WORK_QTY, ACT_THIS_PER_EQUIP_QTY, DRIVING_PATH_FLAG "+
         "FROM TASK";

   }


   /** 
    * Get the Prepared SQL Statement to insert into Table 'TASK'.
    * TASK_ID, PROJ_ID, WBS_ID, 
    * CLNDR_ID, EST_WT, PHYS_COMPLETE_PCT, 
    * REV_FDBK_FLAG, LOCK_PLAN_FLAG, AUTO_COMPUTE_ACT_FLAG, 
    * COMPLETE_PCT_TYPE, TASK_TYPE, DURATION_TYPE, 
    * REVIEW_TYPE, STATUS_CODE, TASK_CODE, 
    * TASK_NAME, RSRC_ID, TOTAL_FLOAT_HR_CNT, 
    * FREE_FLOAT_HR_CNT, REMAIN_DRTN_HR_CNT, ACT_WORK_QTY, 
    * REMAIN_WORK_QTY, TARGET_WORK_QTY, TARGET_DRTN_HR_CNT, 
    * TARGET_EQUIP_QTY, ACT_EQUIP_QTY, REMAIN_EQUIP_QTY, 
    * CSTR_DATE, ACT_START_DATE, ACT_END_DATE, 
    * LATE_START_DATE, LATE_END_DATE, EXPECT_END_DATE, 
    * EARLY_START_DATE, EARLY_END_DATE, RESTART_DATE, 
    * REEND_DATE, TARGET_START_DATE, TARGET_END_DATE, 
    * REVIEW_END_DATE, REM_LATE_START_DATE, REM_LATE_END_DATE, 
    * CSTR_TYPE, PRIORITY_TYPE, GUID, 
    * TMPL_GUID, CSTR_DATE2, CSTR_TYPE2, 
    * ACT_THIS_PER_WORK_QTY, ACT_THIS_PER_EQUIP_QTY, DRIVING_PATH_FLAG
    * @return SQL Statement to insert 51 Columns!
    */
   public static final String getSQLPreparedInsert(){
      return "INSERT INTO TASK("+
         "TASK_ID, PROJ_ID, WBS_ID, "+
         "CLNDR_ID, EST_WT, PHYS_COMPLETE_PCT, "+
         "REV_FDBK_FLAG, LOCK_PLAN_FLAG, AUTO_COMPUTE_ACT_FLAG, "+
         "COMPLETE_PCT_TYPE, TASK_TYPE, DURATION_TYPE, "+
         "REVIEW_TYPE, STATUS_CODE, TASK_CODE, "+
         "TASK_NAME, RSRC_ID, TOTAL_FLOAT_HR_CNT, "+
         "FREE_FLOAT_HR_CNT, REMAIN_DRTN_HR_CNT, ACT_WORK_QTY, "+
         "REMAIN_WORK_QTY, TARGET_WORK_QTY, TARGET_DRTN_HR_CNT, "+
         "TARGET_EQUIP_QTY, ACT_EQUIP_QTY, REMAIN_EQUIP_QTY, "+
         "CSTR_DATE, ACT_START_DATE, ACT_END_DATE, "+
         "LATE_START_DATE, LATE_END_DATE, EXPECT_END_DATE, "+
         "EARLY_START_DATE, EARLY_END_DATE, RESTART_DATE, "+
         "REEND_DATE, TARGET_START_DATE, TARGET_END_DATE, "+
         "REVIEW_END_DATE, REM_LATE_START_DATE, REM_LATE_END_DATE, "+
         "CSTR_TYPE, PRIORITY_TYPE, GUID, "+
         "TMPL_GUID, CSTR_DATE2, CSTR_TYPE2, "+
         "ACT_THIS_PER_WORK_QTY, ACT_THIS_PER_EQUIP_QTY, DRIVING_PATH_FLAG "+
         ") VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+
         "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+
         "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+
         "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+
         "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+
         "? )";


   }


   /** 
    * Get the Prepared SQL Statement to update Table 'TASK'.
    * TASK_ID, PROJ_ID, WBS_ID, 
    * CLNDR_ID, EST_WT, PHYS_COMPLETE_PCT, 
    * REV_FDBK_FLAG, LOCK_PLAN_FLAG, AUTO_COMPUTE_ACT_FLAG, 
    * COMPLETE_PCT_TYPE, TASK_TYPE, DURATION_TYPE, 
    * REVIEW_TYPE, STATUS_CODE, TASK_CODE, 
    * TASK_NAME, RSRC_ID, TOTAL_FLOAT_HR_CNT, 
    * FREE_FLOAT_HR_CNT, REMAIN_DRTN_HR_CNT, ACT_WORK_QTY, 
    * REMAIN_WORK_QTY, TARGET_WORK_QTY, TARGET_DRTN_HR_CNT, 
    * TARGET_EQUIP_QTY, ACT_EQUIP_QTY, REMAIN_EQUIP_QTY, 
    * CSTR_DATE, ACT_START_DATE, ACT_END_DATE, 
    * LATE_START_DATE, LATE_END_DATE, EXPECT_END_DATE, 
    * EARLY_START_DATE, EARLY_END_DATE, RESTART_DATE, 
    * REEND_DATE, TARGET_START_DATE, TARGET_END_DATE, 
    * REVIEW_END_DATE, REM_LATE_START_DATE, REM_LATE_END_DATE, 
    * CSTR_TYPE, PRIORITY_TYPE, GUID, 
    * TMPL_GUID, CSTR_DATE2, CSTR_TYPE2, 
    * ACT_THIS_PER_WORK_QTY, ACT_THIS_PER_EQUIP_QTY, DRIVING_PATH_FLAG
    * Use {@link fillUpdateOrInsert(PreparedStatement)} to fill your Prepared Statement!
    * @return SQL Statement to update 51 Columns!
    */
   public static final String getSQLPreparedUpdate(){
      return "UPDATE TASK "+
         " SET TASK_ID = ?, "+
         " PROJ_ID = ?, "+
         " WBS_ID = ?, "+
         " CLNDR_ID = ?, "+
         " EST_WT = ?, "+
         " PHYS_COMPLETE_PCT = ?, "+
         " REV_FDBK_FLAG = ?, "+
         " LOCK_PLAN_FLAG = ?, "+
         " AUTO_COMPUTE_ACT_FLAG = ?, "+
         " COMPLETE_PCT_TYPE = ?, "+
         " TASK_TYPE = ?, "+
         " DURATION_TYPE = ?, "+
         " REVIEW_TYPE = ?, "+
         " STATUS_CODE = ?, "+
         " TASK_CODE = ?, "+
         " TASK_NAME = ?, "+
         " RSRC_ID = ?, "+
         " TOTAL_FLOAT_HR_CNT = ?, "+
         " FREE_FLOAT_HR_CNT = ?, "+
         " REMAIN_DRTN_HR_CNT = ?, "+
         " ACT_WORK_QTY = ?, "+
         " REMAIN_WORK_QTY = ?, "+
         " TARGET_WORK_QTY = ?, "+
         " TARGET_DRTN_HR_CNT = ?, "+
         " TARGET_EQUIP_QTY = ?, "+
         " ACT_EQUIP_QTY = ?, "+
         " REMAIN_EQUIP_QTY = ?, "+
         " CSTR_DATE = ?, "+
         " ACT_START_DATE = ?, "+
         " ACT_END_DATE = ?, "+
         " LATE_START_DATE = ?, "+
         " LATE_END_DATE = ?, "+
         " EXPECT_END_DATE = ?, "+
         " EARLY_START_DATE = ?, "+
         " EARLY_END_DATE = ?, "+
         " RESTART_DATE = ?, "+
         " REEND_DATE = ?, "+
         " TARGET_START_DATE = ?, "+
         " TARGET_END_DATE = ?, "+
         " REVIEW_END_DATE = ?, "+
         " REM_LATE_START_DATE = ?, "+
         " REM_LATE_END_DATE = ?, "+
         " CSTR_TYPE = ?, "+
         " PRIORITY_TYPE = ?, "+
         " GUID = ?, "+
         " TMPL_GUID = ?, "+
         " CSTR_DATE2 = ?, "+
         " CSTR_TYPE2 = ?, "+
         " ACT_THIS_PER_WORK_QTY = ?, "+
         " ACT_THIS_PER_EQUIP_QTY = ?, "+
         " DRIVING_PATH_FLAG = ? ";
   }





   /** 
    * Method to set all column values of this instance.
    * in a given PreparedStatement object.
    * @param pst allready prepared PreparedStatement object with all columns.
    */
   public void fillUpdateOrInsert(PreparedStatement pst)
    throws SQLException{
      pst.setBigDecimal(1, this.task_id);
      pst.setBigDecimal(2, this.proj_id);
      pst.setBigDecimal(3, this.wbs_id);
      pst.setBigDecimal(4, this.clndr_id);
      pst.setBigDecimal(5, this.est_wt);
      pst.setBigDecimal(6, this.phys_complete_pct);
      pst.setString(7, this.rev_fdbk_flag);
      pst.setString(8, this.lock_plan_flag);
      pst.setString(9, this.auto_compute_act_flag);
      pst.setString(10, this.complete_pct_type);
      pst.setString(11, this.task_type);
      pst.setString(12, this.duration_type);
      pst.setString(13, this.review_type);
      pst.setString(14, this.status_code);
      pst.setString(15, this.task_code);
      pst.setString(16, this.task_name);
      pst.setBigDecimal(17, this.rsrc_id);
      pst.setBigDecimal(18, this.total_float_hr_cnt);
      pst.setBigDecimal(19, this.free_float_hr_cnt);
      pst.setBigDecimal(20, this.remain_drtn_hr_cnt);
      pst.setBigDecimal(21, this.act_work_qty);
      pst.setBigDecimal(22, this.remain_work_qty);
      pst.setBigDecimal(23, this.target_work_qty);
      pst.setBigDecimal(24, this.target_drtn_hr_cnt);
      pst.setBigDecimal(25, this.target_equip_qty);
      pst.setBigDecimal(26, this.act_equip_qty);
      pst.setBigDecimal(27, this.remain_equip_qty);
      pst.setTimestamp(28, this.cstr_date);
      pst.setTimestamp(29, this.act_start_date);
      pst.setTimestamp(30, this.act_end_date);
      pst.setTimestamp(31, this.late_start_date);
      pst.setTimestamp(32, this.late_end_date);
      pst.setTimestamp(33, this.expect_end_date);
      pst.setTimestamp(34, this.early_start_date);
      pst.setTimestamp(35, this.early_end_date);
      pst.setTimestamp(36, this.restart_date);
      pst.setTimestamp(37, this.reend_date);
      pst.setTimestamp(38, this.target_start_date);
      pst.setTimestamp(39, this.target_end_date);
      pst.setTimestamp(40, this.review_end_date);
      pst.setTimestamp(41, this.rem_late_start_date);
      pst.setTimestamp(42, this.rem_late_end_date);
      pst.setString(43, this.cstr_type);
      pst.setString(44, this.priority_type);
      pst.setString(45, this.guid);
      pst.setString(46, this.tmpl_guid);
      pst.setTimestamp(47, this.cstr_date2);
      pst.setString(48, this.cstr_type2);
      pst.setBigDecimal(49, this.act_this_per_work_qty);
      pst.setBigDecimal(50, this.act_this_per_equip_qty);
      pst.setString(51, this.driving_path_flag);
   }


   /** Getter for Column 'TASK_ID'.
     * @return Column Value (BigDecimal)             */
   public BigDecimal getTask_id(){
      return this.task_id;
   }

   /** Setter for Column 'TASK_ID'.
     * @param value new Column Value (NUMBER)             */
   public void setTask_id(BigDecimal value){
      this.task_id = value;
   }

   /** Getter for Column 'PROJ_ID'.
     * @return Column Value (BigDecimal)             */
   public BigDecimal getProj_id(){
      return this.proj_id;
   }

   /** Setter for Column 'PROJ_ID'.
     * @param value new Column Value (NUMBER)             */
   public void setProj_id(BigDecimal value){
      this.proj_id = value;
   }

   /** Getter for Column 'WBS_ID'.
     * @return Column Value (BigDecimal)             */
   public BigDecimal getWbs_id(){
      return this.wbs_id;
   }

   /** Setter for Column 'WBS_ID'.
     * @param value new Column Value (NUMBER)             */
   public void setWbs_id(BigDecimal value){
      this.wbs_id = value;
   }

   /** Getter for Column 'CLNDR_ID'.
     * @return Column Value (BigDecimal)             */
   public BigDecimal getClndr_id(){
      return this.clndr_id;
   }

   /** Setter for Column 'CLNDR_ID'.
     * @param value new Column Value (NUMBER)             */
   public void setClndr_id(BigDecimal value){
      this.clndr_id = value;
   }

   /** Getter for Column 'EST_WT'.
     * @return Column Value (BigDecimal)             */
   public BigDecimal getEst_wt(){
      return this.est_wt;
   }

   /** Setter for Column 'EST_WT'.
     * @param value new Column Value (NUMBER)             */
   public void setEst_wt(BigDecimal value){
      this.est_wt = value;
   }

   /** Getter for Column 'PHYS_COMPLETE_PCT'.
     * @return Column Value (BigDecimal)             */
   public BigDecimal getPhys_complete_pct(){
      return this.phys_complete_pct;
   }

   /** Setter for Column 'PHYS_COMPLETE_PCT'.
     * @param value new Column Value (NUMBER)             */
   public void setPhys_complete_pct(BigDecimal value){
      this.phys_complete_pct = value;
   }

   /** Getter for Column 'REV_FDBK_FLAG'.
     * @return Column Value (String)             */
   public String getRev_fdbk_flag(){
      return this.rev_fdbk_flag;
   }

   /** Setter for Column 'REV_FDBK_FLAG'.
     * @param value new Column Value (VARCHAR2)             */
   public void setRev_fdbk_flag(String value){
      this.rev_fdbk_flag = value;
   }

   /** Getter for Column 'LOCK_PLAN_FLAG'.
     * @return Column Value (String)             */
   public String getLock_plan_flag(){
      return this.lock_plan_flag;
   }

   /** Setter for Column 'LOCK_PLAN_FLAG'.
     * @param value new Column Value (VARCHAR2)             */
   public void setLock_plan_flag(String value){
      this.lock_plan_flag = value;
   }

   /** Getter for Column 'AUTO_COMPUTE_ACT_FLAG'.
     * @return Column Value (String)             */
   public String getAuto_compute_act_flag(){
      return this.auto_compute_act_flag;
   }

   /** Setter for Column 'AUTO_COMPUTE_ACT_FLAG'.
     * @param value new Column Value (VARCHAR2)             */
   public void setAuto_compute_act_flag(String value){
      this.auto_compute_act_flag = value;
   }

   /** Getter for Column 'COMPLETE_PCT_TYPE'.
     * @return Column Value (String)             */
   public String getComplete_pct_type(){
      return this.complete_pct_type;
   }

   /** Setter for Column 'COMPLETE_PCT_TYPE'.
     * @param value new Column Value (VARCHAR2)             */
   public void setComplete_pct_type(String value){
      this.complete_pct_type = value;
   }

   /** Getter for Column 'TASK_TYPE'.
     * @return Column Value (String)             */
   public String getTask_type(){
      return this.task_type;
   }

   /** Setter for Column 'TASK_TYPE'.
     * @param value new Column Value (VARCHAR2)             */
   public void setTask_type(String value){
      this.task_type = value;
   }

   /** Getter for Column 'DURATION_TYPE'.
     * @return Column Value (String)             */
   public String getDuration_type(){
      return this.duration_type;
   }

   /** Setter for Column 'DURATION_TYPE'.
     * @param value new Column Value (VARCHAR2)             */
   public void setDuration_type(String value){
      this.duration_type = value;
   }

   /** Getter for Column 'REVIEW_TYPE'.
     * @return Column Value (String)             */
   public String getReview_type(){
      return this.review_type;
   }

   /** Setter for Column 'REVIEW_TYPE'.
     * @param value new Column Value (VARCHAR2)             */
   public void setReview_type(String value){
      this.review_type = value;
   }

   /** Getter for Column 'STATUS_CODE'.
     * @return Column Value (String)             */
   public String getStatus_code(){
      return this.status_code;
   }

   /** Setter for Column 'STATUS_CODE'.
     * @param value new Column Value (VARCHAR2)             */
   public void setStatus_code(String value){
      this.status_code = value;
   }

   /** Getter for Column 'TASK_CODE'.
     * @return Column Value (String)             */
   public String getTask_code(){
      return this.task_code;
   }

   /** Setter for Column 'TASK_CODE'.
     * @param value new Column Value (VARCHAR2)             */
   public void setTask_code(String value){
      this.task_code = value;
   }

   /** Getter for Column 'TASK_NAME'.
     * @return Column Value (String)             */
   public String getTask_name(){
      return this.task_name;
   }

   /** Setter for Column 'TASK_NAME'.
     * @param value new Column Value (VARCHAR2)             */
   public void setTask_name(String value){
      this.task_name = value;
   }

   /** Getter for Column 'RSRC_ID'.
     * @return Column Value (BigDecimal)             */
   public BigDecimal getRsrc_id(){
      return this.rsrc_id;
   }

   /** Setter for Column 'RSRC_ID'.
     * @param value new Column Value (NUMBER)             */
   public void setRsrc_id(BigDecimal value){
      this.rsrc_id = value;
   }

   /** Getter for Column 'TOTAL_FLOAT_HR_CNT'.
     * @return Column Value (BigDecimal)             */
   public BigDecimal getTotal_float_hr_cnt(){
      return this.total_float_hr_cnt;
   }

   /** Setter for Column 'TOTAL_FLOAT_HR_CNT'.
     * @param value new Column Value (NUMBER)             */
   public void setTotal_float_hr_cnt(BigDecimal value){
      this.total_float_hr_cnt = value;
   }

   /** Getter for Column 'FREE_FLOAT_HR_CNT'.
     * @return Column Value (BigDecimal)             */
   public BigDecimal getFree_float_hr_cnt(){
      return this.free_float_hr_cnt;
   }

   /** Setter for Column 'FREE_FLOAT_HR_CNT'.
     * @param value new Column Value (NUMBER)             */
   public void setFree_float_hr_cnt(BigDecimal value){
      this.free_float_hr_cnt = value;
   }

   /** Getter for Column 'REMAIN_DRTN_HR_CNT'.
     * @return Column Value (BigDecimal)             */
   public BigDecimal getRemain_drtn_hr_cnt(){
      return this.remain_drtn_hr_cnt;
   }

   /** Setter for Column 'REMAIN_DRTN_HR_CNT'.
     * @param value new Column Value (NUMBER)             */
   public void setRemain_drtn_hr_cnt(BigDecimal value){
      this.remain_drtn_hr_cnt = value;
   }

   /** Getter for Column 'ACT_WORK_QTY'.
     * @return Column Value (BigDecimal)             */
   public BigDecimal getAct_work_qty(){
      return this.act_work_qty;
   }

   /** Setter for Column 'ACT_WORK_QTY'.
     * @param value new Column Value (NUMBER)             */
   public void setAct_work_qty(BigDecimal value){
      this.act_work_qty = value;
   }

   /** Getter for Column 'REMAIN_WORK_QTY'.
     * @return Column Value (BigDecimal)             */
   public BigDecimal getRemain_work_qty(){
      return this.remain_work_qty;
   }

   /** Setter for Column 'REMAIN_WORK_QTY'.
     * @param value new Column Value (NUMBER)             */
   public void setRemain_work_qty(BigDecimal value){
      this.remain_work_qty = value;
   }

   /** Getter for Column 'TARGET_WORK_QTY'.
     * @return Column Value (BigDecimal)             */
   public BigDecimal getTarget_work_qty(){
      return this.target_work_qty;
   }

   /** Setter for Column 'TARGET_WORK_QTY'.
     * @param value new Column Value (NUMBER)             */
   public void setTarget_work_qty(BigDecimal value){
      this.target_work_qty = value;
   }

   /** Getter for Column 'TARGET_DRTN_HR_CNT'.
     * @return Column Value (BigDecimal)             */
   public BigDecimal getTarget_drtn_hr_cnt(){
      return this.target_drtn_hr_cnt;
   }

   /** Setter for Column 'TARGET_DRTN_HR_CNT'.
     * @param value new Column Value (NUMBER)             */
   public void setTarget_drtn_hr_cnt(BigDecimal value){
      this.target_drtn_hr_cnt = value;
   }

   /** Getter for Column 'TARGET_EQUIP_QTY'.
     * @return Column Value (BigDecimal)             */
   public BigDecimal getTarget_equip_qty(){
      return this.target_equip_qty;
   }

   /** Setter for Column 'TARGET_EQUIP_QTY'.
     * @param value new Column Value (NUMBER)             */
   public void setTarget_equip_qty(BigDecimal value){
      this.target_equip_qty = value;
   }

   /** Getter for Column 'ACT_EQUIP_QTY'.
     * @return Column Value (BigDecimal)             */
   public BigDecimal getAct_equip_qty(){
      return this.act_equip_qty;
   }

   /** Setter for Column 'ACT_EQUIP_QTY'.
     * @param value new Column Value (NUMBER)             */
   public void setAct_equip_qty(BigDecimal value){
      this.act_equip_qty = value;
   }

   /** Getter for Column 'REMAIN_EQUIP_QTY'.
     * @return Column Value (BigDecimal)             */
   public BigDecimal getRemain_equip_qty(){
      return this.remain_equip_qty;
   }

   /** Setter for Column 'REMAIN_EQUIP_QTY'.
     * @param value new Column Value (NUMBER)             */
   public void setRemain_equip_qty(BigDecimal value){
      this.remain_equip_qty = value;
   }

   /** Getter for Column 'CSTR_DATE'.
     * @return Column Value (Timestamp)             */
   public Timestamp getCstr_date(){
      return this.cstr_date;
   }

   /** Setter for Column 'CSTR_DATE'.
     * @param value new Column Value (DATE)             */
   public void setCstr_date(Timestamp value){
      this.cstr_date = value;
   }

   /** Getter for Column 'ACT_START_DATE'.
     * @return Column Value (Timestamp)             */
   public Timestamp getAct_start_date(){
      return this.act_start_date;
   }

   /** Setter for Column 'ACT_START_DATE'.
     * @param value new Column Value (DATE)             */
   public void setAct_start_date(Timestamp value){
      this.act_start_date = value;
   }

   /** Getter for Column 'ACT_END_DATE'.
     * @return Column Value (Timestamp)             */
   public Timestamp getAct_end_date(){
      return this.act_end_date;
   }

   /** Setter for Column 'ACT_END_DATE'.
     * @param value new Column Value (DATE)             */
   public void setAct_end_date(Timestamp value){
      this.act_end_date = value;
   }

   /** Getter for Column 'LATE_START_DATE'.
     * @return Column Value (Timestamp)             */
   public Timestamp getLate_start_date(){
      return this.late_start_date;
   }

   /** Setter for Column 'LATE_START_DATE'.
     * @param value new Column Value (DATE)             */
   public void setLate_start_date(Timestamp value){
      this.late_start_date = value;
   }

   /** Getter for Column 'LATE_END_DATE'.
     * @return Column Value (Timestamp)             */
   public Timestamp getLate_end_date(){
      return this.late_end_date;
   }

   /** Setter for Column 'LATE_END_DATE'.
     * @param value new Column Value (DATE)             */
   public void setLate_end_date(Timestamp value){
      this.late_end_date = value;
   }

   /** Getter for Column 'EXPECT_END_DATE'.
     * @return Column Value (Timestamp)             */
   public Timestamp getExpect_end_date(){
      return this.expect_end_date;
   }

   /** Setter for Column 'EXPECT_END_DATE'.
     * @param value new Column Value (DATE)             */
   public void setExpect_end_date(Timestamp value){
      this.expect_end_date = value;
   }

   /** Getter for Column 'EARLY_START_DATE'.
     * @return Column Value (Timestamp)             */
   public Timestamp getEarly_start_date(){
      return this.early_start_date;
   }

   /** Setter for Column 'EARLY_START_DATE'.
     * @param value new Column Value (DATE)             */
   public void setEarly_start_date(Timestamp value){
      this.early_start_date = value;
   }

   /** Getter for Column 'EARLY_END_DATE'.
     * @return Column Value (Timestamp)             */
   public Timestamp getEarly_end_date(){
      return this.early_end_date;
   }

   /** Setter for Column 'EARLY_END_DATE'.
     * @param value new Column Value (DATE)             */
   public void setEarly_end_date(Timestamp value){
      this.early_end_date = value;
   }

   /** Getter for Column 'RESTART_DATE'.
     * @return Column Value (Timestamp)             */
   public Timestamp getRestart_date(){
      return this.restart_date;
   }

   /** Setter for Column 'RESTART_DATE'.
     * @param value new Column Value (DATE)             */
   public void setRestart_date(Timestamp value){
      this.restart_date = value;
   }

   /** Getter for Column 'REEND_DATE'.
     * @return Column Value (Timestamp)             */
   public Timestamp getReend_date(){
      return this.reend_date;
   }

   /** Setter for Column 'REEND_DATE'.
     * @param value new Column Value (DATE)             */
   public void setReend_date(Timestamp value){
      this.reend_date = value;
   }

   /** Getter for Column 'TARGET_START_DATE'.
     * @return Column Value (Timestamp)             */
   public Timestamp getTarget_start_date(){
      return this.target_start_date;
   }

   /** Setter for Column 'TARGET_START_DATE'.
     * @param value new Column Value (DATE)             */
   public void setTarget_start_date(Timestamp value){
      this.target_start_date = value;
   }

   /** Getter for Column 'TARGET_END_DATE'.
     * @return Column Value (Timestamp)             */
   public Timestamp getTarget_end_date(){
      return this.target_end_date;
   }

   /** Setter for Column 'TARGET_END_DATE'.
     * @param value new Column Value (DATE)             */
   public void setTarget_end_date(Timestamp value){
      this.target_end_date = value;
   }

   /** Getter for Column 'REVIEW_END_DATE'.
     * @return Column Value (Timestamp)             */
   public Timestamp getReview_end_date(){
      return this.review_end_date;
   }

   /** Setter for Column 'REVIEW_END_DATE'.
     * @param value new Column Value (DATE)             */
   public void setReview_end_date(Timestamp value){
      this.review_end_date = value;
   }

   /** Getter for Column 'REM_LATE_START_DATE'.
     * @return Column Value (Timestamp)             */
   public Timestamp getRem_late_start_date(){
      return this.rem_late_start_date;
   }

   /** Setter for Column 'REM_LATE_START_DATE'.
     * @param value new Column Value (DATE)             */
   public void setRem_late_start_date(Timestamp value){
      this.rem_late_start_date = value;
   }

   /** Getter for Column 'REM_LATE_END_DATE'.
     * @return Column Value (Timestamp)             */
   public Timestamp getRem_late_end_date(){
      return this.rem_late_end_date;
   }

   /** Setter for Column 'REM_LATE_END_DATE'.
     * @param value new Column Value (DATE)             */
   public void setRem_late_end_date(Timestamp value){
      this.rem_late_end_date = value;
   }

   /** Getter for Column 'CSTR_TYPE'.
     * @return Column Value (String)             */
   public String getCstr_type(){
      return this.cstr_type;
   }

   /** Setter for Column 'CSTR_TYPE'.
     * @param value new Column Value (VARCHAR2)             */
   public void setCstr_type(String value){
      this.cstr_type = value;
   }

   /** Getter for Column 'PRIORITY_TYPE'.
     * @return Column Value (String)             */
   public String getPriority_type(){
      return this.priority_type;
   }

   /** Setter for Column 'PRIORITY_TYPE'.
     * @param value new Column Value (VARCHAR2)             */
   public void setPriority_type(String value){
      this.priority_type = value;
   }

   /** Getter for Column 'GUID'.
     * @return Column Value (String)             */
   public String getGuid(){
      return this.guid;
   }

   /** Setter for Column 'GUID'.
     * @param value new Column Value (VARCHAR2)             */
   public void setGuid(String value){
      this.guid = value;
   }

   /** Getter for Column 'TMPL_GUID'.
     * @return Column Value (String)             */
   public String getTmpl_guid(){
      return this.tmpl_guid;
   }

   /** Setter for Column 'TMPL_GUID'.
     * @param value new Column Value (VARCHAR2)             */
   public void setTmpl_guid(String value){
      this.tmpl_guid = value;
   }

   /** Getter for Column 'CSTR_DATE2'.
     * @return Column Value (Timestamp)             */
   public Timestamp getCstr_date2(){
      return this.cstr_date2;
   }

   /** Setter for Column 'CSTR_DATE2'.
     * @param value new Column Value (DATE)             */
   public void setCstr_date2(Timestamp value){
      this.cstr_date2 = value;
   }

   /** Getter for Column 'CSTR_TYPE2'.
     * @return Column Value (String)             */
   public String getCstr_type2(){
      return this.cstr_type2;
   }

   /** Setter for Column 'CSTR_TYPE2'.
     * @param value new Column Value (VARCHAR2)             */
   public void setCstr_type2(String value){
      this.cstr_type2 = value;
   }

   /** Getter for Column 'ACT_THIS_PER_WORK_QTY'.
     * @return Column Value (BigDecimal)             */
   public BigDecimal getAct_this_per_work_qty(){
      return this.act_this_per_work_qty;
   }

   /** Setter for Column 'ACT_THIS_PER_WORK_QTY'.
     * @param value new Column Value (NUMBER)             */
   public void setAct_this_per_work_qty(BigDecimal value){
      this.act_this_per_work_qty = value;
   }

   /** Getter for Column 'ACT_THIS_PER_EQUIP_QTY'.
     * @return Column Value (BigDecimal)             */
   public BigDecimal getAct_this_per_equip_qty(){
      return this.act_this_per_equip_qty;
   }

   /** Setter for Column 'ACT_THIS_PER_EQUIP_QTY'.
     * @param value new Column Value (NUMBER)             */
   public void setAct_this_per_equip_qty(BigDecimal value){
      this.act_this_per_equip_qty = value;
   }

   /** Getter for Column 'DRIVING_PATH_FLAG'.
     * @return Column Value (String)             */
   public String getDriving_path_flag(){
      return this.driving_path_flag;
   }

   /** Setter for Column 'DRIVING_PATH_FLAG'.
     * @param value new Column Value (VARCHAR2)             */
   public void setDriving_path_flag(String value){
      this.driving_path_flag = value;
   }

   public  String toString(){

      StringBuffer buffer = new StringBuffer();

      buffer.append("Task: ");

      buffer.append("[TASK_ID=" + this.task_id+"]");
      buffer.append("[PROJ_ID=" + this.proj_id+"]");
      buffer.append("[WBS_ID=" + this.wbs_id+"]");
      buffer.append("[CLNDR_ID=" + this.clndr_id+"]");
      buffer.append("[EST_WT=" + this.est_wt+"]");
      buffer.append("[PHYS_COMPLETE_PCT=" + this.phys_complete_pct+"]");
      buffer.append("[REV_FDBK_FLAG=" + this.rev_fdbk_flag+"]");
      buffer.append("[LOCK_PLAN_FLAG=" + this.lock_plan_flag+"]");
      buffer.append("[AUTO_COMPUTE_ACT_FLAG=" + this.auto_compute_act_flag+"]");
      buffer.append("[COMPLETE_PCT_TYPE=" + this.complete_pct_type+"]");
      buffer.append("[TASK_TYPE=" + this.task_type+"]");
      buffer.append("[DURATION_TYPE=" + this.duration_type+"]");
      buffer.append("[REVIEW_TYPE=" + this.review_type+"]");
      buffer.append("[STATUS_CODE=" + this.status_code+"]");
      buffer.append("[TASK_CODE=" + this.task_code+"]");
      buffer.append("[TASK_NAME=" + this.task_name+"]");
      buffer.append("[RSRC_ID=" + this.rsrc_id+"]");
      buffer.append("[TOTAL_FLOAT_HR_CNT=" + this.total_float_hr_cnt+"]");
      buffer.append("[FREE_FLOAT_HR_CNT=" + this.free_float_hr_cnt+"]");
      buffer.append("[REMAIN_DRTN_HR_CNT=" + this.remain_drtn_hr_cnt+"]");
      buffer.append("[ACT_WORK_QTY=" + this.act_work_qty+"]");
      buffer.append("[REMAIN_WORK_QTY=" + this.remain_work_qty+"]");
      buffer.append("[TARGET_WORK_QTY=" + this.target_work_qty+"]");
      buffer.append("[TARGET_DRTN_HR_CNT=" + this.target_drtn_hr_cnt+"]");
      buffer.append("[TARGET_EQUIP_QTY=" + this.target_equip_qty+"]");
      buffer.append("[ACT_EQUIP_QTY=" + this.act_equip_qty+"]");
      buffer.append("[REMAIN_EQUIP_QTY=" + this.remain_equip_qty+"]");
      buffer.append("[CSTR_DATE=" + this.cstr_date+"]");
      buffer.append("[ACT_START_DATE=" + this.act_start_date+"]");
      buffer.append("[ACT_END_DATE=" + this.act_end_date+"]");
      buffer.append("[LATE_START_DATE=" + this.late_start_date+"]");
      buffer.append("[LATE_END_DATE=" + this.late_end_date+"]");
      buffer.append("[EXPECT_END_DATE=" + this.expect_end_date+"]");
      buffer.append("[EARLY_START_DATE=" + this.early_start_date+"]");
      buffer.append("[EARLY_END_DATE=" + this.early_end_date+"]");
      buffer.append("[RESTART_DATE=" + this.restart_date+"]");
      buffer.append("[REEND_DATE=" + this.reend_date+"]");
      buffer.append("[TARGET_START_DATE=" + this.target_start_date+"]");
      buffer.append("[TARGET_END_DATE=" + this.target_end_date+"]");
      buffer.append("[REVIEW_END_DATE=" + this.review_end_date+"]");
      buffer.append("[REM_LATE_START_DATE=" + this.rem_late_start_date+"]");
      buffer.append("[REM_LATE_END_DATE=" + this.rem_late_end_date+"]");
      buffer.append("[CSTR_TYPE=" + this.cstr_type+"]");
      buffer.append("[PRIORITY_TYPE=" + this.priority_type+"]");
      buffer.append("[GUID=" + this.guid+"]");
      buffer.append("[TMPL_GUID=" + this.tmpl_guid+"]");
      buffer.append("[CSTR_DATE2=" + this.cstr_date2+"]");
      buffer.append("[CSTR_TYPE2=" + this.cstr_type2+"]");
      buffer.append("[ACT_THIS_PER_WORK_QTY=" + this.act_this_per_work_qty+"]");
      buffer.append("[ACT_THIS_PER_EQUIP_QTY=" + this.act_this_per_equip_qty+"]");
      buffer.append("[DRIVING_PATH_FLAG=" + this.driving_path_flag+"]");
      return buffer.toString();

   }


}//end class: Task