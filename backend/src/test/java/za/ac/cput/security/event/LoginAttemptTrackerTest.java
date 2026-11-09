package za.ac.cput.security.event;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * LoginAttemptTrackerTest.java
 * Author: Avuyile Sitoyi (240971051)
 */
class LoginAttemptTrackerTest {
    @Test
    void notLockedBeforeFiveFailures() {
        LoginAttemptTracker tracker = tracker(new MutableClock());
        for (int i = 0; i < 4; i++) tracker.recordFailure("User@FitNova.test");
        assertFalse(tracker.isLocked("user@fitnova.test"));
        assertEquals(4, tracker.failures("user@fitnova.test"));
    }

    @Test
    void lockedAfterFiveFailures() {
        LoginAttemptTracker tracker = tracker(new MutableClock());
        for (int i = 0; i < 5; i++) tracker.recordFailure("user@fitnova.test");
        assertTrue(tracker.isLocked("user@fitnova.test"));
    }

    @Test
    void resetClearsFailures() {
        LoginAttemptTracker tracker = tracker(new MutableClock());
        tracker.recordFailure("user@fitnova.test");
        tracker.reset("user@fitnova.test");
        assertEquals(0, tracker.failures("user@fitnova.test"));
        assertFalse(tracker.isLocked("user@fitnova.test"));
    }

    @Test
    void failuresOutsideWindowStartNewCount() {
        MutableClock clock = new MutableClock();
        LoginAttemptTracker tracker = tracker(clock);
        tracker.recordFailure("user@fitnova.test");
        clock.advance(Duration.ofMinutes(15));
        tracker.recordFailure("user@fitnova.test");
        assertEquals(1, tracker.failures("user@fitnova.test"));
    }

    private static LoginAttemptTracker tracker(Clock clock) {
        return new LoginAttemptTracker(5, Duration.ofMinutes(15), clock);
    }

    private static final class MutableClock extends Clock {
        private Instant instant = Instant.parse("2026-11-09T12:00:00Z");

        void advance(Duration duration) { instant = instant.plus(duration); }
        @Override public ZoneId getZone() { return ZoneOffset.UTC; }
        @Override public Clock withZone(ZoneId zone) { return this; }
        @Override public Instant instant() { return instant; }
    }
}
