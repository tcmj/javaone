package com.tcmj.common.tools.text;

/**
 * String manipulations.
 * @author Thomas Deutsch
 * @version 1.0
 * @JUnit com.tcmj.common.tools.text.StringToolTest
 */
public class StringTool {

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
        StringBuffer sb = new StringBuffer();

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

    public static String join(String seperator, String[] strings) {
        int length = strings.length;
        if (length == 0) {
            return "";
        }
        StringBuffer buf = new StringBuffer(length * strings[0].length()).append(strings[0]);
        for (int i = 1; i < length; i++) {
            buf.append(seperator).append(strings[i]);
        }
        return buf.toString();
    }

    public static String toString(Object[] array) {
        int len = array.length;
        if (len == 0) {
            return "";
        }
        StringBuffer buf = new StringBuffer(len * 12);
        for (int i = 0; i < len - 1; i++) {
            buf.append(array[i]).append(", ");
        }
        return buf.append(array[len - 1]).toString();
    }

    public static boolean isNotEmpty(String string) {
        return string != null && string.length() > 0;
    }

    public static boolean isEmpty(String string) {
        return string == null || string.length() == 0;
    }

}
