package be.nidel.kinomichi.participant;

import java.util.ArrayList;
import java.util.List;

public class ParticipantGroup {

    public boolean add(Participant participant) {
        return participantList.add(participant);
    }

    public boolean remove(Participant participant) {
        return participantList.remove(participant);
    }

    private List<Participant> participantList = new ArrayList<>();

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
