package com.tcmj.common.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a bunch of closing methods for all classes implementing {@link java.lang.AutoCloseable} !
 * Consider using try-with-resources !
 * @author tcmj - Thomas Deutsch
 * @test com.tcmj.common.lang.CloseTest
 */
public class Close {

    /** slf4j Logging framework. */
    private static final Logger LOG = LoggerFactory.getLogger(Close.class);

    /**
     * instantiation not allowed!
     */
    private Close() {
    }

    /**
     * Internal method which provides the ability to swallow the exception or to
     * throw any exception in an unchecked way {@link com.tcmj.common.lang.Objects#throwUnchecked(Throwable)}
     * @param object The object to be closed
     * @param throwException if the Exception should be thrown
     */
    private static void closeIntern(AutoCloseable object, boolean throwException) {
        if (object != null) {
            try {
                object.close();
            } catch (Exception e) {
                if (throwException) {
                    LOG.debug("Cannot close {} because of {}", object, e.getMessage());
                    Objects.throwUnchecked(e);
                }
            }
        }
    }

    /**
     * Closes the object quietly by swallowing any exception without any logging!
     * @param object to be closed
     */
    public static void inSilence(AutoCloseable object) {
        closeIntern(object, false);
    }

    /**
     * Closes the object removing the need of catching exceptions but throwing them!
     * @param object to be closed
     */
    public static void unchecked(AutoCloseable object) {
        closeIntern(object, true);
    }

}
