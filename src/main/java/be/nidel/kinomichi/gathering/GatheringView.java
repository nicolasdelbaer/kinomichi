package be.nidel.kinomichi.gathering;

import be.nidel.kinomichi.KinomichiView;
import be.nidel.kinomichi.session.Session;
import be.nidel.utils.OutputUtils;
import be.nidel.utils.menu.MenuController;
import be.nidel.utils.menu.MenuFactory;
import be.technifutur.shared.Menu;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class GatheringView implements KinomichiView {

    private final GatheringController controller;
    private Menu context;
    private MenuController current;

    public GatheringView(GatheringController gatheringController) {
        this.controller = gatheringController;
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

        controller.createGathering(new GatheringDTO(title));
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

    @Override
    public void refresh() {
        current.interact();
    }


/*
    private Gathering createGatheringHandler(String inputRequest) {
        OutputUtils.sOutInfo(inputRequest);
        Scanner scanner = new Scanner(System.in);
        Gathering gathering = new Gathering(askInput(scanner, "Title of the event"));

        boolean configureDay = (askInput(
                scanner,
                "Type (d) for configuring a day or (any key) for a single session ?")
        ).equals("d");

        if(configureDay){
            handleNewDay(scanner, gathering);
        }else{
            gatheringController.sessionMenuRequest();
        }
        return  gathering;
    }

    private void handleNewDay(Scanner scanner, Gathering gathering) {
        boolean addNewDay = false;
        int amount = 1;
        do{
            OutputUtils.sOutInfo("A day needs the date, starting time & the number of sessions");
            gathering.addNewDay(
                    askDate(scanner, "Day1 (dd/mm/yyyy)"),
                    askTime(scanner, "From Time (hh:mm)"),
                    askInt(scanner, "How many sessions from the start ?")
            );

            addNewDay = (askInput(
                    scanner,
                    "Type (d) for continue adding days or (any key) to continue")
            ).equals("d");
        }while(addNewDay);
    }
*/
}
