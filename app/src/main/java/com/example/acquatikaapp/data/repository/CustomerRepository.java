package com.example.acquatikaapp.data.repository;

import android.app.Application;
import android.content.Context;

import com.example.acquatikaapp.data.dao.CustomerDao;
import com.example.acquatikaapp.data.database.AcquatikaDatabase;
import com.example.acquatikaapp.data.model.Customer;
import com.example.acquatikaapp.data.util.AppExecutors;

public class CustomerRepository {

    private CustomerDao customerDao;
    private int customerId;
    private AppExecutors appExecutors;

    public CustomerRepository(Application application) {
        customerDao = AcquatikaDatabase.getInstance(application).customerDao();
        appExecutors = AppExecutors.getInstance();
    }

    public int insert(final Customer customer) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
               final long id = customerDao.insert(customer);
                appExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        customerId = (int) id;
                    }
                });
            }
        });

        return customerId;
    }

    public void update(final Customer customer) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                customerDao.update(customer);
            }
        });
    }

    public void delete(final Customer customer) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                customerDao.delete(customer);
            }
        });
    }

}
