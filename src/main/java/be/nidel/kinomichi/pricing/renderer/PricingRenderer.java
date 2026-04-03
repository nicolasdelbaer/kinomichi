package be.nidel.kinomichi.pricing.renderer;

import be.nidel.kinomichi.participant.ParticipantType;
import be.nidel.kinomichi.pricing.Pricing;
import be.nidel.kinomichi.session.SessionType;
import be.nidel.utils.BigDecimalFormatter;
import be.nidel.utils.OutputUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PricingRenderer {

    public void render(List<Pricing> priceList) {
        OutputUtils.sOut(OutputUtils.STYLISABLE_LINE.formatted(
                OutputUtils.ANSI_YELLOW_BACKGROUND + OutputUtils.ANSI_BLACK_BOLD,
                "- Prices Table",
                OutputUtils.ANSI_RESET
        ));

        Map<ParticipantType, List<Pricing>> pricesByParticipantType = priceList.stream()
                .collect(Collectors.groupingBy(Pricing::getParticipantType));

        List<String> enumString = Arrays
                .stream(SessionType.values())
                .map(e -> e.name()).collect(Collectors.toList());
        enumString.addFirst("$$$");

        String format = "%-11s" + "%-14s".repeat(enumString.size()-1);

        OutputUtils.sOut(OutputUtils.STYLISABLE_LINE.formatted(
                OutputUtils.ANSI_WHITE_BOLD,
                format.formatted(enumString.toArray()),
                OutputUtils.ANSI_RESET
        ));

        pricesByParticipantType.forEach((key, values) -> {
            List<String> priceRow = new ArrayList<>();
            priceRow.add(key.name());
            values.forEach(pricing -> {
                BigDecimalFormatter priceFormatter = new BigDecimalFormatter(pricing.getPrice()).formatEuro().zeroToFree();
                priceRow.add(priceFormatter.toString());
            });

            OutputUtils.sOut(OutputUtils.DEFAULT_LINE.formatted(
                    format.formatted(priceRow.toArray())
            ));
        });

    }
}
