/*
 * Created on 03.03.2009
 * Copyright(c) 2009 tcmj.  All Rights Reserved.
 * @author TDEUT - Thomas Deutsch - 2009
 */
package com.tcmj.pm.conflicts;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcmj.pm.conflicts.bars.Bar;
import com.tcmj.pm.conflicts.data.OutputBar;
import com.tcmj.pm.conflicts.data.Precision;
import com.tcmj.pm.conflicts.data.SortItem;
import com.tcmj.pm.conflicts.data.SortItem.SortItemType;

/**
 * Finds conflicts. Last Modify: $Date: 2009/04/30 09:57:04 $ by $Author: TDEUT
 * $
 * 
 * @version $Revision: 1.10 $
 * @author tdeut Thomas Deutsch
 */
public class ConflictFinder {

    /** slf4j Logging framework. */
    private static final transient Logger logger = LoggerFactory.getLogger(ConflictFinder.class);

    /** Input-List. */
    private List<Bar> _inputList;

    /** Sorted (input) list with all start and end items. */
    private List<SortItem> _sortedList;

    /** Result List with all conflicts as message item. */
    private List<OutputBar> _outputConflictList;

    /** Result List with all Bars. */
    private List<OutputBar> _outputBarListAll;

    /**
     * Precision of weight. Default = 4 digits after the decimal point.
     */
    private Precision _precision = Precision.FOUR_DIGITS;

    /**
     * in case of resources this is the max allowed job time. values greater
     * than this value are conflicts.
     */
    private long _maxAllowedWeight;

    /** Name of this ConflictFinder. */
    private String _name;


    /** Constructor.
     * default.Name = 'Resources'
     * default.MaxWeight = 100%
     * default.Precision = 4 digits after decimal point 
     * */
    public ConflictFinder() {
        this("Resources");
    }


    /**
     * Main Constructor.
     * default.MaxWeight = 100%
     * default.Precision = 4 digits after decimal point
     * @param name
     *            can be any name to identify this ConflictFinder
     */
    public ConflictFinder(String name) {
        _name = name;
        setMaxAllowedWeight(1.0d);
    }


    /** Set the time period item list to use.
     * @param list a list of classes which implements the Bar interface
     */
    public void setInputList(List<Bar> list) {
        _inputList = list;
    }


    /** Add some time period items to the input list.
     * creates a new ArrayList if it is necessary.
     * @param list a list of classes which implements the Bar interface
     */
    public void addToInputList(List<Bar> list) {
        if (_inputList == null) {
            _inputList = new ArrayList<Bar>();
        }
        _inputList.addAll(list);
    }


    /** Add a time period item to the input list.
     * @param bar any class which implements the Bar interface
     */
    public void addToInputList(Bar bar) {
        if (_inputList == null) {
            _inputList = new ArrayList<Bar>();
        }
        _inputList.add(bar);
    }


    /** Start calculation and bar building.
     * This method starts building the OutputConflictList and the OutputBarListAll.
     */
    public void calculate() {

        if (_inputList == null || _inputList.isEmpty()) {
            logger.debug("No data to calculate!");
        } else {


            logger.debug("Start computing " + _inputList.size() +
                    " InputItems with max allowed weight of " +
                    String.format("%3.0f%%", _precision.long2Double(getMaxAllowedWeightAsLong()) * 100) + " (Precision=" + getPrecision() + ")");

            //preparation (put all start and enddates in a sorted list)
            buildSortedDatesList();

            //calculation of conflicts and building bars
            buildConflictList();

        }

    }


    /**
     * Creates the sorted dates list from the Inputlist.
     */
    private void buildSortedDatesList() {

        _sortedList = new ArrayList<SortItem>();

        for (Bar bar : _inputList) {

            SortItem sitemA = new SortItem(SortItemType.STARTITEM, _precision.double2Long(bar.getWeight()), bar);
            _sortedList.add(sitemA);

            SortItem sitemE = new SortItem(SortItemType.ENDITEM, _precision.double2Long(bar.getWeight()), bar);
            _sortedList.add(sitemE);
        }



        Collections.sort(_sortedList);

        logger.debug("Sorted dates List: " + _sortedList);


    }


