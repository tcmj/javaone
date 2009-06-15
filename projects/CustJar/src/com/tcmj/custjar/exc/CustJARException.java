/*
 * CustJARException.java
 * Created on 14. Juli 2005, 23:11
 */

package com.tcmj.custjar.exc;

/**
 * Custom Exception thrown on errors in extracting a Jarfile.
 * @author Thomas Deutsch
 * @contact Thomas-Deutsch [at] tcmj [dot] de (2005 - 2009)
 */
public class CustJARException extends Exception{
    
    /**
     * Creates a new instance of CustJARException 
     */
    public CustJARException(String msg) {
        super(msg);
    }
    
}
