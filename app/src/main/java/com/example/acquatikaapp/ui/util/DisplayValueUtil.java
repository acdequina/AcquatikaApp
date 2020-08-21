package com.example.acquatikaapp.ui.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

public final class DisplayValueUtil {

//    private static final int DECIMAL_PLACES = 2;

    public static String convertPriceToStringDisplayValue(long price) {
//        BigDecimal priceBd = new BigDecimal(price);
//        priceBd = priceBd.movePointLeft(DECIMAL_PLACES);
//        priceBd = priceBd.setScale(DECIMAL_PLACES, RoundingMode.HALF_UP);
//        return NumberFormat.getCurrencyInstance().format(priceBd);

        NumberFormat priceFormat = NumberFormat.getCurrencyInstance();
        return priceFormat.format(price / 100.0);
    }
}
