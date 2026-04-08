package be.nidel.kinomichi;

import be.nidel.kinomichi.gathering.*;
import be.nidel.kinomichi.participant.*;
import be.nidel.kinomichi.pricing.PricingDTO;
import be.nidel.kinomichi.pricing.PricingGroupDTO;
import be.nidel.kinomichi.registration.RegistrationController;
import be.nidel.kinomichi.registration.RegistrationDTO;
import be.nidel.kinomichi.registration.RegistrationStatus;
import be.nidel.kinomichi.reporting.ReportingController;
import be.nidel.kinomichi.session.Session;
import be.nidel.kinomichi.session.SessionController;
import be.nidel.kinomichi.session.CreateSessionDTO;
import be.nidel.kinomichi.session.SessionType;
import be.nidel.utils.DateUtils;
import be.nidel.utils.OutputUtils;
import be.nidel.utils.RandomUtils;
import be.technifutur.shared.Menu;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class Kinomichi {

    public static final String SAVE_FILENAME = "./saves/kinomichi.sav";
    private final Logger logger = Logger.getLogger("Kinomichi");

    GatheringController gatheringController;
    SessionController sessionController;
    ParticipantController participantController;
    RegistrationController registrationController;
    ReportingController reportingController;

    public Kinomichi() {
        //kinomichi.populateWithFakeData();
        createControllers(); //controllers need to be setup to load data
        loadData(); //load saved file
        initControllers(); // share models with controllers
    }

    public void loadData() {
        // Deserialization
        try {
            FileInputStream file = new FileInputStream(SAVE_FILENAME);
            ObjectInputStream in = new ObjectInputStream(file);

            participantController.loadData(in.readObject());
            registrationController.loadData(in.readObject());
            sessionController.loadData(in.readObject());
            gatheringController.loadData(in.readObject());

            in.close();
            file.close();
            System.out.println("Data has been loaded");

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void createControllers() {
        gatheringController = new GatheringController();
        sessionController = new SessionController();
        participantController = new ParticipantController();
        registrationController = new RegistrationController();
        reportingController = new ReportingController();
    }

    private void initControllers() {
        //Link models for state sharing
        sessionController.setParticipantModel(participantController.getModel());
        registrationController.setModels(
                participantController.getModel(),
                sessionController.getModel()
        );
        reportingController.setGatheringModel(gatheringController.getModel());
        reportingController.setRegistrationModel(registrationController.getModel());
        reportingController.setParticipantModel(participantController.getModel());
        reportingController.setSessionModel(sessionController.getModel());

        //Mapping events for decoupling
        gatheringController.onSessionRequest.connect(this::handleSessionRequest);
    }

    private void handleSessionRequest(GatheringPayload gatheringPayload) {
        sessionController.showManageMenu(gatheringPayload.context(), gatheringPayload.gathering());
    }

    public void launch() {
        displayWelcomePanel();
        displayMenu();
        handleShutdown();
    }

    private void handleShutdown() {
        // Serialization
        try {
            FileOutputStream file = new FileOutputStream(SAVE_FILENAME);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(participantController.getSaveable());
            out.writeObject(registrationController.getSaveable());
            out.writeObject(sessionController.getSaveable());
            out.writeObject(gatheringController.getSaveable());
            out.close();
            file.close();
            System.out.println("Data has been saved");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        menu.addItem("Manage Gatherings", String.valueOf(id++), () -> {
            gatheringController.showMenu(menu);
        });
        menu.addItem("Manage Participant", String.valueOf(id++), () -> {
            participantController.showMenu(menu);
        });
        menu.addItem("Handle Registrations", String.valueOf(id++), () -> {
            registrationController.showMenu(menu);
        });
        menu.addItem("Reporting", String.valueOf(id++), () -> {
            reportingController.showMenu(menu);
        });
        menu.addHiddenItem("Quit", "q", this::quitApplication);
        menu.setPostRender(OutputUtils.STYLISABLE_LINE.formatted(OutputUtils.ANSI_BLACK_BACKGROUND, "\"q\": Quit", OutputUtils.ANSI_RESET));
        menu.renderAndInteract();
    }

    private void quitApplication() {
        OutputUtils.sOutBye();
    }


    public void populateWithFakeData() {
        registrationController.silenceView(true);
        logger.fine("DEBUG populating data");

        //PARTICIPANTS CREATION
        participantController.createParticipant(new ParticipantDTO(
                "Johnny", "Lawrence", "0477000001", "johnny@cobrai.com", "Cobra Kai", ParticipantType.Sensei
        ));
        participantController.createParticipant(new ParticipantDTO(
                "Daniel", "LaRusso", "0477000002", "daniel@miyagido.com", "Miyagi-Do", ParticipantType.Sensei
        ));
        participantController.createParticipant(new ParticipantDTO(
                "Miguel", "Diaz", "0477000003", "miguel@cobrai.com", "Cobra Kai", ParticipantType.Attendee
        ));
        participantController.createParticipant(new ParticipantDTO(
                "Samantha", "LaRusso", "0477000004", "sam@miyagido.com", "Miyagi-Do", ParticipantType.Attendee
        ));
        participantController.createParticipant(new ParticipantDTO(
                "Robby", "Keene", "0477000005", "robby@miyagido.com", "Miyagi-Do", ParticipantType.Attendee
        ));
        participantController.createParticipant(new ParticipantDTO(
                "Chuck", "Norris", "0477000010", "chuck@roundhouse.com", "Roundhouse Dojo", ParticipantType.Sensei
        ));
        participantController.createParticipant(new ParticipantDTO(
                "Bruce", "Lee", "0477000011", "bruce@jeetkunedo.com", "Jeet Kune Do", ParticipantType.Sensei
        ));
        participantController.createParticipant(new ParticipantDTO(
                "Jean-Claude", "VanDamme", "0477000012", "jcvd@splitacademy.be", "Split Academy", ParticipantType.VIP
        ));
        participantController.createParticipant(new ParticipantDTO(
                "Steven", "Seagal", "0477000013", "steven@aikidovibes.com", "Aikido Vibes", ParticipantType.Trainer
        ));
        participantController.createParticipant(new ParticipantDTO(
                "Ip", "Man", "0477000014", "ip@wingchun.hk", "Wing Chun Institute", ParticipantType.Sensei
        ));
        participantController.createParticipant(new ParticipantDTO(
                "Nicolas", "Cage", "0477000015", "nicolas@specialforces.gov", "Special Forces MK", ParticipantType.Trainer
        ));

        //SESSIONS CREATION
        Consumer<Gathering> daysCreator = (Gathering g) ->{
            sessionController.batchSessionCreation(
                    new CreateSessionDTO(90,
                    "Default Title","Description",
                    DateUtils.StringDateToLocalDate("28/03/2026"),
                    DateUtils.StringTimeToLocalTime("9:30"),
                            SessionType.Exhibition),
                    g,
                    5);
            sessionController.batchSessionCreation(
                    new CreateSessionDTO(90,
                            "Default Title","Description",
                    DateUtils.StringDateToLocalDate("29/03/2026"),
                    DateUtils.StringTimeToLocalTime("9:30"),
                            SessionType.Exhibition),
                    g,
                    3);
            sessionController.createSession(new CreateSessionDTO(360,
                    "Default Title","Description",
                    DateUtils.StringDateToLocalDate("28/03/2026"),
                    DateUtils.StringTimeToLocalTime("18:00"),
                    SessionType.Dinner), g);
            sessionController.createSession(new CreateSessionDTO(720,
                    "Default Title","Description",
                    DateUtils.StringDateToLocalDate("28/03/2026"),
                    DateUtils.StringTimeToLocalTime("22:00"),
                    SessionType.Accommodation),g);
        };


        //PRICING CREATION
        List<PricingGroupDTO> pricingList = List.of(
                new PricingGroupDTO(
            List.of(
                                new PricingDTO(ParticipantType.Attendee, SessionType.Accommodation, new BigDecimal("60.00")),
                                new PricingDTO(ParticipantType.Trainer, SessionType.Accommodation, new BigDecimal("60.00")),
                                new PricingDTO(ParticipantType.Sensei, SessionType.Accommodation, new BigDecimal("60.00")),
                                new PricingDTO(ParticipantType.VIP, SessionType.Accommodation, new BigDecimal("40.00"))
                        ),SessionType.Accommodation
                ),
                new PricingGroupDTO(
                        List.of(
                                new PricingDTO(ParticipantType.Attendee, SessionType.Exhibition, new BigDecimal("10.00")),
                                new PricingDTO(ParticipantType.Trainer, SessionType.Exhibition, new BigDecimal("0.00")),
                                new PricingDTO(ParticipantType.Sensei, SessionType.Exhibition, new BigDecimal("10.00")),
                                new PricingDTO(ParticipantType.VIP, SessionType.Exhibition, new BigDecimal("8.00"))
                        ),SessionType.Exhibition
                ),
                new PricingGroupDTO(
                        List.of(
                            new PricingDTO(ParticipantType.Attendee, SessionType.Dinner, new BigDecimal("15.00")),
                            new PricingDTO(ParticipantType.Trainer, SessionType.Dinner, new BigDecimal("10.00")),
                                new PricingDTO(ParticipantType.Sensei, SessionType.Dinner, new BigDecimal("15.00")),
                                new PricingDTO(ParticipantType.VIP, SessionType.Dinner, new BigDecimal("10.00"))
                        ),SessionType.Dinner
                )
        );

        //GATHERINGS CREATION
        daysCreator.accept(gatheringController.createGathering(new CreateGatheringDTO("Stage pour adolescents",pricingList)));
        daysCreator.accept(gatheringController.createGathering(new CreateGatheringDTO("Stage pour adultes", pricingList)));

        //REGISTRATION RANDOM CREATION
        List<Participant> participants = participantController.getAllParticipants();
        List<Session> sessions = sessionController.getAllSessions();
        for (int i = 0; i <40; i++) {
            RegistrationDTO dto = new RegistrationDTO(
                    RegistrationStatus.getByOrdinal(RandomUtils.getRandomInt(0, RegistrationStatus.values().length)).get(),
                    RandomUtils.getRandomInt(1, participants.size()+1),
                    RandomUtils.getRandomInt(1, sessions.size()+1),
                    RandomUtils.getRandomInt(1, pricingList.size()+1)
            );
            registrationController.createRegistration(dto);
        }

        //SETTING TRAINERS
        List<Participant> trainers = participants.stream()
                .filter((Participant p) ->
                        p.getParticipantType() == ParticipantType.Trainer)
                .toList();
        for (Session session : sessions) {
            Participant trainer = trainers.get(RandomUtils.getRandomInt(0, trainers.size()));
            session.setOrganizer(trainer);
        }
        //System.out.println(gatheringController.getAllGatherings().toString());
        logger.fine("DEBUG populating data -- COMPLETED");
        registrationController.silenceView(false);
    }

}
