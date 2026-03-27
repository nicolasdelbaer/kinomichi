package be.nidel.kinomichi.session;

import be.nidel.kinomichi.KinomichiTrainerException;
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

    private ParticipantGroup attendeeGroup = new ParticipantGroup();
    private Participant trainer;
    private LocalDate day;
    private LocalTime start;
    private LocalTime end;

    public Session(LocalDate day, LocalTime startTime) {
        this(day, startTime, 90);
    }
    public Session(LocalDate day, LocalTime startTime, int duration) {
        this.day = day;
        this.start = startTime;
        this.duration = duration;
        this.end = start.plusMinutes(duration);
    }

    @Override
    public String toString() {
        return "Period{" +
                ", day=" + day +
                ", trainer=" + ((Objects.nonNull(trainer))?trainer.getFullName():"n/a") +
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

    public Optional<Participant> getTrainer() {
        return Optional.ofNullable(trainer);
    }

    public void setTrainer(Participant trainer) {
        //TODO catcher l'exception
        if(trainer.getType() != ParticipantType.Trainer)
            throw new KinomichiTrainerException("Participant must be a trainer");

        this.trainer = trainer;
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
}
