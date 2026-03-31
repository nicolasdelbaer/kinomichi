package be.nidel.kinomichi.pricing;

import be.nidel.kinomichi.participant.Participant;
import be.nidel.kinomichi.participant.ParticipantType;
import be.nidel.kinomichi.session.SessionType;

import java.math.BigDecimal;

public class Pricing {
    SessionType sessionType;
    ParticipantType participantType;
    BigDecimal price = new BigDecimal("15");

    public Pricing(ParticipantType participantType, SessionType sessionType,  BigDecimal price) {
        this.sessionType = sessionType;
        this.participantType = participantType;
        this.price = price;
    }

    public SessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public ParticipantType getParticipantType() {
        return participantType;
    }

    public void setParticipantType(ParticipantType participantType) {
        this.participantType = participantType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
