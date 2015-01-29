package com.tcmj.pm.spread.prima;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Range.
 * 
 * @author tdeut - Thomas Deutsch
 * @version $Id$
 */
public abstract class Range {

    /** Original start date. */
    protected long mStartDate;
    /** Original finish date. */
    protected long mFinishDate;

    Range() {

    }

    /**
     * Constructor.
     * 
     * @param pStartDate
     *                start date
     * @param pFinishDate
     *                finish date
     */
    public Range(final long pStartDate, final long pFinishDate) {
        if (pStartDate > pFinishDate) {
            throw new RuntimeException("StartDate may not be after FinishDate!");
        }
        mStartDate = pStartDate;
        mFinishDate = pFinishDate;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Range other = (Range) obj;
        if (mStartDate != other.mStartDate) {
            return false;
        }
        if (mFinishDate != other.mFinishDate) {
            return false;
        }
        return true;
    }

    public long getFinishDate() {
        return mFinishDate;
    }

    long getOverlapDuration(final Range pRange) {

        // Period is completely outside (before or after) interval
        if (pRange.getStartDate() >= this.getFinishDate()
                || pRange.getFinishDate() <= this.getStartDate()) {
            return 0;
        }

        final List<Long> thelist = new ArrayList<Long>(4);
        thelist.add(mStartDate);
        thelist.add(mFinishDate);
        thelist.add(pRange.getStartDate());
        thelist.add(pRange.getFinishDate());
        Collections.sort(thelist);

        return Math.abs(thelist.get(2) - thelist.get(1));

        //        // Period is completely inside interval
        //        if (pRange.getStartDate() >= this.getStartDate()
        //                && pRange.getFinishDate() <= this.getFinishDate()) {
        //            return pRange.getCalcDuration();
        //        }
        //
        //        // Period is partly outside (before) interval
        //        if (pRange.getStartDate() < this.getStartDate()) {
        //            return pRange.getFinishDate() - this.getStartDate();
        //        } // Period is partly outside (after) interval
        //        else {
        //            return this.getFinishDate() - pRange.getStartDate();
        //        }

    }

    public long getStartDate() {
        return mStartDate;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + (int) (mStartDate ^ (mStartDate >>> 32));
        hash = 67 * hash + (int) (mFinishDate ^ (mFinishDate >>> 32));
        return hash;
    }
}
