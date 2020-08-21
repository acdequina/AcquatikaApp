package com.example.acquatikaapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.acquatikaapp.R;
import com.example.acquatikaapp.data.dto.TransactionDto;
import com.example.acquatikaapp.ui.util.DisplayValueUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>{

    private Context mContext;
    private List<TransactionDto> mTransactions;
    private boolean mIsCurrentTransactions;

    public TransactionAdapter(Context context, boolean isCurrentTransactions) {
        mContext = context;
        mIsCurrentTransactions = isCurrentTransactions;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.transactions_item_layout, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        String dateFormatPattern = "dd/M/yy - hh:mm a";
        TransactionDto transaction = mTransactions.get(position);

        holder.customerNameTv.setText(transaction.getName());
        holder.salesDetailTv.setText(transaction.getDescription());
        holder.totalPriceTv.setText(
                DisplayValueUtil.convertPriceToStringDisplayValue(transaction.getTotalPrice()));

        if(mIsCurrentTransactions) {
            dateFormatPattern = "hh:mm";
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

    public void setTransactions(List<TransactionDto> transactions) {
        mTransactions = transactions;
        notifyDataSetChanged();
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder {

        TextView customerNameTv;
        TextView salesDetailTv;
        TextView totalPriceTv;
        TextView dateTimeTv;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);

            customerNameTv = itemView.findViewById(R.id.customer_name_tv);
            salesDetailTv = itemView.findViewById(R.id.sales_description_tv);
            totalPriceTv = itemView.findViewById(R.id.total_price_tv);
            dateTimeTv = itemView.findViewById(R.id.date_time_tv);
        }
    }
}
