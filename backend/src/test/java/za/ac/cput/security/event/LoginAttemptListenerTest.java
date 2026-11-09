package za.ac.cput.security.event;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
 * LoginAttemptListenerTest.java
 * Author: Avuyile Sitoyi (240971051)
 */
class LoginAttemptListenerTest {
    @Test
    void recordsFailureOnLoginFailed() {
        LoginAttemptTracker tracker = new LoginAttemptTracker();
        LoginAttemptListener listener = new LoginAttemptListener(tracker);
        listener.onSecurityEvent(event(SecurityEvent.Type.LOGIN_FAILED));
        assertEquals(1, tracker.failures("member@fitnova.test"));
    }

    @Test
    void resetsCountOnLoginSuccess() {
        LoginAttemptTracker tracker = new LoginAttemptTracker();
        tracker.recordFailure("member@fitnova.test");
        LoginAttemptListener listener = new LoginAttemptListener(tracker);
        listener.onSecurityEvent(event(SecurityEvent.Type.LOGIN_SUCCESS));
        assertEquals(0, tracker.failures("member@fitnova.test"));
    }

    private static SecurityEvent event(SecurityEvent.Type type) {
        return new SecurityEvent(type, "member@fitnova.test", Instant.parse("2026-11-09T12:00:00Z"));
    }
}
