/*
 * RegistrationService.java
 * Author: Collins Shibambo
 * 230093183
 */
package za.ac.cput.security.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import za.ac.cput.util.Helper;

import java.time.LocalDate;

@Service
public class RegistrationService {

    private final IAccountRepository accounts;
    private final IUserRepository users;
    private final IUserRoleRepository userRoles;
    private final IAddressRepository addresses;
    private final IContactRepository contacts;
    private final IGenderRepository genders;
    private final IDemographicRepository demographics;
    private final INextOfKinContactRepository nextOfKin;
    private final PasswordEncoder encoder;
    private final PasswordPolicySelector policySelector;

    public RegistrationService(IAccountRepository accounts,
                               IUserRepository users,
                               IUserRoleRepository userRoles,
                               IAddressRepository addresses,
                               IContactRepository contacts,
                               IGenderRepository genders,
                               IDemographicRepository demographics,
                               INextOfKinContactRepository nextOfKin,
                               PasswordEncoder encoder,
                               PasswordPolicySelector policySelector) {
        this.accounts = accounts;
        this.users = users;
        this.userRoles = userRoles;
        this.addresses = addresses;
        this.contacts = contacts;
        this.genders = genders;
        this.demographics = demographics;
        this.nextOfKin = nextOfKin;
        this.encoder = encoder;
        this.policySelector = policySelector;
    }

    @Transactional
    public User registerMember(RegisterRequest request) {
        return register(request, RoleType.MEMBER);
    }

    @Transactional
    public User registerStaff(RegisterRequest request, RoleType role) {
        if (role == null || role == RoleType.MEMBER) {
            throw new IllegalArgumentException("Staff registration requires the TRAINER or ADMIN role.");
        }
        return register(request, role);
    }

    private User register(RegisterRequest request, RoleType role) {
        validate(request);

        String email = normalise(request.email());

        if (accounts.findByEmail(email) != null) {
            throw new IllegalArgumentException("An account with this email already exists. Use a different email.");
        }

        policySelector.policyFor(role).enforce(request.password());

        Account account = accounts.save(new Account.Builder()
                .setAccountId(Helper.generateId())
                .setEmail(email)
                .setPassword(encoder.encode(request.password()))
                .setRegistrationDate(LocalDate.now())
                .build());

        Address address = null;
        if (hasAddress(request)) {
            address = addresses.save(new Address.Builder()
                    .setAddressId(Helper.generateId())
                    .setStreetNumber(Helper.isNullOrEmpty(request.streetNumber()) ? null : request.streetNumber().trim())
                    .setStreetName(request.streetName().trim())
                    .setSuburbName(Helper.isNullOrEmpty(request.suburbName()) ? null : request.suburbName().trim())
                    .setPostalCode(Helper.isNullOrEmpty(request.postalCode()) ? null : request.postalCode().trim())
                    .setCity(request.city().trim())
                    .setProvince(request.province().trim())
                    .setCountry(Helper.isNullOrEmpty(request.country()) ? "South Africa" : request.country().trim())
                    .build());
        }

        Contact contact = null;
        if (!Helper.isNullOrEmpty(request.cellphoneNumber())) {
            contact = contacts.save(new Contact.Builder()
                    .setContactId(Helper.generateId())
                    .setCellphoneNumber(request.cellphoneNumber().trim())
                    .setAlternativeCellphoneNumber(Helper.isNullOrEmpty(request.alternativeCellphoneNumber())
                            ? null : request.alternativeCellphoneNumber().trim())
                    .setEmailAddress(email)
                    .build());
        }

        Demographic demographic = null;
        if (!Helper.isNullOrEmpty(request.gender())) {
            String description = request.gender().trim();
            Gender gender = genders.findFirstByDescriptionIgnoreCase(description)
                    .orElseGet(() -> genders.save(new Gender.Builder()
                            .setGenderId(Helper.generateId())
                            .setDescription(description)
                            .build()));
            demographic = demographics.save(new Demographic.Builder()
                    .setDemographyId(Helper.generateId())
                    .setGender(gender)
                    .build());
        }

        User user = users.save(new User.Builder()
                .setUserId(Helper.generateId())
                .setFirstName(request.firstName().trim())
                .setLastName(request.lastName().trim())
                .setDateOfBirth(request.dateOfBirth())
                .setAccount(account)
                .setAddress(address)
                .setContact(contact)
                .setDemographic(demographic)
                .build());

        RegisterRequest.NextOfKin kin = request.nextOfKin();
        if (kin != null) {
            nextOfKin.save(new NextOfKinContact.Builder()
                    .setNextOfKinContactId(Helper.generateId())
                    .setFirstName(kin.firstName().trim())
                    .setLastName(kin.lastName().trim())
                    .setRelationship(kin.relationship().trim())
                    .setCellphoneNumber(kin.cellphoneNumber().trim())
                    .setUser(user)
                    .build());
        }

        userRoles.save(new UserRole.Builder()
                .setUserRoleId(Helper.generateId())
                .setUser(user)
                .setRoleId(role)
                .setDescription(role.name())
                .build());

        return user;
    }

    private void validate(RegisterRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Registration details are required.");
        }
        if (Helper.isNullOrEmpty(request.firstName())) {
            throw new IllegalArgumentException("First name is required.");
        }
        if (Helper.isNullOrEmpty(request.lastName())) {
            throw new IllegalArgumentException("Last name is required.");
        }
        if (!Helper.isValidEmail(request.email())) {
            throw new IllegalArgumentException("A valid email address is required.");
        }
        if (Helper.isNullOrEmpty(request.password())) {
            throw new IllegalArgumentException("Password is required.");
        }
        if (hasAddress(request)) {
            if (Helper.isNullOrEmpty(request.streetName()) || Helper.isNullOrEmpty(request.city())
                    || Helper.isNullOrEmpty(request.province())) {
                throw new IllegalArgumentException("Street name, city and province are required for an address.");
            }
            if (!Helper.isNullOrEmpty(request.postalCode()) && !request.postalCode().trim().matches("^\\d{4}$")) {
                throw new IllegalArgumentException("Postal code must be 4 digits.");
            }
        }
        if (!Helper.isNullOrEmpty(request.cellphoneNumber()) && !isValidPhone(request.cellphoneNumber())) {
            throw new IllegalArgumentException("Enter a valid cellphone number.");
        }
        if (!Helper.isNullOrEmpty(request.alternativeCellphoneNumber()) && !isValidPhone(request.alternativeCellphoneNumber())) {
            throw new IllegalArgumentException("Enter a valid alternative cellphone number.");
        }
        RegisterRequest.NextOfKin kin = request.nextOfKin();
        if (kin != null) {
            if (Helper.isNullOrEmpty(kin.firstName()) || Helper.isNullOrEmpty(kin.lastName())
                    || Helper.isNullOrEmpty(kin.relationship())) {
                throw new IllegalArgumentException("Next of kin first name, last name and relationship are required.");
            }
            if (Helper.isNullOrEmpty(kin.cellphoneNumber()) || !isValidPhone(kin.cellphoneNumber())) {
                throw new IllegalArgumentException("Enter a valid next of kin cellphone number.");
            }
        }
    }

    private boolean hasAddress(RegisterRequest request) {
        return !Helper.isNullOrEmpty(request.streetName())
                || !Helper.isNullOrEmpty(request.city())
                || !Helper.isNullOrEmpty(request.province());
    }

    private boolean isValidPhone(String phone) {
        return phone.trim().matches("^\\+?[0-9 ]{9,15}$");
    }

    private String normalise(String email) {
        return email.trim().toLowerCase();
    }
}
