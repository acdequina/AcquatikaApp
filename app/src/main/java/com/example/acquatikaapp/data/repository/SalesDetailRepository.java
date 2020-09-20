package com.example.acquatikaapp.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.acquatikaapp.data.dao.SalesDetailDao;
import com.example.acquatikaapp.data.database.AcquatikaDatabase;
import com.example.acquatikaapp.data.dto.SalesDetailDto;
import com.example.acquatikaapp.data.model.Customer;
import com.example.acquatikaapp.data.model.SalesDetail;
import com.example.acquatikaapp.data.util.AppExecutors;

import java.util.List;

public class SalesDetailRepository {

    private SalesDetailDao salesDetailDao;
    private AppExecutors appExecutors;

    public SalesDetailRepository(Application application) {
        salesDetailDao = AcquatikaDatabase.getInstance(application).salesDetailDao();
        appExecutors = AppExecutors.getInstance();
    }

    public void insert(final SalesDetail salesDetail) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                salesDetailDao.insert(salesDetail);
            }
        });
    }

    public void update(final SalesDetail salesDetail) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                salesDetailDao.update(salesDetail);
            }
        });
    }

    public void delete(final SalesDetail salesDetail) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                salesDetailDao.delete(salesDetail);
            }
        });
    }

    public void insertSalesDetails(long salesOrderId, List<SalesDetail> salesDetails) {
        for(SalesDetail salesDetail : salesDetails) {
            salesDetail.setSalesOrderId(salesOrderId);
            salesDetailDao.insert(salesDetail);
        }
    }

    public LiveData<List<SalesDetailDto>> getSalesDetailsBySalesOrderId(long salesOrderId) {
        return salesDetailDao.getSalesDetailsBySalesOrderId(salesOrderId);
    }

    public void massDelete(long salesOrderId) {
        salesDetailDao.massDelete(salesOrderId);
    }
}
