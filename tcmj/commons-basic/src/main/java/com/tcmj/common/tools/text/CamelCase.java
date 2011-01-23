package com.tcmj.common.tools.text;

/**
 * Converts strings to camel case conform strings and also java properties
 * to setter or getter methods.
 * @author Thomas Deutsch
 * @version $Revision: $
 */
public class CamelCase {

    /**
     * Converts any name to a java name (first letter capitalised).
     * idea from middlegen's dbnameconverter.
     * @param input eg.: junk_food
     * @return eg.: JunkFood
     */
    public static String toCamelCase(String input) {
        if ("".equals(input) || input == null) {
            return input;
        }
        StringBuilder sb = new StringBuilder();

        boolean capitalize = true;
        boolean lastCapital = false;
        boolean lastDecapitalized = false;
        String p = null;
        for (int i = 0; i < input.length(); i++) {
            String c = input.substring(i, i + 1);
            if ("_".equals(c) || " ".equals(c) || "-".equals(c)) {
                capitalize = true;
                continue;
            }

            if (c.toUpperCase().equals(c)) {
                if (lastDecapitalized && !lastCapital) {
                    capitalize = true;
                }
                lastCapital = true;
            } else {
                lastCapital = false;
            }

            //if(forceFirstLetter && result.length()==0) capitalize = false;

            if (capitalize) {
                if (p == null || !p.equals("_")) {
                    sb.append(c.toUpperCase());
                    capitalize = false;
                    p = c;
                } else {
                    sb.append(c.toLowerCase());
                    capitalize = false;
                    p = c;
                }
            } else {
                sb.append(c.toLowerCase());
                lastDecapitalized = true;
                p = c;
            }

        }
        return sb.toString();
    }

    /**
     * Converts a java property to a get method.
     * @param property input eg.: junk_food
     * @return 
     *  ALL_IN_UPPER_CASE = getAllInUpperCase</br>
     */
    public static String toGetter(String property) {
        return "get".concat(toCamelCase(property));
    }

    /**
     * Converts a java property to a set method.
     * @param property input eg.: junk_food
     * @return eg.: setJunkFood
     */
    public static String toSetter(String property) {
        return "set".concat(toCamelCase(property));
    }


}
