package be.nidel.kinomichi.reporting;

import be.nidel.kinomichi.base.BaseController;
import be.nidel.kinomichi.gathering.Gathering;
import be.nidel.kinomichi.gathering.GatheringModel;
import be.nidel.kinomichi.gathering.renderer.RendererGatheringDTO;
import be.nidel.kinomichi.participant.Participant;
import be.nidel.kinomichi.participant.ParticipantModel;
import be.nidel.kinomichi.registration.Registration;
import be.nidel.kinomichi.registration.RegistrationModel;
import be.nidel.kinomichi.registration.RegistrationStatus;
import be.nidel.kinomichi.session.Session;
import be.nidel.kinomichi.session.SessionModel;
import be.nidel.kinomichi.session.SessionType;
import be.nidel.kinomichi.session.renderer.RendererSessionDTO;
import be.technifutur.shared.Menu;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ReportingController extends BaseController {

    private GatheringModel gatheringModel;
    private SessionModel sessionModel;
    private ParticipantModel participantModel;
    private RegistrationModel registrationModel;
    ReportingView view = new ReportingView(this);

    //region Models mapping
    public void setGatheringModel(GatheringModel gatheringModel){
        this.gatheringModel = gatheringModel;
    }

    public void setRegistrationModel(RegistrationModel registrationModel) {
        this.registrationModel = registrationModel;
    }

    public void setParticipantModel(ParticipantModel participantModel) {
        this.participantModel = participantModel;
    }

    public void setSessionModel(SessionModel sessionModel) {
        this.sessionModel = sessionModel;
    }
    //endregion

    public void showMenu(Menu menu) {
        view.displayUserChoices(menu);
    }

    public void gatheringOverview(int gatheringId){
        try {
            Gathering gathering = gatheringModel.get(gatheringId);
            List<RendererSessionDTO> sessions = new ArrayList<>();
            for (Session session : gathering.getAllSessions())
            {
                List<Participant> sessionAttendees = session.getAttendees();
                Map<Integer, Registration> registrationsByParticipant = getRegistrationBySession(session).stream()
                        .filter(r -> r.getSessionId() == session.getId())
                        .collect(Collectors.toMap(
                                        Registration::getParticipantId, Function.identity()));
                sessions.add(new RendererSessionDTO(session, sessionAttendees, registrationsByParticipant));
            }
            view.renderOverviewReport(new RendererGatheringDTO(gathering, sessions));
        } catch (NoSuchElementException e) {
            view.showInvalidGatheringIdError(gatheringId);
            view.refresh();
        }
    }


    public void gatheringReporting(int gatheringId) {
        try {
            Gathering gathering = gatheringModel.get(gatheringId);
            view.renderGatheringStatus(gathering);
        } catch (NoSuchElementException e) {
            view.showInvalidGatheringIdError(gatheringId);
            view.refresh();
        }
    }

    public List<Participant> getUnpaidParticipants() {
        List<Registration> registrations = registrationModel.getAllByStatus(RegistrationStatus.UNPAID);
        List<Participant> participants = registrations.stream()
                .map(registration -> participantModel.get(registration.getParticipantId())).distinct().toList();
        return participants;
    }

    //region data getter
    public Map<Integer, Registration> getAllRegistrationByGathering(Gathering gathering){
        Map<Integer, Registration> registrationMap = gathering.getAllSessions()
                .stream()
                .flatMap(session -> getRegistrationBySession(session).stream())
                .collect(Collectors.toMap(
                        Registration::getId,
                        Function.identity()));
        return registrationMap;
    }

    public List<Registration> getRegistrationBySession(Session session){
        return registrationModel.getAllRegistrationBySessionId(session.getId());
    }

    public List<Registration> getRegistrationByParticipant(Participant participant) {
        return registrationModel.getAllRegistrationByParticipantId(participant.getId());
    }

    public List<Registration> getUnpaidRegistrationByParticipant(Participant participant) {
        return registrationModel.getUnpaidRegistrationsByParticipantId(participant.getId());
    }

    public Map<Integer, Gathering> getGatheringMap() {
        return gatheringModel.getAllGathering();
    }

    public Map<Integer, Session> getSessionMap() {
        return sessionModel.getAllSession();
    }

    //endregion


    public record Stats(int nbInscriptions, int nbParticipations, int nbAnnulations, int nbAbsences){}
    public record PaymentForecast(BigDecimal totPaid, BigDecimal totDiscount, BigDecimal totUnpaid, BigDecimal forecast) {}
    public record Reservations(long nbDinners, long nbAccommodations, long nbPaidDinners, long nbPaidAccommodations) {}
    public Stats getGatheringStats(Gathering gathering) {
        Map<Integer, Registration> allRegistrations = getAllRegistrationByGathering(gathering);

        Map<RegistrationStatus, Integer> counters = new HashMap<>(Map.of(
            RegistrationStatus.REGISTERED, 0,
            RegistrationStatus.WITHDRAWN, 0,
            RegistrationStatus.UNPAID, 0,
            RegistrationStatus.PAID, 0,
            RegistrationStatus.CANCELLED, 0
        ));
        for (Registration registration : allRegistrations.values()) {
            //incrementing counters from status
            counters.put(
                    registration.getStatus(),
                    counters.get(registration.getStatus())+1
            );
        }

        return new Stats(
                counters.get(RegistrationStatus.REGISTERED),
                counters.get(RegistrationStatus.UNPAID) +
                              counters.get(RegistrationStatus.PAID),
                counters.get(RegistrationStatus.CANCELLED),
                counters.get(RegistrationStatus.WITHDRAWN)
        );
    }
    public PaymentForecast getPaymentForecast(Gathering gathering) {
        BigDecimal totPaid = BigDecimal.ZERO;
        BigDecimal totUnpaid = BigDecimal.ZERO;
        BigDecimal totForecast = BigDecimal.ZERO;

        for (Session session : gathering.getAllSessions()) {
            for (Registration registration : getRegistrationBySession(session)) {
                Participant attendee = participantModel.get(registration.getParticipantId());
                BigDecimal price = gathering.getPriceFor(attendee.getParticipantType(), session.getSessionType()).getPrice();

                if(registration.getStatus() == RegistrationStatus.UNPAID)
                    totUnpaid = totUnpaid.add(price);
                else if (registration.getStatus() == RegistrationStatus.PAID)
                    totPaid = totPaid.add(price);

                if(registration.getStatus() != RegistrationStatus.CANCELLED && registration.getStatus() != RegistrationStatus.WITHDRAWN)
                    totForecast = totForecast.add(price);
            }
        }

        return new PaymentForecast(
                totPaid,
                // TODO implement discounts
                BigDecimal.ZERO,
                totUnpaid,
                totForecast
        );
    }
    public Reservations getReservations(Gathering gathering) {
        record RegistrationSessionMapping(Registration registration, Session session){}

        Map<Integer, Registration> allRegistrations = getAllRegistrationByGathering(gathering);

        // Filter reservations (!= exhibition) & get a mapping of objects
        List<RegistrationSessionMapping> registrationSessionMapping = allRegistrations.values().stream()
                .map(registration -> new RegistrationSessionMapping(
                        registration,
                        sessionModel.get(registration.getSessionId())))
                .filter(mapping -> mapping.session.getSessionType() != SessionType.Exhibition)
                .toList();

         //-> GroupBy session type & Count
        Map<SessionType, Long> groupedTotalSessions = registrationSessionMapping.stream()
                .collect(
                        Collectors.groupingBy(
                                mapping -> mapping.session.getSessionType(),
                                Collectors.counting())
                );
        Map<SessionType, Long> groupedPaidSessions = registrationSessionMapping.stream()
                .filter(mapping -> mapping.registration.getStatus() == RegistrationStatus.PAID)
                .collect(
                        Collectors.groupingBy(
                                mapping -> mapping.session.getSessionType(),
                                Collectors.counting())
                );

        return new Reservations(
                groupedTotalSessions.getOrDefault(SessionType.Dinner, 0L),
                groupedTotalSessions.getOrDefault(SessionType.Accommodation, 0L),
                groupedPaidSessions.getOrDefault(SessionType.Dinner, 0L),
                groupedPaidSessions.getOrDefault(SessionType.Accommodation, 0L)
        );
    }


}