package be.nidel.kinomichi;

import be.nidel.kinomichi.participant.Participant;
import be.nidel.kinomichi.participant.ParticipantController;
import be.nidel.kinomichi.participant.ParticipantType;
import be.nidel.utils.DateUtils;
import be.nidel.utils.OutputUtils;
import be.nidel.utils.RandomUtils;
import be.technifutur.shared.Menu;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Kinomichi {

    List<Participant> participantList = new ArrayList<>();
    List<Animation> animationList = new ArrayList<>();

    //TODO use view & model

    ParticipantController participantController = new ParticipantController();

    public void showMainMenu() {
        OutputUtils.sOutTitle("--- Stage Kinomichi ---");
        displayMenu();
    }
    private void displayMenu() {
        Menu menu = new Menu();
        menu.addItem("Create a new participant", "1", () -> {
            participantController.showMenu(menu);
        });
//        menu.addItem("Create a new animation", "2", () -> {
//            Animation animation = createAnimationHandler("Add New Animation");
//            animationList.add(animation);
//            animationReport(animation);
//            showMainMenu();
//        });
//        menu.addItem("Quit", "q", this::quitHandler);
        menu.interact();
    }
//
//    private Animation createAnimationHandler(String inputRequest) {
//        OutputUtils.sOutInfo(inputRequest);
//        Scanner scanner = new Scanner(System.in);
//        Animation animation = new Animation(askInput(scanner, "Title of the event"));
//
//        boolean configureDay = (askInput(
//                scanner,
//                "Type (d) for configuring a day or (any key) for a single period ?")
//        ).equals("d");
//
//        if(configureDay){
//            handleNewDay(scanner, animation);
//        }else{
//            handleNewPeriod(scanner, animation);
//        }
//        return  animation;
//    }
//
//    private void handleNewDay(Scanner scanner, Animation animation) {
//        boolean addNewDay = false;
//        int amount = 1;
//        do{
//            OutputUtils.sOutInfo("A day needs the date, starting time & the number of periods");
//            animation.addNewDay(
//                    askDate(scanner, "Day1 (dd/mm/yyyy)"),
//                    askTime(scanner, "From Time (hh:mm)"),
//                    askInt(scanner, "How many periods from the start ?")
//            );
//
//            addNewDay = (askInput(
//                    scanner,
//                    "Type (d) for continue adding days or (any key) to continue")
//            ).equals("d");
//        }while(addNewDay);
//    }
//
//    private void handleNewPeriod(Scanner scanner, Animation animation) {
//        boolean addNewPeriod = false;
//        do{
//            OutputUtils.sOutInfo("A period needs the date, time & trainer");
//            Period period = animation.addNewPeriod(
//                    askDate(scanner, "Day1 (dd/mm/yyyy)"),
//                    askTime(scanner, "From Time (hh:mm)")
//            );
//            period.setTrainer(createParticipantHandler("Add New Trainer"));
//
//            addNewPeriod = (askInput(
//                    scanner,
//                    "Type (a) for adding or (any key) to continue")
//            ).equals("a");
//
//        }while(addNewPeriod);
//    }


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

        Animation animation = new Animation("Stage pour enfants - Découverte du Kinomichi");
        animation.addNewDay(samedi, midiStart, 5);
        animation.addNewDay(dimanche, midiStart, 3);

        List<Period> myPeriods = animation.getAllPeriods();

        myPeriods.forEach(p -> p.setTrainer(mockTrainer));

        //Add participants to random periods
        animation.registerAttendeeToPeriod(mockParticipant, new Period[]{
                myPeriods.get(RandomUtils.getRandomInt(0, myPeriods.size())),
                myPeriods.get(RandomUtils.getRandomInt(0, myPeriods.size())),
                myPeriods.get(RandomUtils.getRandomInt(0, myPeriods.size()))
        });
        animation.registerAttendeeToPeriod(mockParticipant2, new Period[]{
                myPeriods.get(RandomUtils.getRandomInt(0, myPeriods.size())),
                myPeriods.get(RandomUtils.getRandomInt(0, myPeriods.size()))
        });
        animation.registerAttendeeToPeriod(mockParticipant3, new Period[]{
                myPeriods.get(RandomUtils.getRandomInt(0, myPeriods.size())),
                myPeriods.get(RandomUtils.getRandomInt(0, myPeriods.size()))
        });
        animationReport(animation);
    }

    private void animationReport(Animation animation){
        System.out.println(animation);

        //print entries
        System.out.println("———————————");
        System.out.println("— RECAP —");
        System.out.println(animation.getAllPeriods().stream()
                .map(p ->
                        p.getDay().getDayOfWeek() + " " +
                                p.getStart()).toList()
        );
        System.out.println(animation.getAllAttendees());
        System.out.println(animation.getAllDays());
        System.out.println("———————————");

    }

}
