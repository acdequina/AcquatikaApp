package com.example.acquatikaapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.acquatikaapp.data.dto.CustomerNameIdDto;
import com.example.acquatikaapp.data.dto.CustomerTotalPurchaseDto;
import com.example.acquatikaapp.data.model.Customer;

import java.util.Date;
import java.util.List;

@Dao
public interface CustomerDao {

    @Insert
    long insert(Customer customer);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Customer customer);

    @Delete
    void delete(Customer customer);

//    @Query("SELECT id, name FROM customer ORDER BY name ASC")
//    LiveData<List<CustomerNameIdDto>> getAllCustomerNameWithId();
//
//    @Query("SELECT * FROM customer WHERE id =:id")
//    LiveData<Customer> getCustomerById(int id);
//
//    @Query("SELECT name FROM customer WHERE id =:id")
//    LiveData<String> getCustomerNameById(int id);
//
//    @Query("SELECT name, SUM(total_price) as totalPurchase FROM customer " +
//            "INNER JOIN sales_order ON sales_order.customer_id = customer.id " +
//            "WHERE (date BETWEEN :fromDate AND :toDate) " +
//            "GROUP BY name ORDER BY total_price DESC LIMIT :limit")
//    LiveData<List<CustomerTotalPurchaseDto>> getTopCustomers(Date fromDate, Date toDate, int limit);

}
