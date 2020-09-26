package com.example.acquatikaapp.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.acquatikaapp.R;
import com.example.acquatikaapp.data.dto.AddSalesItemDto;
import com.example.acquatikaapp.data.model.Product;
import com.example.acquatikaapp.data.util.Constants;
import com.example.acquatikaapp.ui.util.ValueUtil;

import java.util.ArrayList;
import java.util.List;

public class AddSalesItemDialog extends AppCompatDialogFragment {

    private TextView mLeftProductCountTv;
    private ImageView mLeftProductIv;
    private TextView mCenterProductCountTv;
    private ImageView mCenterProductIv;
    private TextView mCustomProductCountTv;
    private ImageView mCustomProductIv;
    private List<Product> mProducts;
    private ArrayList<AddSalesItemDto> mAddSalesItemDtos;
    private LinearLayout mCustomProductFormLl;
    private EditText mCustomProductNameEt;
    private EditText mCustomProductPriceEt;

    private AddSalesItemDto leftProductDto;
    private AddSalesItemDto centerProductDto;
    private AddSalesItemDto customProductDto;

    private boolean mHasAddedProduct = false;
    private boolean mHasCustomProduct = false;

    public AddSalesItemDialog(List<Product> products) {
        mProducts = products;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.add_sales_item_dialog, null);
        setupLayout(view);

        builder.setView(view);
        builder.setTitle("Select a Product");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent addSalesOrderIntent = new Intent(getActivity(), SalesOrderEditorActivity.class);
                getSalesDetailData();
                if(mAddSalesItemDtos == null || mAddSalesItemDtos.isEmpty()) {
                    Toast.makeText(getContext(), "Please select at least 1 product.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(SalesOrderEditorActivity.SALES_ORDER_ADD_EXTRA, mAddSalesItemDtos);
                addSalesOrderIntent.putExtra(SalesOrderEditorActivity.SALES_ORDER_ADD_EXTRA, bundle);
                startActivity(addSalesOrderIntent);

            }
        });

        return builder.create();
    }

    private void setupLayout(View view) {
        mCustomProductFormLl = view.findViewById(R.id.custom_product_form_ll);
        mCustomProductNameEt = view.findViewById(R.id.dialog_customer_product_name_et);
        mCustomProductPriceEt = view.findViewById(R.id.dialog_customer_product_price_et);
        // TODO temporary static, revise on next version for dynamic buttons
        final Product leftProduct = mProducts.get(Constants.SLIM);
        leftProductDto =
                new AddSalesItemDto(leftProduct.getId(), leftProduct.getName(), 0, 0);

        mLeftProductCountTv = view.findViewById(R.id.dialog_left_product_count_tv);
        mLeftProductCountTv.setOnTouchListener(onTouchListener);
        mLeftProductCountTv.setText(leftProduct.getName());
        mLeftProductIv = view.findViewById(R.id.dialog_left_product_iv);
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

        mCenterProductCountTv = view.findViewById(R.id.dialog_center_product_count_tv);
        mCenterProductCountTv.setOnTouchListener(onTouchListener);
        mCenterProductCountTv.setText(centerProduct.getName());
        mCenterProductIv = view.findViewById(R.id.dialog_center_product_iv);
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

        mCustomProductCountTv = view.findViewById(R.id.dialog_custom_count_tv);
        mCustomProductCountTv.setOnTouchListener(onTouchListener);
        mCustomProductCountTv.setText(customProduct.getName());
        mCustomProductIv = view.findViewById(R.id.dialog_custom_iv);
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

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mHasAddedProduct = true;
            return false;
        }
    };

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
