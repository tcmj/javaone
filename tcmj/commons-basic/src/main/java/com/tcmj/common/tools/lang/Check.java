/*
 *  Copyright (C) 2011 Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.tcmj.common.tools.lang;

import java.text.MessageFormat;
import org.apache.commons.lang.StringUtils;

/**
 * Check Utility class (like apaches Validate or Googles Precondition class).
 * <p>All message strings can be formatted using the {@link java.text.MessageFormat} patterns
 * @author tcmj - Thomas Deutsch
 * @see org.apache.commons.lang.Validate
 * @since 13.04.2011
 */
public class Check {

    /**
     * private no-arg-constructor.
     */
    private Check() {
    }


    /**
     * Checks that a object is not null and throws a NullPointerException with
     * a custom message. <p>The message strings can be formatted using the {@link java.text.MessageFormat} patterns</p>
     * <pre>
     *    notNull(name, "Name can not be Null!");
     *    String this.name = notNull(name, "Name can not be Null!");
     *    String this.name = notNull(name, "Name can not be Null! {0,date}", new Date());
     *    String this.name = notNull(name, "Dear {0} your name can not be Null! {1,date}", user, new Date());
     * </pre>
     * @param <T> typesafe
     * @param instance the object to check
     * @param msg a custom message used in the exception text
     * @param params value objects to be placed into the message (starting with {0})
     * @return passes through the instance in order to do the assignment in the same line
     */
    public static <T> T notNull(T instance, String msg, Object... params) {
        if (instance == null) {
            String emsg = buildMessage(msg, params);
            throw new NullPointerException(emsg);
        }
        return instance;
    }


    /**
     * Ensures that a expression has to be true and throws a IllegalArgumentException with
     * a custom message on failure.
     * <p>The message strings can be formatted using the {@link java.text.MessageFormat} patterns</p>
     * <pre>
     *    ensure(!list.isEmpty(), "Your list cannot be empty!");
     *
     *    boolean connected = ensure(session.isConnected(), "Connection lost at {0,date}", new Date());
     * </pre>
     * @param condition expression which must be true or false
     * @param msg a custom message used in the exception text
     * @param params value objects to be placed into the message (starting with {0})
     * @return true if the condition is true
     * @throws IllegalArgumentException if the condition is false
     */
    public static boolean ensure(boolean condition, String msg, Object... params) {
        if (condition == false) {
            String emsg = buildMessage(msg, params);
            throw new IllegalArgumentException(emsg);
        }
        return true;
    }


    /**
     * Ensures that a {@link String} has to be <b>not</b>
     * <li>null</li>
     * <li>empty ("")</li>
     * <li>whitespace ("  ")</li>
     * <p>and throws a IllegalArgumentException with</p>
     * a custom message on failure.
     * <p>The message strings can be formatted using the {@link java.text.MessageFormat} patterns</p>
     * <pre>
     *    notBlank(parameterName, "Your have to provide a non-empty name to use this method!");
     *
     *    this.name = notBlank(parameterName, "Say your name or pay {0,number,currency}!", 25.50);
     * </pre>
     * @param string
     * @param message
     * @return
     */
    public static String notBlank(String string, String message, Object... params) {
        if (StringUtils.isBlank(string)) {
            throw new IllegalArgumentException(buildMessage(message, params));
        }
        return string;
    }


    /** Internal method to produce strings filled with formatted values. */
    private static String buildMessage(String msg, Object... params) {
        if (params == null) {
            return msg;
        } else {
            return MessageFormat.format(msg, params);
        }
    }
}
