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
import za.ac.cput.domain.User;
import za.ac.cput.domain.UserRole;
import za.ac.cput.domain.enums.RoleType;
import za.ac.cput.repository.IAccountRepository;
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
    private final PasswordEncoder encoder;
    private final PasswordPolicySelector policySelector;

    public RegistrationService(IAccountRepository accounts,
                               IUserRepository users,
                               IUserRoleRepository userRoles,
                               PasswordEncoder encoder,
                               PasswordPolicySelector policySelector) {
        this.accounts = accounts;
        this.users = users;
        this.userRoles = userRoles;
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

        User user = users.save(new User.Builder()
                .setUserId(Helper.generateId())
                .setFirstName(request.firstName().trim())
                .setLastName(request.lastName().trim())
                .setDateOfBirth(request.dateOfBirth())
                .setAccount(account)
                .build());

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
    }

    private String normalise(String email) {
        return email.trim().toLowerCase();
    }
}
