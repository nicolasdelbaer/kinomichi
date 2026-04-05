package be.nidel.kinomichi.session;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateSessionDTO(
        int duration,
        String title,
        String description,
        LocalDate day,
        LocalTime start,
        SessionType type
) { }
