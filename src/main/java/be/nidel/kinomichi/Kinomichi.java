package be.nidel.kinomichi;

import be.nidel.kinomichi.gathering.*;
import be.nidel.kinomichi.participant.*;
import be.nidel.kinomichi.registration.RegistrationController;
import be.nidel.kinomichi.session.Session;
import be.nidel.kinomichi.session.SessionController;
import be.nidel.kinomichi.session.SessionDTO;
import be.nidel.utils.DateUtils;
import be.nidel.utils.OutputUtils;
import be.nidel.utils.RandomUtils;
import be.technifutur.shared.Menu;

import java.util.List;
import java.util.function.Consumer;

public class Kinomichi {

    //TODO use view & model
    GatheringController gatheringController;
    SessionController sessionController;
    ParticipantController participantController;
    RegistrationController registrationController;

    public Kinomichi() {
        initControllers();

        defaultData();
    }

    private void initControllers() {
        gatheringController = new GatheringController();
        sessionController = new SessionController();
        participantController = new ParticipantController();
        registrationController = new RegistrationController();

        //Link models for state sharing
        registrationController.setModels(
                participantController.getModel(),
                sessionController.getModel()
        );

        //Mapping events for decoupling
        gatheringController.onSessionRequest.connect(this::handleSessionRequest);
    }

    private void handleSessionRequest(GatheringPayload gatheringPayload) {
        sessionController.showManageMenu(gatheringPayload.context(), gatheringPayload.gathering());
    }

    public void launch() {
        displayWelcomePanel();
        displayMenu();
    }

    private static void displayWelcomePanel() {
        OutputUtils.sOutTitle("—————————————————————————————————————————————————————");
        OutputUtils.sOutTitle("           Kinomichi Administration Console          ");
        OutputUtils.sOutTitle("            Hello, how may I help you today?         ");
        OutputUtils.sOutTitle("                        ='.'=                        ");
        OutputUtils.sOutTitle("—————————————————————————————————————————————————————");
    }

    private void displayMenu() {
        int id = 1;
        Menu menu = new Menu();
        menu.addItem("Create a new gathering", String.valueOf(id++), () -> {
            gatheringController.showMenu(menu);
        });
        menu.addItem("Create a new participant", String.valueOf(id++), () -> {
            participantController.showMenu(menu);
        });
        menu.addItem("Handle subscriptions", String.valueOf(id++), () -> {
            registrationController.showMenu(menu);
        });
        menu.addItem("Quit", "q", this::quitApplication);
        menu.interact();
    }

    private void quitApplication() {
        OutputUtils.sOutBye();
    }


    public void defaultData() {
        //System.out.println("DEBUG populating data");

        participantController.createParticipant(new ParticipantDTO(
                "Johnny", "Lawrence", "johnny@cobrai.com", "0477000001", "Cobra Kai", ParticipantType.Sensei
        ));
        participantController.createParticipant(new ParticipantDTO(
                "Daniel", "LaRusso", "daniel@miyagido.com", "0477000002", "Miyagi-Do", ParticipantType.Sensei
        ));
        participantController.createParticipant(new ParticipantDTO(
                "Miguel", "Diaz", "miguel@cobrai.com", "0477000003", "Cobra Kai", ParticipantType.Attendee
        ));
        participantController.createParticipant(new ParticipantDTO(
                "Samantha", "LaRusso", "sam@miyagido.com", "0477000004", "Miyagi-Do", ParticipantType.Attendee
        ));
        participantController.createParticipant(new ParticipantDTO(
                "Robby", "Keene", "robby@miyagido.com", "0477000005", "Miyagi-Do", ParticipantType.Attendee
        ));
        participantController.createParticipant(new ParticipantDTO(
                "Chuck", "Norris", "chuck@roundhouse.com", "0477000010", "Roundhouse Dojo", ParticipantType.Sensei
        ));
        participantController.createParticipant(new ParticipantDTO(
                "Bruce", "Lee", "bruce@jeetkunedo.com", "0477000011", "Jeet Kune Do", ParticipantType.Sensei
        ));
        participantController.createParticipant(new ParticipantDTO(
                "Jean-Claude", "VanDamme", "jcvd@splitacademy.be", "0477000012", "Split Academy", ParticipantType.VIP
        ));
        participantController.createParticipant(new ParticipantDTO(
                "Steven", "Seagal", "steven@aikidovibes.com", "0477000013", "Aikido Vibes", ParticipantType.Trainer
        ));
        participantController.createParticipant(new ParticipantDTO(
                "Ip", "Man", "ip@wingchun.hk", "0477000014", "Wing Chun Institute", ParticipantType.Sensei
        ));
        participantController.createParticipant(new ParticipantDTO(
                "Nicolas", "Cage", "nicolas@specialforces.gov", "0477000015", "Special Forces MK", ParticipantType.Trainer
        ));

        Consumer<Gathering> daysCreator = (Gathering g) ->{
            sessionController.batchSessionCreation(
                    new SessionDTO(90,
                    DateUtils.StringDateToLocalDate("28/03/2026"),
                    DateUtils.StringTimeToLocalTime("9:30")),
                    g,
                    5);
            sessionController.batchSessionCreation(
                    new SessionDTO(90,
                    DateUtils.StringDateToLocalDate("29/03/2026"),
                    DateUtils.StringTimeToLocalTime("9:30")),
                    g,
                    3);
        };

        daysCreator.accept(gatheringController.createGathering(new GatheringDTO("Stage pour adolescents")));
        daysCreator.accept(gatheringController.createGathering(new GatheringDTO("Stage pour adultes")));


        List<Participant> participants = participantController.getAllParticipants();
        List<Session> sessions = sessionController.getAllSessions();
        for (int i = 0; i <20; i++) {
            Participant participant = participants.get(RandomUtils.getRandomInt(0, participants.size()));
            Session session = sessions.get(RandomUtils.getRandomInt(0, sessions.size()));
            session.addAttendee(participant);
        }

        List<Participant> trainers = participants.stream()
                .filter((Participant p) ->
                        p.getParticipantType() == ParticipantType.Trainer)
                .toList();
        for (Session session : sessions) {
            Participant trainer = trainers.get(RandomUtils.getRandomInt(0, trainers.size()));
            session.setTrainer(trainer);
        }

        //System.out.println(gatheringController.getAllGatherings());
        //System.out.println("DEBUG populating data -- COMPLETED");
    }

}
