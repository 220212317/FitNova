package za.ac.cput.factory;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.NextOfKinContact;

import static org.junit.jupiter.api.Assertions.*;
//Lisakhanya Tshokolo 220239215

class NextOfKinContactFactoryTest {

    @Test
    void createNextOfKinContact() {
        NextOfKinContact contact = NextOfKinContactFactory.createNextOfKinContact("Vivian",
                "Tshokolo",
                "Mother",
                "0821234567");

        assertNotNull(contact);
        assertNotNull(contact.getNextOfKinContactId());
        assertEquals("Vivian", contact.getFirstName());
        assertEquals("Tshokolo", contact.getLastName());
        assertEquals("Mother", contact.getRelationship());
        assertEquals("0821234567", contact.getCellphoneNumber());
    }

    @Test
    void createNextOfKinContactWithEmptyFirstName() {
        NextOfKinContact contact = NextOfKinContactFactory.createNextOfKinContact(
                "",
                "Tshokolo",
                "Mother",
                "0821234567");

        assertNull(contact);
    }

    @Test
    void createNextOfKinContactWithNullFirstName() {
        NextOfKinContact contact = NextOfKinContactFactory.createNextOfKinContact(
                null,
                "Tshokolo",
                "Mother",
                "0821234567");
        assertNull(contact);
    }

    @Test
    void createNextOfKinContactWithEmptyLastName() {
        NextOfKinContact contact = NextOfKinContactFactory.createNextOfKinContact(
                "Vivian",
                "",
                "Mother",
                "0821234567");
        assertNull(contact);
    }

    @Test
    void createNextOfKinContactWithNullLastName() {
        NextOfKinContact contact = NextOfKinContactFactory.createNextOfKinContact(
                "Vivian",
                null,
                "Mother",
                "0821234567");
        assertNull(contact);
    }

    @Test
    void createNextOfKinContactWithEmptyRelationship() {
        NextOfKinContact contact = NextOfKinContactFactory.createNextOfKinContact(
                "Vivian",
                "Tshokolo",
                "",
                "0821234567");
        assertNull(contact);
    }

    @Test
    void createNextOfKinContactWithNullRelationship() {
        NextOfKinContact contact = NextOfKinContactFactory.createNextOfKinContact(
                "Vivian",
                "Tshokolo",
                null,
                "0821234567");
        assertNull(contact);
    }

    @Test
    void createNextOfKinContactWithEmptyCellphoneNumber() {
        NextOfKinContact contact = NextOfKinContactFactory.createNextOfKinContact(
                "John",
                "Tshokolo",
                "Mother",
                "");
        assertNull(contact);
    }

    @Test
    void createNextOfKinContactWithNullCellphoneNumber() {
        NextOfKinContact contact = NextOfKinContactFactory.createNextOfKinContact(
                "Vivian",
                "Tshokolo",
                "Mother",
                null);
        assertNull(contact);
    }

}
