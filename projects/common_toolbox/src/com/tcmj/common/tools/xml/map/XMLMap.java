/*
 * Created on 28. Mai 2005, 00:53
 * Modified on 16. April 2007, 23:53
 * Modified on 27. March 2008, 23:49
 * CVS: $Id$
 * Last Modify: $Date: $
 */
package com.tcmj.common.tools.xml.map;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * This class is a Map implementation for a XML-File.
 * The data will be stored in a map like structure where
 * the key can consist of one or more levels (separated) and the value.
 * Both (key and value) are strings (to write them into xml).
 * As a third part there is an option to append any java object to a key.
 * The XML File will be created if necessary or you can use an existing xml file
 * where you specify the node name into which your data will be stored. <br><br>
 * <b>Example</b><br>
 * <code>
 *  File file = new File("filename.xml");<br>
 *  XMLMap model = new XMLMap(file);<br>
 *  model.readXML();<br>
 *  model.put("dates", "lastrun", "1979-11-02");<br>
 *  model.saveXML();<br>
 * </code>
 * @author Thomas Deutsch - thomas-deutsch@tcmj.de
 * @JUnit Test available!
 */
public class XMLMap implements Map<String, String>, Serializable {

    /** Universal version identifier. */
    private static final long serialVersionUID = -4723949967478064084L;

    /** slf4j Logging framework. */
    private static final transient Logger logger = LoggerFactory.getLogger(XMLMap.class);
    
    
    /** The Data backed by a Map. */
    private Map<String, XMLEntry> data;

    /** The File handle of a XML-File. (does not have to exist). */
    private File xMLFileHandle;
    /** The Name of the root node (default = 'tcmj'). */
    private String xMLRootNodeName = "tcmj";
    /** The Name of the Roots child (default = 'xmlprop'). */
    private String xMLEntryPoint = "xmlprop";
    
    /** XML Document. */
    private Document document = null;


    
    /** Separator to concat levels to each other. Default = '.'  */
    private String levelSeparator;

    /** compiled pattern to split the keys. */
    private Pattern rexpattern;
    
    /**
     * Creates a new instance of XMLMap.
     * @throws XMLMapException Exception if the Filehandle is null
     */
    public XMLMap() {
        this(new File(XMLMap.class.getSimpleName()+".xml"));
    }

    /**
     * Creates a new instance of XMLMap.
     * @param xml File handle to a XML File (does not have to exist).
     */
    public XMLMap(final File xml){

        if (xml == null) {
            throw new XMLMapException("XML File handle cannot be null!");
        }
        
        //this.data = new java.util.TreeMap<String, XMLEntry>();
//        this.data = new java.util.HashMap<String, XMLEntry>();
        this.data = new java.util.LinkedHashMap<String, XMLEntry>();

        this.xMLFileHandle = xml;


        setLevelSeparator(".");
    
    }

    /**
     * Adds a Property without a Java-Object.
     * This Property can be saved with {@link #saveXML }
     * @param key Key Name
     * @param value Value (Text)
     */
    public String put(String key, String value) {

        if (key == null) {
            throw new XMLMapException("Null not accepted as key!");
        }
        
        validateEntry(key);

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
     *         keytocheck allready has a value on any of its levels.
     */
    private void validateEntry(String keytocheck)
            throws XMLMapException {

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
                throw new XMLMapException("Cannot add property '" + keytocheck +
                        "' because of allready existing higher node entry '" + entry.getKey() + "'");
            }
        }

