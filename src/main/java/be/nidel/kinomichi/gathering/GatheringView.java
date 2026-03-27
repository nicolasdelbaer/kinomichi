package be.nidel.kinomichi.gathering;

import be.nidel.kinomichi.KinomichiView;
import be.nidel.utils.OutputUtils;
import be.technifutur.shared.Menu;

import java.util.Scanner;

public class GatheringView implements KinomichiView {

    private final GatheringController gatheringController;
    private Menu context;

    public GatheringView(GatheringController gatheringController) {
        this.gatheringController = gatheringController;
    }

//
//    private Gathering createGatheringHandler(String inputRequest) {
//        OutputUtils.sOutInfo(inputRequest);
//        Scanner scanner = new Scanner(System.in);
//        Gathering gathering = new Gathering(askInput(scanner, "Title of the event"));
//
//        boolean configureDay = (askInput(
//                scanner,
//                "Type (d) for configuring a day or (any key) for a single period ?")
//        ).equals("d");
//
//        if(configureDay){
//            handleNewDay(scanner, gathering);
//        }else{
//            //TODO delegate to SessionVIew
//            gatheringController.showSessionMenu();
//            handleNewSession(scanner, gathering);
//        }
//        return  gathering;
//    }
//
//    private void handleNewDay(Scanner scanner, Gathering gathering) {
//        boolean addNewDay = false;
//        int amount = 1;
//        do{
//            OutputUtils.sOutInfo("A day needs the date, starting time & the number of periods");
//            gathering.addNewDay(
//                    askDate(scanner, "Day1 (dd/mm/yyyy)"),
//                    askTime(scanner, "From Time (hh:mm)"),
//                    askInt(scanner, "How many periods from the start ?")
//            );
//
//            addNewDay = (askInput(
//                    scanner,
//                    "Type (d) for continue adding days or (any key) to continue")
//            ).equals("d");
//        }while(addNewDay);
//    }
}
