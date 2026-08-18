package za.ac.cput.factory;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Contact;

import static org.junit.jupiter.api.Assertions.*;

class ContactFactoryTest {

    @Test
    void createContact() {
        Contact contact = ContactFactory.createContact("07123456789","0819875642","lisa@gmail.com");
        assertNotNull(contact);
        assertNotNull(contact.getContactId());
        assertEquals(contact.getContactId(), contact.toString());
    }
}