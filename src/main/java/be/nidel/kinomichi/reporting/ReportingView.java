package be.nidel.kinomichi.reporting;

import be.nidel.kinomichi.base.BaseView;
import be.nidel.kinomichi.gathering.Gathering;
import be.nidel.kinomichi.participant.Participant;
import be.nidel.kinomichi.participant.ParticipantType;
import be.nidel.kinomichi.pricing.Pricing;
import be.nidel.kinomichi.registration.Registration;
import be.nidel.kinomichi.registration.RegistrationStatus;
import be.nidel.kinomichi.session.Session;
import be.nidel.kinomichi.session.SessionType;
import be.nidel.utils.FormatUtils;
import be.nidel.utils.OutputUtils;
import be.nidel.utils.menu.MenuFactory;
import be.technifutur.shared.Menu;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static be.nidel.utils.InputUtils.askInt;

public class ReportingView extends BaseView<ReportingController> {

    public ReportingView(ReportingController controller) {
        super(controller);
    }

    public void displayUserChoices(Menu context){
        this.context = context;

        int inputId = 1;
        this.current = MenuFactory.backQuitTemplate(context)
                .addItem("Gathering overview", String.valueOf(inputId++), this::showGatheringOverview)
                .addItem("Participant reporting", String.valueOf(inputId++), this::showParticipantStatus)
                .addItem("Gathering status", String.valueOf(inputId++), this::showGatheringStatus)
                .addItem("Accounts reporting", String.valueOf(inputId++), this::showReceivableReporting)
        ;
        this.current.interact();
    }

    private void showGatheringOverview() {
        Scanner scanner = new Scanner(System.in);
        int gatheringId = askInt(scanner, "Please insert gathering id.");
        controller.gatheringOverview(gatheringId);
        displayUserChoices(context);
    }

    //TODO liste sessions et statut
    //TODO payé / à payer / Projection total
    //TODO ristourne (opt.)
    private void showParticipantStatus() {
        displayUserChoices(context);
    }
    private void showReceivableReporting() {
        List<Participant> participants = controller.getUnpaidParticipants();
        Map<Integer, Gathering> gatherings = controller.getGatheringMap();
        Map<Integer, Session> sessions = controller.getSessionMap();

        OutputUtils.sOutTitle(OutputUtils.DEFAULT_LINE.formatted("THEY MUST PAY!"));
        OutputUtils.sOutTitle("%-25s %-13s %-13s".formatted("Attendees", "Sessions", "Reservations"));
        for (Participant participant : participants) {
            List<Registration> unpaidRegistrations = controller.getUnpaidRegistrationByParticipant(participant);

            List<BigDecimal> unpaidTotal = unpaidRegistrations.stream()
                    .collect(
                            Collectors.teeing(
                                Collectors.mapping(registration -> {
                                Session session = sessions.get(registration.getSessionId());
                                Gathering gathering = gatherings.get(session.getGatheringId());

                                BigDecimal price = BigDecimal.ZERO;
                                if(session.getSessionType() == SessionType.Exhibition){
                                    Pricing pricing = gathering.getPriceFor(
                                            participant.getParticipantType(),
                                            session.getSessionType());
                                    price = pricing.getPrice();
                                }
                                return price;
                            }, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)),
                            Collectors.mapping(registration -> {
                                Session session = sessions.get(registration.getSessionId());
                                Gathering gathering = gatherings.get(session.getGatheringId());

                                BigDecimal price = BigDecimal.ZERO;
                                if(session.getSessionType() != SessionType.Exhibition){
                                    Pricing pricing = gathering.getPriceFor(
                                            participant.getParticipantType(),
                                            session.getSessionType());
                                    price = pricing.getPrice();
                                }
                                return price;
                            }, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)),
                    List::of)
                    );

