package za.ac.cput.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.ac.cput.domain.*;
import za.ac.cput.domain.enums.RoleType;
import za.ac.cput.factory.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Author:  Collins Shibambo
 * 230093183
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRoleControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String userBaseUrl = "/user";
    private String baseUrl = "/userrole";

    private static Account account = AccountFactory.createAccount(
            "ACC-ROLE-CTRL-001",
            "role.controller.test@fitnova.com",
            "SecurePass123",
            LocalDate.of(2026, 8, 20)
    );

    private static Gender gender = GenderFactory.createGender("Male");
    private static Race race = RaceFactory.createRace("Coloured");
    private static Demographic demographic = DemographicFactory.createDemographic(gender, race);

    private static Address address = AddressFactory.createAddress(
            "45", "Main Road", "Observatory", "Cape Town", "7925", "Western Cape", "South Africa"
    );

    private static Contact contact = ContactFactory.createContact(
            "0829876543", "0831234567", "role.controller.test@fitnova.com"
    );

    private static NextOfKinContact nextOfKinContact = NextOfKinContactFactory.createNextOfKinContact(
            "Sipho", "Dlamini", "Brother", "0831234567"
    );

    private static User user = UserFactory.createUser(
            "USER-ROLE-CTRL-001", "Lindiwe", "Mahlangu", LocalDate.of(1995, 3, 10),
            account, demographic, address, contact, nextOfKinContact
    );

    private static UserRole userRole = UserRoleFactory.createUserRole(
            "UR-CTRL-001", user, RoleType.MEMBER, "Standard gym member"
    );

    @Test
    @Order(1)
    void setupUser() {
        String url = userBaseUrl + "/create";
        ResponseEntity<User> response = restTemplate.postForEntity(url, user, User.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        System.out.println("Setup - created User: " + response.getBody());
    }

    @Test
    @Order(2)
    void create() {
        String url = baseUrl + "/create";
        ResponseEntity<UserRole> response = restTemplate.postForEntity(url, userRole, UserRole.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        System.out.println("Created: " + response.getBody());
    }

    @Test
    @Order(3)
    void read() {
        String url = baseUrl + "/read/" + userRole.getUserRoleId();
        ResponseEntity<UserRole> response = restTemplate.getForEntity(url, UserRole.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println("Read: " + response.getBody());
    }

    @Test
    @Order(4)
    void readNotFound() {
        String url = baseUrl + "/read/NON-EXISTENT-ID";
        ResponseEntity<UserRole> response = restTemplate.getForEntity(url, UserRole.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(5)
    void findByUser() {
        String url = baseUrl + "/findByUser/" + user.getUserId();
        ResponseEntity<UserRole[]> response = restTemplate.getForEntity(url, UserRole[].class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length > 0);
        System.out.println("Found by user: " + List.of(response.getBody()));
    }

    @Test
    @Order(6)
    void findByRole() {
        String url = baseUrl + "/findByRole/" + RoleType.MEMBER;
        ResponseEntity<UserRole[]> response = restTemplate.getForEntity(url, UserRole[].class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length > 0);
        System.out.println("Found by role: " + List.of(response.getBody()));
    }

    @Test
    @Order(7)
    void findByUserAndRole() {
        String url = baseUrl + "/findByUserAndRole/" + user.getUserId() + "/" + RoleType.MEMBER;
        ResponseEntity<UserRole> response = restTemplate.getForEntity(url, UserRole.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println("Found by user and role: " + response.getBody());
    }

    @Test
    @Order(8)
    void update() {
        String url = baseUrl + "/update";
        UserRole updatedUserRole = new UserRole.Builder().copy(userRole)
                .setDescription("Updated description")
                .build();

        HttpEntity<UserRole> entity = new HttpEntity<>(updatedUserRole);
        ResponseEntity<UserRole> response = restTemplate.exchange(url, HttpMethod.PUT, entity, UserRole.class);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated description", response.getBody().getDescription());
        System.out.println("Updated: " + response.getBody());
    }

    @Test
    @Disabled
    void delete() {
        String url = baseUrl + "/delete/" + userRole.getUserRoleId();
        restTemplate.delete(url);
        ResponseEntity<UserRole> response = restTemplate.getForEntity(baseUrl + "/read/" + userRole.getUserRoleId(), UserRole.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(9)
    void getAll() {
        String url = baseUrl + "/getAll";
        ResponseEntity<UserRole[]> response = restTemplate.getForEntity(url, UserRole[].class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length > 0);
        System.out.println("All UserRoles: " + List.of(response.getBody()));
    }
}