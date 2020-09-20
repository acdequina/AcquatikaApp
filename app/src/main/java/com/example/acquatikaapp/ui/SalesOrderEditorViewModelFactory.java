package com.example.acquatikaapp.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SalesOrderEditorViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Application mApplication;
    private final long mSalesOrderId;

    public SalesOrderEditorViewModelFactory(Application mApplication, Long mSalesOrderId) {
        this.mApplication = mApplication;
        this.mSalesOrderId = mSalesOrderId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SalesOrderEditorViewModel(mApplication, mSalesOrderId);
    }
}
