package com.tcmj.common.xml.map;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import com.tcmj.common.lang.Objects;
import com.tcmj.common.xml.map.intern.XMLEntry;
import com.tcmj.common.xml.map.intern.XMLMapDomAccessor;
import com.tcmj.common.xml.map.intern.XMLMapException;
import com.tcmj.common.xml.map.intern.XMLMapAccessor;

/**
 * This class is a standard java collections 'Map' implementation to work with XML files.
 * The data will be hel in the map whereas the key is the path/address of the xml element.
 *
 * <p>The key can consist of one or more levels (separated) and the value.<br/>
 * Both (key and value) are strings (to be able to write them as xml).<br/>
 * As a third part there is an option to append any java object to a key, which is useful for further processing in the
 * application logic. These additonal objects of course cannot be persisted to the physical file.<br/>
 * You can start from scratch to create new xml files or try to load existing ones, update some values and save them. <br/>
 * Another nice feature is to read only a specific part of an xml file by using a so called xml entry point.</p>
 *
 * <p><b>Simple Example</b><br>
 * <code>
 *  File file = new File("filename.xml");<br>
 *  XMLMap model = new XMLMap(file);<br>
 *  model.readXML();<br>
 *  model.put("dates", "lastrun", "1979-11-02");<br>
 *  model.saveXML();<br>
 * </code>
 * <p/>
 * <p>To investigate all the features please have a look at the unit tests !</p>
 * @author Thomas Deutsch - thomas-deutsch@tcmj.de
 * <p>
 * Created on 28. Mai 2005, 00:53
 * Modified on 16. April 2007, 23:53
 * Modified on 27. March 2008, 23:49
 * Modified on 28. December 2014, 23:50
 * Modified on 09. October 2015, 00:15
 */
public class XMLMap implements Map<String, String>, Serializable {

    /** Universal version identifier. */
    private static final long serialVersionUID = -4723949967478064084L;

    /** Default level separator (keys!). */
    public static final String DEFAULT_LEVEL_SEPARATOR = ".";

    /** The Data backed by a Map. */
    private Map<String, XMLEntry> data;

    /** The File handle of a XML-File. (does not have to exist). */
    private File xMLFileHandle;

    /** The Name of the root node (default = 'tcmj'). */
    private String xMLRootNodeName = "tcmj";

    /** The Name of the Roots child (default = 'xmlprop'). */
    private String xMLEntryPoint /*= "xmlmap"*/;

    /** Separator to concat levels to each other. Default = '.' */
    private String levelSeparator;

    /** compiled pattern to split the keys. */
    private Pattern rexpattern;

    /**
     * Creates a new instance of XMLMap.
     * @throws XMLMapException Exception if the Filehandle is null
     */
    public XMLMap() {
        this(new File(XMLMap.class.getSimpleName() + ".xml"));
    }

    /**
     * Creates a new instance of XMLMap.
     * @param xml File handle to a XML File (does not have to exist).
     */
    public XMLMap(final File xml) {
        this.data = new LinkedHashMap<>();
        this.xMLFileHandle = Objects.notNull(xml, "XML File handle cannot be null!");
        setLevelSeparator(DEFAULT_LEVEL_SEPARATOR);
    }

    /**
     * Constructor which takes a String passing it to the File constructor.
     * @param xmlFilePath which goes to: 'new XMLMap(new File(String)).
     */
    public XMLMap(final String xmlFilePath) {
        this(new File(Objects.notNull(xmlFilePath, "XML File name (String) may not be null!")));
    }

    /**
     * Adds a Property without a Java-Object.
     * This Property can be saved with {@link #saveXML }
     * @param key Key Name
     * @param value Value (Text)
     * @return the previous value associated with key, or null if there was no mapping for key.
     */
    @Override
    public String put(String key, String value) {
        Objects.notBlank(key, "Blank key not accepted!");
        validateEntry(key);

        //TODO do not allow control characters (whitespace only)
        if (value != null) {
            for (char toChar : value.toCharArray()) {
                if (Character.isISOControl(toChar)) {
                    throw new XMLMapException("Cannot handle control characters! Code: " + (int) toChar);
                }
            }
        }

        XMLEntry oldentry = data.put(key, new XMLEntry(key, value));
        return (oldentry == null) ? null : oldentry.getValue();
    }

