package be.nidel.kinomichi.base;

import be.nidel.utils.menu.MenuController;
import be.technifutur.shared.Menu;

public abstract class BaseView<C extends BaseController> {

    protected C controller;
    protected Menu context;
    protected MenuController current;

    public BaseView(C controller) {
        this.controller = controller;
    }

    //Use to retrigger the last current menu -> ie: error in controller after handling input
    public void refresh(){
        current.interact();
    }
}
