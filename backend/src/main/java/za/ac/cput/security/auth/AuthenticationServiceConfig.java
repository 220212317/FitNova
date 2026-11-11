/*
 * AuthenticationServiceConfig.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.security.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import za.ac.cput.security.event.LoginAttemptTracker;
import za.ac.cput.security.event.SecurityEventPublisher;

@Configuration
public class AuthenticationServiceConfig {

    @Bean
    @Primary
    public AuthenticationService authenticationService(JwtAuthenticationService jwtAuthenticationService,
                                                       LoginAttemptTracker tracker,
                                                       SecurityEventPublisher events) {
        AuditingAuthenticationService auditing =
                new AuditingAuthenticationService(jwtAuthenticationService, events);
        return new LockoutAuthenticationService(auditing, tracker, events);
    }
}
