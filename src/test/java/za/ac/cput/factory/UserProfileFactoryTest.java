package za.ac.cput.factory;

/**
 * UserProfileFactoryTest.java
 * Unit test for UserProfileFactory
 * 26 March 2026
 * Author: Avuyile Sitoyi
 * (240971051)
 */

import org.junit.jupiter.api.Test;
import za.ac.cput.entity.UserProfile;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileFactoryTest {

    private static final String PROFILE_ID = "P001";
    private static final String FIRST_NAME = "Avuyile";
    private static final String LAST_NAME = "Sitoyi";
    private static final String GENDER = "Male";
    private static final LocalDate DOB = LocalDate.of(2000, 5, 15);


    @Test
    void testCreateUserProfile_Success_NotNull() {
        UserProfile profile = UserProfileFactory.CreateUserProfile(
                PROFILE_ID, FIRST_NAME, LAST_NAME, GENDER, DOB);
        assertNotNull(profile);
    }

    @Test
    void testCreateUserProfile_Success_ProfileId() {
        UserProfile profile = UserProfileFactory.CreateUserProfile(
                PROFILE_ID, FIRST_NAME, LAST_NAME, GENDER, DOB);
        assertEquals(PROFILE_ID, profile.getProfileId());
    }

    @Test
    void testCreateUserProfile_Success_FirstName() {
        UserProfile profile = UserProfileFactory.CreateUserProfile(
                PROFILE_ID, FIRST_NAME, LAST_NAME, GENDER, DOB);
        assertEquals(FIRST_NAME, profile.getFirstName());
    }

    @Test
    void testCreateUserProfile_Success_LastName() {
        UserProfile profile = UserProfileFactory.CreateUserProfile(
                PROFILE_ID, FIRST_NAME, LAST_NAME, GENDER, DOB);
        assertEquals(LAST_NAME, profile.getLastName());
    }

    @Test
    void testCreateUserProfile_Success_Gender() {
        UserProfile profile = UserProfileFactory.CreateUserProfile(
                PROFILE_ID, FIRST_NAME, LAST_NAME, GENDER, DOB);
        assertEquals(GENDER, profile.getGender());
    }

    @Test
    void testCreateUserProfile_Success_DateOfBirth() {
        UserProfile profile = UserProfileFactory.CreateUserProfile(
                PROFILE_ID, FIRST_NAME, LAST_NAME, GENDER, DOB);
        assertEquals(DOB, profile.getDateOfBirth());
    }

    @Test
    void testCreateUserProfile_Success_NullDateOfBirth_Allowed() {
        // dateOfBirth has no null-check in the factory, so null should be accepted
        UserProfile profile = UserProfileFactory.CreateUserProfile(
                PROFILE_ID, FIRST_NAME, LAST_NAME, GENDER, null);
        assertNotNull(profile);
        assertNull(profile.getDateOfBirth());
    }


    @Test
    void testCreateUserProfile_Fail_NullProfileId() {
        assertThrows(IllegalArgumentException.class, () ->
                UserProfileFactory.CreateUserProfile(
                        null, FIRST_NAME, LAST_NAME, GENDER, DOB));
    }

    @Test
    void testCreateUserProfile_Fail_NullFirstName() {
        assertThrows(IllegalArgumentException.class, () ->
                UserProfileFactory.CreateUserProfile(
                        PROFILE_ID, null, LAST_NAME, GENDER, DOB));
    }

    @Test
    void testCreateUserProfile_Fail_NullLastName() {
        assertThrows(IllegalArgumentException.class, () ->
                UserProfileFactory.CreateUserProfile(
                        PROFILE_ID, FIRST_NAME, null, GENDER, DOB));
    }

    @Test
    void testCreateUserProfile_Fail_NullGender() {
        assertThrows(IllegalArgumentException.class, () ->
                UserProfileFactory.CreateUserProfile(
                        PROFILE_ID, FIRST_NAME, LAST_NAME, null, DOB));
    }


    @Test
    void testCreateUserProfile_Fail_EmptyProfileId() {
        assertThrows(IllegalArgumentException.class, () ->
                UserProfileFactory.CreateUserProfile(
                        "", FIRST_NAME, LAST_NAME, GENDER, DOB));
    }

    @Test
    void testCreateUserProfile_Fail_EmptyFirstName() {
        assertThrows(IllegalArgumentException.class, () ->
                UserProfileFactory.CreateUserProfile(
                        PROFILE_ID, "", LAST_NAME, GENDER, DOB));
    }

    @Test
    void testCreateUserProfile_Fail_EmptyLastName() {
        assertThrows(IllegalArgumentException.class, () ->
                UserProfileFactory.CreateUserProfile(
                        PROFILE_ID, FIRST_NAME, "", GENDER, DOB));
    }

    @Test
    void testCreateUserProfile_Fail_EmptyGender() {
        assertThrows(IllegalArgumentException.class, () ->
                UserProfileFactory.CreateUserProfile(
                        PROFILE_ID, FIRST_NAME, LAST_NAME, "", DOB));
    }


    @Test
    void testTwoProfiles_AreIndependent() {
        UserProfile profile1 = UserProfileFactory.CreateUserProfile(
                "P001", "Avuyile", "Sitoyi", "Male", DOB);
        UserProfile profile2 = UserProfileFactory.CreateUserProfile(
                "P002", "Lisa", "Tshokolo", "Male", LocalDate.of(1999, 3, 10));

        assertNotEquals(profile1.getProfileId(), profile2.getProfileId());
        assertNotEquals(profile1.getFirstName(), profile2.getFirstName());
        assertNotEquals(profile1.getLastName(), profile2.getLastName());
    }
}
