package be.nidel.kinomichi.gathering;

import be.nidel.kinomichi.session.Session;
import be.nidel.kinomichi.participant.Participant;
import be.nidel.utils.OutputUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Gathering {
    private String title;
    private List<Session> sessionList = new ArrayList<>();

    public Gathering(String title) {
        this.title = title;
    }

    public List<Participant> getAllAttendees() {
        return sessionList.stream()
                .flatMap(p -> p.getAttendees().stream())
                .distinct()
                .toList();
    }

    public List<Session> getAllPeriods() {
        return sessionList;
    }


    public Session addNewPeriod(LocalDate day, LocalTime time) {
        Session session = new Session(day, time);
        sessionList.add(session);
        return session;
    }

    public void addNewDay(LocalDate day, LocalTime startingTime, int numberOfPeriods) {
        for (int i = 0; i <numberOfPeriods; i++) {
            Session session = addNewPeriod(day, startingTime);
            startingTime = startingTime.plusMinutes(session.getDuration());
        }
    }

    public List<LocalDate> getAllDays() {
        return sessionList.stream()
                .map(Session::getDay)
                .distinct()
                .toList();
    }

    public void registerAttendeeToPeriod(Participant attendee, Session[] sessions){
        for (Session session : sessions) {
            session.addAttendee(attendee);
        }
    }


    //TODO Refactor -> make view
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder()
            .append(OutputUtils.ANSI_YELLOW_BOLD)
            .append("Title: ")
            .append(title)
            .append("\n")
            .append(OutputUtils.ANSI_RESET)
        ;

        sessionList.forEach(p -> {
            sb
            .append(OutputUtils.ANSI_PURPLE)
            .append("Trainer: ");

            Optional<Participant> trainer = p.getTrainer();
            trainer.ifPresent(participant -> sb.append(participant.getFullName()));

            sb
            .append(" - ")
            .append("day: ")
            .append(p.getDay())
            .append(" ")
            .append(p.getTime())
            .append("\n")
            .append(OutputUtils.ANSI_RESET)
            .append("attendees:")
            .append(p.getAttendees())
            .append("\n")
            .append("\n");
        });
        return sb.toString();
    }
}
