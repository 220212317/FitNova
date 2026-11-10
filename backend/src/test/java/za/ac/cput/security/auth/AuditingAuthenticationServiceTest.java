/*
 * AuditingAuthenticationServiceTest.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.security.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.BadCredentialsException;
import za.ac.cput.domain.enums.RoleType;
import za.ac.cput.security.event.SecurityEvent;
import za.ac.cput.security.event.SecurityEventPublisher;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuditingAuthenticationServiceTest {

    private static final String EMAIL = "collins@fitnova.co.za";
    private static final String PASSWORD = "Passw0rd1";

    private List<SecurityEvent> published;
    private StubAuthenticationService delegate;
    private AuditingAuthenticationService service;

    @BeforeEach
    void setUp() {
        published = new ArrayList<>();
        delegate = new StubAuthenticationService();
        service = new AuditingAuthenticationService(delegate, publisher());
    }

    private SecurityEventPublisher publisher() {
        return new SecurityEventPublisher(new ApplicationEventPublisher() {
            @Override
            public void publishEvent(Object event) {
                published.add((SecurityEvent) event);
            }
        });
    }

    @Test
    void publishesLoginSuccessEvent() {
        AuthResponse response = service.login(new LoginRequest(EMAIL, PASSWORD));

        assertEquals("access-1", response.accessToken());
        assertEquals(1, published.size());
        assertEquals(SecurityEvent.Type.LOGIN_SUCCESS, published.get(0).type());
        assertEquals(EMAIL, published.get(0).subject());
        assertNotNull(published.get(0).at());
    }

    @Test
    void publishesLoginFailedEvent() {
        delegate.fail = true;

        assertThrows(BadCredentialsException.class, () -> service.login(new LoginRequest(EMAIL, PASSWORD)));

        assertEquals(1, published.size());
        assertEquals(SecurityEvent.Type.LOGIN_FAILED, published.get(0).type());
        assertEquals(EMAIL, published.get(0).subject());
    }

    @Test
    void publishesTheFailedEventWithALowerCaseEmail() {
        delegate.fail = true;

        assertThrows(BadCredentialsException.class,
                () -> service.login(new LoginRequest("  COLLINS@FitNova.co.za ", PASSWORD)));

        assertEquals(EMAIL, published.get(0).subject());
    }

    @Test
    void rethrowsTheOriginalException() {
        delegate.fail = true;

        BadCredentialsException thrown = assertThrows(BadCredentialsException.class,
                () -> service.login(new LoginRequest(EMAIL, PASSWORD)));

        assertEquals("Invalid email or password.", thrown.getMessage());
    }

    @Test
    void publishesNoEventWhenTheEmailIsMissing() {
        delegate.fail = true;

        assertThrows(BadCredentialsException.class, () -> service.login(new LoginRequest(null, PASSWORD)));
        assertThrows(BadCredentialsException.class, () -> service.login(null));

        assertTrue(published.isEmpty());
    }

    @Test
    void refreshPublishesNoEvent() {
        AuthResponse response = service.refresh("refresh-1");

        assertEquals("access-1", response.accessToken());
        assertTrue(published.isEmpty());
    }

    private static class StubAuthenticationService implements AuthenticationService {

        private boolean fail;

        @Override
        public AuthResponse login(LoginRequest request) {
            if (fail) {
                throw new BadCredentialsException("Invalid email or password.");
            }
            return response();
        }

        @Override
        public AuthResponse refresh(String refreshToken) {
            return response();
        }

        private AuthResponse response() {
            return AuthResponse.bearer("access-1", "refresh-1", "U001", EMAIL, List.of(RoleType.MEMBER));
        }
    }
}
