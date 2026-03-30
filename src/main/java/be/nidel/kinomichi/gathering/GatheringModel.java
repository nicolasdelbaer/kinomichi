package be.nidel.kinomichi.gathering;

import be.nidel.kinomichi.KinomichiModel;
import be.nidel.kinomichi.session.Session;
import be.nidel.utils.OutputUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GatheringModel implements KinomichiModel {
    private Map<Integer, Gathering> gatheringList = new HashMap<>();

    public boolean isIdValid(Integer instanceId) {
        return gatheringList.containsKey(instanceId);
    }
    public Map<Integer, Gathering> fetchAllGathering() {
        return gatheringList;
    }

    public boolean addGathering(Gathering gathering) {
        try{
            //NOTE manual autoincrement because stuff is not "really deleted" but archived
            int id = gatheringList.size()+1;
            gatheringList.put(id, gathering);
            System.out.println(gatheringList);
        } catch (Exception e) {
            OutputUtils.sOutError(e.getMessage());
            return false;
        }
        return true;
    }

    public void addSession(Integer id, Session session) {
        Gathering gathering = gatheringList.get(id);
        if(Objects.nonNull(gathering)){
            gathering.addNewSession(session);
        }
    }

    public Gathering get(Integer gatheringId) {
        return gatheringList.get(gatheringId);
    }
}
