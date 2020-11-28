package com.example.acquatikaapp.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "sales_order",
        indices = {@Index(name = "dateIdx", value = {"date"}, unique = true),
        @Index(name = "customerIdx", value = {"customer_id"})})
public class SalesOrder {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private Date date;

    @ColumnInfo(name = "receipt_number")
    private String receiptNumber;

    //values = PENDING, DONE
    private int status;

    //values = OTC, DELIVERY
    @ColumnInfo(name = "order_type")
    private int orderType;

    // * 100
    @ColumnInfo(name = "delivery_charge")
    private long deliveryCharge;

    // * 100
    @ColumnInfo(name = "total_price")
    private long totalPrice;

    private long discount;

    @ColumnInfo(name = "customer_id")
    private int customerId;

    private String summary;

    private String remarks;


    @Ignore
    public SalesOrder(Date date, String receiptNumber, int status, int orderType, long deliveryCharge, long totalPrice, long discount, int customerId, String summary, String remarks) {
        this.date = date;
        this.receiptNumber = receiptNumber;
        this.status = status;
        this.orderType = orderType;
        this.deliveryCharge = deliveryCharge;
        this.totalPrice = totalPrice;
        this.discount = discount;
        this.customerId = customerId;
        this.summary = summary;
        this.remarks = remarks;
    }

    @Ignore
    public SalesOrder(long totalPrice, String summary) {
        this.totalPrice = totalPrice;
        this.summary = summary;
    }

    public SalesOrder(long id, Date date, String receiptNumber, int status, int orderType, long deliveryCharge, long totalPrice, long discount, int customerId, String summary, String remarks) {
        this.id = id;
        this.date = date;
        this.receiptNumber = receiptNumber;
        this.status = status;
        this.orderType = orderType;
        this.deliveryCharge = deliveryCharge;
        this.totalPrice = totalPrice;
        this.discount = discount;
        this.customerId = customerId;
        this.summary = summary;
        this.remarks = remarks;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getDiscount() {
        return discount;
    }

    public void setDiscount(long discount) {
        this.discount = discount;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public long getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(long deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }
}
