package be.nidel.kinomichi.session;

import be.nidel.kinomichi.base.Archivable;
import be.nidel.kinomichi.base.BaseEntity;
import be.nidel.kinomichi.base.KinomichiTrainerException;
import be.nidel.kinomichi.participant.ParticipantGroup;
import be.nidel.kinomichi.participant.Participant;
import be.nidel.kinomichi.participant.ParticipantType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Session extends BaseEntity implements Archivable {
    private int duration;

    private int gatheringId = -1;
    private ParticipantGroup attendeeGroup = new ParticipantGroup();
    private Participant organizer;
    private SessionType type;
    private LocalDate day;
    private LocalTime start;
    private LocalTime end;
    private boolean archived = false;

    public Session(int gatheringId, LocalDate day, LocalTime startTime) {
        this(gatheringId, day, startTime, 90, SessionType.Exhibition);
    }

    public Session(int gatheringId, LocalDate day, LocalTime startTime, int duration, SessionType type) {
        this.gatheringId = gatheringId;
        this.day = day;
        this.start = startTime;
        this.duration = duration;
        this.end = start.plusMinutes(duration);
        this.type = type;
    }

    @Override
    public String toString() {
        return "Session{" +
                ", day=" + day +
                ", trainer=" + ((Objects.nonNull(organizer))? organizer.getFullName():"n/a") +
                ", start=" + start +
                ", end=" + end +
                ", type=" + type.name() +type.emoji() +
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


    public int getGatheringId() {
        return gatheringId;
    }

    public SessionType getSessionType() {
        return type;
    }


    @Override public boolean isArchived() {return archived;}
    @Override public void setArchived() {archived = true;}
    @Override public void recoverArchive() {archived = false;}

}
