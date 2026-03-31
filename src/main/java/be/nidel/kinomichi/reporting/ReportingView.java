package be.nidel.kinomichi.reporting;

import be.nidel.kinomichi.base.BaseView;
import be.nidel.kinomichi.gathering.Gathering;
import be.nidel.kinomichi.participant.ParticipantType;
import be.nidel.kinomichi.pricing.Pricing;
import be.nidel.kinomichi.registration.Registration;
import be.nidel.kinomichi.session.Session;
import be.nidel.kinomichi.session.SessionType;
import be.nidel.utils.FormatUtils;
import be.nidel.utils.OutputUtils;
import be.nidel.utils.menu.MenuFactory;
import be.technifutur.shared.Menu;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import static be.nidel.utils.InputUtils.askInt;

public class ReportingView extends BaseView<ReportingController> {

    public ReportingView(ReportingController controller) {
        super(controller);
    }

    public void displayUserChoices(Menu context){
        this.context = context;

        this.current = MenuFactory.backQuitTemplate(context)
                .addItem("Participants by gathering report", "1", this::showGatheringReport)
        ;
        this.current.interact();
    }

    private void showGatheringReport() {
        Scanner scanner = new Scanner(System.in);
        int gatheringId = askInt(scanner, "Please insert gathering id.");
        controller.gatheringReport(gatheringId);
        displayUserChoices(context);
    }

    public void renderReport(Gathering gathering, Map<Integer, Registration> registrationMap) {
        OutputUtils.sOutInfo(renderGatheringInfo(gathering));
        for (Session session : gathering.getAllSessions())
            OutputUtils.sOutInfo(renderSession(session, registrationMap));
        OutputUtils.sOutInfo(renderPrices(gathering.getPriceList(), registrationMap));
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

    private String renderSession(Session session, Map<Integer, Registration> registrationMap) {
        StringBuilder sb = new StringBuilder();
        //Display main infos
        sb.append(OutputUtils.ANSI_PURPLE)
        .append("Trainer: ");

        session.getTrainer().ifPresentOrElse(
                participant -> sb.append(participant.getFullName()),
                () -> sb.append("No trainer"));

        sb.append(" - ")
        .append("day: ")
        .append(session.getDay())
        .append(" | ")
        .append(session.getStart())
        .append(" -> ")
        .append(session.getEnd())
        .append(OutputUtils.ANSI_RESET)
        .append(System.lineSeparator());

        session.getAttendees().forEach(attendee -> sb
                .append(registrationMap.get(session.getId()).getStatus().name())
                .append(" - ")
                .append(attendee.getFullName())
                .append(System.lineSeparator())
        );

        return sb.toString();
    }

    private String renderPrices(List<Pricing> priceList, Map<Integer, Registration> registrationMap) {
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

    public void showInvalidIdError(int gatheringId) {
        OutputUtils.sOutError("Gathering id: %s doesn't exist.");
    }
}
