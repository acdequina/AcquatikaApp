package com.example.acquatikaapp.data.dao;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.acquatikaapp.data.dto.SalesLineGraphDto;
import com.example.acquatikaapp.data.dto.TotalQuantityPerProductDto;
import com.example.acquatikaapp.data.dto.TransactionItemDetailDto;
import com.example.acquatikaapp.data.dto.TransactionDto;
import com.example.acquatikaapp.data.model.SalesOrder;

import java.util.Date;
import java.util.List;

@Dao
public interface SalesOrderDao {

    @Insert
    long insert(SalesOrder salesOrder);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(SalesOrder salesOrder);

    @Delete
    void delete(SalesOrder salesOrder);

    @Transaction
    @Query("SELECT * FROM sales_order WHERE id = :id")
    LiveData<TransactionItemDetailDto> getSalesOrderDetailsById(long id);

    //total sales on dashboard
    @Query("SELECT SUM(total_price) FROM sales_order WHERE status = 0 AND date >= :dateNow")
    LiveData<Long> getCurrentSalesOrder(Date dateNow);

    //dashboard line graph
    @Query("SELECT total_price AS totalPrice, date FROM sales_order WHERE status = 0 AND date >= :date")
    LiveData<SalesLineGraphDto> getSalesAndDate(Date date);

    //current transaction list
//    @Query("SELECT customer.name as name, " +
//            "sales_order.total_price as totalPrice, product.name as itemName, " +
//            "sales_detail.quantity as quantity, sales_order.date as date, " +
//            "sales_order.status as status, sales_order.order_type as orderType " +
//            "FROM sales_order " +
//            "INNER JOIN sales_detail ON sales_detail.sales_order_id = sales_order.id " +
//            "LEFT JOIN product ON product.id = sales_detail.product_id " +
//            "LEFT JOIN customer ON customer.id = sales_order.customer_id " +
//            "WHERE date >= :date ORDER BY status DESC, date DESC")
//    LiveData<List<CurrentTransactionDto>> getCurrentTransactions(Date date);

    //transaction logs
    @Query("SELECT customer.name as name, " +
            "sales_order.total_price as totalPrice, " +
            "sales_order.date as date, " +
            "sales_order.order_type as orderType, " +
            "sales_order.description as description, " +
            "sales_order.status as status " +
            "FROM sales_order " +
            "LEFT JOIN customer ON customer.id = sales_order.customer_id " +
            "WHERE (date BETWEEN :fromDate AND :toDate) " +
            "AND (:status IS NULL OR sales_order.status = :status)" +
            "AND (:customerName IS NULL OR customer.name = :customerName) " +
            "AND (:orderType IS NULL OR sales_order.order_type = :orderType) ")
    LiveData<List<TransactionDto>> getTransactions(Date fromDate, Date toDate,
                                                   @Nullable Integer status,
                                                   @Nullable String customerName,
                                                   @Nullable Integer orderType);

    @Query("SELECT customer.name as name, " +
            "sales_order.total_price as totalPrice, " +
            "sales_order.date as date, " +
            "sales_order.order_type as orderType, " +
            "sales_order.description as description, " +
            "sales_order.status as status " +
            "FROM sales_order " +
            "LEFT JOIN customer ON customer.id = sales_order.customer_id "+
            "WHERE (date BETWEEN :fromDate AND :toDate) ")
    LiveData<List<TransactionDto>> getAllTransactions(Date fromDate, Date toDate);

    //total quantity per product //for dashboard and pie chart
//    @Query("SELECT product.name AS name, SUM(quantity) AS total FROM sales_order " +
//            "JOIN sales_detail ON sales_detail.sales_order_id = sales_order.id " +
//            "JOIN product ON product.id = sales_detail.product_id " +
//            "WHERE status = 0 " +
//            "AND (date BETWEEN :fromDate AND :toDate) " +
//            "AND (:orderType IS NULL OR sales_order.order_type = :orderType) " +
//            "GROUP BY product_id ORDER BY name DESC")
//    LiveData<List<TotalQuantityPerProductDto>> getProductCount(Date fromDate, Date toDate,
//                                                               @Nullable int orderType);

}
