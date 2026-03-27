package be.nidel.kinomichi.participant;
import be.nidel.kinomichi.KinomichiView;
import be.nidel.utils.OutputUtils;
import be.technifutur.shared.Menu;

import java.util.Optional;
import java.util.Scanner;

public class ParticipantView implements KinomichiView {
    private ParticipantController controller;
    private Menu context;

    public ParticipantView(ParticipantController controller) {
        this.controller = controller;
    }

    public void displayUserChoices(Menu context){
        this.context = context;
        Menu defaultMenu = new Menu();
        defaultMenu.addItem("create new participant", "c", this::gatherParticipantData);
        defaultMenu.addItem("back", "b", () -> handleBack(context));
        defaultMenu.addItem("quit", "q", () -> handleQuit(context));
        defaultMenu.interact();
    }

    private void gatherParticipantData() {
        OutputUtils.sOutInfo("Create a new participant:");
        Scanner scanner = new Scanner(System.in);

        String firstName = askInput(scanner,"First name?");
        String lastName = askInput(scanner,"Last name?");
        String phone = askInput(scanner,"Phone ?");
        String email = askInput(scanner,"Email ?");
        String clubName = askInput(scanner,"Club Name ?");
        ParticipantType type = requestParticipantType(scanner);

        controller.createParticipant(new ParticipantDTO(firstName,lastName,phone,email,clubName,type));
        displayUserChoices(context);
    }

    private ParticipantType requestParticipantType(Scanner scanner) {
        OutputUtils.sOutInfo(ParticipantType.values().toString());
        Optional<ParticipantType> type = null;

        do{
            int id = -1; //-1 because of the enum id = 0
            //TODO id handling to refactor, hardcoded
            id = askInt(scanner, "1. Attendee | 2. Trainer | 3. VIP");
            type = ParticipantType.getByOrdinal(id-1); //-1 for handling ids from 1 in input

        }while(!type.isPresent());

        return type.get();
    }
}
