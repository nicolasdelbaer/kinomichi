package be.nidel.kinomichi.session;

import be.nidel.kinomichi.base.BaseView;
import be.nidel.kinomichi.gathering.Gathering;
import be.nidel.kinomichi.gathering.renderer.GatheringSessionRenderer;
import be.nidel.kinomichi.participant.Participant;
import be.nidel.utils.OutputUtils;
import be.nidel.utils.inputprovider.InputProvider;
import be.nidel.utils.inputprovider.ScannerInput;
import be.nidel.utils.inputprovider.StaticInput;
import be.nidel.utils.menu.MenuFactory;
import be.technifutur.shared.Menu;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static be.nidel.utils.InputUtils.*;

public class SessionView extends BaseView<SessionController> {
    private Gathering gathering;

    public SessionView(SessionController controller) {
        super(controller);
    }

    public void displayUserChoices(Menu context){
        displayUserChoices(context, false);
    }
    public void displayUserChoices(Menu context, boolean showList){
        this.menuContext = context;

        if(showList)
            listSessions();

        current = MenuFactory.backQuitTemplate(context)
            .addItem("list sessions", "l", this::listSessionsRequest)
            .addItem("create new session", "c", this::createSessionRequest)
            .addItem("update session (id)", "u", this::updateSessionRequest)
            .addItem("archive session (id)", "d", this::archiveSession)
        ;
        current.renderAndInteract();
    }

    private void listSessionsRequest() {
        listSessions();
        displayUserChoices(menuContext);
    }

    private void listSessions() {
        GatheringSessionRenderer gatheringSessionRenderer = new GatheringSessionRenderer();
        OutputUtils.sOutTitle(GatheringSessionRenderer.FORMAT.formatted(
                "ID. ", "TITLE", "TYPE", "ORGANIZER", "DATE", "START", "END"));
        List<Session> allSessions = gathering.getAllSessions();
        if(allSessions.isEmpty()){
            OutputUtils.sOutWarning("No sessions");
        }else{
            for (Session session : allSessions) {
                gatheringSessionRenderer.render(session);
            }
        }
        System.out.println();
    }

    private void createSessionRequest() {
        OutputUtils.sOutInfo("Creating a new session:");
        CreateSessionDTO createSessionDTO = gatherSessionData();
        controller.createSession(createSessionDTO, gathering);
        continueAddingSession();
    }

    private void updateSessionRequest() {
        OutputUtils.sOutInfo("Updating a session:");
        int sessionId = askInt(
                new ScannerInput(new Scanner(System.in)),
                "Enter the session id"
        );
        Optional<Session> tmpSession = controller.getSessionById(sessionId);
        if(tmpSession.isPresent())
        {
            UpdateSessionDTO updateSessionDTO = gatherUpdateSessionData(tmpSession.get());
            controller.updateSession(updateSessionDTO);
            displayUserChoices(menuContext);
        }
    }

    private UpdateSessionDTO gatherUpdateSessionData(Session source) {
        //Context is used for lambdas that needs a final property
        class Context {
            String title = source.getTitle();
            String description = source.getDescription();
            Participant organizer = source.getOrganizer().orElse(null);
            SessionType type = source.getSessionType();
            LocalDate day = source.getDay();
            LocalTime start = source.getStart();
            int duration = source.getDuration();
        };
        Context ctx = new Context();

        String organizerName = "n/a";
        if(Objects.nonNull(ctx.organizer))
            organizerName = "(id: %s) %s".formatted(ctx.organizer.getId(), ctx.organizer.getFullName());

        askForEditOrSource(menuContext, (provider) -> {
            int participantId = askInt(new StaticInput(provider), "Organizer (id)");
            ctx.organizer = controller.getParticipantById(participantId).orElse(null);
        }, "Organizer", organizerName);
        askForEditOrSource(menuContext, (provider) -> {
            ctx.title = askInput(new StaticInput(provider), "Title");
        }, "Title", ctx.title);
        askForEditOrSource(menuContext, (provider) -> {
            ctx.description = askInput(new StaticInput(provider), "Description");
        }, "Description", ctx.description);
        askForEditOrSource(menuContext, (provider) -> {
            ctx.day = askDate(new StaticInput(provider), "Day (dd/mm/yyyy)");
        }, "Day (dd/mm/yyyy)", ctx.day.toString());
        askForEditOrSource(menuContext, (provider) -> {
            ctx.start = askTime(new StaticInput(provider), "From Time (hh:mm)");
            }, "From Time (hh:mm)", ctx.start.toString());
        askForEditOrSource(menuContext, (provider) -> {
            ctx.duration = askInt(new StaticInput(provider), "Duration (minutes)");
            }, "Duration (minutes)", String.valueOf(ctx.duration));

        return new UpdateSessionDTO(source.getId(),ctx.organizer, ctx.title, ctx.description, ctx.type, ctx.day, ctx.start, ctx.duration);
    }

    private CreateSessionDTO gatherSessionData() {

        class Context{
            int duration = 90;
            Participant organizer = null;
        }
        Context ctx = new Context();


        InputProvider scanner = new ScannerInput(new Scanner(System.in));
        String title = askInput(scanner, "Title");
        String description = askInput(scanner, "Description");

        askForEditOrSource(menuContext, (provider) -> {
            int participantId = askInt(new StaticInput(provider), "Organizer (id)");
            ctx.organizer = controller.getParticipantById(participantId).orElse(null);
        }, "Organizer", "n/a");

        OutputUtils.sOutInfo("A session needs the date & time + duration");
        LocalDate date = askDate(scanner, "Day (dd/mm/yyyy)");
        LocalTime time = askTime(scanner, "From Time (hh:mm)");


        askForEditOrSource(menuContext, (provider) -> {
               ctx.duration = askInt(new StaticInput(provider), "");
        }, "Durations (minutes)", "90");
        int duration = ctx.duration;

        SessionType type = requestSessionType(scanner);
        return new CreateSessionDTO(duration, title, description,date,time, type);
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
        MenuFactory.confirmTemplate(menuContext, this::gatherSessionData)
                .setInteractionMessage("Continue adding sessions ? (y/n)")
                .renderAndInteract();
    }
    private void archiveSession() {
        OutputUtils.sOutInfo("Archive a gathering:");
        int sessionId = askInt(
                new ScannerInput(new Scanner(System.in)),
                "Enter the session id"
        );
        controller.archiveSession(sessionId);
        displayUserChoices(menuContext);
    }

    public void setGatheringData(Gathering gathering) {
        this.gathering = gathering;
    }

    public void showUnknownGatheringError() {
        OutputUtils.sOutWarning("Trying to add session to unknown gathering");
    }
    public void showArchivedFeedback(Session session){
        OutputUtils.sOutWarning("%s. %s %s".formatted(
                session.getId(),
                session.toString(),
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
