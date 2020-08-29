package com.example.acquatikaapp.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.acquatikaapp.data.dao.CustomerDao;
import com.example.acquatikaapp.data.database.AcquatikaDatabase;
import com.example.acquatikaapp.data.dto.CustomerNameIdDto;
import com.example.acquatikaapp.data.model.Customer;
import com.example.acquatikaapp.data.util.AppExecutors;

import java.util.List;

public class CustomerRepository {

    private CustomerDao customerDao;
    private AppExecutors appExecutors;

    public CustomerRepository(Application application) {
        customerDao = AcquatikaDatabase.getInstance(application).customerDao();
        appExecutors = AppExecutors.getInstance();
    }

    public void insert(final Customer customer) {
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
               customerDao.insert(customer);
            }
        });
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

    public LiveData<List<CustomerNameIdDto>> getAllNameWithId() {
        return customerDao.getAllNameWithId();
    }

}
