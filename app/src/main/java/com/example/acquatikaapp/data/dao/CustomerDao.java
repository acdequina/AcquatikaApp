package com.example.acquatikaapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.acquatikaapp.data.dto.CustomerTotalPurchaseDto;
import com.example.acquatikaapp.data.model.Customer;

import java.util.Date;
import java.util.List;

@Dao
public interface CustomerDao {

    @Insert
    void insertCustomer(Customer customer);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCustomer(Customer customer);

    @Delete
    void deleteCustomer(Customer customer);

    @Query("SELECT id, name FROM customer ORDER BY name ASC")
    LiveData<List<Customer>> getAllCustomerNameWithId();

    @Query("SELECT * FROM customer WHERE id =:id")
    LiveData<Customer> getCustomerById(int id);

    @Query("SELECT name FROM customer WHERE id =:id")
    LiveData<Customer> getCustomerNameById(int id);

    @Query("SELECT name, SUM(total_price) FROM customer " +
            "INNER JOIN sales_order ON sales_order.customer_id = customer.id " +
            "GROUP BY name ORDER BY total_price DESC LIMIT :limit")
    LiveData<List<CustomerTotalPurchaseDto>> getTopCustomers(Date fromDate, Date toDate, int limit);

}
