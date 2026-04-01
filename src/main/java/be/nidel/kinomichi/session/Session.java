package be.nidel.kinomichi.session;

import be.nidel.kinomichi.base.KinomichiTrainerException;
import be.nidel.kinomichi.participant.ParticipantGroup;
import be.nidel.kinomichi.participant.Participant;
import be.nidel.kinomichi.participant.ParticipantType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Session {
    private int duration;

    private int id = -1;
    private int gatheringId = -1;
    private ParticipantGroup attendeeGroup = new ParticipantGroup();
    private Participant organizer;
    private SessionType type;
    private LocalDate day;
    private LocalTime start;
    private LocalTime end;

    public Session(int gatheringId, LocalDate day, LocalTime startTime) {
        this(gatheringId, day, startTime, 90);
    }

    public Session(int gatheringId, LocalDate day, LocalTime startTime, int duration) {
        this.gatheringId = gatheringId;
        this.day = day;
        this.start = startTime;
        this.duration = duration;
        this.end = start.plusMinutes(duration);
    }

    @Override
    public String toString() {
        return "Session{" +
                ", day=" + day +
                ", trainer=" + ((Objects.nonNull(organizer))? organizer.getFullName():"n/a") +
                ", start=" + start +
                ", end=" + end +
                ",attendees=" + attendeeGroup +
                '}';
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public LocalDate getDay() {
        return day;
    }

    public Optional<Participant> getOrganizer() {
        return Optional.ofNullable(organizer);
    }

    public void setOrganizer(Participant organizer) {
        //TODO catcher l'exception
        if(organizer.getParticipantType() != ParticipantType.Trainer)
            throw new KinomichiTrainerException("Participant must be a trainer");

        this.organizer = organizer;
    }

    public void addAttendee(Participant attendee) {
        attendeeGroup.add(attendee);
    }

    public List<Participant> getAttendees() {
        return attendeeGroup.toList();
    }

    public int getDuration() {
        return duration;
    }

    public LocalTime getTime() {
        return start;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGatheringId() {
        return gatheringId;
    }

    public SessionType getSessionType() {
        return null;
    }
}
