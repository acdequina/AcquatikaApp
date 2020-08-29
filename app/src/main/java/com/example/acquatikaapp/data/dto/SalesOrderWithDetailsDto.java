package com.example.acquatikaapp.data.dto;

import androidx.lifecycle.LiveData;
import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.acquatikaapp.data.model.Customer;
import com.example.acquatikaapp.data.model.SalesDetail;
import com.example.acquatikaapp.data.model.SalesOrder;

import java.util.List;

public class SalesOrderWithDetailsDto {

    @Embedded
    private SalesOrder salesOrder;

    @Relation(parentColumn = "id", entityColumn = "sales_order_id", entity = SalesDetail.class)
    private List<SalesDetail> salesDetails;

    public SalesOrderWithDetailsDto(SalesOrder salesOrder, List<SalesDetail> salesDetails) {
        this.salesOrder = salesOrder;
        this.salesDetails = salesDetails;
    }

    public SalesOrder getSalesOrder() {
        return salesOrder;
    }

    public void setSalesOrder(SalesOrder salesOrder) {
        this.salesOrder = salesOrder;
    }

    public List<SalesDetail> getSalesDetails() {
        return salesDetails;
    }

    public void setSalesDetails(List<SalesDetail> salesDetails) {
        this.salesDetails = salesDetails;
    }
}
