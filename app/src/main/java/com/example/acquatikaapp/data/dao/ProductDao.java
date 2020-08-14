package com.example.acquatikaapp.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.acquatikaapp.data.model.Product;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert
    void insertProduct(Product product);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateProduct(Product product);

    @Delete
    void deleteProduct(Product product);

    @Query("SELECT id, name FROM product ORDER BY name ASC")
    LiveData<List<Product>> getAllProductNameWithId();

    @Query("SELECT * FROM product ORDER BY name DESC")
    LiveData<List<Product>> getAllProducts();

    @Query("SELECT * FROM product WHERE id =:id")
    LiveData<Product> getProductById(int id);

    @Query("SELECT name FROM product WHERE id =:id")
    LiveData<Product> getProductNameById(int id);

}
