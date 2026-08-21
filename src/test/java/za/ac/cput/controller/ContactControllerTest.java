package za.ac.cput.controller;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import za.ac.cput.domain.Contact;
import za.ac.cput.factory.ContactFactory;

import static org.junit.jupiter.api.Assertions.*;
//Lisakhanya Tshokolo 220239215


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ContactControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/contact";
    }

    @Test
    @Order(1)
    void createContact() {
        Contact contact = ContactFactory.createContact(
                "0821234567",
                "0712345678",
                "john@gmail.com"
        );

        ResponseEntity<Contact> response = restTemplate.postForEntity(
                getBaseUrl() + "/create",
                contact,
                Contact.class
        );

        assertNotNull(response);
        assertNotNull(response.getBody());

        System.out.println("Create Response: " + response.getBody());
    }

    @Test
    @Order(2)
    void readContact() {
        String contactId = "PUT-A-REAL-ID-HERE";

        ResponseEntity<Contact> response = restTemplate.getForEntity(
                getBaseUrl() + "/read/" + contactId,
                Contact.class
        );

        assertNotNull(response);
        System.out.println("Read Response: " + response.getBody());
    }

    @Test
    @Order(3)
    void updateContact() {
        Contact contact = ContactFactory.createContact(
                "0821234567",
                "0798765432",
                "updated@gmail.com"
        );

        restTemplate.put(
                getBaseUrl() + "/update",
                contact
        );

        System.out.println("Update completed");
    }

    @Test
    @Order(4)
    void findByCellphoneNumber() {
        String cellphoneNumber = "0821234567";

        ResponseEntity<Contact> response = restTemplate.getForEntity(
                getBaseUrl() + "/findByCellphoneNumber?cellphoneNumber="
                        + cellphoneNumber,
                Contact.class
        );

        assertNotNull(response);
        System.out.println("Find By Cellphone Response: " + response.getBody());
    }

    @Test
    @Order(5)
    void findByEmailAddress() {
        String emailAddress = "john@gmail.com";

        ResponseEntity<Contact> response = restTemplate.getForEntity(
                getBaseUrl() + "/findByEmailAddress?emailAddress="
                        + emailAddress,
                Contact.class
        );

        assertNotNull(response);
        System.out.println("Find By Email Response: " + response.getBody());
    }

    @Test
    @Order(6)
    void delete() {
        String contactId = "PUT-A-REAL-ID-HERE";

        restTemplate.delete(
                getBaseUrl() + "/delete/" + contactId
        );

        System.out.println("Delete completed");

    }
}