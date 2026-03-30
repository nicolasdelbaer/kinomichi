package be.nidel.kinomichi.participant;

import be.nidel.kinomichi.KinomichiModel;
import be.nidel.kinomichi.gathering.Gathering;
import be.nidel.utils.OutputUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParticipantModel implements KinomichiModel {

    private Map<Integer, Participant> participantList = new HashMap<>();

    public boolean isIdValid(Integer instanceId) {
        return participantList.containsKey(instanceId);
    }

    public void addParticipant(Participant participant) {
        try{
            //NOTE manual autoincrement because stuff is not "really deleted" but archived
            int id = participantList.size()+1;
            participantList.put(id, participant);
        } catch (Exception e) {
            OutputUtils.sOutError(e.getMessage());
        }
    }

    public Participant get(Integer participantId) {
        return participantList.get(participantId);
    }
    public Map<Integer,Participant> getAllParticipant() {
        return participantList;
    }
}
