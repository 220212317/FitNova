/*
 * LockoutAuthenticationServiceTest.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.security.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import za.ac.cput.domain.enums.RoleType;
import za.ac.cput.security.event.LoginAttemptTracker;
import za.ac.cput.security.event.SecurityEvent;
import za.ac.cput.security.event.SecurityEventPublisher;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LockoutAuthenticationServiceTest {

    private static final String EMAIL = "collins@fitnova.co.za";
    private static final String PASSWORD = "Passw0rd1";

    private List<SecurityEvent> published;
    private LoginAttemptTracker tracker;
    private RecordingAuthenticationService delegate;
    private LockoutAuthenticationService service;

    @BeforeEach
    void setUp() {
        published = new ArrayList<>();
        tracker = new LoginAttemptTracker();
        delegate = new RecordingAuthenticationService();
        service = new LockoutAuthenticationService(delegate, tracker, publisher());
    }

    private SecurityEventPublisher publisher() {
        return new SecurityEventPublisher(new ApplicationEventPublisher() {
            @Override
            public void publishEvent(Object event) {
                published.add((SecurityEvent) event);
            }
        });
    }

    private void lockAccount() {
        for (int i = 0; i < LoginAttemptTracker.DEFAULT_MAX_FAILURES; i++) {
            tracker.recordFailure(EMAIL);
        }
    }

    @Test
    void throwsLockedExceptionWhenLocked() {
        lockAccount();

        LockedException thrown = assertThrows(LockedException.class,
                () -> service.login(new LoginRequest(EMAIL, PASSWORD)));

        assertEquals("Too many failed login attempts. Try again later.", thrown.getMessage());
        assertEquals(0, delegate.logins);
    }

    @Test
    void publishesLoginBlockedEventWhenLocked() {
        lockAccount();

        assertThrows(LockedException.class, () -> service.login(new LoginRequest(EMAIL, PASSWORD)));

        assertEquals(1, published.size());
        assertEquals(SecurityEvent.Type.LOGIN_BLOCKED, published.get(0).type());
        assertEquals(EMAIL, published.get(0).subject());
    }

    @Test
    void delegatesLoginWhenNotLocked() {
        AuthResponse response = service.login(new LoginRequest(EMAIL, PASSWORD));

        assertEquals(1, delegate.logins);
        assertEquals("access-1", response.accessToken());
        assertTrue(published.isEmpty());
    }

    @Test
    void delegatesLoginWhenFailuresAreBelowTheLimit() {
        tracker.recordFailure(EMAIL);
        tracker.recordFailure(EMAIL);

        assertDoesNotThrow(() -> service.login(new LoginRequest(EMAIL, PASSWORD)));

        assertEquals(1, delegate.logins);
    }

    @Test
    void locksTheAccountByEmailIgnoringCaseAndSpaces() {
        lockAccount();

        assertThrows(LockedException.class,
                () -> service.login(new LoginRequest("  COLLINS@FitNova.co.za ", PASSWORD)));
    }

    @Test
    void doesNotLockADifferentAccount() {
        lockAccount();

        assertDoesNotThrow(() -> service.login(new LoginRequest("thandi@fitnova.co.za", PASSWORD)));
    }

    @Test
    void passesABlankEmailToTheDelegate() {
        delegate.failNext = true;

        assertThrows(BadCredentialsException.class, () -> service.login(new LoginRequest("   ", PASSWORD)));
        assertThrows(BadCredentialsException.class, () -> service.login(null));
    }

    @Test
    void refreshIsAlwaysDelegated() {
        lockAccount();

        AuthResponse response = service.refresh("refresh-1");

        assertEquals(1, delegate.refreshes);
        assertEquals("access-1", response.accessToken());
        assertTrue(published.isEmpty());
    }

    private static class RecordingAuthenticationService implements AuthenticationService {

        private int logins;
        private int refreshes;
        private boolean failNext;

        @Override
        public AuthResponse login(LoginRequest request) {
            logins++;
            if (failNext) {
                throw new BadCredentialsException("Invalid email or password.");
            }
            return response();
        }

        @Override
        public AuthResponse refresh(String refreshToken) {
            refreshes++;
            return response();
        }

        private AuthResponse response() {
            return AuthResponse.bearer("access-1", "refresh-1", "U001", EMAIL, List.of(RoleType.MEMBER));
        }
    }
}
