package com.example.acquatikaapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.acquatikaapp.R;
import com.example.acquatikaapp.data.dto.DashboardProductsCount;
import com.example.acquatikaapp.data.dto.ProductNameIdDto;
import com.example.acquatikaapp.data.dto.SalesOrderDto;
import com.example.acquatikaapp.data.dto.TotalQuantityPerProductDto;
import com.example.acquatikaapp.ui.util.DisplayValueUtil;

import java.util.List;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView mCurrentTotalSalesTv;
    private TextView mProductLeftLabelTv;
    private TextView mProductCenterLabelTv;
    private TextView mProductLeftCountTv;
    private TextView mProductCenterCountTv;
    private TextView mOthersCountTv;


    private RecyclerView mCurrentSalesOrderRv;
    private TransactionAdapter mTransactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupLayout();
        mTransactionAdapter = new TransactionAdapter(this, true);
        mCurrentSalesOrderRv.setAdapter(mTransactionAdapter);
        setupViewModel();
    }

    private void setupLayout() {
        mCurrentSalesOrderRv = findViewById(R.id.current_sales_orders_rv);
        mCurrentSalesOrderRv.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mCurrentSalesOrderRv.addItemDecoration(decoration);
        mCurrentSalesOrderRv.setHasFixedSize(true);

        mCurrentTotalSalesTv = findViewById(R.id.current_total_sales_tv);
        mProductLeftLabelTv = findViewById(R.id.product_left_label_tv);
        mProductLeftCountTv = findViewById(R.id.product_left_count_tv);
        mProductCenterLabelTv = findViewById(R.id.product_center_label_tv);
        mProductCenterCountTv = findViewById(R.id.product_center_count_tv);
        mOthersCountTv = findViewById(R.id.others_count_tv);
    }

    private void setupViewModel() {
        final MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        viewModel.getCurrentSalesOrders().observe(this, new Observer<List<SalesOrderDto>>() {
            @Override
            public void onChanged(List<SalesOrderDto> transactionDtos) {
                Log.d(TAG, "Updating list of transactions from LiveData in ViewModel");
                mTransactionAdapter.setTransactions(transactionDtos);
            }
        });

        viewModel.getCurrentTotalSales().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                Long total = 0L;
                if(aLong != null) {
                    total = aLong;
                }

                mCurrentTotalSalesTv.setText(DisplayValueUtil.convertPriceToStringDisplayValue(total));
            }
        });

        viewModel.getDashboardProductsCount().observe(this, new Observer<DashboardProductsCount>() {
            @Override
            public void onChanged(DashboardProductsCount products) {
                mProductLeftLabelTv.setText(products.getProductLeftLabel() + ": ");
                mProductLeftCountTv.setText(String.valueOf(products.getProductLeftCount()));
                mProductCenterLabelTv.setText(products.getProductCenterLabel() + ": ");
                mProductCenterCountTv.setText(String.valueOf(products.getProductCenterCount()));
                mOthersCountTv.setText(String.valueOf(products.getProductOthersCount()));
            }
        });
    }
}
