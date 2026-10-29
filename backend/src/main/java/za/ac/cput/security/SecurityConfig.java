package za.ac.cput.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import za.ac.cput.security.adapter.FitNovaUserDetailsService;
import za.ac.cput.security.token.AccessTokenService;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(FitNovaUserDetailsService users, PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(users);
        provider.setPasswordEncoder(encoder);
        return new ProviderManager(provider);
    }

    /** Lets the React dev server (Vite) call the API. Override origins with fitnova.cors.allowed-origin-patterns. */
    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            @Value("${fitnova.cors.allowed-origin-patterns:http://localhost:*,http://127.0.0.1:*,https://localhost:*}")
            String[] originPatterns) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of(originPatterns));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept"));
        config.setExposedHeaders(List.of("WWW-Authenticate"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AccessTokenService tokens,
                                           FitNovaUserDetailsService users) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(unauthorized())
                        .accessDeniedHandler(forbidden()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/register", "/auth/refresh").permitAll()

                        .requestMatchers("/admin/**", "/account/**", "/userrole/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/gender/getAll", "/race/getAll").permitAll()
                        .requestMatchers(HttpMethod.GET, "/gender/**", "/race/**", "/demographic/**").authenticated()
                        .requestMatchers("/gender/**", "/race/**", "/demographic/**").hasAuthority("lookup:manage")

                        .requestMatchers(HttpMethod.GET, "/availability-slots/**").authenticated()
                        .requestMatchers("/availability-slots/**").hasAuthority("slot:manage")

                        .requestMatchers("/booking/getAll", "/booking/findBySlot/**", "/booking/findByStatus/**")
                        .hasAuthority("member:read")
                        .requestMatchers("/user/getAll", "/user/searchByLastName/**", "/user/findByName/**")
                        .hasAuthority("member:read")
                        .requestMatchers(HttpMethod.POST, "/user/create").hasAuthority("user:manage")
                        .requestMatchers(HttpMethod.DELETE, "/user/delete/**").hasAuthority("user:manage")

                        .requestMatchers(HttpMethod.GET,
                                "/address/getAll", "/address/findByCity/**", "/address/findByProvince/**",
                                "/address/findByPostalCode/**",
                                "/contact/getAll", "/contact/findByCellphoneNumber/**", "/contact/findByEmailAddress/**",
                                "/nextofkincontact/getAll", "/nextofkincontact/findByFirstName/**",
                                "/nextofkincontact/findByLastName/**", "/nextofkincontact/findByRelationship/**")
                        .hasAuthority("member:read")

                        .anyRequest().authenticated())
                .addFilterBefore(new JwtAuthFilter(tokens, users), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    /** 401: no usable token. */
    private static AuthenticationEntryPoint unauthorized() {
        return (request, response, ex) -> {
            response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Bearer");
            writeJson(response, HttpStatus.UNAUTHORIZED, "Authentication is required.");
        };
    }

    /** 403: signed in, but the role or permission is not enough. */
    private static AccessDeniedHandler forbidden() {
        return (request, response, ex) ->
                writeJson(response, HttpStatus.FORBIDDEN, "You do not have permission to do that.");
    }

    /** Same JSON shape as GlobalExceptionHandler: status, error, message */
    private static void writeJson(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"status\":" + status.value()
                + ",\"error\":\"" + status.getReasonPhrase()
                + "\",\"message\":\"" + message + "\"}");
    }
}