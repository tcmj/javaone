package com.tcmj.common.jdbc.connect.intern;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * StatementCache.
 * @author tcmj - Thomas Deutsch
 */
public class StatementCache {

    /** slf4j Logging framework. */
    private static final transient Logger logger = LoggerFactory.getLogger(StatementCache.class);


    private Connection con;

    private Stack<Statement> cache;

    private int count_reused;

    private int count_created;

    private int count_released;

    private boolean debug = false;

    public StatementCache(Connection con) {
        this.con = con;
        this.cache = new Stack();
        this.count_reused = 0;
        this.count_created = 0;
        this.count_released = 0;
    }

    public Statement getStatement() throws SQLException {
        if (cache.isEmpty()) {

            Statement st = con.createStatement();
            count_created++;

            if (debug && st != null) {
                logger.trace("creating a new Statement() " + Integer.toHexString(st.hashCode()));
            }
            return st;

        } else {

            Statement st = (Statement) cache.pop();
            this.count_reused++;

            if (debug && st != null) {
                logger.trace("using a cached Statement() " + Integer.toHexString(st.hashCode()) +
                        " (" + cache.size() + " left)");
            }
            return st;

        }
    }

    public Statement getStatement(int rsType, int rsConcurrency,
            int rsHoldability) throws SQLException {
        if (cache.isEmpty()) {

            Statement st = con.createStatement(rsType, rsConcurrency, rsHoldability);
            count_created++;

            if (debug && st != null) {
                logger.trace("creating a new Statement(emptycache) " + Integer.toHexString(st.hashCode()));
            }
            return st;

        } else {

            //search if a stmt exists with the needed behavour:
            Statement statement = null;
            for (Iterator<Statement> it = cache.iterator(); it.hasNext();) {
                statement = it.next();

                int resultsettype = statement.getResultSetType();
                int resultsetconc = statement.getResultSetConcurrency();
                int resultsethold = statement.getResultSetHoldability();

                if (resultsettype == rsType &&
                        resultsetconc == rsConcurrency &&
                        resultsethold == rsHoldability) {

                    this.count_reused++;

                    it.remove();

                    if (debug && statement != null) {
                        logger.trace("using a cached Statement(match) " + Integer.toHexString(statement.hashCode()) +
                                " (" + cache.size() + " left)");
                    }

                    return statement;
                }

            }
            //nothing suitable found...
            if (statement == null) {
                statement = con.createStatement(rsType, rsConcurrency, rsHoldability);
                count_created++;

                if (debug && statement != null) {
                    logger.trace("creating a new Statement(nsfound) " + Integer.toHexString(statement.hashCode()));
                }

            }
            return statement;
        }
    }

    public void releaseStatement(Statement statement) throws SQLException {
        if (!cache.contains(statement)) {
            cache.add(statement);
            count_released++;
        } else {
            throw new SQLException("Statement " + statement + " has already been released! Do not release it twice!");
        }

        if (debug && statement != null) {
            logger.trace("releasing Statement " + Integer.toHexString(statement.hashCode()) +
                    " (cachesize = " + cache.size() + ")");
        }

    }

    public void closeall() {
        java.util.Iterator it = cache.iterator();
        while (it.hasNext()) {
            java.sql.Statement stmt = (java.sql.Statement) it.next();

            try {
                if (debug) {
                    logger.debug("closing Statement " + stmt);
                }
                stmt.close();
                stmt = null;
            } catch (Exception e) {
                if (debug) {
                    logger.debug("error closing Statement " +
                            stmt + ": " + e.getMessage());
                }
            }
        }
        cache.clear();

    }

    /**
     * Getter for property debug.
     * @return Value of property debug.
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Setter for property debug.
     * @param debug New value of property debug.
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
        if (debug) {
            logger.trace("cache.connection " + con);
        }
    }

    public int getCountCreated() {
        return this.count_created;
    }

    public int getCountReleased() {
        return this.count_released;
    }

    public int getCountReused() {
        return this.count_reused;
    }

    public String getReleaseInfo() {

//          float ratio = (float) (count_reused / count_created) * 100F;

        return "created=" + count_created +
                "  reused=" + count_reused +
                "  released=" + count_released + "/" + (count_created + count_reused);
//                    "   (ratio: " + DECF.format(ratio) + " %)";
    }

    @Override
    public String toString() {
        return "StmtCache@" + Integer.toHexString(hashCode());
    }

}
