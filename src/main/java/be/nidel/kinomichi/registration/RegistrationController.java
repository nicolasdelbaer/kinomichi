package be.nidel.kinomichi.registration;

import be.nidel.kinomichi.KinomichiController;
import be.nidel.kinomichi.KinomichiModel;
import be.nidel.kinomichi.participant.Participant;
import be.nidel.kinomichi.participant.ParticipantModel;
import be.nidel.kinomichi.session.Session;
import be.nidel.kinomichi.session.SessionModel;
import be.technifutur.shared.Menu;

import java.util.List;

public class RegistrationController implements KinomichiController {
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

        if(participantValid && sessionValid){
            registration = new Registration();
            registration.setParticipantId(registrationDTO.participantId());
            registration.setSessionId(registrationDTO.sessionId());
            registration.setStatus(registrationDTO.status());
            registration.setPriceId(registrationDTO.priceId());
            model.addRegistration(registration);

            Participant participant = participantModel.get(registrationDTO.participantId());
            Session session = sessionModel.get(registrationDTO.sessionId());
            view.showRegistrationFeedback(participant, session, registration);
            //System.out.println(registrationModel.getAllRegistration());
        }else{
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
    public KinomichiModel getModel() {
        return null;
    }
}
