package be.nidel.kinomichi.participant;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;

public enum ParticipantType implements Serializable {
    Attendee(100, "🙎"),
    Trainer(200, "🧘"),
    Sensei(400, "🏅"),
    VIP(800, "🚨")
    ;

    private final int value;
    private final String emoji;
    ParticipantType(int i, String emoji) {
        value = i;
        this.emoji = emoji;
    }

    public static Optional<ParticipantType> getByValue(int val){
        return Arrays.stream(values())
                .filter(p -> p.ordinal() == val)
                .findFirst();
    }

    public String emoji() {
        return emoji;
    }
}