    /**
     * Takes care about that every key has only one value on the lowest level.<br>
     * not allowed:<br> &lt;one&gt;12&lt;two&gt;55&lt;/two&gt;&lt;/one&gt;<br>
     * <b>case 1:</b><br><pre>
     * put("one","12");
     * put("one.two","55");</pre>
     * <b>case 2:</b><br><pre>
     * put("one.two","55");
     * put("one","12");
     * </pre>
     * @param keytocheck full path of a xml entry to check.
     * @throws XMLMapException if a
     * keytocheck allready has a value on any of its levels.
     */
    private void validateEntry(String keytocheck) throws XMLMapException {

        /* Fehlerbehandlung fuer Fall 1:
         * put("oben","11");
         * put("oben.unten","22");
         */
        String[] keyparts = rexpattern.split(keytocheck);

        String keypart = null;
        for (int x = 0; x < keyparts.length; x++) {

            if (x == 0) {
                keypart = keyparts[x];
            } else {
                keypart = keypart.concat(getLevelSeparator()).concat(keyparts[x]);
            }

            XMLEntry entry = data.get(keypart);
            if (entry != null && !entry.getKey().equals(keytocheck)) {
                throw new XMLMapException("Cannot add property '" + keytocheck
                        + "' because of allready existing higher node entry '" + entry.getKey() + "'");
            }
        }

        /* Fehlerbehandlung fuer Fall 2:
         * put("oben.unten","22");
         * put("oben","11");
         */
        Iterator<String> itdata = data.keySet().iterator();
        while (itdata.hasNext()) {

            String entrykey = itdata.next();

            /* Wenn der zu pruefende key ungleich dem aktuellem key ist.*/
            if (!entrykey.equals(keytocheck)) {

                /* Sonderfall (bezueglich startsWith):
                 * put("key1","xxx");
                 * put("key10","xxx");
                 * oder auch:
                 * put("one.key1","xxx");
                 * put("one.key10","xxx");
                 */

                /* wenn die Keys auf der gleichen Ebene liegen muessen sie nur
                 ungleich sein um angelegt werden zu koennen (valide zu sein)  */
                boolean samelevel = isSameLevel(entrykey, keytocheck);

                if (entrykey.startsWith(keytocheck) && !samelevel) {
                    throw new XMLMapException("Cannot add property '" + keytocheck
                            + "' because of allready existing lower node entry '" + entrykey + "'");
                }
            }
        }
    }

