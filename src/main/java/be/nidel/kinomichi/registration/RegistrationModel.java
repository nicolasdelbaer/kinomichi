package be.nidel.kinomichi.registration;

import be.nidel.kinomichi.KinomichiModel;
import be.nidel.utils.OutputUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class RegistrationModel implements KinomichiModel {
    private Map<Integer, Registration> registrationList = new HashMap<>();

    public boolean isIdValid(Integer instanceId) {
        return registrationList.containsKey(instanceId);
    }

    public boolean addRegistration(Registration registration) {
        try{
            //NOTE manual autoincrement because stuff is not "really deleted" but archived
            int id = registrationList.size()+1;
            registrationList.put(id, registration);
        } catch (Exception e) {
            OutputUtils.sOutError(e.getMessage());
            return false;
        }
        return true;
    }

    public Registration get(Integer registrationId) {
        if(!registrationList.containsKey(registrationId))
            throw new NoSuchElementException("Registration Id doesn't exist");
        return registrationList.get(registrationId);
    }
    public Map<Integer, Registration> getAllRegistration() {
        return registrationList;
    }
}
