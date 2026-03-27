package be.nidel.kinomichi;

import be.nidel.utils.OutputUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Animation {
    private String title;
    private List<Period> periodList = new ArrayList<>();

    public Animation(String title) {
        this.title = title;
    }

    public List<Participant> getAllAttendees() {
        return periodList.stream()
                .flatMap(p -> p.getAttendees().stream())
                .distinct()
                .toList();
    }

    public List<Period> getAllPeriods() {
        return periodList;
    }


    public Period addNewPeriod(LocalDate day, LocalTime time) {
        Period period = new Period(day, time);
        periodList.add(period);
        return period;
    }

    //TODO move ?
    public void configureDay(LocalDate day, int numberOfPeriods, LocalTime time) {

        for (int i = 0; i <numberOfPeriods; i++) {
            Period period = addNewPeriod(day, time);
            time = time.plusMinutes(period.getDuration());
        }
    }

    public List<LocalDate> getAllDays() {
        return periodList.stream()
                .map(Period::getDay)
                .distinct()
                .toList();
    }

    public void registerAttendeeToPeriod(Participant attendee, Period[] periods){
        for (Period period : periods) {
            period.addAttendee(attendee);
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

        periodList.forEach(p -> {
            sb
            .append(OutputUtils.ANSI_PURPLE)
            .append("Trainer: ")
            .append(p.getTrainer().getFullName())
            .append(" - ")
            .append("day: ")
            .append(p.getDay())
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
