package com.example.acquatikaapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.acquatikaapp.R;
import com.example.acquatikaapp.data.dto.DashboardProductsCountDto;
import com.example.acquatikaapp.data.dto.SalesOrderItemDto;
import com.example.acquatikaapp.data.model.Product;
import com.example.acquatikaapp.ui.util.DisplayValueUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

public class MainActivity extends AppCompatActivity implements SalesOrderAdapter.ItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView mCurrentTotalSalesTv;
    private TextView mProductLeftLabelTv;
    private TextView mProductCenterLabelTv;
    private TextView mProductLeftCountTv;
    private TextView mProductCenterCountTv;
    private TextView mOthersCountTv;

    private FloatingActionButton mAddSalesOrderButton;

    private RecyclerView mCurrentSalesOrderRv;
    private SalesOrderAdapter mSalesOrderAdapter;

    private List<Product> mProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        mCurrentTotalSalesTv = findViewById(R.id.current_total_sales_tv);
        mProductLeftLabelTv = findViewById(R.id.product_left_label_tv);
        mProductLeftCountTv = findViewById(R.id.product_left_count_tv);
        mProductCenterLabelTv = findViewById(R.id.product_center_label_tv);
        mProductCenterCountTv = findViewById(R.id.product_center_count_tv);
        mOthersCountTv = findViewById(R.id.others_count_tv);

        mAddSalesOrderButton = findViewById(R.id.sales_order_add_fab);
        mAddSalesOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductDialog();
            }
        });

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
                mCurrentTotalSalesTv.setText(DisplayValueUtil
                        .convertPriceToDisplayValue(aLong != null ? aLong : 0L));
            }
        });

        viewModel.getDashboardProductsCount().observe(this, new Observer<DashboardProductsCountDto>() {
            @Override
            public void onChanged(DashboardProductsCountDto products) {
                if(products.getProductLeftLabel() != null) {
                    mProductLeftLabelTv.setText(products.getProductLeftLabel() + ": ");
                }

                if(products.getProductCenterLabel()  != null) {
                    mProductCenterLabelTv.setText(products.getProductCenterLabel() + ": ");
                }

                mProductLeftCountTv.setText(String.valueOf(products.getProductLeftCount()));
                mProductCenterCountTv.setText(String.valueOf(products.getProductCenterCount()));
                mOthersCountTv.setText(String.valueOf(products.getProductOthersCount()));
            }
        });

        viewModel.getAllProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                mProducts = products;
            }
        });
    }

    private void addProductDialog() {
        AddSalesItemDialog addSalesItemDialog = new AddSalesItemDialog(mProducts);
        addSalesItemDialog.show(getSupportFragmentManager(), "Select a Product");
    }

    @Override
    public void onItemClickListener(long itemId) {
        Intent intent = new Intent(MainActivity.this, SalesOrderEditorActivity.class);
        intent.putExtra(SalesOrderEditorActivity.SALES_ORDER_EDIT_EXTRA, itemId);
        startActivity(intent);
    }
}
