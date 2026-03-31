package be.nidel.kinomichi.reporting;

import be.nidel.kinomichi.base.BaseView;
import be.nidel.utils.menu.MenuFactory;
import be.technifutur.shared.Menu;

public class ReportingView extends BaseView<ReportingController> {

    public ReportingView(ReportingController controller) {
        super(controller);
    }

    public void displayUserChoices(Menu context){
        this.context = context;
        this.current = MenuFactory.backQuitTemplate(context)
                .addItem("show gathering report", "1", this::showGatheringReport);
        this.current.interact();
    }

    private void showGatheringReport() {

    }
}
