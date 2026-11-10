/*
 * RegistrationServiceTest.java
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import za.ac.cput.domain.Account;
import za.ac.cput.domain.Address;
import za.ac.cput.domain.Contact;
import za.ac.cput.domain.Demographic;
import za.ac.cput.domain.Gender;
import za.ac.cput.domain.NextOfKinContact;
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

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    private static final String EMAIL = "collins@fitnova.co.za";

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

    private PasswordEncoder encoder;
    private RegistrationService service;

    @BeforeEach
    void setUp() {
        encoder = new BCryptPasswordEncoder();
        service = new RegistrationService(accounts, users, userRoles, addresses, contacts, genders,
                demographics, nextOfKin, encoder,
                new PasswordPolicySelector(new StandardPasswordPolicy(), new StrictPasswordPolicy()));
    }

    private void stubAccountAndUserSaves() {
        when(accounts.save(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(users.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    private RegisterRequest request(String email, String password) {
        return new RegisterRequest("Collins", "Shibambo", email, password, LocalDate.of(2001, 5, 14));
    }

    private RegisterRequest profileRequest(String streetName, String city, String province, String postalCode,
                                           String gender, String cellphone, RegisterRequest.NextOfKin kin) {
        return new RegisterRequest("Collins", "Shibambo", EMAIL, "Passw0rd1",
                LocalDate.of(2001, 5, 14), "12", streetName, "Woodstock", postalCode, province, city, null,
                gender, cellphone, "0731234567", kin);
    }

    private RegisterRequest.NextOfKin kin() {
        return new RegisterRequest.NextOfKin("Sipho", "Shibambo", "Father", "0821234567");
    }

    private Account existingAccount() {
        return new Account.Builder()
                .setAccountId("A001")
                .setEmail(EMAIL)
                .setPassword("hashed")
                .setRegistrationDate(LocalDate.of(2026, 1, 1))
                .build();
    }

    @Test
    void registersMemberWithFullProfile() {
        stubAccountAndUserSaves();

        User user = service.registerMember(request(EMAIL, "Passw0rd1"));

        assertNotNull(user.getUserId());
        assertEquals("Collins", user.getFirstName());
        assertEquals("Shibambo", user.getLastName());
        assertEquals(LocalDate.of(2001, 5, 14), user.getDateOfBirth());
        assertEquals(EMAIL, user.getAccount().getEmail());
        verify(users, times(1)).save(any(User.class));
    }

    @Test
    void savesTheMemberRole() {
        stubAccountAndUserSaves();

        service.registerMember(request(EMAIL, "Passw0rd1"));

        ArgumentCaptor<UserRole> captor = ArgumentCaptor.forClass(UserRole.class);
        verify(userRoles, times(1)).save(captor.capture());
        assertEquals(RoleType.MEMBER, captor.getValue().getRoleId());
        assertEquals("MEMBER", captor.getValue().getDescription());
    }

    @Test
    void registersStaffWithNamesEmailAndPassword() {
        stubAccountAndUserSaves();

        User user = service.registerStaff(
                new RegisterRequest("Thandi", "Nkosi", "thandi@fitnova.co.za", "Tr@iner123", null),
                RoleType.TRAINER);

        assertEquals("Thandi", user.getFirstName());
        assertEquals("Nkosi", user.getLastName());
        assertNull(user.getDateOfBirth());
        assertEquals("thandi@fitnova.co.za", user.getAccount().getEmail());
    }

    @Test
    void registersAdminRole() {
        stubAccountAndUserSaves();

        service.registerStaff(request("admin@fitnova.co.za", "Adm1n!Pass"), RoleType.ADMIN);

        ArgumentCaptor<UserRole> captor = ArgumentCaptor.forClass(UserRole.class);
        verify(userRoles).save(captor.capture());
        assertEquals(RoleType.ADMIN, captor.getValue().getRoleId());
    }

    @Test
    void staffRegistrationRejectsMemberRole() {
        assertThrows(IllegalArgumentException.class,
                () -> service.registerStaff(request(EMAIL, "Passw0rd1!"), RoleType.MEMBER));
        assertThrows(IllegalArgumentException.class,
                () -> service.registerStaff(request(EMAIL, "Passw0rd1!"), null));
        verify(accounts, never()).save(any(Account.class));
    }

    @Test
    void appliesStandardPolicyToMember() {
        stubAccountAndUserSaves();

        assertDoesNotThrow(() -> service.registerMember(request(EMAIL, "Passw0rd")));
    }

    @Test
    void rejectsWeakPasswordForMember() {
        assertThrows(IllegalArgumentException.class, () -> service.registerMember(request(EMAIL, "short1")));
        assertThrows(IllegalArgumentException.class, () -> service.registerMember(request(EMAIL, "onlyletters")));
        verify(accounts, never()).save(any(Account.class));
    }

    @Test
    void appliesStrictPolicyToStaff() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> service.registerStaff(request("staff@fitnova.co.za", "Passw0rd"), RoleType.TRAINER));

        assertTrue(thrown.getMessage().contains("strict"));
        verify(accounts, never()).save(any(Account.class));
        verify(users, never()).save(any(User.class));
        verify(userRoles, never()).save(any(UserRole.class));
    }

    @Test
    void rejectsDuplicateEmail() {
        when(accounts.findByEmail(EMAIL)).thenReturn(existingAccount());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                () -> service.registerMember(request(EMAIL, "Passw0rd1")));

        assertTrue(thrown.getMessage().contains("already exists"));
        verify(accounts, never()).save(any(Account.class));
    }

    @Test
    void rejectsDuplicateEmailWithDifferentCaseAndSpaces() {
        when(accounts.findByEmail(EMAIL)).thenReturn(existingAccount());

        assertThrows(IllegalArgumentException.class,
                () -> service.registerMember(request("  COLLINS@FitNova.co.za ", "Passw0rd1")));
    }

    @Test
    void storesEmailInLowerCase() {
        stubAccountAndUserSaves();

        service.registerMember(request("  Collins@FitNova.co.za ", "Passw0rd1"));

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accounts).save(captor.capture());
        assertEquals(EMAIL, captor.getValue().getEmail());
    }

    @Test
    void storesHashedPassword() {
        stubAccountAndUserSaves();

        service.registerMember(request(EMAIL, "Passw0rd1"));

        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accounts).save(captor.capture());
        String stored = captor.getValue().getPassword();
        assertNotEquals("Passw0rd1", stored);
        assertTrue(encoder.matches("Passw0rd1", stored));
        assertFalse(encoder.matches("Passw0rd2", stored));
    }

    @Test
    void rejectsMissingFields() {
        assertThrows(IllegalArgumentException.class, () -> service.registerMember(null));
        assertThrows(IllegalArgumentException.class, () -> service.registerMember(
                new RegisterRequest("", "Shibambo", EMAIL, "Passw0rd1", null)));
        assertThrows(IllegalArgumentException.class, () -> service.registerMember(
                new RegisterRequest("Collins", " ", EMAIL, "Passw0rd1", null)));
        assertThrows(IllegalArgumentException.class, () -> service.registerMember(
                new RegisterRequest("Collins", "Shibambo", "not-an-email", "Passw0rd1", null)));
        assertThrows(IllegalArgumentException.class, () -> service.registerMember(
                new RegisterRequest("Collins", "Shibambo", EMAIL, null, null)));
        verify(accounts, never()).save(any(Account.class));
    }

    @Test
    void trimsNames() {
        stubAccountAndUserSaves();

        User user = service.registerMember(
                new RegisterRequest("  Collins ", " Shibambo  ", EMAIL, "Passw0rd1", null));

        assertEquals("Collins", user.getFirstName());
        assertEquals("Shibambo", user.getLastName());
    }

    @Test
    void savesAddressContactGenderAndNextOfKinWhenProvided() {
        stubAccountAndUserSaves();
        when(addresses.save(any(Address.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(contacts.save(any(Contact.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(genders.findFirstByDescriptionIgnoreCase("Male")).thenReturn(Optional.empty());
        when(genders.save(any(Gender.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(demographics.save(any(Demographic.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User user = service.registerMember(
                profileRequest("Main Road", "Cape Town", "Western Cape", "8001", "Male", "0821112222", kin()));

        assertEquals("Main Road", user.getAddress().getStreetName());
        assertEquals("Cape Town", user.getAddress().getCity());
        assertEquals("South Africa", user.getAddress().getCountry());
        assertEquals("0821112222", user.getContact().getCellphoneNumber());
        assertEquals(EMAIL, user.getContact().getEmailAddress());
        assertEquals("Male", user.getDemographic().getGender().getDescription());

        ArgumentCaptor<NextOfKinContact> captor = ArgumentCaptor.forClass(NextOfKinContact.class);
        verify(nextOfKin).save(captor.capture());
        assertEquals("Sipho", captor.getValue().getFirstName());
        assertEquals("Father", captor.getValue().getRelationship());
        assertSame(user, captor.getValue().getUser());
    }

    @Test
    void savesNoProfileExtrasWhenNoneAreProvided() {
        stubAccountAndUserSaves();

        User user = service.registerMember(request(EMAIL, "Passw0rd1"));

        assertNull(user.getAddress());
        assertNull(user.getContact());
        assertNull(user.getDemographic());
        verify(addresses, never()).save(any(Address.class));
        verify(contacts, never()).save(any(Contact.class));
        verify(demographics, never()).save(any(Demographic.class));
        verify(nextOfKin, never()).save(any(NextOfKinContact.class));
    }

    @Test
    void reusesAnExistingGenderIgnoringCase() {
        stubAccountAndUserSaves();
        Gender existing = new Gender.Builder().setGenderId("G001").setDescription("Female").build();
        when(genders.findFirstByDescriptionIgnoreCase("FEMALE")).thenReturn(Optional.of(existing));
        when(demographics.save(any(Demographic.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User user = service.registerMember(profileRequest(null, null, null, null, "FEMALE", null, null));

        assertSame(existing, user.getDemographic().getGender());
        verify(genders, never()).save(any(Gender.class));
    }

    @Test
    void rejectsAnIncompleteAddress() {
        assertThrows(IllegalArgumentException.class, () -> service.registerMember(
                profileRequest("Main Road", null, "Western Cape", null, null, null, null)));
        verify(accounts, never()).save(any(Account.class));
    }

    @Test
    void rejectsAPostalCodeThatIsNotFourDigits() {
        assertThrows(IllegalArgumentException.class, () -> service.registerMember(
                profileRequest("Main Road", "Cape Town", "Western Cape", "80", null, null, null)));
    }

    @Test
    void rejectsAnInvalidCellphoneNumber() {
        assertThrows(IllegalArgumentException.class, () -> service.registerMember(
                profileRequest(null, null, null, null, null, "abc", null)));
    }

    @Test
    void rejectsIncompleteNextOfKin() {
        RegisterRequest.NextOfKin incomplete = new RegisterRequest.NextOfKin("Sipho", "", "Father", "0821234567");

        assertThrows(IllegalArgumentException.class, () -> service.registerMember(
                profileRequest(null, null, null, null, null, null, incomplete)));
        verify(accounts, never()).save(any(Account.class));
    }

    @Test
    void rejectsNextOfKinWithAnInvalidCellphoneNumber() {
        RegisterRequest.NextOfKin badPhone = new RegisterRequest.NextOfKin("Sipho", "Shibambo", "Father", "12");

        assertThrows(IllegalArgumentException.class, () -> service.registerMember(
                profileRequest(null, null, null, null, null, null, badPhone)));
    }
}
