package za.ac.cput.security.token;

public interface TokenCodec {
    String sign(TokenClaims claims);
    /** Throws TokenValidationException on bad signature / expiry / malformed. */
    TokenClaims verify(String token);
}