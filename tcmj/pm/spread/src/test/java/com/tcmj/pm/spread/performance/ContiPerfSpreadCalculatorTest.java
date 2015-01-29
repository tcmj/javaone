package com.tcmj.pm.spread.performance;

import com.tcmj.pm.spread.impl.SpreadCalculatorTest;
import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * ContiPerfSpreadCalculatorTest.
 * @author Thomas Deutsch <thomas-deutsch(a.t)tcmj.de>
 * @since 30.01.2011
 */
@PerfTest(invocations = 500, threads = 30)
public class ContiPerfSpreadCalculatorTest extends SpreadCalculatorTest {

    @Rule
    public ContiPerfRule i = new ContiPerfRule();

    @Test
    @Override
    public void testComputeSpread_Testcase1() {
        super.testComputeSpread_Testcase1();
    }

    @Test
    @Override
    public void testComputeSpread_Testcase2() {
        super.testComputeSpread_Testcase2();
    }

    @Test
    @Override
    public void testComputeSpread_Testcase3() {
        super.testComputeSpread_Testcase3();
    }

    @Test
    @Override
    public void testComputeSpread_Testcase4() {
        super.testComputeSpread_Testcase4();
    }

    @Test
    @Override
    public void testComputeSpread_Testcase5() {
        super.testComputeSpread_Testcase5();
    }

    @Test
    @Override
    public void testComputeSpread_Testcase6() {
        super.testComputeSpread_Testcase6();
    }

    @Test
    @Override
    public void testComputeSpread_Testcase7() {
        super.testComputeSpread_Testcase7();
    }

    @Test
    @Override
    public void testComputeSpread_Testcase8() {
        super.testComputeSpread_Testcase8();
    }

    @Test
    @Override
    public void testComputeSpread_Testcase9() {
        super.testComputeSpread_Testcase9();
    }

    @Test
    @Override
    public void testComputeSpread_Testcase10() {
        super.testComputeSpread_Testcase10();
    }

    @Test
    @Override
    public void testComputeSpread_Testcase11() {
        super.testComputeSpread_Testcase11();
    }

    @Test
    @Override
    public void testComputeSpread_Testcase12() {
        super.testComputeSpread_Testcase12();
    }
}
