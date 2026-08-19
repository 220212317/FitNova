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

    private NextOfKinContact buildNextOfKinContact() {
        return new NextOfKinContact.Builder()
                .setNextOfKinContactId("NOK001")
                .setFirstName("Zanele")
                .setLastName("Nkosi")
                .setRelationship("Sister")
                .setCellphoneNumber("0837654321")
                .build();
    }

    @Test
    @Order(1)
    void createUserValid() {
        User user = UserFactory.createUser("Thando", "Nkosi", LocalDate.of(1999, 5, 20),
                buildAccount(), buildDemographic(), buildAddress(), buildContact(), buildNextOfKinContact());

        assertNotNull(user);
        assertNotNull(user.getUserId());
        assertEquals("Thando", user.getFirstName());
        assertEquals("Nkosi", user.getLastName());
        assertEquals(LocalDate.of(1999, 5, 20), user.getDateOfBirth());
        assertNotNull(user.getAccount());
        assertNotNull(user.getDemographic());
        assertNotNull(user.getAddress());
        assertNotNull(user.getContact());
        assertNotNull(user.getNextOfKinContact());
        System.out.println("Valid User: " + user);
    }

    @Test
    @Order(2)
    void createUserWithSuppliedId() {
        User user = UserFactory.createUser("U001", "Sipho", "Dlamini", LocalDate.of(1995, 3, 10),
                buildAccount(), buildDemographic(), buildAddress(), buildContact(), buildNextOfKinContact());

        assertNotNull(user);
        assertEquals("U001", user.getUserId());
        System.out.println("User with Supplied ID: " + user);
    }

    @Test
    @Order(3)
    void createUserNullFirstNameReturnsNull() {
        User user = UserFactory.createUser(null, "Nkosi", LocalDate.of(1999, 5, 20),
                buildAccount(), buildDemographic(), buildAddress(), buildContact(), buildNextOfKinContact());

        assertNull(user);
    }

    @Test
    @Order(4)
    void createUserEmptyFirstNameReturnsNull() {
        User user = UserFactory.createUser("   ", "Nkosi", LocalDate.of(1999, 5, 20),
                buildAccount(), buildDemographic(), buildAddress(), buildContact(), buildNextOfKinContact());

        assertNull(user);
    }

    @Test
    @Order(5)
    void createUserNullLastNameReturnsNull() {
        User user = UserFactory.createUser("Thando", null, LocalDate.of(1999, 5, 20),
                buildAccount(), buildDemographic(), buildAddress(), buildContact(), buildNextOfKinContact());

        assertNull(user);
    }

    @Test
    @Order(6)
    void createUserNullDateOfBirthReturnsNull() {
        User user = UserFactory.createUser("Thando", "Nkosi", null,
                buildAccount(), buildDemographic(), buildAddress(), buildContact(), buildNextOfKinContact());

        assertNull(user);
    }

    @Test
    @Order(7)
    void createUserFutureDateOfBirthReturnsNull() {
        User user = UserFactory.createUser("Thando", "Nkosi", LocalDate.now().plusDays(1),
                buildAccount(), buildDemographic(), buildAddress(), buildContact(), buildNextOfKinContact());

        assertNull(user);
    }

    @Test
    @Order(8)
    void createUserNullAccountReturnsNull() {
        User user = UserFactory.createUser("Thando", "Nkosi", LocalDate.of(1999, 5, 20),
                null, buildDemographic(), buildAddress(), buildContact(), buildNextOfKinContact());

        assertNull(user);
    }

    @Test
    @Order(9)
    void createUserNullDemographicReturnsNull() {
        User user = UserFactory.createUser("Thando", "Nkosi", LocalDate.of(1999, 5, 20),
                buildAccount(), null, buildAddress(), buildContact(), buildNextOfKinContact());

        assertNull(user);
    }

    @Test
    @Order(10)
    void createUserNullAddressReturnsNull() {
        User user = UserFactory.createUser("Thando", "Nkosi", LocalDate.of(1999, 5, 20),
                buildAccount(), buildDemographic(), null, buildContact(), buildNextOfKinContact());

        assertNull(user);
    }

    @Test
    @Order(11)
    void createUserNullContactReturnsNull() {
        User user = UserFactory.createUser("Thando", "Nkosi", LocalDate.of(1999, 5, 20),
                buildAccount(), buildDemographic(), buildAddress(), null, buildNextOfKinContact());

        assertNull(user);
    }

    @Test
    @Order(12)
    void createUserNullNextOfKinContactReturnsNull() {
        User user = UserFactory.createUser("Thando", "Nkosi", LocalDate.of(1999, 5, 20),
                buildAccount(), buildDemographic(), buildAddress(), buildContact(), null);

        assertNull(user);
    }
}