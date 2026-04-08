package be.nidel.kinomichi.reporting;

import be.nidel.kinomichi.base.BaseView;
import be.nidel.kinomichi.gathering.Gathering;
import be.nidel.kinomichi.gathering.renderer.GatheringRenderer;
import be.nidel.kinomichi.gathering.renderer.RendererGatheringDTO;
import be.nidel.kinomichi.participant.Participant;
import be.nidel.kinomichi.pricing.Pricing;
import be.nidel.kinomichi.registration.Registration;
import be.nidel.kinomichi.session.Session;
import be.nidel.kinomichi.session.SessionType;
import be.nidel.utils.BigDecimalFormatter;
import be.nidel.utils.FormatUtils;
import be.nidel.utils.OutputUtils;
import be.nidel.utils.inputprovider.InputProvider;
import be.nidel.utils.inputprovider.ScannerInput;
import be.nidel.utils.menu.MenuFactory;
import be.technifutur.shared.Menu;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static be.nidel.utils.InputUtils.askInt;

public class ReportingView extends BaseView<ReportingController> {

    public ReportingView(ReportingController controller) {
        super(controller);
    }

    public void displayUserChoices(Menu context){
        this.menuContext = context;

        int inputId = 1;
        this.current = MenuFactory.backQuitTemplate(context)
                .addItem("Gathering overview", String.valueOf(inputId++), this::showGatheringOverview)
                //.addItem("Participant reporting", String.valueOf(inputId++), this::showParticipantStatus) //TODO attendee detail
                .addItem("Gathering status", String.valueOf(inputId++), this::showGatheringStatus)
                .addItem("Payments status", String.valueOf(inputId++), this::showReceivableReporting)
        ;
        this.current.renderAndInteract();
    }

    //region Menu Handlers
    private void showGatheringOverview() {
        InputProvider scanner = new ScannerInput(new Scanner(System.in));
        int gatheringId = askInt(scanner, "Please insert gathering id.");
        controller.gatheringOverview(gatheringId);
        displayUserChoices(menuContext);
    }

    //TODO liste sessions et statut
    //TODO payé / à payer / Projection total
    //TODO ristourne (opt.)
    private void showParticipantStatus() {
        displayUserChoices(menuContext);
    }
    private void showGatheringStatus() {
        InputProvider scanner = new ScannerInput(new Scanner(System.in));
        int gatheringId = askInt(scanner, "Please insert gathering id.");
        controller.gatheringReporting(gatheringId);
        displayUserChoices(menuContext);
    }
    private void showReceivableReporting() {
        List<Participant> participants = controller.getUnpaidParticipants();
        Map<Integer, Gathering> gatherings = controller.getGatheringMap();
        Map<Integer, Session> sessions = controller.getSessionMap();

        OutputUtils.sOutTitle(OutputUtils.DEFAULT_LINE.formatted("%-23s%30s".formatted("THEY MUST PAY!", "(All gatherings)")));
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
        displayUserChoices(menuContext);
    }

    //endregion

    //region Renderings

    public void renderGatheringStatus(Gathering gathering) {
        ReportingController.Stats stats = controller.getGatheringStats(gathering);
        ReportingController.PaymentForecast paymentForecast = controller.getPaymentForecast(gathering);
        ReportingController.Reservations reservations = controller.getReservations(gathering);

        OutputUtils.sOutTitle(OutputUtils.DEFAULT_LINE.formatted("DETAIL GATHERING"));

        OutputUtils.sOut(OutputUtils.DEFAULT_LINE.formatted("Stats"));
        OutputUtils.sOut("%-5s %-24s %-22s".formatted("", "Nb inscriptions", stats.nbInscriptions()));
        OutputUtils.sOut("%-5s %-24s %-22s".formatted("", "Nb participations", stats.nbParticipations()));
        OutputUtils.sOut("%-5s %-24s %-22s".formatted("", "Nb annulations", stats.nbAnnulations()));
        OutputUtils.sOut("%-5s %-24s %-22s".formatted("", "Nb absences", stats.nbAbsences()));

        System.out.println();
        OutputUtils.sOut(OutputUtils.DEFAULT_LINE.formatted("Payments"));
        OutputUtils.sOut("%-5s %-24s %-22s".formatted("", "Total paid", getFormatted(paymentForecast.totPaid())));
        OutputUtils.sOut("%-5s %-24s %-22s".formatted("", "Total discount", getFormatted(paymentForecast.totDiscount())));
        OutputUtils.sOut("%-5s %-24s %-22s".formatted("", "Total left",
                (paymentForecast.totUnpaid().compareTo(BigDecimal.ZERO) > 0 ? OutputUtils.ANSI_RED:"")+
                        getFormatted(paymentForecast.totUnpaid()))
        );
        OutputUtils.sOut("%-5s %-24s %-22s".formatted("", "Forecast",
                (paymentForecast.forecast().compareTo(BigDecimal.ZERO) > 0 ? OutputUtils.ANSI_GREEN:"")+
                        getFormatted(paymentForecast.forecast())));

        System.out.println();
        OutputUtils.sOut(OutputUtils.DEFAULT_LINE.formatted("%-29s %-22s".formatted("Reservations", "nb (nb paid)")));
        OutputUtils.sOut("%-5s %-24s %-15s %-9s".formatted("", "Dinners", "%s (%s)".formatted(reservations.nbDinners(), reservations.nbPaidDinners()), SessionType.Dinner.emoji()));
        OutputUtils.sOut("%-5s %-24s %-15s %-9s".formatted("", "Accomodations", "%s (%s)".formatted(reservations.nbAccommodations(), reservations.nbPaidAccommodations()), SessionType.Accommodation.emoji()));

    }

    private String getFormatted(BigDecimal bigDecimal) {
        BigDecimalFormatter priceFormatter = new BigDecimalFormatter(bigDecimal);
        priceFormatter.formatEuro();
        return priceFormatter.toString();
    }

    public void renderOverviewReport(RendererGatheringDTO gatheringDTO) {
        GatheringRenderer gatheringRenderer = new GatheringRenderer();
        gatheringRenderer.render(gatheringDTO);
    }

    //endregion

    //region Error handling
    public void showInvalidGatheringIdError(int gatheringId) {
        OutputUtils.sOutError("Gathering id: %s doesn't exist.".formatted(gatheringId));
    }

    //endregion
}
