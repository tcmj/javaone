package com.tcmj.common.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to simplify jdbc operations.
 * @author Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 * @since 28.07.2011
 */
public final class SQLTool {

    /** slf4j Logging API. */
    private static transient Logger logger = LoggerFactory.getLogger(SQLTool.class);

    /** instantiation not allowed! */
    private SQLTool() {
    }

    /**
     * tries to close a {@link ResultSet}.
     * @param rs ResultSet object
     */
    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.warn("Failed to close ResultSet: " + e.getMessage());
            }
        }
    }

    /**
     * tries to close a {@link Statement}.
     * @param stmt Statement/PreparedStatement object
     */
    public static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                logger.warn("Failed to close Statement: " + e.getMessage());
            }
        }
    }

    /**
     * tries to close a {@link Connection}.
     * @param connection jdbc connection object
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.warn("Failed to close ResultSet: " + e.getMessage());
            }
        }
    }

    /**
     * tries to close the given ResultSet object and then the Statement.
     * @param stmt Statement/PreparedStatement object
     * @param rs ResultSet object
     */
    public static void close(Statement stmt, ResultSet rs) {
        close(rs);
        close(stmt);
    }

    /**
     * tries to close the given ResultSet object and then the Statement and
     * then the connection object.
     * @param connection jdbc connection object
     * @param stmt Statement/PreparedStatement object
     * @param rs ResultSet object
     */
    public static void close(Connection connection, Statement stmt, ResultSet rs) {
        close(rs);
        close(stmt);
        close(connection);
    }

}
