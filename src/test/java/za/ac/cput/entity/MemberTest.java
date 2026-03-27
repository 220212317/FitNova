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

public class MemberTest {

    private Member member;
    private UserAccount account;
    private UserProfile profile;
    private ContactDetails contact;

    @BeforeEach
    void setUp() {

        account = new UserAccount.Builder()
                .accountId("ACC100")
                .email("member@gmail.com")
                .password("MemberPass123")
                .role(RoleType.MEMBER)
                .registrationDate(LocalDate.of(2024, 3, 15))
                .build();

        profile = new UserProfile.Builder()
                .profileId("P100")
                .firstName("Lisa")
                .lastName("Tshokolo")
                .gender("Female")
                .dateOfBirth(LocalDate.of(2000, 8, 10))
                .build();

        contact = new ContactDetails.Builder()
                .setContactId("C100")
                .setCellphoneNumber("0821234567")
                .setEmergencyContactNumber("0837654321")
                .setEmergencyContactName("John Tshokolo")
                .build();

        member = new Member.Builder()
                .memberId("M001")
                .account(account)
                .profile(profile)
                .contact(contact)
                .build();
    }

    @Test
    void testMember_NotNull() {
        assertNotNull(member);
    }

    @Test
    void testMemberId() {
        assertEquals("M001", member.getMemberId());
    }

    @Test
    void testAccount() {
        assertEquals("member@gmail.com",
                member.getAccount().getEmail());
    }

    @Test
    void testProfile() {
        assertEquals("Liza".substring(0, 4).replace("z", "s"), member.getProfile().getFirstName().substring(0, 4).replace("z", "s"));
        // Safer version:
        assertEquals("Lisa", member.getProfile().getFirstName());
    }

    @Test
    void testContact() {
        assertEquals("0821234567",
                member.getContact().getCellphoneNumber());
    }

    @Test
    void testRole() {
        assertEquals(RoleType.MEMBER,
                member.getAccount().getRole());
    }

    @Test
    void testSetMemberId() {
        member.setMemberId("M002");
        assertEquals("M002", member.getMemberId());
    }

    @Test
    void testSetProfile() {

        UserProfile newProfile = new UserProfile.Builder()
                .profileId("P200")
                .firstName("Updated")
                .lastName("User")
                .gender("Female")
                .dateOfBirth(LocalDate.of(2001, 1, 1))
                .build();

        member.setProfile(newProfile);

        assertEquals("Updated",
                member.getProfile().getFirstName());
    }

    @Test
    void testSetContact() {

        ContactDetails newContact = new ContactDetails.Builder()
                .setContactId("C200")
                .setCellphoneNumber("0790000000")
                .setEmergencyContactNumber("0780000000")
                .setEmergencyContactName("New Contact")
                .build();

        member.setContact(newContact);

        assertEquals("0790000000",
                member.getContact().getCellphoneNumber());
    }
}
