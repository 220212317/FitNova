package za.ac.cput.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;
import za.ac.cput.domain.Account;
import za.ac.cput.domain.User;
import za.ac.cput.domain.UserRole;
import za.ac.cput.domain.enums.RoleType;
import za.ac.cput.repository.IAccountRepository;
import za.ac.cput.repository.IUserRepository;
import za.ac.cput.repository.IUserRoleRepository;
import za.ac.cput.security.token.AccessTokenService;

import java.time.LocalDate;

/**
 * Test helper: makes sure a user with the requested role exists, issues a real
 * access token for it and attaches it to a TestRestTemplate as a Bearer header.
 * Needed because SecurityConfig protects every endpoint except login/register.
 */
@Component
public class TestAuth {

    private final IAccountRepository accounts;
    private final IUserRepository users;
    private final IUserRoleRepository userRoles;
    private final AccessTokenService tokens;

    @Autowired
    public TestAuth(IAccountRepository accounts, IUserRepository users,
                    IUserRoleRepository userRoles, AccessTokenService tokens) {
        this.accounts = accounts;
        this.users = users;
        this.userRoles = userRoles;
        this.tokens = tokens;
    }

    /** Every request sent by this template will carry a token for a user with the given role. */
    public void authenticateAs(TestRestTemplate template, RoleType role) {
        String token = tokenFor(role);
        var interceptors = template.getRestTemplate().getInterceptors();
        interceptors.removeIf(i -> i instanceof BearerInterceptor);
        interceptors.add(new BearerInterceptor(token));
    }

    public String tokenFor(RoleType role) {
        String name = role.name().toLowerCase();
        String email = "test." + name + "@fitnova.test";
        String userId = "TEST-AUTH-" + role.name();

        // Look up by email each time: another Spring context may have recreated the schema.
        if (accounts.findByEmail(email) == null) {
            Account account = accounts.save(new Account.Builder()
                    .setAccountId("ACC-TEST-AUTH-" + role.name())
                    .setEmail(email)
                    .setPassword("not-used-tokens-are-issued-directly")
                    .setRegistrationDate(LocalDate.now())
                    .build());

            User user = users.save(new User.Builder()
                    .setUserId(userId)
                    .setFirstName("Test")
                    .setLastName(role.name())
                    .setDateOfBirth(LocalDate.of(1990, 1, 1))
                    .setAccount(account)
                    .build());

            userRoles.save(new UserRole.Builder()
                    .setUserRoleId("UR-TEST-AUTH-" + role.name())
                    .setUser(user)
                    .setRoleId(role)
                    .setDescription("Test " + name)
                    .build());
        }
        return tokens.issue(email, userId);
    }

    private static final class BearerInterceptor implements ClientHttpRequestInterceptor {
        private final String token;

        private BearerInterceptor(String token) {
            this.token = token;
        }

        @Override
        public org.springframework.http.client.ClientHttpResponse intercept(
                org.springframework.http.HttpRequest request, byte[] body,
                org.springframework.http.client.ClientHttpRequestExecution execution)
                throws java.io.IOException {
            request.getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            return execution.execute(request, body);
        }
    }
}
