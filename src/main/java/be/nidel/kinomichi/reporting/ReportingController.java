package be.nidel.kinomichi.reporting;

import be.nidel.kinomichi.base.BaseController;
import be.nidel.kinomichi.gathering.Gathering;
import be.nidel.kinomichi.gathering.GatheringModel;
import be.nidel.kinomichi.participant.Participant;
import be.nidel.kinomichi.participant.ParticipantModel;
import be.nidel.kinomichi.registration.Registration;
import be.nidel.kinomichi.registration.RegistrationModel;
import be.nidel.kinomichi.registration.RegistrationStatus;
import be.nidel.kinomichi.session.Session;
import be.nidel.kinomichi.session.SessionModel;
import be.technifutur.shared.Menu;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReportingController extends BaseController {

    private GatheringModel gatheringModel;
    private SessionModel sessionModel;
    private ParticipantModel participantModel;
    private RegistrationModel registrationModel;
    ReportingView view = new ReportingView(this);

    //region Models mapping
    public void setGatheringModel(GatheringModel gatheringModel){
        this.gatheringModel = gatheringModel;
    }

    public void setRegistrationModel(RegistrationModel registrationModel) {
        this.registrationModel = registrationModel;
    }

    public void setParticipantModel(ParticipantModel participantModel) {
        this.participantModel = participantModel;
    }

    public void setSessionModel(SessionModel sessionModel) {
        this.sessionModel = sessionModel;
    }
    //endregion

    public void showMenu(Menu menu) {
        view.displayUserChoices(menu);
    }

    public void gatheringOverview(int gatheringId){
        try {
            Gathering gathering = gatheringModel.get(gatheringId);
            view.renderReport(gathering);
        } catch (NoSuchElementException e) {
            view.showInvalidIdError(gatheringId);
            view.refresh();
        }
    }

    public void gatheringReporting(int gatheringId) {

    }

    public List<Participant> getUnpaidParticipants() {
        List<Registration> registrations = registrationModel.getAllByStatus(RegistrationStatus.UNPAID);
        List<Participant> participants = registrations.stream()
                .map(registration -> participantModel.get(registration.getParticipantId())).distinct().toList();
        return participants;
    }

    //region data getter
    public Map<Integer, Registration> getAllRegistration(Gathering gathering){
        Map<Integer, Registration> registrationMap = gathering.getAllSessions()
                .stream()
                .flatMap(session -> getRegistrationBySession(session).stream())
                .collect(Collectors.toMap(
                        Registration::getId,
                        Function.identity()));
        return registrationMap;
    }

    public List<Registration> getRegistrationBySession(Session session){
        return registrationModel.getAllRegistrationBySessionId(session.getId());
    }

    public List<Registration> getRegistrationByParticipant(Participant participant) {
        return registrationModel.getAllRegistrationByParticipantId(participant.getId());
    }

    public List<Registration> getUnpaidRegistrationByParticipant(Participant participant) {
        return registrationModel.getUnpaidRegistrationsByParticipantId(participant.getId());
    }

    public Map<Integer, Gathering> getGatheringMap() {
        return gatheringModel.getAllGathering();
    }

    public Map<Integer, Session> getSessionMap() {
        return sessionModel.getAllSession();
    }
    //endregion
}