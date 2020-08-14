package com.example.acquatikaapp.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import com.example.acquatikaapp.data.model.SalesDetail;

@Dao
public interface SalesDetailDao {
    @Insert
    void insertSalesDetail(SalesDetail salesDetail);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateSalesDetail(SalesDetail salesDetail);

    @Delete
    void deleteSalesDetail(SalesDetail salesDetail);

}
