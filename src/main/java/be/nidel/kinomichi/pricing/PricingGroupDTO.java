package be.nidel.kinomichi.pricing;

import be.nidel.kinomichi.session.SessionType;

import java.util.List;

public record PricingGroupDTO (
    List<PricingDTO> pricingDTOList,
    SessionType sessionType
)
{}
