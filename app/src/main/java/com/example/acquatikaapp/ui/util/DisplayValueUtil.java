package com.example.acquatikaapp.ui.util;

import android.app.Application;

import com.example.acquatikaapp.data.dao.ProductDao;
import com.example.acquatikaapp.data.database.AcquatikaDatabase;
import com.example.acquatikaapp.data.model.Product;
import com.example.acquatikaapp.data.model.SalesDetail;
import com.example.acquatikaapp.data.repository.ProductRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public final class DisplayValueUtil {

//    private static final int DECIMAL_PLACES = 2;

    public static String convertPriceToDisplayValue(long price) {
//        BigDecimal priceBd = new BigDecimal(price);
//        priceBd = priceBd.movePointLeft(DECIMAL_PLACES);
//        priceBd = priceBd.setScale(DECIMAL_PLACES, RoundingMode.HALF_UP);
//        return NumberFormat.getCurrencyInstance().format(priceBd);

        NumberFormat priceFormat = NumberFormat.getCurrencyInstance();
        return priceFormat.format(price / 100.0);
    }

    public static String getDisplayValueDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(date);
    }

    public static String getDisplayValueTime(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        return dateFormat.format(date);
    }

    public static long convertDisplayPriceToLong(String priceString) {
        return Long.parseLong(priceString) * 100L;
    }

    public static String convertAmountToDisplayValue(long price) {
        return String.valueOf(price / 100);
    }

}
