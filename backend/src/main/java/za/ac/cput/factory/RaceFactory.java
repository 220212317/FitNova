package za.ac.cput.factory;

import za.ac.cput.domain.Race;
import za.ac.cput.util.Helper;

/*
 * RaceFactory.java
 * Author: Inga Plati
 * 230126634
 */
public class RaceFactory {

    public static Race createRace(String raceId, String description) {
        if (Helper.isNullOrEmpty(description)) {
            return null;
        }
        if (Helper.isNullOrEmpty(raceId)) {
            raceId = Helper.generateId();
        }
        return new Race.Builder()
                .setRaceId(raceId)
                .setDescription(description.trim())
                .build();
    }

    public static Race createRace(String description) {
        return createRace(Helper.generateId(), description);
    }
}