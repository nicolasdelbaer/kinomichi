package be.nidel.kinomichi;

import be.nidel.utils.DateUtils;
import be.nidel.utils.OutputUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Scanner;
import be.technifutur.shared.Menu;

public interface KinomichiView {

    default LocalTime askTime(Scanner scanner, String s) {
        LocalTime time = DateUtils.StringTimeToLocalTime(askInput(scanner, s));
        return time;
    }

    default LocalDate askDate(Scanner scanner, String s) {
        LocalDate date = DateUtils.StringDateToLocalDate(askInput(scanner, s));
        return date;
    }

    default int askInt(Scanner scanner, String inputRequest) {
        Integer result = null;
        do{
            try{
                result = Integer.parseInt(askInput(scanner, inputRequest));
            } catch (NumberFormatException e) {}
        }while(Objects.isNull(result));

        return result;
    }

    default String askInput(Scanner scanner, String inputRequest) {
        OutputUtils.sOutInfo(inputRequest);
        return scanner.nextLine();
    }

    default void handleBack(Menu context){
        context.interact();
    }
    default void handleQuit(Menu context){
        OutputUtils.sOutTitle("Bye bye !");
    }
}
