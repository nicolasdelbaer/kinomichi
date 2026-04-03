package be.nidel.kinomichi.gathering;

import be.nidel.kinomichi.base.BaseView;
import be.nidel.kinomichi.participant.ParticipantDTO;
import be.nidel.kinomichi.participant.ParticipantType;
import be.nidel.kinomichi.pricing.PricingDTO;
import be.nidel.kinomichi.session.Session;
import be.nidel.kinomichi.session.SessionType;
import be.nidel.utils.FormatUtils;
import be.nidel.utils.OutputUtils;
import be.nidel.utils.inputprovider.InputProvider;
import be.nidel.utils.inputprovider.ScannerInput;
import be.nidel.utils.menu.MenuFactory;
import be.technifutur.shared.Menu;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static be.nidel.utils.InputUtils.*;

public class GatheringView extends BaseView<GatheringController> {

    public GatheringView(GatheringController gatheringController) {
        super(gatheringController);
    }

    public void displayUserChoices(Menu context){
        this.context = context;
        this.current = MenuFactory.backQuitTemplate(context)
                .addItem("list gatherings", "l", this::listGatheringRequest)
                .addItem("create new gathering", "c", this::createGatheringRequest)
                .addItem("update gathering (id)", "u", this::updateGatheringData)
                .addItem("delete gathering (id)", "d", this::updateGatheringData)
                .addItem("manage sessions", "ms", this::manageSessionData);
        this.current.renderAndInteract();
    }

    private void listGatheringRequest() {
        controller.getAllGatherings().forEach(
                gathering ->
                        OutputUtils.sOutInfo(("%s. %s: days: %s - sessions: %s - attendees: %s").formatted(
                                gathering.getId(),
                                gathering.getTitle(),
                                gathering.getAllDays().size(),
                                gathering.getAllSessions().size(),
                                gathering.getAllAttendees().size()
                        )));
        System.out.println();
        displayUserChoices(context);
    }

    private void createGatheringRequest() {
        GatheringDTO dto = gatherGatheringData();
        controller.createGathering(dto);
        displayUserChoices(context);
    }

    private GatheringDTO gatherGatheringData() {
        OutputUtils.sOutInfo("Creating a new gathering...");
        InputProvider scanner = new ScannerInput(new Scanner(System.in));

        String title = askInput(scanner,"Title of the gathering?");
        List<PricingDTO> priceList = new ArrayList<>();
        for (SessionType sessionType : SessionType.values())
            priceList.addAll(gatherPricingData(sessionType, scanner));

        return new GatheringDTO(title, priceList);
    }

    private static List<PricingDTO> gatherPricingData(SessionType sessionType, InputProvider scanner) {

        List<PricingDTO> pricingDTOS = new ArrayList<>();
        String[] prices;
        int count = ParticipantType.values().length;
        do{
            String input = askInput(scanner, "Please enter prices for session type: %s %s \nFormat: %s"
                .formatted(sessionType.name(),sessionType.emoji(), FormatUtils.removeLast("price;".repeat(count))));

            prices = input.split(";");
            pricingDTOS = new ArrayList<>();

            if(count == prices.length){
                try {
                    int i = 0;
                    for (ParticipantType participantType : ParticipantType.values()) {
                        BigDecimal price = new BigDecimal(prices[i]);
                        pricingDTOS.add(new PricingDTO(participantType, sessionType, price));
                        i++;
                    }
                }catch (IllegalArgumentException ignored){
                    showWrongBigDecimalError();
                }
            }else{
                showMissingPriceError();
            }

        }while(pricingDTOS.size() != count);
        return pricingDTOS;
    }

    private static void showMissingPriceError() {
        OutputUtils.sOutWarning("Missing price error, please try again");
    }

    private static void showWrongBigDecimalError() {
        OutputUtils.sOutWarning("Wrong format for a price (10.0 or 10)");
    }

    //TODO update
    private void updateGatheringData() {
        displayUserChoices(context);
    }

    private void manageSessionData() {
        OutputUtils.sOutInfo("Managing sessions for gathering id ... ?");
        Integer gatheringId = askInt(new ScannerInput(new Scanner(System.in)),"Enter the gathering id");
        controller.sessionMenuRequest(current.getCurrentMenu(), gatheringId);
    }

    //TODO use a better visualization
    public void showSessionsForGathering(Gathering gathering){
        OutputUtils.sOutTitle(gathering.getTitle());
        Map<LocalDate,List<Session>> sessionsByDay = gathering.getAllSessions().stream().collect(Collectors.groupingBy(
                s -> s.getDay(),
                Collectors.toList()
        ));

        for (Map.Entry<LocalDate,List<Session>> entry : sessionsByDay.entrySet()) {
            OutputUtils.sOutInfo(entry.getKey().toString());
            for (Session session : sessionsByDay.get(entry.getKey())) {
                OutputUtils.sOut(session.getOrganizer() + ": " + session.getStart() + " to " + session.getEnd());
            }
        }
    }


    public void showInvalidIdError(Integer id) {
        OutputUtils.sOutError("INVALID ID" + id);
    }

}
