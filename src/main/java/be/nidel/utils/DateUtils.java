package be.nidel.utils;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateUtils {

    public static LocalDate StringDateToLocalDate(String stringDate){
        var extractDate = stringDate.split("/");
        int year = Integer.parseInt(extractDate[2]);
        int month = Integer.parseInt(extractDate[1]);
        int day = Integer.parseInt(extractDate[0]);
        return LocalDate.of(year, month, day);
    }

    public static LocalTime StringTimeToLocalTime(String stringTime) {
        var extractDate = stringTime.split(":");
        int hour = Integer.parseInt(extractDate[0]);
        int minute = Integer.parseInt(extractDate[1]);
        return LocalTime.of(hour,minute);
    }
}
