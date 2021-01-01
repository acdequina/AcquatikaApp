package com.example.acquatikaapp.ui.dashboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.acquatikaapp.R;
import com.example.acquatikaapp.data.dto.DashboardProductsCountDto;
import com.example.acquatikaapp.data.dto.SalesLineGraphDto;
import com.example.acquatikaapp.data.dto.SalesOrderItemDto;
import com.example.acquatikaapp.data.util.DataExporter;
import com.example.acquatikaapp.ui.item.selection.AddSalesItemActivity;
import com.example.acquatikaapp.ui.sales.editor.SalesOrderAdapter;
import com.example.acquatikaapp.ui.sales.editor.SalesOrderEditorActivity;
import com.example.acquatikaapp.ui.util.ValueUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
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
    private DashboardSalesChart mDashBoardChart;

    private SalesOrderAdapter mSalesOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        setupLayout();
        setupViewModel();
    }

    private void setupLayout() {
        //Setup Recycler View
        RecyclerView mCurrentSalesOrderRv = findViewById(R.id.current_sales_orders_rv);
        mCurrentSalesOrderRv.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mCurrentSalesOrderRv.addItemDecoration(decoration);
        mCurrentSalesOrderRv.setHasFixedSize(true);

        //Setup Adapter
        mSalesOrderAdapter = new SalesOrderAdapter(this, true, this);
        mCurrentSalesOrderRv.setAdapter(mSalesOrderAdapter);
        mDashBoardChart = new DashboardSalesChart(this, findViewById(R.id.sales_line_chart));

        mCurrentDateTv = findViewById(R.id.dashboard_date_tv);
        mCurrentTotalSalesTv = findViewById(R.id.current_total_sales_tv);
        mProductLeftLabelTv = findViewById(R.id.product_left_label_tv);
        mProductLeftCountTv = findViewById(R.id.product_left_count_tv);
        mProductCenterLabelTv = findViewById(R.id.product_center_label_tv);
        mProductCenterCountTv = findViewById(R.id.product_center_count_tv);
        mOthersCountTv = findViewById(R.id.others_count_tv);

        DateFormat dateFormat = DateFormat.getDateInstance();
        String currentDateString = dateFormat.format(new Date());

        mCurrentDateTv.setText(currentDateString);

        FloatingActionButton mAddSalesOrderButton = findViewById(R.id.sales_order_add_fab);
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

                Toast.makeText(MainActivity.this, R.string.permission_denied_external_storage, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupViewModel() {
        final MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        viewModel.getCurrentSalesOrders().observe(this, new Observer<List<SalesOrderItemDto>>() {
            @Override
            public void onChanged(List<SalesOrderItemDto> transactionDtos) {
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
                mDashBoardChart.setData(salesLineGraphDtos);
            }
        });
    }

    @Override
    public void onItemClickListener(long itemId) {
        Intent intent = new Intent(MainActivity.this, SalesOrderEditorActivity.class);
        intent.putExtra(SalesOrderEditorActivity.SALES_ORDER_EDIT_EXTRA, itemId);
        startActivity(intent);
    }
}
