package be.nidel.kinomichi;

import be.nidel.kinomichi.gathering.Gathering;
import be.nidel.kinomichi.participant.Participant;
import be.nidel.kinomichi.session.Session;

public record Participation(Participant participant, Gathering gathering, Session session) {}
