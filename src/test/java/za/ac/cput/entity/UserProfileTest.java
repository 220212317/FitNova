
package za.ac.cput.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
/**
 * UserProfileTest.java
 * Unit test for UserProfile
 * 26 March 2026
 * Author: Avuyile Sitoyi
 * (240971051)
 */

public class UserProfileTest {


    private UserProfile userProfile;

    @BeforeEach
    void setUp() {
        userProfile = new UserProfile.Builder()
                .profileId("P001")
                .firstName("Sam")
                .lastName("Ndlela")
                .gender("Male")
                .dateOfBirth(LocalDate.of(1999, 5, 20))
                .build();
    }

    @Test
    void testUserProfile_NotNull() {
        assertNotNull(userProfile);
    }

    @Test
    void testProfileId() {
        assertEquals("P001", userProfile.getProfileId());
    }

    @Test
    void testFirstName() {
        assertEquals("Sam", userProfile.getFirstName());
    }

    @Test
    void testLastName() {
        assertEquals("Ndlela", userProfile.getLastName());
    }

    @Test
    void testGender() {
        assertEquals("Male", userProfile.getGender());
    }

    @Test
    void testDateOfBirth() {
        assertEquals(LocalDate.of(1999, 5, 20), userProfile.getDateOfBirth());
    }

    @Test
    void testSetFirstName() {
        userProfile.setFirstName("Updated");
        assertEquals("Updated", userProfile.getFirstName());
    }

    @Test
    void testSetLastName() {
        userProfile.setLastName("Smith");
        assertEquals("Smith", userProfile.getLastName());
    }

    @Test
    void testSetGender() {
        userProfile.setGender("Female");
        assertEquals("Female", userProfile.getGender());
    }

    @Test
    void testSetDateOfBirth() {
        LocalDate newDate = LocalDate.of(2000, 1, 1);
        userProfile.setDateOfBirth(newDate);
        assertEquals(newDate, userProfile.getDateOfBirth());
    }


}
