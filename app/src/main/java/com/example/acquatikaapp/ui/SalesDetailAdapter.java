package com.example.acquatikaapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import com.example.acquatikaapp.R;
import com.example.acquatikaapp.data.dto.SalesDetailDto;
import com.example.acquatikaapp.ui.util.ValueUtil;

public class SalesDetailAdapter extends ArrayAdapter<SalesDetailDto> {

    public SalesDetailAdapter(@NonNull Context context, List<SalesDetailDto> salesDetails) {
        super(context, 0, salesDetails);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.sales_detail_item_layout, parent, false);
        }

        SalesDetailDto salesDetail = getItem(position);

        TextView quantityTv = listItemView.findViewById(R.id.editor_quantity_tv);
        TextView priceTv = listItemView.findViewById(R.id.editor_price_tv);
        TextView nameTv = listItemView.findViewById(R.id.editor_product_name_tv);

        quantityTv.setText(String.valueOf(salesDetail.getQuantity()));
        priceTv.setText(ValueUtil.convertPriceToDisplayValue(salesDetail.getPrice()));
        nameTv.setText(salesDetail.getProductName());

        return listItemView;
    }
}
