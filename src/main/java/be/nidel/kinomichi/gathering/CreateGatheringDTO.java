package be.nidel.kinomichi.gathering;

import be.nidel.kinomichi.pricing.PricingGroupDTO;

import java.util.List;

public record CreateGatheringDTO(
        String title,
        List<PricingGroupDTO> priceList
) { }
