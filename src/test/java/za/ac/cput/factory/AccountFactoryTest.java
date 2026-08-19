package za.ac.cput.factory;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import za.ac.cput.domain.Account;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/*
 * AccountFactoryTest.java
 * Author: Athi Sintiya
 * 220212317
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
class AccountFactoryTest {

    @Test
    @Order(1)
    void createAccountValid() {
        Account account = AccountFactory.createAccount("athi.sintiya@fitnova.co.za", "P@ssword123");
        assertNotNull(account);
        assertNotNull(account.getAccountId());
        assertEquals("athi.sintiya@fitnova.co.za", account.getEmail());
        assertEquals("P@ssword123", account.getPassword());
        assertEquals(LocalDate.now(), account.getRegistrationDate());
        System.out.println("Valid Account: " + account);
    }

    @Test
    @Order(2)
    void createAccountWithRegistrationDate() {
        LocalDate customDate = LocalDate.of(2026, 8, 19);
        Account account = AccountFactory.createAccount("member@fitnova.co.za", "Secure123!", customDate);
        assertNotNull(account);
        assertNotNull(account.getAccountId());
        assertEquals("member@fitnova.co.za", account.getEmail());
        assertEquals(customDate, account.getRegistrationDate());
        System.out.println("Account with Custom Date: " + account);
    }

    @Test
    @Order(3)
    void createAccountWithSuppliedId() {
        LocalDate customDate = LocalDate.of(2026, 8, 19);
        Account account = AccountFactory.createAccount("ACC001", "coach@fitnova.co.za", "Coach@2025", customDate);
        assertNotNull(account);
        assertEquals("ACC001", account.getAccountId());
        assertEquals("coach@fitnova.co.za", account.getEmail());
        assertEquals(customDate, account.getRegistrationDate());
        System.out.println("Account with Supplied ID: " + account);
    }

    @Test
    @Order(4)
    void createAccountNullEmailReturnsNull() {
        Account account = AccountFactory.createAccount(null, "Password123");
        assertNull(account);
    }

    @Test
    @Order(5)
    void createAccountEmptyEmailReturnsNull() {
        Account account = AccountFactory.createAccount("   ", "Password123");
        assertNull(account);
    }

    @Test
    @Order(6)
    void createAccountInvalidEmailFormatReturnsNull() {
        Account account = AccountFactory.createAccount("not-a-valid-email", "Password123");
        assertNull(account);
    }

    @Test
    @Order(7)
    void createAccountEmptyPasswordReturnsNull() {
        Account account = AccountFactory.createAccount("test@fitnova.co.za", "");
        assertNull(account);
    }

    @Test
    @Order(8)
    void createAccountNullPasswordReturnsNull() {
        Account account = AccountFactory.createAccount("test@fitnova.co.za", null);
        assertNull(account);
    }
}