package com.tcmj.pm.spread.performance;

import org.databene.contiperf.junit.ContiPerfSuiteRunner;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

/**
 * PeakLoadTest.
 * @author Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 * @since 26.01.2011
 */
@RunWith(ContiPerfSuiteRunner.class)
@SuiteClasses(ContiPerfSpreadCalculatorTest.class)
public class PeakLoadTest {

}
