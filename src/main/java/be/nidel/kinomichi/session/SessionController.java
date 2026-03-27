package be.nidel.kinomichi.session;

import be.nidel.kinomichi.gathering.Gathering;
import be.nidel.kinomichi.gathering.GatheringController;
import be.technifutur.shared.Menu;

import java.util.Objects;

public class SessionController {
    private final SessionModel model = new SessionModel();
    private final SessionView view = new SessionView(this);

    //CustomEvent onSessionCreated;

    public void createSession(SessionDTO sessionDTO) {
        //assert(Objects.nonNull(currentGathering));
        Session session = new Session(
                sessionDTO.day(),
                sessionDTO.start(),
                sessionDTO.duration());

        model.addSession(session);
        //onSessionCreated.emit(session);
    }

    public void showMenu(Menu context){
        view.displayUserChoices(context);
    }
}
