package be.nidel.kinomichi.gathering;

import be.nidel.kinomichi.base.KinomichiModelOwner;
import be.nidel.kinomichi.base.BaseController;
import be.nidel.kinomichi.pricing.Pricing;
import be.nidel.kinomichi.session.SessionRequestEvent;
import be.technifutur.shared.Menu;

import java.util.List;

public class GatheringController extends BaseController implements KinomichiModelOwner {
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
        Gathering gathering = new Gathering();
        gathering.setTitle(gatheringDTO.title());
        gathering.setPrices(gatheringDTO.priceList().stream()
                .map(dto -> new Pricing(
                        dto.participantType(),
                        dto.sessionType(),
                        dto.price()
                        )).toList()
        );
        model.addGathering(gathering);
        return gathering;
    }
    public List<Gathering> getAllGatherings(){
        return model.getAllGathering().values().stream().toList();
    }

    @Override
    public GatheringModel getModel() {
        return model;
    }
}
