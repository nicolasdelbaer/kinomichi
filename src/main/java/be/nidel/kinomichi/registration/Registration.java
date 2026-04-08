package be.nidel.kinomichi.registration;

import be.nidel.kinomichi.base.BaseEntity;

import java.io.Serializable;

public class Registration extends BaseEntity implements Serializable {
    private RegistrationStatus status = RegistrationStatus.REGISTERED;
    private int participantId;
    private int sessionId;
    private int priceId;

    public RegistrationStatus getStatus() {
        return status;
    }

    public void setStatus(RegistrationStatus status) {
        this.status = status;
    }

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getPriceId() {
        return priceId;
    }

    public void setPriceId(int priceId) {
        this.priceId = priceId;
    }

    @Override
    public String toString() {
        return "Registration{" +
                "id=" + id +
                ", status=" + status.name() +
                ", participantId=" + participantId +
                ", sessionId=" + sessionId +
                ", priceId=" + priceId +
                '}';
    }
}
