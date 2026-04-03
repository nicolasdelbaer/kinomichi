package be.nidel.utils;

import java.math.BigDecimal;

public class FormatUtils {

    public static String truncate(String s, int maxLength) {
        return s.length() <= maxLength ? s : s.substring(0, maxLength - 3) + "...";
    }

    public static String removeLast(String myString) {
        return myString.substring(0,myString.length()-1);
    }
}
