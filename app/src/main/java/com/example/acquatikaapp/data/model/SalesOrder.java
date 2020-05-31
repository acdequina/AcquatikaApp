package com.example.acquatikaapp.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "sales_order",
        indices = {@Index("dateIdx"), @Index(value = {"date"}, unique = true),
        @Index("customerIdx"), @Index(value = {"customerId"})})
public class SalesOrder {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private Date date;

    @ColumnInfo(name = "receipt_number")
    private String receiptNumber;

    //values = PENDING, DONE
    private int status;

    //values = OTC, DELIVERY
    @ColumnInfo(name = "transactionType")
    private int transactionType;

    // * 100
    private long price;

    @ColumnInfo(name = "customer_id")
    private int customerId;

    public SalesOrder(Date date, String receiptNumber, int status, int transactionType, long price, int customerId) {
        this.date = date;
        this.receiptNumber = receiptNumber;
        this.status = status;
        this.transactionType = transactionType;
        this.price = price;
        this.customerId = customerId;
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

    public int getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
