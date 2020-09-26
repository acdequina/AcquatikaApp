package com.example.acquatikaapp.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.acquatikaapp.data.dao.SalesOrderDao;
import com.example.acquatikaapp.data.database.AcquatikaDatabase;
import com.example.acquatikaapp.data.dto.ExportDataDto;
import com.example.acquatikaapp.data.dto.SalesLineGraphDto;
import com.example.acquatikaapp.data.dto.SalesOrderDto;
import com.example.acquatikaapp.data.dto.SalesOrderItemDto;
import com.example.acquatikaapp.data.model.SalesDetail;
import com.example.acquatikaapp.data.model.SalesOrder;
import com.example.acquatikaapp.data.util.AppExecutors;
import com.example.acquatikaapp.ui.SalesOrderEditorActivity;

import java.sql.SQLDataException;
import java.util.Date;
import java.util.List;

public class SalesOrderRepository {

    private static final String TAG = SalesOrderRepository.class.getSimpleName();

    private SalesOrderDao salesOrderDao;
    private AppExecutors appExecutors;

    public SalesOrderRepository(Application application) {
        salesOrderDao = AcquatikaDatabase.getInstance(application).salesOrderDao();
        appExecutors = AppExecutors.getInstance();
    }

    public LiveData<List<SalesOrderItemDto>> getSalesOrders(Date fromDate, Date toDate,
                                                            Integer status,
                                                            String customerName,
                                                            Integer orderType) {
        return salesOrderDao.getSalesOrders(fromDate, toDate, status, customerName, orderType);
    }

    public void update(final SalesOrder salesOrder) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                salesOrderDao.update(salesOrder);
            }
        });
    }

    public void delete(final SalesOrder salesOrder) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                salesOrderDao.delete(salesOrder);
            }
        });
    }

    public LiveData<Long> getCurrentTotalSales(Date dateNow) {
        return salesOrderDao.getCurrentTotalSales(dateNow);
    }

    public LiveData<List<SalesLineGraphDto>> getSalesAndDate(Date date) {
        return salesOrderDao.getSalesAndDate(date);
    }

    public void deleteSalesOrderTransaction(final SalesOrder salesOrder, final SalesDetailRepository salesDetailRepository) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    salesDetailRepository.massDelete(salesOrder.getId());
                    salesOrderDao.delete(salesOrder);
                } catch (Exception e) {
                    //Temporary ignore error
                    Log.i(TAG, e.getMessage());
                }
            }
        });

    }

    public void insertSalesOrderTransaction(final String customerName, final SalesOrder salesOrder, final List<SalesDetail> salesDetails,
                                            final CustomerRepository customerRepository, final SalesDetailRepository salesDetailRepository) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                int customerId = customerRepository.getOrInsertCustomerByName(customerName);
                salesOrder.setCustomerId(customerId);

                salesOrder.setDate(new Date());
                //TODO create receipt number generator
                long salesOrderId = salesOrderDao.insert(salesOrder);
                salesDetailRepository.insertSalesDetails(salesOrderId, salesDetails);
            }
        });
    }

    public LiveData<SalesOrderDto> getSalesOrderDetailsById(long id) {
       return salesOrderDao.getSalesOrderDetailsById(id);
    }

    public List<ExportDataDto> getExportData() {
        return salesOrderDao.getExportData();
    }

}
