package za.ac.cput.security.adapter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public final class AuthenticatedUser implements UserDetails {

    private final String userId;
    private final String email;
    private final String passwordHash;
    private final String displayName;
    private final Collection<? extends GrantedAuthority> authorities;

    private AuthenticatedUser(Builder b) {
        this.userId = b.userId;
        this.email = b.email;
        this.passwordHash = b.passwordHash;
        this.displayName = b.displayName;
        this.authorities = b.authorities;
    }

    public String getUserId() { return userId; }
    public String getDisplayName() { return displayName; }

    @Override public String getUsername() { return email; }
    @Override public String getPassword() { return passwordHash; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    public String primaryRole() {
        for (String role : List.of("ADMIN", "TRAINER", "MEMBER")) {
            if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_" + role))) {
                return role;
            }
        }
        return "MEMBER";
    }

    // UserDetails required methods
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    public static class Builder {
        private String userId;
        private String email;
        private String passwordHash;
        private String displayName;
        private Collection<? extends GrantedAuthority> authorities = List.of();

        public Builder setUserId(String userId) { this.userId = userId; return this; }
        public Builder setEmail(String email) { this.email = email; return this; }
        public Builder setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; return this; }
        public Builder setDisplayName(String displayName) { this.displayName = displayName; return this; }
        public Builder setAuthorities(Collection<? extends GrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }
        public AuthenticatedUser build() { return new AuthenticatedUser(this); }
    }
}