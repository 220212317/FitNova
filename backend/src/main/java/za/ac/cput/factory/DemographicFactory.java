package za.ac.cput.factory;

import za.ac.cput.domain.Demographic;
import za.ac.cput.domain.Gender;
import za.ac.cput.domain.Race;
import za.ac.cput.util.Helper;

/*
 * DemographicFactory.java
 * Author: Inga Plati
 * 230126634
 */
public class DemographicFactory {

    public static Demographic createDemographic(String demographyId, Gender gender, Race race) {
        if (gender == null || race == null) {
            return null;
        }
        if (Helper.isNullOrEmpty(demographyId)) {
            demographyId = Helper.generateId();
        }
        return new Demographic.Builder()
                .setDemographyId(demographyId)
                .setGender(gender)
                .setRace(race)
                .build();
    }

    public static Demographic createDemographic(Gender gender, Race race) {
        return createDemographic(Helper.generateId(), gender, race);
    }
}
