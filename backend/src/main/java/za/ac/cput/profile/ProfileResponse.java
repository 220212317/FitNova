package za.ac.cput.profile;

import java.time.LocalDate;
import java.util.List;

/** Everything we hold about the signed-in user, flattened for the profile page. */
public record ProfileResponse(String userId,
                              String firstName,
                              String lastName,
                              LocalDate dateOfBirth,
                              String email,
                              LocalDate memberSince,
                              List<String> roles,
                              String gender,
                              String streetNumber,
                              String streetName,
                              String suburbName,
                              String city,
                              String postalCode,
                              String province,
                              String country,
                              String cellphoneNumber,
                              String alternativeCellphoneNumber,
                              NextOfKinDto nextOfKin) {

    public record NextOfKinDto(String firstName,
                               String lastName,
                               String relationship,
                               String cellphoneNumber) {
    }
}
