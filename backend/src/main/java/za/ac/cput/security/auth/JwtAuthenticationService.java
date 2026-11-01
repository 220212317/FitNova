/*
 * JwtAuthenticationService.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.security.auth;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.enums.RoleType;
import za.ac.cput.security.adapter.AuthenticatedUser;
import za.ac.cput.security.adapter.FitNovaUserDetailsService;
import za.ac.cput.security.token.AccessTokenService;
import za.ac.cput.security.token.RefreshTokenService;
import za.ac.cput.security.token.TokenClaims;
import za.ac.cput.security.token.TokenValidationException;
import za.ac.cput.util.Helper;

import java.util.ArrayList;
import java.util.List;

@Service
public class JwtAuthenticationService implements AuthenticationService {

    private static final String ROLE_PREFIX = "ROLE_";
    private static final String INVALID_CREDENTIALS = "Invalid email or password.";

    private final FitNovaUserDetailsService users;
    private final PasswordEncoder encoder;
    private final AccessTokenService accessTokens;
    private final RefreshTokenService refreshTokens;

    public JwtAuthenticationService(FitNovaUserDetailsService users,
                                    PasswordEncoder encoder,
                                    AccessTokenService accessTokens,
                                    RefreshTokenService refreshTokens) {
        this.users = users;
        this.encoder = encoder;
        this.accessTokens = accessTokens;
        this.refreshTokens = refreshTokens;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        if (request == null
                || Helper.isNullOrEmpty(request.email())
                || Helper.isNullOrEmpty(request.password())) {
            throw new BadCredentialsException("Email and password are required.");
        }

        AuthenticatedUser user = load(request.email());

        if (!encoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException(INVALID_CREDENTIALS);
        }

        return issueTokens(user);
    }

    @Override
    public AuthResponse refresh(String refreshToken) {
        if (Helper.isNullOrEmpty(refreshToken)) {
            throw new BadCredentialsException("A refresh token is required.");
        }

        TokenClaims claims;
        try {
            claims = refreshTokens.parse(refreshToken);
        } catch (TokenValidationException e) {
            throw new BadCredentialsException("The refresh token is not valid.");
        }

        return issueTokens(load(claims.subject()));
    }

    private AuthenticatedUser load(String email) {
        try {
            return users.loadUserByUsername(email);
        } catch (UsernameNotFoundException e) {
            throw new BadCredentialsException(INVALID_CREDENTIALS);
        }
    }

    private AuthResponse issueTokens(AuthenticatedUser user) {
        String email = user.getUsername();
        String userId = user.getUserId();
        return AuthResponse.bearer(
                accessTokens.issue(email, userId),
                refreshTokens.issue(email, userId),
                userId,
                email,
                rolesOf(user));
    }

    private List<RoleType> rolesOf(AuthenticatedUser user) {
        List<RoleType> roles = new ArrayList<>();
        for (GrantedAuthority authority : user.getAuthorities()) {
            String name = authority.getAuthority();
            if (name == null || !name.startsWith(ROLE_PREFIX)) {
                continue;
            }
            String role = name.substring(ROLE_PREFIX.length());
            for (RoleType type : RoleType.values()) {
                if (type.name().equals(role) && !roles.contains(type)) {
                    roles.add(type);
                }
            }
        }
        return roles;
    }
}
