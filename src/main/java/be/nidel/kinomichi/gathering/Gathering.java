package be.nidel.kinomichi.gathering;

import be.nidel.kinomichi.participant.ParticipantType;
import be.nidel.kinomichi.pricing.Pricing;
import be.nidel.kinomichi.session.Session;
import be.nidel.kinomichi.participant.Participant;
import be.nidel.kinomichi.session.SessionType;
import be.nidel.utils.FormatUtils;
import be.nidel.utils.OutputUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Gathering {
    private int id = -1;
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


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrices(List<Pricing> priceList) {
        this.priceList = priceList;
    }

    public List<Pricing> getPriceList() {
        return priceList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Gathering{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", sessionCount=" + sessionList.size() +
                '}';
    }

    public Pricing getPriceFor(ParticipantType participantType, SessionType sessionType) {
        Optional<Pricing> result =  priceList.stream()
                .filter(p -> p.getParticipantType() == participantType && p.getSessionType() == sessionType).findFirst();

        if(result.isPresent()){
            return result.get();
        }else{
            throw new IllegalArgumentException("Cannot find a price for participant type: %s && session type: %s".formatted(participantType.name(), sessionType.name()));
        }
    }
}
