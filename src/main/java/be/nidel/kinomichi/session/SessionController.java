package be.nidel.kinomichi.session;

import be.nidel.kinomichi.gathering.Gathering;
import be.nidel.kinomichi.gathering.GatheringPayload;
import be.nidel.utils.OutputUtils;
import be.technifutur.shared.Menu;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class SessionController {
    private final SessionModel model = new SessionModel();
    private final SessionView view = new SessionView(this);

    public Session createSession(SessionDTO sessionDTO, Gathering gathering) {
        Session session = null;
        if(Objects.nonNull(gathering))
        {
            session = new Session(
                    sessionDTO.day(),
                    sessionDTO.start(),
                    sessionDTO.duration());

            model.addSession(session);
            gathering.addNewSession(session);
        }else{
            OutputUtils.sOutError("Unknown gathering");
            view.refresh();
        }
        return session;
    }

    //helper method
    public void batchSessionCreation(SessionDTO sessionDTO, Gathering gathering, int numberOfSessions) {
        LocalTime startingTime = sessionDTO.start();
        for (int i = 0; i <numberOfSessions; i++) {
            Session session = new Session(sessionDTO.day(), startingTime, sessionDTO.duration());

            model.addSession(session);
            gathering.addNewSession(session);
            startingTime = startingTime.plusMinutes(sessionDTO.duration());
        }
    }

    public void showMenu(Menu context){
        view.displayUserChoices(context);
    }

    //TODO make gathering immutable?
    public void showManageMenu(Menu context, Gathering gathering) {
        //NOTE temporary usage of "display user choices" before proper menu
        view.displayUserChoices(context);
        view.setGatheringData(gathering);

    }

    public List<Session> getAllSessions() {
        return model.fetchAllSession();
    }
}
