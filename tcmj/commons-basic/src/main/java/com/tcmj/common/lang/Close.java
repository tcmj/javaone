package com.tcmj.common.lang;

import java.io.Closeable;
import java.io.IOException;
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
    private static void closeIntern(AutoCloseable object, boolean throwUnchecked) {
        if (object != null) {
            try {
                object.close();
            } catch (Exception e) {
                if (throwUnchecked) {
                    logger.error("Cannot close {}", object);
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Closes the object quietly swallowing any exception!
     * @param object to be closed
     */
    public static void quiet(AutoCloseable object) {
        closeIntern(object, false);
    }

    /**
     * Closes the object wrapping exceptions to unchecked runtime exception!
     * @param object to be closed
     */
    public static void unchecked(AutoCloseable object) {
        closeIntern(object, true);
    }

    /**
     * Closes the object the 'normal' way using a checked exception!<br>
     * Closes this stream and releases any system resources associated with it. If the stream is already closed then invoking this method has no effect.
     * @param object to be closed
     * @throws input / output errors from the system
     */
    public static void checked(Closeable object) throws IOException {
        if (object != null) {
            object.close();
        }
    }

    /**
     * Transform a unchecked exception to a checked one
     * @param object to be closed
     * @throws Exception containing any exception thrown by the close method.
     */
    public static void checked(AutoCloseable object) throws Exception {
        if (object != null) {
            try {
                object.close();
            } catch (Exception e) {
                throw new Exception(e);
            }
        }
    }
}
