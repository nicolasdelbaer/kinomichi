package be.nidel.utils.menu;

import be.nidel.utils.OutputUtils;
import be.technifutur.shared.Menu;

public class MenuController {
    private Menu currentMenu;

    //IDEA -> use a pile ?
    private Menu backMenu;

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


    //region getter & setter
    //IDEA add a addFirst/addLast Feature or add an optional id for sorting?
    public MenuController addItem(String description, String input, Runnable action) {
        currentMenu.addItem(description, input, action);
        return this;
    }

    public void interact() {
        currentMenu.interact();
    }

    public void setTitle(String title) {
        currentMenu.setTitle(title);
    }

    public String getTitle() {
        return currentMenu.getTitle();
    }
    //endregion
}
