package com.example.acquatikaapp.data.dto;

public class CustomerTotalPurchaseDto {
    public String name;
    public long totalPurchase;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTotalPurchase() {
        return totalPurchase;
    }

    public void setTotalPurchase(long totalPurchase) {
        this.totalPurchase = totalPurchase;
    }
}
