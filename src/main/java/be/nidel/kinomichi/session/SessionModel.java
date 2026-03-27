package be.nidel.kinomichi.session;

import be.nidel.kinomichi.participant.ParticipantModel;
import be.nidel.kinomichi.participant.ParticipantView;
import be.nidel.utils.OutputUtils;
import be.technifutur.shared.Menu;

public class SessionModel {

    public void addSession(Session session) {
        OutputUtils.sOutInfo(session.toString());
    }
}
