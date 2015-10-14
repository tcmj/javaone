/*
 * Created on 03.03.2009
 * Copyright(c) 2009 tcmj.  All Rights Reserved.
 * @author TDEUT - Thomas Deutsch - 2009
 */
package com.tcmj.pm.conflicts.data;

import java.util.Date;

import com.tcmj.pm.conflicts.bars.Bar;

/**
 * SortItem.
 * Last Modify: $Date: 2009/04/30 09:57:04 $ by $Author: TDEUT $
 * @version $Rev$
 * @author tdeut Thomas Deutsch
 */
public class SortItem implements Comparable<SortItem> {

    public enum SortItemType {
        STARTITEM, ENDITEM
    };

    /** Type. (STARTITEM or ENDITEM). */
    private final SortItemType _type;

    /** original input item. */
    private final Bar _baritem;

    /** start or end date depending on item-type. */
    private Date _date;

    /** weight of the current item. */
    private long _weight;


    /**
     * Constructor.
     * @param type Start or End Item
     * @param weight as long
     * @param baritem Bar item
     */
    public SortItem(SortItemType type, long weight, Bar baritem) {
        _type = type;
        _baritem = baritem;

        if (SortItemType.STARTITEM == type) {
            _date = baritem.getStartDate();
        } else if (SortItemType.ENDITEM == type) {
            _date = baritem.getEndDate();
        } else {
            throw new UnsupportedOperationException("given parameter type is not supported!");
        }
        this._weight = weight;
//        setWeight(baritem.getWeight());
    }


    /**
     * Getter of the type of the item.
     * @return SortItemType enum
     */
    public SortItemType getType() {
        return _type;
    }


    /** A Baritem always consists of SortItems (one start and one end).
     * @return the Bar Item of this SortItem
     */
    public Bar getBaritem() {
        return _baritem;
    }


    /**
     * Getter for the date of this object.
     * @return java.util.Date
     */
    public Date getDate() {
        return _date;
    }


    /** Returns the internal used long value. (dependent of precision!)
     * @return internal long value
     */
    public long getWeightAsLong() {
        return _weight;
    }


    /** String representation of this object.
     * @return eg. S:KEY:1979-02-11
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(31);

        if (SortItemType.ENDITEM == _type) {
            sb.append('F');
        } else {
            sb.append('S');
        }
        sb.append(':');
        sb.append(_baritem.getKey());


        return sb.toString();
    }


    /**
     * Compare a given SortItem (=A or E Date) with this object.
     * @param other the other SortItem to compare to
     * @return this.getDate().compareTo(other.getDate())
     */
    public int compareTo(SortItem other) {
        return this.getDate().compareTo(other.getDate());
    }


    @Override
    public boolean equals(Object obj) {
        return obj instanceof SortItem
                && getDate().getTime() == ((SortItem) obj).getDate().getTime();
    }


    @Override
    public int hashCode() {
        return getDate().hashCode();
    }
}
