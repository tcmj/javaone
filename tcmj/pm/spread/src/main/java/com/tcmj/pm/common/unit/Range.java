package com.tcmj.pm.common.unit;

import java.io.Serializable;

/**
 * Range with fixed start and endpoints.
 * A range is defined by a specific begin value and a specific end value. The
 * start and end point must be defined by a fixed long value. The start must be
 * smaller as the finish value and for calculations the start value is inclusive
 * and the finish value is exclusive.
 * @author Thomas Deutsch
 * @since 02.02.2011
 */
public class Range implements Serializable {

    /** Serialization version. */
    private static final long serialVersionUID = 1912553897241326147L;

    private final long start;

    private final long finish;

    private final long duration;


    public Range(final long start, final long finish) {
        if (start >= finish) {
            throw new IllegalArgumentException("Range finish value must be after start value!");
        }
        this.start = start;
        this.finish = finish;
        this.duration = finish - start;
    }


    /**
     * Gets the start of this time interval which is inclusive.
     * @return the start value
     */
    public long getStartMillis() {
        return start;
    }


    /**
     * Gets the end of this time interval which is exclusive.
     * @return the finish
     */
    public long getEndMillis() {
        return finish;
    }


    /**
     * Gets the end of this time interval which is exclusive.
     * @return the finish
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Does this time range contain the specified point in time.
     * <p>
     * Ranges are inclusive of the start and exclusive of the end.
     * @param millis value to check
     * @return true if (millis >= getStartMillis() && millis < getEndMillis())
     */
    public boolean contains(long millis) {
        return (millis >= getStartMillis() && millis < getEndMillis());
    }


    public boolean contains(Range range) {
        if (range == null) {
            throw new NullPointerException("Range object cannot be null!");
        }
        long otherStart = range.getStartMillis();
        long thisEnd = getEndMillis();
        return (getStartMillis() <= otherStart && otherStart < thisEnd && range.getEndMillis() <= thisEnd);
    }


    /**
     * Checks if this range overlaps with another range.
     * @param other Range object to compare
     * @return true if (thisStart < other.getEndMillis() && other.getStartMillis() < thisEnd)
     */
    public boolean overlaps(Range other) {
        if (other == null) {
            throw new NullPointerException("Range object cannot be null!");
        } else {
            return (getStartMillis() < other.getEndMillis() && other.getStartMillis() < getEndMillis());
        }
    }


    public boolean overlapsBefore(Range other) {
        if (other == null) {
            throw new NullPointerException("Range object cannot be null!");
        } else {
            return (getStartMillis() < other.getStartMillis() && getEndMillis() > other.getStartMillis());
        }
    }


    public boolean overlapsAfter(Range other) {
        if (other == null) {
            throw new NullPointerException("Range object cannot be null!");
        } else {
            return (getStartMillis() < other.getEndMillis() && other.getEndMillis() < getEndMillis());
        }
    }


    public long overlapDuration(Range range) {
        if (overlaps(range) == false) {
            return 0;
        }
        return Math.min(getEndMillis(), range.getEndMillis()) - Math.max(getStartMillis(), range.getStartMillis());
    }


    public Range overlapRange(Range range) {
        if (overlaps(range) == false) {
            return null;
        }
        long begin = Math.max(getStartMillis(), range.getStartMillis());
        long end = Math.min(getEndMillis(), range.getEndMillis());
        return new Range(begin, end);
    }


    public boolean isBefore(long millis) {
        return (getEndMillis() <= millis);
    }


    public boolean isBefore(Range range) {
        if (range == null) {
            throw new NullPointerException("Range object cannot be null!");
        }
        return isBefore(range.getStartMillis());
    }


    public boolean isAfter(long millis) {
        return (getStartMillis() > millis);
    }


    public boolean isAfter(Range range) {
        if (range == null) {
            throw new NullPointerException("Range object cannot be null!");
        }
        return (getStartMillis() >= range.getEndMillis());

    }


    public static boolean abuts(Range range1, Range range2) {
        if (range1 == null || range2 == null) {
            throw new NullPointerException("Range objects cannot be null!");
        }
        return (range1.getEndMillis() == range2.getStartMillis());
    }

//
//    public boolean partitionedBy( Range[] args) {
//        if (!isContiguous(args)) {
//            return false;
//        }
//        return this.equals( Range.combination(args));
//    }
//
//
//    public static Range combination( Range[] args) {
//        Arrays.sort(args);
//        if (!isContiguous(args)) {
//            throw new IllegalArgumentException("Unable to combine date ranges");
//        }
//        return new Range(args[0].start, args[args.length - 1].finish);
//    }
//
//
//    public static boolean isContiguous(Range[] args) {
//        Arrays.sort(args);
//        for (int i = 0; i < args.length - 1; i++) {
//            if (! abuts(args[i],args[i + 1])) {
//                return false;
//            }
//        }
//        return true;
//    }


    @Override
    public boolean equals(Object range) {
        if (this == range) {
            return true;
        }
        if (range instanceof Range == false) {
            return false;
        }
        Range other = (Range) range;
        return getStartMillis() == other.getStartMillis()
                && getEndMillis() == other.getEndMillis();
    }


    @Override
    public int hashCode() {
        long begin = getStartMillis();
        long end = getEndMillis();
        int lowerb = (int)(begin ^ (begin >>> 32));
        int upperb = (int)(end ^ (end >>> 32));
        return (lowerb << 16 ^ upperb);

    }


    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder("[");
        buffer.append(getStartMillis());
        buffer.append(",");
        buffer.append(getEndMillis());
        buffer.append("]");
        return buffer.toString();
    }
}
