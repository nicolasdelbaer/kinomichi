package be.nidel.kinomichi.gathering.renderer;

import be.nidel.kinomichi.gathering.Gathering;
import be.nidel.kinomichi.participant.Participant;
import be.nidel.kinomichi.participant.ParticipantType;
import be.nidel.kinomichi.pricing.Pricing;
import be.nidel.kinomichi.registration.Registration;
import be.nidel.kinomichi.registration.RegistrationStatus;
import be.nidel.kinomichi.session.Session;
import be.nidel.kinomichi.session.SessionType;
import be.nidel.utils.BigDecimalFormatter;
import be.nidel.utils.OutputUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GatheringInfoRenderer {
    public void render(Gathering gathering) {
        OutputUtils.sOut(OutputUtils.STYLISABLE_LINE.formatted(
                OutputUtils.ANSI_YELLOW_BACKGROUND + OutputUtils.ANSI_BLACK_BOLD,
                "- Title: " + gathering.getTitle(),
                OutputUtils.ANSI_RESET
        ));
    }

}
