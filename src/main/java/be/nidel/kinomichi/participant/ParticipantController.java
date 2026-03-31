package be.nidel.kinomichi.participant;

import be.nidel.kinomichi.KinomichiController;
import be.technifutur.shared.Menu;

import java.util.List;

public class ParticipantController implements KinomichiController {

    private final ParticipantModel model = new ParticipantModel();
    private final ParticipantView view = new ParticipantView(this);

    public List<Participant> getAllParticipants(){
        return model.getAllParticipant().values().stream().toList();
    }

    public Participant createParticipant(ParticipantDTO input){
        Participant participant = new Participant.Builder()
                .setFirstName(input.firstName())
                .setLastName(input.lastName())
                .setPhone(input.phone())
                .setEmail(input.email())
                .setClubName(input.clubName())
                .setType(input.type())
                .build();

        model.addParticipant(participant);
        return participant;
    }

    public void showMenu(Menu context) {
        view.displayUserChoices(context);
    }

    public ParticipantModel getModel() {
        return model;
    }
}
