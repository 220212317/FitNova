package za.ac.cput.service.impl;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.Contact;
import za.ac.cput.factory.ContactFactory;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
//Lisakhanya Tshokolo 220239215

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ContactServiceImplTest {

    @Autowired
    private ContactServiceImpl Contactservice;

    private static Contact contact;

    @Test
    @Order(1)
    void create() {
        contact = ContactFactory.createContact("0815659910","0783726156","lisa@gmail.com");
        Contact created = Contactservice.create(contact);

        assertNotNull(created);
        assertEquals(contact.getCellphoneNumber(),
                created.getCellphoneNumber()
        );
        assertEquals(contact.getAlternativeCellphoneNumber(),
                created.getAlternativeCellphoneNumber()
        );

        assertEquals(contact.getEmailAddress() ,
                created.getEmailAddress()
        );

        System.out.println("Ceated: " + created);
    }

    @Test
    @Order(2)
    void read() {
        Contact read = Contactservice.read(contact.getContactId());

        assertNotNull(read);
        assertEquals(contact.getContactId(),
                read.getContactId()
        );
        System.out.println("Read: " + read);
    }

    @Test
    @Order(3)
    void update() {
        Contact updated = new Contact.Builder()
                .copy(contact)
                .setEmailAddress("Avu@gmail.com")
                .build();

        Contact result = Contactservice.update(updated);
        assertNotNull(result);
        assertEquals("Avu@gmail.com", result.getEmailAddress()
        );
        System.out.println("Updated: " + result);
    }

    @Test
    @Order(4)
    void findByCellphoneNumber() {
        Contact foundNumber = Contactservice.findByCellphoneNumber(contact.getCellphoneNumber());

        assertNotNull(foundNumber);
        assertEquals(
                contact.getCellphoneNumber(),
                foundNumber.getCellphoneNumber()
        );

        System.out.println("Found by cellphone number: " + foundNumber);
    }

    @Test
    @Order(5)
    void findByEmailAddress() {
        Contact foundEmail = Contactservice.findByEmailAddress("Avu@gmail.com");
        assertNotNull(foundEmail);

        assertEquals("Avu@gmail.com",
                foundEmail.getEmailAddress()
        );
        System.out.println("Found by email address: " + foundEmail);
    }

    @Test
    @Order(6)
    void getAll() {
        List<Contact> contacts = Contactservice.getAll();
        assertNotNull(contacts);
        assertFalse(contacts.isEmpty());
        System.out.println("Get all: " + contacts);
    }

    @Test
    @Order(7)
    void delete() {
        boolean deleted = Contactservice.delete(contact.getContactId());
        assertTrue(deleted);
        Contact deletedContact = Contactservice.read(contact.getContactId());
        assertNull(deletedContact);
    }
}