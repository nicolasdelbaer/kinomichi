package be.nidel.kinomichi.session;

import java.util.Arrays;
import java.util.Optional;

public enum SessionType {
    Exhibition(100),
    Dinner(200),
    Accommodation(300)
    ;

    private final int value;

    SessionType(int i) {
        value = i;
    }

    public static Optional<SessionType> getByValue(int val){
        return Arrays.stream(values())
                .filter(p -> p.ordinal() == val)
                .findFirst();
    }
}
