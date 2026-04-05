package be.nidel.kinomichi.gathering.renderer;

import be.nidel.kinomichi.pricing.renderer.PricingRenderer;
import be.nidel.kinomichi.session.renderer.RendererSessionDTO;
import be.nidel.kinomichi.session.renderer.SessionRenderer;

public class GatheringRenderer{

    public void render(RendererGatheringDTO rendererGatheringDTO) {
        new GatheringInfoRenderer().render(rendererGatheringDTO.gathering());
        SessionRenderer sessionRenderer = new SessionRenderer();
        for (RendererSessionDTO session : rendererGatheringDTO.sessions())
            sessionRenderer.render(session);
        new PricingRenderer().render(rendererGatheringDTO.gathering().getPriceGroupList());
    }
}
