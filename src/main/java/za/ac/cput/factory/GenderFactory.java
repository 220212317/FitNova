package za.ac.cput.factory;

import za.ac.cput.domain.Gender;
import za.ac.cput.util.Helper;

/*
 * GenderFactory.java
 * Author: Inga Plati
 * 230126634
 */
public class GenderFactory {

    public static Gender createGender(String genderId, String description) {
        if (Helper.isNullOrEmpty(description)) {
            return null;
        }
        if (Helper.isNullOrEmpty(genderId)) {
            genderId = Helper.generateId();
        }
        return new Gender.Builder()
                .setGenderId(genderId)
                .setDescription(description.trim())
                .build();
    }

    public static Gender createGender(String description) {
        return createGender(Helper.generateId(), description);
    }
}