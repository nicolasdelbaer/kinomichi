package be.nidel.kinomichi.session.renderer;

import be.nidel.kinomichi.participant.Participant;
import be.nidel.kinomichi.registration.RegistrationStatus;
import be.nidel.kinomichi.session.Session;
import be.nidel.utils.OutputUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SessionRenderer {

    public void render(RendererSessionDTO data) {
        Session session = data.session();
        String orgaName = session.getOrganizer().map(Participant::getFullName).orElse(OutputUtils.ANSI_YELLOW+ "n/a"+OutputUtils.ANSI_PURPLE);
        OutputUtils.sOut(OutputUtils.STYLISABLE_LINE.formatted(
                OutputUtils.ANSI_PURPLE,
                session.getSessionType().emoji() + " Organizer: "+ orgaName + " - "+ session.getSessionType().name(),
                OutputUtils.ANSI_RESET));
        OutputUtils.sOut(OutputUtils.STYLISABLE_LINE.formatted(
                OutputUtils.ANSI_BLUE,
                "Date: " +session.getDay()+ " | "+ session.getStart()+ " -> "+ session.getEnd(),
                OutputUtils.ANSI_RESET));

        if(data.sessionAttendees().isEmpty())
        {
            OutputUtils.sOutWarning("No attendees");
        }else{

            //ORDER PARTICIPANTS BY STATUS
            Map<RegistrationStatus, List<Participant>> attendeesBySessionStatus = data.sessionAttendees().stream()
                    .collect(Collectors.groupingBy(
                            p -> data.registrationsByParticipant().get(p.getId()).getStatus(),
                            Collectors.toList()));

            //DISPLAY ATTENDEE LIST
            attendeesBySessionStatus.forEach((key, value) -> value.forEach(attendee -> {
                String attendeeInfo = "\t%s%s%s".formatted(
                        key.name(),
                        " - ",
                        "%s (%s | id: %s)".formatted(attendee.getFullName(), attendee.getParticipantType().name(), attendee.getId())
                );

                switch (key) {
                    case UNPAID ->
                            OutputUtils.sOut(OutputUtils.STYLISABLE_LINE.formatted(OutputUtils.ANSI_RED, attendeeInfo, OutputUtils.ANSI_RESET));
                    case PAID ->
                            OutputUtils.sOut(OutputUtils.STYLISABLE_LINE.formatted(OutputUtils.ANSI_GREEN, attendeeInfo, OutputUtils.ANSI_RESET));
                    case CANCELLED, WITHDRAWN ->
                            OutputUtils.sOut(OutputUtils.STYLISABLE_LINE.formatted(OutputUtils.ANSI_WHITE_ITALIC, attendeeInfo, OutputUtils.ANSI_RESET));
                    default -> OutputUtils.sOut(OutputUtils.DEFAULT_LINE.formatted(attendeeInfo));
                }
            }));
        }

        System.out.println();
    }
}
