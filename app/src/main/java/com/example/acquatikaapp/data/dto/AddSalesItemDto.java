package com.example.acquatikaapp.data.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class AddSalesItemDto implements Parcelable {

    private long price;

    private int quantity;

    private int productId;

    private String productName;

    public AddSalesItemDto() {
    }

    public AddSalesItemDto(int productId, String productName, long price, int quantity) {
        this.price = price;
        this.quantity = quantity;
        this.productId = productId;
        this.productName = productName;
    }

    protected AddSalesItemDto(Parcel in) {
        price = in.readLong();
        quantity = in.readInt();
        productId = in.readInt();
        productName = in.readString();
    }

    public static final Creator<AddSalesItemDto> CREATOR = new Creator<AddSalesItemDto>() {
        @Override
        public AddSalesItemDto createFromParcel(Parcel in) {
            return new AddSalesItemDto(in);
        }

        @Override
        public AddSalesItemDto[] newArray(int size) {
            return new AddSalesItemDto[size];
        }
    };

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

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(price);
        dest.writeInt(quantity);
        dest.writeInt(productId);
        dest.writeString(productName);
    }
}
