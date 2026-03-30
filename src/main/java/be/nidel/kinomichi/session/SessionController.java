package be.nidel.kinomichi.session;

import be.nidel.kinomichi.gathering.GatheringPayload;
import be.nidel.utils.OutputUtils;
import be.technifutur.shared.Menu;

import java.util.Objects;

public class SessionController {
    private final SessionModel model = new SessionModel();
    private final SessionView view = new SessionView(this);

    GatheringPayload gatheringPayload;

    public Session createSession(SessionDTO sessionDTO) {
        Session session = null;
        if(Objects.nonNull(gatheringPayload))
        {
            session = new Session(
                    sessionDTO.day(),
                    sessionDTO.start(),
                    sessionDTO.duration());

            model.addSession(session);
            gatheringPayload.gathering().addNewSession(session);
        }else{
            OutputUtils.sOutError("Unknown gathering");
            view.refresh();
        }
        return session;
    }

    public void showMenu(Menu context){
        view.displayUserChoices(context);
    }

    public void showManageMenu(GatheringPayload gatheringPayload) {
        this.gatheringPayload = gatheringPayload;
        showMenu(gatheringPayload.context());
    }
}
