package be.nidel.kinomichi.reporting;

import be.nidel.kinomichi.base.BaseController;
import be.nidel.kinomichi.gathering.Gathering;
import be.nidel.kinomichi.gathering.GatheringModel;
import be.nidel.kinomichi.registration.Registration;
import be.nidel.kinomichi.registration.RegistrationModel;
import be.nidel.kinomichi.session.Session;
import be.technifutur.shared.Menu;

import java.util.*;

public class ReportingController extends BaseController {

    private GatheringModel gatheringModel;
    private RegistrationModel registrationModel;

    public void setGatheringModel(GatheringModel gatheringModel){
        this.gatheringModel = gatheringModel;
    }

    public void setRegistrationModel(RegistrationModel registrationModel) {
        this.registrationModel = registrationModel;
    }

    ReportingView view = new ReportingView(this);
    public void showMenu(Menu menu) {
        view.displayUserChoices(menu);
    }

    public void gatheringReport(int gatheringId){
        try {
            Gathering gathering = gatheringModel.get(gatheringId);

            Map<Integer, Registration> registrationMap = new HashMap<>();
            for (Session session : gathering.getAllSessions()) {
                List<Registration> registrationList = registrationModel
                        .getAllRegistrationBySessionId(gatheringId);
                for (int i=0; i<registrationList.size(); i++) {
                    registrationMap.put(i, registrationList.get(i));
                }
            }

            view.renderReport(gathering, registrationMap);
        } catch (NoSuchElementException e) {
            view.showInvalidIdError(gatheringId);
            view.refresh();
        }

    }
}