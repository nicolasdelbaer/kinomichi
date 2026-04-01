package be.nidel.utils;

import be.nidel.kinomichi.participant.ParticipantType;
import be.nidel.kinomichi.session.SessionType;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Scanner;

public class InputUtils {

    public static LocalTime askTime(Scanner scanner, String s) {
        LocalTime time = null;
        do{
            try{
                time = DateUtils.StringTimeToLocalTime(askInput(scanner, s));
            } catch (IllegalArgumentException | NullPointerException | DateTimeException ignored) {}
        }while(Objects.isNull(time));
        return time;
    }

    public static LocalDate askDate(Scanner scanner, String s) {
        LocalDate date = null;
        do{
            try{
                date = DateUtils.StringDateToLocalDate(askInput(scanner, s));
            } catch (IllegalArgumentException | NullPointerException | DateTimeException ignored) {}
        }while(Objects.isNull(date));
        return date;
    }

    public static int askInt(Scanner scanner, String inputRequest) {
        Integer result = null;
        do{
            try{
                result = Integer.parseInt(askInput(scanner, inputRequest));
            } catch (IllegalArgumentException | NullPointerException ignored) {}
        }while(Objects.isNull(result));

        return result;
    }

    public static BigDecimal askBigDecimal(Scanner scanner, String inputRequest) {
        BigDecimal result = null;
        do{
            try{
                result = new BigDecimal(askInput(scanner, inputRequest));
            } catch (IllegalArgumentException|NullPointerException ignored) {}
        }while(Objects.isNull(result));

        return result;
    }

    public static String askInput(Scanner scanner, String inputRequest) {
        OutputUtils.sOutInfo(inputRequest);
        return scanner.nextLine();
    }
}
