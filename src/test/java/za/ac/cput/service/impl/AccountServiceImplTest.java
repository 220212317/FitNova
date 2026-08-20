package za.ac.cput.service.impl;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.Account;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: Athi Sintiya
 * 220212317
 */

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountServiceImplTest {

    @Autowired
    private AccountServiceImpl accountService;

    private static final String ACCOUNT_ID = "ACC-001";
    private static final String EMAIL = "athi.sintiya@test.com";
    private static final LocalDate REG_DATE = LocalDate.of(2026, 8, 20);

    private static final Account account = new Account.Builder()
            .setAccountId(ACCOUNT_ID)
            .setEmail(EMAIL)
            .setPassword("SecurePass123")
            .setRegistrationDate(REG_DATE)
            .build();

    @Test
    @Order(1)
    void create() {
        Account created = accountService.create(account);
        assertNotNull(created);
        assertEquals(account.getAccountId(), created.getAccountId());
        System.out.println("Created: " + created);
    }

    @Test
    @Order(2)
    void read() {
        Account read = accountService.read(account.getAccountId());
        assertNotNull(read);
        assertEquals(account.getEmail(), read.getEmail());
        System.out.println("Read: " + read);
    }

    @Test
    @Order(3)
    void update() {
        Account updatedAccount = new Account.Builder()
                .copy(account)
                .setPassword("NewSecurePass456")
                .build();
        Account updated = accountService.update(updatedAccount);
        assertNotNull(updated);
        assertEquals("NewSecurePass456", updated.getPassword());
        System.out.println("Updated: " + updated);
    }

    @Test
    @Order(4)
    void findByEmail() {
        Account found = accountService.findByEmail(EMAIL);
        assertNotNull(found);
        assertEquals(EMAIL, found.getEmail());
        System.out.println("Found by Email: " + found);
    }

    @Test
    @Order(5)
    void findAccountByRegistrationDate() {
        List<Account> accounts = accountService.findAccountByRegistrationDate(REG_DATE);
        assertNotNull(accounts);
        assertFalse(accounts.isEmpty());
        System.out.println("Found by Registration Date: " + accounts);
    }

    @Test
    @Order(6)
    void getAll() {
        List<Account> all = accountService.getAll();
        assertNotNull(all);
        assertFalse(all.isEmpty());
        System.out.println("All Accounts: " + all);
    }

    @Test
    @Order(7)
    void delete() {
        accountService.delete(account.getAccountId());
        Account read = accountService.read(account.getAccountId());
        assertNull(read);
        System.out.println("Deleted account with ID: " + account.getAccountId());
    }
}