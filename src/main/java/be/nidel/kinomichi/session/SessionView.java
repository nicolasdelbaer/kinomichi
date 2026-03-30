package be.nidel.kinomichi.session;

import be.nidel.kinomichi.KinomichiView;
import be.nidel.utils.OutputUtils;
import be.nidel.utils.menu.MenuController;
import be.nidel.utils.menu.MenuFactory;
import be.technifutur.shared.Menu;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

public class SessionView implements KinomichiView {
    SessionController sessionController;
    private Menu context;
    private MenuController current;

    public SessionView(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public void displayUserChoices(Menu context){
        this.context = context;
        current = MenuFactory.backQuitTemplate(context)
        .addItem("create new session", "c", this::gatherNewSessionData);
        current.interact();
    }

    private void gatherNewSessionData() {
        OutputUtils.sOutInfo("Create a new session:");
        Scanner scanner = new Scanner(System.in);

        OutputUtils.sOutInfo("A session needs the date & time");
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

    @Override
    public void refresh() {
        current.interact();
    }
}
