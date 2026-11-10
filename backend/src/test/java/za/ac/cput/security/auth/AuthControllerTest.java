/*
 * AuthControllerTest.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.security.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import za.ac.cput.domain.Account;
import za.ac.cput.domain.User;
import za.ac.cput.domain.enums.RoleType;
import za.ac.cput.repository.IAccountRepository;
import za.ac.cput.repository.IAddressRepository;
import za.ac.cput.repository.IContactRepository;
import za.ac.cput.repository.IDemographicRepository;
import za.ac.cput.repository.IGenderRepository;
import za.ac.cput.repository.INextOfKinContactRepository;
import za.ac.cput.repository.IUserRepository;
import za.ac.cput.repository.IUserRoleRepository;
import za.ac.cput.security.policy.PasswordPolicySelector;
import za.ac.cput.security.policy.StandardPasswordPolicy;
import za.ac.cput.security.policy.StrictPasswordPolicy;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private static final String EMAIL = "collins@fitnova.co.za";
    private static final String PASSWORD = "Passw0rd1";

    @Mock
    private IAccountRepository accounts;

    @Mock
    private IUserRepository users;

    @Mock
    private IUserRoleRepository userRoles;

    @Mock
    private IAddressRepository addresses;

    @Mock
    private IContactRepository contacts;

    @Mock
    private IGenderRepository genders;

    @Mock
    private IDemographicRepository demographics;

    @Mock
    private INextOfKinContactRepository nextOfKin;

    private AuthController controller;
    private boolean locked;

    private class FakeAuthenticationService implements AuthenticationService {
        @Override
        public AuthResponse login(LoginRequest request) {
            if (locked) {
                throw new LockedException("Too many failed login attempts. Try again later.");
            }
            if (!EMAIL.equals(request.email()) || !PASSWORD.equals(request.password())) {
                throw new BadCredentialsException("Invalid email or password.");
            }
            return AuthResponse.bearer("access-1", "refresh-1", "U001", request.email(), List.of(RoleType.MEMBER));
        }

        @Override
        public AuthResponse refresh(String refreshToken) {
            if (!"refresh-1".equals(refreshToken)) {
                throw new BadCredentialsException("The refresh token is not valid.");
            }
            return AuthResponse.bearer("access-2", "refresh-2", "U001", EMAIL, List.of(RoleType.MEMBER));
        }
    }

    @BeforeEach
    void setUp() {
        locked = false;
        RegistrationService registrationService = new RegistrationService(accounts, users, userRoles, addresses,
                contacts, genders, demographics, nextOfKin, new BCryptPasswordEncoder(),
                new PasswordPolicySelector(new StandardPasswordPolicy(), new StrictPasswordPolicy()));
        controller = new AuthController(new FakeAuthenticationService(), registrationService);
    }

    private void stubAccountAndUserSaves() {
        when(accounts.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(users.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    private RegisterRequest registerRequest(String password) {
        return new RegisterRequest("Collins", "Shibambo", EMAIL, password, LocalDate.of(2001, 5, 14));
    }

    @Test
    void loginReturnsTokensForValidCredentials() {
        ResponseEntity<AuthResponse> response = controller.login(new LoginRequest(EMAIL, PASSWORD));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("access-1", response.getBody().accessToken());
        assertEquals("refresh-1", response.getBody().refreshToken());
        assertEquals("Bearer", response.getBody().tokenType());
        assertEquals("U001", response.getBody().userId());
        assertEquals(List.of(RoleType.MEMBER), response.getBody().roles());
    }

    @Test
    void loginReturns401ForInvalidCredentials() {
        BadCredentialsException thrown = assertThrows(BadCredentialsException.class,
                () -> controller.login(new LoginRequest(EMAIL, "wrong")));

        ResponseEntity<Map<String, Object>> response = controller.handleBadCredentials(thrown);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(401, response.getBody().get("status"));
        assertEquals("Unauthorized", response.getBody().get("error"));
        assertEquals("Invalid email or password.", response.getBody().get("message"));
    }

    @Test
    void loginReturns429WhenLocked() {
        locked = true;

        LockedException thrown = assertThrows(LockedException.class,
                () -> controller.login(new LoginRequest(EMAIL, PASSWORD)));

        ResponseEntity<Map<String, Object>> response = controller.handleLocked(thrown);

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
        assertEquals(429, response.getBody().get("status"));
        assertEquals("Too many failed login attempts. Try again later.", response.getBody().get("message"));
    }

    @Test
    void registerCreatesMember() {
        stubAccountAndUserSaves();

        ResponseEntity<Map<String, Object>> response = controller.register(registerRequest(PASSWORD));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().get("userId"));
        assertEquals("Collins", response.getBody().get("firstName"));
        assertEquals("Shibambo", response.getBody().get("lastName"));
        assertEquals(EMAIL, response.getBody().get("email"));
    }

    @Test
    void registerResponseDoesNotExposeThePassword() {
        stubAccountAndUserSaves();

        ResponseEntity<Map<String, Object>> response = controller.register(registerRequest(PASSWORD));

        assertFalse(response.getBody().containsKey("password"));
        assertFalse(response.getBody().toString().contains(PASSWORD));
    }

    @Test
    void registerRejectsWeakPassword() {
        assertThrows(IllegalArgumentException.class, () -> controller.register(registerRequest("weak")));
    }

    @Test
    void refreshReturnsNewAccessToken() {
        Map<String, String> body = new HashMap<>();
        body.put("refreshToken", "refresh-1");

        ResponseEntity<AuthResponse> response = controller.refresh(body);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("access-2", response.getBody().accessToken());
    }

    @Test
    void refreshWithInvalidTokenIsRejected() {
        Map<String, String> body = new HashMap<>();
        body.put("refreshToken", "garbage");

        assertThrows(BadCredentialsException.class, () -> controller.refresh(body));
    }

    @Test
    void refreshWithMissingBodyOrKeyIsRejected() {
        assertThrows(BadCredentialsException.class, () -> controller.refresh(null));
        assertThrows(BadCredentialsException.class, () -> controller.refresh(new HashMap<>()));
    }
}
