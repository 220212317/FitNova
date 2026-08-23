package za.ac.cput.factory;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import za.ac.cput.domain.Account;
import za.ac.cput.domain.Address;
import za.ac.cput.domain.Contact;
import za.ac.cput.domain.Demographic;
import za.ac.cput.domain.NextOfKinContact;
import za.ac.cput.domain.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/*
 * UserFactoryTest.java
 * Author: Collins Shibambo
 * 230093183
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
class UserFactoryTest {

    private Account buildAccount() {
        return new Account.Builder()
                .setAccountId("ACC001")
                .setEmail("thando.nkosi@fitnova.co.za")
                .setPassword("Secure123!")
                .setRegistrationDate(LocalDate.now())
                .build();
    }

    private Demographic buildDemographic() {
        return new Demographic.Builder()
                .setDemographyId("DEM001")
                .build();
    }

    private Address buildAddress() {
        return new Address.Builder()
                .setAddressId("ADD001")
                .setCity("Cape Town")
                .setCountry("South Africa")
                .build();
    }

    private Contact buildContact() {
        return new Contact.Builder()
                .setContactId("CON001")
                .setCellphoneNumber("0821234567")
                .setEmailAddress("thando.nkosi@fitnova.co.za")
                .build();
    }

    private List<NextOfKinContact> buildNextOfKinContacts() {
        NextOfKinContact nextOfKin = new NextOfKinContact.Builder()
                .setNextOfKinContactId("NOK001")
                .setFirstName("Zanele")
                .setLastName("Nkosi")
                .setRelationship("Sister")
                .setCellphoneNumber("0837654321")
                .build();
        return List.of(nextOfKin);
    }

    @Test
    @Order(1)
    void createUserValid() {
        User user = UserFactory.createUser("Thando", "Nkosi", LocalDate.of(1999, 5, 20),
                buildAccount(), buildDemographic(), buildAddress(), buildContact(), buildNextOfKinContacts());

        assertNotNull(user);
        assertNotNull(user.getUserId());
        assertEquals("Thando", user.getFirstName());
        assertEquals("Nkosi", user.getLastName());
        assertEquals(LocalDate.of(1999, 5, 20), user.getDateOfBirth());
        assertNotNull(user.getAccount());
        assertNotNull(user.getDemographic());
        assertNotNull(user.getAddress());
        assertNotNull(user.getContact());
        assertEquals(1, user.getNextOfKinContacts().size());
        System.out.println("Valid User: " + user);
    }

    @Test
    @Order(2)
    void createUserWithSuppliedId() {
        User user = UserFactory.createUser("U001", "Sipho", "Dlamini", LocalDate.of(1995, 3, 10),
                buildAccount(), buildDemographic(), buildAddress(), buildContact(), buildNextOfKinContacts());

        assertNotNull(user);
        assertEquals("U001", user.getUserId());
        System.out.println("User with Supplied ID: " + user);
    }

    @Test
    @Order(3)
    void createUserWithMultipleNextOfKinContacts() {
        NextOfKinContact first = new NextOfKinContact.Builder()
                .setNextOfKinContactId("NOK001")
                .setFirstName("Zanele")
                .setLastName("Nkosi")
                .setRelationship("Sister")
                .setCellphoneNumber("0837654321")
                .build();

        NextOfKinContact second = new NextOfKinContact.Builder()
                .setNextOfKinContactId("NOK002")
                .setFirstName("Bongani")
                .setLastName("Nkosi")
                .setRelationship("Brother")
                .setCellphoneNumber("0831112222")
                .build();

        User user = UserFactory.createUser("Thando", "Nkosi", LocalDate.of(1999, 5, 20),
                buildAccount(), buildDemographic(), buildAddress(), buildContact(), List.of(first, second));

        assertNotNull(user);
        assertEquals(2, user.getNextOfKinContacts().size());
    }

    @Test
    @Order(4)
    void createUserWithNoNextOfKinContactsIsValid() {
        User user = UserFactory.createUser("Thando", "Nkosi", LocalDate.of(1999, 5, 20),
                buildAccount(), buildDemographic(), buildAddress(), buildContact(), List.of());

        assertNotNull(user);
        assertTrue(user.getNextOfKinContacts().isEmpty());
    }

    @Test
    @Order(5)
    void createUserWithNullNextOfKinContactsIsNormalisedToEmpty() {
        User user = UserFactory.createUser("Thando", "Nkosi", LocalDate.of(1999, 5, 20),
                buildAccount(), buildDemographic(), buildAddress(), buildContact(), null);

        assertNotNull(user);
        assertNotNull(user.getNextOfKinContacts());
        assertTrue(user.getNextOfKinContacts().isEmpty());
    }

    @Test
    @Order(6)
    void createUserNullFirstNameReturnsNull() {
        User user = UserFactory.createUser(null, "Nkosi", LocalDate.of(1999, 5, 20),
                buildAccount(), buildDemographic(), buildAddress(), buildContact(), buildNextOfKinContacts());

        assertNull(user);
    }

    @Test
    @Order(7)
    void createUserEmptyFirstNameReturnsNull() {
        User user = UserFactory.createUser("   ", "Nkosi", LocalDate.of(1999, 5, 20),
                buildAccount(), buildDemographic(), buildAddress(), buildContact(), buildNextOfKinContacts());

        assertNull(user);
    }

    @Test
    @Order(8)
    void createUserNullLastNameReturnsNull() {
        User user = UserFactory.createUser("Thando", null, LocalDate.of(1999, 5, 20),
                buildAccount(), buildDemographic(), buildAddress(), buildContact(), buildNextOfKinContacts());

        assertNull(user);
    }

    @Test
    @Order(9)
    void createUserNullDateOfBirthReturnsNull() {
        User user = UserFactory.createUser("Thando", "Nkosi", null,
                buildAccount(), buildDemographic(), buildAddress(), buildContact(), buildNextOfKinContacts());

        assertNull(user);
    }

    @Test
    @Order(10)
    void createUserFutureDateOfBirthReturnsNull() {
        User user = UserFactory.createUser("Thando", "Nkosi", LocalDate.now().plusDays(1),
                buildAccount(), buildDemographic(), buildAddress(), buildContact(), buildNextOfKinContacts());

        assertNull(user);
    }

    @Test
    @Order(11)
    void createUserNullAccountReturnsNull() {
        User user = UserFactory.createUser("Thando", "Nkosi", LocalDate.of(1999, 5, 20),
                null, buildDemographic(), buildAddress(), buildContact(), buildNextOfKinContacts());

        assertNull(user);
    }

    @Test
    @Order(12)
    void createUserNullDemographicReturnsNull() {
        User user = UserFactory.createUser("Thando", "Nkosi", LocalDate.of(1999, 5, 20),
                buildAccount(), null, buildAddress(), buildContact(), buildNextOfKinContacts());

        assertNull(user);
    }

    @Test
    @Order(13)
    void createUserNullAddressReturnsNull() {
        User user = UserFactory.createUser("Thando", "Nkosi", LocalDate.of(1999, 5, 20),
                buildAccount(), buildDemographic(), null, buildContact(), buildNextOfKinContacts());

        assertNull(user);
    }

    @Test
    @Order(14)
    void createUserNullContactReturnsNull() {
        User user = UserFactory.createUser("Thando", "Nkosi", LocalDate.of(1999, 5, 20),
                buildAccount(), buildDemographic(), buildAddress(), null, buildNextOfKinContacts());

        assertNull(user);
    }
}