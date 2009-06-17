/*
 * Created on 03.03.2009
 * Copyright(c) 2009 tcmj.  All Rights Reserved.
 * @author TDEUT - Thomas Deutsch - 2009
 */

package com.tcmj.pm.conflicts.data;

import com.tcmj.pm.conflicts.bars.Bar;

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
public class SimpleBar implements Bar{
    
    /** Key - id. (eg. ResourceAssignment Id). */
    private String _key;
    /** Beginning date. */
    private Date _startDate;
    /** Finishing date. */
    private Date _endDate;
    /** Weight (default = 1.0). */
    private double _weight = 1.0D;

//    /** Origin object (can be any java object eg.: Assignment, Task etc). */
//    private Object _origin;
    
    /** Properties of bar. can be used individually. Is not used in the algorithmus! */
    protected Map<String,Object> _properties;
    
    /** Constructor with weight set to 100%. */
    public SimpleBar(String key, Date start, Date end){
        this(key, start, end, 1.0D);
    }
    /** Constructor with the possibility to set a custom weight. */
    public SimpleBar(String key, Date start, Date end, double weight){
        _key = key;
        _startDate = start;
        _endDate = end;
        _weight = weight;
    }
        
    public String getKey() {
        return _key;
    }
    public Date getStartDate() {
        return _startDate;
    }
    public Date getEndDate() {
        return _endDate;
    }
//    @Override
//    public Object getOrigin() {
//        return _origin;
//    }
//    public void setOrigin(Object origin) {
//        this._origin = origin;
//    }
    public double getWeight() {
        return _weight;
    }
    public void setWeight(double weight) {
        this._weight = weight;
    }
    public void setStartDate(Date startDate) {
        this._startDate = startDate;
    }
    public void setEndDate(Date endDate) {
        this._endDate = endDate;
    }
   
    @Override
    public String toString() {
        return _key;
    }

    public Object getProperty(String name) {
        if (_properties == null) {
            return null;
        }else{
            return _properties.get(name);
        }
        
    }

    public Object setProperty(String name, Object value) {
        if (_properties == null) {
            _properties = new HashMap<String,Object>();
        }

        return _properties.put(name, value);
        
    }

     public void setProperties(Map<String,Object> map) {
      if (map != null) {
        if (_properties == null) {
            _properties = new HashMap<String,Object>();
        }

        _properties.putAll(map);
}
    }


    public Map<String,Object> getProperties() {
        return _properties;
    }

}
