/*
 * RegisterRequest.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.security.auth;

import java.time.LocalDate;

public record RegisterRequest(String firstName,
                              String lastName,
                              String email,
                              String password,
                              LocalDate dateOfBirth,
                              String streetNumber,
                              String streetName,
                              String suburbName,
                              String postalCode,
                              String province,
                              String city,
                              String country,
                              String gender,
                              String cellphoneNumber,
                              String alternativeCellphoneNumber,
                              NextOfKin nextOfKin) {

    /** Next of kin captured at registration. */
    public record NextOfKin(String firstName,
                            String lastName,
                            String relationship,
                            String cellphoneNumber) {
    }

    /** Backwards-compatible constructor for requests without profile extras (e.g. staff registration). */
    public RegisterRequest(String firstName, String lastName, String email, String password, LocalDate dateOfBirth) {
        this(firstName, lastName, email, password, dateOfBirth,
                null, null, null, null, null, null, null, null, null, null, null);
    }
}
