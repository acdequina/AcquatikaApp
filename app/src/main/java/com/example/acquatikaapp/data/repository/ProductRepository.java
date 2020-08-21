package com.example.acquatikaapp.data.repository;

import android.app.Application;
import android.content.Context;

import com.example.acquatikaapp.data.dao.ProductDao;
import com.example.acquatikaapp.data.database.AcquatikaDatabase;
import com.example.acquatikaapp.data.model.Product;
import com.example.acquatikaapp.data.util.AppExecutors;

public class ProductRepository {

    private ProductDao productDao;
    private AppExecutors appExecutors;

    public ProductRepository(Application application) {
        productDao = AcquatikaDatabase.getInstance(application).productDao();
        appExecutors = AppExecutors.getInstance();
    }

    public void insert(final Product product) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                productDao.insert(product);
            }
        });
    }

    public void update(final Product product) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                productDao.update(product);
            }
        });
    }

    public void delete(final Product product) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                productDao.delete(product);
            }
        });
    }


}
