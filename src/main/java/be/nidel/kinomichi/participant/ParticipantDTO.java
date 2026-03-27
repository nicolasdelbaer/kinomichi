package be.nidel.kinomichi.participant;

public record ParticipantDTO(
        String firstName,
        String lastName,
        String phone,
        String email,
        String clubName,
        ParticipantType type
) { }
