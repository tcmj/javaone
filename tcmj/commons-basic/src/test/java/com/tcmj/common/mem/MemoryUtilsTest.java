package com.tcmj.common.mem;

import com.tcmj.common.text.HumanReadable;
import org.hamcrest.CoreMatchers;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MemoryUtilsTest -Usage-
 * @author tcmj
 */
public class MemoryUtilsTest {

    private static final Logger LOG = LoggerFactory.getLogger(MemoryUtilsTest.class);

    @Test
    public void testGetMemoryUsedPercentage() {
        double result = new MemoryUtils().getMemoryUsedPercentage();
        LOG.info("getMemoryUsedPercentage: {} ({})", HumanReadable.percent(result), result);
        assertThat(result, is(CoreMatchers.any(Double.class)));
    }

    @Test
    public void testGetTotalMemoryUsed() {
        long result = new MemoryUtils().getTotalMemoryUsed();
        LOG.info("getTotalMemoryUsed: {} ({})", HumanReadable.bytes(result), result);
        assertThat(result, is(CoreMatchers.any(Long.class)));
    }

    @Test
    public void testGetTotalMemoryUsedAfterGC() {
        long result = new MemoryUtils().freeMemory();
        LOG.info("freeMemory: {} ({})", HumanReadable.bytes(result), result);
        assertThat(result, is(CoreMatchers.any(Long.class)));
    }

}
