package be.nidel.kinomichi.gathering;

import be.nidel.kinomichi.base.KinomichiModelOwner;
import be.nidel.kinomichi.base.BaseController;
import be.nidel.kinomichi.pricing.Pricing;
import be.nidel.kinomichi.pricing.PricingDTO;
import be.nidel.kinomichi.pricing.PricingGroup;
import be.nidel.kinomichi.pricing.PricingGroupDTO;
import be.nidel.kinomichi.session.SessionRequestEvent;
import be.technifutur.shared.Menu;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class GatheringController extends BaseController implements KinomichiModelOwner {
    GatheringModel model = new GatheringModel();
    GatheringView view = new GatheringView(this);

    public SessionRequestEvent onSessionRequest = new SessionRequestEvent();

    public void showMenu(Menu menu) {
        view.displayUserChoices(menu);
    }

    public void sessionMenuRequest(Menu menu, Integer gatheringId) {
        if(model.isIdValid(gatheringId)) {
            Gathering gathering = model.get(gatheringId);
            //send event for session controller
            onSessionRequest.emit(new GatheringPayload(menu, gathering));
        }else{
            view.showInvalidIdError(gatheringId);
            showMenu(menu);
        }
    }

    public Gathering createGathering(CreateGatheringDTO createGatheringDTO) {
        Gathering gathering = new Gathering();
        gathering.setTitle(createGatheringDTO.title());

        List<PricingGroup> pricingGroupList = getPricingsFromDTO(createGatheringDTO.priceList());

        gathering.setPrices(pricingGroupList);
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

    public void archiveGathering(int gatheringId) {
        try {
            Gathering gathering = model.get(gatheringId);
            gathering.setArchived();
            view.showArchivedFeedback(gathering);
        } catch (NoSuchElementException ignored) {
            view.showArchivedErrorFeedback();
        }
    }

    public Optional<Gathering> getGatheringById(int gatheringId) {
        return Optional.ofNullable(model.get(gatheringId));
    }

    public Gathering updateGathering(UpdateGatheringDTO updateGatheringDTO) {
        Gathering gathering = null;
        try {
            gathering = model.get(updateGatheringDTO.id());
            gathering.setTitle(updateGatheringDTO.title());
            gathering.setPrices(getPricingsFromDTO(updateGatheringDTO.priceList()));
        }catch (NoSuchElementException ignored){
            view.showUpdateErrorFeedback();
        }
        return gathering;
    }

    public static List<PricingGroup> getPricingsFromDTO(List<PricingGroupDTO> pricingList) {
        return pricingList.stream()
                .map(pg -> new PricingGroup(
                        pg.pricingDTOList().stream()
                                .map(p -> new Pricing(p.participantType(), p.sessionType(), p.price()))
                                .toList(),
                        pg.sessionType()
                ))
                .toList();
    }

    public static List<PricingGroupDTO> getDTOFromPricingGroup(List<PricingGroup> pricingList) {
        return pricingList.stream()
                .map(pg -> new PricingGroupDTO(
                        pg.getPricingList().stream()
                                .map(p -> new PricingDTO(p.getParticipantType(), p.getSessionType(), p.getPrice()))
                                .toList(),
                        pg.getSessionType()
                ))
                .toList();
    }
}
