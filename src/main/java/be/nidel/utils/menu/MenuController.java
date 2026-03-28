package be.nidel.utils.menu;

import be.nidel.utils.OutputUtils;
import be.technifutur.shared.Menu;

import java.util.Objects;

public class MenuController {
    private Menu currentMenu;

    //IDEA -> use a pile ?
    private Menu backMenu;

    private String interactionMessage;

    //Menu content -> Wrapped menu
    //Menu context -> Context to get back when navigating trough menus
    public MenuController(Menu currentMenu, Menu backMenu) {
        this.currentMenu = currentMenu;
        this.backMenu = backMenu;
    }

    public void handleBack(){
        backMenu.interact();
    }
    public void handleQuit(){
        OutputUtils.sOutTitle("Bye bye !");
    }


    //region delegated methods
    //IDEA add a addFirst/addLast Feature or add an optional id for sorting?
    public MenuController addItem(String description, String input, Runnable action) {
        currentMenu.addItem(description, input, action);
        return this;
    }

    public void interact() {
        //NOTE maybe separate interact & render from Menu.jar for more control on how it's rendered
        //allowing "hidden inputs" too
        if(Objects.nonNull(interactionMessage) && !interactionMessage.isEmpty())
            OutputUtils.sOutWarning(interactionMessage);
        currentMenu.interact();
    }
    //endregion


    //region getter & setter
    public void setTitle(String title) {
        currentMenu.setTitle(title);
    }

    public String getTitle() {
        return currentMenu.getTitle();
    }

    public String getInteractionMessage() {
        return interactionMessage;
    }

    public MenuController setInteractionMessage(String interactionMessage) {
        this.interactionMessage = interactionMessage;
        return this;
    }
    //endregion
}
