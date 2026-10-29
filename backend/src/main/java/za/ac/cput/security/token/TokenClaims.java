package za.ac.cput.security.token;

import java.time.Instant;

public record TokenClaims(String subject, String userId, String tokenType, Instant expiresAt) {
}