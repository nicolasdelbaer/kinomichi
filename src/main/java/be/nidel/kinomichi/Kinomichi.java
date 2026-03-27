package be.nidel.kinomichi;

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

    //TODO use view
    public void showMainMenu() {
        OutputUtils.sOutTitle("--- Stage Kinomichi ---");
        displayMenu();
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
        LocalTime midiStart = DateUtils.StringTimeToLocalTime("12h30");

        Animation animation = new Animation("Stage pour enfants - Découverte du Kinomichi");
        animation.configureDay(samedi, 5, midiStart);
        animation.configureDay(dimanche, 3, midiStart);

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

        System.out.println(animation);

        //print entries
        System.out.println("———————————");
        System.out.println("— RECAP —");
        System.out.println(animation.getAllPeriods().stream().map(p -> p.getDay().getDayOfWeek() + ": " + p.getStart()).toList());
        System.out.println(animation.getAllAttendees());
        System.out.println(animation.getAllDays());
        System.out.println("———————————");

    }

    private void displayMenu() {
        Menu menu = new Menu();
        menu.addItem("Create a new participant", "1", () -> {
            createParticipantHandler("Add New Participant");
            showMainMenu();
        });
        menu.addItem("Create a new animation", "2", () -> {
            createAnimationHandler("Add New Animation");
            showMainMenu();
        });
        menu.addItem("Quit", "q", this::quitHandler);
        menu.interact();
    }

    private Animation createAnimationHandler(String inputRequest) {
        OutputUtils.sOutInfo(inputRequest);
        Scanner scanner = new Scanner(System.in);
        Animation animation = new Animation(askInput(scanner, "Title of the event"));

        Period period = animation.addNewPeriod(
                askDate(scanner, "Day1 (dd/mm/yyyy)"),
                askTime(scanner, "From Time (hh:mm)")
        );
        period.setTrainer(createParticipantHandler("Add New Trainer"));

        animationList.add(animation);
        return  animation;
    }

    private LocalTime askTime(Scanner scanner, String s) {
        LocalTime time = DateUtils.StringTimeToLocalTime("12h30");
        return time;
    }

    private LocalDate askDate(Scanner scanner, String s) {
        LocalDate date = DateUtils.StringDateToLocalDate("29/03/2026");
        return date;
    }

    public Participant createParticipantHandler(String inputRequest) {
        OutputUtils.sOutInfo(inputRequest);
        Scanner scanner = new Scanner(System.in);

        Participant participant = new Participant.Builder()
                .setFirstName(askInput(scanner, "First name?"))
                .setLastName(askInput(scanner, "Last name?"))
                .setPhone(askInput(scanner, "Phone ?"))
                .setEmail(askInput(scanner, "Email ?"))
                .setClubName(askInput(scanner, "Club Name ?"))
                //TODO add Participant Type from int input
                .setType(requestParticipantType(scanner))
                .build();

        participantList.add(participant);
        return participant;
    }

    private ParticipantType requestParticipantType(Scanner scanner) {
        OutputUtils.sOutInfo(ParticipantType.values().toString());
        Optional<ParticipantType> type = null;

        do{
            //TODO refactor change enum ?
            int id = -1; //-1 because of the enum id = 0
            try {
                id = Integer.parseInt(
                        askInput(scanner, "1. Attendee | 2. Trainer | 3. VIP")
                );
                type = ParticipantType.getByOrdinal(id-1); //-1 for handling ids from 1 in input
            } catch (NumberFormatException e) {
            }
        }while(!type.isPresent());

        return type.get();
    }

    private void quitHandler() {
        participantList.forEach(p -> OutputUtils.sOutWarning(p.toString()));
    }

    private String askInput(Scanner scanner, String requestQuote) {
        OutputUtils.sOutInfo(requestQuote);
        return scanner.nextLine();
    }
}
