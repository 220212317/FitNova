package za.ac.cput.security.directory;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.prepost.PreAuthorize;
import za.ac.cput.domain.Account;
import za.ac.cput.domain.User;
import za.ac.cput.domain.UserRole;
import za.ac.cput.domain.enums.RoleType;
import za.ac.cput.repository.IUserRoleRepository;

import java.time.LocalDate;
import java.lang.reflect.Proxy;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/*
 * TrainerDirectoryControllerTest.java
 * Author: Avuyile Sitoyi (240971051)
 */
class TrainerDirectoryControllerTest {
    @Test
    void returnsOnlyTrainers() {
        IUserRoleRepository userRoles = repositoryReturning(List.of(trainerRole()));
        TrainerDirectoryController controller = new TrainerDirectoryController(userRoles);

        List<TrainerSummary> result = controller.getAll();

        assertEquals(List.of(new TrainerSummary("trainer-1", "Tara", "Trainer")), result);
    }

    @Test
    void requiresAuthentication() throws NoSuchMethodException {
        PreAuthorize rule = TrainerDirectoryController.class.getMethod("getAll").getAnnotation(PreAuthorize.class);
        assertNotNull(rule);
        assertEquals("isAuthenticated()", rule.value());
    }

    @Test
    void doesNotExposeEmailOrPassword() throws Exception {
        String json = new ObjectMapper().writeValueAsString(TrainerSummary.from(trainerRole().getUser()));

        assertFalse(json.contains("email"));
        assertFalse(json.contains("password"));
        assertFalse(json.contains("account"));
    }

    private static UserRole trainerRole() {
        Account account = new Account.Builder()
                .setAccountId("account-1")
                .setEmail("private@fitnova.test")
                .setPassword("never-return-this")
                .setRegistrationDate(LocalDate.of(2026, 1, 1))
                .build();
        User trainer = new User.Builder()
                .setUserId("trainer-1")
                .setFirstName("Tara")
                .setLastName("Trainer")
                .setAccount(account)
                .build();
        return new UserRole.Builder()
                .setUserRoleId("role-1")
                .setUser(trainer)
                .setRoleId(RoleType.TRAINER)
                .build();
    }

    private static IUserRoleRepository repositoryReturning(List<UserRole> roles) {
        return (IUserRoleRepository) Proxy.newProxyInstance(
                IUserRoleRepository.class.getClassLoader(),
                new Class<?>[]{IUserRoleRepository.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("findByRoleId")) return roles;
                    if (method.getName().equals("toString")) return "trainer-role-repository";
                    if (method.getReturnType().equals(boolean.class)) return false;
                    if (method.getReturnType().equals(long.class)) return 0L;
                    return null;
                });
    }
}
