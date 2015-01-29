package com.tcmj.common.xml.jaxb;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * XMLRootObject.
 * @author Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 * @since 11.04.2010
 */
@XmlRootElement( name = "tcmj" )
public class XMLRootObject {

    private int simpleInt;

    private Integer classInt;

    private String anyString;


    /**
     * default no-arg-constructor.
     */
    public XMLRootObject() {
    }


    /**
     * @return the simpleInt
     */
    public int getSimpleInt() {
        return simpleInt;
    }


    /**
     * @param simpleInt the simpleInt to set
     */
    public void setSimpleInt(int simpleInt) {
        this.simpleInt = simpleInt;
    }


    /**
     * @return the classInt
     */
    public Integer getClassInt() {
        return classInt;
    }


    /**
     * @param classInt the classInt to set
     */
    public void setClassInt(Integer classInt) {
        this.classInt = classInt;
    }


    /**
     * @return the anyString
     */
    public String getAnyString() {
        return anyString;
    }


    /**
     * @param anyString the anyString to set
     */
    public void setAnyString(String anyString) {
        this.anyString = anyString;
    }
}
