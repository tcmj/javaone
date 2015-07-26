package com.tcmj.common.mem;

import com.tcmj.common.text.HumanReadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MemoryUtils.
 * @author tcmj
 */

public class MemoryUtils {

    private static final Logger LOG = LoggerFactory.getLogger(MemoryUtils.class);
   

    public double getMemoryUsedPercentage() {
        Runtime runtime = Runtime.getRuntime();
        double maxMemoryAvailableToJVM = runtime.maxMemory();
        double usedMemory = getTotalMemoryUsed();
        double percentageUsed = usedMemory / maxMemoryAvailableToJVM * 100.0D;
        return percentageUsed;
    }

    public long getTotalMemoryUsed() {
        Runtime runtime = Runtime.getRuntime();
        long freeMemory = runtime.freeMemory();
        long currentlyAllocatedByJVM = runtime.totalMemory();
        long usedMemory = currentlyAllocatedByJVM - freeMemory;
        return usedMemory;
    }

    
    
    public long freeMemory() {
        long totalUsed = getTotalMemoryUsed();

        int counter = 0;
        do {
            long totalUsedBeforeGC = totalUsed;
            System.gc();
            System.runFinalization();
            totalUsed = getTotalMemoryUsed();
            
            double difference = 100D - Math.abs( ((double)totalUsed / (double)totalUsedBeforeGC)*100D);
           
            
            
            if (LOG.isDebugEnabled()) {
                LOG.debug("Memory: before GC = '{}' after GC = '{}' ratio='{}'",HumanReadable.bytes(totalUsedBeforeGC), HumanReadable.bytes(totalUsed), HumanReadable.percent(difference));
            }
            if (difference <= 5D) {
                break;
            }
            counter++;
        } while (counter < 100);
        return totalUsed;
    }

 
}
