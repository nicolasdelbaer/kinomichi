package be.nidel.kinomichi.gathering;

import be.nidel.kinomichi.base.Archivable;
import be.nidel.kinomichi.base.BaseEntity;
import be.nidel.kinomichi.participant.ParticipantType;
import be.nidel.kinomichi.pricing.Pricing;
import be.nidel.kinomichi.pricing.PricingGroup;
import be.nidel.kinomichi.session.Session;
import be.nidel.kinomichi.participant.Participant;
import be.nidel.kinomichi.session.SessionType;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Gathering extends BaseEntity implements Archivable, Serializable {
    private String title;
    private List<PricingGroup> priceGroupList = new ArrayList<>();
    private List<Session> sessionList = new ArrayList<>();
    private boolean archived = false;

    public Gathering() {
        this.title = title;
    }

    public List<Session> getAllSessions() {
        return sessionList.stream().filter(
                session -> !session.isArchived()
        ).toList();
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

    public void setPrices(List<PricingGroup> priceList) {
        this.priceGroupList = priceList;
    }

    public List<Pricing> getPriceGroupList() {
        return priceGroupList.stream().flatMap(pg -> pg.getPricingList().stream()).toList();
    }

    public Pricing getPriceFor(ParticipantType participantType, SessionType sessionType) {
        Optional<Pricing> result =  priceGroupList.stream()
                .filter(pg -> pg.getSessionType() == sessionType)
                .flatMap(pg -> pg.getPricingList().stream())
                .filter(p -> p.getParticipantType() == participantType)
                .findFirst();

        if(result.isPresent()){
            return result.get();
        }else{
            throw new IllegalArgumentException("Cannot find a price for participant type: %s && session type: %s".formatted(participantType.name(), sessionType.name()));
        }
    }

    @Override
    public String toString() {
        return "Gathering{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", sessionCount=" + sessionList.size() +
                '}';
    }


    @Override public boolean isArchived() {return archived;}
    @Override public void setArchived() {archived = true;}
    @Override public void recoverArchive() {archived = false;}

    public List<PricingGroup> getPriceGroups() {
        return priceGroupList;
    }
    //endregion
}
