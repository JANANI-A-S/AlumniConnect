package com.alumni.portal.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {

    private static final Locale INDIA_LOCALE = new Locale("en", "IN");

    public static String formatINR(BigDecimal amount) {
        if (amount == null) {
            return "₹0.00";
        }
        try {
            NumberFormat formatter = NumberFormat.getNumberInstance(INDIA_LOCALE);
            formatter.setMinimumFractionDigits(2);
            formatter.setMaximumFractionDigits(2);
            return "₹" + formatter.format(amount);
        } catch (Exception e) {
            DecimalFormat df = new DecimalFormat("#,##,##0.00");
            return "₹" + df.format(amount);
        }
    }
}
