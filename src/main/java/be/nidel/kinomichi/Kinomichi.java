package be.nidel.kinomichi;

import be.nidel.utils.DateUtils;
import be.nidel.utils.OutputUtils;
import be.nidel.utils.RandomUtils;
import be.technifutur.shared.Menu;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Kinomichi {

    List<Participant> participantList = new ArrayList<>();
    List<Animation> animationList = new ArrayList<>();

    public void showMainMenu() {
        OutputUtils.sOutTitle("--- Stage Kinomichi ---");
        displayMenu();
    }
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
        LocalTime midiStart = DateUtils.StringTimeToLocalTime("12h30");

        Animation animation = new Animation.Builder()
                .setTitle("Stage pour enfants - Découverte du Kinomichi")
                .build();
        animation.configureDay(samedi, 5, midiStart);
        animation.configureDay(dimanche, 3, midiStart);

        List<Period> myPeriods = animation.getAllPeriods();

        myPeriods.forEach(p -> p.setTrainer(mockTrainer));

        //Add participants to random periods
        animation.addAttendeeToPeriod(mockParticipant, new Period[]{
                myPeriods.get(gId(myPeriods.size())),
                myPeriods.get(gId(myPeriods.size())),
                myPeriods.get(gId(myPeriods.size()))
        });
        animation.addAttendeeToPeriod(mockParticipant2, new Period[]{
                myPeriods.get(gId(myPeriods.size())),
                myPeriods.get(gId(myPeriods.size()))
        });
        animation.addAttendeeToPeriod(mockParticipant3, new Period[]{
                myPeriods.get(gId(myPeriods.size())),
                myPeriods.get(gId(myPeriods.size()))
        });

        System.out.println(animation);


        //print entries
        System.out.println("———————————");
        System.out.println("— RECAP —");
        System.out.println(animation.getAllPeriods().stream().map(p -> p.getDay().getDayOfWeek() + ": " + p.getStart()).toList());
        System.out.println(animation.getAllAttendees());
        System.out.println(animation.getAllDays());
        System.out.println("———————————");

    }
    private int gId(int size){
        return RandomUtils.getRandomInt(0, size);
    }

    private void displayMenu() {
        Menu menu = new Menu();
        menu.addItem("Create a new participant", "1", () -> {
            createParticipantHandler();
            showMainMenu();
        });
        menu.addItem("Create a new animation", "2", () -> {
            createAnimationHandler();
            showMainMenu();
        });
        menu.addItem("Quit", "q", this::quitHandler);
        menu.interact();
    }

    private void createAnimationHandler() {
        OutputUtils.sOutInfo("Add Animation");
        Scanner scanner = new Scanner(System.in);
        Animation animation = new Animation.Builder()
                .setTitle(askInput(scanner, "Title of the event"))
//                .addDayHelper(
//                        askInput(scanner, "Day1 (dd/mm/yyyy)"),
//                        askInput(scanner, "Number of periods"),
//                        askInput(scanner, "From Time (hh:mm)"),
//                        askInput(scanner, "Trainer id?"),
//                )
                .build();

        animationList.add(animation);
    }

    public void createParticipantHandler() {
        OutputUtils.sOutInfo("Add Participant");
        Scanner scanner = new Scanner(System.in);

        Participant participant = new Participant.Builder()
                .setFirstName(askInput(scanner, "First name?"))
                .setLastName(askInput(scanner, "Last name?"))
                .setPhone(askInput(scanner, "Phone ?"))
                .setEmail(askInput(scanner, "Email ?"))
                .setClubName(askInput(scanner, "Club Name ?"))
                //TODO add Participant Type from int input
                .setType(ParticipantType.Attendee)
                .build();

        participantList.add(participant);
    }

    private void quitHandler() {
        participantList.forEach(p -> OutputUtils.sOutWarning(p.toString()));
    }

    private String askInput(Scanner scanner, String requestQuote) {
        OutputUtils.sOutInfo(requestQuote);
        return scanner.nextLine();
    }
}
