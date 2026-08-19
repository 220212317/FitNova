package za.ac.cput.factory;
import org.junit.jupiter.api.Test;
import za.ac.cput.domain.Contact;
import static org.junit.jupiter.api.Assertions.*;
//Lisakhanya Tshokolo 220239215

class ContactFactoryTest {

    @Test
    void createContact() {
        Contact contact = ContactFactory.createContact(
                "07123456789",
                "0819875642",
                "lisa@gmail.com");

        assertNotNull(contact);
        assertNotNull(contact.getContactId());
        assertEquals("07123456789", contact.getCellphoneNumber());
        assertEquals("0819875642", contact.getAlternativeCellphoneNumber());
        assertEquals("lisa@gmail.com",contact.getEmailAddress());
    }

    @Test
    void createContactWithEmptyCellphoneNumber() {
        Contact contact = ContactFactory.createContact( "",
                "0711234567",
                "lisakhanya@gmail.com" );

        assertNull(contact);
    }


    @Test
    void createContactWithNullCellphoneNumber() {
     Contact contact = ContactFactory.createContact( null,
            "0711234567",
            "lisakhanya@gmail.com" );

        assertNull(contact);
    }

    @Test
    void createContactWithEmptyEmail(){
        Contact contact = ContactFactory.createContact( "0821234567",
                "0711234567",
                "" );

        assertNull(contact);
    }

    @Test
    void createContactWithNullEmail(){
        Contact contact = ContactFactory.createContact( "0821234567",
                "0711234567",
                null );

        assertNull(contact);
    }

    @Test
    void createContactWithInvalidEmail(){
        Contact contact = ContactFactory.createContact( "0821234567",
                "0711234567",
                "invalid-email" );

        assertNull(contact);
    }

    @Test
    void createContactWithDifferentValidEmail(){
        Contact contact = ContactFactory.createContact( "0839876543",
                "0729876543",
                "test.user@gmail.com" );
        assertNotNull(contact);
        assertNotNull("test.user@gmail.com" , contact.getEmailAddress());
    }
}