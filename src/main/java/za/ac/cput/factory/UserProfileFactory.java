package za.ac.cput.factory;

/**
 *
 * 25 March 2026
 * Author: Avuyile Sitoyi
 * (240971051)
 */
import za.ac.cput.entity.UserProfile;
import za.ac.cput.util.helper;

import java.time.LocalDate;

public class UserProfileFactory {
    public static UserProfile CreateUserProfile(String profileId,
                                                String firstName,
                                                String lastName, String gender, LocalDate dateOfBirth) {
        if (helper.isNullOrEmpty(profileId))
            throw new IllegalArgumentException("memberId required");
        if (helper.isNullOrEmpty(firstName))
            throw new IllegalArgumentException("firstName required");
        if (helper.isNullOrEmpty(lastName))
            throw new IllegalArgumentException("lastName required");
        if (helper.isNullOrEmpty(gender))
            throw new IllegalArgumentException("gender required");
        return new UserProfile.Builder().profileId(profileId).firstName(firstName).
                lastName(lastName).gender(gender).dateOfBirth(dateOfBirth).build();


    }
}
