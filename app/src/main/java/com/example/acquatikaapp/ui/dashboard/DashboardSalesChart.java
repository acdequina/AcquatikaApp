package com.example.acquatikaapp.ui.dashboard;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;

import com.example.acquatikaapp.R;
import com.example.acquatikaapp.data.dto.SalesLineGraphDto;
import com.example.acquatikaapp.data.util.Constants;
import com.example.acquatikaapp.data.util.NumberUtil;
import com.example.acquatikaapp.ui.util.TimeAxisValueFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class DashboardSalesChart {

    private LineChart mChart;
    private Context mContext;

    public DashboardSalesChart(Context context, LineChart salesLineChart) {
        this.mContext = context;
        this.mChart = salesLineChart;
        setupLineChart();
    }

    public void setupLineChart() {
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);
        mChart.getDescription().setEnabled(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setTextSize(10);
        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setTextColor(Color.WHITE);
        rightAxis.setTextSize(10);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawGridLines(false);
        //set x axis to bottom
        XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
        xAxis.setPosition(position);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setLabelCount(Constants.TIME_XAXIS_MAX, true);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(7f);
        xAxis.setTextSize(10);
    }

    public void setData(List<SalesLineGraphDto> salesLineGraphDtos) {
        if (salesLineGraphDtos == null || salesLineGraphDtos.isEmpty()) {
            return;
        }

        List<Entry> values = new ArrayList<>();

        long minValue = salesLineGraphDtos.get(0).getDate().getTime();
        long maxValue = salesLineGraphDtos.get(salesLineGraphDtos.size() - 1).getDate().getTime();

        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new TimeAxisValueFormatter(minValue, maxValue));

        long total = 0;
        for (SalesLineGraphDto data : salesLineGraphDtos) {
            total += data.getTotalPrice() / 100L;

            float xValue = NumberUtil.mapRangeDateToAxis(minValue, maxValue,
                    Constants.TIME_XAXIS_MIN, Constants.TIME_XAXIS_MAX, data.getDate().getTime());

            values.add(new Entry(xValue, total));
        }

        LineDataSet set = setupLineChartSet(values);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);

        LineData data = new LineData(dataSets);

        mChart.setData(data);
        mChart.invalidate();

    }

    private LineDataSet setupLineChartSet(List<Entry> values) {
        LineDataSet set;

        if (values.isEmpty()) {
            set = new LineDataSet(values, mContext.getString(R.string.sales_over_time));
            set.setDrawCircles(true);
            set.enableDashedLine(10f, 0f, 0f);
            set.enableDashedHighlightLine(10f, 0f, 0f);

            return set;
        }

        set = new LineDataSet(values, "");
        set.setFillAlpha(110);

        set.setColor(Color.WHITE);
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);//line size
        set.setCircleRadius(5f);
        set.setDrawCircleHole(true);
        set.setCircleHoleColor(mContext.getColor(R.color.colorPrimary));
        set.setDrawFilled(true); // under the line fill
        set.setFormLineWidth(5f);
        set.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set.setFormSize(5.f);
        set.setFillColor(Color.WHITE); // under the line fill
        set.setDrawValues(false);

        return set;
    }


}
