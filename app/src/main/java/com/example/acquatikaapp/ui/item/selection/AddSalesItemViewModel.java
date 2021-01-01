package com.example.acquatikaapp.ui.item.selection;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.acquatikaapp.data.dao.ProductDao;
import com.example.acquatikaapp.data.dto.AddSalesItemDto;
import com.example.acquatikaapp.data.model.Product;
import com.example.acquatikaapp.data.repository.ProductRepository;
import com.example.acquatikaapp.data.util.Constants;
import com.example.acquatikaapp.ui.util.ValueUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddSalesItemViewModel extends AndroidViewModel {

    private LiveData<List<Product>> products;
    private LiveData<Map<Integer, Product>> productsMap;
    private ProductRepository productRepository;

    private AddSalesItemDto mSlimItems;
    private AddSalesItemDto mRoundItems;
    private AddSalesItemDto mCustomItems;

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
        for (Product product : products) {
            mapIdProduct.put(product.getId(), product);
        }

        return mapIdProduct;
    }

    public ArrayList<AddSalesItemDto> generateSalesDetailData() {
        ArrayList<AddSalesItemDto> addSalesItemDtos = new ArrayList<>();
        if (mSlimItems != null && mSlimItems.getQuantity() != 0) {
            addSalesItemDtos.add(mSlimItems);
        }

        if (mRoundItems != null && mRoundItems.getQuantity() != 0) {
            addSalesItemDtos.add(mRoundItems);
        }

        if (mCustomItems != null && mCustomItems.getQuantity() != 0) {
            addSalesItemDtos.add(mCustomItems);
        }

        return addSalesItemDtos;
    }

    public AddSalesItemDto getSalesItem(Product product) {
        switch (product.getId()) {
            case Constants.SLIM:
                if (mSlimItems == null) {
                    mSlimItems = new AddSalesItemDto(product.getId(), product.getName(), product.getPrice(), 0);
                }
                return mSlimItems;
            case Constants.ROUND:
                if (mRoundItems == null) {
                    mRoundItems = new AddSalesItemDto(product.getId(), product.getName(), product.getPrice(), 0);
                }
                return mRoundItems;
            case Constants.CUSTOM:
                if (mCustomItems == null) {
                    mCustomItems = new AddSalesItemDto(product.getId(), product.getName(), 0, 0);
                }
                return mCustomItems;
            default:
                break;
        }

        return null;
    }

    public int calculateAddedItem(AddSalesItemDto item) {
        int quantity = item.getQuantity();
        item.setQuantity(++quantity);
        item.setPrice(item.getQuantity() * item.getPrice());
        return quantity;
    }

}
