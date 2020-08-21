package com.example.acquatikaapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.acquatikaapp.R;
import com.example.acquatikaapp.data.database.AcquatikaDatabase;
import com.example.acquatikaapp.data.dto.TransactionDto;

import java.util.List;

import static androidx.recyclerview.widget.DividerItemDecoration.VERTICAL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mCurrentTransactionsRv;
    private TransactionAdapter mTransactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCurrentTransactionsRv = findViewById(R.id.current_transactions_rv);
        mCurrentTransactionsRv.setLayoutManager(new LinearLayoutManager(this));

        mTransactionAdapter = new TransactionAdapter(this, true);
        mCurrentTransactionsRv.setAdapter(mTransactionAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mCurrentTransactionsRv.addItemDecoration(decoration);
        mCurrentTransactionsRv.setHasFixedSize(true);

        setupViewModel();
    }

    private void setupViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getTransactions().observe(this, new Observer<List<TransactionDto>>() {
            @Override
            public void onChanged(List<TransactionDto> transactionDtos) {
                Log.d(TAG, "Updating list of transactions from LiveData in ViewModel");
                mTransactionAdapter.setTransactions(transactionDtos);
            }
        });
    }
}
