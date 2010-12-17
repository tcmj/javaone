/*
 *  Copyright (C) 2010 Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.tcmj.pm.junit;

import com.tcmj.pm.conflicts.bars.OutputBar;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.*;

/**
 * Extended JUnit assertion class used for bars.
 * @author Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 * @since 17.12.2010
 */
public class BarAssert {

    /**
     * default no-arg-constructor.
     */
    private BarAssert() {
    }


    public static void assertBar(List<OutputBar> result, int index, Date start, Date end, double pct) {
        assertEquals("B" + index + "-Start", start, result.get(index).getStartDate());
        assertEquals("B" + index + "-End", end, result.get(index).getEndDate());
        assertEquals("B" + index + "-Pct", pct, result.get(index).getWeight(), 0D);
    }


    public static void assertConflict(List<OutputBar> result, int index, Date start, Date end, double pct) {
        assertEquals("C" + index + "-Start", start, result.get(index).getStartDate());
        assertEquals("C" + index + "-End", end, result.get(index).getEndDate());
        assertEquals("C" + index + "-Pct", pct, result.get(index).getWeight(), 0D);
    }
}
