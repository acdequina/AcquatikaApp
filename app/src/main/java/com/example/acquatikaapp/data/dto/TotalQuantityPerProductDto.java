package com.example.acquatikaapp.data.dto;

public class TotalQuantityPerProductDto {

    private String name;
    private int total;
    private boolean isOnDashboard;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isOnDashboard() {
        return isOnDashboard;
    }

    public void setOnDashboard(boolean onDashboard) {
        isOnDashboard = onDashboard;
    }
}
