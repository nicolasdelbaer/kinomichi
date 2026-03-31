package be.nidel.kinomichi.gathering;

import be.nidel.kinomichi.pricing.PricingDTO;

import java.util.List;

public record GatheringDTO(String title, List<PricingDTO> priceList) { }
