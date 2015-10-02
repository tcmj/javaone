package com.tcmj.common.lang;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;
import java.util.zip.ZipFile;
import com.tcmj.common.reflection.ReflectionHelper;
import org.apache.commons.lang3.StringUtils;

/**
 * Objects Utility class (like apaches Validate or Googles Precondition class or java.util.Objects).
 * <p>
 * All message strings can be formatted using the {@link org.slf4j.helpers.MessageFormatter} patterns
 * @author tcmj - Thomas Deutsch
 * @see java.util.Objects
 * @see org.apache.commons.lang3.Validate
 * @see com.google.common.base.Preconditions
 * @since 13.04.2011
 */
public class Objects {

    /** private no-arg-constructor because we have only static methods. */
    private Objects() {
    }

    /**
     * Checks that a object is not null and throws a NullPointerException with a custom message.
     * <p>
     * The message strings can be formatted using the {@link org.slf4j.helpers.MessageFormatter} patterns</p>
     * <pre>
     *    notNull(name, "Name can not be Null!");
     *    String this.name = notNull(name, "Name can not be Null!");
     *    String this.name = notNull(name, "Name can not be Null! {}", new Date());
     *    String this.name = notNull(name, "Dear {} your name can not be Null! {}", user, new Date());
     * </pre>
     * @param <T> typesafe
     * @param instance the object to check
     * @param msg a custom message used in the exception text
     * @param params value objects to be placed into the message (placeholder: '{}')
     * @return passes through the instance in order to do the assignment in the same line
     */
    public static <T> T notNull(T instance, String msg, Object... params) {
        if (instance == null) {
            throw new NullPointerException(format(msg, params));
        }
        return instance;
    }

    /**
     * Checks that a object is not null and throws a NullPointerException with a custom message.
     * <p>
     * The message strings can be formatted using the {@link org.slf4j.helpers.MessageFormatter} patterns</p>
     * <pre>
     *    notNull(name, "Name can not be Null!");
     *    String this.name = notNull(name, IOException.class, "Name can not be Null!");
     *    String this.name = notNull(name, RuntimeException.class, "Name can not be Null today on {}!", new Date());
     *    String this.name = notNull(name, Exception.class, "Dear {} your name can not be Null! {}", user, new Date());
     * </pre>
     * @param <T> typesafe
     * @param instance the object to check
     * @param exception the exception we want to throw
     * @param msg a custom message used in the exception text
     * @param params value objects to be placed into the message (placeholder: '{}')
     * @return passes through the instance in order to do the assignment in the same line
     */
    public static <T> T notNull(T instance, Class<? extends Exception> exception, String msg, Object... params) {
        if (instance == null) {
            throwUnchecked(ReflectionHelper.newException(exception, format(msg, params)));
        }
        return instance;
    }


    /**
     * Ensures that a expression has to be true and throws a IllegalArgumentException with a custom message on failure.
     * <p>
     * The message strings can be formatted using the {@link org.slf4j.helpers.MessageFormatter} patterns</p>
     * <pre>
     *    ensure(!list.isEmpty(), "Your list cannot be empty!");
     *
     *    boolean connected = ensure(session.isConnected(), "Connection lost at {}", new Date());
     * </pre>
     * @param condition expression which must be true or false
     * @param msg a custom message used in the exception text
     * @param params value objects to be placed into the message (set placeholders using {})
     * @return true if the condition is true
     * @throws IllegalStateException if the condition is false
     */
    public static boolean ensure(boolean condition, String msg, Object... params) {
        if (!condition) {
            throw new IllegalStateException(format(msg, params));
        }
        return true;
    }

