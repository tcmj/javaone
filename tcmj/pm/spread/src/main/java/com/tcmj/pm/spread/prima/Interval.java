package com.tcmj.pm.spread.prima;

/**
 * Period.
 * 
 * @author twose
 * @version $Id$
 */
class Interval extends Range {

    /** Calculated value of this interval. */
    private final double mDuration;

    /** Calculated value of this interval. */
    private double       mValue;

    /**
     * Constructor.
     * 
     * @param pStartDate
     *                start date
     * @param pFinishDate
     *                finish date
     * @param pValue
     *                the value of the interval
     */
    public Interval(final long pStartDate, final long pFinishDate,
            final double pValue) {
        super(pStartDate, pFinishDate);
        mDuration = pFinishDate - pStartDate;
        mValue = pValue;
    }

    /**
     * @return the mDuration
     */
    double getDuration() {
        return mDuration;
    }

    /**
     * @return the value
     */
    public double getValue() {
        return mValue;
    }

    /**
     * @param value
     *                the value to set
     */
    void setValue(final double value) {
        mValue = value;
    }

}
