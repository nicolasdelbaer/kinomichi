package be.nidel.kinomichi.participant;

import java.util.ArrayList;
import java.util.List;

public class ParticipantModel {

    private List<Participant> participantList = new ArrayList<>();

    public List<Participant> fetchAllParticipant() {
        return participantList.stream().toList();
    }

    public boolean addParticipant(Participant participant) {
        try{
            participantList.add(participant);
        } catch (Exception e) {
            //TODO throw custom exception or handle error
            return false;
        }
        return true;
    }
}
