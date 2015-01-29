package com.tcmj.pm.spread;

import com.tcmj.pm.spread.impl.SpreadPeriod;

/**
 * SpreadCalculator.
 * @author Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 * @since 30.01.2011
 */
@FunctionalInterface
public interface SpreadCalculator {

    /**
     * Spreads one or more values over periods using a curve.
     * The values will be directly set into the given periods for performance reasons.
     * @param values one or more values to be spreaded
     * @param start curve start date (timeframe to be considered)
     * @param finish curve finish date (timeframe to be considered)
     * @param periods time periods
     * @param curve an array of values representing the curve used to spread the values
     * @param exclusions the time periods to be excluded from the spread (e.g. weekends)
     * <b>please note that exclusion periods may not overlap and must be sorted</b>
     */
    public void computeSpread(double[] values, long start, long finish,
            SpreadPeriod[] periods, double[] curve, SpreadPeriod[] exclusions);

}
