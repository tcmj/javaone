package com.tcmj.pm.spread.jfreechart;

import com.tcmj.pm.spread.impl.SpreadPeriod;
import java.util.Date;
import org.jfree.data.gantt.Task;
import org.jfree.data.time.SimpleTimePeriod;

/**
 *
 * @author Administrator
 */
public class MyTask<E extends SpreadPeriod> extends Task {

    private E bar;

    public MyTask(E bar) {

        super(String.valueOf(bar.getPeriodValue(0))
                , new SimpleTimePeriod(new Date(bar.getStartMillis()), new Date(bar.getEndMillis())));

        //setPercentComplete(bar.getWeight());

        this.bar = bar;

    }

    /**
     * @return the bar
     */
    public E getBar() {
        return bar;
    }

    /**
     * @param bar the bar to set
     */
    public void setBar(E bar) {
        this.bar = bar;
    }



}
