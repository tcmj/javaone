package com.tcmj.common.jdbc.connect;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Map;

/**
 * DBQConnection. Wrapper class to use the DBQuickConnect class like an
 * normal java.sql.Connection object.<br>
 * This Connection uses all features of DBQuickConnect like:
 * <ul>
 * <li>Statement Cache</li>
 * <li>Extended features to clean up resources (ResultSets etc)</li>
 * </ul>
 * @author tcmj - Thöomas Deutsch (c)2009-06
 */
public class DBQConnection extends DBQuickConnect implements Connection {

    public DBQConnection() {
        super();
    }

    public Statement createStatement() throws SQLException {
        return mSCache.getStatement();
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return pstPrepareStatement(sql);
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        return getConnection().prepareCall(sql);
    }

    public String nativeSQL(String sql) throws SQLException {
        return getConnection().nativeSQL(sql);
    }

    public boolean getAutoCommit() throws SQLException {
        return getConnection().getAutoCommit();
    }

    public void commit() throws SQLException {
        getConnection().commit();
    }

    public void rollback() throws SQLException {
        getConnection().rollback();
    }

    public void close() throws SQLException {
        closeConnection();
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        return getConnection().getMetaData();
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        getConnection().setReadOnly(readOnly);
    }

    public boolean isReadOnly() throws SQLException {
        return getConnection().isReadOnly();
    }

    public void setCatalog(String catalog) throws SQLException {
        getConnection().setCatalog(catalog);
    }

    public String getCatalog() throws SQLException {
        return getConnection().getCatalog();
    }

    public void setTransactionIsolation(int level) throws SQLException {
        getConnection().setTransactionIsolation(level);
    }

    public int getTransactionIsolation() throws SQLException {
        return getConnection().getTransactionIsolation();
    }

    public SQLWarning getWarnings() throws SQLException {
        return getConnection().getWarnings();
    }

    public void clearWarnings() throws SQLException {
        getConnection().clearWarnings();
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return mSCache.getStatement(resultSetType, resultSetConcurrency, getConnection().getHoldability());
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return pstWatcher.getPreparedStatement(sql, resultSetType, resultSetConcurrency, connection.getHoldability());
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return getConnection().prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return getConnection().getTypeMap();
    }

    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        getConnection().setTypeMap(map);
    }

    public void setHoldability(int holdability) throws SQLException {
        getConnection().setHoldability(holdability);
    }

    public int getHoldability() throws SQLException {
        return getConnection().getHoldability();
    }

    public Savepoint setSavepoint() throws SQLException {
        return getConnection().setSavepoint();
    }

    public Savepoint setSavepoint(String name) throws SQLException {
        return getConnection().setSavepoint(name);
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        getConnection().rollback(savepoint);
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        getConnection().releaseSavepoint(savepoint);
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return mSCache.getStatement(resultSetType, resultSetConcurrency, resultSetConcurrency);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return pstWatcher.getPreparedStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return getConnection().prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return pstWatcher.getPreparedStatement(sql, autoGeneratedKeys);
    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return pstWatcher.getPreparedStatement(sql, columnIndexes);
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return pstWatcher.getPreparedStatement(sql, columnNames);
    }

//    public Clob createClob() throws SQLException {
//        return getConnection().createClob();
//    }
//
//    public Blob createBlob() throws SQLException {
//        return getConnection().createBlob();
//    }
//
//    public NClob createNClob() throws SQLException {
//        return getConnection().createNClob();
//    }
//
//    public SQLXML createSQLXML() throws SQLException {
//        return getConnection().createSQLXML();
//    }
//
//    public boolean isValid(int timeout) throws SQLException {
//        return (connection != null && connection.isValid(timeout));
//    }
//
//    public void setClientInfo(String name, String value) throws SQLClientInfoException {
//        getConnection().setClientInfo(name, value);
//    }
//
//    public void setClientInfo(Properties properties) throws SQLClientInfoException {
//        getConnection().setClientInfo(properties);
//    }
//
//    public String getClientInfo(String name) throws SQLException {
//        return getConnection().getClientInfo(name);
//    }
//
//    public Properties getClientInfo() throws SQLException {
//        return getConnection().getClientInfo();
//    }
//
//    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
//        return getConnection().createArrayOf(typeName, elements);
//    }
//
//    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
//        return getConnection().createStruct(typeName, attributes);
//    }
//
//    public <T> T unwrap(Class<T> iface) throws SQLException {
//        return getConnection().unwrap(iface);
//    }
//
//    public boolean isWrapperFor(Class<?> iface) throws SQLException {
//        return getConnection().isWrapperFor(iface);
//    }


}
