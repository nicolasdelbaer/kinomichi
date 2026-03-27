package be.nidel.kinomichi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Period {
    private int duration = 90;

    private GroupParticipant attendeeGroup = new GroupParticipant();
    private Participant trainer;
    private LocalDate day;
    private LocalTime start;
    private LocalTime end;

    public Period(LocalDate day, LocalTime startTime) {
        this.day = day;
        this.start = startTime;
        this.end = start.plusMinutes(duration);
    }

    @Override
    public String toString() {
        return "Period{" +
                ", day=" + day +
                ", trainer=" + trainer.getFullName() +
                ", start=" + start +
                ", end=" + end +
                ", trainer=" + trainer +
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
}
