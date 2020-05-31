package com.example.acquatikaapp.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sales_detail")
public class SalesDetail {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "product_id")
    private int productId;

    // * 100
    private long price;
    private int quantity;

    @ColumnInfo(name = "sales_order_id")
    private long salesOrderId;

    public SalesDetail(int productId, long price, int quantity, long salesOrderId) {
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.salesOrderId = salesOrderId;
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
}
