package be.nidel.kinomichi.session;

import be.nidel.kinomichi.base.BaseView;
import be.nidel.kinomichi.gathering.Gathering;
import be.nidel.utils.OutputUtils;
import be.nidel.utils.menu.MenuController;
import be.nidel.utils.menu.MenuFactory;
import be.technifutur.shared.Menu;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Scanner;

import static be.nidel.utils.InputUtils.askDate;
import static be.nidel.utils.InputUtils.askTime;

public class SessionView extends BaseView<SessionController> {
    private Gathering gathering;

    public SessionView(SessionController controller) {
        super(controller);
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

        controller.createSession(new SessionDTO(90, date,time), gathering);
        continueAddingSession();
    }

    public void continueAddingSession(){
        MenuFactory.confirmTemplate(context, this::gatherNewSessionData)
                .setInteractionMessage("Continue adding sessions ? (y/n)")
                .interact();
    }

    public void setGatheringData(Gathering gathering) {
        this.gathering = gathering;
    }

    public void showUnknownGatheringError() {
        OutputUtils.sOutWarning("Trying to add session to unknown gathering");
    }
}
