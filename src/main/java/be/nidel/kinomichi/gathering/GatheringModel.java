package be.nidel.kinomichi.gathering;

import be.nidel.kinomichi.base.KinomichiModel;
import be.nidel.utils.OutputUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class GatheringModel implements KinomichiModel {
    private Map<Integer, Gathering> gatheringList = new HashMap<>();

    public boolean isIdValid(Integer instanceId) {
        return gatheringList.containsKey(instanceId);
    }

    public boolean addGathering(Gathering gathering) {
        try{
            //NOTE manual autoincrement because stuff is not "really deleted" but archived
            int id = gatheringList.size()+1;
            gatheringList.put(id, gathering);
        } catch (Exception e) {
            OutputUtils.sOutError(e.getMessage());
            return false;
        }
        return true;
    }

    public Gathering get(Integer gatheringId) {
        if(!gatheringList.containsKey(gatheringId))
            throw new NoSuchElementException("Gathering Id doesn't exist");
        return gatheringList.get(gatheringId);
    }
    public Map<Integer, Gathering> getAllGathering() {
        return gatheringList;
    }
}
