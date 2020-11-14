package com.example.acquatikaapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acquatikaapp.R;
import com.example.acquatikaapp.data.dto.DashboardProductsCountDto;
import com.example.acquatikaapp.data.dto.SalesLineGraphDto;
import com.example.acquatikaapp.data.dto.SalesOrderItemDto;
import com.example.acquatikaapp.data.model.Product;
import com.example.acquatikaapp.data.util.Constants;
import com.example.acquatikaapp.data.util.DataExporter;
import com.example.acquatikaapp.data.util.NumberUtil;
import com.example.acquatikaapp.ui.util.DateUtil;
import com.example.acquatikaapp.ui.util.ValueUtil;
import com.example.acquatikaapp.ui.util.TimeAxisValueFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

public class MainActivity extends AppCompatActivity implements SalesOrderAdapter.ItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView mCurrentDateTv;
    private TextView mCurrentTotalSalesTv;
    private TextView mProductLeftLabelTv;
    private TextView mProductCenterLabelTv;
    private TextView mProductLeftCountTv;
    private TextView mProductCenterCountTv;
    private TextView mOthersCountTv;
    private LineChart mSalesLineChart;

    private FloatingActionButton mAddSalesOrderButton;

    private RecyclerView mCurrentSalesOrderRv;
    private SalesOrderAdapter mSalesOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0);

        setupLayout();
        setupViewModel();
    }

    private void setupLayout() {
        //Setup Recycler View
        mCurrentSalesOrderRv = findViewById(R.id.current_sales_orders_rv);
        mCurrentSalesOrderRv.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mCurrentSalesOrderRv.addItemDecoration(decoration);
        mCurrentSalesOrderRv.setHasFixedSize(true);

        //Setup Adapter
        mSalesOrderAdapter = new SalesOrderAdapter(this, true, this);
        mCurrentSalesOrderRv.setAdapter(mSalesOrderAdapter);

        //Line chart
        setupLineChart();

        mCurrentDateTv = findViewById(R.id.dashboard_date_tv);
        mCurrentTotalSalesTv = findViewById(R.id.current_total_sales_tv);
        mProductLeftLabelTv = findViewById(R.id.product_left_label_tv);
        mProductLeftCountTv = findViewById(R.id.product_left_count_tv);
        mProductCenterLabelTv = findViewById(R.id.product_center_label_tv);
        mProductCenterCountTv = findViewById(R.id.product_center_count_tv);
        mOthersCountTv = findViewById(R.id.others_count_tv);

        DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
        String currentDateString = dateFormat.format(new Date());

        mCurrentDateTv.setText(currentDateString);

        mAddSalesOrderButton = findViewById(R.id.sales_order_add_fab);
        mAddSalesOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addSalesOrderIntent = new Intent(MainActivity.this, AddSalesItemActivity.class);
                startActivity(addSalesOrderIntent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.dashboard_csv_export)  {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
            DataExporter exporter = new DataExporter(this.getApplication());
            exporter.runExport();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void setupViewModel() {
        final MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        viewModel.getCurrentSalesOrders().observe(this, new Observer<List<SalesOrderItemDto>>() {
            @Override
            public void onChanged(List<SalesOrderItemDto> transactionDtos) {
                Log.d(TAG, "Updating list of transactions from LiveData in ViewModel");
                mSalesOrderAdapter.setTransactions(transactionDtos);
            }
        });

        viewModel.getCurrentTotalSales().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                mCurrentTotalSalesTv.setText(ValueUtil
                        .convertPriceToDisplayValue(aLong != null ? aLong : 0L));
            }
        });

        viewModel.getDashboardProductsCount().observe(this, new Observer<DashboardProductsCountDto>() {
            @Override
            public void onChanged(DashboardProductsCountDto products) {
                if(products.getProductLeftLabel() != null) {
                    mProductLeftLabelTv.setText(products.getProductLeftLabel());
                }

                if(products.getProductCenterLabel()  != null) {
                    mProductCenterLabelTv.setText(products.getProductCenterLabel());
                }

                mProductLeftCountTv.setText(String.valueOf(products.getProductLeftCount()));
                mProductCenterCountTv.setText(String.valueOf(products.getProductCenterCount()));
                mOthersCountTv.setText(String.valueOf(products.getProductOthersCount()));
            }
        });

        viewModel.getSalesLineGraphValues().observe(this, new Observer<List<SalesLineGraphDto>>() {
            @Override
            public void onChanged(List<SalesLineGraphDto> salesLineGraphDtos) {
                setLineChartData(salesLineGraphDtos);
            }
        });
    }

    @Override
    public void onItemClickListener(long itemId) {
        Intent intent = new Intent(MainActivity.this, SalesOrderEditorActivity.class);
        intent.putExtra(SalesOrderEditorActivity.SALES_ORDER_EDIT_EXTRA, itemId);
        startActivity(intent);
    }

    private void setLineChartData(List<SalesLineGraphDto> salesLineGraphDtos) {

        if(salesLineGraphDtos == null || salesLineGraphDtos.isEmpty()) {
            return;
        }

        List<Entry> values = new ArrayList<>();

        long minValue = salesLineGraphDtos.get(0).getDate().getTime();
        long maxValue = salesLineGraphDtos.get(salesLineGraphDtos.size() - 1).getDate().getTime();

        XAxis xAxis = mSalesLineChart.getXAxis();
        xAxis.setValueFormatter(new TimeAxisValueFormatter(minValue, maxValue));

        long total = 0;
        for (SalesLineGraphDto data : salesLineGraphDtos) {
            total += data.getTotalPrice() / 100L;

            float xValue = NumberUtil.mapRange((float) minValue, (float) maxValue, Constants.TIME_XAXIS_MIN, Constants.TIME_XAXIS_MAX, (float) data.getDate().getTime());
            values.add(new Entry(xValue, total));
        }

        LineDataSet set = setupLineChartSet(values);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set);

        LineData data = new LineData(dataSets);

        mSalesLineChart.setData(data);
        mSalesLineChart.invalidate();
    }

    private LineDataSet setupLineChartSet(List<Entry> values) {
        LineDataSet set;

        if(values.isEmpty()) {
            set = new LineDataSet(values, "Sales overtime");
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
        set.setCircleHoleColor(getColor(R.color.colorPrimary));
        set.setDrawFilled(true); // under the line fill
        set.setFormLineWidth(5f);
        set.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
        set.setFormSize(5.f);
        set.setFillColor(Color.WHITE); // under the line fill
        set.setDrawValues(false);

        return set;
    }

    private void setupLineChart() {
        mSalesLineChart = findViewById(R.id.sales_line_chart);
        mSalesLineChart.setTouchEnabled(true);
        mSalesLineChart.setPinchZoom(true);
        mSalesLineChart.getDescription().setEnabled(false);

        YAxis leftAxis = mSalesLineChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setTextSize(10);
        YAxis rightAxis = mSalesLineChart.getAxisRight();
        rightAxis.setTextColor(Color.WHITE);
        rightAxis.setTextSize(10);

        XAxis xAxis = mSalesLineChart.getXAxis();
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
}
