package com.tcmj.common.lang;

import java.util.Collection;
import java.util.Map;

/**
 * Expression Helper with extended functionality.
 * Consider using {@link org.apache.commons.lang.Validate}
 * @author Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 * @since 07.06.2010
 */
public final class Expression {

    /** private no-arg-constructor. */
    private Expression() {
    }

    /**
     * Ensures that the given collection is null or empty.
     * @param collection any list or set or map
     * @return true or false
     */
    private static boolean isEmptyCollection(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Ensures that the given map is null or empty.
     * @param map any list or set or map
     * @return true or false
     */
    private static boolean isEmptyMap(Map map) {
        return map == null || map.isEmpty();
    }

    /**
     * checks if an object is null or empty.<br/>
     * Supports<br/>
     * <ul>
     * <li>java.util.Collection (isEmpty)</li>
     * <li>java.util.Map (isEmpty)</li>
     * </ul>
     * @param obj Object
     * @return bool
     */
    public static boolean isEmpty(Object obj) {
        boolean empty;
        if (obj == null) {
            empty = true;
        } else {
            if (obj instanceof Collection) {
                empty = isEmptyCollection((Collection) obj);
            } else if (obj instanceof Map) {
                empty = isEmptyMap((Map) obj);
            } else {
                empty = false;
            }
        }
        return empty;
    }

    /**
     * Negotiates the isEmpty method.<br/>
     * {@link #isEmpty(Object)}<br/>
     * @param obj Object
     * @return true or false
     */
    public static boolean isNotEmpty(Object obj) {
        return !(isEmpty(obj));
    }

}
