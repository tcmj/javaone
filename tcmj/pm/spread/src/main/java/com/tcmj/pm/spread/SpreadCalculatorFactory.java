package com.tcmj.pm.spread;

import com.tcmj.pm.spread.impl.SpreadDoubleImpl;

/**
 * SpreadCalculatorFactory.
 * @author Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 * @since 30.01.2011
 */
public class SpreadCalculatorFactory {

    /**
     * default no-arg-constructor.
     */
    public static SpreadCalculator getSpreadCalculatorDoubleImpl() {
        return new SpreadDoubleImpl();
    }

}
