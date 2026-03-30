package be.nidel.kinomichi.gathering;

import be.nidel.utils.OutputUtils;
import be.technifutur.shared.Menu;

public class GatheringController {
    GatheringModel gatheringModel = new GatheringModel();
    GatheringView gatheringView = new GatheringView(this);

    public SessionRequestEvent onSessionRequest = new SessionRequestEvent();

    public void showMenu(Menu menu) {
        gatheringView.displayUserChoices(menu);
    }

    public void sessionMenuRequest(Menu menu, Integer gatheringId) {
        if(gatheringModel.isIdValid(gatheringId)) {
            //NOTE coupling by passing the model /!\
            Gathering gathering = gatheringModel.get(gatheringId);
            gatheringView.showSessionsForGathering(gathering);
            onSessionRequest.emit(new GatheringPayload(menu, gatheringId, gatheringModel, gathering));
        }else{
            OutputUtils.sOutError("INVALID ID");
            showMenu(menu);
        }
    }

    public void createGathering(GatheringDTO gatheringDTO) {
        Gathering gathering = new Gathering(gatheringDTO.title());
        gatheringModel.addGathering(gathering);
    }
}
