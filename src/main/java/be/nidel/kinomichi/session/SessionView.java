package be.nidel.kinomichi.session;

import be.nidel.kinomichi.base.BaseView;
import be.nidel.kinomichi.gathering.Gathering;
import be.nidel.kinomichi.participant.ParticipantType;
import be.nidel.utils.OutputUtils;
import be.nidel.utils.inputprovider.InputProvider;
import be.nidel.utils.inputprovider.ScannerInput;
import be.nidel.utils.menu.MenuController;
import be.nidel.utils.menu.MenuFactory;
import be.technifutur.shared.Menu;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import static be.nidel.utils.InputUtils.*;

public class SessionView extends BaseView<SessionController> {
    private Gathering gathering;

    public SessionView(SessionController controller) {
        super(controller);
    }

    public void displayUserChoices(Menu context){
        this.context = context;
        current = MenuFactory.backQuitTemplate(context)
        .addItem("create new session", "c", this::gatherNewSessionData);
        current.renderAndInteract();
    }

    private void gatherNewSessionData() {
        OutputUtils.sOutInfo("Create a new session:");
        InputProvider scanner = new ScannerInput(new Scanner(System.in));

        OutputUtils.sOutInfo("A session needs the date & time");
        LocalDate date = askDate(scanner, "Day (dd/mm/yyyy)");
        LocalTime time = askTime(scanner, "From Time (hh:mm)");
        SessionType type = requestSessionType(scanner);

        controller.createSession(new SessionDTO(90, date,time, type), gathering);
        continueAddingSession();
    }
    private SessionType requestSessionType(InputProvider inputProvider) {
        OutputUtils.sOutInfo("What is the type of the session?");
        Optional<SessionType> type = null;

        do{
            int id = 0;
            String enumString = Arrays
                    .stream(SessionType.values())
                    .map(st -> "%s. %s\t\t".formatted(st.ordinal(), st.name() + st.emoji()))
                    .collect(Collectors.joining());
            id = askInt(inputProvider, enumString);
            type = SessionType.getByValue(id);
        }while(!type.isPresent());

        return type.get();
    }

    public void continueAddingSession(){
        MenuFactory.confirmTemplate(context, this::gatherNewSessionData)
                .setInteractionMessage("Continue adding sessions ? (y/n)")
                .renderAndInteract();
    }

    public void setGatheringData(Gathering gathering) {
        this.gathering = gathering;
    }

    public void showUnknownGatheringError() {
        OutputUtils.sOutWarning("Trying to add session to unknown gathering");
    }
}
