package com.example.acquatikaapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.acquatikaapp.data.dto.SalesDetailDto;
import com.example.acquatikaapp.data.model.SalesDetail;

import java.util.List;

@Dao
public interface SalesDetailDao {

    @Insert
    void insert(SalesDetail salesDetail);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(SalesDetail salesDetail);

    @Delete
    void delete(SalesDetail salesDetail);

    @Query("DELETE FROM sales_detail " +
            "WHERE sales_detail.sales_order_id = :salesOrderId")
    void massDelete(long salesOrderId);

    @Query("SELECT sales_detail.id, product_id, quantity, sales_order_id, " +
            "sales_detail.price, description, product.name as productName FROM sales_detail " +
            "LEFT JOIN product ON product.id = sales_detail.product_id " +
            "WHERE sales_detail.sales_order_id = :salesOrderId")
    LiveData<List<SalesDetailDto>> getSalesDetailsBySalesOrderId(long salesOrderId);


}
