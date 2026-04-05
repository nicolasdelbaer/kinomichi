package be.nidel.kinomichi.gathering.renderer;

import be.nidel.kinomichi.session.Session;

import java.util.List;

public record RendererSessionDTOLite(
        List<Session> sessions
) {}