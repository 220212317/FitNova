package za.ac.cput.security.event;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class LoginAttemptTracker {
    public static final int DEFAULT_MAX_FAILURES = 5;
    public static final Duration DEFAULT_WINDOW = Duration.ofMinutes(15);

    private final int maxFailures;
    private final Duration window;
    private final Clock clock;
    private final ConcurrentMap<String, AttemptState> attempts = new ConcurrentHashMap<>();

    public LoginAttemptTracker() {
        this(DEFAULT_MAX_FAILURES, DEFAULT_WINDOW, Clock.systemUTC());
    }

    LoginAttemptTracker(int maxFailures, Duration window, Clock clock) {
        if (maxFailures < 1) throw new IllegalArgumentException("maxFailures must be positive");
        if (window == null || window.isZero() || window.isNegative()) {
            throw new IllegalArgumentException("window must be positive");
        }
        this.maxFailures = maxFailures;
        this.window = window;
        this.clock = Objects.requireNonNull(clock);
    }

    public int recordFailure(String subject) {
        String key = normalize(subject);
        Instant now = clock.instant();
        return attempts.compute(key, (ignored, current) -> {
            if (current == null || expired(current, now)) return new AttemptState(1, now);
            return new AttemptState(current.failures() + 1, current.firstFailureAt());
        }).failures();
    }

    public boolean isLocked(String subject) {
        String key = normalize(subject);
        AttemptState current = attempts.get(key);
        if (current == null) return false;
        if (expired(current, clock.instant())) {
            attempts.remove(key, current);
            return false;
        }
        return current.failures() >= maxFailures;
    }

    public int failures(String subject) {
        String key = normalize(subject);
        AttemptState current = attempts.get(key);
        if (current == null) return 0;
        if (expired(current, clock.instant())) {
            attempts.remove(key, current);
            return 0;
        }
        return current.failures();
    }

    public void reset(String subject) {
        attempts.remove(normalize(subject));
    }

    private boolean expired(AttemptState state, Instant now) {
        return !state.firstFailureAt().plus(window).isAfter(now);
    }

    private static String normalize(String subject) {
        if (subject == null || subject.isBlank()) throw new IllegalArgumentException("subject is required");
        return subject.trim().toLowerCase(Locale.ROOT);
    }

    private record AttemptState(int failures, Instant firstFailureAt) { }
}
