package za.ac.cput.entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.entity.enums.RoleType;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
/**
 * UserAccountTest.java
 * Unit test for UserAccountTest
 * 26 March 2026
 * Author: Avuyile Sitoyi
 * (240971051)
 */

public class UserAccountTest
{






        private UserAccount userAccount;

        @BeforeEach
        void setUp() {
            userAccount = new UserAccount.Builder()
                    .accountId("ACC001")
                    .email("trainer@gmail.com")
                    .password("TrainerPass123")
                    .role(RoleType.TRAINER)
                    .registrationDate(LocalDate.of(2024, 1, 10))
                    .build();
        }

        @Test
        void testUserAccount_NotNull() {
            assertNotNull(userAccount);
        }

        @Test
        void testAccountId() {
            assertEquals("ACC001", userAccount.getAccountId());
        }

        @Test
        void testEmail() {
            assertEquals("trainer@gmail.com", userAccount.getEmail());
        }

        @Test
        void testPassword() {
            assertEquals("TrainerPass123", userAccount.getPassword());
        }

        @Test
        void testRole() {
            assertEquals(RoleType.TRAINER, userAccount.getRole());
        }

        @Test
        void testRegistrationDate() {
            assertEquals(LocalDate.of(2024, 1, 10),
                    userAccount.getRegistrationDate());
        }

        @Test
        void testSetEmail() {
            userAccount.setEmail("updated@gmail.com");
            assertEquals("updated@gmail.com", userAccount.getEmail());
        }

        @Test
        void testSetPassword() {
            userAccount.setPassword("NewPassword123");
            assertEquals("NewPassword123", userAccount.getPassword());
        }

        @Test
        void testSetRole() {
            userAccount.setRole(RoleType.MEMBER);
            assertEquals(RoleType.MEMBER, userAccount.getRole());
        }

        @Test
        void testSetRegistrationDate() {
            LocalDate newDate = LocalDate.of(2025, 1, 1);
            userAccount.setRegistrationDate(newDate);
            assertEquals(newDate, userAccount.getRegistrationDate());
        }




}
