/* Copyright 2009 */
package com.tcmj.common.tools.lang;

import java.util.Collection;
import java.util.Map;

/**
 * Expression Helper with extended functionality.
 * @author Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 * @since 07.06.2010
 */
public class Expression {

    /**
     * private no-arg-constructor.
     */
    private Expression() {
    }


    /**
     * Ensures that the given collection is null or empty.
     * @param collection any list or set or map
     * @return true or false
     */
    private static final boolean isEmptyCollection(Collection collection) {
        return (collection == null || collection.isEmpty());
    }


    /**
     * Ensures that the given map is null or empty.
     * @param map any list or set or map
     * @return true or false
     */
    private static final boolean isEmptyMap(Map map) {
        return (map == null || map.isEmpty());
    }


    /**
     * checks if an object is null or empty.<br/>
     * Supports<br/>
     * <ul>
     * <li>java.util.Collection (isEmpty)</li>
     * <li>java.util.Map (isEmpty)</li>
     * </ul>
     * @param object
     * @return
     */
    public static final boolean isEmpty(Object object) {
        boolean empty;
        if (object == null) {
            empty = true;
        } else {
            if (object instanceof Collection) {
                empty = isEmptyCollection((Collection) object);
            } else if (object instanceof Map) {
                empty = isEmptyMap((Map) object);
            } else {
                empty = (object != null);
            }
        }
        return empty;
    }


    /**
     * Negotiates the isEmpty method.<br/>
     * {@link #isEmpty(Obect)}<br/>
     * @param object
     * @return true or false
     */
    public static final boolean isNotEmpty(Object object) {
        return !(isEmpty(object));
    }
}