        /* Fehlerbehandlung fuer Fall 2:
         * put("oben.unten","22");
         * put("oben","11"); 
         */
        Iterator<String> itdata = data.keySet().iterator();
        while (itdata.hasNext()) {

            String entrykey = itdata.next();

            /* Wenn der zu prüfende key ungleich dem aktuellem key ist.*/
            if (!entrykey.equals(keytocheck)) {

                /* Sonderfall (bezüglich startsWith):
                 * put("key1","xxx");
                 * put("key10","xxx");
                 * oder auch:
                 * put("one.key1","xxx");
                 * put("one.key10","xxx");
                 */

                /* wenn die Keys auf der gleichen Ebene liegen müssen sie nur
                ungleich sein um angelegt werden zu können (valide zu sein)  */
                boolean samelevel = isSameLevel(entrykey, keytocheck);

                if (entrykey.startsWith(keytocheck) && !samelevel) {
                    throw new XMLMapException("Cannot add property '" + keytocheck +
                            "' because of allready existing lower node entry '" + entrykey + "'");
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
        if (key1.equals(key2) ||
                (key1.indexOf(getLevelSeparator()) == -1) &&
                (key2.indexOf(getLevelSeparator()) == -1)) {
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
     * You can also use {@link #put(String,String,Object) }
     * <BR><B>Note</B><BR>
     * To update the value (text) use {@link put(String,String,String)}
     * with the specific key<BR>
     * To delete the object also use this method with null in the value parameter<BR>
     *
     * @param key Keyname on which a java object shall be attached.
     * @param obj any java class instance
     */
    public void putObject(String key, Object obj){

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
     * Adds a list of Properties (without a Java-Object).
     * This Property can be saved with {@link #saveXML }
     * @param key Key Name
     * @param values Value (Text)
     */
    public void putListValue(String key, String[] values){

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
        Iterator itd = data.entrySet().iterator();
        Object obj = null;
        while (itd.hasNext()) {
            Map.Entry mapen = (Map.Entry) itd.next();

            XMLEntry xmlentry = (XMLEntry) mapen.getValue();

            if (value.equals(xmlentry.getValue())) {
                
                if(obj==null) { //first occurence
                    obj = xmlentry.getObject();
                }else{
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
    public String remove(Object key) {
        XMLEntry entry = data.remove(key);
        return entry == null ? null : entry.getValue();
    }

    /**
     * Reads all Entries below the RootNode - ChildNode (xmlprop).
     */
    public void readXML() {


        logger.debug("START reading: " + xMLFileHandle);


        this.data.clear();

        if (xMLFileHandle.exists()) {

//            Document document = null;
            Element root = null;

            try {

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();


                //TODO make global - do not read before save!!!
                document = builder.parse(xMLFileHandle);
                root = document.getDocumentElement();
            } catch (Exception ex) {
                logger.error("Error reading XML: " + ex.getMessage());
            }

            if (root != null) {

                for (Node uppernode = root.getFirstChild(); uppernode != null;
                        uppernode = uppernode.getNextSibling()) {

                    //<root>.<XMLEntryPoint>
                    if ((uppernode.getNodeType() == Node.ELEMENT_NODE)) {

                        if (uppernode.getNodeName().equals(getXMLEntryPoint())) {

                            for (Node groupchild = uppernode.getFirstChild(); groupchild != null;
                                    groupchild = groupchild.getNextSibling()) {


                                if ((groupchild.getNodeType() == Node.ELEMENT_NODE)) {


                                    String groupname = groupchild.getNodeName();

                                    logger.debug("ELEMENT_NODE: " + groupname);



                                    deeper(groupchild, groupname);

                                }


                            }
                        }
                    }
                }
            }
        }

    }

    public String getAttribute(String key, String attribname) {
        
        XMLEntry entry = data.get(key);
        
        if(entry==null) {
            throw new XMLMapException("Entry not found for key: "+key);
        }
        
        Map<String, String> allattribs = entry.getAttributes();
        if(allattribs==null) {
            throw new XMLMapException("No Attributes found for key: "+key);
        }
        String attribvalue = allattribs.get(attribname);
        
        return attribvalue;
        
        
        
    }

    public void setAttribute(String key, String attribname, String value) {
        
        XMLEntry entry = data.get(key);
        
        if(entry==null) {
            throw new XMLMapException("Entry not found for key: "+key);
        }
        
        entry.addAttribute(attribname, value);
         
        
        
    }
    
    
    
    
    /** Iterates recursive throug the XML Nodes.
     * @param parent Parent Node
     */
    private void deeper(Node node, String path) {

        Node child = node.getFirstChild();

        if (child != null) {

            String value = child.getNodeValue();

            if (value != null && !"".equals(value.trim())) {


                XMLEntry entry = data.get(path);

                if (entry == null) {
                    entry = new XMLEntry(path, value);
                    this.data.put(path, entry);
                } else {
                    entry.addValue(value);
                }
 
                
                parseAttributes(node, entry);
                
                
                return;
            } else {
                
                logger.debug("going deeper above "+path+"..."+value);
                
                while (child != null) {

                    if (child.getNodeType() == Node.ELEMENT_NODE) {

                        
                        String extendedpath 
                                = path.concat(getLevelSeparator()).concat(child.getNodeName());

                        deeper(child, extendedpath);
                    } else if (child.getNodeType() == Node.CDATA_SECTION_NODE) {
                        logger.debug("CDATA_SECTION_NODE = " + child.getNodeValue());
                        XMLEntry entry = data.get(path);

                        if (entry == null) {
                            entry = new XMLEntry(path, child.getNodeValue());
                            this.data.put(path, entry);
                        } else {
                            entry.addValue(child.getNodeValue());
                        }

                        entry.setXmlNodeType(child.getNodeType());

                    }


                    child = child.getNextSibling();
                }
            }
        } else {
            
            logger.debug("!!!! no childs: "+path); 
            XMLEntry entry = new XMLEntry(path, node.getNodeValue());
            parseAttributes(node, entry);
            this.data.put(path, entry);
            
            return;
        }
    }
    
    
    
    private void parseAttributes(Node node, XMLEntry entry) {

        if (node.hasAttributes()) {

            NamedNodeMap nnm = node.getAttributes();

            for (int index = 0; index < nnm.getLength(); index++) {

                Node attnode = nnm.item(index);

                logger.debug("reading attribute of element '" + node + "': " + attnode.getNodeName() + " = " + attnode.getNodeValue());

                entry.addAttribute(attnode.getNodeName(), attnode.getNodeValue());

            }
        }
    }


    /**
     * Saves the current Entries to file. <BR>
     * If a node (only under RootNode-ChildNode) exist in the Map
     * but not in the File, the Entry will be deleted from File!
     * @throws java.io.FileNotFoundException the file will be created if neccessary
     * @throws javax.xml.transform.TransformerConfigurationException xml error
     * @throws java.io.UnsupportedEncodingException characterset error
     * @throws javax.xml.transform.TransformerException xml error
     * @throws java.io.IOException Input Output Errors input/output failure
     * @throws ParserConfigurationException ParserConfiguration
     */
    public void saveXML()
            throws FileNotFoundException, TransformerConfigurationException,
            UnsupportedEncodingException, TransformerException, IOException, ParserConfigurationException {

        saveXML(this.xMLFileHandle);

    }



    
    private void saveXML(File outputfile)
            throws FileNotFoundException, TransformerConfigurationException,
            UnsupportedEncodingException, TransformerException, IOException, ParserConfigurationException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();


        Element root = null;


        if (document == null) {
            document = builder.newDocument();
            root = document.createElement(xMLRootNodeName);
            document.appendChild(root);
        } else {
            root = document.getDocumentElement();
        }




        //Ermittle ob dieser Knoten ein Kind namens <xMLEntryPoint> hat:
        Node uppernode = root.getFirstChild();

        while ((uppernode != null) && (!getXMLEntryPoint().equals(uppernode.getNodeName()))) {
            uppernode = uppernode.getNextSibling();
        }

        //<tcmj><xmlprop>
        if (uppernode != null) { //wenn er ein Kind namens <xMLEntryPoint> hat...
            Node prevnode = uppernode.getPreviousSibling();

            //...löschen:
            if (prevnode.getNodeType() == Node.TEXT_NODE) {
                root.removeChild(prevnode);
            }

            root.removeChild(uppernode);

        }

        //Element <xMLEntryPoint> anlegen (default=<xmlprop>)
        uppernode = document.createElement(xMLEntryPoint);
        root.appendChild(uppernode);

        //Daten durchlaufen...
        Iterator<XMLEntry> itData = data.values().iterator();

        while(itData.hasNext()) {

            XMLEntry xmlentry = itData.next();


            //Variable fuer die unterste Ebene:
            Node actualnode = uppernode, prevnode = null;

            //Key zerteilen:
            String[] keyparts = rexpattern.split(xmlentry.getKey());


            String keypart = null;

            //Schleife durch alle parts des keys, wobei nicht vorhandene ebenen
            //angelegt werden und bereits vorhandene übersprungen!
            //Zudem wird immer der aktuelle Knoten (actnode) und der Vorgänger
            //gespeichert (Vorgänger wird benötigt bei Listen-Values)
            for (int x = 0; x < keyparts.length; x++) {

                keypart = keyparts[x];

                //Key-Part suchen:
                Node ngroup = searchNode(keypart, actualnode);

                //wenn nicht gefunden --> anlegen:
                if (ngroup == null) {
                    ngroup = document.createElement(keypart);
                }

                actualnode.appendChild(ngroup);
                prevnode = actualnode;
                actualnode = ngroup;

            }

            Element element = (Element) actualnode;


            //Attribute anlegen
            Map<String, String> allattribs = xmlentry.getAttributes();
            if (allattribs != null) {

                for (Map.Entry<String, String> aentry : allattribs.entrySet()) {

                    element.setAttribute(aentry.getKey(), aentry.getValue());

                }

            }

            //Value oder Values anlegen:
            if (xmlentry.getValue() != null) {

                
                if (xmlentry.getXmlNodeType() == Node.CDATA_SECTION_NODE) {
                    actualnode.appendChild(document.createCDATASection(xmlentry.getValue()));
                } else {
                    actualnode.appendChild(document.createTextNode(xmlentry.getValue()));
                }
                
                
            } else {

                String[] values = xmlentry.getListValue();

                String val = values == null ? null : values[0];


                if (values != null) {
                    actualnode.appendChild(document.createTextNode(val));

                    for (int ix = 1; ix < values.length; ix++) {

                        Node ngroup = document.createElement(keypart);

                        prevnode.appendChild(ngroup);

                        ngroup.appendChild(document.createTextNode(values[ix]));

                    }
                }
            }
        }

        FileOutputStream outpt = new FileOutputStream(outputfile);
        try {


            TransformerFactory tfactory = TransformerFactory.newInstance();
            tfactory.setAttribute("indent-number", 2);
            Transformer transformer = tfactory.newTransformer();

            try {

                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                transformer.setOutputProperty(OutputKeys.MEDIA_TYPE, "text/xml");
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
            } catch (Exception ex) {
                logger.warn("Not supported Attribute in writing XML: " + ex.getMessage());
            }

            transformer.transform(new DOMSource(document), new StreamResult(new OutputStreamWriter(outpt, "UTF-8")));

            outpt.flush();
            outpt.getFD().sync();
            outpt.close();

        } catch (IOException ioe) {
            outpt.close();
            throw ioe;
        }

    }

    /**
     * Internal Method to search for a specific xml node.
     * @param name node name to search for
     * @param parent parent to search under
     * @return the node
     */
    private static Node searchNode(String name, Node parent) {
        Node actnode = parent.getFirstChild();
        while ((actnode != null) && (!name.equals(actnode.getNodeName()))) {
            if (name.equals(actnode.getNodeName())) {
                return actnode;
            } else {
                actnode = actnode.getNextSibling();
            }
        }
        return actnode;
    }

    /** Clears all entries in the model.
     * if you save the model (after calling this method) you will get an empty
     * xmlfile area (non XMLMap - nodes will not be deleted)
     */
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
            StringBuffer buffer = new StringBuffer(initialsize);
            String line = System.getProperty("line.separator");
            Iterator itData = this.data.values().iterator();
            while (itData.hasNext()) {

                XMLEntry entry = (XMLEntry) itData.next();
                
                buffer.append(String.valueOf(entry));

                if (showobjects) {
                    String obj = String.valueOf(entry.getObject());
                    buffer.append(" O:[".concat(obj).concat("]"));
                }
                
                if (attributes) {
                    Map map = entry.getAttributes();
                    if(map!=null){
                        buffer.append(" A:["+map.entrySet()+"]");
                    } 
                    
                }

                buffer.append(line);
            }
            return buffer.toString();
        }
    }

    /**
     * Exception which was thrown on Class specific Errors
     */
    public static class XMLMapException extends RuntimeException {
        private static final long serialVersionUID = 5244742674279170273L;

        /**
         * Constructor which creates a new Exception.
         * @param message Errormessage
         * @param cause Throwable
         */
        public XMLMapException(String message, Throwable cause) {
            super(message, cause);
        }

        /**
         * Constructor which creates a new Exception.
         * @param msg Errormessage
         */
        public XMLMapException(String msg) {
            super(msg);
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
     * Entry of the XML Document.
     * contains key, value as String
     * @author tcmj - Thomas Deutsch - tdeut
     */
    static final class XMLEntry implements Map.Entry<String, String>, Serializable {
        private static final long serialVersionUID = -3974837921466840353L;
        private static final int TYPE_SINGLE = 0x0;
        private static final int TYPE_MULTI = 0x1;
        private int type = TYPE_SINGLE;

        private int xmlNodeType;
        
        /** Key */
        private final String key;
        /** Value */
        private String[] value;
        /** Java Object */
        private Object[] object;

        /** Attributes */
        private Map<String, String> attributes;
        
        
        /**
         * Constructor 1.
         * @param key keyname
         * @param val string value
         */
        public XMLEntry(String key, String val) {
            this.key = key;
            this.object = new Object[1];
            this.value = new String[]{val};
        }

        /**
         * Constructor 2.
         * @param key key name
         * @param val string value
         * @param obj java object
         */
        public XMLEntry(String key, String val, Object obj) {
            this.key = key;
            this.value = new String[]{val};
            this.object = new Object[]{obj};
        }

        /**
         * Getter for Property Key.
         * @return Keyname
         */
        public String getKey() {
            return this.key;
        }

        /**
         * Getter for Property Value.
         * @return Value
         */
        public String getValue() {

            if (type == TYPE_SINGLE) {
                return this.value[0];
            }

            return null;
        }

        /**
         * Getter for Property Value.
         * @return Value
         */
        public String[] getListValue() {
            if (type == TYPE_MULTI) {
                String[] copy = new String[this.value.length];
                System.arraycopy(this.value, 0, copy, 0, this.value.length);
                return copy;
            }
            return null;
        }

        /**
         * String representation of this Entry.
         * @return String concatenation key and value
         */
        @Override
        public String toString() {
            return "K:[".concat(key).concat("] V:").concat(String.valueOf(Arrays.asList(value))).concat(" ");
        }

        /**
         * Getter for the Object Property.
         * @return the Java Object which was added before
         */
        public Object getObject() {
            if (type == TYPE_SINGLE) {
                return this.object[0];
            }
            return null;
        }

        /**
         * Sets the Java Object of this Entry.
         * @param obj any instance of a java object
         */
        public void setObject(Object obj) {
            if (type == TYPE_SINGLE) {
                this.object[0] = obj;
            }

        }

        public String setValue(String val) {
            String old = this.value[0];
            if (type == TYPE_SINGLE) {
                this.value[0] = val;
            }else{
                throw new UnsupportedOperationException("not allowed for single type");
            }
            return old;
        }

        public void addValue(String val) {

            type = TYPE_MULTI;

            int newsize = this.value.length + 1;

            String nvalue[] = new String[newsize];
            Object nobject[] = new Object[newsize];

            for (int i = 0; i < this.value.length; i++) {
                nvalue[i] = value[i];
                nobject[i] = object[i];
            }
            nvalue[newsize - 1] = val;

            this.value = nvalue;
            this.object = nobject;

        }

        public void addAttribute(String name, String value) {

            if (this.attributes == null) {
                this.attributes = new HashMap();
            }
            this.attributes.put(name, value);

        }
        
        
        public Map<String, String> getAttributes() {
            return this.attributes;
        }
        
        
        

        @Override
        public int hashCode() {
            return (key==null   ? 0 : key.hashCode()) ^
                   (value==null ? 0 : value.hashCode());
        }
        
        
        @Override
        public boolean equals(Object no2) {

            if (!(no2 instanceof XMLEntry) && !(no2 instanceof String)) {
                return false;
            } else {

                if (no2 instanceof XMLEntry) {

                    XMLEntry obj = (XMLEntry) no2;

                    if (obj.key == null || this.key.equals(obj.getKey())) {

                        if (this.value.length != obj.value.length) {
                            return false;
                        } else {

                            if (!this.value[0].equals(obj.value[0])) {
                                return false;
                            } else {

                                for (int i = 1; i < value.length; i++) {
                                    if (!this.value[i].equals(obj.value[i])) {
                                        return false;
                                    }
                                }

                                return true;
                            }

                        }

                    } else {
                        return false;
                    }

                } else {

                    String obj2 = (String) no2;
                    return this.value[0].equals(obj2);
                }
            }

        }

        /**
         * @return the xmlNodeType
         */
        public int getXmlNodeType() {
            return xmlNodeType;
        }

        /**
         * @param xmlNodeType the xmlNodeType to set
         */
        public void setXmlNodeType(int xmlNodeType) {
            this.xmlNodeType = xmlNodeType;
        }
    }//end of inner class: XMLEntry

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
        levelSeparator = separator;
        rexpattern = Pattern.compile( mask(separator) );
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
    public int size() {
        return this.data.size();
    }
    
    public boolean isEmpty() {
        return (size() == 0);
    }

    public boolean containsKey(Object object) {
        return this.data.containsKey(object);
    }

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
    public String get(Object key) {
        XMLEntry entry = data.get(key);
        return entry == null ? null : entry.getValue();
    }
    

    public Set<String> keySet() {
        return this.data.keySet();
    }

    public Collection<String> values() {

        Set<String> setValues = new LinkedHashSet<String>();

        for (XMLEntry elem : this.data.values()) {

            setValues.add(elem.getValue());
        }

        return setValues;
    }

    public Set<Map.Entry<String, String>> entrySet() {

        Set<Map.Entry<String, String>> setEntries = new LinkedHashSet<Map.Entry<String, String>>();

        for (XMLEntry elem : this.data.values()) {

            Entry<String, String> entry = new XMLEntry(elem.getKey(), elem.getValue());

            setEntries.add(entry);
        }

        return setEntries;
    }

    public void putAll(Map stringmap) {

        Iterator<Map.Entry<String, String>> itentryset = stringmap.entrySet().iterator();
        while (itentryset.hasNext()) {
            Map.Entry<String, String> xmlentry = itentryset.next();
            put(xmlentry.getKey(), xmlentry.getValue());
        }
    }
    
    
    private static final String mask (final String value) {
        String rebuilt = value;
        if (value.contains(".")){
            rebuilt = value.replaceAll("\\.", "\\\\.");
        }
        if (value.contains("*")){
            rebuilt = value.replaceAll("\\*", "\\\\*");
        }
        if (value.contains("|")){
            rebuilt = value.replaceAll("\\|", "\\\\|");
        }
        if (value.contains("?")){
            rebuilt = value.replaceAll("\\?", "\\\\?");
        }
        if (value.contains("+")){
            rebuilt = value.replaceAll("\\+", "\\\\+");
        }
        if (value.contains("\\")){
            rebuilt = value.replaceAll("\\\\", "\\\\\\\\");
        }
        
        return rebuilt;
    }
    
    
}//end: class