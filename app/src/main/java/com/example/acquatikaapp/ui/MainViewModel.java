package com.example.acquatikaapp.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.acquatikaapp.data.dto.DashboardProductsCount;
import com.example.acquatikaapp.data.dto.ProductNameIdDto;
import com.example.acquatikaapp.data.dto.SalesLineGraphDto;
import com.example.acquatikaapp.data.dto.SalesOrderDto;
import com.example.acquatikaapp.data.dto.TotalQuantityPerProductDto;
import com.example.acquatikaapp.data.repository.CustomerRepository;
import com.example.acquatikaapp.data.repository.ProductRepository;
import com.example.acquatikaapp.data.repository.SalesOrderRepository;
import com.example.acquatikaapp.ui.util.DateUtil;

import java.util.Date;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private SalesOrderRepository salesOrderRepository;
    private ProductRepository productRepository;
    private LiveData<Long> currentTotalSales;
    private LiveData<List<SalesOrderDto>> currentSalesOrders;
    private LiveData<SalesLineGraphDto> salesLineGraphValues;
    private LiveData<List<TotalQuantityPerProductDto>> productCount;
    private LiveData<DashboardProductsCount> dashboardProductsCount;

    private Date fromDate = DateUtil.getStartOfDay(new Date());
    private Date toDate = DateUtil.getEndOfDay(new Date());

    public MainViewModel(@NonNull Application application) {
        super(application);

        salesOrderRepository = new SalesOrderRepository(application);
        productRepository = new ProductRepository(application);

        currentSalesOrders = salesOrderRepository
                .getSalesOrders(fromDate, toDate,
                        null, null, null);

        currentTotalSales = salesOrderRepository.getCurrentTotalSales(fromDate);
        salesLineGraphValues = salesOrderRepository.getSalesAndDate(fromDate);
        productCount = productRepository.getProductCount(fromDate, toDate, null);

        dashboardProductsCount = Transformations.map(productCount, new Function<List<TotalQuantityPerProductDto>, DashboardProductsCount>() {
            @Override
            public DashboardProductsCount apply(List<TotalQuantityPerProductDto> products) {
               return generateDashboardProductsCount(products);
            }
        });
    }

    public LiveData<List<SalesOrderDto>> getCurrentSalesOrders() {
        return currentSalesOrders;
    }

    public LiveData<SalesLineGraphDto> getSalesLineGraphValues() {
        return salesLineGraphValues;
    }

    public LiveData<Long> getCurrentTotalSales() {
        return currentTotalSales;
    }

    public LiveData<DashboardProductsCount> getDashboardProductsCount() {
        return dashboardProductsCount;
    }

    private DashboardProductsCount generateDashboardProductsCount(List<TotalQuantityPerProductDto> products) {
        DashboardProductsCount dashboardProductsCount = new DashboardProductsCount("N/A", 0, "N/A", 0, 0);
        if (products == null || products.isEmpty()) {
            return dashboardProductsCount;
        }

        for (TotalQuantityPerProductDto product : products) {
            if (product.isOnDashboard()) {
                if ("N/A".compareTo(dashboardProductsCount.getProductLeftLabel()) == 0) {
                    dashboardProductsCount.setProductLeftLabel(product.getName());
                    dashboardProductsCount.setProductLeftCount(product.getTotal());
                    continue;
                } else {
                    dashboardProductsCount.setProductCenterLabel(product.getName());
                    dashboardProductsCount.setProductCenterCount(product.getTotal());
                    continue;
                }
            }

            dashboardProductsCount.setProductOthersCount(
                    dashboardProductsCount.getProductOthersCount() + product.getTotal());
        }

        return dashboardProductsCount;
    }
}