    /**
     * Builds the conflict and bar list using the sorted dates list.
     */
    private void buildConflictList() {

        //TODO: speed test with local copies and maybe hashsets

        //init output lists: (one for conflicts, one for all bars)
        _outputConflictList = new ArrayList<OutputBar>();
        _outputBarListAll = new ArrayList<OutputBar>();


        //init list to hold causing bars
        List<Bar> tempBarList = new ArrayList<Bar>();


        //init weight counter:
        long counter = 0;


        //loop through the list of sorted dates:
        for (ListIterator<SortItem> it = _sortedList.listIterator(); it.hasNext();) {

            //take first/next start/end date:
            SortItem sortItem = it.next();


            //if the date is a startdate...
            if (sortItem.getType() == SortItemType.STARTITEM) {

                //then sum up the weight of the bar to the actual counter:
                counter += sortItem.getWeightAsLong();

                //and put the bar to the temporary causing bar list
                tempBarList.add(sortItem.getBaritem());


            //if the date is a finishdate...
            } else if (sortItem.getType() == SortItemType.ENDITEM) {

                //then subtract the weight of the bar from the actual counter:
                counter -= sortItem.getWeightAsLong();

                //and remove the bar from the temporary causing bar list
                tempBarList.remove(sortItem.getBaritem());

            }


            //retrieve the next date-element to get the end date:
            SortItem tempnext = null;
            if (it.hasNext()) {
                tempnext = it.next();
                it.previous(); //go back!
            }


            //check if there is a duration:
            final boolean hasDuration =
                    (counter > 0 && (sortItem.getDate().compareTo(tempnext.getDate()) != 0));

            //check if there is a conflict:
            final boolean isConflict = (counter > getMaxAllowedWeightAsLong());

            //re-compute the internal used long value as double
            final double dblCounter = _precision.long2Double(counter);



            //check if the log4j message should be drawn
            if (logger.isDebugEnabled()) {
                logItem(sortItem, tempnext, counter, tempBarList, isConflict, hasDuration);
            }



            if (hasDuration) { //true means: start (at least) creation of a bar


                //the new bar.key is the string representation of the causing bar list:
                final String newkey = String.valueOf(tempBarList);



                //create a new conflict item (this includes one or more causing bars!)
                OutputBar nbar = new OutputBar(newkey, sortItem.getDate(),
                        tempnext.getDate(), dblCounter, isConflict);

                //put all (current) causing bars onto the output bar:
                nbar.addCausingBars(tempBarList);

                if (tempBarList.size() == 1) {
                    nbar.setProperties(tempBarList.get(0).getProperties());
                }




                //put it in the output bar list:
                _outputBarListAll.add(nbar);


                //in case of conflict there will be created a bar for the conflict output list
                if (isConflict) {//wichtig dass 'hasDuration' auch true ist!
//                    OutputBar conflictbar = new OutputBar(newkey, sortItem.getDate(),
//                            tempnext.getDate(), dblCounter, isConflict);
//                    conflictbar.addCausingBars(tempBarList);
//                    _outputConflictList.add(conflictbar);
                    _outputConflictList.add(nbar);

                }



            }

        }

    }


    /** Log4j output (maybe a little time consuming). */
    private void logItem(SortItem item, SortItem nextitem, long counter, List<Bar> tempBarList, boolean conflict, boolean hasDuration) {

//        final String formatString = "[{0}] : {1}   ({2} {3} {4})= {5} : {6}  End: {7}   Bars: {8}";
        final String formatString = "[{0}] : {1} {2,date,yyyy-MM-dd}  ({3} {4} {5})= {6} : {7}  End: {8} {9,date,yyyy-MM-dd}  Bars: {10}";

        Object[] data = new Object[11];

        if (conflict && hasDuration) {
            data[0] = "C+B";
        } else if (hasDuration) {
            data[0] = "Bar";
        } else {
            data[0] = "-N-";
        }

        data[1] = item;

        if (item != null) {
            data[2] = item.getDate();

            //calc back..
            if (item.getType() == SortItemType.STARTITEM) {

                data[3] = String.format("%3.0f",
                        _precision.long2Double((counter - item.getWeightAsLong())) * 100);
                data[4] = "+";

            } else {

                data[3] = String.format("%3.0f",
                        _precision.long2Double((counter + item.getWeightAsLong())) * 100);
                data[4] = "-";

            }

            data[5] = String.format("%3.0f",
                    _precision.long2Double((item.getWeightAsLong())) * 100);
        }
        data[6] = String.format("%3.0f%%",
                _precision.long2Double(counter) * 100);

        data[7] = ((conflict) ? "Conflict!!!" : "No Conflict");
        data[8] = nextitem;
        data[9] = nextitem == null ? null : nextitem.getDate();
        data[10] = tempBarList;




        String result = MessageFormat.format(formatString, data);


        logger.debug(result);

    }


