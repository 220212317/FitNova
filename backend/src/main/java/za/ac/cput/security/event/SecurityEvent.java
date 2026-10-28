package za.ac.cput.security.event;

public record SecurityEvent(Type type, String subject, java.time.Instant at) {
    // TODO: implement
    public enum Type { LOGIN_SUCCESS, LOGIN_FAILED, LOGIN_BLOCKED, REGISTERED, ROLE_CHANGED }
}
