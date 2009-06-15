/*
 * Merger.java
 * Created on 26. November 2005, 02:02
 */

package com.tcmj.custjar.mrg;

import com.tcmj.custjar.exc.CustJARException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Merger Interface.
 * @author Thomas Deutsch
 * @contact Thomas-Deutsch [at] tcmj [dot] de (2005 - 2009)
 */
public interface Merger {
    
    public void addJar(File file) throws CustJARException;
    
    public void setManifest(File file) throws CustJARException, IOException;
    
    public void setMainClass(String mainclass) throws CustJARException;
    
    public void putManifestAttribute(String key, String value);
    
    public void create() throws CustJARException, FileNotFoundException, IOException;
    
}
