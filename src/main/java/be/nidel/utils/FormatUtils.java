package be.nidel.utils;

import java.math.BigDecimal;

public class FormatUtils {

    public static String formatPrice(BigDecimal price){
        String result = "";
        if(price.compareTo(BigDecimal.ZERO) == 0)
            result = "FREE";
        else
            result = price.toString() + " eur";
        return result;
    }

    public static String truncate(String s, int maxLength) {
        return s.length() <= maxLength ? s : s.substring(0, maxLength - 3) + "...";
    }
}
