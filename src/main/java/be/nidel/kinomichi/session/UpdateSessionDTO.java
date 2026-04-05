package be.nidel.kinomichi.session;

import be.nidel.kinomichi.participant.Participant;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateSessionDTO(
        int id,
        Participant organizer,
        String title,
        String description,
        SessionType type,
        LocalDate day,
        LocalTime start,
        int duration)
{ }