    /**
     * Ensures that a {@link String} has to be <b>not</b>
     * <li>null</li>
     * <li>empty ("")</li>
     * <li>whitespace (" ")</li>
     * <p>
     * and throws a IllegalArgumentException with</p>
     * a custom message on failure.
     * <p>
     * The message strings can be formatted using the {@link org.slf4j.helpers.MessageFormatter} patterns</p>
     * <pre>
     *    notBlank(parameterName, "Your have to provide a non-empty name to use this method!");
     *
     *    this.name = notBlank(parameterName, "Say your name or pay {}!", 25.50);
     * </pre>
     * @param string the string to check
     * @param message a message used by the thrown exception
     * @return the given string parameter (parameter no 1)
     * @throws IllegalArgumentException if the string is blank {@link org.apache.commons.lang3.StringUtils#isBlank(CharSequence)}  }
     */
    public static CharSequence notBlank(CharSequence string, String message, Object... params) {
        if (StringUtils.isBlank(string)) {
            throw new IllegalArgumentException(format(message, params));
        }
        return string;
    }

    /**
     * checks if an object is null or empty.<br/>
     * Supports<br/>
     * <ul>
     * <li>any 'null' object will be returned as 'false'</li>
     * <li>{@link java.util.Collection} (isEmpty())</li>
     * <li>{@link java.util.Map} (isEmpty)</li>
     * <li>Primitive arrays return true if obj.length == 0</li>
     * <li>Strings or {@link CharSequence} return true if obj.length == 0</li>
     * <li>{@link java.util.zip.ZipFile} (size)</li>
     * </ul>
     * <b>All other classes will only be checked if they are null (=true) or not null (=false)!</b>
     * @param obj insert the Object to check for emptyness here
     * @return boolean
     */
    public static boolean isEmpty(Object obj) {

        if (obj == null) {
            return true;
        }
        if (Collection.class.isAssignableFrom(obj.getClass())) {
            return ((Collection) obj).isEmpty();
        }
        if (Map.class.isAssignableFrom(obj.getClass())) {
            return ((Map) obj).isEmpty();
        }
        if (Object[].class.isAssignableFrom(obj.getClass())) {
            return ((Object[]) obj).length == 0;
        }
        if (CharSequence.class.isAssignableFrom(obj.getClass())) {
            return ((CharSequence) obj).length() == 0;
        }
        if (Number.class.isAssignableFrom(obj.getClass())) {
            return ((Number) obj).longValue() == 0L;
        }
        if (Stream.class.isAssignableFrom(obj.getClass())) {
            return ((Stream) obj).count() <= 0;
        }
        if (ZipFile.class.isAssignableFrom(obj.getClass())) {
            return ((ZipFile) obj).size() == 0;
        }
        if (ResultSet.class.isAssignableFrom(obj.getClass())) {
            try {
                //true if the cursor is before the first row; false if the cursor is at any other position or the result set contains no rows
                return !((ResultSet) obj).isBeforeFirst();
            } catch (SQLFeatureNotSupportedException sqlfnsex) {
                Objects.throwUnchecked(sqlfnsex); // Feature not supported by the jdbc driver
            } catch (SQLException sqlex) {
                return true;
            }
        }
        throw new UnsupportedOperationException("Class " + obj.getClass() + " not supported for empty checks!");

    }

    /**
     * Inverted version of {@link #isEmpty(Object)}
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * Internal method to produce strings filled with formatted values.
     * This method uses the slf4j formatting method using '{}'.
     * @param msg The message which can contain placeholders optionally
     * @param params The optional possible parameters
     */
    public static String format(String msg, Object... params) {
        if (params == null) {
            return msg;
        } else if (params.length == 1) {
            return org.slf4j.helpers.MessageFormatter.format(msg, params[0]).getMessage();
        } else if (params.length == 2) {
            return org.slf4j.helpers.MessageFormatter.format(msg, params[0], params[1]).getMessage();
        } else {
            return org.slf4j.helpers.MessageFormatter.arrayFormat(msg, params).getMessage();
        }
    }


    /**
     * Throws any exception as a unchecked exception!
     * <pre>Objects.throwUnchecked(new java.io.IOException("abc"));</pre>
     * @param e any checked or unchecked exception object
     */
    public static void throwUnchecked(Throwable e) {
        Objects.<RuntimeException>throwAny(e);
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void throwAny(Throwable e) throws E {
        throw (E) e;
    }
}
