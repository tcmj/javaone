package com.tcmj.pm.conflicts.data.scase;

import com.tcmj.pm.conflicts.bars.Bar;
import com.tcmj.pm.conflicts.bars.SimpleBar;
import java.util.Date;

/**
 * TODO implement this class
 * @author tcmj
 */
public class SpecialBar extends SimpleBar implements Bar {

    public SpecialBar(String key, Date start, Date end, double weight) {
        super(key, start, end, weight);
    }
}
