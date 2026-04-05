package be.nidel.kinomichi.gathering;

import be.nidel.kinomichi.pricing.PricingGroupDTO;

import java.util.List;

public record UpdateGatheringDTO(
        int id,
        String title,
        List<PricingGroupDTO> priceList)
{ }

