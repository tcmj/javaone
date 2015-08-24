package com.tcmj.common.lang;

import java.util.Collection;
import java.util.Map;
import java.util.zip.ZipFile;
import com.tcmj.common.reflection.ReflectionHelper;
import org.apache.commons.lang3.StringUtils;

/**
 * Objects Utility class (like apaches Validate or Googles Precondition class or java.util.Objects).
 * <p/>
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
        if (condition == false) {
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
     * <li>{@link java.util.Collection} (isEmpty())</li>
     * <li>{@link java.util.Map} (isEmpty)</li>
     * <li>Primitive arrays (length == 0)</li>
     * <li>Strings or {@link CharSequence} (length)</li>
     * <li>{@link java.util.zip.ZipFile} (size)</li>
     * </ul>
     * <b>All other classes will only be checked if they are null (=true) or not null (=false)!</b>
     * @param obj insert the Object to check for emptyness here
     * @return boolean
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else {
            if (Collection.class.isAssignableFrom(obj.getClass())) {
                return ((Collection) obj).isEmpty();
            } else if (Map.class.isAssignableFrom(obj.getClass())) {
                return ((Map) obj).isEmpty();
            } else if (Object[].class.isAssignableFrom(obj.getClass())) {
                return ((Object[]) obj).length == 0;
            } else if (CharSequence.class.isAssignableFrom(obj.getClass())) {
                return ((CharSequence) obj).length() == 0;
            } else if (ZipFile.class.isAssignableFrom(obj.getClass())) {
                return ((ZipFile) obj).size() == 0;
            } else {
                return false;
            }
        }
    }

    /**
     * Internal method to produce strings filled with formatted values.
     * This method uses the slf4j formatting method using '{}'.
     */
    private static String format(String msg, Object... params) {
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
