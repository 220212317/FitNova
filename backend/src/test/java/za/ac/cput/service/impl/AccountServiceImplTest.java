package za.ac.cput.service.impl;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.Account;
import za.ac.cput.factory.AccountFactory;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountServiceImplTest {

    @Autowired
    private AccountServiceImpl accountService;

    private static Account account = AccountFactory.createAccount(
            "ACC-001",
            "athi.sintiya@test.com",
            "SecurePass123",
            LocalDate.of(2026, 8, 20)
    );

    @Test
    @Order(1)
    void create() {
        Account account1 = accountService.create(account);
        assertNotNull(account1);
        System.out.println(account1);
    }

    @Test
    @Order(2)
    void read() {
        Account account1 = accountService.read(account.getAccountId());
        System.out.println(account1);
    }

    @Test
    @Order(3)
    void update() {
        Account account1 = new Account.Builder().copy(account)
                .setPassword("NewSecurePass456")
                .build();

        Account updatedAccount = accountService.update(account1);
        System.out.println(updatedAccount);
    }

    @Test
    void delete() {
        accountService.delete(account.getAccountId());
        Account read = accountService.read(account.getAccountId());
        assertNull(read);
    }

    @Test
    @Order(4)
    void getAll() {
        List<Account> accounts = accountService.getAll();
        System.out.println(accounts);
    }

    @Test
    @Order(5)
    void findByEmail() {
        Account account1 = accountService.findByEmail("athi.sintiya@test.com");
        System.out.println(account1);
    }

    @Test
    @Order(6)
    void findAccountByRegistrationDate() {
        List<Account> accounts = accountService.findAccountByRegistrationDate(LocalDate.of(2026, 8, 20));
        System.out.println(accounts);
    }
}