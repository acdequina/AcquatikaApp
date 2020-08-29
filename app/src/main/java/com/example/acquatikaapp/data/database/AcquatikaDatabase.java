package com.example.acquatikaapp.data.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.acquatikaapp.data.dao.CustomerDao;
import com.example.acquatikaapp.data.dao.ProductDao;
import com.example.acquatikaapp.data.dao.SalesDetailDao;
import com.example.acquatikaapp.data.dao.SalesOrderDao;
import com.example.acquatikaapp.data.model.Customer;
import com.example.acquatikaapp.data.model.Product;
import com.example.acquatikaapp.data.model.SalesDetail;
import com.example.acquatikaapp.data.model.SalesOrder;
import com.example.acquatikaapp.data.util.AppExecutors;
import com.example.acquatikaapp.data.util.DateConverter;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

    public static AcquatikaDatabase getInstance(final Context context) {
        if(sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AcquatikaDatabase.class, AcquatikaDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .addCallback(prePopulateDbRoomCallback)
                        .build();
                Log.i(LOG_TAG, "Acquatika database created.");
            }
        }

        return sInstance;
    }

    private static RoomDatabase.Callback prePopulateDbRoomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    new PrePopulateDbDefaultData(sInstance);
                }
            });
        }
    };

    public static class PrePopulateDbDefaultData {
        private PrePopulateDbDefaultData(AcquatikaDatabase database) {
            long customerId = database.customerDao()
                    .insert(getCustomer());

            long soId = database.salesOrderDao()
                    .insert(getSalesOrder((int) customerId, "Slim", 1));

            List<Long> productIds = database.productDao().massInsert(getProducts());

            database.salesDetailDao().insert(getSalesDetail(soId, productIds.get(0).intValue()));

            long soId2 = database.salesOrderDao()
                    .insert(getSalesOrder((int) customerId, "Round", 2));

            database.salesDetailDao().insert(getSalesDetail(soId, productIds.get(1).intValue()));
            database.salesDetailDao().insert(getSalesDetail(soId2, productIds.get(1).intValue()));

            long soId3 = database.salesOrderDao()
                    .insert(getSalesOrder((int) customerId, "Others", 3));

            database.salesDetailDao().insert(getSalesDetail(soId3, productIds.get(2).intValue()));
            database.salesDetailDao().insert(getSalesDetail(soId3, productIds.get(2).intValue()));
            database.salesDetailDao().insert(getSalesDetail(soId3, productIds.get(2).intValue()));

        }

        public static Customer getCustomer() {
            return new Customer("Juan Dela Cruz", "PH", "+639123");
        }

        public static List<Product> getProducts() {
            List<Product> products = Arrays.asList(
                    new Product("Slim", 20, 2000L, true),
                    new Product("Round", 20, 2000L, true),
                    new Product("Others", 0, 0, false)
            );

            return products;
        }

        public static SalesOrder getSalesOrder(int customerId, String prodName, int prodQty) {
            String sampleRemarks = prodName +" x "+prodQty;
            return new SalesOrder(new Date(), "000000", 0, 0, 2000L, 0, customerId, sampleRemarks);
        }

        public static SalesDetail getSalesDetail(long salesOrderId, int productId) {
            return new SalesDetail(productId, 2000L, 1, salesOrderId, null);
        }
    }

    public abstract CustomerDao customerDao();
    public abstract ProductDao productDao();
    public abstract SalesOrderDao salesOrderDao();
    public abstract SalesDetailDao salesDetailDao();

}
