package com.example.acquatikaapp.data.dto;

import androidx.room.Ignore;

import com.example.acquatikaapp.data.dao.SalesOrderDao;
import com.example.acquatikaapp.data.model.SalesOrder;

import java.util.Date;

public class SalesOrderDto extends SalesOrder {

    private String customerName;

    public SalesOrderDto(long id, Date date, String receiptNumber, int status, int orderType,
                         long totalPrice, long discount, int customerId, String description, String customerName) {
        super(id, date, receiptNumber, status, orderType, totalPrice, discount, customerId, description);
        this.customerName = customerName;
    }

    @Ignore
    public SalesOrderDto(long totalPrice, String description) {
        super(totalPrice, description);
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
