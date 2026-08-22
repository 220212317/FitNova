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
import za.ac.cput.factory.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Collins Shibambbo
 * 230093183
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl = "/user";

    private static Account account = AccountFactory.createAccount(
            "ACC-USER-CTRL-001",
            "user.controller.test@fitnova.com",
            "SecurePass123",
            LocalDate.of(2026, 8, 20)
    );

    private static Gender gender = GenderFactory.createGender("Female");
    private static Race race = RaceFactory.createRace("African");
    private static Demographic demographic = DemographicFactory.createDemographic(gender, race);

    private static Address address = AddressFactory.createAddress(
            "12", "Long Street", "Gardens", "Cape Town", "8001", "Western Cape", "South Africa"
    );

    private static Contact contact = ContactFactory.createContact(
            "0821234567", "0837654321", "user.controller.test@fitnova.com"
    );

    private static NextOfKinContact nextOfKinContact = NextOfKinContactFactory.createNextOfKinContact(
            "Zanele", "Nkosi", "Sister", "0837654321"
    );

    private static User user = UserFactory.createUser(
            "USER-CTRL-001", "Thando", "Nkosi", LocalDate.of(1999, 5, 20),
            account, demographic, address, contact, nextOfKinContact
    );

    @Test
    @Order(1)
    void create() {
        String url = baseUrl + "/create";
        ResponseEntity<User> response = restTemplate.postForEntity(url, user, User.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        System.out.println("Created: " + response.getBody());
    }

    @Test
    @Order(2)
    void read() {
        String url = baseUrl + "/read/" + user.getUserId();
        ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println("Read: " + response.getBody());
    }

    @Test
    @Order(3)
    void readNotFound() {
        String url = baseUrl + "/read/NON-EXISTENT-ID";
        ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(4)
    void findByFirstNameAndLastName() {
        String url = baseUrl + "/findByName/Thando/Nkosi";
        ResponseEntity<User[]> response = restTemplate.getForEntity(url, User[].class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length > 0);
        System.out.println("Found by name: " + List.of(response.getBody()));
    }

    @Test
    @Order(5)
    void searchByLastName() {
        String url = baseUrl + "/searchByLastName/nko";
        ResponseEntity<User[]> response = restTemplate.getForEntity(url, User[].class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length > 0);
        System.out.println("Search results: " + List.of(response.getBody()));
    }

    @Test
    @Order(6)
    void update() {
        String url = baseUrl + "/update";
        User updatedUser = new User.Builder().copy(user)
                .setLastName("Dlamini")
                .build();

        HttpEntity<User> entity = new HttpEntity<>(updatedUser);
        ResponseEntity<User> response = restTemplate.exchange(url, HttpMethod.PUT, entity, User.class);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Dlamini", response.getBody().getLastName());
        System.out.println("Updated: " + response.getBody());
    }

    @Test
    @Disabled
    void delete() {
        String url = baseUrl + "/delete/" + user.getUserId();
        restTemplate.delete(url);
        ResponseEntity<User> response = restTemplate.getForEntity(baseUrl + "/read/" + user.getUserId(), User.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Order(7)
    void getAll() {
        String url = baseUrl + "/getAll";
        ResponseEntity<User[]> response = restTemplate.getForEntity(url, User[].class);
        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length > 0);
        System.out.println("All Users: " + List.of(response.getBody()));
    }
}