package com.example.acquatikaapp.data.dao;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.acquatikaapp.data.dto.TotalQuantityPerProductDto;
import com.example.acquatikaapp.data.model.Product;

import java.util.Date;
import java.util.List;

@Dao
public interface ProductDao {

    @Insert
    long insert(Product product);

    @Insert
    List<Long> massInsert(List<Product> products);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Product product);

    @Delete
    void delete(Product product);

    @Query("SELECT * FROM product ORDER BY id ASC")
    LiveData<List<Product>> getAllProducts();

    @Query("SELECT * FROM product WHERE id =:id")
    LiveData<Product> getProductById(int id);

    @Query("SELECT name FROM product WHERE id =:id")
    String getProductNameById(int id);

    @Query("SELECT sum(id) FROM product WHERE is_on_dashboard = 0")
    int getDashboardProductCount();

    //total quantity per product //for dashboard and pie chart
    @Query("SELECT product.name AS name, SUM(quantity) AS total, is_on_dashboard AS isOnDashboard FROM product " +
            "LEFT OUTER JOIN sales_detail ON product.id = sales_detail.product_id " +
            "LEFT OUTER JOIN sales_order ON sales_detail.sales_order_id = sales_order.id " +
            "WHERE (status IS NULL OR status = 0) " +
            "AND (date IS NULL OR (date BETWEEN :fromDate AND :toDate)) " +
            "AND (:orderType IS NULL OR order_type = :orderType) " +
            "GROUP BY product.id ORDER BY product.id ASC")
    LiveData<List<TotalQuantityPerProductDto>> getProductCount(Date fromDate, Date toDate,
                                                               @Nullable Integer orderType);

}
