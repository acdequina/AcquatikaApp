package com.example.acquatikaapp.ui.util;

import android.app.Application;

import com.example.acquatikaapp.data.dao.ProductDao;
import com.example.acquatikaapp.data.database.AcquatikaDatabase;
import com.example.acquatikaapp.data.model.Product;
import com.example.acquatikaapp.data.model.SalesDetail;
import com.example.acquatikaapp.data.repository.ProductRepository;
import com.example.acquatikaapp.data.util.Constants;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public final class ValueUtil {

//    private static final int DECIMAL_PLACES = 2;

    public static String convertPriceToDisplayValue(long price) {
//        BigDecimal priceBd = new BigDecimal(price);
//        priceBd = priceBd.movePointLeft(DECIMAL_PLACES);
//        priceBd = priceBd.setScale(DECIMAL_PLACES, RoundingMode.HALF_UP);
//        return NumberFormat.getCurrencyInstance().format(priceBd);

        NumberFormat priceFormat = new DecimalFormat("₱#,###");
        return priceFormat.format(price / 100.0);
    }

    public static long convertDisplayPriceToLong(String priceString) {
        return Long.parseLong(priceString) * 100L;
    }

    public static String convertAmountToDisplayValue(long amount) {
        return String.valueOf(amount / 100);
    }

    public static String getOrderTypeName(int value) {
        switch (value) {
            case Constants.DELIVERY:
                return "DELIVERY";
            case Constants.WALKIN:
            default:
                return "WALK-IN";
        }
    }

    public static String getStatusName(int value) {
        switch (value) {
            case Constants.PENDING:
                return "PENDING";
            case Constants.COMPLETE:
            default:
                return "COMPLETED";
        }
    }

}
