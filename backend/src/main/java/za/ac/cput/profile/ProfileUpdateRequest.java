package za.ac.cput.profile;

import java.time.LocalDate;

/** Fields a user may change on their own profile. Email, password and roles are not editable here. */
public record ProfileUpdateRequest(String firstName,
                                   String lastName,
                                   LocalDate dateOfBirth,
                                   String gender,
                                   String race,
                                   String streetNumber,
                                   String streetName,
                                   String suburbName,
                                   String city,
                                   String postalCode,
                                   String province,
                                   String country,
                                   String cellphoneNumber,
                                   String alternativeCellphoneNumber,
                                   ProfileResponse.NextOfKinDto nextOfKin) {
}
