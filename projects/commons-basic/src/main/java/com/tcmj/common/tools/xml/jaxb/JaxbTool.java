/* Copyright 2009 */
package com.tcmj.common.tools.xml.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 * JaxbTool.
 * @author Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 * @since 16.10.2009
 */
public class JaxbTool {

    /**
     * default no-arg-constructor.
     */
    public JaxbTool() {
    }


    public static final <T> void createXml(T instance) throws JAXBException {

        JAXBContext context = JAXBContext.newInstance(instance.getClass());
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.marshal(instance, System.out);

    }
}
