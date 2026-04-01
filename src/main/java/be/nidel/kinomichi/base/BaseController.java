package be.nidel.kinomichi.base;

public abstract class BaseController {
    //If you don't want to show feedback & errors
    protected boolean silentView = false;
    public void silenceView(boolean b) {
        silentView = b;
    }
}
