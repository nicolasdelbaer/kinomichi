package be.nidel.kinomichi.participant;

import java.util.Arrays;
import java.util.Optional;

public enum ParticipantType {
    Attendee,
    Trainer,
    Sensei;

    public static Optional<ParticipantType> getByOrdinal(int ord){
        return Arrays.stream(values())
                .filter(p -> p.ordinal() == ord)
                .findFirst();
    }
}
