package be.nidel.utils.menu;

import be.nidel.utils.OutputUtils;
import be.technifutur.shared.Menu;

import java.util.function.Consumer;

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

    public static MenuController editTemplate(Menu context, Consumer<String> editAction, String field, String content, String freeInputPattern) {
        Menu menu = new Menu();
        MenuController menuController = new MenuController(menu, context);
        menu.addRegexItem("free input", freeInputPattern, () -> {
            try {
                editAction.accept(menuController.getLastEntry());
            } catch (IllegalArgumentException e) {
                menuController.interact();
            }
        });
        menu.addHiddenItem("empty", "", () ->{});
        menu.setPreRender(OutputUtils.STYLISABLE_LINE.formatted(OutputUtils.ANSI_BLACK_BACKGROUND, "Edit Mode - \"enter\": pass", OutputUtils.ANSI_RESET));
        menuController.setPostRender("%sUpdate %s: %s ?%s".formatted(OutputUtils.ANSI_YELLOW, field, content, OutputUtils.ANSI_RESET));
        return menuController;
    }
}
