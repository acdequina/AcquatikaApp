package com.example.acquatikaapp.data.repository;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.example.acquatikaapp.data.dao.ProductDao;
import com.example.acquatikaapp.data.database.AcquatikaDatabase;
import com.example.acquatikaapp.data.dto.ProductNameIdDto;
import com.example.acquatikaapp.data.dto.TotalQuantityPerProductDto;
import com.example.acquatikaapp.data.model.Product;
import com.example.acquatikaapp.data.util.AppExecutors;

import java.util.Date;
import java.util.List;

public class ProductRepository {

    private ProductDao productDao;
    private AppExecutors appExecutors;
    private int PRODUCT_DASHBOARD_LIMIT = 2;

    private static final String TAG = ProductRepository.class.getSimpleName();

    public ProductRepository(Application application) {
        productDao = AcquatikaDatabase.getInstance(application).productDao();
        appExecutors = AppExecutors.getInstance();
    }

    public void insert(final Product product) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(product.getIsOnDashboard()
                        && !(productDao.getDashboardProductCount() == PRODUCT_DASHBOARD_LIMIT)) {
                    Log.i(TAG, "Cannot assign more than " + PRODUCT_DASHBOARD_LIMIT +" products on dashboard");
                    product.setIsOnDashboard(false);
                }
                productDao.insert(product);
            }
        });
    }

    public void update(final Product product) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if(product.getIsOnDashboard()
                        && !(productDao.getDashboardProductCount() == PRODUCT_DASHBOARD_LIMIT)) {
                    Log.i(TAG, "Cannot assign more than " + PRODUCT_DASHBOARD_LIMIT +" products on dashboard");
                    product.setIsOnDashboard(false);
                }
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

    public LiveData<List<ProductNameIdDto>> getAllProductNameWithId() {
        return productDao.getAllProductNameWithId();
    }


    public LiveData<List<TotalQuantityPerProductDto>> getProductCount(Date fromDate, Date toDate,
                                                                      @Nullable Integer orderType) {
        return productDao.getProductCount(fromDate, toDate, orderType);
    }

}
