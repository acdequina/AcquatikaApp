package com.example.acquatikaapp.data.util;

public class NumberUtil {

    public static float mapRange(float fromMin, float fromMax,
                                  float toMin, float toMax, float value){
        return toMin + ((value - fromMin)*(toMax - toMin))/(fromMax - fromMin);
    }

    public static long mapRange(long fromMin, long fromMax,
                                long toMin, long toMax, long value){
        return toMin + ((value - fromMin)*(toMax - toMin))/(fromMax - fromMin);
    }

}
