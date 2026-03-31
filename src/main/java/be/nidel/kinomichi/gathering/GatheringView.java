package be.nidel.kinomichi.gathering;

import be.nidel.kinomichi.base.BaseView;
import be.nidel.kinomichi.participant.ParticipantType;
import be.nidel.kinomichi.pricing.PricingDTO;
import be.nidel.kinomichi.session.Session;
import be.nidel.kinomichi.session.SessionType;
import be.nidel.utils.OutputUtils;
import be.nidel.utils.menu.MenuFactory;
import be.technifutur.shared.Menu;

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
                .addItem("create new gathering", "c", this::gatherGatheringData)
                //.addItem("update gathering id (cmd;id)", "u", this::updateGatheringData)
                .addItem("manage sessions id", "u", this::manageSessionData);
        this.current.interact();
    }

    private void gatherGatheringData() {
        OutputUtils.sOutInfo("Creating a new gathering...");
        Scanner scanner = new Scanner(System.in);

        String title = askInput(scanner,"Title of the gathering?");
        List<PricingDTO> priceList = new ArrayList<>();
        for (SessionType sessionType : SessionType.values()) {
            for (ParticipantType participantType : ParticipantType.values()) {
                priceList.add(
                        new PricingDTO(participantType,
                                sessionType,
                                askBigDecimal(scanner,
                                        "Please enter the price for %s -> %s:".formatted(
                                                sessionType.name(),
                                                participantType.name()
                                ))
                        )
                );
            }
        }

        controller.createGathering(new GatheringDTO(title, priceList));
        displayUserChoices(context);
    }

    //TODO update
    private void updateGatheringData() {
        displayUserChoices(context);
    }

    private void manageSessionData() {
        OutputUtils.sOutInfo("Managing sessions for gathering id ... ?");
        Integer gatheringId = askInt(new Scanner(System.in),"Enter the gathering id");
        controller.sessionMenuRequest(current.getCurrentMenu(), gatheringId);
    }

    public void showSessionsForGathering(Gathering gathering){
        OutputUtils.sOutTitle(gathering.getTitle());
        Map<LocalDate,List<Session>> sessionsByDay = gathering.getAllSessions().stream().collect(Collectors.groupingBy(
                s -> s.getDay(),
                Collectors.toList()
        ));

        for (Map.Entry<LocalDate,List<Session>> entry : sessionsByDay.entrySet()) {
            OutputUtils.sOutInfo(entry.getKey().toString());
            for (Session session : sessionsByDay.get(entry.getKey())) {
                OutputUtils.sOut(session.getTrainer() + ": " + session.getStart() + " to " + session.getEnd());
            }
        }
    }


    public void showInvalidIdError(Integer id) {
        OutputUtils.sOutError("INVALID ID" + id);
    }

}
