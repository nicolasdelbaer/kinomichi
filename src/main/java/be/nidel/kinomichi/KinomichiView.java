package be.nidel.kinomichi;

import be.nidel.utils.DateUtils;
import be.nidel.utils.OutputUtils;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Scanner;
import be.technifutur.shared.Menu;

public interface KinomichiView {

    default LocalTime askTime(Scanner scanner, String s) {
        LocalTime time = null;
        do{
            try{
                time = DateUtils.StringTimeToLocalTime(askInput(scanner, s));
            } catch (IllegalArgumentException | DateTimeException ignored) {}
        }while(Objects.isNull(time));
        return time;
    }

    default LocalDate askDate(Scanner scanner, String s) {
        LocalDate date = null;
        do{
            try{
                date = DateUtils.StringDateToLocalDate(askInput(scanner, s));
            } catch (IllegalArgumentException | DateTimeException ignored) {}
        }while(Objects.isNull(date));
        return date;
    }

    default int askInt(Scanner scanner, String inputRequest) {
        Integer result = null;
        do{
            try{
                result = Integer.parseInt(askInput(scanner, inputRequest));
            } catch (IllegalArgumentException ignored) {}
        }while(Objects.isNull(result));

        return result;
    }

    default String askInput(Scanner scanner, String inputRequest) {
        OutputUtils.sOutInfo(inputRequest);
        return scanner.nextLine();
    }

    void refresh();
}
