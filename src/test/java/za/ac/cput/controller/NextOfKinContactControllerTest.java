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
import za.ac.cput.util.Helper;

import static org.junit.jupiter.api.Assertions.*;

// Lisakhanya Tshokolo 220239215

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
    private static String createdId;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/nextofkincontact";
    }

    @Test
    @Order(1)
    void create() {

        // Create User with an ID using Helper.generateId()
        user = userRepository.save(new User.Builder()
                .setUserId(Helper.generateId())
                .setFirstName("Lisakhanya")
                .setLastName("Tshokolo")
                .build()
        );

        assertNotNull(user);
        assertNotNull(user.getUserId());

        // Factory generates NextOfKinContact ID
        NextOfKinContact contact =
                NextOfKinContactFactory.createNextOfKinContact(
                        "Vivian",
                        "Tshokolo",
                        "Mother",
                        "0821234567",
                        user
                );

        assertNotNull(contact);
        assertNotNull(contact.getNextOfKinContactId());

        ResponseEntity<NextOfKinContact> response =
                restTemplate.postForEntity(
                        getBaseUrl() + "/create",
                        contact,
                        NextOfKinContact.class
                );

        assertNotNull(response);
        assertNotNull(response.getBody());

        createdId =
                response.getBody().getNextOfKinContactId();

        assertNotNull(createdId);

        System.out.println(
                "Create Response: " + response.getBody()
        );
    }

    @Test
    @Order(2)
    void read() {

        assertNotNull(createdId);

        ResponseEntity<NextOfKinContact> response =
                restTemplate.getForEntity(
                        getBaseUrl() + "/read/" + createdId,
                        NextOfKinContact.class
                );

        assertNotNull(response);
        assertNotNull(response.getBody());

        assertEquals(
                createdId,
                response.getBody().getNextOfKinContactId()
        );

        System.out.println(
                "Read Response: " + response.getBody()
        );
    }

    @Test
    @Order(3)
    void update() {

        assertNotNull(createdId);

        // Use the SAME ID as the contact created in test 1
        NextOfKinContact contact =
                new NextOfKinContact.Builder()
                        .setNextOfKinContactId(createdId)
                        .setFirstName("Vivian")
                        .setLastName("Tshokolo")
                        .setRelationship("Mother")
                        .setCellphoneNumber("0798765432")
                        .setUser(user)
                        .build();

        restTemplate.put(
                getBaseUrl() + "/update",
                contact
        );

        // Check that the update actually happened
        ResponseEntity<NextOfKinContact> response =
                restTemplate.getForEntity(
                        getBaseUrl() + "/read/" + createdId,
                        NextOfKinContact.class
                );

        assertNotNull(response);
        assertNotNull(response.getBody());

        assertEquals(
                createdId,
                response.getBody().getNextOfKinContactId()
        );

        assertEquals(
                "0798765432",
                response.getBody().getCellphoneNumber()
        );

        System.out.println(
                "Update Response: " + response.getBody()
        );
    }

    @Test
    @Order(4)
    void findByFirstName() {

        String firstName = "Vivian";

        ResponseEntity<NextOfKinContact> response =
                restTemplate.getForEntity(
                        getBaseUrl()
                                + "/findByFirstName?firstName="
                                + firstName,
                        NextOfKinContact.class
                );

        assertNotNull(response);
        assertNotNull(response.getBody());

        assertEquals(
                "Vivian",
                response.getBody().getFirstName()
        );

        System.out.println(
                "Find By First Name Response: "
                        + response.getBody()
        );
    }

    @Test
    @Order(5)
    void findByLastName() {

        String lastName = "Tshokolo";

        ResponseEntity<NextOfKinContact> response =
                restTemplate.getForEntity(
                        getBaseUrl()
                                + "/findByLastName?lastName="
                                + lastName,
                        NextOfKinContact.class
                );

        assertNotNull(response);
        assertNotNull(response.getBody());

        assertEquals(
                "Tshokolo",
                response.getBody().getLastName()
        );

        System.out.println(
                "Find By Last Name Response: "
                        + response.getBody()
        );
    }

    @Test
    @Order(6)
    void findByRelationship() {

        String relationship = "Mother";

        ResponseEntity<NextOfKinContact> response =
                restTemplate.getForEntity(
                        getBaseUrl()
                                + "/findByRelationship?relationship="
                                + relationship,
                        NextOfKinContact.class
                );

        assertNotNull(response);
        assertNotNull(response.getBody());

        assertEquals(
                "Mother",
                response.getBody().getRelationship()
        );

        System.out.println(
                "Find By Relationship Response: "
                        + response.getBody()
        );
    }

    @Test
    @Order(7)
    void delete() {

        assertNotNull(createdId);

        restTemplate.delete(
                getBaseUrl() + "/delete/" + createdId
        );

        // Confirm that the contact was deleted
        ResponseEntity<NextOfKinContact> response =
                restTemplate.getForEntity(
                        getBaseUrl() + "/read/" + createdId,
                        NextOfKinContact.class
                );

        System.out.println("Delete completed");

        assertTrue(
                response.getBody() == null
                        || response.getStatusCode().is4xxClientError()
        );
    }
}