    /** Loops through the input list and builds a new sorted list of all dates.
     * @return the first element of the sorted dates list (=very first start date)<br>
     *         returns null if the inputlist is not set or empty
     * @todo build the sorted dates list only once (and clear it on any call to addInputBar)
     */
    public Date getMinStartDate() {

        if (_inputList == null || _inputList.isEmpty()) {
            return null;
        }

        if (_sortedList == null) {

            final List<SortItem> tempList = new ArrayList<SortItem>();

            for (Bar bar : _inputList) {
                SortItem sitemA = new SortItem(SortItemType.STARTITEM, _precision.double2Long(bar.getWeight()), bar);
                tempList.add(sitemA);
            }

            Collections.sort(tempList);

            return tempList.get(0).getDate();

        } else {
            return _sortedList.get(0).getDate();
        }

    }


    /** Loops through the input list and builds a new sorted list of all dates.
     * @return the first element of the sorted dates list (=very first start date)<br>
     *         returns null if the inputlist is not set or empty
     */
    public Date getMaxEndDate() {

        if (_inputList == null || _inputList.isEmpty()) {
            return null;
        }

        if (_sortedList == null) {

            final List<SortItem> tempList = new ArrayList<SortItem>();

            for (Bar bar : _inputList) {
                SortItem sitemA = new SortItem(SortItemType.STARTITEM, _precision.double2Long(bar.getWeight()), bar);
                tempList.add(sitemA);
            }

            Collections.sort(tempList);

            return tempList.get(tempList.size() - 1).getDate();

        } else {
            return _sortedList.get(_sortedList.size() - 1).getDate();
        }

    }


    /**
     * Getter for the List which holds all bars.
     * This method should be used after calculation.
     * @return List of Bar items.
     */
    public List<OutputBar> getOutputBarListAll() {
        return _outputBarListAll;
    }


    /**
     * Getter for the List which holds all conflicts.
     * This method should be used after calculation.
     * @return List of OutputBar items.
     */
    public List<OutputBar> getOutputConflictList() {
        return _outputConflictList;
    }


    /**
     * Gets the precision value.
     * @return long value - eg. 1000 means 4 digits
     */
    public Precision getPrecision() {
        return _precision;
    }


    /** Sets the precision used for calculations.
     * Default = 1000 which is 4 digits after the point
     * @param precision eg.: 1000 will round double-weight-values from 0.1234567 to 0.1234
     */
    public void setPrecision(Precision precision) {
        _precision = precision;
    }


    /** Returns the maximum allowed weight in percent.
     *  if you set the max allowed weight to 1.0 this method
     *  returns the internal long value (1.0 * getValue())
     * @return internal long value
     */
    private long getMaxAllowedWeightAsLong() {
        return _maxAllowedWeight;
    }


    /**
     * The max weight limit used to detect conflicts.
     * @return the value set wit setMaxAllowedWeight
     */
    public double getMaxAllowedWeight() {
        return _precision.long2Double(this._maxAllowedWeight);
    }


    /** The max weight limit used to detect conflicts.
     * @param maxWeight use 1.0D to set it to 100%
     */
    public void setMaxAllowedWeight(double maxWeight) {
        this._maxAllowedWeight = _precision.double2Long(maxWeight);
    }


    /** Returns the reference to the input list.
     * @return list of Bar items
     */
    public List<Bar> getInputList() {
        return _inputList;
    }


    /**
     * @return the _name
     */
    public String getName() {
        return _name;
    }


    /**
     * @param name the _name to set
     */
    public void setName(String name) {
        this._name = name;
    }
}
