package za.ac.cput.security.token;

import za.ac.cput.security.token.TokenValidationException.Reason;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public abstract class TokenService {

    private final TokenCodec codec;

    protected TokenService(TokenCodec codec) {
        this.codec = codec;
    }

    protected abstract String type();
    protected abstract Duration lifetime();

    public String issue(String email, String userId) {
        if (email == null || email.isBlank() || userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("subject and userId are required");
        }
        return codec.sign(new TokenClaims(email, userId, type(), Instant.now().plus(lifetime())));
    }

    /** Soft validation – returns empty on any failure. */
    public Optional<TokenClaims> validate(String token) {
        try {
            return Optional.of(parse(token));
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    /** Hard validation – throws TokenValidationException with a Reason. */
    public TokenClaims parse(String token) {
        TokenClaims claims = codec.verify(token);
        if (!type().equals(claims.tokenType())) {
            throw new TokenValidationException(Reason.WRONG_TYPE,
                    "Expected token type '" + type() + "' but got '" + claims.tokenType() + "'");
        }
        return claims;
    }
}