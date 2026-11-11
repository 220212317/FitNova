/*
 * AuditingAuthenticationService.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.security.auth;

import org.springframework.security.authentication.BadCredentialsException;
import za.ac.cput.security.event.SecurityEvent;
import za.ac.cput.security.event.SecurityEventPublisher;
import za.ac.cput.util.Helper;

public class AuditingAuthenticationService implements AuthenticationService {

    private final AuthenticationService delegate;
    private final SecurityEventPublisher events;

    public AuditingAuthenticationService(AuthenticationService delegate, SecurityEventPublisher events) {
        this.delegate = delegate;
        this.events = events;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            AuthResponse response = delegate.login(request);
            events.publish(SecurityEvent.Type.LOGIN_SUCCESS, response.email());
            return response;
        } catch (BadCredentialsException e) {
            if (request != null && !Helper.isNullOrEmpty(request.email())) {
                events.publish(SecurityEvent.Type.LOGIN_FAILED, request.email().trim().toLowerCase());
            }
            throw e;
        }
    }

    @Override
    public AuthResponse refresh(String refreshToken) {
        return delegate.refresh(refreshToken);
    }
}
