/*
 * AuthResponse.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.security.auth;

import za.ac.cput.domain.enums.RoleType;

import java.util.List;

public record AuthResponse(String accessToken,
                           String refreshToken,
                           String tokenType,
                           String userId,
                           String email,
                           List<RoleType> roles) {

    public static AuthResponse bearer(String accessToken,
                                      String refreshToken,
                                      String userId,
                                      String email,
                                      List<RoleType> roles) {
        return new AuthResponse(accessToken, refreshToken, "Bearer", userId, email, roles);
    }
}
