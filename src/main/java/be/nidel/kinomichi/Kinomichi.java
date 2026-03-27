package be.nidel.kinomichi;

import be.nidel.kinomichi.gathering.Gathering;
import be.nidel.kinomichi.gathering.GatheringController;
import be.nidel.kinomichi.participant.Participant;
import be.nidel.kinomichi.participant.ParticipantController;
import be.nidel.kinomichi.participant.ParticipantType;
import be.nidel.kinomichi.session.Session;
import be.nidel.kinomichi.session.SessionController;
import be.nidel.utils.DateUtils;
import be.nidel.utils.OutputUtils;
import be.nidel.utils.RandomUtils;
import be.technifutur.shared.Menu;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Kinomichi {

    List<Participant> participantList = new ArrayList<>();
    List<Gathering> gatheringList = new ArrayList<>();

    //TODO use view & model

    GatheringController gatheringController;
    SessionController sessionController;
    ParticipantController participantController;

    public Kinomichi() {
        gatheringController = new GatheringController();
        sessionController = new SessionController();
        participantController = new ParticipantController();
    }

    public void showMainMenu() {
        OutputUtils.sOutTitle("--- Stage Kinomichi ---");
        displayMenu();
    }
    private void displayMenu() {
        int id = 1;
        Menu menu = new Menu();
        menu.addItem("Create a new participant", String.valueOf(id++), () -> {
            participantController.showMenu(menu);
        });
        menu.addItem("Create a new session", String.valueOf(id++), () -> {
            sessionController.showMenu(menu);
        });
        menu.addItem("Create a new gathering", String.valueOf(id++), () -> {
            gatheringController.showMenu(menu);
        });
        menu.addItem("Quit", "q", this::quitHandler);
        menu.interact();
    }

    private void quitHandler() {
        participantList.forEach(p -> OutputUtils.sOutWarning(p.toString()));
    }

    //TODO refactor - move somewhere else
    public void debug() {
        Participant mockTrainer = new Participant.Builder()
                .setFirstName("Laurence")
                .setLastName("Sensei")
                .setType(ParticipantType.Trainer)
                .build();

        Participant mockParticipant = new Participant.Builder()
                .setFirstName("Al")
                .setLastName("Paccino")
                .setType(ParticipantType.Attendee)
                .build();

        Participant mockParticipant2 = new Participant.Builder()
                .setFirstName("De")
                .setLastName("Niro")
                .setType(ParticipantType.Attendee)
                .build();

        Participant mockParticipant3 = new Participant.Builder()
                .setFirstName("Patrick")
                .setLastName("Demer")
                .setType(ParticipantType.Attendee)
                .build();

        LocalDate samedi = DateUtils.StringDateToLocalDate("28/03/2026");
        LocalDate dimanche = DateUtils.StringDateToLocalDate("29/03/2026");
        LocalTime midiStart = DateUtils.StringTimeToLocalTime("12:30");

        Gathering gathering = new Gathering("Stage pour enfants - Découverte du Kinomichi");
        gathering.addNewDay(samedi, midiStart, 5);
        gathering.addNewDay(dimanche, midiStart, 3);

        List<Session> mySessions = gathering.getAllPeriods();

        mySessions.forEach(p -> p.setTrainer(mockTrainer));

        //Add participants to random periods
        gathering.registerAttendeeToPeriod(mockParticipant, new Session[]{
                mySessions.get(RandomUtils.getRandomInt(0, mySessions.size())),
                mySessions.get(RandomUtils.getRandomInt(0, mySessions.size())),
                mySessions.get(RandomUtils.getRandomInt(0, mySessions.size()))
        });
        gathering.registerAttendeeToPeriod(mockParticipant2, new Session[]{
                mySessions.get(RandomUtils.getRandomInt(0, mySessions.size())),
                mySessions.get(RandomUtils.getRandomInt(0, mySessions.size()))
        });
        gathering.registerAttendeeToPeriod(mockParticipant3, new Session[]{
                mySessions.get(RandomUtils.getRandomInt(0, mySessions.size())),
                mySessions.get(RandomUtils.getRandomInt(0, mySessions.size()))
        });
        animationReport(gathering);
    }

    private void animationReport(Gathering gathering){
        System.out.println(gathering);

        //print entries
        System.out.println("———————————");
        System.out.println("— RECAP —");
        System.out.println(gathering.getAllPeriods().stream()
                .map(p ->
                        p.getDay().getDayOfWeek() + " " +
                                p.getStart()).toList()
        );
        System.out.println(gathering.getAllAttendees());
        System.out.println(gathering.getAllDays());
        System.out.println("———————————");

    }

}
