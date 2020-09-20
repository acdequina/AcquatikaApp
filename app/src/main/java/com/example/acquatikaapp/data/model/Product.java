package com.example.acquatikaapp.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "product")
public class Product {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private double volume;
    // * 100
    private long price;

    @ColumnInfo(name = "is_on_dashboard")
    private boolean isOnDashboard;

    @Ignore
    public Product(String name, double volume, long price, boolean isOnDashboard) {
        this.name = name;
        this.volume = volume;
        this.price = price;
        this.isOnDashboard = isOnDashboard;
    }

    public Product(int id, String name, double volume, long price, boolean isOnDashboard) {
        this.id = id;
        this.name = name;
        this.volume = volume;
        this.price = price;
        this.isOnDashboard = isOnDashboard;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public boolean getIsOnDashboard() {
        return isOnDashboard;
    }

    public void setIsOnDashboard(boolean isOnDashboard) {
        this.isOnDashboard = isOnDashboard;
    }
}
