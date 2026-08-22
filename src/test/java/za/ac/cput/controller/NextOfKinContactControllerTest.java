package za.ac.cput.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import za.ac.cput.domain.NextOfKinContact;
import za.ac.cput.domain.User;
import za.ac.cput.factory.NextOfKinContactFactory;
import za.ac.cput.repository.IUserRepository;

import static org.junit.jupiter.api.Assertions.*;
//Lisakhanya Tshokolo 220239215


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NextOfKinContactControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private IUserRepository userRepository;

    private static User user;

    private String getBaseUrl() {

        return "http://localhost:" + port + "/nextofkincontact";
    }
    private static String createdId;

    @Test
    @Order(1)
    void create() {

        user = userRepository.save(new User.Builder()
                .setFirstName("Lisakhanya")
                .setLastName("Tshokolo")
                .build()
        );

        NextOfKinContact contact = NextOfKinContactFactory.createNextOfKinContact(
                "Vivian",
                "Tshokolo",
                "Mother",
                "0821234567",
                user
        );

        ResponseEntity<NextOfKinContact> response = restTemplate.postForEntity(
                getBaseUrl() + "/create", contact, NextOfKinContact.class);

        assertNotNull(response);
        assertNotNull(response.getBody());
        createdId = response.getBody().getNextOfKinContactId();

        System.out.println("Create Response: " + response.getBody());
    }

    @Test
    @Order(2)
    void read() {
        //String nextOfKinContactId = "PUT-A-REAL-ID-HERE";

        ResponseEntity<NextOfKinContact> response = restTemplate.getForEntity(getBaseUrl() + "/read/" + createdId, NextOfKinContact.class);

        assertNotNull(response);
        System.out.println("Read Response: " + response.getBody());
    }

    @Test
    @Order(3)
    void update() {
        NextOfKinContact contact = NextOfKinContactFactory.createNextOfKinContact(
                "Vvian",
                "Tshokolo",
                "Mother",
                "0798765432",
                user
        );

        restTemplate.put(getBaseUrl() + "/update", contact);

        System.out.println("Update completed");
    }

    @Test
    @Order(4)
    void findByFirstName() {
        String firstName = "Vivian";

        ResponseEntity<NextOfKinContact> response = restTemplate.getForEntity(getBaseUrl() + "/findByFirstName?firstName=" + firstName, NextOfKinContact.class);

        assertNotNull(response);
        System.out.println("Find By First Name Response: " + response.getBody());
    }

    @Test
    @Order(5)
    void findByLastName() {
        String lastName = "Tshokolo";

        ResponseEntity<NextOfKinContact> response = restTemplate.getForEntity(getBaseUrl() + "/findByLastName?lastName=" + lastName, NextOfKinContact.class);

        assertNotNull(response);
        System.out.println("Find By Last Name Response: " + response.getBody());
    }

    @Test
    @Order(6)
    void findByRelationship() {
        String relationship = "Mother";

        ResponseEntity<NextOfKinContact> response = restTemplate.getForEntity(getBaseUrl() + "/findByRelationship?relationship=" + relationship, NextOfKinContact.class);

        assertNotNull(response);
        System.out.println("Find By Relationship Response: " + response.getBody());
    }

    @Test
    @Order(7)
    void delete() {
        //String nextOfKinContactId = "PUT-A-REAL-ID-HERE";

        restTemplate.delete(getBaseUrl() + "/delete/" + createdId);

        System.out.println("Delete completed");
    }

}