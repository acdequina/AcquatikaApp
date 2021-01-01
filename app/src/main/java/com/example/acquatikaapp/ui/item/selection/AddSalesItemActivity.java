package com.example.acquatikaapp.ui.item.selection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.acquatikaapp.R;
import com.example.acquatikaapp.data.dto.AddSalesItemDto;
import com.example.acquatikaapp.data.model.Product;
import com.example.acquatikaapp.data.util.Constants;
import com.example.acquatikaapp.ui.sales.editor.SalesOrderEditorActivity;
import com.example.acquatikaapp.ui.util.ValueUtil;

import java.util.ArrayList;
import java.util.Map;

public class AddSalesItemActivity extends AppCompatActivity {

    private TextView mSlimProductCountTv;
    private ImageView mSlimProductIv;
    private TextView mRoundProductCountTv;
    private ImageView mRoundProductIv;
    private TextView mCustomProductCountTv;
    private ImageView mCustomProductIv;
    private Map<Integer, Product> productsMap;
    private LinearLayout mCustomProductFormLl;
    private EditText mCustomProductNameEt;
    private EditText mCustomProductPriceEt;
    private Button mSalesItemPlaceOrderBtn;

    private AddSalesItemViewModel viewModel;

    private boolean mHasCustomProduct = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sales_item);
        setTitle(getString(R.string.select_a_product));
        setupLayout();
        setupViewModel();
    }

    private void setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(AddSalesItemViewModel.class);

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
        Product slimProduct = productsMap.get(Constants.SLIM);
        mSlimProductCountTv = findViewById(R.id.add_item_left_product_count_tv);
        mSlimProductCountTv.setText(slimProduct.getName());
        mSlimProductIv = findViewById(R.id.add_item_left_product_iv);
        mSlimProductIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuantity(slimProduct, mSlimProductCountTv);
            }
        });

        Product roundProduct = productsMap.get(Constants.ROUND);
        mRoundProductCountTv = findViewById(R.id.add_item_center_product_count_tv);
        mRoundProductCountTv.setText(roundProduct.getName());
        mRoundProductIv = findViewById(R.id.add_item_center_product_iv);
        mRoundProductIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQuantity(roundProduct, mRoundProductCountTv);
            }
        });

        final Product customProduct = productsMap.get(Constants.CUSTOM);
        mCustomProductCountTv = findViewById(R.id.add_item_custom_count_tv);
        mCustomProductCountTv.setText(customProduct.getName());
        mCustomProductIv = findViewById(R.id.add_item_custom_iv);
        mCustomProductIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mHasCustomProduct) {
                    mCustomProductFormLl.setVisibility(View.VISIBLE);
                    mHasCustomProduct = true;
                }

                addQuantity(customProduct, mCustomProductCountTv);
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
                ArrayList<AddSalesItemDto> addSalesItemDtos = getData();
                if (addSalesItemDtos == null || addSalesItemDtos.isEmpty()) {
                    Toast.makeText(AddSalesItemActivity.this, R.string.no_product_selected, Toast.LENGTH_SHORT).show();
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(SalesOrderEditorActivity.SALES_ORDER_ADD_EXTRA, addSalesItemDtos);
                addSalesOrderIntent.putExtra(SalesOrderEditorActivity.SALES_ORDER_ADD_EXTRA, bundle);
                startActivity(addSalesOrderIntent);

            }
        });
    }

    private ArrayList<AddSalesItemDto> getData() {

        AddSalesItemDto customItem = viewModel.getSalesItem(productsMap.get(Constants.CUSTOM));

        if (mCustomProductNameEt.getText() != null) {
            customItem.setProductName(mCustomProductNameEt.getText().toString().trim());
        }

        long customPrice = 0;
        if (mCustomProductPriceEt.getText() != null && !mCustomProductPriceEt.getText().toString().isEmpty()) {
            customPrice = ValueUtil.convertDisplayPriceToLong(mCustomProductPriceEt.getText().toString());
            customItem.setPrice(customPrice * customItem.getQuantity());
        }

        return viewModel.generateSalesDetailData();
    }

    private void addQuantity(Product product, TextView textView) {
        AddSalesItemDto item = viewModel.getSalesItem(product);
        int newQuantity = viewModel.calculateAddedItem(item);
        textView.setText(String.valueOf(newQuantity));
    }
}
