package be.nidel.kinomichi.session;

import be.nidel.kinomichi.base.BaseController;
import be.nidel.kinomichi.base.KinomichiModelOwner;
import be.nidel.kinomichi.gathering.Gathering;
import be.nidel.kinomichi.participant.Participant;
import be.nidel.kinomichi.participant.ParticipantModel;
import be.technifutur.shared.Menu;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

public class SessionController extends BaseController implements KinomichiModelOwner {
    private ParticipantModel participantModel;
    private final SessionModel model = new SessionModel();
    private final SessionView view = new SessionView(this);

    public Session createSession(CreateSessionDTO createSessionDTO, Gathering gathering) {
        Session session = null;
        if(Objects.nonNull(gathering))
        {
            session = new Session(
                    gathering.getId(),
                    createSessionDTO.title(),
                    createSessionDTO.description(),
                    createSessionDTO.day(),
                    createSessionDTO.start(),
                    createSessionDTO.duration(),
                    createSessionDTO.type());

            model.addSession(session);
            gathering.addNewSession(session);
        }else{
            view.showUnknownGatheringError();
            view.refresh();
        }
        return session;
    }

    public void setParticipantModel(ParticipantModel model){
        participantModel = model;
    }

    //helper method
    public void batchSessionCreation(CreateSessionDTO createSessionDTO, Gathering gathering, int numberOfSessions) {
        LocalTime startingTime = createSessionDTO.start();
        for (int i = 0; i <numberOfSessions; i++) {
            Session session = new Session(
                    gathering.getId(),
                    createSessionDTO.title(),
                    createSessionDTO.description(),
                    createSessionDTO.day(),
                    startingTime,
                    createSessionDTO.duration(),
                    createSessionDTO.type());

            model.addSession(session);
            gathering.addNewSession(session);
            startingTime = startingTime.plusMinutes(createSessionDTO.duration());
        }
    }

    public void showManageMenu(Menu context, Gathering gathering) {
        view.setGatheringData(gathering);
        view.displayUserChoices(context);
    }

    public List<Session> getAllSessions() {
        return model.getAllSession().values().stream().toList();
    }

    public SessionModel getModel() {
        return model;
    }

    public void archiveSession(int sessionId) {
        try {
            Session session = model.get(sessionId);
            session.setArchived();
            view.showArchivedFeedback(session);
        } catch (NoSuchElementException ignored) {
            view.showArchivedErrorFeedback();
        }
    }

    public Optional<Session> getSessionById(int sessionId) {
        return Optional.ofNullable(model.get(sessionId));
    }

    public Session updateSession(UpdateSessionDTO updateSessionDTO) {
        Session session = null;
        try {
            session = model.get(updateSessionDTO.id());
            session.setOrganizer(updateSessionDTO.organizer());
            session.setTitle(updateSessionDTO.title());
            session.setDescription(updateSessionDTO.description());
            session.setSessionType(updateSessionDTO.type());
            session.setStart(updateSessionDTO.start());
            session.setDuration(updateSessionDTO.duration());

        }catch (NoSuchElementException ignored){
            view.showUpdateErrorFeedback();
        }
        return session;
    }

    public Optional<Participant> getParticipantById(int participantId) {
        return Optional.ofNullable(participantModel.get(participantId));
    }
}
