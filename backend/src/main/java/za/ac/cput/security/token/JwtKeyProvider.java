package za.ac.cput.security.token;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtKeyProvider {

    private final SecretKey key;

    public JwtKeyProvider(@Value("${fitnova.security.jwt.secret}") String base64Secret) {
        if (base64Secret == null || base64Secret.isBlank()) {
            throw new IllegalStateException("JWT_SECRET is missing or blank");
        }
        String trimmed = base64Secret.trim();
        try {
            byte[] decoded = Decoders.BASE64.decode(trimmed);
            if (decoded.length < 32) {
                throw new IllegalStateException("JWT secret must decode to at least 32 bytes");
            }
            this.key = Keys.hmacShaKeyFor(decoded);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("JWT secret is not valid Base64", e);
        }
    }

    public SecretKey signingKey() {
        return key;
    }
}