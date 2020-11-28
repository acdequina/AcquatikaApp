package com.example.acquatikaapp.data.dto;

import java.util.Date;

public class SalesOrderItemDto {

    private long id;
    private String name;
    private long totalPrice;
    private Date date;
    private int orderType;
    private String summary;
    private int status;

    public SalesOrderItemDto(long id, String name, long totalPrice, Date date, int orderType, String summary, int status) {
        this.id = id;
        this.name = name;
        this.totalPrice = totalPrice;
        this.date = date;
        this.orderType = orderType;
        this.summary = summary;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
