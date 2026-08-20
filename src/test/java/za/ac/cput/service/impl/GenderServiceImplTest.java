/*
 * GenderServiceImplTest.java
 * Author: Inga Plati
 * 230126634
 */
package za.ac.cput.service.impl;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.Gender;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GenderServiceImplTest {

    @Autowired
    private GenderServiceImpl service;

    private static Gender gender;

    @Test
    @Order(1)
    void createGender() {
        gender = new Gender.Builder().setGenderId("GEN-TEST-001").setDescription("Male").build();
        Gender created = service.create(gender);
        assertNotNull(created);
        assertEquals("Male", created.getDescription());
        System.out.println("Created: " + created);
    }

    @Test
    @Order(2)
    void readGender() {
        Gender read = service.read(gender.getGenderId());
        assertNotNull(read);
        assertEquals(gender.getGenderId(), read.getGenderId());
        System.out.println("Read: " + read);
    }

    @Test
    @Order(3)
    void updateGender() {
        Gender updated = new Gender.Builder().copy(gender).setDescription("Updated Male").build();
        Gender result = service.update(updated);
        assertNotNull(result);
        assertEquals("Updated Male", result.getDescription());
        System.out.println("Updated: " + result);
    }

    @Test
    @Order(4)
    void getAllGenders() {
        List<Gender> genders = service.getAll();
        assertNotNull(genders);
        assertFalse(genders.isEmpty());
        System.out.println("All Genders: " + genders);
    }

    @Test
    @Order(5)
    void deleteGender() {
        service.delete(gender.getGenderId());
        Gender deleted = service.read(gender.getGenderId());
        assertNull(deleted);
    }
}