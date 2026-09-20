package za.ac.cput.factory;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.NextOfKinContact;
import za.ac.cput.domain.User;

import static org.junit.jupiter.api.Assertions.*;
//Lisakhanya Tshokolo 220239215

class NextOfKinContactFactoryTest {
    private static final User user = new User.Builder()
            .setUserId("C20")
            .setFirstName("Lisakhanya")
            .setLastName("Tshokolo")
            .build();

    @Test
    void createNextOfKinContact() {
        NextOfKinContact contact = NextOfKinContactFactory.createNextOfKinContact("Vivian",
                "Tshokolo",
                "Mother",
                "0821234567",
                user);

        assertNotNull(contact);
        assertNotNull(contact.getNextOfKinContactId());
        assertEquals("Vivian", contact.getFirstName());
        assertEquals("Tshokolo", contact.getLastName());
        assertEquals("Mother", contact.getRelationship());
        assertEquals("0821234567", contact.getCellphoneNumber());
        assertEquals(user, contact.getUser());
    }

    @Test
    void createNextOfKinContactWithEmptyFirstName() {
        NextOfKinContact contact = NextOfKinContactFactory.createNextOfKinContact(
                "",
                "Tshokolo",
                "Mother",
                "0821234567",
                user);

        assertNull(contact);
    }

    @Test
    void createNextOfKinContactWithNullFirstName() {
        NextOfKinContact contact = NextOfKinContactFactory.createNextOfKinContact(
                null,
                "Tshokolo",
                "Mother",
                "0821234567",
                user);
        assertNull(contact);
    }

    @Test
    void createNextOfKinContactWithEmptyLastName() {
        NextOfKinContact contact = NextOfKinContactFactory.createNextOfKinContact(
                "Vivian",
                "",
                "Mother",
                "0821234567",
                user);
        assertNull(contact);
    }

    @Test
    void createNextOfKinContactWithNullLastName() {
        NextOfKinContact contact = NextOfKinContactFactory.createNextOfKinContact(
                "Vivian",
                null,
                "Mother",
                "0821234567",
                user);
        assertNull(contact);
    }

    @Test
    void createNextOfKinContactWithEmptyRelationship() {
        NextOfKinContact contact = NextOfKinContactFactory.createNextOfKinContact(
                "Vivian",
                "Tshokolo",
                "",
                "0821234567",
                user);
        assertNull(contact);
    }

    @Test
    void createNextOfKinContactWithNullRelationship() {
        NextOfKinContact contact = NextOfKinContactFactory.createNextOfKinContact(
                "Vivian",
                "Tshokolo",
                null,
                "0821234567",
                user);
        assertNull(contact);
    }

    @Test
    void createNextOfKinContactWithEmptyCellphoneNumber() {
        NextOfKinContact contact = NextOfKinContactFactory.createNextOfKinContact(
                "John",
                "Tshokolo",
                "Mother",
                "",
                user);
        assertNull(contact);
    }

    @Test
    void createNextOfKinContactWithNullCellphoneNumber() {
        NextOfKinContact contact = NextOfKinContactFactory.createNextOfKinContact(
                "Vivian",
                "Tshokolo",
                "Mother",
                null,
                user);
        assertNull(contact);
    }

    @Test
    void createNextOfKinContactWithNullUser() {
        NextOfKinContact contact = NextOfKinContactFactory.createNextOfKinContact(
                "Vivian", "Tshokolo", "Mother", "0821234567", null);
        assertNull(contact);
    }

}
