package com.example.acquatikaapp.ui.util;

import com.example.acquatikaapp.data.util.Constants;
import com.example.acquatikaapp.data.util.NumberUtil;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

public class TimeAxisValueFormatter extends ValueFormatter {

    private long mMinTime;
    private long mMaxTime;

    public TimeAxisValueFormatter(long minTime, long maxTime) {
        mMinTime = minTime;
        mMaxTime = maxTime;
    }

    @Override
    public String getAxisLabel(float value, AxisBase axis) {

        if (mMinTime == mMaxTime) {
            return DateUtil.convertEpochMilliToTimeString(mMinTime);
        }

        long epochMilliValue = NumberUtil.mapRange(Constants.TIME_XAXIS_MIN, Constants.TIME_XAXIS_MAX, mMinTime, mMaxTime, Math.round(value));

        return DateUtil.convertEpochMilliToTimeString(epochMilliValue);
    }

}
