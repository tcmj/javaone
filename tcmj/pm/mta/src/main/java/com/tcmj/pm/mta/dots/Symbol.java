/*
 * Copyright(c) 2009 INTECO GmbH.
 * All Rights Reserved.
 */
package com.tcmj.pm.mta.dots;


/**
 * IMTASymbol enum 
 * @author tdeut - Thomas Deutsch
 */
public enum Symbol {

    DEFAULT_BY_ORDER("00","to be computed"),
    No_01("01", "dot_2.gif"),
    No_02("02", "dot_3.gif"),
    No_03("03", "dot_4.gif"),
    No_04("04", "dot_5.gif"),
    No_05("05", "dot_6.gif"),
    No_06("06", "dot_7.gif"),
    No_07("07", "dot_8.gif"),
    No_08("08", "dot_9.gif"),
    No_09("09", "dot_10.gif"),
    No_10("10", "dot_11.gif"),
    No_11("11", "dot_12.gif"),
    No_12("12", "dot_13.gif"),
    No_13("13", "dot_14.gif"),
    No_14("14", "dot_15.gif"),
    No_15("15", "dot_16.gif"),
    No_16("16", "dot_17.gif"),
    No_17("17", "dot_18.gif"),
    No_18("18", "dot_19.gif");

    String name;

    String fileName;


    Symbol(String name, String filename) {
        this.name = name;
        this.fileName = filename;
    }


    public String getName() {
        return this.fileName;
    }


    public String getFileName() {
        return this.fileName;
    }


    /**
     * returns an internal index of a symbol except for the
     * enum value 'DEFAULT_BY_ORDER' in that case the given parameter will be returned
     * @param seriesindx current number of the series (in a loop)
     * @return ordinal index or the given index
     */
    public int getIndex(int seriesindx) {
        if (this==DEFAULT_BY_ORDER) {
            return seriesindx;
        } else {
            return ordinal();
        }
    }

}
