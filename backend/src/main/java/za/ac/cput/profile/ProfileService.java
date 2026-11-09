package za.ac.cput.profile;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.ac.cput.domain.Address;
import za.ac.cput.domain.Contact;
import za.ac.cput.domain.Demographic;
import za.ac.cput.domain.Gender;
import za.ac.cput.domain.NextOfKinContact;
import za.ac.cput.domain.User;
import za.ac.cput.domain.UserRole;
import za.ac.cput.domain.enums.RoleType;
import za.ac.cput.repository.IAddressRepository;
import za.ac.cput.repository.IContactRepository;
import za.ac.cput.repository.IDemographicRepository;
import za.ac.cput.repository.IGenderRepository;
import za.ac.cput.repository.INextOfKinContactRepository;
import za.ac.cput.repository.IUserRepository;
import za.ac.cput.repository.IUserRoleRepository;
import za.ac.cput.service.IUserService;
import za.ac.cput.util.Helper;

import java.util.List;
import java.util.NoSuchElementException;

/** Self-service profile operations: a user can only ever act on their own record. */
@Service
public class ProfileService {

    private static final java.util.regex.Pattern PHONE = java.util.regex.Pattern.compile("^\\+?[0-9 ]{9,15}$");

    private final IUserRepository users;
    private final IAddressRepository addresses;
    private final IContactRepository contacts;
    private final IGenderRepository genders;
    private final IDemographicRepository demographics;
    private final INextOfKinContactRepository nextOfKin;
    private final IUserRoleRepository userRoles;
    private final IUserService userService;
    private final PasswordEncoder encoder;

    public ProfileService(IUserRepository users,
                          IAddressRepository addresses,
                          IContactRepository contacts,
                          IGenderRepository genders,
                          IDemographicRepository demographics,
                          INextOfKinContactRepository nextOfKin,
                          IUserRoleRepository userRoles,
                          IUserService userService,
                          PasswordEncoder encoder) {
        this.users = users;
        this.addresses = addresses;
        this.contacts = contacts;
        this.genders = genders;
        this.demographics = demographics;
        this.nextOfKin = nextOfKin;
        this.userRoles = userRoles;
        this.userService = userService;
        this.encoder = encoder;
    }

    @Transactional(readOnly = true)
    public ProfileResponse get(String userId) {
        return toResponse(load(userId));
    }

    @Transactional
    public ProfileResponse update(String userId, ProfileUpdateRequest req) {
        if (req == null) throw new IllegalArgumentException("Profile details are required.");
        User user = load(userId);

        if (Helper.isNullOrEmpty(req.firstName()) || Helper.isNullOrEmpty(req.lastName())) {
            throw new IllegalArgumentException("First name and last name are required.");
        }
        if (req.dateOfBirth() == null || req.dateOfBirth().isAfter(java.time.LocalDate.now())) {
            throw new IllegalArgumentException("Enter a valid date of birth.");
        }
        validatePhone(req.cellphoneNumber(), "cellphone number");
        validatePhone(req.alternativeCellphoneNumber(), "alternative cellphone number");
        if (!Helper.isNullOrEmpty(req.postalCode()) && !req.postalCode().trim().matches("^\\d{4}$")) {
            throw new IllegalArgumentException("Postal code must be 4 digits.");
        }

        // Address
        Address address = user.getAddress();
        if (!Helper.isNullOrEmpty(req.streetName()) || !Helper.isNullOrEmpty(req.city())) {
            if (Helper.isNullOrEmpty(req.streetName()) || Helper.isNullOrEmpty(req.city())
                    || Helper.isNullOrEmpty(req.province())) {
                throw new IllegalArgumentException("Street name, city and province are required for an address.");
            }
            address = addresses.save(new Address.Builder()
                    .setAddressId(address != null ? address.getAddressId() : Helper.generateId())
                    .setStreetNumber(trimOrNull(req.streetNumber()))
                    .setStreetName(req.streetName().trim())
                    .setSuburbName(trimOrNull(req.suburbName()))
                    .setCity(req.city().trim())
                    .setPostalCode(trimOrNull(req.postalCode()))
                    .setProvince(req.province().trim())
                    .setCountry(Helper.isNullOrEmpty(req.country()) ? "South Africa" : req.country().trim())
                    .build());
        }

        // Contact (email stays tied to the login account)
        Contact contact = user.getContact();
        if (!Helper.isNullOrEmpty(req.cellphoneNumber())) {
            contact = contacts.save(new Contact.Builder()
                    .setContactId(contact != null ? contact.getContactId() : Helper.generateId())
                    .setCellphoneNumber(req.cellphoneNumber().trim())
                    .setAlternativeCellphoneNumber(trimOrNull(req.alternativeCellphoneNumber()))
                    .setEmailAddress(user.getAccount() != null ? user.getAccount().getEmail() : null)
                    .build());
        }

        // Gender (race is preserved)
        Demographic demographic = user.getDemographic();
        if (!Helper.isNullOrEmpty(req.gender())) {
            String description = req.gender().trim();
            Gender gender = genders.findFirstByDescriptionIgnoreCase(description)
                    .orElseGet(() -> genders.save(new Gender.Builder()
                            .setGenderId(Helper.generateId())
                            .setDescription(description)
                            .build()));
            demographic = demographics.save(new Demographic.Builder()
                    .setDemographyId(demographic != null ? demographic.getDemographyId() : Helper.generateId())
                    .setGender(gender)
                    .setRace(demographic != null ? demographic.getRace() : null)
                    .build());
        }

        User saved = users.save(new User.Builder().copy(user)
                .setFirstName(req.firstName().trim())
                .setLastName(req.lastName().trim())
                .setDateOfBirth(req.dateOfBirth())
                .setAddress(address)
                .setContact(contact)
                .setDemographic(demographic)
                .build());

        // Next of kin: update the first record, or create one
        ProfileResponse.NextOfKinDto kin = req.nextOfKin();
        if (kin != null && !Helper.isNullOrEmpty(kin.firstName())) {
            if (Helper.isNullOrEmpty(kin.lastName()) || Helper.isNullOrEmpty(kin.relationship())) {
                throw new IllegalArgumentException("Next of kin first name, last name and relationship are required.");
            }
            if (Helper.isNullOrEmpty(kin.cellphoneNumber()) || !PHONE.matcher(kin.cellphoneNumber().trim()).matches()) {
                throw new IllegalArgumentException("Enter a valid next of kin cellphone number.");
            }
            List<NextOfKinContact> existing = nextOfKin.findByUser(saved);
            String kinId = existing.isEmpty() ? Helper.generateId() : existing.get(0).getNextOfKinContactId();
            nextOfKin.save(new NextOfKinContact.Builder()
                    .setNextOfKinContactId(kinId)
                    .setFirstName(kin.firstName().trim())
                    .setLastName(kin.lastName().trim())
                    .setRelationship(kin.relationship().trim())
                    .setCellphoneNumber(kin.cellphoneNumber().trim())
                    .setUser(saved)
                    .build());
        }

        return toResponse(load(userId));
    }

