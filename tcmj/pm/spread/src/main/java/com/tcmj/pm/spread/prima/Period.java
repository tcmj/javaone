package com.tcmj.pm.spread.prima;

/**
 * Period.
 * 
 * @author twose
 * @version $Id$
 */
public class Period extends Range implements Cloneable {

    /**
     * 
     */
    private static final long serialVersionUID = 860028331769081246L;
    /** Start date used for calculation purposes. */
    private long              mCalcStartDate;
    /** Finish date used for calculation purposes. */
    private long              mCalcFinishDate;
    /**
     * Calculated value(s) of this period, including summation, i.e., whenever
     * the method computeSpread is called, the values are added to the existing
     * values.
     */
    private double[]          mPeriodValues;
    /**
     * Calculated value(s) of this period, without summation, i.e., for each
     * call of the method computeSpread, the values are being reset to NaN.
     */
    private double[]          mPeriodSingleValues;
    /** Flag indicating if period is inside timespan to be applied for curve. */
    private boolean           mInsideTimespan;

    Period() {
    }

    /**
     * Constructor.
     * 
     * @param pStartDate
     *                start date
     * @param pFinishDate
     *                finish date
     */
    public Period(final long pStartDate, final long pFinishDate) {
        super(pStartDate, pFinishDate);
        mCalcStartDate = pStartDate;
        mCalcFinishDate = pFinishDate;
    }

    @Override
    public Period clone() {
        final Period p = new Period(mStartDate, mFinishDate);
        p.mCalcStartDate = mCalcStartDate;
        p.mCalcFinishDate = mCalcFinishDate;
        p.mInsideTimespan = mInsideTimespan;
        return p;
    }

    long getCalcDuration() {
        return mCalcFinishDate - mCalcStartDate;
    }

    /**
     * @return the mCalcFinishDate
     */
    long getCalcFinishDate() {
        return mCalcFinishDate;
    }

    /**
     * @return the mCalcStartDate
     */
    long getCalcStartDate() {
        return mCalcStartDate;
    }

    /**
     * Returns the value in the value array of the given index. If the value
     * array is not initialized (null) or empty then 0 is returned
     * 
     * @param pIndex
     *                the index of the array
     * @return the value or 0 if empty
     */
    public double getPeriodSingleValue(final int pIndex) {
        if (mPeriodSingleValues == null || mPeriodSingleValues.length == 0) {
            //            throw new RuntimeException("No single value available for this period!");
            return Double.NaN;
        }
        return mPeriodSingleValues[pIndex];
    }

    /**
     * @return the value
     */
    public double[] getPeriodSingleValues() {
        return mPeriodSingleValues;
    }

    /**
     * Returns the first value in the value array. If the value array is not
     * initialized (null) or empty then 0 is returned
     * 
     * @return the value or 0 if empty
     */
    public double getPeriodValue() {
        if (mPeriodValues == null || mPeriodValues.length == 0) {
            return Double.NaN;
        }
        if (mPeriodValues.length == 1) {
            return mPeriodValues[0];
        } else {
            throw new RuntimeException(
                    "You cannot use this method if you have more than one total value!");
        }
    }

    /**
     * Returns the value in the value array of the given index. If the value
     * array is not initialized (null) or empty then 0 is returned
     * 
     * @param pIndex
     *                the index of the array
     * @return the value or 0 if empty
     */
    public double getPeriodValue(final int pIndex) {
        if (mPeriodValues == null || mPeriodValues.length == 0) {
            return Double.NaN;
        }
        return mPeriodValues[pIndex];
    }

    /**
     * @return the value
     */
    public double[] getPeriodValues() {
        return mPeriodValues;
    }

    /**
     * @return the mInsideTimespan
     */
    boolean isInsideTimespan() {
        return mInsideTimespan;
    }

    /**
     * @param mCalcFinishDate
     *                the mCalcFinishDate to set
     */
    void setCalcFinishDate(final long mCalcFinishDate) {
        this.mCalcFinishDate = mCalcFinishDate;
    }

    /**
     * @param mCalcStartDate
     *                the mCalcStartDate to set
     */
    void setCalcStartDate(final long mCalcStartDate) {
        this.mCalcStartDate = mCalcStartDate;
    }

    /**
     * @param mInsideTimespan
     *                the mInsideTimespan to set
     */
    void setInsideTimespan(final boolean mInsideTimespan) {
        this.mInsideTimespan = mInsideTimespan;
    }

    /**
     * @param value
     *                the value to set
     */
    void setPeriodSingleValue(final int index, final double value) {
        if (mPeriodSingleValues == null) {
            throw new RuntimeException("single periodvalues not initialized");
        }
        mPeriodSingleValues[index] = value;
    }

    /**
     * @param value
     *                the value to set
     */
    void setPeriodSingleValues(final double[] value) {
        mPeriodSingleValues = value;
    }

    /**
     * @param value
     *                the value to set
     */
    public void setPeriodValue(final int index, final double value) {
        if (mPeriodValues == null) {
            throw new RuntimeException("periodvalues not initialized");
        }
        mPeriodValues[index] = value;
    }

    /**
     * @param value
     *                the value to set
     */
    public void setPeriodValues(final double[] value) {
        mPeriodValues = value;
    }

    @Override
    public String toString() {
        if (mPeriodValues != null && mPeriodValues.length > 1) {
            final StringBuilder result = new StringBuilder("[");
            for (int i = 0; i < mPeriodValues.length; i++) {
                if (i > 0) {
                    result.append(", ");
                }
                result.append(mPeriodValues[i]);
            }
            result.append("]");
            return result.toString();
        } else {
            return String.valueOf(getPeriodValue());
        }
    }
}
