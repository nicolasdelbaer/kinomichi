package be.nidel.kinomichi.participant;
import be.nidel.kinomichi.base.BaseView;
import be.nidel.utils.menu.MenuController;
import be.nidel.utils.menu.MenuFactory;
import be.nidel.utils.OutputUtils;
import be.technifutur.shared.Menu;

import java.util.*;
import java.util.stream.Collectors;

import static be.nidel.utils.InputUtils.askInput;
import static be.nidel.utils.InputUtils.askInt;

public class ParticipantView extends BaseView<ParticipantController> {

    public ParticipantView(ParticipantController controller) {
        super(controller);
    }

    public void displayUserChoices(Menu context){
        this.context = context;
        MenuFactory.backQuitTemplate(context)
        .addItem("list new participant", "l", this::listParticipantRequest)
        .addItem("create new participant", "c", this::createParticipantRequest)
        .addItem("update new participant", "u", this::updateParticipantRequest)
        .addItem("delete new participant", "d", this::archiveParticipantRequest)
        .renderAndInteract();
    }

    private void listParticipantRequest() {
        controller.getAllParticipants().forEach(
                participant ->
                OutputUtils.sOutInfo(("%s. %s - %s; %s - %s, %s").formatted(
                        participant.getId(),
                        participant.getFullName(),
                        participant.getEmail(),
                        participant.getPhone(),
                        participant.getClubName(),
                        participant.getParticipantType().name()
        )));
        displayUserChoices(context);
    }

    private void updateParticipantRequest() {
        OutputUtils.sOutInfo("Updating a participant:");
        Integer participantId = askInt(
                new Scanner(System.in),
                "Enter the participant id"
        );
        ParticipantDTO dto = updateParticipantData(controller.getParticipantById(participantId));
        controller.updateParticipant(participantId, dto);
        displayUserChoices(context);
    }

    private void archiveParticipantRequest() {

    }

    private void createParticipantRequest() {
        ParticipantDTO dto = gatherParticipantData();
        controller.createParticipant(dto);
        displayUserChoices(context);
    }

    private ParticipantDTO gatherParticipantData() {
        OutputUtils.sOutInfo("Create a new participant:");
        Scanner scanner = new Scanner(System.in);
        String firstName = askInput(scanner,"First name?");
        String lastName = askInput(scanner,"Last name?");
        String phone = askInput(scanner,"Phone?");
        String email = askInput(scanner,"Email?");
        String clubName = askInput(scanner,"Club Name?");
        ParticipantType type = requestParticipantType(scanner);

        return new ParticipantDTO(firstName,lastName,phone,email,clubName,type);
    }

    private ParticipantDTO updateParticipantData(Participant source) {
        return new ParticipantDTO("","","","","",ParticipantType.Attendee);
    }

    private ParticipantType requestParticipantType(Scanner scanner) {
        OutputUtils.sOutInfo(ParticipantType.values().toString());
        Optional<ParticipantType> type = null;

        do{
            int id = 0;
            String enumString = Arrays
                    .stream(ParticipantType.values())
                    .map(st -> "%s. %s\t\t".formatted(st.ordinal(), st.name()+st.emoji()))
                    .collect(Collectors.joining());
            id = askInt(scanner, enumString);
            type = ParticipantType.getByValue(id);
        }while(!type.isPresent());

        return type.get();
    }

}
