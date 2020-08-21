package com.example.acquatikaapp.ui;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.acquatikaapp.data.database.AcquatikaDatabase;
import com.example.acquatikaapp.data.dto.TransactionDto;
import com.example.acquatikaapp.data.model.Customer;
import com.example.acquatikaapp.data.model.SalesOrder;
import com.example.acquatikaapp.data.repository.SalesOrderRepository;
import com.example.acquatikaapp.ui.util.DateUtil;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private SalesOrderRepository salesOrderRepository;
    private LiveData<List<TransactionDto>> transactions;
    private Date currentDate = new Date();

    public MainViewModel(@NonNull Application application) {
        super(application);

        salesOrderRepository = new SalesOrderRepository(application);

        Date fromDate = DateUtil.getStartOfDay(currentDate);
        Date toDate = DateUtil.getEndOfDay(currentDate);

        transactions = salesOrderRepository
                .getTransactions(DateUtil.getStartOfDay(currentDate), DateUtil.getEndOfDay(currentDate),
                        null, null, null);
    }

    public LiveData<List<TransactionDto>> getTransactions() {
        return transactions;
    }

}
