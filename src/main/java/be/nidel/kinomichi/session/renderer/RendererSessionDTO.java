package be.nidel.kinomichi.session.renderer;

import be.nidel.kinomichi.participant.Participant;
import be.nidel.kinomichi.registration.Registration;
import be.nidel.kinomichi.session.Session;

import java.util.List;
import java.util.Map;

public record RendererSessionDTO(
        Session session,
        List<Participant> sessionAttendees,
        Map<Integer, Registration> registrationsByParticipant
) {};
