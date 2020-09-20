package com.example.acquatikaapp.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.acquatikaapp.data.dao.CustomerDao;
import com.example.acquatikaapp.data.database.AcquatikaDatabase;
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

    public LiveData<List<String>> getAllCustomerName() {
        return customerDao.getAllCustomerName();
    }

    public int getOrInsertCustomerByName(String name) {
        int customerId = (int) customerDao.getCustomerIdByName(name);
        if(customerId > 0) {
            return customerId;
        }

        return (int) customerDao.insert(new Customer(name, null, null));
    }

    public LiveData<String> getCustomerNameById(int id) {
        return customerDao.getCustomerNameById(id);
    }

}
