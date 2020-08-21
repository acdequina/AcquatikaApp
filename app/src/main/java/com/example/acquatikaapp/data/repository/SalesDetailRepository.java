package com.example.acquatikaapp.data.repository;

import android.app.Application;
import android.content.Context;

import com.example.acquatikaapp.data.dao.SalesDetailDao;
import com.example.acquatikaapp.data.database.AcquatikaDatabase;
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

    public void insert(final List<SalesDetail> salesDetails) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                salesDetailDao.massInsert(salesDetails);
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
}