    /**
     * Checks if two keys are on the same level.
     * @param key1 first key to compare.
     * @param key2 second key to compare
     * @return true if the level is equal or false if not.
     */
    protected boolean isSameLevel(String key1, String key2) {

        //wenn die keys komplett gleich sind ist der pfad derselbe,
        //oder wenn beide keinen separator besitzen (root)
        if (key1.equals(key2)
                || (!key1.contains(getLevelSeparator()))
                && (!key2.contains(getLevelSeparator()))) {
            return true;
        } else {
            //..wenn nicht muss genau ermittelt werden:
            int firstsep_key1 = key1.indexOf(getLevelSeparator());
            int firstsep_key2 = key2.indexOf(getLevelSeparator());

            //beide liegen im obersten knoten:
            if (firstsep_key1 == -1 && firstsep_key2 == -1) {
                return true;
            } else {

                //wenn die separatoren an derselben stelle sind kann geschnippselt werden
                if (firstsep_key1 == firstsep_key2) {

                    String nlevel1 = key1.substring(0, firstsep_key1);
                    String nlevel2 = key2.substring(0, firstsep_key1);

                    //wenn die (vorderen) level gleich sind wird tiefergegangen:
                    if (nlevel1.equals(nlevel2)) {
                        String nkey1 = key1.substring(firstsep_key1 + 1);
                        String nkey2 = key2.substring(firstsep_key1 + 1);
                        return isSameLevel(nkey1, nkey2); //<--Rekursion
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
    }

    /**
     * Adds a Property with any Java-Object.
     * This Property can be saved to file with {@link #saveXML } but not
     * the java object!
     * @param key path to add (eg.: 'db.user')
     * @param value information to this key (eg.: 'admin')
     * @param object any Java Object
     */
    public void put(String key, String value, Object object) {
        XMLEntry entry = new XMLEntry(key, value, object);
        data.put(key, entry);
    }

    /**
     * Adds a Java Object to a specific key.<BR>
     * You can also use {@link #put(String,String,Object)}
     * To delete the object also use this method with null in the value parameter<BR>
     * @param key Keyname on which a java object shall be attached.
     * @param obj any java class instance
     */
    public void putObject(String key, Object obj) {
        XMLEntry entry = data.get(key);
        if (entry == null) {
            put(key, null, obj);
        } else {
            entry.setObject(obj);
        }
    }

    /**
     * Gets the Value of a specific key.
     * @param key full path to look for
     * @return the value or null if entry not found.
     */
    public String[] getListValue(String key) {
        XMLEntry entry = data.get(key);
        return (entry == null) ? null : entry.getListValue();
    }


    /**
     * Adds one ore more values to a key.<br>
     * This Property can be saved with {@link #saveXML }
     * @param key Key Name
     * @param values Value (Text)
     */
    public void putListValue(String key, String[] values) {
        XMLEntry entry = new XMLEntry(key, values[0]);
        data.put(key, entry);
        for (int i = 1; i < values.length; i++) {
            entry.addValue(values[i]);
        }
    }

    /**
     * Getter for the java object of a specific key.
     *
     * @param key full path to look for
     * @return Java Object - cast it (eg. Date date = (Date)getObject(...))
     */
    public Object getObject(String key) {
        XMLEntry entry = data.get(key);
        return entry == null ? null : entry.getObject();
    }

    /**
     * Getter for the java object of a specific value.<br>
     * <b>The first record with value equal to parameter will be returned</b>
     * @param value to look for
     * @return Java Object - cast it (eg. Date date = (Date)getObject(...))
     */
    public Object getObjectByValue(String value) {
        Iterator<Entry<String, XMLEntry>> itd = data.entrySet().iterator();
        Object obj = null;
        while (itd.hasNext()) {
            Map.Entry<String, XMLEntry> mapen = itd.next();
            XMLEntry xmlentry = mapen.getValue();
            if (value.equals(xmlentry.getValue())) {

                if (obj == null) { //first occurrence
                    obj = xmlentry.getObject();
                } else {
                    throw new XMLMapException("Value exists more than once! Cannot perform search by value!");
                }
            }
        }
        return obj;
    }

    /**
     * Removes a specific entry (key) from the model.
     * @param key name including full path of the key to remove.
     * @return the value of the removed entry or null if the key was not found.
     */
    @Override
    public String remove(Object key) {
        XMLEntry entry = data.remove(key);
        return entry == null ? null : entry.getValue();
    }

    private XMLMapDomAccessor accessor;

    private XMLMapAccessor getXMLAccessor() {
        if (accessor == null) {
            accessor = new XMLMapDomAccessor(getXMLRootNodeName(), getXMLEntryPoint(), getLevelSeparator(), this.rexpattern);
        }
        accessor.setLevelSeparator(getLevelSeparator());
        accessor.setXmlRootNodeName(getXMLRootNodeName());
        accessor.setXmlEntryPoint(getXMLEntryPoint());
        accessor.setRegexPattern(this.rexpattern);
        return accessor;
    }

    /**
     * Reads all Entries below the RootNode - ChildNode (xmlprop).
     */
    public void readXML() {
        this.data = getXMLAccessor().read(getXMLFileHandle());
    }

    /**
     * Saves the current Entries to file. <BR>
     * If a node (only under RootNode-ChildNode) exist in the Map but not in the
     * File, the Entry will be deleted from File! the file will be created if
     * neccessary
     *
     * @throws XMLMapException on any error
     */
    public void saveXML() throws XMLMapException {
        getXMLAccessor().save(this.data, getXMLFileHandle());
    }

    public String getAttribute(String key, String attribname) {
        XMLEntry entry = data.get(key);
        if (entry == null) {
            throw new XMLMapException("Entry not found for key: " + key);
        }
        Map<String, String> allattribs = entry.getAttributes();
        if (allattribs == null) {
            //throw new XMLMapException("No Attributes found for key: " + key);
            return null;
        }
        String attribvalue = allattribs.get(attribname);

        return attribvalue;
    }

    /**
     * Gets the attribute value of a specific key on multi type entries.
     * @param key full path to look for
     * @return the value or null if entry not found.
     */
    public String[] getListAttribute(String key, String attribname) {
        XMLEntry entry = data.get(key);
        if (entry == null) {
            throw new XMLMapException("Entry not found for key: " + key);
        }
        Map<String, String> allattribs = entry.getListAttributes();
        if (allattribs == null) {
            return null;
        }
        String attribvalue = allattribs.get(attribname);
        return attribvalue.split(mask("|"));
    }

    public void setAttribute(String key, String attribname, String value) {

        XMLEntry entry = data.get(key);

        if (entry == null) {
            throw new XMLMapException("Entry not found for key: " + key);
        }

        entry.addAttribute(attribname, value);
    }


    public String getComment(String key) {
        XMLEntry entry = data.get(key);
        if (entry == null) {
            throw new XMLMapException("Entry not found for key: " + key);
        }
        return entry.getComment();
    }

    public void setComment(String key, String value) {
        XMLEntry entry = data.get(key);
        if (entry == null) {
            throw new XMLMapException("Entry not found for key: " + key);
        }
        entry.setComment(value);
    }

    /** Clears all entries in the model.
     * if you save the model (after calling this method) you will get an empty
     * xmlfile area (non XMLMap - nodes will not be deleted)
     */
    @Override
    public void clear() {
        this.data.clear();
    }

    /**
     * Returns a String representation of all data entries.
     * @param showobjects true appends the stored java objects to the output
     * @param attributes true appends the stored xml attributes to the output
     * @return String representation of the entries
     */
    public String showDataEntries(boolean showobjects, boolean attributes) {

        if (this.data.isEmpty()) {
            return "No Entries available!";
        } else {
            int initialsize = data.size() * 50; //(Anzahl Einträge x 50 Zeichen pro Zeile
            StringBuilder buffer = new StringBuilder(initialsize);
            String line = System.getProperty("line.separator");
            buffer.append("XMLMap [").append(getXMLFileHandle()).append("]").append(line);

            Iterator<XMLEntry> itData = this.data.values().iterator();
            while (itData.hasNext()) {

                XMLEntry entry = itData.next();

                buffer.append(String.valueOf(entry));

                if (showobjects) {
                    String obj = String.valueOf(entry.getObject());
                    buffer.append(" O:[".concat(obj).concat("]"));
                }

                if (attributes) {
                    Map<String, String> map = entry.getAttributes();
                    if (map != null) {
                        buffer.append(" A:[").append(map.entrySet()).append("]");
                    }
                     map = entry.getListAttributes();
                    if (map != null) {
                        buffer.append(" A:").append(map.entrySet());
                    }
                }

                buffer.append(line);
            }
            return buffer.toString();
        }
    }

    /**
     * Returns the Name of the Rootnode of the XML File.
     * @return property xMLRootNodeName
     */
    public String getXMLRootNodeName() {
        return xMLRootNodeName;
    }

    /**
     * Sets the rootname to use if the XML File does not exist.<BR>
     * if the xml file already exists this method takes no effect!
     * @param root name of the rootnode to use
     */
    public void setXMLRootNodeName(String root) {
        this.xMLRootNodeName = root;
    }

    /**
     * Getter for property xMLEntryPoint.
     * @return name of the childnode under the rootnode
     */
    public String getXMLEntryPoint() {
        return xMLEntryPoint;
    }

    /**
     * Sets the Name of the child to be created under the Rootnode.
     * @param childnode Name of the childnode
     */
    public void setXMLEntryPoint(String childnode) {
        this.xMLEntryPoint = childnode;
    }

    /**
     * Gets the separator which is used to split the key.
     * @return separator
     */
    public String getLevelSeparator() {
        return levelSeparator;
    }

    /**
     * Sets the separator which is used to splitt the key.
     * In this method the regex pattern to split the keys is being compiled.
     * @param separator
     */
    public final void setLevelSeparator(String separator) {
        if (separator == null
                || separator.contains(":")) {
            throw new UnsupportedOperationException("Not allowed level separator: " + separator);
        }
        levelSeparator = separator;
        rexpattern = Pattern.compile(mask(separator));
    }

    /**
     * Gets the java file handle used by this model.
     * @return java.io.File object
     */
    public File getXMLFileHandle() {
        return this.xMLFileHandle;
    }

    public void setXMLFileHandle(File filehandle) {
        this.xMLFileHandle = filehandle;
    }

    /**
     * Getter for the Amount of all Entries.
     * @return the size of the Map
     */
    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    public boolean isEmpty() {
        return (size() == 0);
    }

    @Override
    public boolean containsKey(Object object) {
        return this.data.containsKey(object);
    }

    @Override
    public boolean containsValue(Object object) {
        Iterator<XMLEntry> itDataValues = this.data.values().iterator();
        while (itDataValues.hasNext()) {

            XMLEntry xmlEntry = itDataValues.next();

            if (xmlEntry.equals(object)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the Value of a specific key.
     * @param key full path to look for
     * @return the value or null if entry not found.
     */
    @Override
    public String get(Object key) {
        XMLEntry entry = data.get(key);
        return entry == null ? null : entry.getValue();
    }

    @Override
    public Set<String> keySet() {
        return this.data.keySet();
    }

    @Override
    public Collection<String> values() {
        Set<String> setValues = new LinkedHashSet<>();
        for (XMLEntry elem : this.data.values()) {
            setValues.add(elem.getValue());
        }
        return setValues;
    }

    @Override
    public Set<Map.Entry<String, String>> entrySet() {

        Set<Map.Entry<String, String>> setEntries = new LinkedHashSet<>();

        for (XMLEntry elem : this.data.values()) {

            Entry<String, String> entry = new XMLEntry(elem.getKey(), elem.getValue());

            setEntries.add(entry);
        }

        return setEntries;
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> stringmap) {
        for (Map.Entry<? extends String, ? extends String> entry : stringmap.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    private static String mask(final String value) {
        String rebuilt = value;
        if (value.contains(".")) {
            rebuilt = value.replaceAll("\\.", "\\\\.");
        }
        if (value.contains("*")) {
            rebuilt = value.replaceAll("\\*", "\\\\*");
        }
        if (value.contains("|")) {
            rebuilt = value.replaceAll("\\|", "\\\\|");
        }
        if (value.contains("?")) {
            rebuilt = value.replaceAll("\\?", "\\\\?");
        }
        if (value.contains("+")) {
            rebuilt = value.replaceAll("\\+", "\\\\+");
        }
        if (value.contains("\\")) {
            rebuilt = value.replaceAll("\\\\", "\\\\\\\\");
        }

        return rebuilt;
    }
}
