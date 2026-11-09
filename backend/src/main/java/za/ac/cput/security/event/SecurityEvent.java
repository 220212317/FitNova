package za.ac.cput.security.event;

import java.time.Instant;
import java.util.Objects;

public record SecurityEvent(Type type, String subject, Instant at) {
    public SecurityEvent {
        Objects.requireNonNull(type, "type is required");
        Objects.requireNonNull(at, "timestamp is required");
        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("subject is required");
        }
        subject = subject.trim().replace('\n', '_').replace('\r', '_');
    }

    public enum Type { LOGIN_SUCCESS, LOGIN_FAILED, LOGIN_BLOCKED, REGISTERED, ROLE_CHANGED }
}