            OutputUtils.sOut("%s%-25s%s %s%-13s %-13s%s".formatted(
                OutputUtils.ANSI_WHITE,
                FormatUtils.truncate(participant.getFullName(), 25),
                OutputUtils.ANSI_RESET,
                OutputUtils.ANSI_RED,
                unpaidTotal.get(0),
                unpaidTotal.get(1),
                OutputUtils.ANSI_RESET
                ));
        }
        displayUserChoices(context);
    }

    //TODO (total inscriptions, annulations, ...)
    //TODO projections paiements
    //TODO nb de logements validés / payés
    private void showGatheringStatus() {
        Scanner scanner = new Scanner(System.in);
        int gatheringId = askInt(scanner, "Please insert gathering id.");
        controller.gatheringReporting(gatheringId);
        displayUserChoices(context);
    }

    public void renderReport(Gathering gathering) {
        renderGatheringInfo(gathering);
        for (Session session : gathering.getAllSessions())
            renderSession(session);
        renderPrices(gathering.getPriceList());
    }

    private void renderGatheringInfo(Gathering gathering) {
        OutputUtils.sOut(OutputUtils.STYLISABLE_LINE.formatted(
                OutputUtils.ANSI_YELLOW_BACKGROUND + OutputUtils.ANSI_BLACK_BOLD,
                "- Title: "+gathering.getTitle(),
                OutputUtils.ANSI_RESET
        ));
    }

    private void renderSession(Session session) {
        String orgaName = session.getOrganizer().map(Participant::getFullName).orElse(OutputUtils.ANSI_YELLOW+ "n/a"+OutputUtils.ANSI_PURPLE);
        OutputUtils.sOut(OutputUtils.STYLISABLE_LINE.formatted(
                OutputUtils.ANSI_PURPLE,
                session.getSessionType().emoji() + " Organizer: "+ orgaName + " - "+ session.getSessionType().name(),
                OutputUtils.ANSI_RESET));
        OutputUtils.sOut(OutputUtils.STYLISABLE_LINE.formatted(
                OutputUtils.ANSI_BLUE,
                "Date: " +session.getDay()+ " | "+ session.getStart()+ " -> "+ session.getEnd(),
                OutputUtils.ANSI_RESET));

        List<Participant> sessionAttendees = session.getAttendees();
        Map<Integer, Registration> registrationsByParticipant = controller.getRegistrationBySession(session).stream()
                .filter(r -> r.getSessionId() == session.getId())
                .collect(Collectors.toMap(
                        Registration::getParticipantId, Function.identity()));

        //ORDER BY STATUS
        Map<RegistrationStatus, List<Participant>> attendeesBySessionStatus = sessionAttendees.stream()
                .collect(Collectors.groupingBy(
                        p -> registrationsByParticipant.get(p.getId()).getStatus(),
                        Collectors.toList()));

        //DISPLAY ATTENDEE LIST
        attendeesBySessionStatus.forEach((key, value) -> value.forEach(attendee -> {
            String attendeeInfo = "\t%s%s%s".formatted(
                    key.name(),
                    " - ",
                    "%s (id: %s)".formatted(attendee.getFullName(), attendee.getId())
            );

            switch (key) {
                case UNPAID ->
                        OutputUtils.sOut(OutputUtils.STYLISABLE_LINE.formatted(OutputUtils.ANSI_RED, attendeeInfo, OutputUtils.ANSI_RESET));
                case PAID ->
                        OutputUtils.sOut(OutputUtils.STYLISABLE_LINE.formatted(OutputUtils.ANSI_GREEN, attendeeInfo, OutputUtils.ANSI_RESET));
                case CANCELLED, WITHDRAWN ->
                        OutputUtils.sOut(OutputUtils.STYLISABLE_LINE.formatted(OutputUtils.ANSI_WHITE_ITALIC, attendeeInfo, OutputUtils.ANSI_RESET));
                default -> OutputUtils.sOut(OutputUtils.DEFAULT_LINE.formatted(attendeeInfo));
            }
        }));
        System.out.println("");
    }

    private void renderPrices(List<Pricing> priceList) {
        OutputUtils.sOut(OutputUtils.STYLISABLE_LINE.formatted(
                OutputUtils.ANSI_YELLOW_BACKGROUND + OutputUtils.ANSI_BLACK_BOLD,
                "- Prices Table",
                OutputUtils.ANSI_RESET
        ));

        Map<ParticipantType, List<Pricing>> pricesByParticipantType = priceList.stream()
                .collect(Collectors.groupingBy(Pricing::getParticipantType));

        List<String> enumString = Arrays
                .stream(SessionType.values())
                .map(e -> e.name()).collect(Collectors.toList());
        enumString.addFirst("$$$");

        String format = "%-11s" + "%-14s".repeat(enumString.size()-1);

        OutputUtils.sOut(OutputUtils.STYLISABLE_LINE.formatted(
                OutputUtils.ANSI_WHITE_BOLD,
                format.formatted(enumString.toArray()),
                OutputUtils.ANSI_RESET
        ));

        pricesByParticipantType.forEach((key, values) -> {
            List<String> priceRow = new ArrayList<>();
            priceRow.add(key.name());
            values.forEach(pricing -> {
                priceRow.add(FormatUtils.formatPrice(pricing.getPrice()));
            });

            OutputUtils.sOut(OutputUtils.DEFAULT_LINE.formatted(
                    format.formatted(priceRow.toArray())
            ));
        });

    }



    //region Error handling
    public void showInvalidIdError(int gatheringId) {
        OutputUtils.sOutError("Gathering id: %s doesn't exist.".formatted(gatheringId));
    }

    //endregion
}
