/*
 * Created on 03.03.2009
 * Copyright(c) 2009 tcmj.  All Rights Reserved.
 * @author TDEUT - Thomas Deutsch - 2009
 */
package com.tcmj.pm.conflicts.bars;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a TimePeriod fragment with start and end date.
 * You can also set a weight (eg. jobtime).
 * Last Modify: $Date: 2009/04/02 10:07:05 $ by $Author: TDEUT $
 * @version $Revision: 1.2 $
 * @author tdeut Thomas Deutsch
 */
public class SimpleBar implements Bar {

    /** Key - id. (eg. ResourceAssignment Id). */
    private String key;

    /** Beginning date. */
    private Date startDate;

    /** Finishing date. */
    private Date endDate;

    /** Weight (default = 1.0). */
    private double weight = 1.0D;

    /** Properties of bar. can be used individually. Is not used in the algorithmus! */
    protected Map<String, Object> properties;


    /** Constructor with weight set to 100%. */
    public SimpleBar(String key, Date start, Date end) {
        this(key, start, end, 1.0D);
    }


    /** Constructor with the possibility to set a custom weight. */
    public SimpleBar(String key, Date start, Date end, double weight) {
        this.key = key;
        this.startDate = start;
        this.endDate = end;
        this.weight = weight;
    }


    @Override
    public String getKey() {
        return this.key;
    }


    @Override
    public Date getStartDate() {
        return this.startDate;
    }


    @Override
    public Date getEndDate() {
        return this.endDate;
    }


    @Override
    public double getWeight() {
        return this.weight;
    }


    public void setWeight(double weight) {
        this.weight = weight;
    }


    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }


    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }


    @Override
    public String toString() {
        return this.key;
    }


    @Override
    public Object getProperty(String name) {
        if (this.properties == null) {
            return null;
        } else {
            return this.properties.get(name);
        }

    }


    public Object setProperty(String name, Object value) {
        if (this.properties == null) {
            this.properties = new HashMap<String, Object>();
        }

        return this.properties.put(name, value);

    }


    public void setProperties(Map<String, Object> map) {
        if (map != null) {
            if (this.properties == null) {
                this.properties = new HashMap<String, Object>();
            }

            this.properties.putAll(map);
        }
    }


    @Override
    public Map<String, Object> getProperties() {
        return this.properties;
    }
}
