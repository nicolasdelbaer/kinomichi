package be.nidel.kinomichi.participant;

import be.nidel.kinomichi.base.BaseController;
import be.nidel.kinomichi.base.KinomichiModelOwner;
import be.technifutur.shared.Menu;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

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
        Participant participant = null;
        try {
            participant = model.get(participantId);
            participant.setFirstName(participantDTO.firstName());
            participant.setLastName(participantDTO.lastName());
            participant.setPhone(participantDTO.phone());
            participant.setEmail(participantDTO.email());
            participant.setClubName(participantDTO.clubName());
            participant.setParticipantType(participantDTO.type());
        } catch (NoSuchElementException ignored) {
            view.showUpdateErrorFeedback();}
        return participant;
    }

    public void showMenu(Menu context) {
        view.displayUserChoices(context);
    }

    public ParticipantModel getModel() {
        return model;
    }

    public Optional<Participant> getParticipantById(Integer participantId) {
        Optional<Participant> participant = Optional.empty();
        try {
            participant = Optional.ofNullable(model.get(participantId));
        } catch (Exception ignored) {
        }
        return participant;
    }

    public void archiveParticipant(Integer participantId) {
        try {
            Participant participant = model.get(participantId);
            participant.setArchived();
            view.showArchivedFeedback(participant);
        } catch (NoSuchElementException ignored) {
            view.showArchivedErrorFeedback();
        }
    }
}
