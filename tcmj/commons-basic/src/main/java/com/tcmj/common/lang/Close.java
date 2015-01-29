package com.tcmj.common.lang;

import java.io.Closeable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a bunch of closing methods for streams, readers, writers, channels.
 * Take a look at {@link java.io.Closeable} !
 * @author tcmj
 */
public class Close {

    /** slf4j Logging framework. */
    private static final Logger logger = LoggerFactory.getLogger(Close.class);

    /**
     * instantiation not allowed!
     */
    private Close() {
    }

    /**
     * Internal method which provides the ability to swallow the exception or to
     * wrap the exception to an unchecked exception.
     */
    private static void closeIntern(Closeable object, boolean throwUnchecked) {
        if (object != null) {
            try {
                object.close();
            } catch (Exception e) {
                logger.error("Cannot close {}", object);
                if (throwUnchecked) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Closes the object quietly swallowing any exception!
     * @param object to be closed
     */
    public static void quiet(Closeable object) {
        closeIntern(object, false);
    }

    /**
     * Closes the object wrapping exceptions to unchecked runtime exception!
     * @param object to be closed
     */
    public static void unchecked(Closeable object) {
        closeIntern(object, true);
    }
}
