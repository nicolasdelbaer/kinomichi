package be.nidel.utils.menu;

import be.nidel.utils.OutputUtils;
import be.technifutur.shared.Menu;

//Objectif enlever de la complexité dans la création et gestion des menus
public class MenuFactory {

    public static MenuController backQuitTemplate(Menu context){
        Menu menu = new Menu();
        MenuController menuController = new MenuController(menu, context);
        menu.addHiddenItem("back", "b", menuController::handleBack);
        menu.addHiddenItem("quit", "q", menuController::handleQuit);
        menu.setPostRender(OutputUtils.STYLISABLE_LINE.formatted(OutputUtils.ANSI_BLACK_BACKGROUND, "\"b\": back - \"q\": quit", OutputUtils.ANSI_RESET));
        return menuController;
    }

    public static MenuController confirmTemplate(Menu context, Runnable continueAction){
        Menu menu = new Menu();
        MenuController menuController = new MenuController(menu, context);
        menu.addHiddenItem("yes", "y", continueAction);
        menu.addHiddenItem("no", "n", menuController::handleBack);
        menu.setPostRender(OutputUtils.STYLISABLE_LINE.formatted(OutputUtils.ANSI_BLACK_BACKGROUND, "\"y\": yes - \"n\": no", OutputUtils.ANSI_RESET));
        menuController.setInteractionMessage("Do you confirm?");
        return menuController;
    }
}
