package be.nidel.kinomichi;

import be.nidel.utils.DateUtils;
import be.nidel.utils.OutputUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Animation {
    private String title;
    private List<Period> periodList = new ArrayList<>();

    private Animation() {
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

    private void addPeriod(Period period) {
        periodList.add(period);
    }
    public void configureDay(LocalDate day, int numberOfPeriods, LocalTime time) {

        for (int i = 0; i <numberOfPeriods; i++) {
            Period period = new Period(day, time);
            addPeriod(period);
            time = time.plusMinutes(period.getDuration());
        }
    }

    public List<LocalDate> getAllDays() {
        return periodList.stream()
                .map(Period::getDay)
                .distinct()
                .toList();
    }

    public void addAttendeeToPeriod(Participant attendee, Period[] periods){
        for (Period period : periods) {
            period.addAttendee(attendee);
        }
    }

    //region printing



    //endregion

    //TODO make view
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

    public static class Builder{
        private String title;

        public Builder setTitle(String title){
            this.title = title;
            return this;
        }

        public Animation build(){
            Animation animation = new Animation();
            animation.title = title;
            return animation;
        }

    }
}
