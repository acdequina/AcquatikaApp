package com.example.acquatikaapp.data.dto;

import java.util.Date;

public class SalesLineGraphDto {

    public long totalPrice;
    public Date date;

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
