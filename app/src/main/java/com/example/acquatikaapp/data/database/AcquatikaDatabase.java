package com.example.acquatikaapp.data.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.acquatikaapp.data.dao.CustomerDao;
import com.example.acquatikaapp.data.dao.ProductDao;
import com.example.acquatikaapp.data.model.Customer;
import com.example.acquatikaapp.data.model.Product;
import com.example.acquatikaapp.data.model.SalesDetail;
import com.example.acquatikaapp.data.model.SalesOrder;
import com.example.acquatikaapp.data.util.DateConverter;

@Database(entities = {Customer.class, Product.class,
        SalesOrder.class, SalesDetail.class},
        version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AcquatikaDatabase extends RoomDatabase {

    private static final String LOG_TAG = AcquatikaDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "acquatika";

    // for singleton instantiation
    private static final Object LOCK = new Object();
    private static AcquatikaDatabase sInstance;

    public static AcquatikaDatabase getInstance(Context context) {
        if(sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AcquatikaDatabase.class, AcquatikaDatabase.DATABASE_NAME).build();
                Log.i(LOG_TAG, "Acquatika database created.");
            }
        }

        return sInstance;
    }

    public abstract CustomerDao customerDao();
    public abstract ProductDao productDao();
    public abstract SalesOrder salesOrder();
    public abstract SalesDetail salesDetail();

}
