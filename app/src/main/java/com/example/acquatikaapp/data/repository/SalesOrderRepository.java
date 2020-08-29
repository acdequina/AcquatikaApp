package com.example.acquatikaapp.data.repository;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.acquatikaapp.data.database.AcquatikaDatabase;
import com.example.acquatikaapp.data.dto.CustomerNameIdDto;
import com.example.acquatikaapp.data.dto.SalesLineGraphDto;
import com.example.acquatikaapp.data.dto.SalesOrderDto;
import com.example.acquatikaapp.data.dto.TotalQuantityPerProductDto;
import com.example.acquatikaapp.data.model.Customer;
import com.example.acquatikaapp.data.model.SalesDetail;
import com.example.acquatikaapp.data.model.SalesOrder;
import com.example.acquatikaapp.data.util.AppExecutors;

import java.util.Date;
import java.util.List;

public class SalesOrderRepository {

    private AcquatikaDatabase database;
    private AppExecutors appExecutors;

    public SalesOrderRepository(Application application) {
        database = AcquatikaDatabase.getInstance(application);
        appExecutors = AppExecutors.getInstance();
    }

    public LiveData<List<SalesOrderDto>> getSalesOrders(Date fromDate, Date toDate,
                                                        Integer status,
                                                        String customerName,
                                                        Integer orderType) {
        return database.salesOrderDao().getSalesOrderDetails(fromDate, toDate, status, customerName, orderType);
    }

    public LiveData<Long> getCurrentTotalSales(Date dateNow) {
        return database.salesOrderDao().getCurrentTotalSales(dateNow);
    }

    public LiveData<SalesLineGraphDto> getSalesAndDate(Date date) {
        return database.salesOrderDao().getSalesAndDate(date);
    }

    public void insert(final CustomerNameIdDto customerDetails, final SalesOrder salesOrder, final List<SalesDetail> salesDetails) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                int customerId = customerDetails.getId();
                if(customerId <= 0) {
                    customerId = (int) database.customerDao().insert(new Customer(customerDetails.getName()));
                }

                salesOrder.setDate(new Date());
                //TODO create receipt number generator
                salesOrder.setReceiptNumber(null);
                salesOrder.setCustomerId(customerId);
                long salesOrderId = database.salesOrderDao().insert(salesOrder);
                insertSalesDetails(salesOrderId, salesDetails);
            }
        });
    }

//    private String generateDescription(List<SalesDetail> salesDetails) {
//        ProductDao productDao = database.productDao();
//        int listSize = salesDetails.size();
//        StringBuilder descriptionSB = new StringBuilder();
//        for (SalesDetail detail : salesDetails) {
//            String productName = productDao.getProductNameById(detail.getProductId());
//            descriptionSB.append(productName);
//            descriptionSB.append(" x");
//            descriptionSB.append(detail.getQuantity());
//
//            if(--listSize > 0) {
//                descriptionSB.append("   ");
//            }
//        }
//
//        return descriptionSB.toString();
//    }

    private void insertSalesDetails(long salesOrderId, List<SalesDetail> salesDetails) {
        for(SalesDetail salesDetail : salesDetails) {
            salesDetail.setSalesOrderId(salesOrderId);
            database.salesDetailDao().insert(salesDetail);
        }
    }


}
