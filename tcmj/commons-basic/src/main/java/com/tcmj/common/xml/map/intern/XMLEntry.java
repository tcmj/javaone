package com.tcmj.common.xml.map.intern;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Entry of the XML Document.
 * contains key, value as String
 * @author tcmj - Thomas Deutsch - tdeut
 */
public class XMLEntry implements Map.Entry<String, String>, Serializable {
    private static final long serialVersionUID = -3974837921466840353L;
    private static final int TYPE_SINGLE = 0x0;
    private static final int TYPE_MULTI = 0x1;
    private int type = TYPE_SINGLE;
    private int xmlNodeType;
    /** Key. */
    private final String key;
    /** Value. */
    private String[] value;
    /** Java Object. */
    private Object[] object;
    /** Attributes. */
    private Map<String, String> attributes;
    /** Comment. */
    private String comment;
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
    @Override
    public String getKey() {
        return this.key;
    }

    /**
     * Getter for Property Value.
     * @return Value
     */
    @Override
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

    @Override
    public String setValue(String val) {
        String old = this.value[0];
        if (type == TYPE_SINGLE) {
            this.value[0] = val;
        } else {
            throw new UnsupportedOperationException("not allowed for single type");
        }
        return old;
    }

    /**
     * Used for multiple values on the last node.
     * The type of the entity will be marked as TYPE_MULTI.
     * @param val the value to be added (on the same key)
     */
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
            this.attributes = new LinkedHashMap<>();
        }
        if (!this.attributes.containsKey(name)) {
            this.attributes.put(name, value);
        } else {
            //Get the value using the key and append the value with a '|' as separator
            String attrValue = this.attributes.get(name);

            //deletion or add ?
            if (type == TYPE_SINGLE && value == null) {
                this.attributes.remove(name);
            } else if (type == TYPE_MULTI && value == null) {
                throw new UnsupportedOperationException("deletion on multi types not supported!");
            } else if (type == TYPE_SINGLE && value != null) {
                this.attributes.put(name, value);
            } else if (type == TYPE_MULTI && value != null) {
                this.attributes.put(name, attrValue + "|" + value);
            }
        }
    }

    public Map<String, String> getAttributes() {
        if (type == TYPE_SINGLE) {
            return this.attributes;
        }
        return null;
    }

    public Map<String, String> getListAttributes() {
        if (type == TYPE_MULTI) {
            return this.attributes;
        }
        return null;
    }

    @Override
    public int hashCode() {
        return (key == null ? 0 : key.hashCode())
                ^ (value == null ? 0 : Arrays.hashCode(value));
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
