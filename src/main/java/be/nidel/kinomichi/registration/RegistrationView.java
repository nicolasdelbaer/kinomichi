package be.nidel.kinomichi.registration;

import be.nidel.kinomichi.base.BaseView;
import be.nidel.kinomichi.participant.Participant;
import be.nidel.kinomichi.session.Session;
import be.nidel.utils.OutputUtils;
import be.nidel.utils.menu.MenuController;
import be.nidel.utils.menu.MenuFactory;
import be.technifutur.shared.Menu;

import java.util.Objects;
import java.util.logging.Logger;

public class RegistrationView extends BaseView<RegistrationController> {

    protected final Logger logger = Logger.getLogger("Kinomichi");

    MenuController menu;

    public RegistrationView(RegistrationController controller) {
        super(controller);
    }

    //TODO refactor this method by splitting :)
    public void displayUserChoices(Menu context){
        this.menuContext = context;

        OutputUtils.sOutInfo(
                """
                     —————————————————————————————————————————————————————
                     Code Status:                                        
                        1 -> Registered                                  
                        2 -> Withdrawn - cancelled before the event      
                        3 -> Participation confirmed - need to pay       
                        4 -> Payment done!                               
                        5 -> Cancelled - didn't participate              
                    —————————————————————————————————————————————————————
                    """);
        OutputUtils.sOutTitle( "      Enter: participant, session and status         ");

        MenuController menu = MenuFactory.backQuitTemplate(context);
        menu.addRegexItem("Test regex",
                        "[0-9];[0-9];[0-9]",
                        () -> this.treatRegistrationInput(menu))
            .setInteractionMessage("                  Format: 1;4;1                      \n");
        menu.renderAndInteract();
    }

    private void treatRegistrationInput(MenuController menu) {
        String entry = menu.getLastEntry();

        Integer idParticipant = null;
        Integer idSession = null;
        Integer idStatus = null;
        RegistrationStatus status = null;
        int idPrice = 0;

        boolean isValid = false;
        boolean inputValid = false;

        String[] split = entry.split(";");

        if(split.length >= 3){
            try{
                idParticipant = Integer.parseInt(split[0].trim());
                idSession = Integer.parseInt(split[1].trim());
                idStatus = Integer.parseInt(split[2].trim());
                status = RegistrationStatus.getByOrdinal(idStatus).get();
                inputValid = true;
            }catch (RuntimeException e){
                OutputUtils.sOutWarning("Bad arguments, please try again");
                OutputUtils.sOutWarning("Participant ID; Session ID; statusCode");
            }

            if(inputValid) {
                //Sanity check for ids
                Registration result = controller.createRegistration(new RegistrationDTO(
                        status,
                        idParticipant,
                        idSession,
                        idPrice
                ));

                if (Objects.nonNull(result))
                    isValid = true;
            }
        }else{
            OutputUtils.sOutWarning("Please enter data with this format:");
            OutputUtils.sOutWarning("Participant ID; Session ID; statusCode");
        }

        if(!isValid)
            menu.interact();
        else
            continueAddingSession();

    }

    public void continueAddingSession(){
        MenuFactory.confirmTemplate(menuContext, () -> displayUserChoices(menuContext))
                .setInteractionMessage("Continue ? (y/n)")
                .renderAndInteract();
    }

    public void displayParticipantError(int id) {
        OutputUtils.sOutError("Wrong participant id: " + id);
    }

    public void displaySessionError(int id) {
        OutputUtils.sOutError("Wrong session id: " + id);
    }

    public void showRegistrationFeedback(Participant participant, Session session, Registration registration) {
        OutputUtils.sOutWarning("%s %s \"%s\" %s %s [%s -> %s]".formatted(
                participant.getFullName(),
                "has been registered with status",
                registration.getStatus().name(),
                "for session:",
                session.getDay(),
                session.getStart(),
                session.getEnd()
                ));
    }

    public void displayAlreadyExistingEntry(int participantId, int sessionId) {
        OutputUtils.sOutError("Already existing registering for session id: %s && participant id: %s ".formatted(participantId, sessionId));
        OutputUtils.sOutError("Please consider editing the entry first");
    }
}
