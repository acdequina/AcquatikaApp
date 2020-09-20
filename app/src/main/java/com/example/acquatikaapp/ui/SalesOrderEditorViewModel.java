package com.example.acquatikaapp.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.acquatikaapp.data.dto.SalesDetailDto;
import com.example.acquatikaapp.data.dto.AddSalesItemDto;
import com.example.acquatikaapp.data.dto.SalesOrderDto;
import com.example.acquatikaapp.data.model.SalesDetail;
import com.example.acquatikaapp.data.model.SalesOrder;
import com.example.acquatikaapp.data.repository.CustomerRepository;
import com.example.acquatikaapp.data.repository.ProductRepository;
import com.example.acquatikaapp.data.repository.SalesDetailRepository;
import com.example.acquatikaapp.data.repository.SalesOrderRepository;

import java.util.ArrayList;
import java.util.List;

public class SalesOrderEditorViewModel extends AndroidViewModel {

    private CustomerRepository customerRepository;
    private SalesOrderRepository salesOrderRepository;
    private ProductRepository productRepository;
    private SalesDetailRepository salesDetailRepository;
    private LiveData<List<String>> customerNames;
    private LiveData<SalesOrderDto> salesOrderDetails;
    private LiveData<List<SalesDetailDto>> salesDetails;

    public SalesOrderEditorViewModel(@NonNull Application application, long salesOrderId) {
        super(application);

        customerRepository = new CustomerRepository(application);
        salesOrderRepository = new SalesOrderRepository(application);
        productRepository = new ProductRepository(application);
        salesDetailRepository = new SalesDetailRepository(application);
        customerNames = customerRepository.getAllCustomerName();

        if(salesOrderId != 0L) {
            salesOrderDetails = salesOrderRepository.getSalesOrderDetailsById(salesOrderId);
            salesDetails = salesDetailRepository.getSalesDetailsBySalesOrderId(salesOrderId);
        }
    }

    public void insert(String customerName, SalesOrderDto salesOrder, List<? extends SalesDetail> salesDetails) {
        salesOrderRepository.insertSalesOrderTransaction(customerName, salesOrder, (List<SalesDetail>) salesDetails, customerRepository, salesDetailRepository);
    }

    public void update(SalesOrder salesOrder) {
        salesOrderRepository.update(salesOrder);
    }

    public void delete(SalesOrder salesOrder) {
        salesOrderRepository.deleteSalesOrderTransaction(salesOrder, salesDetailRepository);
    }

    public LiveData<List<String>> getCustomerNames() {
        return customerNames;
    }

    public LiveData<SalesOrderDto> getSalesOrderDetails() {
        return salesOrderDetails;
    }

    public LiveData<List<SalesDetailDto>> getSalesDetails() {
        return salesDetails;
    }

    public ArrayList<AddSalesItemDto> getSalesDetailViewData(List<SalesDetailDto> salesDetails) {
        ArrayList<AddSalesItemDto> addSalesItemDtos = new ArrayList<>();
        for(SalesDetailDto salesDetail : salesDetails) {
            AddSalesItemDto dto = new AddSalesItemDto();
            dto.setPrice(salesDetail.getPrice());
            dto.setQuantity(salesDetail.getQuantity());
            dto.setProductId(salesDetail.getProductId());
            String productName = salesDetail.getRemarks() != null ? salesDetail.getRemarks() : salesDetail.getProductName();
            dto.setProductName(productName);
            addSalesItemDtos.add(dto);
        }

        return addSalesItemDtos;
    }

}
