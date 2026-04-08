package be.nidel.kinomichi.participant;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ParticipantGroup implements Serializable {

    private List<Participant> participantList = new ArrayList<>();

    public boolean add(Participant participant) {
        return participantList.add(participant);
    }

    public boolean remove(Participant participant) {
        return participantList.remove(participant);
    }


    @Override
    public String toString() {
        return "GroupParticipant{" +
                "participantList=" + participantList +
                '}';
    }

    public List<Participant> toList() {
        return participantList.stream().toList();
    }
}
