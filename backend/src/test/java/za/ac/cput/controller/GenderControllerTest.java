/*
 * GenderControllerTest.java
 * Author: Inga Plati
 * 230126634
 */
package za.ac.cput.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import za.ac.cput.domain.Gender;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GenderControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static Gender gender;

    @Test
    @Order(1)
    void createGender() {
        Gender toCreate = new Gender.Builder().setGenderId("GEN-CTRL-001").setDescription("Male").build();
        gender = restTemplate.postForObject("/gender/create", toCreate, Gender.class);
        assertNotNull(gender);
        assertEquals("Male", gender.getDescription());
        System.out.println("Created: " + gender);
    }

    @Test
    @Order(2)
    void readGender() {
        Gender read = restTemplate.getForObject("/gender/read/" + gender.getGenderId(), Gender.class);
        assertNotNull(read);
        assertEquals(gender.getGenderId(), read.getGenderId());
        System.out.println("Read: " + read);
    }

    @Test
    @Order(3)
    void updateGender() {
        Gender updated = new Gender.Builder().copy(gender).setDescription("Updated Male").build();
        restTemplate.put("/gender/update", updated);
        Gender result = restTemplate.getForObject("/gender/read/" + gender.getGenderId(), Gender.class);
        assertNotNull(result);
        assertEquals("Updated Male", result.getDescription());
        System.out.println("Updated: " + result);
    }

    @Test
    @Order(4)
    void getAllGenders() {
        Gender[] genders = restTemplate.getForObject("/gender/getAll", Gender[].class);
        assertNotNull(genders);
        assertTrue(genders.length > 0);
        System.out.println("All Genders count: " + genders.length);
    }

    @Test
    @Order(5)
    void deleteGender() {
        restTemplate.delete("/gender/delete/" + gender.getGenderId());
        Gender deleted = restTemplate.getForObject("/gender/read/" + gender.getGenderId(), Gender.class);
        assertNull(deleted);
    }
}