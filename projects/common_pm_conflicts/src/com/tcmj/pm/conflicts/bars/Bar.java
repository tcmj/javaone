/*
 * Created on 03.03.2009
 * Copyright(c) 2009 tcmj.  All Rights Reserved.
 * @author TDEUT - Thomas Deutsch - 2009
 */

package com.tcmj.pm.conflicts.bars;

import java.util.Date;
import java.util.Map;

/**
 * Interface for a single Bar with Start- and EndDate. 
 * Last Modify: $WCDATE$ 
 * @version $WCREV$
 * char *Revision = "$WCREV$";
char *Modified = "$WCMODS?Geändert:Nicht geändert$";
char *Date     = "$WCDATE$";
char *RevRange = "$WCRANGE$";
char *Mixed    = "$WCMIXED?Gemischte Revisionen:Nicht gemischt$";
char *URL      = "$WCURL$";

 * @author tdeut Thomas Deutsch
 */
public interface Bar {

    /** Key (Id). */
    public String getKey();


    /** Beginning Date of the Bar. */
    public Date getStartDate();


    /** Finish Date of the Bar. */
    public Date getEndDate();


    /** Weight of the Bar. */
    public double getWeight();


    /** Getter of Properties of the Bar. (can be used individually). */
    public Object getProperty(String name);


    /** Getter of Properties of the Bar. (can be used individually). */
    public Map<String, Object> getProperties();

}
