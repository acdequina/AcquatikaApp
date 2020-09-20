package com.example.acquatikaapp.data.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class AddSalesItemDto implements Parcelable {

    private Long id;

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

    public AddSalesItemDto(Long id, long price, int quantity, int productId, String productName) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.productId = productId;
        this.productName = productName;
    }

    protected AddSalesItemDto(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        price = in.readLong();
        quantity = in.readInt();
        productId = in.readInt();
        productName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeLong(price);
        dest.writeInt(quantity);
        dest.writeInt(productId);
        dest.writeString(productName);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
