/*
 * LockoutAuthenticationService.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.security.auth;

import org.springframework.security.authentication.LockedException;
import za.ac.cput.security.event.LoginAttemptTracker;
import za.ac.cput.security.event.SecurityEvent;
import za.ac.cput.security.event.SecurityEventPublisher;
import za.ac.cput.util.Helper;

public class LockoutAuthenticationService implements AuthenticationService {

    private static final String LOCKED_MESSAGE = "Too many failed login attempts. Try again later.";

    private final AuthenticationService delegate;
    private final LoginAttemptTracker tracker;
    private final SecurityEventPublisher events;

    public LockoutAuthenticationService(AuthenticationService delegate,
                                        LoginAttemptTracker tracker,
                                        SecurityEventPublisher events) {
        this.delegate = delegate;
        this.tracker = tracker;
        this.events = events;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        if (request == null || Helper.isNullOrEmpty(request.email())) {
            return delegate.login(request);
        }

        String email = request.email().trim().toLowerCase();

        if (tracker.isLocked(email)) {
            events.publish(SecurityEvent.Type.LOGIN_BLOCKED, email);
            throw new LockedException(LOCKED_MESSAGE);
        }

        return delegate.login(request);
    }

    @Override
    public AuthResponse refresh(String refreshToken) {
        return delegate.refresh(refreshToken);
    }
}
