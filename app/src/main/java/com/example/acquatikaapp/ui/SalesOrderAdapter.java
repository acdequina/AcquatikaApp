package com.example.acquatikaapp.ui;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.acquatikaapp.R;
import com.example.acquatikaapp.data.dto.SalesOrderItemDto;
import com.example.acquatikaapp.ui.util.ValueUtil;

import java.text.SimpleDateFormat;
import java.util.List;

public class SalesOrderAdapter extends RecyclerView.Adapter<SalesOrderAdapter.SalesOrderViewHolder>{

    final private ItemClickListener mItemClickListener;
    private Context mContext;
    private List<SalesOrderItemDto> mTransactions;
    private boolean mIsCurrentTransactions;

    public SalesOrderAdapter(Context context, boolean isCurrentTransactions, ItemClickListener listener) {
        mContext = context;
        mIsCurrentTransactions = isCurrentTransactions;
        mItemClickListener = listener;
    }

    @NonNull
    @Override
    public SalesOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.sales_order_item_layout, parent, false);
        return new SalesOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesOrderViewHolder holder, int position) {
        String dateFormatPattern = "dd/M/yy - hh:mm a";
        SalesOrderItemDto transaction = mTransactions.get(position);

        holder.customerNameTv.setText(transaction.getName());
        holder.salesDetailTv.setText(transaction.getDescription());
        holder.totalPriceTv.setText(
                ValueUtil.convertPriceToDisplayValue(transaction.getTotalPrice()));

        holder.orderTypeStatusIv.setImageResource(getOrderImageId(transaction.getOrderType()));
        GradientDrawable statusCircle = (GradientDrawable) holder.orderTypeStatusIv.getBackground();
        statusCircle.setColor(ContextCompat.getColor(mContext, getStatusColor(transaction.getStatus())));

        if(mIsCurrentTransactions) {
            dateFormatPattern = "hh:mm aa";
        }

        SimpleDateFormat formatter = new SimpleDateFormat(dateFormatPattern);
        String dateDisplayValue = formatter.format(transaction.getDate());

        holder.dateTimeTv.setText(dateDisplayValue);
    }

    @Override
    public int getItemCount() {
        if (mTransactions == null) {
            return 0;
        }

        return mTransactions.size();
    }

    public void setTransactions(List<SalesOrderItemDto> transactions) {
        mTransactions = transactions;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(long itemId);
    }

    class SalesOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView customerNameTv;
        TextView salesDetailTv;
        TextView totalPriceTv;
        TextView dateTimeTv;
        ImageView orderTypeStatusIv;

        public SalesOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            customerNameTv = itemView.findViewById(R.id.customer_name_tv);
            salesDetailTv = itemView.findViewById(R.id.sales_description_tv);
            totalPriceTv = itemView.findViewById(R.id.total_price_tv);
            dateTimeTv = itemView.findViewById(R.id.date_time_tv);
            orderTypeStatusIv = itemView.findViewById(R.id.order_type_status_iv);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            long elementId = mTransactions.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }

    private int getOrderImageId(int orderType) {
        switch (orderType) {
            case 1:
                return R.drawable.ic_local_shipping_white_24dp;
            case 0:
            default:
                return R.drawable.ic_store_white_24dp;
        }
    }

    private int getStatusColor(int status) {
        switch (status) {
            case 1:
                return R.color.flatRed;
            case 0:
            default:
                return R.color.colorAccent;
        }
    }

}
