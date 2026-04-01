package be.nidel.kinomichi.registration;

public class Registration {
    private int id = -1;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
