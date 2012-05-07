/**
 * Copyright(c) 2003 - 2010 by INTECO GmbH
 * All Rights Reserved.
 */

package com.tcmj.pm.mta.ex;

/**
 * IMTAException.
 * @author tdeut - Thomas Deutsch
 * @version $Id$
 */
public class MTAException extends Exception {

    /** {@inheritDoc} */
    public MTAException(Throwable cause) {
        super(cause);
    }

    /** {@inheritDoc} */
    public MTAException(String message, Throwable cause) {
        super(message, cause);
    }

    /** {@inheritDoc} */
    public MTAException(String message) {
        super(message);
    }

    /** {@inheritDoc} */
    public MTAException() {
    }


}
