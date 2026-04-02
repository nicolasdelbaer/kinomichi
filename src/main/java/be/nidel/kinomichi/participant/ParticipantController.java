package be.nidel.kinomichi.participant;

import be.nidel.kinomichi.base.BaseController;
import be.nidel.kinomichi.base.KinomichiModelOwner;
import be.technifutur.shared.Menu;

import java.util.List;

public class ParticipantController extends BaseController implements KinomichiModelOwner {

    private final ParticipantModel model = new ParticipantModel();
    private final ParticipantView view = new ParticipantView(this);

    public List<Participant> getAllParticipants(){
        return model.getAllParticipant().values().stream().toList();
    }

    public Participant createParticipant(ParticipantDTO participantDTO){
        Participant participant = new Participant.Builder()
                .setFirstName(participantDTO.firstName())
                .setLastName(participantDTO.lastName())
                .setPhone(participantDTO.phone())
                .setEmail(participantDTO.email())
                .setClubName(participantDTO.clubName())
                .setParticipantType(participantDTO.type())
                .build();

        model.addParticipant(participant);
        return participant;
    }

    public Participant updateParticipant(int participantId, ParticipantDTO participantDTO) {
        Participant participant = model.get(participantId);
        participant.setFirstName(participantDTO.firstName());
        participant.setLastName(participantDTO.lastName());
        participant.setPhone(participantDTO.phone());
        participant.setEmail(participantDTO.email());
        participant.setClubName(participantDTO.clubName());
        participant.setParticipantType(participantDTO.type());
        return participant;
    }

    public void showMenu(Menu context) {
        view.displayUserChoices(context);
    }

    public ParticipantModel getModel() {
        return model;
    }

    public Participant getParticipantById(Integer participantId) {
        return model.get(participantId);
    }
}
