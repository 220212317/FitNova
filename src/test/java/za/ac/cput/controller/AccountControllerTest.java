package za.ac.cput.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.domain.Account;
import za.ac.cput.factory.AccountFactory;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author: Athi Sintiya
 * 220212317
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AccountControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl = "/account";

    private static Account account = AccountFactory.createAccount(
            "ACC-CTRL-001",
            "controller.test@fitnova.com",
            "SecurePass123",
            LocalDate.of(2026, 8, 20)
    );

    @Test
    @Order(1)
    void create() {
        String url = baseUrl + "/create";
        ResponseEntity<Account> response = restTemplate.postForEntity(url, account, Account.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Account createdAccount = response.getBody();
        System.out.println("Created: " + createdAccount);
    }

    @Test
    @Order(2)
    void read() {
        String url = baseUrl + "/read/" + account.getAccountId();
        ResponseEntity<Account> response = restTemplate.getForEntity(url, Account.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Account readAccount = response.getBody();
        System.out.println("Read: " + readAccount);
    }

    @Test
    @Order(3)
    void readNotFound() {
        String url = baseUrl + "/read/NON-EXISTENT-ID";
        ResponseEntity<Account> response = restTemplate.getForEntity(url, Account.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(4)
    void update() {
        String url = baseUrl + "/update";
        Account updatedAccount = new Account.Builder().copy(account)
                .setPassword("NewSecurePass456")
                .build();

        HttpEntity<Account> entity = new HttpEntity<>(updatedAccount);
        ResponseEntity<Account> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Account.class);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("NewSecurePass456", response.getBody().getPassword());
        System.out.println("Updated: " + response.getBody());
    }

    @Test
    @Disabled
    void delete() {
        String url = baseUrl + "/delete/" + account.getAccountId();
        restTemplate.delete(url);
        ResponseEntity<Account> response = restTemplate.getForEntity(baseUrl + "/read/" + account.getAccountId(), Account.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(5)
    void findByEmail() {
        String url = baseUrl + "/findByEmail/" + account.getEmail();
        ResponseEntity<Account> response = restTemplate.getForEntity(url, Account.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println("Found by Email: " + response.getBody());
    }

    @Test
    @Order(6)
    void findAccountByRegistrationDate() {
        String url = baseUrl + "/findByRegistrationDate/2026-08-20";
        ResponseEntity<Account[]> response = restTemplate.getForEntity(url, Account[].class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length > 0);
        System.out.println("Found by Registration Date: " + List.of(response.getBody()));
    }

    @Test
    @Order(7)
    void getAll() {
        String url = baseUrl + "/getAll";
        ResponseEntity<Account[]> response = restTemplate.getForEntity(url, Account[].class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length > 0);
        System.out.println("All Accounts: " + List.of(response.getBody()));
    }
}