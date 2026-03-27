package be.nidel.kinomichi;

import be.nidel.utils.OutputUtils;
import be.technifutur.shared.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Kinomichi {

    List<Participant> participantList = new ArrayList<>();

    public void showMainMenu() {
        OutputUtils.sOutTitle("--- Stage Kinomichi ---");
        displayMenu();
    }

    private void displayMenu() {
        Menu menu = new Menu();
        menu.addItem("Add a new participant", "1", () -> {
            addParticipantHandler();
            showMainMenu();
        });
        menu.addItem("Quit", "q", this::quitHandler);
        menu.interact();
    }

    private void quitHandler() {
        participantList.forEach(p -> OutputUtils.sOutWarning(p.toString()));
    }

    public void addParticipantHandler() {
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

    private String askInput(Scanner scanner, String requestQuote) {
        OutputUtils.sOutInfo(requestQuote);
        return scanner.nextLine();
    }
}
