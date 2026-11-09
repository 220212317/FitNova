/*
 * AuthenticationService.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.security.auth;

public interface AuthenticationService {

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(String refreshToken);
}
