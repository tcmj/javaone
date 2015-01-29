package com.tcmj.pm.spread.impl;

import com.tcmj.pm.common.unit.Range;

/**
 * SpreadPeriod is a {@link com.tcmj.pm.common.unit.Range} object containing
 * further fields for calculations.
 * @author tcmj
 * @version $Id$
 */
public class SpreadPeriod extends Range{

    /** Start date used for calculation purposes. */
    private long calculatedStart;

    /** Finish date used for calculation purposes. */
    private long calculatedEnd;

    /** Calculated value of this period. */
    private double[] mPeriodValues;

    /** Calculated value of this period. */
    private double[] mPeriodSingleValues;

    /** Flag indicating if period is inside timespan to be applied for curve. */
    private boolean mInsideTimespan;


    /**
     * Constructor.
     * @param start start date
     * @param finish finish date
     */
    public SpreadPeriod(long start, long finish) {
        super(start, finish);
        this.calculatedStart = start;
        this.calculatedEnd = finish;
    }

 
    long getCalcDuration() {
        return calculatedEnd - calculatedStart;
    }


    /**
     * @return the value
     */
    public double[] getPeriodValues() {
        return mPeriodValues;
    }


    /**
     * @return the value
     */
    public double[] getPeriodSingleValues() {
        return mPeriodSingleValues;
    }


    /**
     * Returns the first value in the value array.
     * If the value array is not initialized (null) or empty then 0 is returned
     * @return the value or 0 if empty
     */
    public double getPeriodValue() {
        if (mPeriodValues == null || mPeriodValues.length == 0) {
            return 0;
        }
        if (mPeriodValues.length == 1) {
            return mPeriodValues[0];
        } else {
            throw new RuntimeException("You cannot use this method if you have more than one total value!");
        }
    }


    /**
     * Returns the value in the value array of the given index.
     * If the value array is not initialized (null) or empty then 0 is returned
     * @param pIndex the index of the array
     * @return the value or 0 if empty
     */
    public double getPeriodValue(int pIndex) {
        if (mPeriodValues == null || mPeriodValues.length == 0) {
            return 0;
        }
        return mPeriodValues[pIndex];
    }


    /**
     * Returns the value in the value array of the given index.
     * If the value array is not initialized (null) or empty then 0 is returned
     * @param pIndex the index of the array
     * @return the value or 0 if empty
     */
    public double getPeriodSingleValue(int pIndex) {
        if (mPeriodSingleValues == null || mPeriodSingleValues.length == 0) {
//            throw new RuntimeException("No single value available for this period!");
            return 0;
        }
        return mPeriodSingleValues[pIndex];
    }


    /**
     * @param value the value to set
     */
    void setPeriodValues(double[] value) {
        this.mPeriodValues = value;
    }


    /**
     * @param value the value to set
     */
    void setPeriodSingleValues(double[] value) {
        this.mPeriodSingleValues = value;
    }


    /**
     * @param value the value to set
     */
    void setPeriodValue(int index, double value) {
//        if (this.mPeriodValues == null) {
//            throw new RuntimeException("periodvalues not initialized");
//        }
        this.mPeriodValues[index] = value;
    }


    /**
     * 
     * @param value the value to set
     */
    void setPeriodSingleValue(int index, double value) {
//        if (this.mPeriodSingleValues == null) {
//            throw new RuntimeException("single periodvalues not initialized");
//        }
        this.mPeriodSingleValues[index] = value;
    }


    /**
     * @return the mCalcStartDate
     */
    long getCalcStartDate() {
        return calculatedStart;
    }


    /**
     * @param mCalcStartDate the mCalcStartDate to set
     */
    void setCalcStartDate(long mCalcStartDate) {
        this.calculatedStart = mCalcStartDate;
    }


    /**
     * @return the mCalcFinishDate
     */
    long getCalcFinishDate() {
        return calculatedEnd;
    }


    /**
     * @param mCalcFinishDate the mCalcFinishDate to set
     */
    void setCalcFinishDate(long mCalcFinishDate) {
        this.calculatedEnd = mCalcFinishDate;
    }

    /**
     * Setzt im Falle einer Überlappung das Start-(1) oder Endedatum(2) auf den Wert des
     * übergebenen Range Objekts.
     * (1) Findet die Überlappung zu Beginn statt wird das Berechnungs-Start-Datum gesetzt
     * (2) Findet die Überlappung am Ende statt wird das Berechnungs-Ende-Datum gesetzt
     *
     * @param timeframe
     */
    protected void adjustIntersectionDate(Range timeframe) {


//                  xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
//        xxxxx  xxxxx  xxxxx  xxxxx  xxxxx  xxxxx  xxxxx  xxxxx

        if (overlapsBefore(timeframe)) {
            setCalcStartDate(timeframe.getStartMillis());
        }
        if (overlapsAfter(timeframe)) {
            setCalcFinishDate(timeframe.getEndMillis());
        }

        //TODO remove this:
        setInsideTimespan(overlaps(timeframe));
//        System.out.println(super.toString()+" is inside ["+range.getStartMillis()+","+range.getEndMillis()+"] ====> "+overlaps(range));

     

    }

public long calculatedOverlapDuration(long cstart, long cend) {
        if ((getCalcStartDate() < cend && cstart < getCalcFinishDate()) == false) {
            return 0;
        }
        return Math.min(calculatedEnd, cend) - Math.max(calculatedStart, cstart);
    }


    /**
     * @return the mInsideTimespan
     */
    boolean isInsideTimespan() {
        return mInsideTimespan;
    }


    /**
     * @param mInsideTimespan the mInsideTimespan to set
     */
    void setInsideTimespan(boolean mInsideTimespan) {
        this.mInsideTimespan = mInsideTimespan;
    }


    @Override
    public String toString() {
        if (mPeriodValues != null && mPeriodValues.length > 1) {
            StringBuilder result = new StringBuilder("[");
            for (int i = 0; i < mPeriodValues.length; i++) {
                if (i>0) {
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
