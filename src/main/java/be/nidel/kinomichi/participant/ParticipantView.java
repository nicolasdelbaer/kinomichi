package be.nidel.kinomichi.participant;
import be.nidel.kinomichi.base.BaseView;
import be.nidel.utils.inputprovider.InputProvider;
import be.nidel.utils.inputprovider.ScannerInput;
import be.nidel.utils.inputprovider.StaticInput;
import be.nidel.utils.menu.MenuFactory;
import be.nidel.utils.OutputUtils;
import be.technifutur.shared.Menu;

import java.util.*;
import java.util.stream.Collectors;

import static be.nidel.utils.InputUtils.*;

public class ParticipantView extends BaseView<ParticipantController> {

    public ParticipantView(ParticipantController controller) {
        super(controller);
    }

    public void displayUserChoices(Menu context){
        this.context = context;
        MenuFactory.backQuitTemplate(context)
        .addItem("list participants", "l", this::listParticipantRequest)
        .addItem("create new participant", "c", this::createParticipantRequest)
        .addItem("update participant (id)", "u", this::updateParticipantRequest)
        .addItem("delete participant (id)", "d", this::archiveParticipantRequest)
        .renderAndInteract();
    }

    private void listParticipantRequest() {
        controller.getAllParticipants().forEach(
                participant ->
                OutputUtils.sOutInfo(("%s. %s - %s; %s - %s, %s").formatted(
                        participant.getId(),
                        participant.getFullName(),
                        participant.getPhone(),
                        participant.getEmail(),
                        participant.getClubName(),
                        participant.getParticipantType().name()
        )));
        System.out.println();
        displayUserChoices(context);
    }

    private void updateParticipantRequest() {
        OutputUtils.sOutInfo("Updating a participant:");
        int participantId = askInt(
                new ScannerInput(new Scanner(System.in)),
                "Enter the participant id"
        );
        Optional<Participant> tmpParticipant = controller.getParticipantById(participantId);
        if(tmpParticipant.isPresent()){
            ParticipantDTO dto = gatherUpdateParticipantData(tmpParticipant.get());
            Participant participant = controller.updateParticipant(participantId, dto);
            OutputUtils.sOutInfo(("%s. %s - %s; %s - %s, %s").formatted(
                participant.getId(),
                participant.getFullName(),
                participant.getPhone(),
                participant.getEmail(),
                participant.getClubName(),
                participant.getParticipantType().name()
            ));
            System.out.println();
        }else{
            showUpdateErrorFeedback();
        }
        displayUserChoices(context);
    }

    private void archiveParticipantRequest() {
        OutputUtils.sOutInfo("Archive a participant:");
        int participantId = askInt(
                new ScannerInput(new Scanner(System.in)),
                "Enter the participant id"
        );
        controller.archiveParticipant(participantId);
        displayUserChoices(context);
    }

    private void createParticipantRequest() {
        ParticipantDTO dto = gatherParticipantData();
        controller.createParticipant(dto);
        displayUserChoices(context);
    }

    private ParticipantDTO gatherParticipantData() {
        OutputUtils.sOutInfo("Create a new participant:");
        InputProvider scanner = new ScannerInput(new Scanner(System.in));
        String firstName = askInput(scanner,"First name?");
        String lastName = askInput(scanner,"Last name?");
        String phone = askInput(scanner,"Phone?");
        String email = askInput(scanner,"Email?");
        String clubName = askInput(scanner,"Club Name?");
        ParticipantType type = requestParticipantType(scanner);

        return new ParticipantDTO(firstName,lastName,phone,email,clubName,type);
    }

    private ParticipantDTO gatherUpdateParticipantData(Participant source) {
        //Context is used for lambdas that need a final property
        class Context {
            String firstName = source.getFirstName();
            String lastName = source.getLastName();
            String phone = source.getPhone();
            String email = source.getEmail();
            String clubName = source.getClubName();
            ParticipantType type = source.getParticipantType();
        };
        Context ctx = new Context();

        //Injection of a static provider, it'll get the first input and mock a inputProvider (scanner)
        //to inject the user input if valid. It skips one step of input management
        askForEditOrSource(context, (provider) -> {
            ctx.firstName = askInput(new StaticInput(provider), "First name?");
        }, "First name", source.getFirstName());
        askForEditOrSource(context, (provider) -> {
            ctx.lastName = askInput(new StaticInput(provider), "Last name?");
        }, "Last name",source.getLastName());
        askForEditOrSource(context, (provider) -> {
            ctx.phone = askInput(new StaticInput(provider), "Phone?");
        }, "Phone",source.getPhone());
        askForEditOrSource(context, (provider) -> {
            ctx.email = askInput(new StaticInput(provider), "Email?");
        }, "Email",source.getEmail());
        askForEditOrSource(context, (provider) -> {
            ctx.clubName = askInput(new StaticInput(provider), "Club Name?");
        }, "Club name",source.getClubName());

        askForEditOrSource(context, (provider) ->
                ctx.type = requestParticipantType(new StaticInput(provider)),
                "Participant Type","%s. %s".formatted(source.getParticipantType().ordinal(), source.getParticipantType().name())
        );
        return new ParticipantDTO(ctx.firstName, ctx.lastName, ctx.phone, ctx.email, ctx.clubName, ctx.type);
    }


    private ParticipantType requestParticipantType(InputProvider scanner) {
        OutputUtils.sOutInfo("Pick a participant type");
        Optional<ParticipantType> type = null;

        do{
            int id = 0;
            String enumString = Arrays
                    .stream(ParticipantType.values())
                    .map(st -> "%s. %s %s\t".formatted(st.ordinal(), st.name(), st.emoji()))
                    .collect(Collectors.joining());
            id = askInt(scanner, enumString);
            type = ParticipantType.getByValue(id);
        }while(!type.isPresent());

        return type.get();
    }

    public void showArchivedFeedback(Participant participant){
        OutputUtils.sOutWarning("%s. %s %s".formatted(
                participant.getId(),
                participant.getFullName(),
                "has been archived"
        ));
    }

    public void showArchivedErrorFeedback() {
        OutputUtils.sOutWarning("Cannot execute deletion, bad participant id?");
    }
    public void showUpdateErrorFeedback() {
        OutputUtils.sOutWarning("Cannot execute update, bad participant id?");
    }
}
