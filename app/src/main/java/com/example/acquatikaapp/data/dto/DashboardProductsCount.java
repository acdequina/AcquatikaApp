package com.example.acquatikaapp.data.dto;

public class DashboardProductsCount {

    private String productLeftLabel;

    private int productLeftCount;

    private String productCenterLabel;

    private int productCenterCount;

    private int productOthersCount;

    public DashboardProductsCount() {
    }

    public DashboardProductsCount(String productLeftLabel, int productLeftCount, String productCenterLabel, int productCenterCount, int productOthersCount) {
        this.productLeftLabel = productLeftLabel;
        this.productLeftCount = productLeftCount;
        this.productCenterLabel = productCenterLabel;
        this.productCenterCount = productCenterCount;
        this.productOthersCount = productOthersCount;
    }

    public String getProductLeftLabel() {
        return productLeftLabel;
    }

    public void setProductLeftLabel(String productLeftLabel) {
        this.productLeftLabel = productLeftLabel;
    }

    public int getProductLeftCount() {
        return productLeftCount;
    }

    public void setProductLeftCount(int productLeftCount) {
        this.productLeftCount = productLeftCount;
    }

    public String getProductCenterLabel() {
        return productCenterLabel;
    }

    public void setProductCenterLabel(String productCenterLabel) {
        this.productCenterLabel = productCenterLabel;
    }

    public int getProductCenterCount() {
        return productCenterCount;
    }

    public void setProductCenterCount(int productCenterCount) {
        this.productCenterCount = productCenterCount;
    }

    public int getProductOthersCount() {
        return productOthersCount;
    }

    public void setProductOthersCount(int productOthersCount) {
        this.productOthersCount = productOthersCount;
    }
}
