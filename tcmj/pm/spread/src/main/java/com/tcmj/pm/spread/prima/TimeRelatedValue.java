package com.tcmj.pm.spread.prima;


public class TimeRelatedValue extends Range {

    /** The time related value */
    private double mValue;
    /** The array index of the value to be used for summing up the values */
    private int    mIndex;
    /** Start date used for calculation purposes. */
    private long   mCalcStartDate;
    /** Finish date used for calculation purposes. */
    private long   mCalcFinishDate;

    /**
     * @param pValue
     *                the time related value to set
     * @param pStartDate
     *                the start date of the timespan valid for the value
     * @param pFinishDate
     *                the finish date of the timespan valid for the value
     * @param pIndex
     *                the array index of the value to be used for summing up the
     *                values
     */
    public TimeRelatedValue(final double pValue, final long pStartDate,
            final long pFinishDate, final int pIndex) {
        mValue = pValue;
        mStartDate = pStartDate;
        mCalcStartDate = pStartDate;
        mFinishDate = pFinishDate;
        mCalcFinishDate = pFinishDate;
        mIndex = pIndex;
    }

    /**
     * @return the calculated duration (mCalcFinishDate - mCalcStartDate)
     */
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
     * @return the array index of the value to be used for summing up the values
     */
    public int getIndex() {
        return mIndex;
    }

    /**
     * @return the value referring to the defined timespan
     *         (pStartDate,pFinishDate)
     */
    public double getValue() {
        return mValue;
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
     * @param pIndex
     *                the array index of the value to be used for summing up the
     *                values
     */
    public void setIndex(final int pIndex) {
        mIndex = pIndex;
    }

    /**
     * @param pValue
     *                the value referring to the defined timespan
     *                (pStartDate,pFinishDate)
     */
    public void setValue(final double pValue) {
        mValue = pValue;
    }
}
