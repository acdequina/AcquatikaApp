package com.example.acquatikaapp.data.dto;

import androidx.room.Ignore;

import com.example.acquatikaapp.data.model.SalesDetail;

public class SalesDetailDto extends SalesDetail {

    private String productName;

    @Ignore
    public SalesDetailDto(int productId, long price, int quantity, long salesOrderId, String remarks, String productName) {
        super(productId, price, quantity, salesOrderId, remarks);
        this.productName = productName;
    }


    public SalesDetailDto(long id, int productId, long price, int quantity, long salesOrderId, String remarks, String productName) {
        super(id, productId, price, quantity, salesOrderId, remarks);
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
