package be.nidel.kinomichi.reporting;

import be.nidel.kinomichi.base.BaseController;
import be.technifutur.shared.Menu;

public class ReportingController extends BaseController {

    ReportingView view = new ReportingView(this);
    public void showMenu(Menu menu) {
        view.displayUserChoices(menu);
    }

}
