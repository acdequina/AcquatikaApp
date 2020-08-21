package com.example.acquatikaapp.data.repository;

import android.app.Application;
import android.content.Context;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.acquatikaapp.data.dao.SalesOrderDao;
import com.example.acquatikaapp.data.database.AcquatikaDatabase;
import com.example.acquatikaapp.data.dto.TransactionDto;
import com.example.acquatikaapp.data.model.SalesOrder;
import com.example.acquatikaapp.data.util.AppExecutors;

import java.util.Date;
import java.util.List;

public class SalesOrderRepository {

    private SalesOrderDao salesOrderDao;
    private AppExecutors appExecutors;
    private long salesOrderId;

    public SalesOrderRepository(Application application) {
        salesOrderDao = AcquatikaDatabase.getInstance(application).salesOrderDao();
        appExecutors = AppExecutors.getInstance();
    }

    public long insert(final SalesOrder salesOrder) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final long id = salesOrderDao.insert(salesOrder);
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        salesOrderId = id;
                    }
                });
            }
        });

        return salesOrderId;
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

    public LiveData<List<TransactionDto>> getTransactions(Date fromDate, Date toDate,
                                                          Integer status,
                                                          String customerName,
                                                          Integer orderType) {
        return salesOrderDao.getTransactions(fromDate, toDate, status, customerName, orderType);
    }


}
