/*
 * JwtAuthenticationServiceTest.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.security.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import za.ac.cput.domain.enums.RoleType;
import za.ac.cput.security.adapter.AuthenticatedUser;
import za.ac.cput.security.adapter.FitNovaUserDetailsService;
import za.ac.cput.security.token.AccessTokenService;
import za.ac.cput.security.token.RefreshTokenService;
import za.ac.cput.security.token.TokenClaims;
import za.ac.cput.security.token.TokenCodec;
import za.ac.cput.security.token.TokenValidationException;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationServiceTest {

    private static final String EMAIL = "collins@fitnova.co.za";
    private static final String PASSWORD = "test-login-1";
    private static final String STORED_HASH = "hashed:test-login-1";

    private AccessTokenService accessTokens;
    private RefreshTokenService refreshTokens;
    private JwtAuthenticationService service;

    @BeforeEach
    void setUp() {
        TokenCodec codec = new FakeCodec();
        accessTokens = new AccessTokenService(codec, 15);
        refreshTokens = new RefreshTokenService(codec, 7);
        service = new JwtAuthenticationService(new FakeUsers(), new FakeEncoder(), accessTokens, refreshTokens);
    }

    @Test
    void loginReturnsAccessAndRefreshTokens() {
        AuthResponse response = service.login(new LoginRequest(EMAIL, PASSWORD));

        assertNotNull(response);
        assertNotNull(response.accessToken());
        assertNotNull(response.refreshToken());
        assertNotEquals(response.accessToken(), response.refreshToken());
        assertEquals("Bearer", response.tokenType());
        assertEquals("U001", response.userId());
        assertEquals(EMAIL, response.email());
    }

    @Test
    void loginReturnsOnlyRoleAuthoritiesAsRoles() {
        AuthResponse response = service.login(new LoginRequest(EMAIL, PASSWORD));

        assertEquals(List.of(RoleType.MEMBER, RoleType.TRAINER), response.roles());
    }

    @Test
    void loginRejectsWrongPassword() {
        assertThrows(BadCredentialsException.class,
                () -> service.login(new LoginRequest(EMAIL, "test-wrong-1")));
    }

    @Test
    void loginRejectsUnknownEmailWithTheSameMessageAsAWrongPassword() {
        BadCredentialsException unknown = assertThrows(BadCredentialsException.class,
                () -> service.login(new LoginRequest("nobody@fitnova.co.za", PASSWORD)));
        BadCredentialsException wrongPassword = assertThrows(BadCredentialsException.class,
                () -> service.login(new LoginRequest(EMAIL, "test-wrong-1")));

        assertEquals(wrongPassword.getMessage(), unknown.getMessage());
    }

    @Test
    void loginRejectsNullRequest() {
        assertThrows(BadCredentialsException.class, () -> service.login(null));
    }

    @Test
    void loginRejectsBlankPassword() {
        assertThrows(BadCredentialsException.class, () -> service.login(new LoginRequest(EMAIL, "   ")));
    }

    @Test
    void refreshIssuesNewAccessToken() {
        String refreshToken = refreshTokens.issue(EMAIL, "U001");

        AuthResponse response = service.refresh(refreshToken);

        assertNotNull(response.accessToken());
        assertEquals("U001", response.userId());
        assertEquals(EMAIL, response.email());
    }

    @Test
    void refreshRejectsInvalidToken() {
        assertThrows(BadCredentialsException.class, () -> service.refresh("not-a-real-token"));
    }

    @Test
    void refreshRejectsAnAccessTokenUsedAsARefreshToken() {
        String accessToken = accessTokens.issue(EMAIL, "U001");

        assertThrows(BadCredentialsException.class, () -> service.refresh(accessToken));
    }

    @Test
    void refreshRejectsNullToken() {
        assertThrows(BadCredentialsException.class, () -> service.refresh(null));
    }

    private static final class FakeEncoder implements PasswordEncoder {
        @Override
        public String encode(CharSequence rawPassword) {
            return "hashed:" + rawPassword;
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return encodedPassword != null && encodedPassword.equals(encode(rawPassword));
        }
    }

    private static final class FakeUsers extends FitNovaUserDetailsService {
        private FakeUsers() {
            super(null, null, null, null);
        }

        @Override
        public AuthenticatedUser loadUserByUsername(String email) {
            if (!EMAIL.equals(email)) {
                throw new UsernameNotFoundException("Unknown account");
            }
            return new AuthenticatedUser.Builder()
                    .setUserId("U001")
                    .setEmail(EMAIL)
                    .setPasswordHash(STORED_HASH)
                    .setDisplayName("Collins Shibambo")
                    .setAuthorities(List.of(
                            new SimpleGrantedAuthority("ROLE_MEMBER"),
                            new SimpleGrantedAuthority("ROLE_TRAINER"),
                            new SimpleGrantedAuthority("member:read")))
                    .build();
        }
    }

    private static final class FakeCodec implements TokenCodec {
        @Override
        public String sign(TokenClaims claims) {
            return claims.subject() + "|" + claims.userId() + "|" + claims.tokenType() + "|" + claims.expiresAt();
        }

        @Override
        public TokenClaims verify(String token) {
            if (token == null || token.isBlank()) {
                throw new TokenValidationException(TokenValidationException.Reason.MALFORMED, "Token is blank");
            }
            String[] parts = token.split("\\|");
            if (parts.length != 4) {
                throw new TokenValidationException(TokenValidationException.Reason.MALFORMED, "Malformed token");
            }
            Instant expiresAt;
            try {
                expiresAt = Instant.parse(parts[3]);
            } catch (RuntimeException e) {
                throw new TokenValidationException(TokenValidationException.Reason.MALFORMED, "Bad expiry", e);
            }
            if (expiresAt.isBefore(Instant.now())) {
                throw new TokenValidationException(TokenValidationException.Reason.EXPIRED, "Token has expired");
            }
            return new TokenClaims(parts[0], parts[1], parts[2], expiresAt);
        }
    }
}
