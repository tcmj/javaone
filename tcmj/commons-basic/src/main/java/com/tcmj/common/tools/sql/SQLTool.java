/*
 * Copyright (C) 2011 tcmj development
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.tcmj.common.tools.sql;

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
public class SQLTool {

    /** slf4j Logging API. */
    private static final transient Logger logger = LoggerFactory.getLogger(SQLTool.class);


    /**
     * instantiation not allowed!
     */
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
    public static void close(Connection con, Statement stmt, ResultSet rs) {
        close(rs);
        close(stmt);
        close(con);
    }
    
}
