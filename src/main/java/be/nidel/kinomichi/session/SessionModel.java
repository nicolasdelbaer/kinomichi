package be.nidel.kinomichi.session;

import be.nidel.utils.OutputUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionModel {
    private Map<Integer, Session> sessionList = new HashMap<>();
    public boolean isIdValid(Integer instanceId) {
        return sessionList.containsKey(instanceId);
    }
    public List<Session> fetchAllSession() {
        return sessionList.values().stream().toList();
    }


    public void addSession(Session session) {
        try{
            //NOTE manual autoincrement because stuff is not "really deleted" but archived
            int id = sessionList.size()+1;
            sessionList.put(id, session);
        } catch (Exception e) {
            OutputUtils.sOutError(e.getMessage());
        }
    }
}
