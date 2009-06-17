

package com.tcmj.pm.conflicts.data.scase;

import com.tcmj.pm.conflicts.bars.Bar;
import com.tcmj.pm.conflicts.data.SimpleBar;
import java.util.Date;

/**
 *
 * @author tcmj
 */
public class SpecialBar extends SimpleBar implements Bar{

    public SpecialBar(String key, Date start, Date end, double weight) {
        super(key, start, end, weight);
    }






}
