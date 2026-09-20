package za.ac.cput.factory;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import za.ac.cput.domain.User;
import za.ac.cput.domain.UserRole;
import za.ac.cput.domain.enums.RoleType;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/*
 * UserRoleFactoryTest.java
 * Author: Collins SHibambo
 * 230093183
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
class UserRoleFactoryTest {

    private User buildUser() {
        return new User.Builder()
                .setUserId("U001")
                .setFirstName("Thando")
                .setLastName("Nkosi")
                .setDateOfBirth(LocalDate.of(1999, 5, 20))
                .build();
    }

    @Test
    @Order(1)
    void createUserRoleValid() {
        UserRole userRole = UserRoleFactory.createUserRole(buildUser(), RoleType.MEMBER, "Standard gym member");

        assertNotNull(userRole);
        assertNotNull(userRole.getUserRoleId());
        assertEquals(RoleType.MEMBER, userRole.getRoleId());
        assertEquals("Standard gym member", userRole.getDescription());
        System.out.println("Valid UserRole: " + userRole);
    }

    @Test
    @Order(2)
    void createUserRoleWithSuppliedId() {
        UserRole userRole = UserRoleFactory.createUserRole("UR001", buildUser(), RoleType.TRAINER, "Certified trainer");

        assertNotNull(userRole);
        assertEquals("UR001", userRole.getUserRoleId());
        assertEquals(RoleType.TRAINER, userRole.getRoleId());
        System.out.println("UserRole with Supplied ID: " + userRole);
    }

    @Test
    @Order(3)
    void createUserRoleWithNullDescriptionIsAllowed() {
        UserRole userRole = UserRoleFactory.createUserRole(buildUser(), RoleType.ADMIN, null);

        assertNotNull(userRole);
        assertNull(userRole.getDescription());
    }

    @Test
    @Order(4)
    void createUserRoleNullUserReturnsNull() {
        UserRole userRole = UserRoleFactory.createUserRole(null, RoleType.MEMBER, "Standard gym member");

        assertNull(userRole);
    }

    @Test
    @Order(5)
    void createUserRoleNullRoleTypeReturnsNull() {
        UserRole userRole = UserRoleFactory.createUserRole(buildUser(), null, "Standard gym member");

        assertNull(userRole);
    }
}