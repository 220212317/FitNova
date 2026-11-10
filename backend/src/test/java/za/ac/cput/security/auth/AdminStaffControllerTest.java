/*
 * AdminStaffControllerTest.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.security.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import za.ac.cput.domain.Account;
import za.ac.cput.domain.User;
import za.ac.cput.domain.UserRole;
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

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminStaffControllerTest {

    private static final String EMAIL = "thandi@fitnova.co.za";

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

    private AdminStaffController controller;

    @BeforeEach
    void setUp() {
        RegistrationService registrationService = new RegistrationService(accounts, users, userRoles, addresses,
                contacts, genders, demographics, nextOfKin, new BCryptPasswordEncoder(),
                new PasswordPolicySelector(new StandardPasswordPolicy(), new StrictPasswordPolicy()));
        controller = new AdminStaffController(registrationService);
    }

    private void stubAccountAndUserSaves() {
        when(accounts.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(users.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    private RegisterRequest request(String password) {
        return new RegisterRequest("Thandi", "Nkosi", EMAIL, password, null);
    }

    @Test
    void createStaffRequiresAdmin() throws NoSuchMethodException {
        Method create = AdminStaffController.class.getMethod("create", RegisterRequest.class, RoleType.class);

        PreAuthorize rule = create.getAnnotation(PreAuthorize.class);

        assertNotNull(rule);
        assertEquals("hasRole('ADMIN')", rule.value());
    }

    @Test
    void createStaffCreatesTrainer() {
        stubAccountAndUserSaves();

        ResponseEntity<Map<String, Object>> response = controller.create(request("Tr@iner123"), RoleType.TRAINER);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Thandi", response.getBody().get("firstName"));
        assertEquals(EMAIL, response.getBody().get("email"));
        assertFalse(response.getBody().containsKey("password"));

        ArgumentCaptor<UserRole> captor = ArgumentCaptor.forClass(UserRole.class);
        verify(userRoles).save(captor.capture());
        assertEquals(RoleType.TRAINER, captor.getValue().getRoleId());
    }

    @Test
    void createStaffCreatesAdmin() {
        stubAccountAndUserSaves();

        controller.create(request("Adm1n!Pass"), RoleType.ADMIN);

        ArgumentCaptor<UserRole> captor = ArgumentCaptor.forClass(UserRole.class);
        verify(userRoles).save(captor.capture());
        assertEquals(RoleType.ADMIN, captor.getValue().getRoleId());
    }

    @Test
    void createStaffAppliesStrictPasswordPolicy() {
        assertThrows(IllegalArgumentException.class, () -> controller.create(request("Passw0rd"), RoleType.TRAINER));
        assertThrows(IllegalArgumentException.class, () -> controller.create(request("Passw0rd"), RoleType.ADMIN));
        verify(accounts, never()).save(any(Account.class));
    }

    @Test
    void createStaffRejectsMemberRole() {
        assertThrows(IllegalArgumentException.class, () -> controller.create(request("Tr@iner123"), RoleType.MEMBER));
        verify(accounts, never()).save(any(Account.class));
    }
}
