package be.nidel.utils;

import java.math.BigDecimal;

public class FormatUtils {

    public static final String PHONE_REGEX = "^\\d{6,14}$";
    public static final String EMAIL_REGEX = "^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$";

    public static String truncate(String s, int maxLength) {
        return s.length() <= maxLength ? s : s.substring(0, maxLength - 3) + "...";
    }

    public static String removeLast(String myString) {
        return myString.substring(0,myString.length()-1);
    }

    public static String formattedPhoneNumber(String phoneNumber){
        String formatted = "";
        if(phoneNumber.length() >= 6)
            formatted = phoneNumber.substring(0,3) + " " +
                    phoneNumber.substring(3);
        if(phoneNumber.length() >= 10)
            formatted = phoneNumber.substring(0,4) + " " +
                    phoneNumber.substring(4,7) + " " +
                    phoneNumber.substring(7);
        if(phoneNumber.length() >= 13)
            formatted = phoneNumber.substring(0,4) + " " +
                    phoneNumber.substring(4,7) + " " +
                    phoneNumber.substring(7, 10) + " " +
                    phoneNumber.substring(10, 13);
        return formatted;
    }

    public static String trimWhitespaces(String input) {
        return input.replaceAll("\\s+","");
    }
}
