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
                              LocalDate dateOfBirth) {
}
