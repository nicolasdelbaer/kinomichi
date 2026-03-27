package be.nidel.kinomichi;

import be.nidel.utils.OutputUtils;
import be.technifutur.shared.Menu;

public class Kinomichi {

    public void showMenu() {
        OutputUtils.sOutTitle("--- Stage Kinomichi ---");

        Menu menu = new Menu();
        menu.addItem("Add a new participant", "1", this::addParticipantHandler);
        menu.interact();
    }

    public void addParticipantHandler() {
        OutputUtils.sOutInfo("Add Participant");
    }
}
