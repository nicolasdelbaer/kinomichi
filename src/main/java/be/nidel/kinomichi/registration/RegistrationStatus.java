package be.nidel.kinomichi.registration;

import be.nidel.kinomichi.participant.ParticipantType;

import java.util.Arrays;
import java.util.Optional;

public enum RegistrationStatus {
    REGISTERED, //registered
    WITHDRAWN, //was registered but cancelled before the event
    PAYMENT_PENDING, //has participated and must pay
    PAYED, //has participated and has paid
    CANCELLED; //was registered and didn't participate

    public static Optional<RegistrationStatus> getByOrdinal(int ord){
        return Arrays.stream(values())
                .filter(p -> p.ordinal() == ord)
                .findFirst();
    }
}
