package be.nidel.kinomichi.gathering.renderer;

import be.nidel.kinomichi.pricing.renderer.PricingRenderer;
import be.nidel.kinomichi.session.renderer.RendererSessionDTO;
import be.nidel.kinomichi.session.renderer.SessionRenderer;
import be.nidel.utils.OutputUtils;

import java.util.List;

public class GatheringRenderer{

    public void render(RendererGatheringDTO rendererGatheringDTO) {
        new GatheringInfoRenderer().render(rendererGatheringDTO.gathering());
        SessionRenderer sessionRenderer = new SessionRenderer();
        List<RendererSessionDTO> allSessions = rendererGatheringDTO.sessions();
        if(allSessions.isEmpty()){
            OutputUtils.sOutWarning("No sessions");
        }else{
            for (RendererSessionDTO session : allSessions)
                sessionRenderer.render(session);
        }
        new PricingRenderer().render(rendererGatheringDTO.gathering().getPriceGroupList());
    }
}
