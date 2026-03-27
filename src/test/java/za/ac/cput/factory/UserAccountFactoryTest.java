package za.ac.cput.factory;

/**
 * UserAccountFactoryTest.java
 * Unit test for UserAccountFactory
 * 26 March 2026
 * Author: Avuyile Sitoyi
 * (240971051)
 */

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.entity.UserAccount;
import za.ac.cput.entity.enums.RoleType;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserAccountFactoryTest {

    private static final String ACCOUNT_ID = "ACC001";
    private static final String EMAIL = "avuyile.sitoyi@gmail.com";
    private static final String PASSWORD = "SecurePass123";
    private static final RoleType ROLE = RoleType.MEMBER;
    private static final LocalDate REG_DATE = LocalDate.of(2024, 1, 10);

    private UserAccount account;

    @BeforeEach
    void setUp() {
        account = UserAccountFactory.CreateUserAccount(ACCOUNT_ID, EMAIL, PASSWORD, ROLE, REG_DATE);
    }


    @Test
    void testCreateUserAccount_Success_NotNull() {
        assertNotNull(account);
    }

    @Test
    void testCreateUserAccount_Success_AccountId() {
        assertEquals(ACCOUNT_ID, account.getAccountId());
    }

    @Test
    void testCreateUserAccount_Success_Email() {
        assertEquals(EMAIL, account.getEmail());
    }

    @Test
    void testCreateUserAccount_Success_Password() {
        assertEquals(PASSWORD, account.getPassword());
    }

    @Test
    void testCreateUserAccount_Success_Role() {
        assertEquals(RoleType.MEMBER, account.getRole());
    }

    @Test
    void testCreateUserAccount_Success_RegistrationDate() {
        assertEquals(REG_DATE, account.getRegistrationDate());
    }


    @Test
    void testCreateUserAccount_Success_NullRole_Allowed() {
        UserAccount a = UserAccountFactory.CreateUserAccount(ACCOUNT_ID, EMAIL, PASSWORD, null, REG_DATE);
        assertNotNull(a);
        assertNull(a.getRole());
    }

    @Test
    void testCreateUserAccount_Success_NullRegistrationDate_Allowed() {
        UserAccount a = UserAccountFactory.CreateUserAccount(ACCOUNT_ID, EMAIL, PASSWORD, ROLE, null);
        assertNotNull(a);
        assertNull(a.getRegistrationDate());
    }


    @Test
    void testCreateUserAccount_Fail_NullAccountId() {
        assertThrows(IllegalArgumentException.class, () -> UserAccountFactory.CreateUserAccount(null, EMAIL, PASSWORD, ROLE, REG_DATE));
    }

    @Test
    void testCreateUserAccount_Fail_NullEmail() {
        assertThrows(IllegalArgumentException.class, () -> UserAccountFactory.CreateUserAccount(ACCOUNT_ID, null, PASSWORD, ROLE, REG_DATE));
    }

    @Test
    void testCreateUserAccount_Fail_NullPassword() {
        assertThrows(IllegalArgumentException.class, () -> UserAccountFactory.CreateUserAccount(ACCOUNT_ID, EMAIL, null, ROLE, REG_DATE));
    }


    @Test
    void testCreateUserAccount_Fail_EmptyAccountId() {
        assertThrows(IllegalArgumentException.class, () -> UserAccountFactory.CreateUserAccount("", EMAIL, PASSWORD, ROLE, REG_DATE));
    }

    @Test
    void testCreateUserAccount_Fail_EmptyEmail() {
        assertThrows(IllegalArgumentException.class, () -> UserAccountFactory.CreateUserAccount(ACCOUNT_ID, "", PASSWORD, ROLE, REG_DATE));
    }

    @Test
    void testCreateUserAccount_Fail_EmptyPassword() {
        assertThrows(IllegalArgumentException.class, () -> UserAccountFactory.CreateUserAccount(ACCOUNT_ID, EMAIL, "", ROLE, REG_DATE));
    }


    @Test
    void testSetAccountId() {
        account.setAccountId("ACC999");
        assertEquals("ACC999", account.getAccountId());
    }

    @Test
    void testSetEmail() {
        account.setEmail("sitoyi.zusiphe@gmail.com");
        assertEquals("sitoyi.zusiphe@gmail.com", account.getEmail());
    }

    @Test
    void testSetPassword() {
        account.setPassword("NewPass456");
        assertEquals("NewPass456", account.getPassword());
    }

    @Test
    void testSetRole() {
        account.setRole(RoleType.TRAINER);
        assertEquals(RoleType.TRAINER, account.getRole());
    }

    @Test
    void testSetRegistrationDate() {
        LocalDate newDate = LocalDate.of(2025, 6, 1);
        account.setRegistrationDate(newDate);
        assertEquals(newDate, account.getRegistrationDate());
    }


    @Test
    void testCreateUserAccount_Success_MemberRole() {
        UserAccount a = UserAccountFactory.CreateUserAccount(ACCOUNT_ID, EMAIL, PASSWORD, RoleType.MEMBER, REG_DATE);
        assertEquals(RoleType.MEMBER, a.getRole());
    }

    @Test
    void testCreateUserAccount_Success_TrainerRole() {
        UserAccount a = UserAccountFactory.CreateUserAccount(ACCOUNT_ID, EMAIL, PASSWORD, RoleType.TRAINER, REG_DATE);
        assertEquals(RoleType.TRAINER, a.getRole());
    }


    @Test
    void testBuilderToString_NotNullOrEmpty() {
        String result = new UserAccount.Builder().accountId(ACCOUNT_ID).email(EMAIL).password(PASSWORD).role(ROLE).registrationDate(REG_DATE).toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.contains(ACCOUNT_ID));
        assertTrue(result.contains(EMAIL));
    }


    @Test
    void testTwoAccounts_AreIndependent() {
        UserAccount account1 = UserAccountFactory.CreateUserAccount("ACC001", "Athi@gmail.com", "Pass123", RoleType.MEMBER, LocalDate.of(2024, 1, 10));
        UserAccount account2 = UserAccountFactory.CreateUserAccount("ACC002", "lisakhanyar@gmail.com", "Pass456", RoleType.TRAINER, LocalDate.of(2024, 6, 20));

        assertNotEquals(account1.getAccountId(), account2.getAccountId());
        assertNotEquals(account1.getEmail(), account2.getEmail());
        assertNotEquals(account1.getPassword(), account2.getPassword());
        assertNotEquals(account1.getRole(), account2.getRole());
        assertNotEquals(account1.getRegistrationDate(), account2.getRegistrationDate());
    }
}