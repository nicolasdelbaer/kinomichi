package be.nidel.kinomichi;

import be.nidel.kinomichi.gathering.*;
import be.nidel.kinomichi.participant.*;
import be.nidel.kinomichi.session.Session;
import be.nidel.kinomichi.session.SessionController;
import be.nidel.kinomichi.session.SessionDTO;
import be.nidel.kinomichi.session.SessionModel;
import be.nidel.utils.DateUtils;
import be.nidel.utils.OutputUtils;
import be.nidel.utils.RandomUtils;
import be.technifutur.shared.Menu;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Kinomichi {

    //TODO use view & model
    GatheringController gatheringController;
    SessionController sessionController;
    ParticipantController participantController;

    public Kinomichi() {
        gatheringController = new GatheringController();
        sessionController = new SessionController();
        participantController = new ParticipantController();

        gatheringController.onSessionRequest.connect((GatheringPayload payload) -> sessionController.showManageMenu(payload));

        defaultData();
    }

    public void launch() {
        OutputUtils.sOutTitle("--- Kinomichi Administration Console ---");
        OutputUtils.sOutInfo("Hello, what do you want to do today?");
        displayMenu();
    }
    private void displayMenu() {
        int id = 1;
        Menu menu = new Menu();
        menu.addItem("Create a new gathering", String.valueOf(id++), () -> {
            gatheringController.showMenu(menu);
        });
        menu.addItem("Create a new participant", String.valueOf(id++), () -> {
            participantController.showMenu(menu);
        });
//        menu.addItem("Create a new session", String.valueOf(id++), () -> {
//            sessionController.showMenu(menu);
//        });
        menu.addItem("Quit", "q", this::quitApplication);
        menu.interact();
    }

    private void quitApplication() {

    }


    public void defaultData() {
    }

    private void animationReport(Gathering gathering){
        System.out.println(gathering);

        //print entries
        System.out.println("———————————");
        System.out.println("— RECAP —");
        System.out.println(gathering.getAllSessions().stream()
                .map(p ->
                        p.getDay().getDayOfWeek() + " " +
                                p.getStart()).toList()
        );
        System.out.println(gathering.getAllAttendees());
        System.out.println(gathering.getAllDays());
        System.out.println("———————————");

    }

}
