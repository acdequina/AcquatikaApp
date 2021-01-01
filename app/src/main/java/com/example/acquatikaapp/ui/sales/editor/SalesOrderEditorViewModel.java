package com.example.acquatikaapp.ui.sales.editor;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.acquatikaapp.data.dto.AddSalesItemDto;
import com.example.acquatikaapp.data.dto.SalesDetailDto;
import com.example.acquatikaapp.data.dto.SalesOrderDto;
import com.example.acquatikaapp.data.repository.CustomerRepository;
import com.example.acquatikaapp.data.repository.ProductRepository;
import com.example.acquatikaapp.data.repository.SalesDetailRepository;
import com.example.acquatikaapp.data.repository.SalesOrderRepository;
import com.example.acquatikaapp.data.util.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SalesOrderEditorViewModel extends AndroidViewModel {

    private CustomerRepository mCustomerRepository;
    private SalesOrderRepository mSalesOrderRepository;
    private ProductRepository mProductRepository;
    private SalesDetailRepository mSalesDetailRepository;
    private LiveData<List<String>> mCustomerNamesLd;
    private LiveData<SalesOrderDto> mSalesOrderDetailsLd;
    private LiveData<List<SalesDetailDto>> mSalesDetailsLd;
    private SalesOrderDto mSalesOrder;
    private List<SalesDetailDto> mSalesDetails;
    private Application mApplication;

    public SalesOrderEditorViewModel(@NonNull Application application, long salesOrderId) {
        super(application);
        mApplication = application;
        mCustomerRepository = new CustomerRepository(application);
        mSalesOrderRepository = new SalesOrderRepository(application);
        mProductRepository = new ProductRepository(application);
        mSalesDetailRepository = new SalesDetailRepository(application);
        mCustomerNamesLd = mCustomerRepository.getAllCustomerName();

        if (salesOrderId != 0L) {
            mSalesOrderDetailsLd = mSalesOrderRepository.getSalesOrderDetailsById(salesOrderId);
            mSalesDetailsLd = mSalesDetailRepository.getSalesDetailsBySalesOrderId(salesOrderId);
        }
    }

    public void insert(String customerName) {
        mSalesOrderRepository.insertSalesOrderTransaction(customerName, mSalesOrder, mSalesDetails, mCustomerRepository, mSalesDetailRepository);
    }

    public void update() {
        mSalesOrderRepository.update(mSalesOrder);
    }

    public void delete() {
        mSalesOrderRepository.deleteSalesOrderTransaction(mSalesOrder, mSalesDetailRepository);
    }

    public LiveData<List<String>> getCustomerNamesLd() {
        return mCustomerNamesLd;
    }

    public LiveData<SalesOrderDto> getSalesOrderDetailsLd() {
        return mSalesOrderDetailsLd;
    }

    public LiveData<List<SalesDetailDto>> getSalesDetailsLd() {
        return mSalesDetailsLd;
    }

    public void processSalesItemDetail(ArrayList<AddSalesItemDto> addSalesItemDtos) {
        long totalPrice = 0;
        long deliveryCharge = 0;
        StringBuilder summarySB = new StringBuilder();
        int listSize = addSalesItemDtos.size();
        mSalesDetails = new ArrayList<>();

        for (AddSalesItemDto detail : addSalesItemDtos) {
            String description = getDescription(detail);
            mSalesDetails.add(new SalesDetailDto(
                    detail.getProductId(),
                    detail.getPrice(),
                    detail.getQuantity(),
                    0L,
                    description,
                    description != null ? description : detail.getProductName()
            ));

            totalPrice += detail.getPrice();
            deliveryCharge += Constants.DELIVERY_CHARGE * detail.getQuantity();
            summarySB.append(detail.getProductName());
            summarySB.append(" x ");
            summarySB.append(detail.getQuantity());

            if (--listSize > 0) {
                summarySB.append("   ");
            }
        }

        mSalesOrder = new SalesOrderDto(new Date(), totalPrice, deliveryCharge, summarySB.toString());

    }

    public SalesOrderDto getSalesOrder() {
        if (mSalesOrder == null) {
            mSalesOrder = new SalesOrderDto();
        }
        return mSalesOrder;
    }

    public void setSalesOrder(SalesOrderDto salesOrder) {
        this.mSalesOrder = salesOrder;
    }

    public List<SalesDetailDto> getSalesDetails() {
        return mSalesDetails;
    }

    public void setSalesDetails(List<SalesDetailDto> salesDetails) {
        this.mSalesDetails = salesDetails;
    }

    private String getDescription(AddSalesItemDto dto) {
        if (dto.getProductId() != Constants.CUSTOM) {
            return null;
        }

        return dto.getProductName();
    }

    public void hasDeliveryCharge(boolean hasDeliveryCharge) {
        long deliveryCharge = mSalesOrder.getDeliveryCharge();
        if (!hasDeliveryCharge) {
            deliveryCharge = -deliveryCharge;
        }

        calculateTotalPrice(deliveryCharge);
    }

    public long calculateTotalPrice(long valueToAdd) {
        long totalPrice = mSalesOrder.getTotalPrice() + valueToAdd;
        mSalesOrder.setTotalPrice(totalPrice);
        return totalPrice;
    }

    public void processOrderType(int deliveryType) {
        boolean status = deliveryType != 0;
        mSalesOrder.setOrderType(deliveryType);
        hasDeliveryCharge(status);
    }

    public long setDiscount(long discount) {
        mSalesOrder.setDiscount(discount > 0 ? discount : 0);
        return calculateTotalPrice(discount);
    }

}
