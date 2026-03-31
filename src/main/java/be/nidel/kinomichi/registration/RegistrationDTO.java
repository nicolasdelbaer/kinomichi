package be.nidel.kinomichi.registration;

public record RegistrationDTO(
    RegistrationStatus status,
    int participantId,
    int sessionId,
    int priceId) {
}
