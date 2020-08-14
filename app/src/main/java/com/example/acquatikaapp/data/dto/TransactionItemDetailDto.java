package com.example.acquatikaapp.data.dto;

import androidx.lifecycle.LiveData;
import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.acquatikaapp.data.model.Customer;
import com.example.acquatikaapp.data.model.SalesDetail;
import com.example.acquatikaapp.data.model.SalesOrder;

import java.util.List;

public class TransactionItemDetailDto {

    @Embedded
    public LiveData<SalesOrder> salesOrder;

    @Relation(
            parentColumn = "id",
            entityColumn = "sales_order_id"
    )
    public LiveData<List<SalesDetail>> salesDetails;
}
