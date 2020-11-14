package com.example.acquatikaapp.data.dto;

import java.util.Date;

public class ExportDataDto {

    private String customerName;

    private Date date;

    private long discount;

    private int orderType;

    private long price;

    private String productName;

    private int quantity;

    private String remarks;

    private long salesOrderId;

    private int status;

    private long totalPrice;

    public ExportDataDto(String customerName, Date date, long discount, int orderType, long price, String productName, int quantity, String remarks, long salesOrderId, int status, long totalPrice) {
        this.customerName = customerName;
        this.date = date;
        this.discount = discount;
        this.orderType = orderType;
        this.price = price;
        this.productName = productName;
        this.quantity = quantity;
        this.remarks = remarks;
        this.salesOrderId = salesOrderId;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getDiscount() {
        return discount;
    }

    public void setDiscount(long discount) {
        this.discount = discount;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getSalesOrderId() {
        return salesOrderId;
    }

    public void setSalesOrderId(long salesOrderId) {
        this.salesOrderId = salesOrderId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
