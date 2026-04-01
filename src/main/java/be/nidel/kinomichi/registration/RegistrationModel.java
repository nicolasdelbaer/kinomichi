package be.nidel.kinomichi.registration;

import be.nidel.kinomichi.base.KinomichiModel;
import be.nidel.utils.OutputUtils;

import java.util.HashMap;
import java.util.List;
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
            registration.setId(id);
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

    public boolean hasEntry(int participantId, int sessionId) {
        return registrationList.values().stream().anyMatch(r -> r.getSessionId() == sessionId && r.getParticipantId() == participantId);
    }

    public List<Registration> getAllByStatus(RegistrationStatus status){
        return registrationList.values().stream().filter(r -> r.getStatus() == status).toList();
    }

    public List<Registration> getAllRegistrationBySessionId(int sessionId) {
        return registrationList
                .values()
                .stream()
                .filter(registration -> registration.getSessionId() == sessionId)
                .toList();
    }

    public List<Registration> getAllRegistrationByParticipantId(int participantId) {
        return registrationList
                .values()
                .stream()
                .filter(registration -> registration.getParticipantId() == participantId)
                .toList();
    }

    public List<Registration> getUnpaidRegistrationsByParticipantId(int participantId) {
        return getAllRegistrationByParticipantId(participantId).stream().filter(r -> r.getStatus() == RegistrationStatus.UNPAID).toList();
    }
}
