/*
 * Copyright (C) 2012 tcmj development
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.tcmj.common.tools.lang;

import java.io.Closeable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a bunch of closing methods for streams, readers, writers, channels.
 * Take a look at {@link java.io.Closeable} !
 * @author tcmj
 */
public class Close {

    /** slf4j Logging framework. */
    private static final Logger logger = LoggerFactory.getLogger(Close.class);
    
    /**
     * instantiation not allowed!
     */
    private Close() {
    }
    
    
    /**
     * Internal method which provides the ability to swallow the exception or to
     * wrap the exception to an unchecked exception.
     */
    private static void closeIntern(Closeable object, boolean throwUnchecked) {
        if (object != null) {
            try {
                object.close();
            } catch (Exception e) {
                logger.error("Cannot close {}",object);
                if (throwUnchecked) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Closes the object quietly swallowing any exception!
     * @param object to be closed
     */
    public static void quiet(Closeable object) {
        closeIntern(object, false);
    }

    /**
     * Closes the object wrapping exceptions to unchecked runtime exception!
     * @param object to be closed
     */
    public static void unchecked(Closeable object) {
        closeIntern(object, true);
    }
}
