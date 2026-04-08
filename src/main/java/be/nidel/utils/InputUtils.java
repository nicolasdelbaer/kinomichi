package be.nidel.utils;

import be.nidel.kinomichi.participant.ParticipantType;
import be.nidel.kinomichi.session.SessionType;
import be.nidel.utils.inputprovider.InputProvider;
import be.nidel.utils.menu.MenuFactory;
import be.technifutur.shared.Menu;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputUtils {

    public static LocalTime askTime(InputProvider inputProvider, String s) {
        LocalTime time = null;
        do{
            try{
                time = DateUtils.StringTimeToLocalTime(askInput(inputProvider, s));
            } catch (IllegalArgumentException | NullPointerException | DateTimeException ignored) {}
        }while(Objects.isNull(time));
        return time;
    }

    public static LocalDate askDate(InputProvider inputProvider, String s) {
        LocalDate date = null;
        do{
            try{
                date = DateUtils.StringDateToLocalDate(askInput(inputProvider, s));
            } catch (IllegalArgumentException | NullPointerException | DateTimeException ignored) {}
        }while(Objects.isNull(date));
        return date;
    }

    public static int askInt(InputProvider inputProvider, String inputRequest) {
        Integer result = null;
        do{
            try{
                result = Integer.parseInt(askInput(inputProvider, inputRequest));
            } catch (IllegalArgumentException | NullPointerException ignored) {}
        }while(Objects.isNull(result));

        return result;
    }

    public static BigDecimal askBigDecimal(InputProvider inputProvider, String inputRequest) {
        BigDecimal result = null;
        do{
            try{
                result = new BigDecimal(askInput(inputProvider, inputRequest));
            } catch (IllegalArgumentException|NullPointerException ignored) {}
        }while(Objects.isNull(result));

        return result;
    }

    public static String askInput(InputProvider inputProvider, String inputRequest) {
        OutputUtils.sOutInfo(inputRequest);
        return inputProvider.nextLine();
    }

    public static String askEmail(InputProvider inputProvider, String inputRequest) {
        boolean hasMatch = false;
        String input = null;
        do{
            try{
                input = null;
                input = askInput(inputProvider, inputRequest);
                input = FormatUtils.trimWhitespaces(input);
                hasMatch = Pattern.matches(EMAIL_REGEX,input);
            } catch (IllegalArgumentException | NullPointerException ignored) {
                OutputUtils.sOutWarning("Wrong format, pls try again");
            }
        }while(Objects.isNull(input) || !hasMatch);
        return input;
    }

    public static String askPhone(InputProvider inputProvider, String inputRequest) {
        boolean hasMatch = false;
        String input = null;
        do{
            try{
                input = null;
                input = askInput(inputProvider, inputRequest + " format(0032 484 888 777)");
                input = FormatUtils.trimWhitespaces(input);
                hasMatch = Pattern.matches(PHONE_REGEX,input);
            } catch (IllegalArgumentException | NullPointerException ignored) {
                OutputUtils.sOutWarning("Wrong format, pls try again");
            }
        }while(Objects.isNull(input) || !hasMatch);
        return input;
    }

    public static void askForEditOrSource(Menu context, Consumer<String> action, String field, String content){
        askForEditOrSource(context, action, field, content, ".*");
    }
    public static void askForEditOrSource(Menu context, Consumer<String> action, String field, String content, String freeInputPattern){
        MenuFactory.editTemplate(context, action, field, content, freeInputPattern).renderAndInteract();
    }


    public static final String PHONE_REGEX = "^\\d{6,14}$";
    public static final String EMAIL_REGEX = "^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$";
}
