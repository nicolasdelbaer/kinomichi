package be.nidel.kinomichi.reporting;

import be.nidel.kinomichi.base.BaseView;
import be.nidel.kinomichi.gathering.Gathering;
import be.nidel.kinomichi.participant.Participant;
import be.nidel.kinomichi.participant.ParticipantType;
import be.nidel.kinomichi.pricing.Pricing;
import be.nidel.kinomichi.registration.Registration;
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
                .addItem("Participant reporting", String.valueOf(inputId++), this::showParticipantReporting)
                .addItem("Gathering reporting", String.valueOf(inputId++), this::showGatheringReporting)
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

    private void showParticipantReporting() {

    }
    private void showReceivableReporting() {
        List<Participant> participants = controller.getUnpaidParticipants();
        Map<Integer, Gathering> gatherings = controller.getGatheringMap();
        Map<Integer, Session> sessions = controller.getSessionMap();

        OutputUtils.sOutTitle("%-30s %-14s %-14s".formatted("Attendees", "Sessions", "Reservations"));
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

            OutputUtils.sOut("%s%-30s%s %s%-14s %-14s%s".formatted(
                    OutputUtils.ANSI_WHITE,
                    participant.getFullName(),
                    OutputUtils.ANSI_RESET,
                    OutputUtils.ANSI_RED,
                    unpaidTotal.get(0),
                    unpaidTotal.get(1),
                    OutputUtils.ANSI_RESET
                    ));

        }
    }

    private void showGatheringReporting() {
        Scanner scanner = new Scanner(System.in);
        int gatheringId = askInt(scanner, "Please insert gathering id.");
        controller.gatheringReporting(gatheringId);
        displayUserChoices(context);
    }


    public void renderGatheringDetails(Gathering gathering){

    }


    public void renderReceivableDetails(){

    }


    public void renderReport(Gathering gathering) {
        OutputUtils.sOutInfo(renderGatheringInfo(gathering));
        for (Session session : gathering.getAllSessions())
            OutputUtils.sOutInfo(renderSession(session));
        OutputUtils.sOutInfo(renderPrices(gathering.getPriceList()));
    }


    private String renderGatheringInfo(Gathering gathering) {
        StringBuilder sb = new StringBuilder();
        sb.append(OutputUtils.ANSI_YELLOW_BOLD)
                .append(System.lineSeparator())
                .append("- Title: ")
                .append(gathering.getTitle())
                .append(OutputUtils.ANSI_RESET)
        ;
        return sb.toString();
    }

    private String renderSession(Session session) {
        StringBuilder stringBuilder = new StringBuilder();
        //Display main infos
        stringBuilder.append(OutputUtils.ANSI_PURPLE)
        .append("Trainer: ");

        session.getOrganizer().ifPresentOrElse(
                participant -> stringBuilder.append(participant.getFullName()),
                () -> stringBuilder.append("No trainer"));

        stringBuilder.append(" - day: ")
        .append(session.getDay())
        .append(" | ")
        .append(session.getStart())
        .append(" -> ")
        .append(session.getEnd())
        .append(OutputUtils.ANSI_RESET)
        .append(System.lineSeparator());


        List<Participant> attendees = session.getAttendees();
        Map<Integer, Registration> registrations = controller.getRegistrationBySession(session).stream()
                .filter(r -> r.getSessionId() == session.getId())
                .collect(Collectors.toMap(
                        Registration::getParticipantId, Function.identity()))
                ;

        attendees.forEach(attendee -> {
                    stringBuilder
                        .append(registrations.get(attendee.getId()).getStatus().name())
                        .append(" - ")
                        .append(attendee.getFullName())
                        .append(System.lineSeparator());
                }
        );
        return stringBuilder.toString();
    }

    private String renderPrices(List<Pricing> priceList) {
        StringBuilder sb = new StringBuilder();
        sb.append(System.lineSeparator())
                .append(OutputUtils.ANSI_YELLOW_BOLD)
                .append("- Prices:")
                .append(System.lineSeparator())
                .append(OutputUtils.ANSI_RESET);

        Map<ParticipantType, List<Pricing>> list = priceList.stream()
                .collect(Collectors.groupingBy(Pricing::getParticipantType));

        String enumString = Arrays
                .stream(SessionType.values())
                .map(st -> st.name() + " \t\t")
                .collect(Collectors.joining());
        sb.append("Type \t\t")
          .append(enumString)
        ;

        list.forEach((key, values) -> {
            sb.append(System.lineSeparator());
            sb.append(key.name());
            sb.append("\t\t");
            values.forEach(pricing -> {
                sb
                .append(OutputUtils.ANSI_CYAN_BOLD)
                .append(FormatUtils.formatPrice(pricing.getPrice()))
                .append(OutputUtils.ANSI_RESET)
                .append("\t\t");
            });
        });
        return sb.toString();
    }



    //region Error handling
    public void showInvalidIdError(int gatheringId) {
        OutputUtils.sOutError("Gathering id: %s doesn't exist.".formatted(gatheringId));
    }

    //endregion
}
