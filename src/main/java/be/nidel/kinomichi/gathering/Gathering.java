package be.nidel.kinomichi.gathering;

import be.nidel.kinomichi.participant.ParticipantType;
import be.nidel.kinomichi.pricing.Pricing;
import be.nidel.kinomichi.session.Session;
import be.nidel.kinomichi.participant.Participant;
import be.nidel.utils.FormatUtils;
import be.nidel.utils.OutputUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Gathering {
    private String title;
    private List<Pricing> priceList = new ArrayList<>();
    private List<Session> sessionList = new ArrayList<>();

    public Gathering() {
        this.title = title;
    }

    public List<Session> getAllSessions() {
        return sessionList;
    }


    public void addNewSession(Session session) {
        sessionList.add(session);
    }


    //region Data handling
    public List<LocalDate> getAllDays() {
        return sessionList.stream()
                .map(Session::getDay)
                .distinct()
                .toList();
    }

    public void registerAttendeeToSession(Participant attendee, Session[] sessions){
        for (Session session : sessions) {
            session.addAttendee(attendee);
        }
    }

    public List<Participant> getAllAttendees() {
        return sessionList.stream()
                .flatMap(p -> p.getAttendees().stream())
                .distinct()
                .toList();
    }

    //endregion


    //TODO Refactor -> make view
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder()
            .append(OutputUtils.ANSI_YELLOW_BOLD)
            .append("\n")
            .append("- Title: ")
            .append(title)
            .append("\n")
            .append(OutputUtils.ANSI_RESET)
        ;

        sessionList.forEach(session -> {
            sb
            .append(OutputUtils.ANSI_PURPLE)
            .append("Trainer: ");

            session.getTrainer().ifPresentOrElse(
                    participant -> sb.append(participant.getFullName()),
                    () -> sb.append("No trainer"));

            sb
            .append(" - ")
            .append("day: ")
            .append(session.getDay())
            .append(" | ")
            .append(session.getStart())
            .append(" -> ")
            .append(session.getEnd())
            .append("\n")
            .append(OutputUtils.ANSI_RESET)

            .append("- Attendees:");
            session.getAttendees().forEach(attendee -> sb.append(attendee.getFullName()).append(", "));
            sb.delete(sb.length()-2, sb.length()); //remove last ", "
            sb.append("\n");
        });

        sb.append("\n")
          .append(OutputUtils.ANSI_YELLOW_BOLD)
          .append("- Prices:\n")
          .append(OutputUtils.ANSI_RESET);

        priceList.forEach(price ->
            sb
            .append(price.getParticipantType().name())
            .append(" - ")
            .append(price.getSessionType().name())
            .append(": ")
            .append(OutputUtils.ANSI_CYAN_BOLD)
            .append(FormatUtils.formatPrice(price.getPrice()))
            .append(OutputUtils.ANSI_RESET)
            .append("\n")
        );
        sb.append("\n");
        return sb.toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrices(List<Pricing> priceList) {
        this.priceList = priceList;
    }
}
