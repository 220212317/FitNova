package za.ac.cput.service.impl;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.NextOfKinContact;
import za.ac.cput.factory.NextOfKinContactFactory;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

//Lisakhanya Tshokolo 220239215

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NextOfKinContactServiceImplTest {

    @Autowired
    private NextOfKinContactServiceImpl nextOfKinContactServiceImpl;

    private static NextOfKinContact nextOfKinContact =
            NextOfKinContactFactory.createNextOfKinContact(
                    "Vivia",
                    "Tshokolo",
                    "Mother",
                    "0733838487"
            );

    @Test
    @Order(1)
    void create() {

        NextOfKinContact created =
                nextOfKinContactServiceImpl.create(nextOfKinContact);

        assertNotNull(created);

        assertEquals(
                nextOfKinContact.getFirstName(),
                created.getFirstName()
        );

        assertEquals(
                nextOfKinContact.getLastName(),
                created.getLastName()
        );

        assertEquals(
                nextOfKinContact.getRelationship(),
                created.getRelationship()
        );

        assertEquals(
                nextOfKinContact.getCellphoneNumber(),
                created.getCellphoneNumber()
        );

        System.out.println("Created: " + created);
    }


    @Test
    @Order(2)
    void read() {

        NextOfKinContact read =
                nextOfKinContactServiceImpl.read(
                        nextOfKinContact.getNextOfKinContactId()
                );

        assertNotNull(read);

        assertEquals(
                nextOfKinContact.getNextOfKinContactId(),
                read.getNextOfKinContactId()
        );

        System.out.println("Read: " + read);
    }


    @Test
    @Order(3)
    void update() {

        NextOfKinContact updated =
                new NextOfKinContact.Builder()
                        .copy(nextOfKinContact)
                        .setCellphoneNumber("0791234567")
                        .build();

        NextOfKinContact result =
                nextOfKinContactServiceImpl.update(updated);

        assertNotNull(result);

        assertEquals(
                "0791234567",
                result.getCellphoneNumber()
        );

        System.out.println("Updated: " + result);
    }


    @Test
    @Order(4)
    void getAll() {

        List<NextOfKinContact> contacts =
                nextOfKinContactServiceImpl.getAll();

        assertNotNull(contacts);

        assertFalse(contacts.isEmpty());

        System.out.println(
                "All Next Of Kin Contacts: " + contacts
        );
    }


    @Test
    @Order(5)
    void findByfirstName() {

        List<NextOfKinContact> contacts =
                Collections.singletonList(
                        nextOfKinContactServiceImpl.findByfirstName(
                                nextOfKinContact.getFirstName()
                        )
                );

        assertNotNull(contacts);

        assertFalse(contacts.isEmpty());

        assertEquals(
                "Vivia",
                contacts.get(0).getFirstName()
        );

        System.out.println(
                "Found by first name: " + contacts
        );
    }


    @Test
    @Order(6)
    void findBylastName() {

        List<NextOfKinContact> contacts =
                Collections.singletonList(
                        nextOfKinContactServiceImpl.findBylastName(
                                nextOfKinContact.getLastName()
                        )
                );

        assertNotNull(contacts);

        assertFalse(contacts.isEmpty());

        assertEquals(
                "Tshokolo",
                contacts.get(0).getLastName()
        );

        System.out.println(
                "Found by last name: " + contacts
        );
    }


    @Test
    @Order(7)
    void findByrelationship() {

        NextOfKinContact foundRelationship =
                nextOfKinContactServiceImpl.findByrelationship(
                        nextOfKinContact.getRelationship()
                );

        assertNotNull(foundRelationship);

        assertEquals(
                "Mother",
                foundRelationship.getRelationship()
        );

        System.out.println(
                "Found by relationship: " + foundRelationship
        );
    }


    @Test
    @Order(8)
    void delete() {

        boolean deleted =
                nextOfKinContactServiceImpl.delete(
                        nextOfKinContact.getNextOfKinContactId()
                );

        assertTrue(deleted);

        NextOfKinContact deletedContact =
                nextOfKinContactServiceImpl.read(
                        nextOfKinContact.getNextOfKinContactId()
                );

        assertNull(deletedContact);

        System.out.println(
                "Next Of Kin Contact deleted successfully"
        );
    }
}