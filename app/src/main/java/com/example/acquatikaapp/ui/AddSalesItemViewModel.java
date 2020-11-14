package com.example.acquatikaapp.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.acquatikaapp.data.dao.ProductDao;
import com.example.acquatikaapp.data.model.Product;
import com.example.acquatikaapp.data.repository.ProductRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddSalesItemViewModel extends AndroidViewModel {

    private LiveData<List<Product>> products;
    private LiveData<Map<Integer, Product>> productsMap;
    private ProductRepository productRepository;

    public AddSalesItemViewModel(@NonNull Application application) {
        super(application);

        productRepository = new ProductRepository(application);

        products = productRepository.getAllProducts();

        productsMap = Transformations.map(products, new Function<List<Product>, Map<Integer, Product>>() {

            @Override
            public Map<Integer, Product> apply(List<Product> input) {
                return mapProducts(input);
            }
        });
    }

    public LiveData<List<Product>> getAllProducts() {
        return products;
    }

    public LiveData<Map<Integer, Product>> getProductsMap() {
        return productsMap;
    }

    public Map<Integer, Product> mapProducts(List<Product> products) {
        Map<Integer, Product> mapIdProduct = new HashMap<>();
        for(Product product : products) {
            mapIdProduct.put(product.getId(), product);
        }

        return mapIdProduct;
    }


}
