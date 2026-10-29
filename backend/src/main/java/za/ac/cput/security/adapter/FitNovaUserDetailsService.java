package za.ac.cput.security.adapter;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import za.ac.cput.domain.Account;
import za.ac.cput.domain.User;
import za.ac.cput.domain.UserRole;
import za.ac.cput.domain.enums.RoleType;
import za.ac.cput.repository.IAccountRepository;
import za.ac.cput.repository.IUserRepository;
import za.ac.cput.repository.IUserRoleRepository;
import za.ac.cput.security.permission.RoleAuthorityResolver;

import java.util.List;
import java.util.Optional;

@Service
public class FitNovaUserDetailsService implements UserDetailsService {

    private final IAccountRepository accounts;
    private final IUserRepository users;
    private final IUserRoleRepository userRoles;
    private final RoleAuthorityResolver resolver;

    public FitNovaUserDetailsService(IAccountRepository accounts, IUserRepository users,
                                     IUserRoleRepository userRoles, RoleAuthorityResolver resolver) {
        this.accounts = accounts;
        this.users = users;
        this.userRoles = userRoles;
        this.resolver = resolver;
    }

    @Override
    @Transactional(readOnly = true)
    public AuthenticatedUser loadUserByUsername(String email) {
        Account account = accounts.findByEmail(email == null ? "" : email.trim().toLowerCase());
        if (account == null) {
            throw new UsernameNotFoundException("Unknown account");
        }

        // Walk the relationship from Account → User
        User user = account.getUser();
        if (user == null) {
            // fallback: search all users (only used if the bidirectional link is not loaded)
            user = users.findAll().stream()
                    .filter(u -> u.getAccount() != null
                            && account.getAccountId().equals(u.getAccount().getAccountId()))
                    .findFirst()
                    .orElseThrow(() -> new UsernameNotFoundException("No profile for account"));
        }

        List<RoleType> roles = userRoles.findByUser_UserId(user.getUserId()).stream()
                .map(UserRole::getRoleId)
                .toList();

        if (roles.isEmpty()) {
            roles = List.of(RoleType.MEMBER);
        }

        return new AuthenticatedUser.Builder()
                .setUserId(user.getUserId())
                .setEmail(account.getEmail())
                .setPasswordHash(account.getPassword())
                .setDisplayName((user.getFirstName() + " " + user.getLastName()).trim())
                .setAuthorities(resolver.resolve(roles))
                .build();
    }
}