package za.ac.cput.entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import za.ac.cput.entity.enums.RoleType;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * 26 March 2026
 * Author: Lisakhanya Tshokolo
 * (220239215)
 */

public class TrainerTest {

    private Trainer trainer;
    private UserAccount account;
    private UserProfile profile;
    private ContactDetails contactDetails;

    @BeforeEach
    void setUp() {

        account = new UserAccount.Builder()
                .accountId("ACC001")
                .email("trainer@gmail.com")
                .password("TrainerPass123")
                .role(RoleType.TRAINER)
                .registrationDate(LocalDate.of(2024, 1, 10))
                .build();

        profile = new UserProfile.Builder()
                .profileId("P001")
                .firstName("Sam")
                .lastName("Ndlela")
                .gender("Male")
                .dateOfBirth(LocalDate.of(1999, 5, 20))
                .build();

        contactDetails = new ContactDetails.Builder()
                .setContactId("C001")
                .setCellphoneNumber("0712345678")
                .setEmergencyContactNumber("0798765432")
                .setEmergencyContactName("Nomawethu Ndlela")
                .build();

        trainer = new Trainer.Builder()
                .setTrainerId("T001")
                .setAccount(account)
                .setProfile(profile)
                .setContactDetails(contactDetails)
                .build();
    }

    @Test
    void testTrainer_NotNull() {
        assertNotNull(trainer);
    }

    @Test
    void testTrainerId() {
        assertEquals("T001", trainer.getTrainerId());
    }

    @Test
    void testEmail() {
        assertEquals("trainer@gmail.com",
                trainer.getAccount().getEmail());
    }

    @Test
    void testRole() {
        assertEquals(RoleType.TRAINER,
                trainer.getAccount().getRole());
    }

    @Test
    void testFirstName() {
        assertEquals("Sam",
                trainer.getProfile().getFirstName());
    }

    @Test
    void testDateOfBirth() {
        assertEquals(LocalDate.of(1999, 5, 20),
                trainer.getProfile().getDateOfBirth());
    }

    @Test
    void testCellphoneNumber() {
        assertEquals("0712345678",
                trainer.getContactDetails().getCellphoneNumber());
    }

    @Test
    void testSetTrainerId() {
        trainer.setTrainerId("T002");
        assertEquals("T002", trainer.getTrainerId());
    }

    @Test
    void testSetProfile() {

        UserProfile newProfile = new UserProfile.Builder()
                .profileId("P002")
                .firstName("Updated")
                .lastName("Name")
                .gender("Male")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .build();

        trainer.setProfile(newProfile);

        assertEquals("Updated",
                trainer.getProfile().getFirstName());
    }

    @Test
    void testSetContactDetails() {

        ContactDetails newContact = new ContactDetails.Builder()
                .setContactId("C002")
                .setCellphoneNumber("076543218")
                .setEmergencyContactNumber("0787654321")
                .setEmergencyContactName("New Contact")
                .build();

        trainer.setContactDetails(newContact);

        assertEquals("076543218",
                trainer.getContactDetails().getCellphoneNumber());
    }

    @Test
    void testToString_NotNull() {
        assertNotNull(trainer.toString());
    }
}
