package be.nidel.kinomichi.gathering.renderer;

import be.nidel.kinomichi.gathering.Gathering;
import be.nidel.kinomichi.session.renderer.RendererSessionDTO;

import java.util.List;

public record RendererGatheringDTO(
        Gathering gathering,
        List<RendererSessionDTO> sessions
) {}