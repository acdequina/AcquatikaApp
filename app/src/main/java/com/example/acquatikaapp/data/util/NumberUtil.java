package com.example.acquatikaapp.data.util;

public class NumberUtil {

    public static float mapRangeDateToAxis(long fromMin, long fromMax,
                                           long toMin, long toMax, long value) {
        if (fromMin == value) {
            return toMin;
        }

        if (fromMax == value) {
            return toMax;
        }

        return toMin + ((float) (value - fromMin) * (toMax - toMin)) / (float) (fromMax - fromMin);
    }

    public static long mapRangeAxisToDate(long fromMin, long fromMax,
                                          long toMin, long toMax, float value) {
        if (fromMin == value) {
            return toMin;
        }

        if (fromMax == value) {
            return toMax;
        }

        return toMin + (Math.round((value - fromMin) * (toMax - toMin))) / (fromMax - fromMin);
    }

}
