package com.example.acquatikaapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.acquatikaapp.R;
import com.example.acquatikaapp.data.dto.AddSalesItemDto;
import com.example.acquatikaapp.data.model.Product;
import com.example.acquatikaapp.data.util.Constants;
import com.example.acquatikaapp.ui.util.ValueUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddSalesItemActivity extends AppCompatActivity {

    private TextView mLeftProductCountTv;
    private ImageView mLeftProductIv;
    private TextView mCenterProductCountTv;
    private ImageView mCenterProductIv;
    private TextView mCustomProductCountTv;
    private ImageView mCustomProductIv;
    private Map<Integer, Product> productsMap;
    private ArrayList<AddSalesItemDto> mAddSalesItemDtos;
    private LinearLayout mCustomProductFormLl;
    private EditText mCustomProductNameEt;
    private EditText mCustomProductPriceEt;

    private AddSalesItemDto leftProductDto;
    private AddSalesItemDto centerProductDto;
    private AddSalesItemDto customProductDto;

    private boolean mHasCustomProduct = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sales_item);

        setupLayout();
        setupViewModel();
    }

    private void setupViewModel() {
        final AddSalesItemViewModel viewModel = ViewModelProviders.of(this).get(AddSalesItemViewModel.class);

        viewModel.getProductsMap().observe(this, new Observer<Map<Integer, Product>>() {
            @Override
            public void onChanged(Map<Integer, Product> integerProductMap) {
                productsMap = integerProductMap;
            }
        });

    }

    private void initializeItemActions() {
        // TODO temporary static, revise on next version for dynamic buttons
        final Product leftProduct = mProducts.get(Constants.SLIM);
        leftProductDto =
                new AddSalesItemDto(leftProduct.getId(), leftProduct.getName(), 0, 0);

        mLeftProductCountTv = findViewById(R.id.add_item_left_product_count_tv);
        mLeftProductCountTv.setText(leftProduct.getName());
        mLeftProductIv = findViewById(R.id.add_item_left_product_iv);
        mLeftProductIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = leftProductDto.getQuantity();
                leftProductDto.setQuantity(++count);
                mLeftProductCountTv.setText(String.valueOf(count));
            }
        });

        final Product centerProduct = mProducts.get(Constants.ROUND);
        centerProductDto =
                new AddSalesItemDto(centerProduct.getId(), centerProduct.getName(), 0, 0);

        mCenterProductCountTv = findViewById(R.id.add_item_center_product_count_tv);
        mCenterProductCountTv.setText(centerProduct.getName());
        mCenterProductIv = findViewById(R.id.add_item_center_product_iv);
        mCenterProductIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = centerProductDto.getQuantity();
                centerProductDto.setQuantity(++count);
                mCenterProductCountTv.setText(String.valueOf(count));
            }
        });

        final Product customProduct = mProducts.get(Constants.CUSTOM);
        customProductDto =
                new AddSalesItemDto(customProduct.getId(), customProduct.getName(), 0, 0);

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

                int count = customProductDto.getQuantity();
                customProductDto.setQuantity(++count);
                mCustomProductCountTv.setText(String.valueOf(count));
            }
        });
    }

    private void setupLayout() {
        mCustomProductFormLl = findViewById(R.id.custom_product_form_ll);
        mCustomProductNameEt = findViewById(R.id.add_item_customer_product_name_et);
        mCustomProductPriceEt = findViewById(R.id.add_item_customer_product_price_et);

    }

    private ArrayList<AddSalesItemDto> getSalesDetailData() {
        mAddSalesItemDtos = new ArrayList<>();
        if(leftProductDto.getQuantity() != 0) {
            Product leftProduct = mProducts.get(Constants.SLIM);
            leftProductDto.setPrice(leftProductDto.getQuantity()* leftProduct.getPrice());
            mAddSalesItemDtos.add(leftProductDto);
        }

        if(centerProductDto.getQuantity() != 0) {
            Product centerProduct = mProducts.get(Constants.ROUND);
            centerProductDto.setPrice(centerProductDto.getQuantity() * centerProduct.getPrice());
            mAddSalesItemDtos.add(centerProductDto);
        }

        if(customProductDto.getQuantity() != 0) {
            if(mCustomProductNameEt.getText() != null) {
                customProductDto.setProductName(mCustomProductNameEt.getText().toString().trim());
            }

            long productPrice = 0;
            if(mCustomProductPriceEt.getText() != null && !mCustomProductPriceEt.getText().toString().isEmpty()) {
                productPrice = ValueUtil.convertDisplayPriceToLong(mCustomProductPriceEt.getText().toString());
            }

            customProductDto.setPrice(productPrice * customProductDto.getQuantity());

            mAddSalesItemDtos.add(customProductDto);
        }

        return mAddSalesItemDtos;
    }
}
