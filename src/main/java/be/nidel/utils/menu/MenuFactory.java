package be.nidel.utils.menu;

import be.technifutur.shared.Menu;

//Objectif enlever de la complexité dans la création et gestion des menus
public class MenuFactory {

    public static MenuController backQuitTemplate(Menu context){
        Menu menu = new Menu();
        MenuController menuController = new MenuController(menu, context);
        menu.addItem("back", "b", menuController::handleBack);
        menu.addItem("quit", "q", menuController::handleQuit);
        return menuController;
    }

    public static MenuController confirmTemplate(Menu context, Runnable continueAction){
        Menu menu = new Menu();
        MenuController menuController = new MenuController(menu, context);
        menu.addItem("yes", "y", continueAction);
        menu.addItem("no", "n", menuController::handleBack);
        menuController.setInteractionMessage("Continue doing this action? (y/n)");
        return menuController;
    }
}
