package com.example.acquatikaapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acquatikaapp.R;
import com.example.acquatikaapp.data.dto.AddSalesItemDto;
import com.example.acquatikaapp.data.model.Product;
import com.example.acquatikaapp.data.util.Constants;
import com.example.acquatikaapp.ui.util.ValueUtil;

import java.util.ArrayList;
import java.util.Map;

public class AddSalesItemActivity extends AppCompatActivity {

    private TextView mSlimProductTv;
    private ImageView mSlimProductIv;
    private TextView mRoundProductCountTv;
    private ImageView mRoundProductIv;
    private TextView mCustomProductCountTv;
    private ImageView mCustomProductIv;
    private Map<Integer, Product> productsMap;
    private ArrayList<AddSalesItemDto> mAddSalesItemDtos;
    private LinearLayout mCustomProductFormLl;
    private EditText mCustomProductNameEt;
    private EditText mCustomProductPriceEt;
    private Button mSalesItemPlaceOrderBtn;

    private AddSalesItemDto slimItems;
    private AddSalesItemDto roundItems;
    private AddSalesItemDto customItems;

    private boolean mHasCustomProduct = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sales_item);
        setTitle("Select a Product");
        setupLayout();
        setupViewModel();
    }

    private void setupViewModel() {
        final AddSalesItemViewModel viewModel = ViewModelProviders.of(this).get(AddSalesItemViewModel.class);

        viewModel.getProductsMap().observe(this, new Observer<Map<Integer, Product>>() {
            @Override
            public void onChanged(Map<Integer, Product> integerProductMap) {
                productsMap = integerProductMap;
                initializeItemActions();
            }
        });

    }

    private void initializeItemActions() {
        // TODO temporary static, revise on next version for dynamic buttons
        final Product slimProduct = productsMap.get(Constants.SLIM);
        slimItems = new AddSalesItemDto(slimProduct.getId(), slimProduct.getName(), 0, 0);

        mSlimProductTv = findViewById(R.id.add_item_left_product_count_tv);
        mSlimProductTv.setText(slimProduct.getName());
        mSlimProductIv = findViewById(R.id.add_item_left_product_iv);
        mSlimProductIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = slimItems.getQuantity();
                slimItems.setQuantity(++count);
                mSlimProductTv.setText(String.valueOf(count));
            }
        });

        final Product roundProduct = productsMap.get(Constants.ROUND);
        roundItems = new AddSalesItemDto(roundProduct.getId(), roundProduct.getName(), 0, 0);

        mRoundProductCountTv = findViewById(R.id.add_item_center_product_count_tv);
        mRoundProductCountTv.setText(roundProduct.getName());
        mRoundProductIv = findViewById(R.id.add_item_center_product_iv);
        mRoundProductIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = roundItems.getQuantity();
                roundItems.setQuantity(++count);
                mRoundProductCountTv.setText(String.valueOf(count));
            }
        });

        final Product customProduct = productsMap.get(Constants.CUSTOM);
        customItems = new AddSalesItemDto(customProduct.getId(), customProduct.getName(), 0, 0);

        mCustomProductCountTv = findViewById(R.id.add_item_custom_count_tv);
        mCustomProductCountTv.setText(customProduct.getName());
        mCustomProductIv = findViewById(R.id.add_item_custom_iv);
        mCustomProductIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mHasCustomProduct) {
                    mCustomProductFormLl.setVisibility(View.VISIBLE);
                    mHasCustomProduct = true;
                }

                int count = AddSalesItemActivity.this.customItems.getQuantity();
                AddSalesItemActivity.this.customItems.setQuantity(++count);
                mCustomProductCountTv.setText(String.valueOf(count));
            }
        });
    }

    private void setupLayout() {
        mCustomProductFormLl = findViewById(R.id.custom_product_form_ll);
        mCustomProductNameEt = findViewById(R.id.add_item_customer_product_name_et);
        mCustomProductPriceEt = findViewById(R.id.add_item_customer_product_price_et);
        mSalesItemPlaceOrderBtn = findViewById(R.id.sales_item_place_order_btn);

        mSalesItemPlaceOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addSalesOrderIntent = new Intent(AddSalesItemActivity.this, SalesOrderEditorActivity.class);
                getSalesDetailData();
                if(mAddSalesItemDtos == null || mAddSalesItemDtos.isEmpty()) {
                    Toast.makeText(AddSalesItemActivity.this, "Please select at least 1 product.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(SalesOrderEditorActivity.SALES_ORDER_ADD_EXTRA, mAddSalesItemDtos);
                addSalesOrderIntent.putExtra(SalesOrderEditorActivity.SALES_ORDER_ADD_EXTRA, bundle);
                startActivity(addSalesOrderIntent);

            }
        });
    }

    private void getSalesDetailData() {
        mAddSalesItemDtos = new ArrayList<>();
        if(slimItems.getQuantity() != 0) {
            Product slimProduct = productsMap.get(slimItems.getProductId());
            slimItems.setPrice(slimItems.getQuantity()* slimProduct.getPrice());
            mAddSalesItemDtos.add(slimItems);
        }

        if(roundItems.getQuantity() != 0) {
            Product roundProduct = productsMap.get(roundItems.getProductId());
            roundItems.setPrice(roundItems.getQuantity() * roundProduct.getPrice());
            mAddSalesItemDtos.add(roundItems);
        }

        if(customItems.getQuantity() != 0) {
            if(mCustomProductNameEt.getText() != null) {
                customItems.setProductName(mCustomProductNameEt.getText().toString().trim());
            }

            long productPrice = 0;
            if(mCustomProductPriceEt.getText() != null && !mCustomProductPriceEt.getText().toString().isEmpty()) {
                productPrice = ValueUtil.convertDisplayPriceToLong(mCustomProductPriceEt.getText().toString());
            }

            customItems.setPrice(productPrice * customItems.getQuantity());

            mAddSalesItemDtos.add(customItems);
        }
    }
}
