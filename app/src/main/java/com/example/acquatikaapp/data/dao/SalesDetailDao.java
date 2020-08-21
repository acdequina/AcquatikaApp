package com.example.acquatikaapp.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;

import com.example.acquatikaapp.data.model.SalesDetail;

import java.util.List;

@Dao
public interface SalesDetailDao {

    @Insert
    void insert(SalesDetail salesDetail);

    @Insert
    void massInsert(List<SalesDetail> salesDetails);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(SalesDetail salesDetail);

    @Delete
    void delete(SalesDetail salesDetail);

}
