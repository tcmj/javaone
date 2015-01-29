package com.tcmj.pm.spread;

import com.tcmj.pm.spread.impl.SpreadDoubleImpl;
import org.junit.Ignore;

/**
 * AbstractSpreadTest.
 * @author Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 * @since 22.12.2010
 */
@Ignore
public abstract class AbstractSpreadTest {

    /** Maximal allowed precision difference. */
    protected static final double DELTA = 0.000001D;

    /** Spread implementation instance. */
    protected SpreadCalculator spread;

    /**
     * default no-arg-constructor.
     */
    public AbstractSpreadTest() {
        spread = getSpreadImplementation();
    }

    /** Getter which returns the spread implementation class. */
    public final SpreadCalculator getSpreadImplementation() {
        return new SpreadDoubleImpl();
//        return new SpreadCalculatorSaprimaImpl();
    }

}
