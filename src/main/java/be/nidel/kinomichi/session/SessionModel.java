package be.nidel.kinomichi.session;

import be.nidel.kinomichi.base.KinomichiModel;
import be.nidel.utils.OutputUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class SessionModel implements KinomichiModel {
    private Map<Integer, Session> sessionList = new HashMap<>();
    public boolean isIdValid(Integer instanceId) {
        return sessionList.containsKey(instanceId);
    }
    public List<Session> getAllSession() {
        return sessionList.values().stream().toList();
    }

    public void addSession(Session session) {
        try{
            //NOTE manual autoincrement because stuff is not "really deleted" but archived
            int id = sessionList.size()+1;
            session.setId(id);
            sessionList.put(id, session);
        } catch (Exception e) {
            OutputUtils.sOutError(e.getMessage());
        }
    }
    public Session get(Integer sessionId) {
        if(!sessionList.containsKey(sessionId))
            throw new NoSuchElementException("Session Id doesn't exist");
        return sessionList.get(sessionId);
    }


}
