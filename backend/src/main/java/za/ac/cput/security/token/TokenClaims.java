package za.ac.cput.security.token;

public record TokenClaims(String subject, String userId, String tokenType, java.time.Instant expiresAt) {
    // TODO: implement
}
