package com.example.acquatikaapp.data.dao;

import android.database.Cursor;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.acquatikaapp.data.dto.ExportDataDto;
import com.example.acquatikaapp.data.dto.SalesLineGraphDto;
import com.example.acquatikaapp.data.dto.SalesOrderDto;
import com.example.acquatikaapp.data.dto.SalesOrderItemDto;
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

    @Query("SELECT sales_order.id, date, receipt_number," +
            "status, order_type, delivery_charge, total_price, " +
            "discount, customer_id, summary, customer.name as customerName, " +
            "remarks " +
            "FROM sales_order " +
            "LEFT JOIN customer ON customer.id = sales_order.customer_id " +
            "WHERE sales_order.id = :id")
    LiveData<SalesOrderDto> getSalesOrderDetailsById(long id);

    //total sales on dashboard
    @Query("SELECT SUM(total_price) FROM sales_order WHERE status = 0 AND date >= :dateNow")
    LiveData<Long> getCurrentTotalSales(Date dateNow);

    //dashboard line graph
    @Query("SELECT total_price AS totalPrice, date FROM sales_order WHERE status = 0 AND date >= :date")
    LiveData<List<SalesLineGraphDto>> getSalesAndDate(Date date);

    @Transaction
    @Query("SELECT sales_order.id as id, customer.name as name, " +
            "sales_order.total_price as totalPrice, " +
            "sales_order.date as date, " +
            "sales_order.order_type as orderType, " +
            "sales_order.summary as summary, " +
            "sales_order.status as status " +
            "FROM sales_order " +
            "LEFT JOIN customer ON customer.id = sales_order.customer_id " +
            "WHERE (date BETWEEN :fromDate AND :toDate) " +
            "AND (:status IS NULL OR sales_order.status = :status)" +
            "AND (:customerName IS NULL OR customer.name = :customerName) " +
            "AND (:orderType IS NULL OR sales_order.order_type = :orderType) " +
            "ORDER BY status DESC, date DESC")
    LiveData<List<SalesOrderItemDto>> getSalesOrders(Date fromDate, Date toDate,
                                                     @Nullable Integer status,
                                                     @Nullable String customerName,
                                                     @Nullable Integer orderType);


    @Transaction
    @Query("SELECT customer.name as customerName, sales_order.id as salesOrderId, " +
            "date, status, order_type as orderType, delivery_charge as deliveryCharge, total_price as totalPrice, " +
            "discount, ifnull(sales_detail.description, product.name) as productName, " +
            "sales_detail.price as price, quantity, sales_order.remarks as remarks " +
            "FROM sales_order " +
            "JOIN customer ON sales_order.customer_id = customer.id " +
            "JOIN sales_detail ON sales_order.id = sales_detail.sales_order_id " +
            "JOIN product ON sales_detail.product_id = product.id")
    List<ExportDataDto> getExportData();

}
