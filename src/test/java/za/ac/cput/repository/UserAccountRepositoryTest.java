package za.ac.cput.repository;

/**

 * 26 March 2026
 * Author: Avuyile Sitoyi
 * (240971051)
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.entity.UserAccount;
import za.ac.cput.entity.enums.RoleType;
import za.ac.cput.factory.UserAccountFactory;
import za.ac.cput.repository.impl.UserAccountRepositoryImpl;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

    class UserAccountRepositoryTest {

        private UserAccountRepositoryImpl repo;

        private UserAccount account1;
        private UserAccount account2;

        @BeforeEach
        void setUp() {
            repo = UserAccountRepositoryImpl.getRepository();

            // Clean up singleton state between tests
            repo.delete("ACC001");
            repo.delete("ACC002");

            account1 = UserAccountFactory.CreateUserAccount(
                    "ACC001", "avuyile.sitoyi@example.com", "SecurePass123",
                    RoleType.MEMBER, LocalDate.of(2024, 1, 10));

            account2 = UserAccountFactory.CreateUserAccount(
                    "ACC002", "sipho.ndlovu@gmail.com", "TrainerPass456",
                    RoleType.TRAINER, LocalDate.of(2024, 6, 20));
        }



        @Test
        void testGetRepository_ReturnsSameInstance() {
            UserAccountRepositoryImpl instance1 = UserAccountRepositoryImpl.getRepository();
            UserAccountRepositoryImpl instance2 = UserAccountRepositoryImpl.getRepository();
            assertSame(instance1, instance2);
        }



        @Test
        void testCreate_NotNull() {
            UserAccount created = repo.create(account1);
            assertNotNull(created);
        }

        @Test
        void testCreate_ReturnsCorrectAccount() {
            UserAccount created = repo.create(account1);
            assertEquals(account1.getAccountId(), created.getAccountId());
        }

        @Test
        void testCreate_StoresAccount() {
            repo.create(account1);
            assertNotNull(repo.read("ACC001"));
        }

        @Test
        void testCreate_CorrectEmail() {
            repo.create(account1);
            assertEquals("avuyile.sitoyi@gmail.com", repo.read("ACC001").getEmail());
        }

        @Test
        void testCreate_CorrectRole() {
            repo.create(account1);
            assertEquals(RoleType.MEMBER, repo.read("ACC001").getRole());
        }

        @Test
        void testCreate_MultipleAccounts() {
            repo.create(account1);
            repo.create(account2);
            assertNotNull(repo.read("ACC001"));
            assertNotNull(repo.read("ACC002"));
        }



        @Test
        void testRead_ExistingAccount_NotNull() {
            repo.create(account1);
            assertNotNull(repo.read("ACC001"));
        }

        @Test
        void testRead_ExistingAccount_CorrectEmail() {
            repo.create(account1);
            assertEquals("avuyile.sitoyi@gmail.com", repo.read("ACC001").getEmail());
        }

        @Test
        void testRead_ExistingAccount_CorrectPassword() {
            repo.create(account1);
            assertEquals("SecurePass123", repo.read("ACC001").getPassword());
        }

        @Test
        void testRead_ExistingAccount_CorrectRole() {
            repo.create(account1);
            assertEquals(RoleType.MEMBER, repo.read("ACC001").getRole());
        }

        @Test
        void testRead_ExistingAccount_CorrectRegistrationDate() {
            repo.create(account1);
            assertEquals(LocalDate.of(2024, 1, 10), repo.read("ACC001").getRegistrationDate());
        }

        @Test
        void testRead_NonExistentAccount_ReturnsNull() {
            assertNull(repo.read("DOES_NOT_EXIST"));
        }



        @Test
        void testFindAll_NotNull() {
            assertNotNull(repo.findAll());
        }
}
