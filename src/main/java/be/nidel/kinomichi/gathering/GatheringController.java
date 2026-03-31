package be.nidel.kinomichi.gathering;

import be.nidel.kinomichi.Kinomichi;
import be.nidel.kinomichi.KinomichiController;
import be.nidel.kinomichi.KinomichiModel;
import be.nidel.utils.OutputUtils;
import be.technifutur.shared.Menu;

import java.util.List;

public class GatheringController implements KinomichiController {
    GatheringModel model = new GatheringModel();
    GatheringView view = new GatheringView(this);

    public SessionRequestEvent onSessionRequest = new SessionRequestEvent();

    public void showMenu(Menu menu) {
        view.displayUserChoices(menu);
    }

    public void sessionMenuRequest(Menu menu, Integer gatheringId) {
        if(model.isIdValid(gatheringId)) {
            //NOTE coupling by passing the model /!\
            Gathering gathering = model.get(gatheringId);
            view.showSessionsForGathering(gathering);

            onSessionRequest.emit(new GatheringPayload(menu, gathering));
        }else{
            view.showInvalidIdError(gatheringId);
            showMenu(menu);
        }
    }

    public Gathering createGathering(GatheringDTO gatheringDTO) {
        Gathering gathering = new Gathering(gatheringDTO.title());
        model.addGathering(gathering);
        return gathering;
    }
    public List<Gathering> getAllGatherings(){
        return model.getAllGathering().values().stream().toList();
    }

    @Override
    public GatheringModel getModel() {
        return null;
    }
}