    @Transactional
    public void delete(String userId, String password) {
        User user = load(userId);
        if (Helper.isNullOrEmpty(password) || user.getAccount() == null
                || !encoder.matches(password, user.getAccount().getPassword())) {
            throw new IllegalArgumentException("Incorrect password. Your profile was not deleted.");
        }
        boolean isAdmin = userRoles.findByUser_UserId(userId).stream()
                .anyMatch(r -> r.getRoleId() == RoleType.ADMIN);
        if (isAdmin && userRoles.findByRoleId(RoleType.ADMIN).size() <= 1) {
            throw new IllegalArgumentException("You are the only administrator, so this profile cannot be deleted.");
        }
        userService.delete(userId);
    }

    private User load(String userId) {
        return users.findById(userId).orElseThrow(() -> new NoSuchElementException("User not found."));
    }

    private ProfileResponse toResponse(User u) {
        Address a = u.getAddress();
        Contact c = u.getContact();
        NextOfKinContact k = nextOfKin.findByUser(u).stream().findFirst().orElse(null);
        List<String> roles = userRoles.findByUser_UserId(u.getUserId()).stream()
                .map(UserRole::getRoleId).map(Enum::name).toList();
        return new ProfileResponse(
                u.getUserId(), u.getFirstName(), u.getLastName(), u.getDateOfBirth(),
                u.getAccount() != null ? u.getAccount().getEmail() : null,
                u.getAccount() != null ? u.getAccount().getRegistrationDate() : null,
                roles,
                u.getDemographic() != null && u.getDemographic().getGender() != null
                        ? u.getDemographic().getGender().getDescription() : null,
                a != null ? a.getStreetNumber() : null,
                a != null ? a.getStreetName() : null,
                a != null ? a.getSuburbName() : null,
                a != null ? a.getCity() : null,
                a != null ? a.getPostalCode() : null,
                a != null ? a.getProvince() : null,
                a != null ? a.getCountry() : null,
                c != null ? c.getCellphoneNumber() : null,
                c != null ? c.getAlternativeCellphoneNumber() : null,
                k == null ? null : new ProfileResponse.NextOfKinDto(
                        k.getFirstName(), k.getLastName(), k.getRelationship(), k.getCellphoneNumber()));
    }

    private static void validatePhone(String phone, String label) {
        if (!Helper.isNullOrEmpty(phone) && !PHONE.matcher(phone.trim()).matches()) {
            throw new IllegalArgumentException("Enter a valid " + label + ".");
        }
    }

    private static String trimOrNull(String s) {
        return Helper.isNullOrEmpty(s) ? null : s.trim();
    }
}
