package be.nidel.kinomichi.gathering;

import be.technifutur.shared.Menu;

//NOTE too much gathering info id + model + gathering
public record GatheringPayload(Menu context, Integer id, GatheringModel model, Gathering gathering) { }
