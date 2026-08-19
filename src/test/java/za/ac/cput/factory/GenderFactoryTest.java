package za.ac.cput.factory;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import za.ac.cput.domain.Gender;

import static org.junit.jupiter.api.Assertions.*;

/*
 * GenderFactoryTest.java
 * Author: Inga Plati
 * 230126634
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
class GenderFactoryTest {

    @Test
    @Order(1)
    void createGenderValid() {
        Gender gender = GenderFactory.createGender("Male");
        assertNotNull(gender);
        assertNotNull(gender.getGenderId());
        assertEquals("Male", gender.getDescription());
        System.out.println("Valid Gender: " + gender);
    }

    @Test
    @Order(2)
    void createGenderWithSuppliedId() {
        Gender gender = GenderFactory.createGender("GEN001", "Female");
        assertNotNull(gender);
        assertEquals("GEN001", gender.getGenderId());
        assertEquals("Female", gender.getDescription());
        System.out.println("Gender with Supplied ID: " + gender);
    }

    @Test
    @Order(3)
    void createGenderTrimsDescription() {
        Gender gender = GenderFactory.createGender("  Non-binary  ");
        assertNotNull(gender);
        assertEquals("Non-binary", gender.getDescription());
    }

    @Test
    @Order(4)
    void createGenderNullDescriptionReturnsNull() {
        Gender gender = GenderFactory.createGender(null);
        assertNull(gender);
    }

    @Test
    @Order(5)
    void createGenderEmptyDescriptionReturnsNull() {
        Gender gender = GenderFactory.createGender("   ");
        assertNull(gender);
    }
}