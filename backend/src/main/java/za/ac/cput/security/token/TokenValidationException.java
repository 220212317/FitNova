package za.ac.cput.security.token;

public class TokenValidationException extends RuntimeException {

    public enum Reason {
        EXPIRED,
        MALFORMED,
        INVALID_SIGNATURE,
        MISSING_CLAIMS,
        WRONG_TYPE,
        INVALID_TOKEN
    }

    private final Reason reason;

    public TokenValidationException(Reason reason, String message) {
        super(message);
        this.reason = reason;
    }

    public TokenValidationException(Reason reason, String message, Throwable cause) {
        super(message, cause);
        this.reason = reason;
    }

    public Reason reason() {
        return reason;
    }
}