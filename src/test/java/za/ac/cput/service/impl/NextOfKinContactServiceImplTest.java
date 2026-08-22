package za.ac.cput.service.impl;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import za.ac.cput.domain.NextOfKinContact;
import za.ac.cput.domain.User;
import za.ac.cput.factory.NextOfKinContactFactory;
import za.ac.cput.repository.IUserRepository;
import za.ac.cput.service.IUserService;

import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

//Lisakhanya Tshokolo 220239215

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NextOfKinContactServiceImplTest {

    @Autowired
    private NextOfKinContactServiceImpl nextOfKinContactServiceImpl;

    @Autowired
    private IUserRepository userRepository;

    private static User user;

    private static NextOfKinContact nextOfKinContact;

    @Test
    @Order(1)
    void create() {
        user = userRepository.save(new User.Builder()
                .setFirstName("Lisakhanya")
                .setLastName("Tshokolo")
                .build()
        );
        assertNotNull(user);
        assertNotNull(user.getUserId());

        nextOfKinContact = NextOfKinContactFactory.createNextOfKinContact("Vivian","Tshokolo","Mother","07338487",user);

        NextOfKinContact created = nextOfKinContactServiceImpl.create(nextOfKinContact);
        assertNotNull(created);
        assertEquals(nextOfKinContact.getFirstName(), created.getFirstName());
        assertEquals(nextOfKinContact.getLastName(), created.getLastName());
        assertEquals(nextOfKinContact.getRelationship(), created.getRelationship());
        assertEquals(nextOfKinContact.getCellphoneNumber(), created.getCellphoneNumber());

        System.out.println("Created: " + created);
    }


    @Test
    @Order(2)
    void read() {

        NextOfKinContact read = nextOfKinContactServiceImpl.read(nextOfKinContact.getNextOfKinContactId());

        assertNotNull(read);
        assertEquals(nextOfKinContact.getNextOfKinContactId(), read.getNextOfKinContactId());

        System.out.println("Read: " + read);
    }


    @Test
    @Order(3)
    void update() {

        NextOfKinContact updated = new NextOfKinContact.Builder()
                        .copy(nextOfKinContact)
                        .setCellphoneNumber("0791234567")
                        .build();

        NextOfKinContact result = nextOfKinContactServiceImpl.update(updated);

        assertNotNull(result);
        assertEquals("0791234567", result.getCellphoneNumber());

        System.out.println("Updated: " + result);
    }


    @Test
    @Order(4)
    void getAll() {

        List<NextOfKinContact> contacts = nextOfKinContactServiceImpl.getAll();

        assertNotNull(contacts);
        assertFalse(contacts.isEmpty());

        System.out.println("All Next Of Kin Contacts: " + contacts);
    }


    @Test
    @Order(5)
    void findByFirstName() {
        NextOfKinContact found = nextOfKinContactServiceImpl.findByFirstName(nextOfKinContact.getFirstName());

        assertNotNull(found);
        assertEquals("Vivian", found.getFirstName());

        System.out.println("Found by first name: " + found);

    }


    @Test
    @Order(6)
    void findByLastName() {
        NextOfKinContact found = nextOfKinContactServiceImpl.findByLastName(nextOfKinContact.getLastName());

        assertNotNull(found);
        assertEquals("Tshokolo", found.getLastName());

        System.out.println("Found by last name: " + found);


    }


    @Test
    @Order(7)
    void findByRelationship() {
        NextOfKinContact found = nextOfKinContactServiceImpl.findByRelationship(nextOfKinContact.getRelationship());

        assertNotNull(found);
        assertEquals("Mother", found.getRelationship());

        System.out.println("Found by relationship: " + found);

    }

    @Test
    @Order(8)
    void findByUser() {
        List<NextOfKinContact> contacts = nextOfKinContactServiceImpl.findByUser(user);

        assertNotNull(contacts);
        assertFalse(contacts.isEmpty());

        System.out.println("Found by user: " + contacts);
    }


    @Test
    @Order(9)
    void delete() {

        boolean deleted = nextOfKinContactServiceImpl.delete(nextOfKinContact.getNextOfKinContactId());

        assertTrue(deleted);

        NextOfKinContact deletedContact = nextOfKinContactServiceImpl.read(nextOfKinContact.getNextOfKinContactId());

        assertNull(deletedContact);

        System.out.println("Next Of Kin Contact deleted successfully");
    }
}