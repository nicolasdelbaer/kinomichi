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
        backMenu.renderAndInteract();
    }
    public void handleQuit(){
        OutputUtils.sOutBye();
    }


    //region delegated methods
    //IDEA add a addFirst/addLast Feature or add an optional id for sorting?
    public MenuController addItem(String description, String input, Runnable action) {
        currentMenu.addItem(description, input, action);
        return this;
    }

    public MenuController addRegexItem(String description, String input, Runnable action) {
        currentMenu.addRegexItem(description, input, action);
        return this;
    }

    public MenuController addItem(String description, String[] inputs, Runnable action) {
        currentMenu.addItem(description, inputs, action);
        return this;
    }

    public MenuController addHiddenItem(String description, String[] inputs, Runnable action) {
        currentMenu.addHiddenItem(description, inputs, action);
        return this;
    }

    public MenuController addHiddenItem(String description, String input, Runnable action) {
        currentMenu.addHiddenItem(description, input, action);
        return this;
    }

    public MenuController setPreRender(String postRender) {
        currentMenu.setPostRender(postRender);
        return this;
    }

    public MenuController setPostRender(String postRender) {
        currentMenu.setPostRender(postRender);
        return this;
    }

    public void interact() {
        if(Objects.nonNull(interactionMessage) && !interactionMessage.isEmpty())
            OutputUtils.sOutWarning(interactionMessage);
        currentMenu.interact();
    }

    public void renderAndInteract() {
        if(Objects.nonNull(interactionMessage) && !interactionMessage.isEmpty())
            OutputUtils.sOutWarning(interactionMessage);
        currentMenu.renderAndInteract();
    }
    public void setTitle(String title) {
        currentMenu.setTitle(title);
    }

    public String getTitle() {
        return currentMenu.getTitle();
    }
    //endregion


    //region getter & setter


    public String getLastEntry() {
        return currentMenu.getLastEntry();
    }

    public String getInteractionMessage() {
        return interactionMessage;
    }

    public MenuController setInteractionMessage(String interactionMessage) {
        this.interactionMessage = interactionMessage;
        return this;
    }

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public Menu getBackMenu() {
        return backMenu;
    }
    //endregion
}
