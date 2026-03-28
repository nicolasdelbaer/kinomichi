package be.nidel.kinomichi.session;

import be.nidel.kinomichi.KinomichiView;
import be.nidel.utils.OutputUtils;
import be.nidel.utils.menu.MenuFactory;
import be.technifutur.shared.Menu;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class SessionView implements KinomichiView {
    SessionController sessionController;
    private Menu context;

    public SessionView(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    //TODO display view from gathering ? - QUESTION how to pass gathering data to here
    public void displayUserChoices(Menu context){
        this.context = context;
        MenuFactory.backQuitTemplate(context)
        .addItem("create new period", "c", this::gatherNewSessionData)
        .interact();
    }

    private void gatherNewSessionData() {
        OutputUtils.sOutInfo("Create a new period:");
        Scanner scanner = new Scanner(System.in);

        OutputUtils.sOutInfo("A period needs the date & time");
        LocalDate date = askDate(scanner, "Day (dd/mm/yyyy)");
        LocalTime time = askTime(scanner, "From Time (hh:mm)");

        sessionController.createSession(new SessionDTO(90, date,time));
        continueAddingSession();
    }

    public void continueAddingSession(){
        MenuFactory.confirmTemplate(context, this::gatherNewSessionData)
                .setInteractionMessage("Continue adding sessions ? (y/n)")
                .interact();
    }
}
