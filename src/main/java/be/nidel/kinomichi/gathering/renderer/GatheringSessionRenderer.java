package be.nidel.kinomichi.gathering.renderer;

import be.nidel.kinomichi.participant.Participant;
import be.nidel.kinomichi.session.Session;
import be.nidel.utils.OutputUtils;


public class GatheringSessionRenderer {

    public static String FORMAT = "%9s | %-20s | %-18s %-20s %10s | %5s -> %-5s";
    public void render(Session session) {
        String orgaName = session.getOrganizer().map(Participant::getFullName).orElse(OutputUtils.ANSI_YELLOW+ "n/a"+OutputUtils.ANSI_PURPLE);

        OutputUtils.sOut(FORMAT.formatted(
                "(id: %s)".formatted(session.getId()),
                session.getTitle(),
                session.getSessionType().name(),
                orgaName,
                session.getDay(),
                session.getStart(),
                session.getEnd())
        );
    }
}
