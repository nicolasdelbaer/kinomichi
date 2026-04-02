package be.nidel.kinomichi.participant;
import be.nidel.kinomichi.base.BaseController;
import be.nidel.kinomichi.base.BaseView;
import be.nidel.kinomichi.session.SessionType;
import be.nidel.utils.menu.MenuFactory;
import be.nidel.utils.OutputUtils;
import be.technifutur.shared.Menu;

import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
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
        .addItem("create new participant", "c", this::gatherParticipantData)
        .renderAndInteract();
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
