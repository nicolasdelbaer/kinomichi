package be.nidel.kinomichi.gathering;

import be.nidel.kinomichi.base.BaseView;
import be.nidel.kinomichi.gathering.renderer.GatheringSessionRenderer;
import be.nidel.kinomichi.participant.ParticipantType;
import be.nidel.kinomichi.pricing.Pricing;
import be.nidel.kinomichi.pricing.PricingDTO;
import be.nidel.kinomichi.pricing.PricingGroup;
import be.nidel.kinomichi.pricing.PricingGroupDTO;
import be.nidel.kinomichi.session.Session;
import be.nidel.kinomichi.session.SessionType;
import be.nidel.utils.FormatUtils;
import be.nidel.utils.OutputUtils;
import be.nidel.utils.inputprovider.InputProvider;
import be.nidel.utils.inputprovider.ScannerInput;
import be.nidel.utils.inputprovider.StaticInput;
import be.nidel.utils.menu.MenuFactory;
import be.technifutur.shared.Menu;

import java.math.BigDecimal;
import java.util.*;

import static be.nidel.utils.InputUtils.*;

public class GatheringView extends BaseView<GatheringController> {

    public GatheringView(GatheringController gatheringController) {
        super(gatheringController);
    }

    public void displayUserChoices(Menu context){
        this.menuContext = context;
        this.current = MenuFactory.backQuitTemplate(context)
                .addItem("list gatherings", "l", this::listGatheringRequest)
                .addItem("create new gathering", "c", this::createGatheringRequest)
                .addItem("update gathering (id)", "u", this::updateGatheringRequest)
                .addItem("delete gathering (id)", "d", this::archiveGathering)
                .addItem("manage sessions", "ms", this::manageSessionData);
        this.current.renderAndInteract();
    }

    private void updateGatheringRequest() {
        OutputUtils.sOutInfo("Updating a gathering:");
        int gatheringId = askInt(
                new ScannerInput(new Scanner(System.in)),
                "Enter the gathering id"
        );
        Optional<Gathering> tmpGathering = controller.getGatheringById(gatheringId);
        if(tmpGathering.isPresent())
        {
            UpdateGatheringDTO updateSessionDTO = gatherUpdateGatheringData(tmpGathering.get());
            controller.updateGathering(updateSessionDTO);
            displayUserChoices(menuContext);
        }

    }

    private void archiveGathering() {
        OutputUtils.sOutInfo("Archive a gathering:");
        int participantId = askInt(
                new ScannerInput(new Scanner(System.in)),
                "Enter the gathering id"
        );
        controller.archiveGathering(participantId);
        displayUserChoices(menuContext);
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
        displayUserChoices(menuContext);
    }

    private void createGatheringRequest() {
        OutputUtils.sOutInfo("Creating a new gathering:");
        CreateGatheringDTO dto = gatherGatheringData();
        controller.createGathering(dto);
        displayUserChoices(menuContext);
    }

    private CreateGatheringDTO gatherGatheringData() {
        InputProvider scanner = new ScannerInput(new Scanner(System.in));

        String title = askInput(scanner,"Title of the gathering?");
        List<PricingGroupDTO> priceList = new ArrayList<>();
        for (SessionType sessionType : SessionType.values()){

            try {
                priceList.add(gatherPricingData(sessionType, scanner));
            } catch (Exception ignored) {
            }
        }

        return new CreateGatheringDTO(title, priceList);
    }

    private PricingGroupDTO gatherPricingData(SessionType sessionType, InputProvider scanner) {
        List<PricingDTO> pricingDTOS = new ArrayList<>();
        String[] prices;

        int count = ParticipantType.values().length;
        String instructions = "Enter 1 price for all or specify all prices, \ne.g.: (%s) or (%s)"
                .formatted("price", FormatUtils.removeLast("price;".repeat(count)));
        OutputUtils.sOutInfo(instructions);
        do{
            String input = askInput(scanner, "Please enter prices for session type: %s %s"
                .formatted(sessionType.name(),sessionType.emoji()));

            prices = input.split(";");
            pricingDTOS = new ArrayList<>();

            //Auto fill with default value
            if(prices.length == 1) {
                String buffer = prices[0];
                prices = new String[count];
                Arrays.fill(prices, buffer);
            }

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
                    throw new IllegalArgumentException("Wrong characters");
                }
            }else{
                showMissingPriceError();
            }

        }while(pricingDTOS.size() != count);
        return new PricingGroupDTO(pricingDTOS, sessionType);
    }

    private void showMissingPriceError() {
        OutputUtils.sOutWarning("Missing price error, please try again");
    }

    private void showWrongBigDecimalError() {
        OutputUtils.sOutWarning("Wrong format for a price (10.0 or 10)");
    }


    private UpdateGatheringDTO gatherUpdateGatheringData(Gathering source) {
        //Context is used for lambdas that need a final property
        class Context {
            String title = source.getTitle();
            List<PricingGroupDTO> priceGroupList = new ArrayList<> (GatheringController
                    .getDTOFromPricingGroup(source.getPriceGroups()));
            int curPriceIndex = 0;
        };
        Context ctx = new Context();

        askForEditOrSource(menuContext, (providedValue) -> {
            ctx.title = askInput(new StaticInput(providedValue), "Title");
        }, "Title", source.getTitle());

        for (PricingGroup priceGroup : source.getPriceGroups()) {
            askForEditOrSource(menuContext, (providedValue) -> {
                if(providedValue.isEmpty())
                    providedValue = priceGroup.pricesToString();
                InputProvider provider = new StaticInput(providedValue);
                PricingGroupDTO newGroup = gatherPricingData(priceGroup.getSessionType(),provider);
                ctx.priceGroupList.set(ctx.curPriceIndex, newGroup);
            }, "Prices", priceGroup.pricesToString(), "\\d+(\\.\\d+)?");
            ctx.curPriceIndex ++;
        }

        return new UpdateGatheringDTO(source.getId(), ctx.title, ctx.priceGroupList);
    }

    private void manageSessionData() {
        OutputUtils.sOutInfo("Managing sessions for gathering id ... ?");
        Integer gatheringId = askInt(new ScannerInput(new Scanner(System.in)),"Enter the gathering id");
        controller.sessionMenuRequest(current.getCurrentMenu(), gatheringId);
    }

    public void showSessionsForGathering(Gathering gathering){
        GatheringSessionRenderer gatheringSessionRenderer = new GatheringSessionRenderer();
        OutputUtils.sOutTitle(GatheringSessionRenderer.FORMAT.formatted(
                "ID. ", "TYPE", "ORGANIZER", "DATE", "START", "END"));
        for (Session session : gathering.getAllSessions()) {
            gatheringSessionRenderer.render(session);
        }
        System.out.println();
    }
    public void showArchivedFeedback(Gathering gathering){
        OutputUtils.sOutWarning("%s. %s %s".formatted(
                gathering.getId(),
                gathering.getTitle(),
                "has been archived"
        ));
    }

    public void showArchivedErrorFeedback() {
        OutputUtils.sOutWarning("Cannot execute deletion, bad participant id?");
    }

    public void showInvalidIdError(Integer id) {
        OutputUtils.sOutError("INVALID ID" + id);
    }

    public void showUpdateErrorFeedback() {
        OutputUtils.sOutWarning("Cannot execute update, bad participant id?");
    }
}
