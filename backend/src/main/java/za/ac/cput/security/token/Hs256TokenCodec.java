package za.ac.cput.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;
import za.ac.cput.security.token.TokenValidationException.Reason;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class Hs256TokenCodec implements TokenCodec {

    private final SecretKey key;

    public Hs256TokenCodec(JwtKeyProvider keys) {
        this.key = keys.signingKey();
    }

    @Override
    public String sign(TokenClaims c) {
        return Jwts.builder()
                .subject(c.subject())
                .claim("uid", c.userId())
                .claim("token_type", c.tokenType())
                .issuedAt(new Date())
                .expiration(Date.from(c.expiresAt()))
                .signWith(key)
                .compact();
    }

    @Override
    public TokenClaims verify(String token) {
        if (token == null || token.isBlank()) {
            throw new TokenValidationException(Reason.MALFORMED, "Token is null or blank");
        }
        try {
            Claims cl = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String subject = cl.getSubject();
            String uid = cl.get("uid", String.class);
            String type = cl.get("token_type", String.class);
            Date exp = cl.getExpiration();

            if (subject == null || uid == null || type == null || exp == null) {
                throw new TokenValidationException(Reason.MISSING_CLAIMS, "Required claims missing");
            }

            return new TokenClaims(subject, uid, type, exp.toInstant());
        } catch (ExpiredJwtException e) {
            throw new TokenValidationException(Reason.EXPIRED, "Token has expired", e);
        } catch (SignatureException e) {
            throw new TokenValidationException(Reason.INVALID_SIGNATURE, "Invalid signature", e);
        } catch (MalformedJwtException e) {
            throw new TokenValidationException(Reason.MALFORMED, "Malformed token", e);
        } catch (JwtException e) {
            throw new TokenValidationException(Reason.MALFORMED, "Invalid token", e);
        }
    }
}