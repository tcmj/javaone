/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcmj.common.tools.text;

/**
 *
 * @author tcmj
 */
public class StringTool {

    /**
     * Converts a database name (table or column) to a java name (first letter capitalised).
     * employee_name -> EmployeeName.
     *
     * Derived from middlegen's dbnameconverter.
     * @param s The database name to convert.
     *
     * @return The converted database name.
     */
    public static String toUpperCamelCase(String s) {
        if ("".equals(s)) {
            return s;
        }
        StringBuffer result = new StringBuffer();

        boolean capitalize = true;
        boolean lastCapital = false;
        boolean lastDecapitalized = false;
        String p = null;
        for (int i = 0; i < s.length(); i++) {
            String c = s.substring(i, i + 1);
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
                    result.append(c.toUpperCase());
                    capitalize = false;
                    p = c;
                } else {
                    result.append(c.toLowerCase());
                    capitalize = false;
                    p = c;
                }
            } else {
                result.append(c.toLowerCase());
                lastDecapitalized = true;
                p = c;
            }

        }
        String r = result.toString();
        return r;
    }

}
