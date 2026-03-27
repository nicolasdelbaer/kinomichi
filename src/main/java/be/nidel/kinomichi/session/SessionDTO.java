package be.nidel.kinomichi.session;

import java.time.LocalDate;
import java.time.LocalTime;

public record SessionDTO(
        int duration,
        LocalDate day,
        LocalTime start
) { }
