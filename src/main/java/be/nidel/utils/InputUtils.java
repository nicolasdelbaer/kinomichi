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
                OutputUtils.sOutInfo(inputRequest);
                result = inputProvider.nextInt();
            } catch (NoSuchElementException | IllegalStateException ignored) {}
        }while(Objects.isNull(result));

        return result;
    }

    public static BigDecimal askBigDecimal(InputProvider inputProvider, String inputRequest) {
        BigDecimal result = null;
        do{
            try{
                OutputUtils.sOutInfo(inputRequest);
                result = inputProvider.nextBigDecimal();
            } catch (NoSuchElementException | IllegalStateException ignored) {}
        }while(Objects.isNull(result));

        return result;
    }

    public static String askInput(InputProvider inputProvider, String inputRequest) {
        OutputUtils.sOutInfo(inputRequest);
        return inputProvider.nextLine();
    }

    public static void askForEditOrSource(Menu context, Consumer<String> action, String field, String content){
        MenuFactory.editTemplate(context, action, field, content).renderAndInteract();
    }
}
