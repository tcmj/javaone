package com.tcmj.common.tools.xml.map.intern;

/**
 * XMLMap (Runtime) Exception.
 */
public class XMLMapException extends RuntimeException {

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
