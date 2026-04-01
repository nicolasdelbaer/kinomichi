package be.nidel.kinomichi.registration;

import be.nidel.kinomichi.base.BaseController;
import be.nidel.kinomichi.base.KinomichiModelOwner;
import be.nidel.kinomichi.participant.Participant;
import be.nidel.kinomichi.participant.ParticipantModel;
import be.nidel.kinomichi.session.Session;
import be.nidel.kinomichi.session.SessionModel;
import be.technifutur.shared.Menu;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrationController extends BaseController implements KinomichiModelOwner {
    private final Logger logger = Logger.getLogger(getClass().getName());

    RegistrationModel model = new RegistrationModel();
    RegistrationView view = new RegistrationView(this);

    ParticipantModel participantModel;
    SessionModel sessionModel;

    public void showMenu(Menu menu) {
        view.displayUserChoices(menu);
    }

    public Registration createRegistration(RegistrationDTO registrationDTO) {
        Registration registration = null;
        //sanity check
        boolean participantValid = participantModel.isIdValid(registrationDTO.participantId());
        boolean sessionValid = sessionModel.isIdValid(registrationDTO.sessionId());
        logger.finest("creating new registration");

        if(participantValid && sessionValid){

            if(!model.hasEntry(registrationDTO.participantId(), registrationDTO.sessionId())){
                registration = new Registration();
                registration.setParticipantId(registrationDTO.participantId());
                registration.setSessionId(registrationDTO.sessionId());
                registration.setStatus(registrationDTO.status());
                registration.setPriceId(registrationDTO.priceId());
                model.addRegistration(registration);

                Participant participant = participantModel.get(registrationDTO.participantId());
                Session session = sessionModel.get(registrationDTO.sessionId());
                session.addAttendee(participant);

                view.showRegistrationFeedback(participant, session, registration);
            }else{
                view.displayAlreadyExistingEntry(registrationDTO.participantId(), registrationDTO.sessionId());
            }


        }else{
            logger.warning("participantId|sessionId not found");
            if(!participantValid)
                view.displayParticipantError(registrationDTO.participantId());
            if(!sessionValid)
                view.displaySessionError(registrationDTO.sessionId());
        }
        return registration;
    }
    public List<Registration> getAllRegistrations(){
        return model.getAllRegistration().values().stream().toList();
    }

    public void setModels(ParticipantModel participantModel, SessionModel sessionModel) {
        this.participantModel = participantModel;
        this.sessionModel = sessionModel;
    }

    @Override
    public RegistrationModel getModel() {
        return model;
    }
}
