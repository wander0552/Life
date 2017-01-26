package com.wander.base.utils;

/**
 * Created on 2016/6/22.
 */
public class TimeUtils {
    public static final long SECOND = 1000L;
    public static final long MINUTE = 60000L;
    public static final long HOUR = 3600000L;
    public static final long DAY = 86400000L;
    public static final long WEEK = 604800000L;
    public static final long MONTH = 2592000000L;
    public static final long SEASON = 7776000000L;
    public static final long YEAR = 31536000000L;

    public TimeUtils() {
    }

    public static String toString(long milliSecs) {
        StringBuffer sb = null;
        sb = new StringBuffer();
        long second = milliSecs / 1000L;
        long m = second / 60L;
        sb.append(m < 10L?"0":"").append(m);
        sb.append(":");
        long s = second % 60L;
        sb.append(s < 10L?"0":"").append(s);
        return sb.toString();
    }
}
