package com.tcmj.common.text;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * HumanReadable.
 * @author tcmj
 */
public class HumanReadable {

    public static String bytes(long bytes) {
        return bytes(Locale.getDefault(), bytes);
    }

    public static String bytes(Locale locale, long bytes) {
        return convertNumber(locale, bytes, new String[]{"bytes", "KB", "MB", "GB", "TB"}, new double[]{1024.0D, 1024.0D, 1024.0D, 1024.0D});
    }

    public static String nanoSec(long nanoSeconds) {
        return nanoSec(Locale.getDefault(), nanoSeconds);
    }

    public static String nanoSec(Locale locale, long nanoSeconds) {
        return convertNumber(locale, nanoSeconds, new String[]{"ns", "µs", "ms", "sec", "min", "h", "days"}, new double[]{1000.0D, 1000.0D, 1000.0D, 60.0D, 60.0D, 24.0D});
    }

    public static String milliSec(long millis) {
        return milliSec(Locale.getDefault(), millis);
    }

    public static String milliSec(Locale locale, long millis) {
        return convertNumber(locale, millis, new String[]{"ms", "sec", "min", "h", "days"}, new double[]{1000.0D, 60.0D, 60.0D, 24.0D});
    }
    
    public static String percent(double percent) {
        return String.format("%.1f", percent) + " %";
    }

    public static String convertNumber(Locale locale, double number, String[] units, double... divisors) {
        int magnitude = 0;
        do {
            double currentDivisor = divisors[java.lang.Math.min(magnitude, divisors.length - 1)];
            if (number < currentDivisor) {
                break;
            }
            number /= currentDivisor;
            magnitude++;
        } while (magnitude < units.length - 1);
        NumberFormat format = NumberFormat.getNumberInstance(locale);
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(0);
        String result = format.format(number) + " " + units[magnitude];
        return result;
    }
}
