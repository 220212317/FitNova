package za.ac.cput.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import za.ac.cput.security.adapter.FitNovaUserDetailsService;
import za.ac.cput.security.token.AccessTokenService;

import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final AccessTokenService tokens;
    private final FitNovaUserDetailsService users;

    public JwtAuthFilter(AccessTokenService tokens, FitNovaUserDetailsService users) {
        this.tokens = tokens;
        this.users = users;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            tokens.validate(header.substring(7).trim()).ifPresent(claims -> {
                try {
                    UserDetails details = users.loadUserByUsername(claims.subject());
                    var auth = new UsernamePasswordAuthenticationToken(
                            details, null, details.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } catch (UsernameNotFoundException ignored) {
                    // user was deleted after the token was issued
                }
            });
        }
        chain.doFilter(request, response);
    }
}