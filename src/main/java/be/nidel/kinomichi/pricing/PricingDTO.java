package be.nidel.kinomichi.pricing;

import be.nidel.kinomichi.participant.ParticipantType;
import be.nidel.kinomichi.session.SessionType;

import java.math.BigDecimal;

public record PricingDTO(
ParticipantType participantType,
        SessionType sessionType,
        BigDecimal price) {
}
