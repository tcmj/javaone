 /*
 * Created on 04.03.2009
 * Copyright(c) 2009 tcmj.  All Rights Reserved.
 * @author TDEUT - Thomas Deutsch - 2009
 */
package com.tcmj.pm.conflicts.bars;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Used to store conflicts. 
 * Extends the SimpleBar class and implements the Bar interface.
 * Last Modify: $Date: 2009/04/30 09:57:04 $ by $Author: TDEUT $
 * @version $Revision: 1.3 $
 * @author tdeut Thomas Deutsch 
 */
public class OutputBar extends SimpleBar {

    /** all bars which are in this conflict. */
    private List<Bar> lstBars = new ArrayList<Bar>();

    /** is this bar a conflict. */
    private final boolean conflict;


    /**
     * Constructor (calls super).
     * @param key - can be any identification 
     * @param start - Start Date of the Bar
     * @param end - Finish Date of the Bar
     * @param weight - Should be the weight sum of all Bars causing this conflict
     */
    public OutputBar(String key, Date start, Date end, double weight, boolean isconflict) {
        super(key, start, end, weight);
        this.conflict = isconflict;
    }


    /** adds an additional causing bar to this conflict item.     */
    public void addCausingBar(Bar bar) {
        this.lstBars.add(bar);
    }


    /** adds some tasks (Bars) to this conflict item.     */
    public void addCausingBars(List<Bar> barlist) {
        this.lstBars.addAll(barlist);
    }


    /**
     * Returns the list of bars which are causing the conflict.
     * @return list of Bar Items
     */
    public List<Bar> getCausingBars() {
        return this.lstBars;
    }


    /**
     * @return the conflict
     */
    public boolean isConflict() {
        return conflict;
    }
}
