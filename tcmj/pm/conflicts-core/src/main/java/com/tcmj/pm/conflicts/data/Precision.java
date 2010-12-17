package com.tcmj.pm.conflicts.data;

import java.io.Serializable;

/**
 * Precision used for calculations.
 *  Precision    Weight    Result
 *  100          0.12345   123     - 3 digits
 *  1000         0.12345   1234    - 4 digits
 * @author Thomas Deutsch (2009)
 */
public class Precision implements Serializable {

    private static final long serialVersionUID = 1748160704424660872L;

    /** 2 digit precision. 0.123456 will be calculated as 0.12 */
    public static final Precision TWO_DIGITS = new Precision(100);

    /** 3 digit precision. 0.123456 will be calculated as 0.123 */
    public static final Precision THREE_DIGITS = new Precision(1000);

    /** 4 digit precision. 0.123456 will be calculated as 0.1234 */
    public static final Precision FOUR_DIGITS = new Precision(10000);

    /** 5 digit precision. 0.123456 will be calculated as 0.12345 */
    public static final Precision FIVE_DIGITS = new Precision(100000);

    /** Precision value.  */
    private final long value;


    /**
     * Constructs a new precision object.
     * @param precision eg 100 = 3 digits after the decimal point.
     */
    public Precision(long precision) {
        if (precision < 100) {
            throw new UnsupportedOperationException("Precision value must be at least 100!");
        }
        this.value = precision;
    }


    /**
     * Calculates a double value to an long value
     * This method is used for internal calculations only.
     * * Example (if precision = 1000L):
     *       input: 1.123456789  output: 1123
     * @param value eg.: 1.234321d
     * @return Math.round((double) value * (double) getValue());
     */
    public long double2Long(double value) {
        return Math.round((double) value * (double) getValue());
    }


    /**
     * Calculates an internal long value to an double value
     * This method is used for internal calculations only.
     * Example (if precision = 1000L):
     *       input: 1000  output: 1.0
     * @param value internal long value
     * @return (double) value / (double) getValue();
     */
    public double long2Double(long value) {
        return (double) value / (double) getValue();
    }


    /** getter for the internal value modifier.
     * @return the Precision
     */
    public long getValue() {
        return this.value;
    }


    @Override
    public String toString() {
        return String.valueOf(getValue());
    }
}
