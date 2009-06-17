/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcmj.pm.conflicts.jfreechart;

import com.tcmj.pm.conflicts.bars.Bar;
import org.jfree.data.gantt.Task;
import org.jfree.data.time.SimpleTimePeriod;

/**
 *
 * @author Administrator
 */
public class MyTask<E extends Bar> extends Task {

    private E bar;

    public MyTask(E bar) {

        super(bar.getKey(), new SimpleTimePeriod(bar.getStartDate(), bar.getEndDate()));

        setPercentComplete(bar.getWeight());

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
