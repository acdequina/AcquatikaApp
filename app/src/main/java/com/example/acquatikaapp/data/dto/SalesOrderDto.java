package com.example.acquatikaapp.data.dto;

import androidx.room.Ignore;

import com.example.acquatikaapp.data.dao.SalesOrderDao;
import com.example.acquatikaapp.data.model.SalesOrder;

import java.util.Date;

public class SalesOrderDto extends SalesOrder {

    private String customerName;

    public SalesOrderDto(long id, Date date, String receiptNumber, int status, int orderType, long deliveryCharge,
                         long totalPrice, long discount, int customerId, String summary, String remarks, String customerName, boolean isDistributor) {
        super(id, date, receiptNumber, status, orderType, deliveryCharge, totalPrice, discount, customerId, summary, remarks, isDistributor);
        this.customerName = customerName;
    }

    @Ignore
    public SalesOrderDto(Date date, long totalPrice, long deliveryCharge, String summary) {
        super(date, totalPrice, deliveryCharge, summary);
    }

    @Ignore
    public SalesOrderDto() {}

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
