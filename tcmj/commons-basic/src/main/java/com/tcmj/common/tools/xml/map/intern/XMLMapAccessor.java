package com.tcmj.common.tools.xml.map.intern;

import java.io.File;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Interface used to implement a xml reader and writer.
 * @author tcmj
 */
public interface XMLMapAccessor {


    public String getXMLEntryPoint();

    public String getXMLRootNodeName();

    public String getXMLLevelSeparator();

    public Pattern getRegexPattern();

    public Map<String, XMLEntry> read(File xmlFile) throws XMLMapException;

    public void save(Map<String, XMLEntry> data, File outputfile) throws XMLMapException;

}
