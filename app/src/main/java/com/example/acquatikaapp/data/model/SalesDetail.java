package com.example.acquatikaapp.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "sales_detail",
        indices = {@Index(name = "salesOrderIdx", value = {"sales_order_id"})})
public class SalesDetail {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "product_id")
    private int productId;

    // snapshot of the current price upon purchase
    // * 100
    private long price;

    private int quantity;

    @ColumnInfo(name = "sales_order_id")
    private long salesOrderId;

    private String remarks;

    @Ignore
    public SalesDetail(int productId, long price, int quantity, long salesOrderId, String remarks) {
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.salesOrderId = salesOrderId;
        this.remarks = remarks;
    }

    public SalesDetail(long id, int productId, long price, int quantity, long salesOrderId, String remarks) {
        this.id = id;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.salesOrderId = salesOrderId;
        this.remarks = remarks;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
