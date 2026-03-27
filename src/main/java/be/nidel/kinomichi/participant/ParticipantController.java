package be.nidel.kinomichi.participant;

import be.technifutur.shared.Menu;

import java.util.List;

public class ParticipantController {

    private final ParticipantModel model = new ParticipantModel();
    private final ParticipantView view = new ParticipantView(this);

    public List<Participant> getAllParticipant(){
        return model.fetchAllParticipant();
    }

    public void createParticipant(ParticipantDTO input){
        Participant participant = new Participant.Builder()
                .setFirstName(input.firstName())
                .setLastName(input.lastName())
                .setPhone(input.phone())
                .setEmail(input.email())
                .setClubName(input.clubName())
                .setType(input.type())
                .build();

        model.addParticipant(participant);
    }

    public void showMenu(Menu context) {
        view.displayUserChoices(context);
    }
}
